package top.fols.box.util.encode;

import top.fols.atri.io.CharsWriters;
import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.Help;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;

public class URLEncoders {

    // encodeURIComponent
    private static final char[] HEX_CHAR_UPPERCASE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
        'B', 'C', 'D', 'E', 'F' };
    private static final Charset DEFAULT_CHARSET = Finals.Charsets.UTF_8;


    /*
     * @param charset nullable
     */
    public static String encodeURIComponent(String string, Charset charset) {
        if (null == string) {
            throw new NullPointerException("string");
        }
        if (null == charset) {
            charset = DEFAULT_CHARSET;
        }
        byte[] bytes = string.getBytes(charset);
        char[] result = new char[bytes.length * 2 + bytes.length * 1];
        int i2 = 0;
        for (int i = 0; i < bytes.length; i++) {
            int a;
            byte b = bytes[i];
            if (b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }
            result[i2++] = '%';
            result[i2++] = HEX_CHAR_UPPERCASE[a / 16];
            result[i2++] = HEX_CHAR_UPPERCASE[a % 16];
        }
        bytes = null;
        return new String(result);
    }

    /*
     * @param charset nullable
     */
    public static String encodeURIComponent(String string, String charset) throws UnsupportedCharsetException {
        Charset charset0 = null == charset ? DEFAULT_CHARSET : Charset.forName(charset);
        return encodeURIComponent(string, charset0);
    }












    // encode

    static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');
    static {
        /*
         * The list of characters that are not encoded has been determined as follows:
         *
         * RFC 2396 states: ----- Data characters that are allowed in a URI but do not
         * have a reserved purpose are called unreserved. These include upper and lower
         * case letters, decimal digits, and a limited set of punctuation marks and
         * symbols.
         *
         * unreserved = alphanum | mark
         *
         * mark = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
         *
         * Unreserved characters can be escaped without changing the semantics of the
         * URI, but this should not be done unless the URI is being used in a context
         * that does not allow the unescaped character to appear. -----
         *
         * It appears that both Netscape and Internet Explorer escape all special
         * characters from this list with the exception of "-", "_", ".", "*". While it
         * is not clear why they are escaping the other characters, perhaps it is safest
         * to assume that there might be contexts in which the others are unsafe if not
         * escaped. Therefore, we will use the same list. It is also noteworthy that
         * this is consistent with O'Reilly's "HTML: The Definitive Guide" (page 164).
         *
         * As a last note, Intenet Explorer does not encode the "@" character which is
         * clearly not unreserved according to the RFC. We are being consistent with the
         * RFC in this matter, as is Netscape.
         *
         */
        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(' '); /*
         * encoding a space to a + is done in the encode() method
         */
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
    }

    /*
     * @param charset nullable
     */
    public static String encode(String string, String charset) throws UnsupportedCharsetException {
        Charset charset0 = null == charset ? DEFAULT_CHARSET : Charset.forName(charset);
        return encode(string, charset0);
    }
    public static String encode(String string, Charset charset) throws NullPointerException {
        if (null == string) {
            throw new NullPointerException("string");
        }
        if (null == charset) {
            charset = DEFAULT_CHARSET;
        }

        boolean needToChange = false;
        StringBuilder out = new StringBuilder(string.length());
        CharsWriters charArrayWriter = new CharsWriters();

        for (int i = 0; i < string.length();) {
            int c = (int) string.charAt(i);
            // System.out.println("Examining character: " + c);
            if (dontNeedEncoding.get(c)) {
                if (c == ' ') {
                    c = '+';
                    needToChange = true;
                }
                // System.out.println("Storing: " + c);
                out.append((char) c);
                i++;
            } else {
                // convert to external encoding before hex conversion
                do {
                    charArrayWriter.write(c);
                    /*
                     * If this character represents the start of a Unicode surrogate pair, then pass
                     * in two characters. It's not clear what should be done if a bytes reserved in
                     * the surrogate pairs range occurs outside of a legal surrogate pair. For now,
                     * just treat it as if it were any other character.
                     */
                    if (c >= 0xD800 && c <= 0xDBFF) {
                        /*
                         * System.out.println(Integer.toHexString(c) + " is high surrogate");
                         */
                        if ((i + 1) < string.length()) {
                            int d = (int) string.charAt(i + 1);
                            /*
                             * System.out.println("\tExamining " + Integer.toHexString(d));
                             */
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                /*
                                 * System.out.println("\t" + Integer.toHexString(d) + " is low surrogate");
                                 */
                                charArrayWriter.write(d);
                                i++;
                            }
                        }
                    }
                    i++;
                } while (i < string.length() && !dontNeedEncoding.get((c = (int) string.charAt(i))));

                charArrayWriter.flush();
                String str = new String(charArrayWriter.toCharArray());
                byte[] ba = str.getBytes(charset);
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
                    // converting to use uppercase letter as part of
                    // the hex tip if ch is a letter.
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                    ch = Character.forDigit(ba[j] & 0xF, 16);
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                }
                charArrayWriter.reset();
                needToChange = true;
            }
        }
        String result = (needToChange ? out.toString() : string);
        charArrayWriter.close();
        return result;
    }



    /*
     * @param charset nullable
     */
    public static String decode(String string, String charset) throws UnsupportedCharsetException {
        Charset charset0 = null == charset ? DEFAULT_CHARSET : Charset.forName(charset);
        return decode(string, charset0);
    }

    // decode
    public static String decode(String string, Charset charset) {
        if (null == string) {
            throw new NullPointerException("string");
        }
        if (null == charset) {
            charset = DEFAULT_CHARSET;
        }

        boolean needToChange = false;
        int numChars = string.length();
        CharsWriters sb = new CharsWriters(numChars > 500 ? numChars / 2 : numChars);
        int i = 0;

        char c;
        byte[] bytes = null;
        while (i < numChars) {
            c = string.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    i++;
                    needToChange = true;
                    break;
                case '%':
                    /*
                     * Starting with this instance of %, process all consecutive substrings of the
                     * form %xy. Each substring %xy will yield a byte. Convert all consecutive bytes
                     * obtained this way to whatever character(s) they represent in the provided
                     * encoding.
                     */
                    try {
                        // (numChars-i)/3 is an upper bound for the number
                        // of remaining bytes
                        if (null == bytes)
                            bytes = new byte[(numChars - i) / 3];
                        int pos = 0;

                        while (((i + 2) < numChars) && (c == '%')) {
                            int v = Integer.parseInt(string.substring(i + 1, i + 3), 16);
                            if (v < 0)
                                throw new IllegalArgumentException(
                                    "URLDecoder: Illegal hex characters in escape (%) pattern - negative tip");
                            bytes[pos++] = (byte) v;
                            i += 3;
                            if (i < numChars)
                                c = string.charAt(i);
                        }
                        // A trailing, incomplete byte encoding such as
                        // "%x" will cause an exception to be thrown
                        if ((i < numChars) && (c == '%'))
                            throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
                        sb.append(new String(bytes, 0, pos, charset));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                            "URLDecoder: Illegal hex characters in escape (%) pattern - " + e.getMessage());
                    }
                    needToChange = true;
                    break;
                default:
                    sb.append(c);
                    i++;
                    break;
            }
        }
        String result = (needToChange ? sb.toString() : string);
        sb.close();
        return result;
    }











    /*
     * [\u4E00-\u9FA5] Chinese %([0-9a-fA-F]{2}) matcher %XX
     *
     * (%[0-9a-fA-F]{2})+
     */
    /*
     * will % replaced with %25 (decode after %)
     */
    public static String formatEncodedString(String string) {
        return string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
    }




    // decodeMatch

    /*
     * search [%XX, +] index
     */
    @SuppressWarnings("ManualMinMaxCalculation")
    public static class URLCodeMatcher {
        private final String text;
        private final int length;
        private int start = -1;
        private int end = -1;

        public URLCodeMatcher(String text) {
            this.text = null == text ? "" : text;
            this.length = this.text.length();
        }

        public boolean find() {
            int nextIndex = this.end < 0 ? 0 : this.end;
            int uclen = 0;
            for (int i = nextIndex; i < this.length; i++) {
                if ((uclen = nextURLCodeLength(i)) != 0) {
                    nextIndex = i;
                    break;
                }
            }
            if (uclen == 0) {
                this.end = -1;
                return false;
            }
            this.start = nextIndex;
            while (true) {
                int length;
                if (nextIndex >= this.length || (length = nextURLCodeLength(nextIndex)) == 0) {
                    break;
                }
                nextIndex += length;
            }
            this.end = nextIndex;
            // System.out.println("start=" + this.start + ", end=" + end);
            return true;
        }

        /**
         * 0-9 48>=x<=57
         * a-f 97>=x<=102
         * a-f 65>=x<=70
         */
        @Help("%XX or +")
        private int nextURLCodeLength(int start) {
            if (text.charAt(start) == '+') {
                return 1;
            }
            E:if (text.charAt(start) == '%') {
                if (start + 2 < length) {
                    char ch;
                    ch = text.charAt(start + 1);
                    if (!((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F'))) {
                        break E;
                    }
                    ch = text.charAt(start + 2);
                    if (!((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F'))) {
                        break E;
                    }
                    return 3;
                }
            }
            return 0;
        }

        public int start() {
            return start;
        }

        public int end() {
            return end;
        }

        public String group() {
            return text.substring(start, end);
        }
    }

    /*
     * Only decode the coded part
     *
     * @param charset nullable
     */
    public static String decodeMatch(String string, String charset) {
        Charset charset0 = null == charset ? DEFAULT_CHARSET : Charset.forName(charset);
        return decodeMatch(string, charset0);
    }
    public static String decodeMatch(String string, Charset charset) {
        if (null == string) {
            throw new NullPointerException("string");
        }
        if (null == charset) {
            charset = DEFAULT_CHARSET;
        }

        // "(%[0-9a-fA-F]{2})+"
        URLCodeMatcher indexOf = new URLCodeMatcher(string);
        StringBuilder buf = new StringBuilder();
        int start = 0, end = -1;
        if (!indexOf.find()) {
            return string;
        }

        do {
            end = indexOf.start();
            buf.append(string, start, end).append(decode(indexOf.group(), charset));
            start = indexOf.end();
        } while (indexOf.find());

        buf.append(string.substring(start, string.length()));
        return buf.toString();
    }


    public static boolean hasURLEncode(String string) {
        return null != string && new URLCodeMatcher(string).find();
    }
}


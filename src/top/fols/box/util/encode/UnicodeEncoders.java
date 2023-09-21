package top.fols.box.util.encode;

import top.fols.atri.io.CharsWriters;

import java.io.Serializable;

@SuppressWarnings("SameParameterValue")
public class UnicodeEncoders {
    /**
     * Will not decode if encountering unicode characters that cannot be decoded
     */
    public static String decode(String unicodeString) {
        if (null == unicodeString) {
            return null;
        }
        CharsWriters out = new CharsWriters();
        int sz = unicodeString.length();
        CharsWriters unicode = new CharsWriters(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        for (int i = 0; i < sz; i++) {
            char ch = unicodeString.charAt(i);
            if (inUnicode) {
                unicode.append(ch);
                if (unicode.size() == 4) {
                    int value = parseUnicodeInt(unicode.toString(), 16);
                    if (value == -1) {
                        out.write('\\');
                        out.write('u');
                        out.write(unicode.buffer(), 0, unicode.size());
                        inUnicode = false;
                        hadSlash = false;
                        continue;
                    }
                    out.write((char) value);
                    unicode.buffer_length(0);
                    inUnicode = false;
                    hadSlash = false;
                }
                continue;
            }
            if (hadSlash) {
                // handle an escaped tip
                hadSlash = false;
                switch (ch) {
                    case 'u': {
                            // uh-oh, we're in unicode country....
                            inUnicode = true;
                            break;
                        }
                    default:
                        out.write('\\');
                        out.write(ch);
                        break;
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            out.write(ch);
        }
        if (hadSlash) {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            out.write('\\');
        }
        if (inUnicode) {
            out.write('\\');
            out.write('u');
            out.write(unicode.buffer(), 0, unicode.size());
        }
        return out.toString();
    }

    /**
     * decode method use
     */
    private static int parseUnicodeInt(String s, int radix) throws NumberFormatException {
        /*
         * WARNING: This method may be invoked early during VM initialization before
         * IntegerCache is initialized. Care must be taken to not use the valueOf
         * method.
         */
        if (null == s) {
            throw new NumberFormatException("null");
        }
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
        }
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
        }

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+')
                    return -1;
                if (len == 1) // Cannot have lone "+" or "-"
                    return -1;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return -1;
                }
                if (result < multmin) {
                    return -1;
                }
                result *= radix;
                if (result < limit + digit) {
                    return -1;
                }
                result -= digit;
            }
        } else {
            return -1;
        }
        return negative ? result : -result;
    }

    /**
     * encode
     */
    public static String encode(String string) {
        if (null == string) {
            return null;
        }
        CharsWriters writer = new CharsWriters();
        int count = string.length();
        for (int i = 0; i < count; i++) {
            char c = string.charAt(i);
            writer.append('\\');
            writer.append('u');
            writeHexString(writer, c);
        }
        String content = writer.toString();
        writer.release();
        return content;
    }

    private static void writeHexString(CharsWriters writer, int i) {
        writeUnsignedString(writer, i, 4);
    }

    private static void writeUnsignedString(CharsWriters writer, int i, int shift) {
        char[] buf = new char[32];
        int charPos = 32;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[--charPos] = digits[i & mask];
            i >>>= shift;
        } while (i != 0);
        writer.write(buf, charPos, (32 - charPos));
    }

    private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
        'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
}

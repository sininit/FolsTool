package top.fols.box.util;
import java.io.IOException;
import top.fols.box.io.base.XCharArrayWriter;
public class XUnicodeSimple {
	
	// ([^\\x00-\\xff]) 双字节字符
	public static String encode(String str) throws IOException {
		int sz = str.length();
		XCharArrayWriter out = new XCharArrayWriter();
		for (int i = 0;i < sz;i++) {
			char ch = str.charAt(i);
			if (ch > 0xfff) {
				out.append("\\u").append(XEscape.hex(ch));
			} else if (ch > 0xff) {
				out.append("\\u0").append(XEscape.hex(ch));
			} else if (ch > 0x7f) {
				out.append("\\u00").append(XEscape.hex(ch));
			} else if (ch < 32) {
				if (ch > 0xf) {
					out.append("\\u00").append(XEscape.hex(ch));// (ch > 0xf = ch > 15), hex = 0123456789abcdef,ch = 10?a ch = 15?f, ch = 16?10
				} else {
					out.append("\\u000").append(XEscape.hex(ch));// (ch > 0xf = ch > 15), hex = 0123456789abcdef,ch = 10?a ch = 15?f, ch = 16?10
				}
			} else {
				out.write(ch);
			}
		}
		String result = new String(out.toCharArray()); out.releaseBuffer();
		return result;
	}
	public static String decode(String str) throws IOException {
		XCharArrayWriter out = new XCharArrayWriter();
		int sz = str.length();
		XCharArrayWriter unicode = new XCharArrayWriter(4);
        boolean hadSlash = false, inUnicode = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (inUnicode) {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.size() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode character
                    try {
						int value = Integer.parseInt(unicode.toString(), 16);
                        out.write((char) value);
                        unicode.setBuffSize(0);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException nfe) {
                        throw new IOException("Unable to parse unicode value: " + unicode, nfe);
                    }
                }
                continue;
            }
            if (hadSlash) {
                // handle an escaped value
                hadSlash = false;
				if (ch == 'u') inUnicode = true;
				else out.write(ch);
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
		String result = new String(out.toCharArray()); out.releaseBuffer();
		return result;
	}
}

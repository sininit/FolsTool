package top.fols.box.net;


import top.fols.box.lang.XString;
import top.fols.box.net.XURLConnectionMessageHeader;

import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XCookie {
    public static final String HEADER_KEY_COOKIE = "Cookie";
    public static final String HEADER_KEY_SET_COOKIE = "Set-Cookie";

    private static final String PROJECT_SEPARATOR = ";";
    private static final String ASSIGNMENT_SYMBOL = "=";

    public static Map<String, String> getHeaderCookieToMap(URLConnection urlc) {
        Map<String, String> val = new LinkedHashMap<>();
        XURLConnectionMessageHeader hm = new XURLConnectionMessageHeader(urlc.getHeaderFields());
        val.putAll(XCookie.cookieValueToMap(hm.getInnerValueList(XCookie.HEADER_KEY_COOKIE)));
        val.putAll(XCookie.cookieValueToMap(hm.getInnerValueList(XCookie.HEADER_KEY_SET_COOKIE)));
        return val;
    }



    // qrsig=1; Path=/; Domain=google.com;
    public static Map<String, String> cookieValueToMap(String cookie) {
        Map<String, String> val = new LinkedHashMap<>();
        if (null == cookie) {
            return val;
        }
        List<String> s = XString.split(
                (cookie.endsWith(XCookie.PROJECT_SEPARATOR)) ? cookie : (cookie + XCookie.PROJECT_SEPARATOR),
                XCookie.PROJECT_SEPARATOR);
        for (String str2 : s) {
            str2 = str2.trim();

            int valTag = str2.indexOf(XCookie.ASSIGNMENT_SYMBOL);
            if (valTag >= 0) {
                String k = str2.substring(0, valTag);
                String v = str2.substring(valTag + XCookie.ASSIGNMENT_SYMBOL.length(), str2.length());
                val.put(k, v);
            } else {
                String k = str2;
                val.put(k, null);
            }
        }
        return val;
    }


    public static Map<String, String> cookieValueToMap(List<String> setCookie) {
        Map<String, String> val = new LinkedHashMap<>();
        if (null != setCookie) {
            for (String str : setCookie) {
                val.putAll(XCookie.cookieValueToMap(str));
            }
        }
        return val;
    }
    // ----
    public static String cookieMapToString(Map<String, String> val) {
        StringBuilder sb = new StringBuilder();
        for (String key : val.keySet()) {
            String value = val.get(key);
            sb.append(key);
            if (null != value) {
                sb.append(XCookie.ASSIGNMENT_SYMBOL).append(value);
            }
            sb.append(XCookie.PROJECT_SEPARATOR).append(" ");
        }
        return sb.toString().trim();
    }

}

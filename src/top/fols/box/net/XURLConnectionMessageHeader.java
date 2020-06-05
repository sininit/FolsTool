package top.fols.box.net;

import java.io.Serializable;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.base.XStringReader;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArrays;
import top.fols.box.util.XObjects;
import top.fols.box.util.interfaces.XInterfaceGetOriginMap;

public class XURLConnectionMessageHeader implements Serializable, XInterfaceGetOriginMap {
    private static final long serialVersionUID = 1L;


    /**
     * HTTP 的规范中如此描述（3.2.4. Field Parsing）：
     * ISO-8859-1 编码是单字节编码，向下兼容ASCII，其编码范围是0x00-0xFF，0x00-0x7F之间完全和ASCII一致，0x80-0x9F之间是控制字符，0xA0-0xFF之间是文字符号。
     * 即 HTTP 头部的事实字符集乃是 US-ASCII 一个子集，所以HTTP 规范允许的字符集是 ISO-8859-1。
     * 如果想在服务器支持HTTP头部有汉字，那么只好把可能包含中文的头部值进行 url编码，然后整体进行ISO-8859-1编码。
     */
    public static final Charset HTTP_REQUEST_HEADER_CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");





    protected static List<String> getHeaderFieldValue(URLConnection urlc, String key) {
        Map<String, List<String>> map = urlc.getHeaderFields();
        List<String> values = map.get(key);
        if (null == values) {
            String newKey = findEqualsKey(map.keySet(), key, true, true);
            return map.get(newKey);
        }
        return values;
    }
    








    public static final String LINE_SEPARATOR = new String(XStaticFixedValue.Chars_NextLineRN());
    public static final char ASSIGNMENT_SYMBOL_CHAR = ':';

    private Map<String, List<String>> uaValues = new LinkedHashMap<String, List<String>>();



    private void set0(String k, String v) {
        List<String> newValues = new ArrayList<String>();
        if (null != v) {
            newValues.add(v);
        }
        this.set0(k, newValues);
    }


    private void set0(String k, List<String> v) {
        List<String> newValues = null == v ?v = new ArrayList<String>(): new ArrayList<>(v);
        this.uaValues.put(k, newValues);
    }

    private void add0(String k, String v) {
        List<String> newValues = this.uaValues.get(k);
        if (null == newValues) {
            this.uaValues.put(k, newValues = new ArrayList<String>());
        }
        newValues.add(v);
    }

    /*
     * deal multi line able ^ for The beginning of a line
     * 
     * ^key: value\n ^key2: value2\n ^...
     */
    private static final char[][] LINE_SEPARATOR_ALL = new char[][]{XStaticFixedValue.Chars_NextLineRN(), XStaticFixedValue.Chars_NextLineR(), XStaticFixedValue.Chars_NextLineN()};
    private static void dealMultiLine0(String ua, boolean putValue, XURLConnectionMessageHeader m) {
        XStringReader rowStreanm = new XStringReader(ua);
        char lines[];
        char splitchar = XURLConnectionMessageHeader.ASSIGNMENT_SYMBOL_CHAR;

        while (null != (lines = rowStreanm.readLine(LINE_SEPARATOR_ALL, false))) {
            if (lines.length == 0) {
                continue;
            }
            int splistCharindex = XArrays.indexOf(lines, splitchar, 0, lines.length);
            String ki = null, vi = null;
            if (splistCharindex != -1) {
                ki = new String(lines, 0, splistCharindex);
                splistCharindex++;
                vi = new String(lines, splistCharindex, lines.length - splistCharindex).trim();
            } else {
                vi = new String(lines).trim();
            }
            if (putValue) {
                m.set0(ki, vi);
            } else {
                m.add(ki, vi);
            }
            lines = null;
        }
        rowStreanm.close();
    }




    public XURLConnectionMessageHeader() {
        this((String) null);
    }

    public XURLConnectionMessageHeader(String ua) {
        if (null != ua) {
            this.addAll(ua);
        }
    }
    public XURLConnectionMessageHeader(String... ua) {
        if (null != ua) {
            this.addAll(ua);
        }
    }

    public XURLConnectionMessageHeader(Map<String, List<String>> ua) {
        if (null != ua) {
            this.addAll(ua);
        }
    }
    
    
    
    
    
    
    
    
    @XAnnotations("set")
    public XURLConnectionMessageHeader put(String k, String v) {
        this.set0(k, v);
        return this;
    }

    @XAnnotations("set")
    public XURLConnectionMessageHeader put(String k, List<String> v) {
        this.set0(k, v);
        return this;
    }

    @XAnnotations("deal multi line able")
    public XURLConnectionMessageHeader putAll(String multiLineContent) {
        XURLConnectionMessageHeader.dealMultiLine0(multiLineContent, true, this);
        return this;
    }

    @XAnnotations("deal multi line able")
    public XURLConnectionMessageHeader putAll(String... multiLineContent) {
        StringBuilder buf = new StringBuilder();
        for (String s : multiLineContent) {
            buf.append(s).append(XURLConnectionMessageHeader.LINE_SEPARATOR);
        }
        this.putAll(buf.toString());
        buf = null;
        return this;
    }

    public XURLConnectionMessageHeader putAll(XURLConnectionMessageHeader ua) {
        if (null == ua) {
            return this;
        }
        return this.putAll(ua.uaValues);
    }

    public XURLConnectionMessageHeader putAll(Map<String, List<String>> ua) {
        if (null == ua) {
            return this;
        }
        for (String key : ua.keySet()) {
            String k = key;
            List<String> values = ua.get(k);
            this.uaValues.put(k, null == values ? null : new ArrayList<String>(values));
        }
        return this;
    }




    @XAnnotations("add")
    public XURLConnectionMessageHeader add(String k, String v) {
        this.add0(k, v);
        return this;
    }

    @XAnnotations("deal multi line able")
    public XURLConnectionMessageHeader addAll(String Content) {
        XURLConnectionMessageHeader.dealMultiLine0(Content, false, this);
        return this;
    }

    @XAnnotations("deal multi line able")
    public XURLConnectionMessageHeader addAll(String... Content) {
        StringBuilder buf = new StringBuilder();
        for (String s : Content) {
            buf.append(s).append(XURLConnectionMessageHeader.LINE_SEPARATOR);
        }
        this.addAll(buf.toString());
        return this;
    }

    public XURLConnectionMessageHeader addAll(XURLConnectionMessageHeader ua) {
        if (null == ua) {
            return this;
        }
        return this.addAll(ua.uaValues);
    }

    public XURLConnectionMessageHeader addAll(Map<String, List<String>> ua) {
        if (null == ua) {
            return this;
        }
        for (String key : ua.keySet()) {
            String k = key;
            List<String> newValues = ua.get(k);
            if (null != newValues) {
                List<String> originList = this.uaValues.get(k);
                if (null == originList) {
                    originList = new ArrayList<String>();
                }
                originList.addAll(newValues);
                this.uaValues.put(k, originList);
            }
        }
        return this;
    }



    /*
     * 寻找忽略大小写并且去除首尾空相等的key
     */
    @XAnnotations("ignore case, find key")
    public String findEqualsKey(String key) {
        return this.findEqualsKey(key, true, true);
    }
    public String findEqualsKey(String key, boolean ignoreCase, boolean trim) {
        return XURLConnectionMessageHeader.findEqualsKey(this.uaValues.keySet(), key, ignoreCase, trim);
    }
    private static String findEqualsKey(Set<String> keys, String key, boolean ignoreCase, boolean trim) {
        if (null == key) {
            return null;
        }
        if (keys.contains(key)
            || (!ignoreCase && !trim)
            ) {
            return key;
        }
        if (trim) { key = key.trim(); }
        if (ignoreCase) { key = key.toLowerCase(Locale.ENGLISH); }
        String sKey = key;
        for (String ki : keys) {
            if (null != ki) {
                if (trim) { ki = ki.trim(); }
                if (ignoreCase) { ki = ki.toLowerCase(Locale.ENGLISH); }
                if (ki.equals(sKey)) {
                    return ki;
                }
            }
        }
        return key;
    }



    public String get(String k) {
        List<String> newValues = this.uaValues.get(k);
        return null == newValues || newValues.size() == 0 ? null : newValues.get(0);
    }

    public List<String> getAll(String k) {
        List<String> newValues = this.uaValues.get(k);
        return null == newValues ? null : newValues;
    }

    public XURLConnectionMessageHeader setAll(String k, List<String> list) {
        this.uaValues.put(k, list);
        return this;
    }

    @Override
    public Map<String, List<String>> getMap() {
        return this.uaValues;
    }


    public int size(String key) {
        List<String> list = this.uaValues.get(key);
        return null == list ? 0 : list.size();
    }

    public int size() {
        return this.uaValues.size();
    }

    public Set<String> keySet() {
        return this.uaValues.keySet();
    }

    public XURLConnectionMessageHeader reset() {
        this.uaValues.clear();
        return this;
    }

    public XURLConnectionMessageHeader remove(String key) {
        this.uaValues.remove(key);
        return this;
    }






    @Override
    public String toString() {
        // TODO: Implement this method
        StringBuilder strbuf = new StringBuilder();
        for (String key : this.uaValues.keySet()) {
            List<String> values = this.getAll(key);
            if (null == values || values.size() == 0) {
                strbuf
                    .append(key)
                    .append(XURLConnectionMessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                    .append(' ')
                    .append(XURLConnectionMessageHeader.LINE_SEPARATOR);
            } else {
                for (String v : values) {
                    strbuf
                        .append(key)
                        .append(XURLConnectionMessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                        .append(' ')
                        .append(v)
                        .append(XURLConnectionMessageHeader.LINE_SEPARATOR);
                }
            }
        }
        return strbuf.toString();
    }
    /**
     * read to null line stop
     */
    public String toHttpHeaderString(String requestOrReturnLine) {
        StringBuilder strbuf = new StringBuilder();
        if (null != requestOrReturnLine && requestOrReturnLine.length() > 0) {
            strbuf
                .append(requestOrReturnLine)
                .append(XURLConnectionMessageHeader.LINE_SEPARATOR);
        }
        if (this.size() != 0) {
            for (String key : this.uaValues.keySet()) {
                List<String> values = this.getAll(key);
                if (null == values || values.size() == 0) {
                    strbuf
                        .append(key)
                        .append(XURLConnectionMessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                        .append(' ')
                        .append(XURLConnectionMessageHeader.LINE_SEPARATOR);
                } else {
                    for (String v : values) {
                        strbuf
                            .append(key)
                            .append(XURLConnectionMessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                            .append(' ')
                            .append(v)
                            .append(XURLConnectionMessageHeader.LINE_SEPARATOR);
                    }
                }
            }
        }
        strbuf.append(XURLConnectionMessageHeader.LINE_SEPARATOR);
        String result = strbuf.toString(); strbuf = null;
        return result;
    }




    public boolean containsKey(String key) {
        return this.uaValues.containsKey(key);
    }




    /*
     * 不会设置key为空的字段
     */
    @XAnnotations("the empty key won't be set")
    public XURLConnectionMessageHeader setToURLConnection(URLConnection con) {
        for (String k : this.uaValues.keySet()) {
            List<String> vs = this.uaValues.get(k);
            if (XObjects.isEmpty(k)) {
                continue;
            }
            if (null == vs || vs.size() == 0) {
                con.setRequestProperty(k, "");
                continue;
            } else {
                int i = 0;
                for (String vi : vs) {
                    if (i == 0) {
                        con.setRequestProperty(k, vi);
                    } else {
                        con.addRequestProperty(k, vi);
                    }
                    i++;
                }
            }
        }
        return this;
    }

    /*
     * 不会添加key为空的字段
     */
    @XAnnotations("the empty key won't be add")
    public XURLConnectionMessageHeader addToURLConnection(URLConnection con) {
        for (String k : this.uaValues.keySet()) {
            List<String> vs = this.uaValues.get(k);
            if (XObjects.isEmpty(k)) {
                continue;
            }
            if (null == vs) {
                con.addRequestProperty(k, "");
            } else {
                for (String vi : vs) {
                    con.addRequestProperty(k, vi);
                }
            }
        }
        return this;
    }

}



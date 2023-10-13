package top.fols.atri.net;

import java.io.Serializable;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

import top.fols.atri.assist.util.IgnoreCaseLinkedHashMap;
import top.fols.atri.interfaces.annotations.Help;
import top.fols.atri.io.StringReaders;
import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.interfaces.IInnerMap;
import top.fols.box.lang.Arrayy;
import top.fols.box.net.header.CookieText;

/**
 * HTTP/1.1和HTTP/2报头都是不区分大小写的。
 * 根据RFC 7230(http/1.1)： 每个标头字段由一个不区分大小写的字段名和一个冒号、 可选的前导空格、字段值和可选的尾随空格组成。
 * 还有，RFC 7540(http/2)：  就像在HTTP/1.x中一样，头字段名是ASCII的字符串以不区分大小写的方式进行比较的字符。
 *
 */

@SuppressWarnings({"UnusedReturnValue", "CharsetObjectCanBeUsed"})
public class MessageHeader implements Serializable, IInnerMap<String, List<String>> {
    private static final long serialVersionUID = 1L;


    /**
     * HTTP 的规范中如此描述（3.2.4. Field Parsing）：
     * ISO-8859-1 编码是单字节编码，向下兼容ASCII，其编码范围是0x00-0xFF，0x00-0x7F之间完全和ASCII一致，0x80-0x9F之间是控制字符，0xA0-0xFF之间是文字符号。
     * 即 HTTP 头部的事实字符集乃是 US-ASCII 一个子集，所以HTTP 规范允许的字符集是 ISO-8859-1。
     * 如果想在服务器支持HTTP头部有汉字，那么只好把可能包含中文的头部值进行 url编码，然后整体进行ISO-8859-1编码。
     */
    public 	static final Charset 		HTTP_MESSAGE_HEADER_CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");

    public 	static final String 		LINE_SEPARATOR = new String(Finals.LineSeparator.getCharsLineSeparatorRN());
    public 	static final char 			ASSIGNMENT_SYMBOL_CHAR = ':';



    public static final String REQUEST_HEADER_COOKIE           = "Cookie";
    public static final String REQUEST_HEADER_HOST             = "Host";
    public static final String REQUEST_HEADER_RANGE            = "Range";
    public static final String REQUEST_HEADER_CONTENT_TYPE     = "Content-Type";

    public static final String REQUEST_HEADER_ACCEPT_ENCODING  = "Accept-Encoding";
    public static final String REQUEST_HEADER_VALUE_ACCEPT_ENCODING_IDENTITY            = "identity";

    public static final String RESPONSE_HEADER_CONTENT_RANGE           = "Content-Range";
    public static final String RESPONSE_HEADER_ACCEPT_RANGES           = "Accept-Ranges";
    public static final String RESPONSE_HEADER_CONTENT_LENGTH          = "Content-Length";
    public static final String RESPONSE_HEADER_CONTENT_TYPE            = "Content-Type";
    public static final String RESPONSE_HEADER_CONTENT_DISPOSITION     = "Content-Disposition";
    public static final String RESPONSE_HEADER_CONTENT_ENCODING        = "Content-Encoding";
    public static final String RESPONSE_HEADER_LOCATION                = "location";
    public static final String RESPONSE_HEADER_SERVER                  = "Server";
    public static final String RESPONSE_HEADER_SET_COOKIE              = "Set-Cookie";

    public static final String RESPONSE_HEADER_TRANSFER_ENCODING                = "Transfer-Encoding";
    public static final String RESPONSE_HEADER_VALUE_TRANSFER_ENCODING_CHUNKED  = "chunked";






    private final IgnoreCaseLinkedHashMap<String, List<String>> headerValue = new IgnoreCaseLinkedHashMap<>();//no use thread...


    private void setValue0(String k, String v) {
        List<String> newValues = new ArrayList<String>();
        if (null != v) {
            newValues.add(v);
        }
        this.setValueList0(k, newValues);
    }
    /**
     * @param v original List
     */
    private void setValueList0(String k, List<String> v) {
        List<String> newValues = null == v ?v = new ArrayList<String>(): v;
        this.headerValue.remove(k);// update key
        this.headerValue.put(k, newValues);
    }



    private void addValue0(String k, String v) {
        List<String> newValues = this.headerValue.get(k);
        if (null == newValues) {
            newValues = new ArrayList<String>();
        }
        this.headerValue.remove(k);// update key
        this.headerValue.put(k, newValues);
        newValues.add(v);
    }


    private void addValueList0(String k, List<String> v) {
        if (null == v) {
            return;
        } else {
            List<String> newValues = this.headerValue.get(k);
            if (null == newValues) {
                newValues = new ArrayList<String>();
            }
            this.headerValue.remove(k);// update key
            this.headerValue.put(k, newValues);
            newValues.addAll(v);
        }
    }



    private String getValue0(String k) {
        List<String> newValues = this.headerValue.get(k);
        return (null == newValues || newValues.size() == 0) ? null : newValues.get(newValues.size() - 1);
    }


    private List<String> getValueList0(String k) {
        return this.headerValue.get(k);
    }






    public boolean containsKey(String key) {
        return this.headerValue.containsKey(key);
    }



    public boolean containsValue(List<String> value) {
        return this.headerValue.containsValue(value);
    }

    public MessageHeader remove(String key) {
        this.headerValue.remove(key);
        return this;
    }



    public int size() {
        return this.headerValue.size();
    }

    public Set<String> keySet() {
        return this.headerValue.keySet();
    }

    public MessageHeader reset() {
        this.headerValue.clear();
        return this;
    }




    @Override
    public Map<String, List<String>> getInnerMap() {
        return this.headerValue;
    }

    public Map<String, List<String>> toStringKeyMap() {
        Map<String, List<String>> map = new LinkedHashMap<>();//no use thread...
        for (String key: this.keySet()) {
            List<String> list = this.getValueList0(key);
            map.put(key, null == list ?null: new ArrayList<>(list));
        }
        return map;
    }













    /*
     * deal multi line able ^ for The beginning of a line
     *
     * ^key: tip\n ^key2: value2\n ^...
     */
    static void dealMultiLineMessage0(MessageHeader m, String headerMessage, boolean putValue) {
        StringReaders reader = new StringReaders(headerMessage);
        reader.setDelimiterAsLine();

        MessageHeader properties = new MessageHeader();
        char[]  lines;
        while (reader.findNext()) {
            lines = reader.readNext();
            if (reader.lastIsReadReadSeparator())
                continue;

            int splistCharindex = Arrayy.indexOf(lines, MessageHeader.ASSIGNMENT_SYMBOL_CHAR, 0, lines.length);
            String ski = null, svi = null;
            if (splistCharindex != -1) {
                ski = new String(lines, 0, splistCharindex);
                splistCharindex++;
                svi = new String(lines, splistCharindex, lines.length - splistCharindex).trim();
            } else {
                svi = new String(lines).trim();
            }

            properties.addValue0(ski, svi);
        }
        if (putValue) {
            for (String key: properties.keySet()) {
                m.setValueList0(key, properties.getValueList0(key));
            }
        } else {
            for (String key: properties.keySet()) {
                m.addValueList0(key, properties.getValueList0(key));
            }
        }
        reader.close();
    }



    public MessageHeader() {
        this((String) null);
    }
    public MessageHeader(String ua) {
        if (null != ua) {
            this.addAll(ua);
        }
    }
    public MessageHeader(String... ua) {
        if (null != ua) {
            this.addAll(ua);
        }
    }
    public MessageHeader(Map<String, List<String>> ua) {
        if (null != ua) {
            this.addAll(ua);
        }
    }








    @Help("set")
    public MessageHeader put(String k, String v) {
        this.setValue0(k, v);
        return this;
    }

    public MessageHeader put(String k, List<String> v) {
        this.setValueList0(k, null == v ?null: new ArrayList<>(v));
        return this;
    }

    @Help("deal multi line able")
    public MessageHeader putAll(String... multiLineContent) {
        StringBuilder buf = new StringBuilder();
        for (String s : multiLineContent) {
            buf.append(s).append(MessageHeader.LINE_SEPARATOR);
        }
        this.putAll(buf.toString());
        buf = null;
        return this;
    }

    @Help("deal multi line able")
    public MessageHeader putAll(String multiLineContent) {
        MessageHeader.dealMultiLineMessage0(this, multiLineContent, true);
        return this;
    }

    public MessageHeader putAll(MessageHeader ua) {
        if (null != ua) {
            return this.putAllKeyMap(ua.getInnerMap());
        }
        return this;
    }
    public MessageHeader putAllValue(Map<String, String> ua) {
        if (null != ua) {
            for (String key : ua.keySet()) {
                String k = key;
                String value = ua.get(k);
                this.setValue0(k, value);
            }
        }
        return this;
    }
    public MessageHeader putAll(Map<String, List<String>> ua) {
        if (null != ua) {
            for (String key : ua.keySet()) {
                String k = key;
                List<String> values = ua.get(k);
                this.setValueList0(k, null == values ? null : new ArrayList<>(values));
            }
        }
        return this;
    }
    public MessageHeader putAllKeyMap(Map<String, List<String>> ua) {
        if (null != ua) {
            for (String key : ua.keySet()) {
                List<String> values = ua.get(key);
                this.setValueList0(key, null == values ? null : new ArrayList<>(values));
            }
        }
        return this;
    }






    @Help("add")
    public MessageHeader add(String k, String v) {
        this.addValue0(k, v);
        return this;
    }

    public MessageHeader addAll(String k, List<String> v) {
        this.addValueList0(k, v);
        return this;
    }

    @Help("deal multi line able")
    public MessageHeader addAll(String... Content) {
        StringBuilder buf = new StringBuilder();
        for (String s : Content) {
            buf.append(s).append(MessageHeader.LINE_SEPARATOR);
        }
        this.addAll(buf.toString());
        return this;
    }

    @Help("deal multi line able")
    public MessageHeader addAll(String Content) {
        MessageHeader.dealMultiLineMessage0(this, Content, false);
        return this;
    }

    public MessageHeader addAll(MessageHeader ua) {
        if (null != ua) {
            return this.addAllKeyMap(ua.getInnerMap());
        }
        return this;
    }
    public MessageHeader addAllValue(Map<String, String> ua) {
        if (null != ua) {
            for (String key : ua.keySet()) {
                String k = key;
                String values = ua.get(k);
                this.addValue0(k, values);
            }
        }
        return this;
    }
    public MessageHeader addAll(Map<String, List<String>> ua) {
        if (null != ua) {
            for (String key : ua.keySet()) {
                String k = key;
                List<String> values = ua.get(k);
                this.addValueList0(k, values);
            }
        }
        return this;
    }
    public MessageHeader addAllKeyMap(Map<String, List<String>> ua) {
        if (null != ua) {
            for (String key : ua.keySet()) {
                List<String> values = ua.get(key);
                this.addValueList0(key, null == values ? null : new ArrayList<>(values));
            }
        }
        return this;
    }






    public String get(String k) {
        return this.getValue0(k);
    }


    public String getNoNNull(String k, String defaultValue) {
        String value = this.getValue0(k);
        return null != value ?value: defaultValue;
    }


    public List<String> getInnerValueList(String k) {
        return this.getValueList0(k);
    }



    public boolean containsValue(String key, String value) {
        List<String> newValues = this.getValueList0(key);
        return null != newValues && newValues.contains(value);
    }



    public MessageHeader removeValue(String key, String value) {
        List<String> newValues = this.getValueList0(key);
        if (null != newValues) {
            newValues.remove(value);
        }
        return this;
    }




    public int size(String key) {
        List<String> list = this.getValueList0(key);
        return null == list ? 0 : list.size();
    }




    @Override
    public String toString() {
        // TODO: Implement this method
        return this.toHeaderString();
    }





    /**
     * @return [Cookie or Set-Cookie]
     */
    public CookieText getCookies() {
        CookieText cookieText = new CookieText();
        if (containsKey(REQUEST_HEADER_COOKIE)) {
            cookieText.putAll(getCookies(REQUEST_HEADER_COOKIE));
        }
        if (containsKey(RESPONSE_HEADER_SET_COOKIE)) {
            cookieText.putAll(getCookies(RESPONSE_HEADER_SET_COOKIE));
        }
        return cookieText;
    }

    public CookieText getCookies(String key)    {
        List<String> value0 = this.getValueList0(key);
        CookieText cookieText = new CookieText();
        if (null != value0) {
            for (String s : value0) {
                CookieText.parse(cookieText.getInnerMap(), s);
            }
        }
        return cookieText;
    }


    public MessageHeader setCookies(String key, CookieText cookieText) {
        if (null == cookieText || cookieText.isEmpty()) { return this; }

        String value = cookieText.toString();
        this.setValue0(key, value);

        return this;
    }












    /**
     * key: tip\n
     * ...
     */
    public String toHeaderString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String fk : this.keySet()) {
            String ok = fk;
            String oks = null == ok ?null: String.valueOf(ok);
            List<String> values = this.getValueList0(fk);
            if (null == values || values.size() == 0) {
                stringBuilder
                        .append(oks)
                        .append(MessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                        .append(' ')
                        .append(MessageHeader.LINE_SEPARATOR);
            } else {
                for (String v : values) {
                    stringBuilder
                            .append(oks)
                            .append(MessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                            .append(' ')
                            .append(v)
                            .append(MessageHeader.LINE_SEPARATOR);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * http protocol read to null line stop
     */
    /**
     * requestOrReturnLine\n
     * key: tip\n
     * ...
     * key: tip\n
     * \n
     */
    public String toHttpHeaderString(String requestOrReturnLine) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != requestOrReturnLine && requestOrReturnLine.length() > 0) {
            stringBuilder
                    .append(requestOrReturnLine)
                    .append(MessageHeader.LINE_SEPARATOR);
        }
        if (this.size() != 0) {
            for (String fk : this.keySet()) {
                String ok = fk;
                String oks = null == ok ?null: String.valueOf(ok);
                List<String> values = this.getValueList0(fk);
                if (null == values || values.size() == 0) {
                    stringBuilder
                            .append(oks)
                            .append(MessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                            .append(' ')
                            .append(MessageHeader.LINE_SEPARATOR);
                } else {
                    for (String v : values) {
                        stringBuilder
                                .append(oks)
                                .append(MessageHeader.ASSIGNMENT_SYMBOL_CHAR)
                                .append(' ')
                                .append(v)
                                .append(MessageHeader.LINE_SEPARATOR);
                    }
                }
            }
        }
        stringBuilder.append(MessageHeader.LINE_SEPARATOR);
        String result = stringBuilder.toString(); stringBuilder = null;
        return result;
    }









    /*
     * 不会设置key为空的字段
     */
    @Help("the empty key won't be set")
    public MessageHeader setToURLConnection(URLConnection con) {
        for (String fk : this.keySet()) {
            String ok = fk;
            String oks = null == ok ?null: String.valueOf(ok);
            if (null == oks || oks.length() == 0) {
                continue;
            }
            List<String> vs = this.getValueList0(fk);
            MessageHeader.setToURLConnection(con, oks, vs);
        }
        return this;
    }
    public static void setToURLConnection(URLConnection con, String oks, List<String> vs) {
        if (null == vs) {
            con.setRequestProperty(oks, "");
        } else if (vs.size() == 0) {
            con.setRequestProperty(oks, "");
        } else {
            int i = 0;
            for (String vi : vs) {
                if (i == 0) {
                    con.setRequestProperty(oks, String.valueOf(vi));
                } else {
                    con.addRequestProperty(oks, String.valueOf(vi));
                }
                i++;
            }
        }
    }






    /*
     * 不会添加key为空的字段
     */
    @Help("the empty key won't be add")
    public MessageHeader addToURLConnection(URLConnection con) {
        for (String fk : this.keySet()) {
            String ok = fk;
            String oks = null == ok ?null: String.valueOf(ok);
            if (null == oks || oks.length() == 0) {
                continue;
            }
            List<String> vs = this.getValueList0(fk);
            MessageHeader.addToURLConnection(con, oks, vs);
        }
        return this;
    }
    public static void addToURLConnection(URLConnection con, String oks, List<String> vs) {
        if (null == vs) {
            con.addRequestProperty(oks, "");
        } else if (vs.size() == 0) {
            con.addRequestProperty(oks, "");
        } else {
            for (String vi : vs) {
                con.addRequestProperty(oks, String.valueOf(vi));
            }
        }
    }

}






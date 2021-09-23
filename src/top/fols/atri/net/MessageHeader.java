package top.fols.atri.net;

import java.io.Serializable;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import top.fols.atri.lang.Finals;
import top.fols.atri.util.interfaces.InnerMap;
import top.fols.box.annotation.BaseAnnotations;
import top.fols.box.io.base.XStringReader;
import top.fols.box.util.XArrays;
import top.fols.atri.util.BlurryKey;
import top.fols.atri.util.BlurryKey.IgnoreCaseKey;

/**
 * HTTP/1.1和HTTP/2报头都是不区分大小写的。
 * 根据RFC 7230(http/1.1)： 每个标头字段由一个不区分大小写的字段名和一个冒号、 可选的前导空格、字段值和可选的尾随空格组成。
 * 还有，RFC 7540(http/2)：  就像在HTTP/1.x中一样，头字段名是ASCII的字符串以不区分大小写的方式进行比较的字符。
 *
 */

@SuppressWarnings({"unchecked", "UnusedReturnValue", "CharsetObjectCanBeUsed"})
public class MessageHeader implements Serializable, InnerMap<IgnoreCaseKey<String>, List<String>> {
    private static final long serialVersionUID = 1L;




    /**
     * HTTP 的规范中如此描述（3.2.4. Field Parsing）：
     * ISO-8859-1 编码是单字节编码，向下兼容ASCII，其编码范围是0x00-0xFF，0x00-0x7F之间完全和ASCII一致，0x80-0x9F之间是控制字符，0xA0-0xFF之间是文字符号。
     * 即 HTTP 头部的事实字符集乃是 US-ASCII 一个子集，所以HTTP 规范允许的字符集是 ISO-8859-1。
     * 如果想在服务器支持HTTP头部有汉字，那么只好把可能包含中文的头部值进行 url编码，然后整体进行ISO-8859-1编码。
     */
    public 	static final Charset 		HTTP_MESSAGE_HEADER_CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");

    public 	static final String 		LINE_SEPARATOR = new String(Finals.Separator.getCharsLineSeparatorRN());
    public 	static final char 			ASSIGNMENT_SYMBOL_CHAR = ':';

    private static final IgnoreCaseKey KeyFactory = BlurryKey.IgnoreCaseKey.getDefaultFactory();






    public static final BlurryKey.IgnoreCaseKey<String> REQUEST_HEADER_COOKIE           = KeyFactory.newKey("Cookie");
    public static final BlurryKey.IgnoreCaseKey<String> REQUEST_HEADER_HOST             = KeyFactory.newKey("Host");
    public static final BlurryKey.IgnoreCaseKey<String> REQUEST_HEADER_RANGE            = KeyFactory.newKey("Range");

    public static final BlurryKey.IgnoreCaseKey<String> REQUEST_HEADER_CONTENT_TYPE     = KeyFactory.newKey("Content-Type");

    public static final BlurryKey.IgnoreCaseKey<String> REQUEST_HEADER_ACCEPT_ENCODING  = KeyFactory.newKey("Accept-Encoding");
    public static final String REQUEST_HEADER_VALUE_ACCEPT_ENCODING_IDENTITY            = "identity";





    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_CONTENT_RANGE           = KeyFactory.newKey("Content-Range");
    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_ACCEPT_RANGES           = KeyFactory.newKey("Accept-Ranges");

    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_CONTENT_LENGTH          = KeyFactory.newKey("Content-Length");
    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_CONTENT_TYPE            = KeyFactory.newKey("Content-Type");
    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_CONTENT_DISPOSITION     = KeyFactory.newKey("Content-Disposition");
    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_CONTENT_ENCODING        = KeyFactory.newKey("Content-Encoding");
    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_LOCATION                = KeyFactory.newKey("location");

    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_SERVER                  = KeyFactory.newKey("Server");

    public static final BlurryKey.IgnoreCaseKey<String> RESPONSE_HEADER_SET_COOKIE              = KeyFactory.newKey("Set-Cookie");









    private final Map<IgnoreCaseKey<String>, List<String>> headerValue = new LinkedHashMap<>();


    private void setValue0(String k, String v) {
        this.setValue0(KeyFactory.newKey(k), v);
    }
    private void setValue0(IgnoreCaseKey<String> k, String v) {
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
        this.setValueList0(KeyFactory.newKey(k), v);
    }
    private void setValueList0(IgnoreCaseKey<String> k, List<String> v) {
        List<String> newValues = null == v ?v = new ArrayList<String>(): v;
        this.headerValue.remove(k);// update key
        this.headerValue.put(k, newValues);
    }



    private void addValue0(String k, String v) {
        this.addValue0(KeyFactory.newKey(k), v);
    }
    private void addValue0(IgnoreCaseKey<String> k, String v) {
        List<String> newValues = this.headerValue.get(k);
        if (null == newValues) {
            newValues = new ArrayList<String>();
        }
        this.headerValue.remove(k);// update key
        this.headerValue.put(k, newValues);
        newValues.add(v);
    }


    private void addValueList0(String k, List<String> v) {
        this.addValueList0(KeyFactory.newKey(k), v);
    }
    private void addValueList0(IgnoreCaseKey<String> k, List<String> v) {
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
        return this.getValue0(KeyFactory.newKey(k));
    }
    private String getValue0(IgnoreCaseKey<String> k) {
        List<String> newValues = this.headerValue.get(k);
        return (null == newValues || newValues.size() == 0) ? null : newValues.get(newValues.size() - 1);
    }


    private List<String> getValueList0(String k) {
        return this.getValueList0(KeyFactory.newKey(k));
    }
    private List<String> getValueList0(IgnoreCaseKey<String> k) {
        return this.headerValue.get(k);
    }







    public boolean containsKey(String key) {
        return this.containsKey(KeyFactory.newKey(key));
    }
    public boolean containsKey(IgnoreCaseKey<String> key) {
        return this.headerValue.containsKey(key);
    }



    public boolean containsValue(List<String> value) {
        return this.headerValue.containsValue(value);
    }




    public int size() {
        return this.headerValue.size();
    }

    public Set<IgnoreCaseKey<String>> keySet() {
        return this.headerValue.keySet();
    }

    public MessageHeader reset() {
        this.headerValue.clear();
        return this;
    }

    public MessageHeader remove(String key) {
        return this.remove(KeyFactory.newKey(key));
    }
    public MessageHeader remove(IgnoreCaseKey<String> key) {
        this.headerValue.remove(key);
        return this;
    }



    @Override
    public Map<IgnoreCaseKey<String>, List<String>> getInnerMap() {
        return this.headerValue;
    }

    public Map<String, List<String>> toOriginStringKeyMap() {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (IgnoreCaseKey<String> key: this.keySet()) {
            List<String> list = this.getValueList0(key);
            map.put((String)(null == key ?null: key.getOriginKey()), null == list ?null: new ArrayList<String>(list));
        }
        return map;
    }


    public Map<String, List<String>> toFormatStringKeyMap() {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (IgnoreCaseKey<String> key: this.keySet()) {
            List<String> list = this.getValueList0(key);
            map.put((String)(null == key ?null: key.getFormatKey()), null == list ?null: new ArrayList<String>(list));
        }
        return map;
    }













    /*
     * deal multi line able ^ for The beginning of a line
     *
     * ^key: value\n ^key2: value2\n ^...
     */
    private static final char[][] LINE_SEPARATOR_ALL = new char[][]{
            Finals.Separator.getCharsLineSeparatorRN(),
            Finals.Separator.getCharsLineSeparatorR(),
            Finals.Separator.getCharsLineSeparatorN()
    };
    private static void dealMultiLineMessage0(MessageHeader m, String headerMessage, boolean putValue) {
        XStringReader rowStreanm = new XStringReader(headerMessage);
        char lines[];
        char splitchar = MessageHeader.ASSIGNMENT_SYMBOL_CHAR;


        MessageHeader properties = new MessageHeader();

        while (null != (lines = rowStreanm.readLine(LINE_SEPARATOR_ALL, false))) {
            if (lines.length == 0) {
                continue;
            }
            int splistCharindex = XArrays.indexOf(lines, splitchar, 0, lines.length);
            String ski = null, svi = null;
            if (splistCharindex != -1) {
                ski = new String(lines, 0, splistCharindex);
                splistCharindex++;
                svi = new String(lines, splistCharindex, lines.length - splistCharindex).trim();
            } else {
                svi = new String(lines).trim();
            }

            properties.addValue0(ski, svi);

            lines = null;
        }

        if (putValue) {
            for (IgnoreCaseKey<String> key: properties.keySet()) {
                m.setValueList0(key, properties.getValueList0(key));
            }
        } else {
            for (IgnoreCaseKey<String> key: properties.keySet()) {
                m.addValueList0(key, properties.getValueList0(key));
            }
        }

        properties = null;
        rowStreanm.close();
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








    @BaseAnnotations("set")
    public MessageHeader put(String k, String v) {
        this.setValue0(k, v);
        return this;
    }
    public MessageHeader put(IgnoreCaseKey<String> k, String v) {
        this.setValue0(k, v);
        return this;
    }

    public MessageHeader put(String k, List<String> v) {
        this.setValueList0(k, null == v ?null: new ArrayList<>(v));
        return this;
    }
    public MessageHeader put(IgnoreCaseKey<String> k, List<String> v) {
        this.setValueList0(k, null == v ?null: new ArrayList<>(v));
        return this;
    }

    @BaseAnnotations("deal multi line able")
    public MessageHeader putAll(String... multiLineContent) {
        StringBuilder buf = new StringBuilder();
        for (String s : multiLineContent) {
            buf.append(s).append(MessageHeader.LINE_SEPARATOR);
        }
        this.putAll(buf.toString());
        buf = null;
        return this;
    }

    @BaseAnnotations("deal multi line able")
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
    public MessageHeader putAllKeyMap(Map<IgnoreCaseKey<String>, List<String>> ua) {
        if (null != ua) {
            for (IgnoreCaseKey<String> key : ua.keySet()) {
                List<String> values = ua.get(key);
                this.setValueList0(key, null == values ? null : new ArrayList<>(values));
            }
        }
        return this;
    }






    @BaseAnnotations("add")
    public MessageHeader add(String k, String v) {
        this.addValue0(k, v);
        return this;
    }
    public MessageHeader add(IgnoreCaseKey<String> k, String v) {
        this.addValue0(k, v);
        return this;
    }

    public MessageHeader addAll(String k, List<String> v) {
        this.addValueList0(k, v);
        return this;
    }
    public MessageHeader addAll(IgnoreCaseKey<String> k, List<String> v) {
        this.addValueList0(k, v);
        return this;
    }

    @BaseAnnotations("deal multi line able")
    public MessageHeader addAll(String... Content) {
        StringBuilder buf = new StringBuilder();
        for (String s : Content) {
            buf.append(s).append(MessageHeader.LINE_SEPARATOR);
        }
        this.addAll(buf.toString());
        return this;
    }

    @BaseAnnotations("deal multi line able")
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
    public MessageHeader addAllKeyMap(Map<IgnoreCaseKey<String>, List<String>> ua) {
        if (null != ua) {
            for (IgnoreCaseKey<String> key : ua.keySet()) {
                List<String> values = ua.get(key);
                this.addValueList0(key, null == values ? null : new ArrayList<>(values));
            }
        }
        return this;
    }






    public String get(String k) {
        return this.getValue0(k);
    }
    public String get(IgnoreCaseKey<String> k) {
        return this.getValue0(k);
    }


    public String getNoNNull(String k, String defaultValue) {
        String value = this.getValue0(k);
        return null != value ?value: defaultValue;
    }
    public String getNoNNull(IgnoreCaseKey<String> k, String defaultValue) {
        String value = this.getValue0(k);
        return null != value ?value: defaultValue;
    }



    public List<String> getInnerValueList(String k) {
        return this.getValueList0(k);
    }
    public List<String> getInnerValueList(IgnoreCaseKey<String> k) {
        return this.getValueList0(k);
    }



    public boolean containsValue(String key, String value) {
        return this.containsKey(KeyFactory.newKey(key));
    }
    public boolean containsValue(IgnoreCaseKey<String> key, String value) {
        List<String> newValues = this.getValueList0(key);
        return null != newValues && newValues.contains(value);
    }



    public MessageHeader removeValue(String key, String value) {
        return this.removeValue(KeyFactory.newKey(key), value);
    }
    public MessageHeader removeValue(IgnoreCaseKey<String> key, String value) {
        List<String> newValues = this.getValueList0(key);
        if (null != newValues) {
            newValues.remove(value);
        }
        return this;
    }




    public int size(String key) {
        return this.size(KeyFactory.newKey(key));
    }
    public int size(IgnoreCaseKey<String> key) {
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
    public Cookies getCookies() {
        Cookies cookies = new Cookies();
        if (containsKey(REQUEST_HEADER_COOKIE)) {
            cookies.putAll(getCookies(REQUEST_HEADER_COOKIE));
        }
        if (containsKey(RESPONSE_HEADER_SET_COOKIE)) {
            cookies.putAll(getCookies(RESPONSE_HEADER_SET_COOKIE));
        }
        return cookies;
    }

    public Cookies getCookies(String key)                   {
        return this.getCookies(KeyFactory.newKey(key));
    }
    public Cookies getCookies(IgnoreCaseKey<String> key)    {
        List<String> value0 = this.getValueList0(key);
        Cookies cookies = new Cookies();
        if (null != value0) {
            for (String s : value0) {
                Cookies.parse(cookies.getInnerMap(), s);
            }
        }
        return cookies;
    }


    public MessageHeader setCookies(String key, Cookies cookies) {
        return this.setCookies(KeyFactory.newKey(key), cookies);
    }
    public MessageHeader setCookies(IgnoreCaseKey<String> key, Cookies cookies) {
        if (null == cookies || cookies.isEmpty()) { return this; }

        String value = cookies.toString();
        this.setValue0(key, value);

        return this;
    }












    /**
     * key: value\n
     * ...
     */
    public String toHeaderString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (IgnoreCaseKey<String> fk : this.keySet()) {
            String ok = fk.getOriginKey();
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
     * key: value\n
     * ...
     * key: value\n
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
            for (IgnoreCaseKey<String> fk : this.keySet()) {
                String ok = fk.getOriginKey();
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
    @BaseAnnotations("the empty key won't be set")
    public MessageHeader setToURLConnection(URLConnection con) {
        for (IgnoreCaseKey<String> fk : this.keySet()) {
            String ok = fk.getOriginKey();
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
    @BaseAnnotations("the empty key won't be add")
    public MessageHeader addToURLConnection(URLConnection con) {
        for (IgnoreCaseKey<String> fk : this.keySet()) {
            String ok = fk.getOriginKey();
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






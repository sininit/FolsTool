package top.fols.box.net;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.XString;
import top.fols.box.util.encode.XURLEncoder;
import top.fols.box.util.interfaces.XInterfaceGetInnerMap;

public class XURLParam implements Serializable, XInterfaceGetInnerMap {
    private static final long serialVersionUID = 1L;
    
    
    public static final String PARAM_SYMBOL = XURL.PARAM_SYMBOL;
    public static final char PARAM_SYMBOL_CHAR = XURL.PARAM_SYMBOL_CHAR;

    public static final String PARAM_PROJECT_SEPARATOR = XURL.PARAM_PROJECT_SEPARATOR;
    public static final String PARAM_PROJECT_ASSIGNMENT_SYMBOL = XURL.PARAM_PROJECT_ASSIGNMENT_SYMBOL;

    private Map<String, String> param = new LinkedHashMap<String, String>();

    /**
     * format: key=value&key1=value1...
     */
    private void parse0(String all) {
        List<String> tmp = XString.split(new StringBuilder(all).append(XURL.PARAM_PROJECT_SEPARATOR).toString(),
            XURL.PARAM_PROJECT_SEPARATOR);
        for (String kv : tmp) {
            if (kv.isEmpty()) {
                continue;
            }
            int vindex = kv.indexOf(XURL.PARAM_PROJECT_ASSIGNMENT_SYMBOL);
            if (vindex > -1) {
                String k;
                String v;

                k = kv.substring(0, vindex);

                v = kv.substring(vindex + XURL.PARAM_PROJECT_ASSIGNMENT_SYMBOL.length(), kv.length());

                param.put(k, v);
            } else {
                param.put(kv, null);
            }
        }
        tmp = null;
    }

    public XURLParam() {
        super();
    }
    /**
     * @param param format: key=value&key1=value1...
     */
    public XURLParam(String param) {
        this.parse0(param);
    }

    public XURLParam(XURL xurl) {
        this.parse0(xurl.getParam());
    }




    
    @XAnnotations("this method will return the value after the original result is decoded.")
    public String get(String key) {
        return this.get(key, true, null);
    }

    @XAnnotations("this method will return the value after the original result is decoded.")
    public String get(String key, String charsetName) {
        return this.get(key, true, charsetName);
    }

    @XAnnotations("direct return value")
    public String getND(String key) {
        return this.get(key, false, null);
    }

    public String get(String key, boolean decodeValue, String charsetName) {
        if (null == key) {
            return null;
        }
        String result = param.get(key);
        if (null == result) {
            result = param.get(XURLEncoder.encode(key, charsetName));
        }
        if (null == result) {
            return null;
        }
        if (decodeValue) {
            return XURLEncoder.decodeMatch(result, charsetName);
        }
        return result;
    }

    @XAnnotations("direct put data")
    public XURLParam putNE(String orginkey, String orginValue) {
        this.put(orginkey, orginValue, false, false, null);
        return this;
    }

    @XAnnotations("this method will put after encoding the key and Value.")
    public XURLParam put(String orginkey, String orginValue) {
        this.put(orginkey, orginValue, true, true, null);
        return this;
    }

    @XAnnotations("this method will put after encoding the key and Value.")
    public XURLParam put(String orginkey, String orginValue, String charsetName) {
        this.put(orginkey, orginValue, true, true, charsetName);
        return this;
    }

    public XURLParam put(String orginkey, String orginValue, boolean encodeKey, boolean encodeValue,
                         String charsetName) {
        if (null == orginkey) {
            return this;
        }
        if (encodeKey) {
            orginkey = XURLEncoder.encode(orginkey, charsetName);
        }
        if (encodeValue) {
            orginValue = XURLEncoder.encode(orginValue, charsetName);
        }
        this.param.put(orginkey, orginValue);
        orginkey = null;
        orginValue = null;
        return this;
    }

    public void putAll(XURLParam params) {
        this.param.putAll(params.param);
    }


    @XAnnotations("direct remove")
    public XURLParam remove(String key) {
        this.param.remove(key);
        return this;
    }

    @XAnnotations("direct check")
    public boolean containsKey(String key) {
        return this.param.containsKey(key);
    }


    @XAnnotations("if you want to manually modify his please also manual coding")
    @Override
    public Map<String, String> getInnerMap() {
        return this.param;
    }

    public Set<String> keySet() {
        return this.param.keySet();
    }


    public int size() {
        return this.param.size();
    }

    public boolean isExistParam() {
        return !this.param.isEmpty();
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        return this.toFormatString();
    }

    public XURLParam clear() {
        this.param.clear();
        return this;
    }

    @XAnnotations("direct return, result format:  key=value&key1=value1...")
    public String toFormatString() {
        return this.toFormatString0(false);
    }

    private String toFormatString0(boolean addUrlAndParamSplitChars) {
        StringBuilder sb = new StringBuilder();
        if (addUrlAndParamSplitChars) {
            sb.append(XURL.PARAM_SYMBOL);
        }
        int i = 0, length = this.param.size();
        for (String k : this.param.keySet()) {
            sb.append(null == k ? "null" : k);
            String v = this.param.get(k);
            if (null != v) {
                sb.append(XURL.PARAM_PROJECT_ASSIGNMENT_SYMBOL);
                sb.append(null == v ? "null" : v);
            }

            if (i < length - 1) {
                sb.append(XURL.PARAM_PROJECT_SEPARATOR);
            }
            i++;
        }
        return sb.toString();
    }
}



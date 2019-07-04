package top.fols.box.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.XString;

public class XURLParam {
	public XURLParam() {
		super();
	}
	public XURLParam(String url) {
		this(new XURL(url));
	}
	public XURLParam(XURL xurl) {
		String all = new StringBuilder(xurl.getFileNameDetailed()).append(XURL.ParamSplitChars).toString();
		int ep = null == all ?-1: all.indexOf(XURL.UrlAndParamSplitChars);
		if (ep > -1) {
			all = all.substring(ep + XURL.UrlAndParamSplitChars.length(), all.length());
			List<String> s = XString.split(all, XURL.ParamSplitChars);
			for (String kv:s) {
				if (kv.isEmpty())
					continue;
				int vindex = kv.indexOf(XURL.ParamKeyValueSplitChars);
				if (vindex > -1) {
					String k;
					String v;
					byte[] bs;

					bs = kv.substring(0, vindex).getBytes();
					k = new String(bs);
					bs = null;

					bs = kv.substring(vindex + XURL.ParamKeyValueSplitChars.length(), kv.length()).getBytes();
					v = new String(bs);
					bs = null;

					param.put(k, v);
				}
			}
			s.clear();
		}	
	}



	private Map<String,String> param = new HashMap<String,String>();
	
	public String get(String key, boolean decodeValue, String charsetName) {
		if (null == key)
			return null;
		String result = param.get(key);
		if (null == result)
			result = param.get(XURLCoder.encode(key, charsetName));
		if (null == result)
			return null;
		if (decodeValue) 
			return XURLCoder.decodeS(result, charsetName);
		return result;
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
	public Map<String,String> getAll() {
		return this.param;
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
		return this.param.toString();
	}
	public XURLParam clear() {
		this.param.clear();
		return this;
	}

	@XAnnotations("direct return, result format:  ?key=value&...")
	public String param2URLFormat() {
		StringBuilder builder;
		builder = new StringBuilder();
		builder.append(XURL.UrlAndParamSplitChars);
		int i = 0,length = this.param.size();
		for (String k:this.param.keySet()) {
			builder.append(null == k ?"null": k);
			builder.append(XURL.ParamKeyValueSplitChars);
			String v = this.param.get(k);
			builder.append(null == v ?"null": v);
			if (i != length - 1) 
				builder.append(XURL.ParamSplitChars);
			i++;
		}
		return builder.toString();
	}




	@XAnnotations("direct put data")
	public XURLParam putNE(String orginkey, String orginValue) {
		put(orginkey, orginValue, false, false, null);
		return this;
	}

	@XAnnotations("this method will put after encoding the key and Value.")
	public XURLParam put(String orginkey, String orginValue) {
		put(orginkey, orginValue, true, true, null);
		return this;
	}
	@XAnnotations("this method will put after encoding the key and Value.")
	public XURLParam put(String orginkey, String orginValue, String charsetName) {
		put(orginkey, orginValue, true, true, charsetName);
		return this;
	}
	
	public XURLParam put(String orginkey, String orginValue, boolean encodeKey, boolean encodeValue, String charsetName) {
		if (null == orginkey)
			return this;
		if (encodeKey)
			orginkey = XURLCoder.encode(orginkey, charsetName);
		if (encodeValue)
			orginValue = XURLCoder.encode(orginValue, charsetName);
		this.param.put(orginkey, orginValue);
		orginkey = null;
		orginValue = null;
		return this;
	}
}

package top.fols.atri.net;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import top.fols.atri.util.interfaces.IInnerMap;
import top.fols.box.annotation.BaseAnnotations;
import top.fols.box.util.encode.XURLEncoder;

@SuppressWarnings("SpellCheckingInspection")
public class URLParams implements Serializable, IInnerMap<String, String> {
	private static final long serialVersionUID = 1L;


	public static final String PARAM_SYMBOL 		= XURL.PARAM_SYMBOL;
	public static final char   PARAM_SYMBOL_CHAR    = XURL.PARAM_SYMBOL_CHAR;

	public static final char   PARAM_ELEMENT_SEPARATOR_CHAR   = '&';
	public static final char   ASSIGNMENT_ELEMENT_SYMBOL_CHAR = '=';

	private final Map<String, String> values = new LinkedHashMap<>();






	/**
	 * format: key=value&key1=value1...
	 */
	private void parse0(String all) {
		parse(this.values, all);
	}

	public URLParams() {
		super();
	}
	/**
	 * @param param format: key=value&key1=value1...
	 */
	public URLParams(String param) {
		this.parse0(param);
	}

	public URLParams(XURL xurl) {
		this.parse0(xurl.getParam());
	}





	@BaseAnnotations("this method will return the value after the original result is decoded.")
	public String get(String key) {
		return this.get(key, true, null);
	}

	@BaseAnnotations("this method will return the value after the original result is decoded.")
	public String get(String key, String charsetName) {
		return this.get(key, true, charsetName);
	}

	@BaseAnnotations("direct return value")
	public String getData(String key) {
		return this.get(key, false, null);
	}

	public String get(String key, boolean decodeValue, String charsetName) {
		if (null == key) { return null; }

		String result = values.get(key);

		if (null == result) { result = values.get(XURLEncoder.encode(key, charsetName)); }
		if (null == result) { return null;}

		if (decodeValue) { return XURLEncoder.decodeMatch(result, charsetName); }
		return result;
	}

	@BaseAnnotations("direct put data")
	public URLParams putData(String orginkey, String orginValue) {
		this.put(orginkey, orginValue, false, false, null);
		return this;
	}

	@BaseAnnotations("this method will put after encoding the key and Value.")
	public URLParams put(String orginkey, String orginValue) {
		this.put(orginkey, orginValue, true, true, null);
		return this;
	}

	@BaseAnnotations("this method will put after encoding the key and Value.")
	public URLParams put(String orginkey, String orginValue, String charsetName) {
		this.put(orginkey, orginValue, true, true, charsetName);
		return this;
	}

	public URLParams put(String orginkey, String orginValue, boolean encodeKey, boolean encodeValue,
						 String charsetName) {
		if (null == orginkey) { return this; }

		if (encodeKey)   { orginkey   = XURLEncoder.encode(orginkey,   charsetName); }
		if (encodeValue) { orginValue = XURLEncoder.encode(orginValue, charsetName); }

		this.values.put(orginkey, orginValue);
		return this;
	}

	public void putAll(URLParams params) {
		this.values.putAll(params.values);
	}


	@BaseAnnotations("direct remove")
	public URLParams remove(String key) {
		this.values.remove(key);
		return this;
	}

	@BaseAnnotations("direct check")
	public boolean containsKey(String key) {
		return this.values.containsKey(key);
	}


	@BaseAnnotations("if you want to manually modify his please also manual coding")
	@Override
	public Map<String, String> getInnerMap() {
		return this.values;
	}

	public Set<String> keySet() {
		return this.values.keySet();
	}


	public int size() {
		return this.values.size();
	}

	public boolean isEmpty() {
		return this.values.isEmpty();
	}
	public boolean nonEmpty() {
		return !this.values.isEmpty();
	}

	public URLParams clear() {
		this.values.clear();
		return this;
	}


	@BaseAnnotations("direct return, result format:  key=value&key1=value1...")
	@Override
	public String toString() {
		// TODO: Implement this method
		StringBuilder sb = new StringBuilder();
		for (String k : this.values.keySet()) {
			if (null == k) { continue; }
			sb.append(k);

			String  v = this.values.get(k);
			if (null != v) {
				sb.append(ASSIGNMENT_ELEMENT_SYMBOL_CHAR);
				sb.append(v);
			}

			sb.append(PARAM_ELEMENT_SEPARATOR_CHAR);
		}
		if (sb.length() > 1) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}


	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (obj == this) { return true; }
		if (obj instanceof URLParams == false) { return false; }

		URLParams instance = (URLParams) obj;
		return instance.values.equals(values);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return values.hashCode();
	}

	@Override
	public URLParams clone() {
		// TODO: Implement this method
		URLParams p;
		p = new URLParams();
		p.putAll(this);

		return p;
	}





	public static Map<String,String> parse(Map<String,String> put, String value) {
		if (null == put) { put = new LinkedHashMap<>();}
		if (null == value) { return put; }

		int last = 0, offset, length = value.length();
		while (true) {
			if ((offset  = value.indexOf(PARAM_ELEMENT_SEPARATOR_CHAR, last)) == -1) {
				if (last <   length) {
					offset = length;
				} else {
					break;
				}
			}
			int assignment = -1;
			if (last < length) {
				for (int i = last; i < offset; i++) {
					if (value.charAt(i) == ASSIGNMENT_ELEMENT_SYMBOL_CHAR) {
						assignment = i;
						break;
					}
				}
			}
			if (assignment == -1) {
				String  element = substring(value, last, offset);
				put.put(element, null);
			} else {
				String  key = substring(value, last, assignment);
				String  val = substring(value, assignment + 1, offset);
				put.put(key, val);
			}

			last = offset + 1;// + char length, olny 1
		}
		return put;
	}
	public static String substring(String str, int startIndex, int endIndex) {
		return str.substring(startIndex, endIndex);
	}
}

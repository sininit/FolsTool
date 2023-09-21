package top.fols.atri.net;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.atri.interfaces.interfaces.IInnerMap;
import top.fols.box.util.encode.URLEncoders;

public class HttpURLParam implements Serializable, IInnerMap<String, List<String>> {
	private static final long serialVersionUID = 1L;

	public static final char   PARAM_ELEMENT_SEPARATOR_CHAR   = HttpURL.PARAM_PROJECT_SEPARATOR_CHAR;
	public static final char   ASSIGNMENT_ELEMENT_SYMBOL_CHAR = HttpURL.PARAM_PROJECT_ASSIGNMENT_SYMBOL_CHAR;

	private final Map<String, List<String>> values = new LinkedHashMap<>();//no use thread...

	@Override
	public Map<String, List<String>> getInnerMap() {
		return this.values;
	}
	public Set<String> keySet() {
		return this.values.keySet();
	}


	public HttpURLParam() {
		super();
	}
	/**
	 * @param param format: key=tip&key1=value1...
	 */
	public HttpURLParam(String param) {
		this.parseParamString(param);
	}

	public HttpURLParam(HttpURL xurl) {
		this.parseParamString(xurl.getParam());
	}


	/**
	 * format: key=tip&key1=value1...
	 */
	private void parseParamString(String all) {
		this.putAll(parse(all));
	}




	Charset charset = Finals.Charsets.UTF_8;
	public HttpURLParam setCharset(Charset charset) {
		this.charset = charset; 
		return this; 
	}


	public boolean isEncode(String str) {
		return URLEncoders.hasURLEncode(str);
	}
	public String toEncode(String str) {
		return isEncode(str) ? str : URLEncoders.encode(str, charset);
	}
	public String toDecode(String str) {
		return isEncode(str) ? URLEncoders.decodeMatch(str, charset) : str;
	}


	public String findValue(List<String> list, String v) {
		String vk;
		if (list.contains(vk = v))           return vk;
		if (list.contains(vk = toEncode(v))) return vk;
		if (list.contains(vk = toDecode(v))) return vk;
		return null;
	} 
	public String findKey(String k) {
		String vk;
		if (this.values.containsKey(vk = k))           return vk;
		if (this.values.containsKey(vk = toEncode(k))) return vk;
		if (this.values.containsKey(vk = toDecode(k)))   return vk;
		return null;
	}


	private boolean containsValue0(String key, String value) {
		List<String> list = this.getValueList0(key);
		if (null ==  list) return false;
		if (null == value) return true;
		String find = findValue(list, value);
		return null != find;
	}
	private boolean removeValue0(String key, String value) {
		List<String> list = this.getValueList0(key);
		if (null ==  list) return false;

		if (null == value) return true;
		String find = findValue(list, value);
		return list.remove(find);
	}

	private boolean containsKey0(String k) {
		String find = findKey(k);
		return null != find;
	}
	private boolean removeKey0(String k) {
		String find = findKey(k);
		return null != this.values.remove(find);
	}




	private String getValue0(String k) {
		return Objects.last(this.getValueList0(k));
	}
	private List<String> getValueList0(String k) {
		return this.values.get(findKey(k));
	}

	private void setValue0(String k, String v) {
		List<String> newValues = new ArrayList<>();
		if (null != v) {
			newValues.add(v);
		}
		this.setValueList0(k, newValues);
	}
	private void setValueList0(String k0, List<String> v0) {
		this.values.remove(findKey(k0));// update key

		List<String> list = new ArrayList<>();
		for (String v0e: v0)	{
			list.add(v0e);
		}
		this.values.put(k0, list);
	}

	private void addValue0(String k, String v) {
		List<String> newValues = new ArrayList<>();
		if (null != v) {
			newValues.add(v);
		}
		this.addValueList0(k, newValues);
	}
	private void addValueList0(String k0, List<String> v0) {
		if (null != v0) {
			String findKey = findKey(k0);
			List<String> lastValues = this.values.get(findKey);
			if (null ==  lastValues) {
				lastValues = new ArrayList<>();
			}
			this.values.remove(findKey);// update key

			for (String v0e: v0)	{
				lastValues.add(v0e);
			}
			this.values.put(k0, lastValues);
		}
	}

	private boolean equals0(HttpURLParam instance) {
		return instance.values.equals(values);
	}
	private int hashCode0() {
		return values.hashCode();
	}


	private String toStringAsToOriginal0() {
		StringBuilder sb = new StringBuilder();
		for (String k : this.values.keySet()) {
			if (null == k) { continue; }

			List<String>   vs = this.getValueList0(k);
			for (String v: vs) {
				sb.append(k);
				if (null != v) {
					sb.append(ASSIGNMENT_ELEMENT_SYMBOL_CHAR);
					sb.append(v);
				}
				sb.append(PARAM_ELEMENT_SEPARATOR_CHAR);
			}
		}
		if (sb.length() > 1) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
	private String toStringAsToEncode0() {
		StringBuilder sb = new StringBuilder();
		for (String k : this.values.keySet()) {
			if (null == k) { continue; }

			List<String>   vs = this.getValueList0(k);
			for (String v: vs) {
				sb.append(toEncode(k));
				if (null != v) {
					sb.append(ASSIGNMENT_ELEMENT_SYMBOL_CHAR);
					sb.append(toEncode(v));
				}
				sb.append(PARAM_ELEMENT_SEPARATOR_CHAR);
			}
		}
		if (sb.length() > 1) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
	private String toStringAsToDecode0() {
		StringBuilder sb = new StringBuilder();
		for (String k : this.values.keySet()) {
			if (null == k) { continue; }

			List<String>   vs = this.getValueList0(k);
			for (String v: vs) {
				sb.append(toDecode(k));
				if (null != v) {
					sb.append(ASSIGNMENT_ELEMENT_SYMBOL_CHAR);
					sb.append(toDecode(v));
				}
				sb.append(PARAM_ELEMENT_SEPARATOR_CHAR);
			}
		}
		if (sb.length() > 1) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
	

	public String getOriginalValue(String k) {
		return this.getValue0(k);
	}
	public List<String> getOriginalValues(String k) {
		List<String> cache = this.getValueList0(k);
		List<String> list = new ArrayList<>();
		for (String ce : cache) {
			list.add(ce);
		}
		return list;
	}
	
	
	public String getDecodeValue(String k) {
		return toDecode(this.getValue0(k));
	}
	public List<String> getDecodeValues(String k) {
		List<String> cache = this.getValueList0(k);
		List<String> list = new ArrayList<>();
		for (String ce : cache) {
			list.add(toDecode(ce));
		}
		return list;
	}


	
	











	public HttpURLParam put(String k, Object v){
		return this.put(k, String.valueOf(v));
	}
	public HttpURLParam put(String k, String v) {
		this.setValue0(k, v);
		return this;
	}
	public HttpURLParam put(String k, List<String> v) {
		this.setValueList0(k, null == v ?null: v);
		return this;
	}
	public HttpURLParam putAllValue(Map<String, String> ua) {
		if (null != ua) {
			for (String key : ua.keySet()) {
				String k = key;
				String value = ua.get(k);
				this.setValue0(k, value);
			}
		}
		return this;
	}
	public HttpURLParam putAll(Map<String, List<String>> ua) {
		if (null != ua) {
			for (String key : ua.keySet()) {
				List<String> values = ua.get(key);
				this.setValueList0(key, null == values ? null : values);
			}
		}
		return this;
	}
	public HttpURLParam putAll(HttpURLParam ua) {
		if (null != ua) {
			return this.putAll(ua.getInnerMap());
		}
		return this;
	}




	public HttpURLParam add(String k, Object v){
		return this.add(k, String.valueOf(v));
	}
	public HttpURLParam add(String k, String v) {
		this.addValue0(k, v);
		return this;
	}
	public HttpURLParam addAll(String k, List<String> v) {
		this.addValueList0(k, v);
		return this;
	}
	public HttpURLParam addAllValue(Map<String, String> ua) {
		if (null != ua) {
			for (String key : ua.keySet()) {
				String values = ua.get(key);
				this.addValue0(key, values);
			}
		}
		return this;
	}
	public HttpURLParam addAll(Map<String, List<String>> ua) {
		if (null != ua) {
			for (String key : ua.keySet()) {
				List<String> values = ua.get(key);
				this.addValueList0(key, values);
			}
		}
		return this;
	}
	public HttpURLParam addAll(HttpURLParam ua) {
		if (null != ua) {
			return this.addAll(ua.getInnerMap());
		}
		return this;
	}


	public HttpURLParam remove(String key) {
		this.removeKey0(key);
		return this;
	}


	public boolean containsKey(String key) {
		return this.containsKey0(key);
	}

	public int count(String key) {
		List<String> list = this.getValueList0(key);
		return null == list ? 0 : list.size();
	}

	public boolean containsValue(String key, String value) {
		return containsValue0(key, value);
	}

	public boolean removeValue(String key, String value) {
		return removeValue0(key, value);
	}

	@Override
	public HttpURLParam clone() {
		// TODO: Implement this method
		HttpURLParam p;
		p = new HttpURLParam();
		p.putAll(this);
		return p;
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

	public HttpURLParam clear() {
		this.values.clear();
		return this;
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return toStringAsToEncode0();
	}

	public String toEecodeString() {
		return toStringAsToEncode0();
	}
	public String toDecodeString() {
		return toStringAsToDecode0();
	}
	
	public String toOriginString() {
		return toStringAsToOriginal0();
	}
	
	

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (obj == this) { return true; }
		if (!(obj instanceof HttpURLParam)) { return false; }

		HttpURLParam instance = (HttpURLParam) obj;
		return equals0(instance);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return hashCode0();
	}









	public static Map<String,List<String>> parse(String value) {
		Map<String,List<String>> map = new LinkedHashMap<>();
		if (null == value) { return map; }

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

			String  key, val = null;
			if (assignment == -1) {
				key = substring(value, last, offset);
			} else {
				key = substring(value, last, assignment);
				val = substring(value, assignment + 1, offset);
			}
			List<String> valueList = map.get(key);
			if (null == valueList) {
				map.put(key, valueList = new ArrayList<>());
			}
			valueList.add(val);

			last = offset + 1;// + char length, olny 1
		}
		return map;
	}
	public static String substring(String str, int startIndex, int endIndex) {
		return str.substring(startIndex, endIndex);
	}

}

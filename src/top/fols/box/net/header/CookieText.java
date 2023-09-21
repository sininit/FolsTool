package top.fols.box.net.header;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import top.fols.atri.net.MessageHeader;
import top.fols.atri.interfaces.interfaces.IInnerMap;
import top.fols.atri.interfaces.annotations.Tips;

@SuppressWarnings("SpellCheckingInspection")
public class CookieText implements Serializable, IInnerMap<String, String> {
	private static final long serialVersionUID = 1L;

	public static final char COOKIE_ELEMENT_SEPARATOR_CHAR = ';';
	public static final char COOKIE_ELEMENT_ASSIGNMENT_SYMBOL_CHAR = '=';

	Map<String, String> values;//no use thread...


	public CookieText() {
		values = parse(null, null);
	}
	/**
	 * @param value : "Cookie: tip"
	 */
	public CookieText(String value) {
		values = parse(null, value);
	}
	public CookieText(MessageHeader header) {
		CookieText cookieText = header.getCookies(MessageHeader.REQUEST_HEADER_COOKIE);
		this.putAll(cookieText);
	}







	public String get(String key) {
		return values.get(key);
	}
	public CookieText put(String orginkey, String orginValue) {
		this.values.put(orginkey, orginValue);
		return this;
	}

	public void putAll(CookieText params) {
		if (null == params) { return; }
		this.values.putAll(params.values);
	}



	public CookieText remove(String key) {
		this.values.remove(key);
		return this;
	}


	public boolean containsKey(String key) {
		return this.values.containsKey(key);
	}



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

	public CookieText clear() {
		this.values.clear();
		return this;
	}







	@Tips("direct return, result format:  key=tip; key1=value1...")
	@Override
	public String toString() {
		// TODO: Implement this method
		StringBuilder sb = new StringBuilder();
		for (String k : this.values.keySet()) {
			if (null == k) { continue; }
			sb.append(k);

			String  v = this.values.get(k);
			if (null != v) {
				sb.append(COOKIE_ELEMENT_ASSIGNMENT_SYMBOL_CHAR);
				sb.append(v);
			}

			sb.append(COOKIE_ELEMENT_SEPARATOR_CHAR);
			sb.append(' ');
		}
		if (sb.length() > 2) {
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}


	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (obj == this) { return true; }
		if (!(obj instanceof CookieText)) { return false; }

		CookieText instance =(CookieText) obj;
		return instance.values.equals(values);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return values.hashCode();
	}

	@Override
	public CookieText clone() {
		// TODO: Implement this method
		CookieText p;
		p = new CookieText();
		p.putAll(this);

		return p;
	}





	public static Map<String,String> parse(Map<String,String> put, String value) {
		if (null == put) { put = new LinkedHashMap<>();}
		if (null == value) { return put; }

		int last = 0, offset, length = value.length();
		while (true) {
			if ((offset  = value.indexOf(COOKIE_ELEMENT_SEPARATOR_CHAR, last)) == -1) {
				if (last <   length) {
					offset = length;
				} else {
					break;
				}
			} 
			int assignment = -1;
			if (last < length) {
				for (int i = last; i < offset; i++) {
					if (value.charAt(i) == COOKIE_ELEMENT_ASSIGNMENT_SYMBOL_CHAR) {
						assignment = i;
						break;
					}
				}
			}
			if (assignment == -1) {
				String  element = subtrim(value, last, offset);
				put.put(element, null);
			} else {
				String  key = subtrim(value, last, assignment);
				String  val = subtrim(value, assignment + 1, offset);
				put.put(key, val);
			}

			last = offset + 1;// + char length, olny 1
		}
		return put;
	}
	public static String subtrim(String spec, int start, int limit) {
		while ((limit > 0) && (spec.charAt(limit - 1) <= ' ')) limit--;
		while ((start < limit) && (spec.charAt(start) <= ' ')) start++;
		return spec.substring(start, limit);
	}
}

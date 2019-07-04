package top.fols.box.net;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XStringReader;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.util.XArrays;
import top.fols.box.util.XObjects;

public class XURLConnectionTool {

	@XAnnotations("MessageHeader")
	public static class UA {
		private Map<String,List<String>> uaValues = new HashMap<String,List<String>>();
		private void set0(String k, String v) {
			List<String> newValues = new ArrayList<String>();
			if (null != v) {
				newValues.add(v);
			}
			uaValues.put(k, newValues);
		}
		private void add0(String k, String v) {
			List<String> newValues = uaValues.get(k);
			if (null == newValues)
				uaValues.put(k, newValues = new ArrayList<String>());
			newValues.add(v);
		}

		/*
		 * deal multi line able
		 * ^ for The beginning of a line
		 * 
		 * ^key: value\n
		 * ^key2: value2\n
		 * ^...
		 */
		private static void dealMultiLine0(String ua, boolean putValue, UA m) {
			XStringReader rowStreanm = new XStringReader(ua);
			char lines[];
			char splitchar = ':';
			while (null != (lines = rowStreanm.readLine(XStaticFixedValue.Chars_NextLineN, false))) {
				if (lines.length == 0 || (lines.length >= 1 && lines[0] == XStaticFixedValue.Char_NextLineR)) {
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
		}
		public UA() {
			this((String)null);
		}
		public UA(String ua) {
			if (null != ua)
				this.addAll(ua);
		}
		public UA(String... ua) {
			if (null != ua)
				this.addAll(ua);
		}
		public UA(Map<String, List<String>> ua) {
			if (null != ua)
				this.addAll(ua);
		}




		@XAnnotations("set")
		public UA put(String k, String v) {
			this.set0(k, v);
			return this;
		}
		@XAnnotations("deal multi line able")
		public UA putAll(String multiLineContent) {
			UA.dealMultiLine0(multiLineContent, true, this);
			return this;
		}
		@XAnnotations("deal multi line able")
		public UA putAll(String... multiLineContent) {
			StringBuilder buf = new StringBuilder();
			for (String s:multiLineContent)
				buf.append(s).append(XStaticFixedValue.String_NextLineRN);
			this.putAll(buf.toString());
			buf = null;
			return this;
		}
		public UA putAll(UA ua) {
			if (null == ua) {
				return this;
			}
			return putAll(ua.uaValues);
		}
		public UA putAll(Map<String,List<String>> ua) {
			if (null == ua) {
				return this;
			}
			for (String key: ua.keySet()) {
				String k = key;
				List<String> values = ua.get(k);
				this.uaValues.put(k, null ==  values ?null: new ArrayList<String>(values));
			}
			return this;
		}
		
		
		
		@XAnnotations("add")
		public UA add(String k, String v) {
			this.add0(k, v);
			return this;
		}
		@XAnnotations("deal multi line able")
		public UA addAll(String Content) {
			UA.dealMultiLine0(Content, false, this);
			return this;
		}
		@XAnnotations("deal multi line able")
		public UA addAll(String... Content) {
			StringBuilder buf = new StringBuilder();
			for (String s:Content)
				buf.append(s).append(XStaticFixedValue.String_NextLineRN);
			this.addAll(buf.toString());
			return this;
		}
		public UA addAll(UA ua) {
			if (null == ua) {
				return this;
			}
			return addAll(ua.uaValues);
		}
		public UA addAll(Map<String, List<String>> ua) {
			if (null == ua) {
				return this;
			}
			for (String key: ua.keySet()) {
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
		public String fuzzyFindKey(String key) {
			if (null == key) {
				return null;
			}
			if (this.uaValues.containsKey(key)) {
				return key;
			}
			String sKey = key.trim().toLowerCase(Locale.ENGLISH);
			for (String ki: uaValues.keySet()) {
				if (null != ki && ki.trim().toLowerCase(Locale.ENGLISH).equals(sKey)) {
					return ki;
				}
			}
			return key;
		}
		/*
		 * 寻找忽略大小写获取值
		 */
		public int fuzzyGetInt(String key, int defaultValue) {
			String sk = fuzzyFindKey(key);
			try {
				return Integer.parseInt(sk);
			} catch (Exception e) {
				return defaultValue;
			}
		}
		public long fuzzyGetLong(String key, long defaultValue) {
			String sk = fuzzyFindKey(key);
			try {
				return Long.parseLong(sk);
			} catch (Exception e) {
				return defaultValue;
			}
		}
		public String fuzzyGetString(String key, String defaultValue) {
			String sk = fuzzyFindKey(key);
			if (this.uaValues.containsKey(sk)) {
				return get(sk);
			} else {
				return defaultValue;
			}
		}




		public String get(String k) {
			List<String> newValues = this.uaValues.get(k);
			return null == newValues || newValues.size() == 0 ?null: newValues.get(0);
		}
		public List<String> getAll(String k) {
			List<String> newValues = this.uaValues.get(k);
			return null == newValues ?null: newValues;
		}
		public UA setAll(String k, List<String> list) {
			uaValues.put(k, list);
			return this;
		}
		public Map<String, List<String>> getAll() {
			return this.uaValues;
		}

		public int size(String key) {
			List<String> list = this.uaValues.get(key);
			return list == null ?0: list.size();
		}
		public int size() {
			return this.uaValues.size();
		}



		public List<String> keys() {
			return XObjects.keys(this.uaValues);
		}

		public UA reset() {
			this.uaValues.clear();
			return this;
		}
		public UA remove(String key) {
			this.uaValues.remove(key);
			return this;
		}
		public UA removeValue(String k) {
			this.set0(k, null);
			return this;
		}
		@Override
		public String toString() {
			// TODO: Implement this method
			StringBuilder buf = new StringBuilder();
			for (String k: this.uaValues.keySet()) {
				List<String> vs = getAll(k);
				if (null == vs) {
					buf.append(k).append(':').append(' ').append(XStaticFixedValue.String_NextLineRN);
				} else {
					for (String v: vs) {
						buf.append(k).append(':').append(' ').append(v).append(XStaticFixedValue.String_NextLineRN);
					}
				}
			}
			return buf.toString();
		}
		public boolean containsKey(String key) {
			return this.uaValues.containsKey(key);
		}

		
		/*
		 * 不会设置key为空的字段
		 */
		@XAnnotations("the empty key won't be set")
		public UA setToURLConnection(URLConnection con) {
			for (String k: this.uaValues.keySet()) {
				List<String> vs = this.uaValues.get(k);
				if (XObjects.isEmpty(k)) {
					continue;
				}
				if (null == vs || vs.size() == 0) {
					con.setRequestProperty(k, "");
					continue;
				} else {
					int i = 0;
					for (String vi: vs) {
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
		public UA addToURLConnection(URLConnection con) {
			for (String k: this.uaValues.keySet()) {
				List<String> vs = this.uaValues.get(k);
				if (XObjects.isEmpty(k)) {
					continue;
				}
				if (null == vs) {
					con.addRequestProperty(k, "");
				} else {
					for (String vi: vs) {
						con.addRequestProperty(k, vi);
					}
				}
			}
			return this;
		}
		/*
		 * 删除所有key首尾空
		 */
		public UA toTrimKeyUa() {
			Map<String,List<String>> newUaMap = new HashMap<String,List<String>>();
			for (String key:uaValues.keySet()) {
				String newKey = null == key ?null: key.trim();
				List<String> newValue = uaValues.get(key);

				newUaMap.put(newKey, null == newValue  ?null: new ArrayList<String>(newValue));
			}
			return new UA().putAll(newUaMap);
		}
	}




	public static final int defaultConnectTimeout = 12000;
	public static final int defaultReadTimeout = 6000;
	public static class http {
		private HttpURLConnection con;
		private UA ua = new UA();
		private InputStream in;
		private OutputStream ot;
		private boolean writeHeader = false;
		public http(String url) throws IOException {
			this((HttpURLConnection)new URL(url).openConnection());
		}
		public http(HttpURLConnection httpURLConnection) {
			if (!(httpURLConnection instanceof HttpURLConnection)) {
				throw new RuntimeException("need HttpURLConnection instance, input: " + (null == httpURLConnection ?null: httpURLConnection.getClass().getCanonicalName()));
			}
			this.con = httpURLConnection;
		}
		public URLConnection getURLConnection() {
			return con;
		}
		public http connectTimeout(int time) {
			con.setConnectTimeout(time);
			return this;
		}
		public http readTimeout(int time) {
			con.setReadTimeout(time);
			return this;
		}
		public http ua(String Content) {
			return ua(new UA(Content));
		}
		public http ua(UA a) {
			if (null == a) {
				return this;
			} else {
				this.ua = a;
				return this;
			}
		}
		public UA getUA() {
			return this.ua;
		}

		public InputStream getInputStream() throws IOException {
			return null == in ? in = con.getInputStream(): in;
		}
		public OutputStream getOutputStream() throws IOException {
			return null == ot ? ot = con.getOutputStream(): ot;
		}
		public void disconnect() {
			try {
				con.disconnect();
				getInputStream().close();
				getOutputStream().close();
				in = null;
				ot = null;
			} catch (Exception e) {
				e = null;
			}
		}

		public void setRequestMethod(String method) throws ProtocolException {
			this.con.setRequestMethod(method);
		}
		public http read2(OutputStream backoutput) throws IOException {
			if (null == backoutput)
				return this;
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			XStream.copy(getInputStream(), backoutput);
			return this;
		}
		public http write(byte[] b) throws IOException {
			return write(b, 0, b.length);
		}
		public http write(byte[] b, int off, int len) throws IOException {
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			OutputStream stream = getOutputStream();
			stream.write(b, off, len);
			return this;
		}
		public http write(InputStream backoutput) throws IOException {
			if (null == backoutput)
				return this;
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			XStream.copy(backoutput, getOutputStream());
			return this;
		}
		public http write(InputStream backoutput, long length) throws IOException {
			if (null == backoutput)
				return this;
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			XStream.copy(new XInputStreamFixedLength<InputStream>(backoutput, length), getOutputStream());
			return this;
		}



		public String toString() {
			return toString(null);
		}
		public String toString(String encoding) {
			XByteArrayOutputStream bytearrout = new XByteArrayOutputStream();
			try {
				read2(bytearrout);
				if (null == encoding)
					return bytearrout.toString();
				return bytearrout.toString(encoding);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		public byte[] toBytes() {
			XByteArrayOutputStream bytearrout = new XByteArrayOutputStream();
			try {
				read2(bytearrout);
				byte[] bs = bytearrout.toByteArray();
				bytearrout.releaseBuffer();
				return bs;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	public static class get {
		private URLConnection con;
		private UA ua;
		private InputStream in;
		private OutputStream ot;
		private boolean writeHeader = false;

		public get(String url) throws IOException {
			this(new URL(url).openConnection());
		}
		public get(URLConnection con) {
			this.ua = new UA();
			this.con = XObjects.requireNonNull(con);
		}
		public URLConnection getURLConnection() {
			return con;
		}
		public get connectTimeout(int time) {
			con.setConnectTimeout(time);
			return this;
		}
		public get readTimeout(int time) {
			con.setReadTimeout(time);
			return this;
		}
		public get ua(String Content) {
			return ua(new UA(Content));
		}
		public get ua(UA a) {
			if (null == a) {
				return this;
			} else {
				this.ua = a;
				return this;
			}
		}
		public UA getUA() {
			return ua;
		}
		public InputStream getInputStream() throws IOException {
			return null == in ? in = con.getInputStream(): in;

		}
		public OutputStream getOutputStream() throws IOException {
			return null == ot ? ot = con.getOutputStream(): ot;
		}

		public void disconnect() {
			try {
				if (con instanceof HttpURLConnection)
					((HttpURLConnection)con).disconnect();
				getInputStream().close();
				getOutputStream().close();
				in = null;
				ot = null;
			} catch (Exception e) {
				e = null;
			}
		}


		public get read2(OutputStream BackOutput) throws IOException {
			if (null == BackOutput)
				return this;
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			XStream.copy(getInputStream(), BackOutput);
			return this;
		}


		public String toString() {
			return toString(null);
		}
		public String toString(String encoding) {
			XByteArrayOutputStream bytearrout = new XByteArrayOutputStream();
			try {
				read2(bytearrout);
				if (null == encoding)
					return bytearrout.toString();
				return bytearrout.toString(encoding);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		public byte[] toBytes() {
			XByteArrayOutputStream bytearrout = new XByteArrayOutputStream();
			try {
				read2(bytearrout);
				byte[] bs = bytearrout.toByteArray();
				bytearrout.releaseBuffer();
				return bs;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}





	public static class post {
		private URLConnection con;
		private UA ua;
		private InputStream in;
		private OutputStream ot;
		private boolean writeHeader = false;

		public post(String url) throws IOException {
			this(new URL(url).openConnection());
		}
		public post(URLConnection con) {
			this.ua = new UA();
			this.con = XObjects.requireNonNull(con);
			this.con.setDoOutput(true);
			this.con.setDoInput(true);
			try {
				if (con instanceof HttpURLConnection)
					((HttpURLConnection)con).setRequestMethod("POST");
			} catch (ProtocolException e) {
				throw new RuntimeException(e);
			}
		}
		public URLConnection getURLConnection() {
			return con;
		}
		public post connectTimeout(int time) {
			con.setConnectTimeout(time);
			return this;
		}
		public post readTimeout(int time) {
			con.setReadTimeout(time);
			return this;
		}
		public post ua(String Content) {
			return ua(new UA(Content));
		}
		public post ua(UA a) {
			if (null == a)
				return this;
			this.ua = a;
			return this;
		}
		public UA getUA() {
			return ua;
		}

		public InputStream getInputStream() throws IOException {
			return null == in ? in = con.getInputStream(): in;

		}
		public OutputStream getOutputStream() throws IOException {
			return null == ot ? ot = con.getOutputStream(): ot;
		}
		public post write(byte[] b) throws IOException {
			return write(b, 0, b.length);
		}

		public post write(byte[] b, int off, int len) throws IOException {
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			OutputStream stream = getOutputStream();
			stream.write(b, off, len);
			return this;
		}
		public post write(InputStream BackOutput) throws IOException {
			if (null == BackOutput)
				return this;
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			XStream.copy(BackOutput, getOutputStream());
			return this;
		}
		public post write(InputStream BackOutput, long length) throws IOException {
			if (null == BackOutput)
				return this;
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			XStream.copy(new XInputStreamFixedLength<InputStream>(BackOutput, length), getOutputStream());
			return this;
		}
		public void disconnect() {
			try {
				if (con instanceof HttpURLConnection)
					((HttpURLConnection)con).disconnect();
				getInputStream().close();
				getOutputStream().close();
				in = null;
				ot = null;
			} catch (Exception e) {
				e = null;
			}
		}
		public post read2(OutputStream BackOutput) throws IOException {
			if (null == BackOutput)
				return this;
			if (!writeHeader) {
				ua.setToURLConnection(con);
				writeHeader = true;
			}
			XStream.copy(getInputStream(), BackOutput);
			return this;
		}




		public String toString() {
			return toString(null);
		}
		public String toString(String encoding) {
			XByteArrayOutputStream bytearrout = new XByteArrayOutputStream();
			try {
				read2(bytearrout);
				if (null == encoding)
					return bytearrout.toString();
				return bytearrout.toString(encoding);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public byte[] toBytes() {
			XByteArrayOutputStream bytearrout = new XByteArrayOutputStream();
			try {
				read2(bytearrout);
				byte[] bs = bytearrout.toByteArray();
				bytearrout.releaseBuffer();
				return bs;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}









}

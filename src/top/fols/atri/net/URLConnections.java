package top.fols.atri.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.time.XTimeTool;

public class URLConnections {

	public static final String HTTP_REQUEST_METHOD_GET 		= "GET";
	public static final String HTTP_REQUEST_METHOD_POST 	= "POST";
	public static final String HTTP_REQUEST_METHOD_HEAD 	= "HEAD";
	public static final String HTTP_REQUEST_METHOD_OPTIONS 	= "OPTIONS";
	public static final String HTTP_REQUEST_METHOD_PUT 		= "PUT";
	public static final String HTTP_REQUEST_METHOD_DELETE 	= "DELETE";
	public static final String HTTP_REQUEST_METHOD_TRACE 	= "TRACE";

	public static final int DEFAULT_CONNECT_TIMEOUT = 12 * (int)XTimeTool.time_1s;
	public static final int DEFAULT_READ_TIMEOUT 	= 6 *  (int)XTimeTool.time_1s;

	public static class URLConnectionUtil implements Closeable {

		private URLConnection con;
		private InputStream in;
		private OutputStream ot;

		public boolean isHttpURLConnection() {
			return this.con instanceof HttpURLConnection;
		}

		public boolean isJarURLConnection() {
			return this.con instanceof JarURLConnection;
		}

		public URLConnectionUtil(String url) throws IOException {
			this(new URL(url).openConnection());
		}

		public URLConnectionUtil(URLConnection urlConnection) {
			this.con = urlConnection;
		}

		public URLConnection getURLConnection() {
			return this.con;
		}

		public URLConnectionUtil connectTimeout(int time) {
			this.con.setConnectTimeout(time);
			return this;
		}

		public URLConnectionUtil readTimeout(int time) {
			this.con.setReadTimeout(time);
			return this;
		}



		public URLConnectionUtil messageHeader(String Content) {
			return this.messageHeader(new MessageHeader(Content));
		}
		public URLConnectionUtil messageHeader(MessageHeader a) {
			a.setToURLConnection(this.con);
			return this;
		}

		public InputStream getInputStream() throws IOException {
			return null == this.in ? this.in = this.con.getInputStream() : this.in;
		}

		public OutputStream getOutputStream() throws IOException {
			return null == this.ot ? this.ot = this.con.getOutputStream() : this.ot;
		}

		public void disconnect() {
			try {
				this.getInputStream().close();
				this.in = null;
			} catch (Throwable e) {
				e = null;
			}
			try {
				this.getOutputStream().close();
				this.ot = null;
			} catch (Throwable e) {
				e = null;
			}
		}

		@Override
		public void close() {
			// TODO: Implement this method
			this.disconnect();
		}

		public int available() throws java.io.IOException {
			return this.getInputStream().available();
		}

		public URLConnectionUtil readTo(OutputStream backoutput) throws IOException {
			XStream.copy(this.getInputStream(), backoutput);
			return this;
		}

		public int read() throws java.io.IOException {
			return this.getInputStream().read();
		}

		public int read(byte[] b) throws java.io.IOException {
			return this.read(b, 0, b.length);
		}

		public int read(byte[] b, int off, int len) throws java.io.IOException {
			return this.getInputStream().read(b, off, len);
		}

		public URLConnectionUtil write(int b) throws IOException {
			this.getOutputStream().write(b);
			return this;
		}

		public URLConnectionUtil write(byte[] b) throws IOException {
			return this.write(b, 0, b.length);
		}

		public URLConnectionUtil write(byte[] b, int off, int len) throws IOException {
			OutputStream stream = this.getOutputStream();
			stream.write(b, off, len);
			return this;
		}

		public URLConnectionUtil write(InputStream stream) throws IOException {
			XStream.copy(stream, this.getOutputStream());
			return this;
		}

		public URLConnectionUtil write(InputStream stream, long length) throws IOException {
			XStream.copy(new XInputStreamFixedLength<>(stream, length), this.getOutputStream());
			return this;
		}

		public URLConnectionUtil flush() throws IOException {
			this.getOutputStream().flush();
			return this;
		}

		public String toString() 				{ return this.readString(); }
		public String toString(String encoding) { return this.readString(encoding); }





		public byte[] readBytes() {
			XByteArrayOutputStream bytearrout = new XByteArrayOutputStream();
			try {
				this.readTo(bytearrout);
				byte[] bs = bytearrout.toByteArray();
				bytearrout.releaseBuffer();
				return bs;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


		public String readString() {
			return this.readString(null);
		}
		public String readString(String encoding) {
			XByteArrayOutputStream stream = new XByteArrayOutputStream();
			try {
				this.readTo(stream);
				return null == encoding ? stream.toString() : stream.toString(encoding);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


	}




	public static class HttpURLConnectionUtil extends URLConnectionUtil {
		private HttpURLConnection con;

		public HttpURLConnectionUtil(String httpUrl) throws IOException {
			this((HttpURLConnection) new URL(httpUrl).openConnection());
		}

		public HttpURLConnectionUtil(HttpURLConnection httpURLConnection) {
			super(httpURLConnection);
			this.con = httpURLConnection;
		}

		@Override
		public HttpURLConnection getURLConnection() {
			return this.con;
		}

		public void setRequestMethod(String method) throws ProtocolException {
			this.con.setRequestMethod(method);
		}
	}

	public static class GetRequest extends HttpURLConnectionUtil {
		public GetRequest(String httpUrl) throws IOException {
			this((HttpURLConnection) new URL(httpUrl).openConnection());
		}

		public GetRequest(HttpURLConnection con) throws ProtocolException {
			super(con);
			super.setRequestMethod(URLConnections.HTTP_REQUEST_METHOD_GET);
		}
	}

	public static class PostRequest extends HttpURLConnectionUtil {
		public PostRequest(String httpUrl) throws IOException {
			this((HttpURLConnection) new URL(httpUrl).openConnection());
		}

		public PostRequest(HttpURLConnection con) throws ProtocolException {
			super(con);
			super.setRequestMethod(URLConnections.HTTP_REQUEST_METHOD_POST);
			super.con.setDoInput(true);
			super.con.setDoOutput(true);
		}
	}

}

package top.fols.box.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import top.fols.atri.io.CharSeparatorReader;
import top.fols.atri.io.StreamList;
import top.fols.atri.io.Streams;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.atri.lock.Locks;
import top.fols.atri.net.Cookies;
import top.fols.atri.net.MessageHeader;
import top.fols.atri.net.URLBuilder;
import top.fols.atri.net.URLConnections;
import top.fols.atri.net.XURL;
import top.fols.atri.util.BlurryKey;
import top.fols.atri.util.TabPrint;
import top.fols.atri.util.Throwables;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.net.header.ContentType;

/**
 * Test
 */
public class Requests {

    /**
     * Directly simulate the sending of complete HTTP network packets. The complete packets must include:
     *
     *----------------------------
     * METHOD FULL_URL HTTP_VERSION
     * HTTP_HEADER
     * HTTP_HEADER
     * HTTP_HEADER
     *
     * REQUEST_DATA
     *----------------------------
     *
     * example:
     * request("GET https://fanyi.baidu.com/#zh/en/%E8%87%AA%E5%AE%9A%E4%B9%89%E8%BE%93%E5%87%BA HTTP/1.1\n" +
     *         "Host: fanyi.baidu.com\n")
     *
     *
     * request("POST https://fanyi.baidu.com/#zh/en/%E8%87%AA%E5%AE%9A%E4%B9%89%E8%BE%93%E5%87%BA HTTP/1.1\n" +
     *         "Host: fanyi.baidu.com\n"+
     *         "\r\n"+
     *         "POST_DATA")
     */
    public static String request(String requestSource) throws IOException {
        Request request = createRequest(requestSource);
        return   request.readString();
    }


    public static Request createRequest(
            String httpRequestDataPacket) throws IOException {
        CharSeparatorReader reader = new CharSeparatorReader(httpRequestDataPacket);

        String first = reader.next();
        String method;
        String url;

        try {
            method = first.split("\\s+")[0];
            url    = first.split("\\s+")[1];
        } catch (Throwable e) {
            method = URLConnections.HTTP_REQUEST_METHOD_GET;
            url = first;
        }

        StringBuilder headers = new StringBuilder();
        String line;
        while (reader.hasNext()) {
            line = reader.next(true);
            if (line.length() == reader.separatorSize()) {
                break;
            }
            headers.append(line);
        }

        int position = Math.min(httpRequestDataPacket.length(), reader.position());
        String data = httpRequestDataPacket.substring(position, httpRequestDataPacket.length());
        return createRequest(method, url, headers.toString(), data);
    }

    public static Request createRequest(
            String method, String url,
            String headers,
            String write) throws IOException {

        MessageHeader header = new MessageHeader(headers);
        return createRequest(method, url, header, write, (int) TimeUnit.SECONDS.toMillis(60), (int) TimeUnit.SECONDS.toMillis(60));
    }
    public static Request createRequest(
            String method, String url,
            MessageHeader header,
            String write,

            int connectionOvertime,
            int readStreamOvertime) throws IOException {

        header.remove((BlurryKey.IgnoreCaseKey<String>) null);
        header.put(MessageHeader.REQUEST_HEADER_ACCEPT_ENCODING, MessageHeader.REQUEST_HEADER_VALUE_ACCEPT_ENCODING_IDENTITY);


        String charset = Finals.Charsets.defaultCharset().name();
        if (!write.isEmpty())  {
            ContentType request_content_type = ContentType.parse(header.get(MessageHeader.RESPONSE_HEADER_CONTENT_TYPE));
            if (request_content_type.hasCharset()) {
                charset = request_content_type.getCharset();
            } else {
                request_content_type.setCharset(charset);
                header.put(MessageHeader.RESPONSE_HEADER_CONTENT_TYPE, request_content_type.toString());
            }
        }
        byte[] dataBytes = write.getBytes(charset);

        String content_length = header.get(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH);
        if (null != content_length) {
            header.put(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH, String.valueOf(dataBytes.length));
        }


        XURL xurl = new XURL(url);
        URLBuilder xurlBuilder = xurl.toBuilder();
        if  (Objects.empty(xurl.getProtocol())) {
            xurlBuilder.protocol("http");
        }
        if (Objects.empty(xurl.getHost())) {
            if (header.containsKey(MessageHeader.REQUEST_HEADER_HOST)) {
                String host = header.get(MessageHeader.REQUEST_HEADER_HOST);
                int pi;
                if ((pi = host.indexOf(XURL.PORT_SYMBOL)) > -1) {
                    xurlBuilder.host(host.substring(0, pi));
                    xurlBuilder.port(host.substring(pi + XURL.PORT_SYMBOL.length(), host.length()));
                }  else {
                    xurlBuilder.host(host);
                }
            }
        }
        xurl = new XURL(xurlBuilder.build());
        if (Objects.empty(header.get(MessageHeader.REQUEST_HEADER_HOST))) {
            header.put(MessageHeader.REQUEST_HEADER_HOST, xurl.getHostAndPort());
        }


        URLConnections.HttpURLConnectionUtil request = new URLConnections.HttpURLConnectionUtil(xurl.getUrl());
        request.connectTimeout(connectionOvertime);
        request.readTimeout(readStreamOvertime);
        request.setRequestMethod(method);
        request.messageHeader(header);

        request.getURLConnection().setDoInput(true);

        if (write.length() > 0) {
            request.getURLConnection().setDoOutput(true);
            request.write(dataBytes);
            request.flush();
        }

        @SuppressWarnings("UnnecessaryLocalVariable")
        Request response = new Request(request, method, url, header);
        return   response;
    }






    /**
     *
     * Data will not be submitted
     *
     *
     * FORMAT:
     * METHOD URL HTTP_VERSION
     * HTTP_HEADER
     * HTTP_HEADER
     * HTTP_HEADER
     */
    public static Request createCustomOutputRequest(
            String httpRequestDataPacket,
            Long contentLength) throws IOException {
        CharSeparatorReader reader = new CharSeparatorReader(httpRequestDataPacket);

        String first = reader.next();
        String method;
        String url;

        try {
            method = first.split("\\s+")[0];
            url    = first.split("\\s+")[1];
        } catch (Throwable e) {
            throw new IOException("cannot parse head first line: " + first);
        }

        StringBuilder headers = new StringBuilder();
        String line;
        while (reader.hasNext()) {
            line = reader.next(true);
            if (line.length() == reader.separatorSize()) {
                break;
            }
            headers.append(line);
        }
        return createCustomOutputRequest(method, url, headers.toString(), contentLength);
    }

    public static Request createCustomOutputRequest(
            String method, String url,
            String headers,
            Long contentLength) throws IOException {

        MessageHeader header = new MessageHeader(headers);
        return createCustomOutputRequest(method, url, header, contentLength, (int) TimeUnit.SECONDS.toMillis(60), (int) TimeUnit.SECONDS.toMillis(60));
    }
    public static Request createCustomOutputRequest(
            String method, String url,
            MessageHeader header,
            Long contentLength,

            int connectionOvertime,
            int readStreamOvertime) throws IOException {

        header.remove((BlurryKey.IgnoreCaseKey<String>) null);
        header.put(MessageHeader.REQUEST_HEADER_ACCEPT_ENCODING, MessageHeader.REQUEST_HEADER_VALUE_ACCEPT_ENCODING_IDENTITY);




        String content_length = header.get(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH);
        if (null != contentLength) {
            if (null != content_length) {
                if (contentLength > 0) {
                    header.put(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH, String.valueOf(contentLength));
                } else {
                    header.remove(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH);
                }
            }
        } else {
            header.remove(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH);
        }


        XURL xurl = new XURL(url);
        URLBuilder xurlBuilder = xurl.toBuilder();
        if  (Objects.empty(xurl.getProtocol())) {
            xurlBuilder.protocol("http");
        }
        if (Objects.empty(xurl.getHost())) {
            if (header.containsKey(MessageHeader.REQUEST_HEADER_HOST)) {
                String host = header.get(MessageHeader.REQUEST_HEADER_HOST);
                int pi;
                if ((pi = host.indexOf(XURL.PORT_SYMBOL)) > -1) {
                    xurlBuilder.host(host.substring(0, pi));
                    xurlBuilder.port(host.substring(pi + XURL.PORT_SYMBOL.length(), host.length()));
                }  else {
                    xurlBuilder.host(host);
                }
            }
        }
        xurl = new XURL(xurlBuilder.build());
        if (Objects.empty(header.get(MessageHeader.REQUEST_HEADER_HOST))) {
            header.put(MessageHeader.REQUEST_HEADER_HOST, xurl.getHostAndPort());
        }



        URLConnections.HttpURLConnectionUtil request = new URLConnections.HttpURLConnectionUtil(xurl.getUrl());
        request.connectTimeout(connectionOvertime);
        request.readTimeout(readStreamOvertime);
        request.setRequestMethod(method);
        request.messageHeader(header);


        request.getURLConnection().setDoInput(true);

        try {
            request.getURLConnection().setDoOutput(true);
        } catch (Throwable ignored) {}


        @SuppressWarnings("UnnecessaryLocalVariable")
        Request response = new Request(request, method, url, header);
        return   response;
    }























    public static class Request implements Closeable {
        String method; String url;
        URLConnections.HttpURLConnectionUtil request;




        //		public static Request wrap(URLConnections.HttpURLConnectionUtil request) {
//			return wrap(request, null);
//		}
        public static Request wrap(URLConnections.HttpURLConnectionUtil request) {
            String method = request.getRequestMethod();
            String url = request.getURLConnection().getURL().toExternalForm();
            MessageHeader header = request.getResponseHeader();

            @SuppressWarnings("UnnecessaryLocalVariable")
            Request response = new Request(request, method, url, header);
            return response;
        }
        Request(URLConnections.HttpURLConnectionUtil request,
                String method, String url,
                MessageHeader requestHeader) {

            this.method = method;
            this.url    = url;

            this.request 	   = request;
            this.requestHeader = requestHeader;
//            this.requestData   = requestData;
        }






        @Override
        public void close() {
            // TODO: Implement this method
            Streams.close(request);
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
        public Request readTo(OutputStream backoutput) throws IOException {
            Streams.copy(this.getInputStream(), backoutput);
            return this;
        }


        public Request write(int b) throws IOException {
            this.getOutputStream().write(b);
            return this;
        }
        public Request write(byte[] b) throws IOException {
            return this.write(b, 0, b.length);
        }
        public Request write(byte[] b, int off, int len) throws IOException {
            OutputStream stream = this.getOutputStream();
            stream.write(b, off, len);
            return this;
        }
        public Request write(InputStream stream) throws IOException {
            Streams.copy(stream, this.getOutputStream());
            return this;
        }
        public Request write(InputStream stream, long length) throws IOException {
            Streams.copy(new XInputStreamFixedLength<>(stream, length), this.getOutputStream());
            return this;
        }
        public Request flush() throws IOException {
            this.getOutputStream().flush();
            return this;
        }






        public URLConnections.HttpURLConnectionUtil request() {
            return this.request;
        }



        public String  getRequestMethod() {
            return method;
        }

        public String  getRequestUrl() {
            return url;
        }

        MessageHeader requestHeader;
        public MessageHeader getRequestHeader() {
            return this.requestHeader;
        }

//        String requestData;
//        public String getRequestData() {
//            return this.requestData;
//        }



        public HttpURLConnection getURLConnection() {
            return request.getURLConnection();
        }


        public void connect() throws IOException {
            getURLConnection().connect();
            try {
                getInputStream();
            } catch (Throwable ignored) {}
        }


        public int    code() {
            try {
                return getURLConnection().getResponseCode();
            } catch (IOException e) {
                return 404;
            }
        }
        public String message() {
            try {
                return getURLConnection().getResponseMessage();
            } catch (IOException e) {
                return "";
            }
        }

        public boolean isResponse() {
            int code = code();
            return code >= 200 && code <= 300;
        }








        public Request clone(String url) throws IOException {
            url = Objects.requireNonNull(url, "url");


            MessageHeader requestHeader = new MessageHeader().putAll(this.getRequestHeader());
            Cookies requestCookies = requestHeader.getCookies(MessageHeader.REQUEST_HEADER_COOKIE);


            MessageHeader responseHeader = getResponseHeader();
            Cookies responseCookie = responseHeader.getCookies();

            //if set Expires = -1 ...
            for (String key: responseCookie.keySet()) {
                requestCookies.put(key, responseCookie.get(key));
            }

            URLConnections.HttpURLConnectionUtil request = new URLConnections.HttpURLConnectionUtil(url);
            request.setRequestMethod(this.request().getRequestMethod());
            request.setInstanceFollowRedirects(this.request().getInstanceFollowRedirects());
            request.messageHeader(requestHeader);
            return wrap(request);
        }







        MessageHeader responseHeader;
        public MessageHeader getResponseHeader() {
            MessageHeader h = this.responseHeader;
            if (null == h) {
                this.responseHeader = h = new MessageHeader(getURLConnection().getHeaderFields());
            }
            return h;
        }
        public ContentType 	getResponseType() {
            return ContentType.parse(getResponseHeader().get(MessageHeader.RESPONSE_HEADER_CONTENT_TYPE));
        }
        public Charset 		getResponseCharset() {
            ContentType   response_content_type = this.getResponseType();
            Charset       response_charset      = MessageHeader.HTTP_MESSAGE_HEADER_CHARSET_ISO_8859_1;
            if (response_content_type.hasCharset()) {
                response_charset = Charset.forName(response_content_type.getCharset());
            }
            return response_charset;
        }
        public Long getResponseLength() {
            Long length = null;
            try {
                length = Long.parseLong(getResponseHeader().get(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH));
            } catch (Throwable ignored) {}
            return length;
        }

        byte[] data;
        public byte[] readBytes() {
            byte[] data = this.data;
            if (null == data) {
                this.data = data = request.readBytes();
            }
            return data;
        }
        public String readString() {
            return new String(readBytes(), getResponseCharset());
        }
        public String readString(Charset charset) {
            return new String(readBytes(), charset);
        }


        public InputStream getInputStream() throws IOException {
            return getURLConnection().getInputStream();
        }
        public OutputStream getOutputStream() throws IOException {
            return getURLConnection().getOutputStream();
        }
    }













    protected StreamList streams = new StreamList();

    Requests() {}

    String url;
    public static Requests on(String http) {
        Requests requests;
        requests = new Requests();
        requests.url = Objects.requireNonNull(http, "url");
        return requests;
    }
    public String url() {
        return this.url;
    }

    String method;
    public String method() {
        return null == this.method ?this.method = URLConnections.HTTP_REQUEST_METHOD_GET: this.method;
    }
    public Requests method(String method) {
        this.method = method;
        return this;
    }


    MessageHeader header;
    public Requests headers(String headers) {
        return null == headers ?headers((MessageHeader)null): headers(new MessageHeader(headers));
    }
    public Requests headers(MessageHeader headers) {
        MessageHeader header;
        if (null == headers) {
            header = null;
        } else {
            header = headers;
        }
        this.header = header;
        return this;
    }
    public MessageHeader headers() {
        return null == this.header ? this.header = new MessageHeader(): this.header;
    }
    public String headersString() {
        return null == this.header ? "": this.header.toHeaderString();
    }
    public boolean emptyHeaders() {
        return null == this.header || this.headers().size() == 0;
    }
    public Requests addHeader(String k, String v) {
        this.headers().add(k, v);
        return this;
    }
    public Requests addHeaders(String lines) {
        this.headers().addAll(lines);
        return this;
    }
    public Requests removeHeader(String k) {
        this.headers().remove(k);
        return this;
    }
    public boolean hasHeader(String k) {
        return this.headers().containsKey(k);
    }



    Integer connectTimeout;
    public Requests connectTimeout(int time) {
        this.connectTimeout = time;
        return this;
    }
    public int connectTimeout() {
        return null == this.connectTimeout ?this.connectTimeout = (int) TimeUnit.SECONDS.toMillis(60): this.connectTimeout;
    }

    Integer readTimeout;
    public Requests readTimeout(int time) {
        this.readTimeout = time;
        return this;
    }
    public int readTimeout() {
        return null == this.readTimeout ?this.readTimeout = (int) TimeUnit.SECONDS.toMillis(60): this.readTimeout;
    }


    byte[] data;
    Charset charset;



    public Requests data(byte[] data) {
        this.data = data;
        this.charset = null;
        return this;
    }
    public Requests data(String data) {
        this.data = data.getBytes();
        this.charset = Finals.Charsets.defaultCharset();
        return this;
    }



    public Requests onEnd(Objects.CallbackValue<Requests> callback) {
        this.end = callback;
        return this;
    }
    Objects.CallbackValue<Requests> 	end;

    @Override
    public String toString() {
        // TODO: Implement this method
        TabPrint tp = new TabPrint();
        tp.tag = getClass().getName();
        tp.add("request", this.started());
        tp.add("method", this.method());
        tp.add("url", this.url());
        tp.add("headers", TabPrint.wrap(this.headers()));
        tp.add("data",  null == data ?null: data.length);
        tp.add("charset",  charset);
        tp.add("error", Throwables.toStrings(ex));
        return tp.toString();
    }





    final Object lock = new Object();


    Throwable ex;
    Request  request;
    public Throwable error() {
        return this.ex;
    }
    public Request  request() {
        return this.request;
    }
    public Requests start() {
        synchronized (lock) {
            if (null == this.threadLock) {
                try {
                    this.streams.close();
                    this.ex = null;
                    this.request = null;
                    this.threadLock = new Locks();

                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                if (null != data)  {
                                    if (null != Requests.this.charset) {
                                        String charset = Requests.this.charset.name();
                                        ContentType request_content_type = ContentType.parse(header.get(MessageHeader.RESPONSE_HEADER_CONTENT_TYPE));
                                        if (!request_content_type.hasCharset()) {
                                            request_content_type.setCharset(charset);
                                            header.put(MessageHeader.RESPONSE_HEADER_CONTENT_TYPE, request_content_type.toString());
                                        }
                                    }
                                }




                                byte[] dataBytes = data;
                                Requests.this.request = streams.add(createCustomOutputRequest(
                                        Requests.this.method(),
                                        Requests.this.url(),
                                        Requests.this.headers(),
                                        (long) (null == dataBytes ?0: dataBytes.length),
                                        Requests.this.connectTimeout(),
                                        Requests.this.readTimeout()));
                                if (null != dataBytes && dataBytes.length > 0) {
                                    request.write(dataBytes);
                                    request.flush();
                                }
                                request.connect();
                            } catch (Throwable e) {
                                ex = e;
                            }
                            try {
                                try {
                                    if (null != end) {
                                        end.callback(Requests.this);
                                    }
                                } catch (Throwable ignored) {}
                            } finally {
                                synchronized (cleanLock) {
                                    try {
                                        clean();
                                    } finally {
                                        threadLock.unlock();
                                        threadLock = null;
                                    }
                                }
                            }

                        }
                    }.start();
                } catch (Throwable e) {
                    this.threadLock = null;
                    throw new InternalError(e);
                }
            } else {
                Objects.requireTrue(false, "started");
            }
            return this;
        }
    }
    public Requests stop() {
        synchronized (lock) {
            if (null != this.threadLock) {
                this.clean();
            }
            return this;
        }
    }
    public Requests await() throws InterruptedException {
        synchronized (lock) {
            Locks locks = this.threadLock;
            if (null != locks) {
                this.threadLock.await();
            }
            return this;
        }
    }

    public boolean started() {
        synchronized (lock) {
            return null != this.threadLock;
        }
    }

    Locks threadLock;
    final Object cleanLock = new Object();
    public Requests clean() {
        synchronized (cleanLock) {
            if (null != this.threadLock) {
                return this;
            }
            streams.close();
            return this;
        }
    }

}





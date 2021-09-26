package top.fols.box.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import top.fols.atri.io.CharSeparatorReader;
import top.fols.atri.io.Streams;
import top.fols.atri.net.MessageHeader;
import top.fols.atri.net.URLBuilder;
import top.fols.atri.net.URLConnections;
import top.fols.atri.net.XURL;
import top.fols.atri.util.BlurryKey;
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
        Response request = createRequest(requestSource);
        return   request.readString();
    }


    public static Response createRequest(
            String httpRequestDataPacket) throws IOException {
        CharSeparatorReader reader = new CharSeparatorReader(httpRequestDataPacket);

        String first = reader.next();
        String method;
        String url;

        try {
            method = first.split("\\s+")[0];
            url    = first.split("\\s+")[1];
        } catch (Throwable e){
            throw new IOException("cannot parse head first line: "+first);
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

    public static Response createRequest(
            String method, String url,
            String headers,
            String write) throws IOException {

        MessageHeader header = new MessageHeader(headers);
        return createRequest(method, url, header, write, (int) TimeUnit.SECONDS.toMillis(60), (int) TimeUnit.SECONDS.toMillis(60));
    }
    public static Response createRequest(
            String method, String url,
            MessageHeader header,
            String write,

            int connectionOvertime,
            int readStreamOvertime) throws IOException {

        header.remove((BlurryKey.IgnoreCaseKey<String>) null);
        header.put(MessageHeader.REQUEST_HEADER_ACCEPT_ENCODING, MessageHeader.REQUEST_HEADER_VALUE_ACCEPT_ENCODING_IDENTITY);

        String charset = Charset.defaultCharset().name();
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
        if  (xurl.getProtocol() == null) {
            URLBuilder urlBuilder;
            urlBuilder = xurl.toBuilder();
            urlBuilder.protocol("http");
            urlBuilder.host(header.get(MessageHeader.REQUEST_HEADER_HOST));
            xurl = new XURL(urlBuilder.build());
        } else {
            if (!header.containsKey(MessageHeader.REQUEST_HEADER_HOST)) {
                header.put(MessageHeader.REQUEST_HEADER_HOST, xurl.getHostAndPort());
            }
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
        Response response = new Response(request, method, url, header, write);
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
    public static Response createCustomOutputRequest(
            String httpRequestDataPacket,
            long contentLength) throws IOException {
        CharSeparatorReader reader = new CharSeparatorReader(httpRequestDataPacket);

        String first = reader.next();
        String method;
        String url;

        try {
            method = first.split("\\s+")[0];
            url    = first.split("\\s+")[1];
        } catch (Throwable e){
            throw new IOException("cannot parse head first line: "+first);
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

    public static Response createCustomOutputRequest(
            String method, String url,
            String headers,
            long contentLength) throws IOException {

        MessageHeader header = new MessageHeader(headers);
        return createCustomOutputRequest(method, url, header, contentLength, (int) TimeUnit.SECONDS.toMillis(60), (int) TimeUnit.SECONDS.toMillis(60));
    }
    public static Response createCustomOutputRequest(
            String method, String url,
            MessageHeader header,
            long contentLength,

            int connectionOvertime,
            int readStreamOvertime) throws IOException {

        header.remove((BlurryKey.IgnoreCaseKey<String>) null);
        header.put(MessageHeader.REQUEST_HEADER_ACCEPT_ENCODING, MessageHeader.REQUEST_HEADER_VALUE_ACCEPT_ENCODING_IDENTITY);

        String content_length = header.get(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH);
        if (null != content_length) {
            if (contentLength > 0) {
                header.put(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH, String.valueOf(contentLength));
            } else {
                header.remove(MessageHeader.RESPONSE_HEADER_CONTENT_LENGTH);
            }
        }

        XURL xurl = new XURL(url);
        if  (xurl.getProtocol() == null) {
            URLBuilder urlBuilder;
            urlBuilder = xurl.toBuilder();
            urlBuilder.protocol("http");
            urlBuilder.host(header.get(MessageHeader.REQUEST_HEADER_HOST));
            xurl = new XURL(urlBuilder.build());
        } else {
            if (!header.containsKey(MessageHeader.REQUEST_HEADER_HOST)) {
                header.put(MessageHeader.REQUEST_HEADER_HOST, xurl.getHostAndPort());
            }
        }

        URLConnections.HttpURLConnectionUtil request = new URLConnections.HttpURLConnectionUtil(xurl.getUrl());
        request.connectTimeout(connectionOvertime);
        request.readTimeout(readStreamOvertime);
        request.setRequestMethod(method);
        request.messageHeader(header);

        request.getURLConnection().setDoInput(true);

        if (contentLength > 0) {
            request.getURLConnection().setDoOutput(true);
        }

        @SuppressWarnings("UnnecessaryLocalVariable")
        Response response = new Response(request, method, url, header, null);
        return   response;
    }























    public static class Response {
        String method; String url;
        URLConnections.HttpURLConnectionUtil request;


        Response(URLConnections.HttpURLConnectionUtil request,
                 String method, String url,
                 MessageHeader requestHeader, String requestData) {

            this.method = method;
            this.url    = url;

            this.request 	   = request;
            this.requestHeader = requestHeader;
            this.requestData   = requestData;
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
        public Response readTo(OutputStream backoutput) throws IOException {
            Streams.copy(this.getInputStream(), backoutput);
            return this;
        }


        public Response write(int b) throws IOException {
            this.getOutputStream().write(b);
            return this;
        }
        public Response write(byte[] b) throws IOException {
            return this.write(b, 0, b.length);
        }
        public Response write(byte[] b, int off, int len) throws IOException {
            OutputStream stream = this.getOutputStream();
            stream.write(b, off, len);
            return this;
        }
        public Response write(InputStream stream) throws IOException {
            Streams.copy(stream, this.getOutputStream());
            return this;
        }
        public Response write(InputStream stream, long length) throws IOException {
            Streams.copy(new XInputStreamFixedLength<>(stream, length), this.getOutputStream());
            return this;
        }
        public Response flush() throws IOException {
            this.getOutputStream().flush();
            return this;
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

        String requestData;
        public String getRequestData() {
            return this.requestData;
        }



        public HttpURLConnection getURLConnection() {
            return request.getURLConnection();
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



        public InputStream getInputStream() throws IOException {
            return getURLConnection().getInputStream();
        }
        public OutputStream getOutputStream() throws IOException {
            return getURLConnection().getOutputStream();
        }


    }
}



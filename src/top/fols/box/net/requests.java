package top.fols.box.net;

import top.fols.atri.lang.StringLineReader;
import top.fols.atri.net.MessageHeader;
import top.fols.atri.net.URLBuilder;
import top.fols.atri.net.URLConnections;
import top.fols.atri.net.XURL;
import top.fols.box.net.header.ContentType;

import java.io.IOException;
import java.nio.charset.Charset;

public class requests {
    /**
     *
     * METHOD URL HTTP_VERSION
     * HTTP_HEADER
     * HTTP_HEADER
     * HTTP_HEADER
     *
     * REQUEST_DATA
     */
    public static String request(String requestSource) throws IOException {
        StringLineReader reader = new StringLineReader(requestSource);
        String first =   reader.next();

        String method = first.split(" ")[0];
        String url    = first.split(" ")[1];

        MessageHeader header = new MessageHeader();
        String line;
        while (reader.hasNext()) {
            line = reader.next(false);
            if (line.length() == 0) {
                break;
            }
            header.putAll(line);
        }
        header.put("Accept-Encoding", "identity");

        String charset = MessageHeader.HTTP_MESSAGE_HEADER_CHARSET_ISO_8859_1.name();
        ContentType request_content_type = ContentType.parse(header.get("Content-Type"));
        if (request_content_type.hasCharset()) {
            charset = request_content_type.getCharset();
        }


        StringBuilder data = new StringBuilder();
        while (reader.hasNext()) {
            line = reader.next(false);
            data.append(line);
        }
        byte[] dataBytes = data.toString().getBytes(charset);

        String content_length = header.get("Content-Length");
        if (null != content_length) {
            long length = Long.parseLong(content_length);
            if  (length != dataBytes.length) {
                header.put("Content-Length", String.valueOf(dataBytes.length));
            }
        }

        XURL xurl = new XURL(url);
        if  (xurl.getProtocol() == null) {
            URLBuilder urlBuilder;
            urlBuilder = xurl.toBuilder();
            urlBuilder.protocol("http");
            urlBuilder.host(header.get("host"));
            xurl = new XURL(urlBuilder.build());
        }

        URLConnections.HttpURLConnectionUtil request = new URLConnections.HttpURLConnectionUtil(xurl.getUrl());
        request.setRequestMethod(method);
        request.messageHeader(header);

        request.getURLConnection().setDoInput(true);

        if (data.length() > 0) {
            request.getURLConnection().setDoOutput(true);
            request.write(dataBytes);
        }

        byte[] bytes = request.readBytes();


        MessageHeader responseHeader        = new MessageHeader(request.getURLConnection().getHeaderFields());
        ContentType   response_content_type = ContentType.parse(responseHeader.get("Content-Type"));
        Charset       response_charset      = MessageHeader.HTTP_MESSAGE_HEADER_CHARSET_ISO_8859_1;
        if (response_content_type.hasCharset()) {
            response_charset = Charset.forName(response_content_type.getCharset());
        }
        return new String(bytes, response_charset);
    }

}

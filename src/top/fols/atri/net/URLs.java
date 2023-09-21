package top.fols.atri.net;

import top.fols.atri.io.file.Filex;
import top.fols.atri.io.util.Streams;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLs {
    public static final String PROTOCOL_SYMBOL = ":";

    public static final char   QUERY_SYMBOL_CHAR = '?';
    public static final String QUERY_SYMBOL = "" + QUERY_SYMBOL_CHAR;

    public static final char   PATH_SEPARATOR_CHAR = '/';
    public static final String PATH_SEPARATOR = "" + PATH_SEPARATOR_CHAR;

    public static final String REF_SYMBOL = "#";

    public static String spec(String url, String spec) {
        try {
            return spec(new URL(url), spec);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static String spec(URL url, String spec) {
        try {
            return new URL(url, spec).toString();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String deleteProtocolSymbol(String path) {
        return path.replace(PROTOCOL_SYMBOL, PATH_SEPARATOR);//去除子路径的协议符号 :
    }

    public static String formatToCanonicalRelativeURL(String path) {
        path = deleteProtocolSymbol(path);
        path = Filex.getCanonicalRelativePath(path, PATH_SEPARATOR_CHAR);
        if (path.startsWith(PATH_SEPARATOR)) {
            path = path.substring(PATH_SEPARATOR.length(), path.length());
        }
        return path;
    }

    public static URLConnection openRelativeURLConnection(URL url, String specPath) {
        if (null == url)
            return null;
        URLConnection urlConnection = null;
        try {
            urlConnection = new URL(url, formatToCanonicalRelativeURL(specPath)).openConnection();
            if (null != urlConnection.getInputStream()) {
                return  urlConnection;
            }
        } catch (Exception ignored) {}
        Streams.close(urlConnection);
        return null;
    }


//    static void main(String[] args) throws IOException {
//        String pathname = "C:/documents/360Sync/Programming/JAVA/fols/folsTool/document/re/例子/loader/main.re";
//        URL url = new File(pathname).toURL();
//        System.out.println(url.toString());
//
//        System.out.println(new URL("file:784920843@.classpath").openConnection().getContentLengthLong());
//        System.out.println(new Attributes(new URL("file:784920843@.classpath")));
//        System.out.println(new Attributes(new URL(url.toExternalForm())));
//        System.out.println(new Attributes(new URL("https://xd.x6d.com/i-wz-18881.html?a=a")));
//
//        System.out.println(new HttpURL(url.toExternalForm()).getHost());
//        System.out.println(new HttpURL(url.toExternalForm()).getFilePath());
//    }
}

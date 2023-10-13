package top.fols.atri.net;


import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Value;

/**
 * it does not convert absolute addresses
 *
 * @author GXin https://github.com/xiaoxinwangluo
 */
@SuppressWarnings("StringOperationCanBeSimplified")
public class HttpURL implements Serializable {
    private static final long serialVersionUID = 1L;


    public static final String INET6HOST_PREFIX = "[", INET6HOST_SUFFIX = "]";
    public static final String INET6HOST_GROUP_SYMBOL = ":";

    public static final String PROTOCOL_SYMBOL = "://";

    public static final String USER_SYMBOL = "@";

    public static final String PORT_SYMBOL = ":";


    public static final String QUERY_SYMBOL = URLs.QUERY_SYMBOL;
    public static final char   QUERY_SYMBOL_CHAR =  URLs.QUERY_SYMBOL_CHAR;

    public static final String PATH_SEPARATOR = URLs.PATH_SEPARATOR;
    public static final char   PATH_SEPARATOR_CHAR = URLs.PATH_SEPARATOR_CHAR;

    public static final String REF_SYMBOL = URLs.REF_SYMBOL;


    public static final char    PARAM_PROJECT_SEPARATOR_CHAR = '&';
    public static final String  PARAM_PROJECT_SEPARATOR = "" + PARAM_PROJECT_SEPARATOR_CHAR;

    public static final char   PARAM_PROJECT_ASSIGNMENT_SYMBOL_CHAR = '=';
    public static final String PARAM_PROJECT_ASSIGNMENT_SYMBOL = "" + PARAM_PROJECT_ASSIGNMENT_SYMBOL_CHAR;


    /**
     * URL FORMAT:
     * protocol://user@host:port/dir/filename?param=tip&multiplyParam=tip#ref
     *
     * @param protocol is url protocol(http/https/ftp/...)
     * @param user     null / user
     * @param host     host
     * @param port     null / port
     * @param filePath null/ /dir/filename
     * @param param    null / param=tip&multiplyParam=tip
     * @param ref      null / ref
     */
    public static String createURL(String protocol, String user, String host, String port, String filePath,
                                   String param, String ref) {
        StringBuilder sb = new StringBuilder();

        if (null != protocol) {
            sb.append(protocol);

            if (!protocol.endsWith(HttpURL.PROTOCOL_SYMBOL)) {
                sb.append(HttpURL.PROTOCOL_SYMBOL);
            }
        }
        if (null != user) {
            sb.append(user);

            if (!user.endsWith(HttpURL.USER_SYMBOL)) {
                sb.append(HttpURL.USER_SYMBOL);
            }
        }
        if (null != host) {
            sb.append(host);
        }
        if (null != port) {
            if (!port.startsWith(HttpURL.PORT_SYMBOL)) {
                sb.append(HttpURL.PORT_SYMBOL);
            }

            sb.append(port);
        }
        if (null != filePath) {
            if (!filePath.startsWith(PATH_SEPARATOR)) {
                sb.append(PATH_SEPARATOR);
            }

            sb.append(filePath);
        }
        if (null != param) {
            if (!param.startsWith(QUERY_SYMBOL)) {
                sb.append(QUERY_SYMBOL);
            }

            sb.append(param);
        }
        if (null != ref) {
            if (!ref.startsWith(REF_SYMBOL)) {
                sb.append(REF_SYMBOL);
            }

            sb.append(ref);
        }

        return sb.toString();
    }

    /**
     * if the host is an ipv6 address, '[]' will be added automatically
     */
    public static String fromHostToStandardizationHost(String host) {
        if (host.contains(HttpURL.INET6HOST_GROUP_SYMBOL)) {
            if (HttpURL.fromHostAndPortHasInet6Host(host)) {
                return host;
            } else {
                return HttpURL.INET6HOST_PREFIX + host + HttpURL.INET6HOST_SUFFIX;
            }
        } else {
            return host;
        }
    }
    static boolean fromHostAndPortHasInet6Host(String address) {
        if (address.startsWith(INET6HOST_PREFIX)) {
            int prefixIndex = INET6HOST_PREFIX.length();
            int suffixIndex = address.indexOf(INET6HOST_SUFFIX, prefixIndex);
            return suffixIndex >= prefixIndex;
        }
        return false;
    }
    static String fromHostAndPortGetHost(String address) {
        if (address.startsWith(INET6HOST_PREFIX)) {
            int prefixIndex = INET6HOST_PREFIX.length();
            int suffixIndex = address.indexOf(INET6HOST_SUFFIX, prefixIndex);
            if (suffixIndex < prefixIndex) {
                int start = address.lastIndexOf(HttpURL.PORT_SYMBOL);
                if (start > -1) {
                    return address.substring(0, start);
                } else {
                    return address;
                }
            } else {
                return address.substring(0, suffixIndex + INET6HOST_SUFFIX.length());
            }
        } else {
            int start = address.lastIndexOf(HttpURL.PORT_SYMBOL);
            if (start > -1) {
                return address.substring(0, start);
            } else {
                return address;
            }
        }
    }
    static String fromHostAndPortGetPort(String address) {
        if (address.startsWith(INET6HOST_PREFIX)) {
            int prefixIndex = INET6HOST_PREFIX.length();
            int suffixIndex = address.indexOf(INET6HOST_SUFFIX, prefixIndex);
            if (suffixIndex < prefixIndex) {
                int start = address.lastIndexOf(HttpURL.PORT_SYMBOL);
                if (start > -1) {
                    return address.substring(start + HttpURL.PORT_SYMBOL.length(), address.length());
                }
            } else {
                suffixIndex += INET6HOST_SUFFIX.length();
                int start = address.lastIndexOf(HttpURL.PORT_SYMBOL);
                if (start >= suffixIndex) {
                    return address.substring(start + HttpURL.PORT_SYMBOL.length(), address.length());
                }
            }
            return null;
        } else {
            int start = address.lastIndexOf(HttpURL.PORT_SYMBOL);
            if (start > -1) {
                return address.substring(start + HttpURL.PORT_SYMBOL.length(), address.length());
            }
            return null;
        }
    }






    private String oUrl; // origin url

    private String uProtocol; // null / protocol(http/https/ftp/...)
    private String uUser; // null / user
    private String uHostAndPort; // host(:port)
    private String uRoot; // (protocol://)(user@)host(:port)
    private String uDir; // /(dir/)
    private String uPathAndParam; // /(dir/filename)(?param=tip&multiplyParam=tip)
    private String uRef; // null / ref


    transient Value<String> cAuthority; // (user@)host(:port)
    transient Value<String> cHost; // host
    transient Value<String> cPort; // null / port
    transient Value<String> cFilePath; // /(dir/filename)
    transient Value<String> cDirName; // dirname
    transient Value<String> cFileName; // filename
    transient Value<String> cFileNameAndParam; // filename(?param=tip&multiplyParam=tip)
    transient Value<String> cParam; // null / param=tip&multiplyParam=tip

    transient Value<String> cFormatUrl; // formatURL


    public HttpURLBuilder toBuilder() {
        //protocol://user@host:port/dir/filename?param=tip&multiplyParam=tip#ref
        HttpURLBuilder urlBuilder = new HttpURLBuilder();
        urlBuilder.protocol(this.getProtocol());
        urlBuilder.user(this.getUser());
        urlBuilder.host(this.getHost());
        urlBuilder.port(this.getPort());
        urlBuilder.path(this.getFilePath());
        urlBuilder.param(this.getParam());
        urlBuilder.ref(this.getRef());
        return urlBuilder;
    }



    /**
     * "@return create the parameter URL for this instance"
     */
    public String getUrl() {
        return this.oUrl;
    }

    /**
     * @return (protocol://)(user@)host(:port)/(dir/filename)(?param=tip&multiplyParam=tip)(#ref)
     */
    public String getUrlFormat() {
        if (null != this.cFormatUrl) {
            return this.cFormatUrl.get();
        }
        return (this.cFormatUrl = Value.wrap(createURL( this.getProtocol(), this.getUser(), this.getHost(), this.getPort(),
                                                        this.getFilePath(),
                                                        this.getParam(), this.getRef()))).get();
    }

    /**
     * @return null/protocol
     */
    public String getProtocol() {
        return this.uProtocol;
    }

    /**
     * @return host
     */
    public String getHost() {
        if (null != this.cHost) {
            return  this.cHost.get();
        }
        String hostAndPort = this.getHostAndPort();
        return (this.cHost = Value.wrap(fromHostAndPortGetHost(hostAndPort))).get();
    }

    /**
     * @return null/port
     */
    public String getPort() {
        if (null != this.cPort) {
            return  this.cPort.get();
        }
        String hostAndPort = this.getHostAndPort();
        return (this.cPort = Value.wrap(fromHostAndPortGetPort(hostAndPort))).get();
    }

    /**
     * @return port
     */
    public int getPortIntValue() {
        String portString = this.getPort();
        return null == portString ? -1 : Objects.get_Int(portString);
    }

    /**
     * @return port
     */
    public int getPortIntValue(int defaultPort) {
        int port = this.getPortIntValue();
        return port == -1 ? defaultPort : port;
    }

    /**
     * @return port
     */
    public int getHttpPortIntValue() {
        int port = 80;
        if ("HTTP".equalsIgnoreCase(this.getProtocol())) {
            if (this.getPortIntValue() != -1) {
                port = this.getPortIntValue();
            }
        } else if ("HTTPS".equalsIgnoreCase(this.getProtocol())) {
            if (this.getPortIntValue() == -1) {
                port = 443;
            } else {
                port = this.getPortIntValue();
            }
            return port;
        }
        return port;
    }

    /**
     * @return host(:port)
     */
    public String getHostAndPort() {
        return this.uHostAndPort;
    }

    /**
     * @return null/user
     */
    public String getUser() {
        return this.uUser;
    }

    /**
     * @return (protocol://)(user@)host(:port)
     */
    public String getRoot() {
        return this.uRoot;
    }

    /**
     * @return /(dir/)
     */
    public String getDir() {
        return this.uDir;
    }

    /**
     * @return /(dir/filename)
     */
    public String getFilePath() {
        if (null != this.cFilePath) {
            return this.cFilePath.get();
        }
        String tmp = this.getPathDetailed();
        int start;
        if ((start = tmp.indexOf(REF_SYMBOL)) > -1) {
               tmp = tmp.substring(0, start);
        }
        if ((start = tmp.indexOf(QUERY_SYMBOL)) > -1) {
               tmp = tmp.substring(0, start);
        }
        return (this.cFilePath = Value.wrap(tmp)).get();
    }

    /**
     * @return /(dir/filename)(?param=tip&multiplyParam=tip)
     */
    public String getPathDetailed() {
        return this.uPathAndParam;
    }

    /**
     * "@return filename"
     */
    public String getFileName() {
        if (null != this.cFileName) {
            return this.cFileName.get();
        }
        String tmp = this.getFilePath();
        if (!tmp.startsWith(PATH_SEPARATOR)) {
             tmp = PATH_SEPARATOR + tmp;
        }
        int start = -1;
        if ((start = tmp.lastIndexOf(PATH_SEPARATOR)) > -1) {
            tmp = tmp.substring(start + PATH_SEPARATOR.length(), tmp.length());
        }
        return (this.cFileName = Value.wrap(tmp)).get();
    }

    /**
     * @return filename(?param=tip&multiplyParam=tip)
     */
    public String getFileNameDetailed() {
        if (null != this.cFileNameAndParam) {
            return this.cFileNameAndParam.get();
        }
        String tmp = this.getPathDetailed();
        if (!tmp.startsWith(PATH_SEPARATOR)) {
             tmp = PATH_SEPARATOR + tmp;
        }
        int start = -1;
        if ((start = tmp.indexOf(QUERY_SYMBOL)) > -1) {
            if ((start = tmp.lastIndexOf(PATH_SEPARATOR, start - QUERY_SYMBOL.length())) > -1) {
                   tmp = tmp.substring(start + PATH_SEPARATOR.length(), tmp.length());
            }
        } else {
            if ((start = tmp.lastIndexOf(PATH_SEPARATOR)) > -1) {
                   tmp = tmp.substring(start + PATH_SEPARATOR.length(), tmp.length());
            }
        }
        return (this.cFileNameAndParam = Value.wrap(tmp)).get();
    }

    /**
     * get dir name
     *
     * @return dirname
     */
    public String getDirName() {
        if (null != this.cDirName) {
            return this.cDirName.get();
        }
        String tmp = this.getDir();
        int lastIndex = tmp.lastIndexOf(PATH_SEPARATOR);
        int startIndex = tmp.lastIndexOf(PATH_SEPARATOR, lastIndex - PATH_SEPARATOR.length());
        String dirname;
        if (lastIndex > startIndex) {
            dirname = tmp.substring(startIndex + PATH_SEPARATOR.length(), lastIndex);
        } else {
            dirname = "";
        }
        return (this.cDirName = Value.wrap(dirname)).get();
    }

    /**
     * get parent dir
     *
     * example: "http://test:80/gvv/e/ec/cc/gg/sss/",
     * "http://test:80/gvv/e/ec/cc/gg/"
     * <p>
     * example: "http://test:80/a", "http://test:80/"
     * <p>
     * example: "http://test:80", "http://test:80/"
     *
     * @return #ref
     */
    public HttpURL getParent() {
        String pd = this.getDir();// endWith(PATH_SEPARATOR) >> true
        int li = pd.lastIndexOf(PATH_SEPARATOR, (pd.length() - 1) - PATH_SEPARATOR.length());
        if (li > -1) {
            pd = pd.substring(0, li + PATH_SEPARATOR.length());
        } else {
            pd = null;
        }
        return new HttpURL(createURL(  this.getProtocol(), this.getUser(), this.getHost(), this.getPort(),
                                    pd,
                                    this.getParam(), this.getRef()));
    }

    /**
     * @return this.getFilePath().endsWith(PATH_SEPARATOR)
     */
    public boolean isDir() {
        return this.getFilePath().endsWith(PATH_SEPARATOR);
    }

    /**
     * @return null/ref
     */
    public String getRef() {
        return this.uRef;
    }

    /**
     * @return (user@)host(:port)
     */
    public String getUserAndHostAndPort() {
        if (null != this.cAuthority) {
            return this.cAuthority.get();
        }
        StringBuilder buf = new StringBuilder();
        if (null != this.getUser()) {
            buf.append(this.getUser()).append(HttpURL.USER_SYMBOL);
        }
        buf.append(this.getHostAndPort());
        return (this.cAuthority = Value.wrap(buf.toString())).get();
    }

    /**
     * @return null/param=tip&multiplyParam=tip
     */
    public String getParam() {
        if (null != this.cParam) {
            return this.cParam.get();
        }
        String param = this.getFileNameDetailed();
        int indexof = param.indexOf(QUERY_SYMBOL);
        if (indexof > -1) {
            param = param.substring(indexof + QUERY_SYMBOL.length(), param.length());

            int refindxof = param.indexOf(REF_SYMBOL);
            if (refindxof > -1) {
                param = param.substring(0, refindxof);
            }
        } else {
            param = null;
        }
        return (this.cParam = Value.wrap(param)).get();
    }

    /**
     * @param url the String to parse as a URL.
     */
    public HttpURL(String url) {
        this.init(url);
    }
    public HttpURL(String root, String path) {
        String start = root;
        if (start.endsWith(URLs.PATH_SEPARATOR)) {
            start = start.substring(0, start.length() - URLs.PATH_SEPARATOR.length());
        }
        String end = "";
        int pIndex = path.indexOf(URLs.QUERY_SYMBOL);
        if (pIndex > -1) {
            end = path.substring(pIndex);
            path = path.substring(0, pIndex);
        }
        String relativePath = Filex.getCanonicalRelativePath(path, URLs.PATH_SEPARATOR_CHAR);
        String url = start + relativePath + end;
        this.init(url);
    }
    private void init(String spec) {
        this.oUrl = spec;

        if (null == spec) {
            spec = "";
        }

        int start = 0;
        int limit = spec.length();
        while ((limit > 0) && (spec.charAt(limit - 1) <= ' ')) {
            limit--;
        }
        while ((start < limit) && (spec.charAt(start) <= ' ')) {
            start++;
        }
        // deal ref
        int removeNotesStart = -1;
        if ((removeNotesStart = spec.indexOf(REF_SYMBOL)) > -1) {
            this.uRef = spec.substring(removeNotesStart + REF_SYMBOL.length(), limit);
            limit = removeNotesStart;
        } else {
            this.uRef = null;
        }
        if (start != 0 || limit != spec.length()) {
            spec = spec.substring(start, limit);
        }

        // deal protocol
        int protocolStrLen = HttpURL.PROTOCOL_SYMBOL.length();
        int protocolStart = spec.indexOf(HttpURL.PROTOCOL_SYMBOL);
        if (protocolStart > -1) {
            this.uProtocol = spec.substring(0, protocolStart);
            spec = spec.substring(protocolStrLen + protocolStart, spec.length());
        } else {
            this.uProtocol = null;
            protocolStart = 0;
        }

        // deal PATH_SEPARATOR
        int split = spec.indexOf(PATH_SEPARATOR);
        int paramSplitCharIndex = spec.indexOf(QUERY_SYMBOL);
        if (paramSplitCharIndex > -1 && (paramSplitCharIndex < split || split <= -1)) {
            spec = spec.substring(0, paramSplitCharIndex) + PATH_SEPARATOR_CHAR +
                    spec.substring(paramSplitCharIndex, spec.length());
        } else {
            if (split <= -1) {
                spec = spec + PATH_SEPARATOR;
            }
        }

        // deal user
        int pathSeparatorIndex = spec.indexOf(PATH_SEPARATOR);// http://ip.cn:8080/a/b/c/1.html >>17
        int userIndex = spec.lastIndexOf(HttpURL.USER_SYMBOL, pathSeparatorIndex - PATH_SEPARATOR.length());
        if (userIndex > -1) {
            this.uUser = spec.substring(0, userIndex);
        } else {
            this.uUser = null;
        }

        // deal host and port
        this.uHostAndPort = spec.substring(userIndex <= -1 ? 0 : userIndex + HttpURL.USER_SYMBOL.length(),
                pathSeparatorIndex);// >>ip.cn:8080

        StringBuilder buf = new StringBuilder();
        if (null != this.uProtocol) {
            buf.append(this.uProtocol).append(HttpURL.PROTOCOL_SYMBOL);
        }
        if (null != this.uUser) {
            buf.append(this.uUser).append(HttpURL.USER_SYMBOL);
        }

        // deal root
        this.uRoot = buf.append(this.uHostAndPort).toString();
        buf = null;

        // deal path and param
        this.uPathAndParam = (spec = spec.substring(pathSeparatorIndex, spec.length()));// /a/b/c/1.html

        int ind = spec.indexOf(QUERY_SYMBOL);
        if (ind > -1) {
            spec = spec.substring(0, ind);
        }

        // deal dir
        this.uDir = spec.substring(0, spec.lastIndexOf(PATH_SEPARATOR) + PATH_SEPARATOR.length()); // /a/b/c/
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        return this.getUrlFormat();
    }

    public HttpURLParam param() {
        String param = this.getParam();
        return null == param ? null : new HttpURLParam(param);
    }
    public HttpURLParam paramNonNull() {
        HttpURLParam param = this.param();
        return null == param ? new HttpURLParam() : param;
    }







    transient URL toURL;
    public URL toURL() {
        if (null == this.toURL) {
            try {
                this.toURL = new URL(this.getUrl());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return this.toURL;
    }

    /**
     * @see URL(URL, String)
     */
    public String  spec(String spec) {
        return URLs.spec(toURL(), spec);
    }
    public HttpURL specs(String spec) {
        return new HttpURL(this.spec(spec));
    }



}

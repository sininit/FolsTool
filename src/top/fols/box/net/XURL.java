package top.fols.box.net;

import java.io.Serializable;
import top.fols.box.io.os.XFile;
import top.fols.box.lang.XObject;
import top.fols.box.util.XObjects;

/**
 * it does not convert absolute addresses
 * 
 * @author GXin https://github.com/xiaoxinwangluo
 */
public class XURL implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * URL FORMAT:
     * protocol://user@host:port/dir/filename?param=value&multiplyParam=value#ref
     *
     * @param protocol is url protocol(http/https/ftp/...)
     * @param user     null / user
     * @param host     host
     * @param port     null / port
     * @param filePath null/ /dir/filename
     * @param param    null / param=value&multiplyParam=value
     * @param ref      null / ref
     */
    public static String createURL(String protocol, String user, String host, String port, String filePath,
                                   String param, String ref) {
        StringBuilder sb = new StringBuilder();

        if (null != protocol) {
            sb.append(protocol);

            if (!protocol.endsWith(XURL.PROTOCOL_SYMBOL)) {
                sb.append(XURL.PROTOCOL_SYMBOL);
            }
        }
        if (null != user) {
            sb.append(user);

            if (!user.endsWith(XURL.USER_SYMBOL)) {
                sb.append(XURL.USER_SYMBOL);
            }
        }
        if (null != host) {
            sb.append(host);
        }
        if (null != port) {
            if (!port.startsWith(XURL.PORT_SYMBOL)) {
                sb.append(XURL.PORT_SYMBOL);
            }

            sb.append(port);
        }
        if (null != filePath) {
            if (!filePath.startsWith(XURL.PATH_SEPARATOR)) {
                sb.append(XURL.PATH_SEPARATOR);
            }

            sb.append(filePath);
        }
        if (null != param) {
            if (!param.startsWith(XURL.PARAM_SYMBOL)) {
                sb.append(XURL.PARAM_SYMBOL);
            }

            sb.append(param);
        }
        if (null != ref) {
            if (!ref.startsWith(XURL.REF_SYMBOL)) {
                sb.append(XURL.REF_SYMBOL);
            }

            sb.append(ref);
        }

        return sb.toString();
    }

    public static final String PROTOCOL_SYMBOL = "://";

    public static final String USER_SYMBOL = "@";

    public static final String PORT_SYMBOL = ":";

    public static final String PATH_SEPARATOR = "/";
    public static final char PATH_SEPARATOR_CHAR = '/';

    public static final String PARAM_SYMBOL = "?";
    public static final char PARAM_SYMBOL_CHAR = '?';

    public static final String PARAM_PROJECT_SEPARATOR = "&";
    public static final String PARAM_PROJECT_ASSIGNMENT_SYMBOL = "=";

    public static final String REF_SYMBOL = "#";

    private String oUrl; // origin url

    private String uProtocol; // null / protocol(http/https/ftp/...)
    private String uUser; // null / user
    private String uHostAndPort; // host(:port)
    private String uRoot; // (protocol://)(user@)host(:port)
    private String uDir; // /(dir/)
    private String uPathAndParam; // /(dir/filename)(?param=value&multiplyParam=value)
    private String uRef; // null / ref

    private static class Cache<T extends Object> extends XObject<T> {
        private Cache(T object) {
            super(object);
        }
        private static <T extends Object> Cache<T> wrap(T object) {
            return new Cache<T>(object);
        }
    }

    private transient Cache<String> cAuthority = null; // (user@)host(:port)
    private transient Cache<String> cHost = null; // host
    private transient Cache<String> cPort = null; // null / port
    private transient Cache<String> cFilePath = null; // /(dir/filename)
    private transient Cache<String> cDirName = null; // dirname
    private transient Cache<String> cFileName = null; // filename
    private transient Cache<String> cFileNameAndParam = null; // filename(?param=value&multiplyParam=value)
    private transient Cache<String> cParam = null; // null / param=value&multiplyParam=value

    private transient Cache<String> cAbsoluteUrl = null; // absoluteURL
    private transient Cache<String> cFormatUrl = null; // formatURL

    /**
     * "@return create the parameter URL for this instance"
     */
    public String getUrl() {
        return this.oUrl;
    }

    /**
     * @return (protocol://)(user@)host(:port)/(dir/filename)(?param=value&multiplyParam=value)(#ref)
     */
    public String getUrlFormat() {
        if (null != this.cFormatUrl) {
            return this.cFormatUrl.get();
        }
        return (this.cFormatUrl = XURL.Cache.wrap(createURL(this.getProtocol(), this.getUser(), this.getHost(),
                this.getPort(), this.getFilePath(), this.getParam(), this.getRef()))).get();
    }

    /**
     * @return protocol://(user@)host(:port)/(dir/filename)(?param=value&multiplyParam=value)(#ref)
     */
    public String getAbsoluteUrl() {
        if (null != this.cAbsoluteUrl) {
            return this.cAbsoluteUrl.get();
        }
        return (this.cAbsoluteUrl = XURL.Cache.wrap(
            createURL(null == this.getProtocol() ? ("" + null) : this.getProtocol(), this.getUser(), this.getHost(),
                this.getPort(), XURL.formatPath(this.getFilePath()), this.getParam(), this.getRef()))).get();
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
            return this.cHost.get();
        }
        String newHost;
        int start = this.getHostAndPort().indexOf(XURL.PORT_SYMBOL);
        if (start > -1) {
            newHost = this.getHostAndPort().substring(0, start);
        } else {
            newHost = this.getHostAndPort();
        }
        return (this.cHost = XURL.Cache.wrap(newHost)).get();
    }

    /**
     * @return null/port
     */
    public String getPort() {
        if (null != this.cPort) {
            return this.cPort.get();
        }

        String result = null;
        String hp = this.getHostAndPort();
        int start = hp.lastIndexOf(XURL.PORT_SYMBOL);
        if (start > -1) {
            String str = hp.substring(start + XURL.PORT_SYMBOL.length(), hp.length());
            result = str;
        }

        return (this.cPort = XURL.Cache.wrap(result)).get();
    }

    /**
     * @return port
     */
    public int getPortIntValue() {
        String portString = this.getPort();
        int port = null == portString ? -1 : XObjects.parseInt(portString);
        return port;
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
            if (this.getPortIntValue() == -1) {
                port = 80;
            } else {
                port = this.getPortIntValue();
            }
        } else if ("HTTPS".equalsIgnoreCase(this.getProtocol())) {
            if (this.getPortIntValue() == -1) {
                port = 443;
            } else {
                port = this.getPortIntValue();
            }
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
        if ((start = tmp.indexOf(XURL.REF_SYMBOL)) > -1) {
            tmp = tmp.substring(0, start);
        }
        if ((start = tmp.indexOf(XURL.PARAM_SYMBOL)) > -1) {
            tmp = tmp.substring(0, start);
        }
        return (this.cFilePath = XURL.Cache.wrap(tmp)).get();
    }

    /**
     * @return /(dir/filename)(?param=value&multiplyParam=value)
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
        if (tmp.startsWith(XURL.PATH_SEPARATOR) == false) {
            tmp = new StringBuilder(XURL.PATH_SEPARATOR).append(tmp).toString();
        }
        int start = -1;
        if ((start = tmp.lastIndexOf(XURL.PATH_SEPARATOR)) > -1) {
            tmp = tmp.substring(start + XURL.PATH_SEPARATOR.length(), tmp.length());
        }
        return (this.cFileName = XURL.Cache.wrap(tmp)).get();
    }

    /**
     * @return filename(?param=value&multiplyParam=value)
     */
    public String getFileNameDetailed() {
        if (null != this.cFileNameAndParam) {
            return this.cFileNameAndParam.get();
        }
        String tmp = this.getPathDetailed();
        if (!tmp.startsWith(XURL.PATH_SEPARATOR)) {
            tmp = new StringBuilder(XURL.PATH_SEPARATOR).append(tmp).toString();
        }
        int start = -1;
        if ((start = tmp.indexOf(XURL.PARAM_SYMBOL)) > -1) {
            if ((start = tmp.lastIndexOf(XURL.PATH_SEPARATOR, start - XURL.PARAM_SYMBOL.length())) > -1) {
                tmp = tmp.substring(start + XURL.PATH_SEPARATOR.length(), tmp.length());
            }
        } else {
            if ((start = tmp.lastIndexOf(XURL.PATH_SEPARATOR)) > -1) {
                tmp = tmp.substring(start + XURL.PATH_SEPARATOR.length(), tmp.length());
            }
        }
        return (this.cFileNameAndParam = XURL.Cache.wrap(tmp)).get();
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
        int lastIndex = tmp.lastIndexOf(XURL.PATH_SEPARATOR);
        int startIndex = tmp.lastIndexOf(XURL.PATH_SEPARATOR, lastIndex - XURL.PATH_SEPARATOR.length());
        String dirname;
        if (lastIndex > startIndex) {
            dirname = tmp.substring(startIndex + XURL.PATH_SEPARATOR.length(), lastIndex);
        } else {
            dirname = "";
        }
        return (this.cDirName = XURL.Cache.wrap(dirname)).get();
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
    public XURL getParent() {
        String pd = this.getDir();// endWith(XURL.PATH_SEPARATOR) >> true
        int li = pd.lastIndexOf(XURL.PATH_SEPARATOR, (pd.length() - 1) - XURL.PATH_SEPARATOR.length());
        if (li > -1) {
            pd = pd.substring(0, li + XURL.PATH_SEPARATOR.length());
        } else {
            pd = null;
        }
        return new XURL(createURL(this.getProtocol(), this.getUser(), this.getHost(), this.getPort(), pd,
                this.getParam(), this.getRef()));
    }

    /**
     * @return this.getFilePath().endsWith(PATH_SEPARATOR)
     */
    public boolean isDir() {
        return this.getFilePath().endsWith(XURL.PATH_SEPARATOR);
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
            buf.append(this.getUser()).append(XURL.USER_SYMBOL);
        }
        buf.append(this.getHostAndPort());
        return (this.cAuthority = XURL.Cache.wrap(buf.toString())).get();
    }

    /**
     * @return null/param=value&multiplyParam=value
     */
    public String getParam() {
        if (null != this.cParam) {
            return this.cParam.get();
        }
        String param = this.getFileNameDetailed();
        int indexof = param.indexOf(XURL.PARAM_SYMBOL);
        if (indexof > -1) {
            param = param.substring(indexof + XURL.PARAM_SYMBOL.length(), param.length());

            int refindxof = param.indexOf(XURL.REF_SYMBOL);
            if (refindxof > -1) {
                param = param.substring(0, refindxof);
            }
        } else {
            param = null;
        }
        return (this.cParam = XURL.Cache.wrap(param)).get();
    }

    /**
     * @param spec the String to parse as a URL.
     */
    public XURL(String spec) {
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
        if ((removeNotesStart = spec.indexOf(XURL.REF_SYMBOL)) > -1) {
            this.uRef = spec.substring(removeNotesStart + XURL.REF_SYMBOL.length(), limit);
            limit = removeNotesStart;
        }
        if (start != 0 || limit != spec.length()) {
            spec = spec.substring(start, limit);
        }

        // deal protocol
        int protocolStrLen = XURL.PROTOCOL_SYMBOL.length();
        int protocolStart = spec.indexOf(XURL.PROTOCOL_SYMBOL);
        if (protocolStart > -1) {
            this.uProtocol = spec.substring(0, protocolStart);
            spec = spec.substring(protocolStrLen + protocolStart, spec.length());
        } else {
            this.uProtocol = null;
            protocolStart = 0;
        }

        // deal PATH_SEPARATOR
        int split = spec.indexOf(XURL.PATH_SEPARATOR);
        int paramSplitCharIndex = spec.indexOf(XURL.PARAM_SYMBOL);
        if (paramSplitCharIndex > -1 && (paramSplitCharIndex < split || split <= -1)) {
            spec = new StringBuilder(spec.substring(0, paramSplitCharIndex)).append(XURL.PATH_SEPARATOR_CHAR)
                .append(spec.substring(paramSplitCharIndex, spec.length())).toString();
        } else {
            if (split <= -1) {
                spec = new StringBuilder(spec).append(XURL.PATH_SEPARATOR).toString();
            }
        }

        // deal user
        int pathSeparatorIndex = spec.indexOf(XURL.PATH_SEPARATOR);// http://ip.cn:8080/a/b/c/1.html >>17
        int userIndex = spec.lastIndexOf(XURL.USER_SYMBOL, pathSeparatorIndex - XURL.PATH_SEPARATOR.length());
        if (userIndex > -1) {
            this.uUser = spec.substring(0, userIndex);
        }

        // deal host and port
        this.uHostAndPort = spec.substring(userIndex <= -1 ? 0 : userIndex + XURL.USER_SYMBOL.length(),
            pathSeparatorIndex);// >>ip.cn:8080

        StringBuilder buf = new StringBuilder();
        if (null != this.uProtocol) {
            buf.append(this.uProtocol).append(XURL.PROTOCOL_SYMBOL);
        }
        if (null != this.uUser) {
            buf.append(this.uUser).append(XURL.USER_SYMBOL);
        }

        // deal root
        this.uRoot = buf.append(this.uHostAndPort).toString();
        buf = null;

        // deal path and param
        this.uPathAndParam = (spec = spec.substring(pathSeparatorIndex, spec.length()));// /a/b/c/1.html

        int ind = spec.indexOf(XURL.PARAM_SYMBOL);
        if (ind > -1) {
            spec = spec.substring(0, ind);
        }

        // deal dir
        this.uDir = spec.substring(0, spec.lastIndexOf(XURL.PATH_SEPARATOR) + XURL.PATH_SEPARATOR.length()); // /a/b/c/

        this.cHost = null;
        this.cPort = null;
        this.cFilePath = null;
        this.cFileNameAndParam = null;
        this.cFileName = null;
        this.cDirName = null;
        this.cAuthority = null;
        this.cParam = null;

        this.cFormatUrl = null;
        this.cAbsoluteUrl = null;

        spec = null;
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        return this.getUrlFormat();
    }

    public XURL absoluteUrl() {
        return new XURL(this.getAbsoluteUrl());
    }

    public XURLParam param() {
        String param = this.getParam();
        return null == param ? null : new XURLParam();
    }

    public static String formatPath(String path) {
        return XFile.getCanonicalPath(path, PATH_SEPARATOR_CHAR);
    }

}


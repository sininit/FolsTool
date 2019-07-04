package top.fols.box.net;

import java.util.List;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.annotation.XExample;
import top.fols.box.lang.XString;
import top.fols.box.util.XObjects;

@XAnnotations("it does not convert absolute addresses")
@XExample({
		@XExample(e = "protocol://user@host:port/dir/filename?param=value&multiplyParam=value#ref",v = "format"),
	})
public class XURL {
	private String uUrl = null; //origin url
	private String uProtocol = null;// http https ...
	private String uHostAndPort = null;// xxx.xxx.xxx:xx
	private String uRoot = null;// http://xxx.xxx.xxx
	private String uDir = null;//  /a/
	private String uPath = null;// /index.html
	private String uRef = null;//#后面内容
	private String uUser = null;//协议和@之间的内容

	private String 		uAuthority = null;//协议和根目录之间的内容
	private String 		uUrlFormat = null;//格式化后的URL
	private String 		uHost = null;
	private int 		uPort = -1;
	private String 		uFilePath = null;// uPath去除?#符号后的文件路径
	private String 		uFileNameDetailed = null;
	private String 		uFileName = null;
	private String[]	uLevelDomainSplit = null;
	private String 		uDirName = null;

	public static final String 	HttpProtocolSplitChars = "://";
	public static final String 	DomainLevelSplitChars = ".";
	public static final String 	HostPortSplitChars = ":";
	public static final String 	PathSplitChars = "/";
	public static final char 	PathSplitchar = '/';
	public static final String 	UserAndHostSplitChars = "@";
	public static final String 	UrlAndParamSplitChars = "?";
	public static final char 	UrlAndParamSplitChar = '?';
	public static final String 	ParamSplitChars = "&";
	public static final String 	ParamKeyValueSplitChars = "=";
	public static final String 	RefChars = "#";



	public int getDomainLevel() {
		return getDomainLevelList().length;
	}
	public String getDomainLevel(int level) {
		return getDomainLevelList()[level - 1];
	}
	private String[] getDomainLevelList() {
		if (null != this.uLevelDomainSplit)
			return this.uLevelDomainSplit;
		List<String> Hostsplit = XString.split(getHost(), DomainLevelSplitChars);
		if (Hostsplit.size() <= 1) {
			this.uLevelDomainSplit = new String[]{getHost()};
		} else {
			int level = Hostsplit.size();
			StringBuilder sj = new StringBuilder();
			String[] s0 = new String[level];
			sj.append(Hostsplit.get(level - 1));
			s0[0] = Hostsplit.get(level - 1);
			for (int i = level - 1 - 1;i >= 0;i--) {
				sj.insert(0, Hostsplit.get(i) + ".");
				s0[level - i - 1] = sj.toString();
			}
			sj = null;
			this.uLevelDomainSplit = s0;
		}
		Hostsplit.clear();
		return this.uLevelDomainSplit;
	}


	@XAnnotations("origin url")
	public String getUrl() {
		return uUrl;
	}
	@XAnnotations("processed Url")
	@XExample(e = "protocol://user@host:port/dir/filename?param=value&multiplyParam=value#ref",v = "return format")
	public String getUrlFormat() {
		if (null != uUrlFormat)
			return this.uUrlFormat;
		StringBuilder buf = new StringBuilder();
		buf.append(getRoot());
		buf.append(getPathDetailed());
		if (null != getRef())
			buf.append(RefChars).append(getRef());
		this.uUrlFormat = buf.toString();
		buf = null;
		return this.uUrlFormat;
	}

	@XAnnotations("url protocol")
	@XExample({
			@XExample(e = "protocol",v="return format"),
			@XExample(e = "",v="return format")
		}) 
	public String getProtocol() {
		/*
		 http://127.0.0.1:7777/_HM4X/u
		 http://
		 */
		return uProtocol;
	}
	public String getHost() {
		if (null != uHost)
			return uHost;
		String newHost = "";
		int start = getHostAndPort().indexOf(HostPortSplitChars);
		if (start > -1)
			newHost = getHostAndPort().substring(0, start);
		else
			newHost = getHostAndPort();
		uHost = newHost;
		return uHost;
	}
	public int getPort() {
		if (uPort != -1)
			return uPort;
		int port = -1;
		try {
			int start = getHostAndPort().indexOf(HostPortSplitChars);
			if (start > -1) {
				String portstr = getHostAndPort().substring(start + HostPortSplitChars.length(), getHostAndPort().length());
				if (!portstr.equals(""))
					port = Integer.parseInt(portstr.trim());
				else
					port = -1;
			}
			uPort = port;
		} catch (Exception e) {
			uPort = -1;
		}
		return uPort;
	}
	@XExample({
			@XExample(e = "host:port",v="return format"),
			@XExample(e = "host",v="return format")
		}) 
	public String getHostAndPort() {
		/*
		 http://127.0.0.1:7777/_HM4X/u

		 127.0.0.1:7777
		 */
		return uHostAndPort;
	}
	public String getUser() {
		return this.uUser;
	}





	@XAnnotations("url root")
	@XExample(e = "protocol://user@:host:port",v="return format")
	public String getRoot() {
		/*
		 http://127.0.0.1:7777/_HM4X/u
		 http://127.0.0.1:7777
		 */
		return uRoot;
	}



	@XAnnotations("url dir")
	@XExample(e = "/dir/",v="return format")
	public String getDir() {
		/*
		 http://127.0.0.1:7777/_HM4X/u
		 /_HM4X/
		 */
		return uDir;
	}



	@XAnnotations("url getPathDetailed() after formatting")
	@XExample(e = "/dir/filename",v= "return format")
	public String getFilePath() {
		/*
		 http://127.0.0.1:7777/_HM4X/u?asd
		 /_HM4X/u
		 */
		if (null != this.uFilePath)
			return this.uFilePath;
		String tmp = getPathDetailed();
		int start;
		if ((start = tmp.indexOf(RefChars)) > -1)
			tmp = tmp.substring(0, start);
		if ((start = tmp.indexOf(UrlAndParamSplitChars)) > -1)
			tmp = tmp.substring(0, start);
		return this.uFilePath = tmp;
	}

	@XAnnotations("url file path and param")
	@XExample(e = "/dir/filename?param=value&multiplyParam=value",v= "return format")
	public String getPathDetailed() {
		/*
		 http://127.0.0.1:7777/_HM4X/u?asd
		 /_HM4X/u?asd
		 */
		return uPath;
	}



	@XExample(e = "filename",v= "return format")
	public String getFileName() {
		/*
		 http://127.0.0.1:7777/_HM4X/u&r??_#hash.html#?a=b
		 u&r
		 */
		if (null != uFileName)
			return uFileName;
		String tmp = getFilePath();
		if (tmp.startsWith(PathSplitChars) == false)
			tmp =  new StringBuilder(PathSplitChars).append(tmp).toString();
		int start = -1;
		if ((start = tmp.lastIndexOf(PathSplitChars)) > -1)
			tmp = tmp.substring(start + PathSplitChars.length(), tmp.length());
		uFileName = tmp;
		tmp = null;
		return uFileName;
	}

	@XAnnotations("url filename and param")
	@XExample(e = "filename?param=value&multiplyParam=value",v= "return format")
	public String getFileNameDetailed() {
		/*
		 http://127.0.0.1:7777/_HM4X/u&r??_#hash.html#?a=b
		 u&r??_#hash.html#?a=b

		 http://127.0.0.1:7777/l?path=%2F_PhoneFile%2F/listmode=1
		 l?path=%2F_PhoneFile%2F/listmode=1
		 */
		if (null != uFileNameDetailed)
			return uFileNameDetailed;
		String tmp = getPathDetailed();
		if (tmp.startsWith(PathSplitChars) == false)
			tmp =  new StringBuilder(PathSplitChars).append(tmp).toString();
		int start = -1;
		if ((start = tmp.indexOf(UrlAndParamSplitChars)) > -1) {
			if ((start = tmp.lastIndexOf(PathSplitChars, start - UrlAndParamSplitChars.length())) > -1)
				tmp = tmp.substring(start + PathSplitChars.length(), tmp.length());
		} else {
			if ((start = tmp.lastIndexOf(PathSplitChars)) > -1)
				tmp = tmp.substring(start + PathSplitChars.length(), tmp.length());
		}
		uFileNameDetailed = tmp;
		tmp = null;
		return uFileNameDetailed;
	}



	@XAnnotations("url dir name")
	@XExample(e = "dir",v= "return format")
	public String getDirName() {
		/*
		 http://127.0.0.1:7777/_HM4X/u&r??_#hash.html#?a=b
		 _HM4X
		 */
		if (null != uDirName)
			return uDirName;
		String tmp = getDir();
		int lastIndex = tmp.lastIndexOf(PathSplitChars);
		int startIndex = tmp.lastIndexOf(PathSplitChars, lastIndex - PathSplitChars.length());
		if (lastIndex > startIndex)
			uDirName = tmp.substring(startIndex + PathSplitChars.length(), lastIndex);
		else
			uDirName = "";
		tmp = null;
		return uDirName;
	}



	@XExample({
			@XExample(e = "http://test:80/gvv/e/ec/cc/gg/sss/",v= "/gvv/e/ec/cc../gg/"),
			@XExample(e = "http://test:80/gvv/e/ec/cc/gg/sss/u",v= "/gvv/e/ec/cc../gg/sss/"),
			@XExample(e = "http://test:80/a",v= "/"),
			@XExample(e = "http://test:80)",v= "/")
		})
	public XURL getParent() {
		String tmp = getFilePath();
		int lastIndex = tmp.lastIndexOf(PathSplitChars);
		if (tmp.endsWith(PathSplitChars))
			lastIndex = tmp.lastIndexOf(PathSplitChars, lastIndex - PathSplitChars.length());
		if (lastIndex <= -1) {
			tmp = "";
		} else {
			tmp = tmp.substring(0, lastIndex + PathSplitChars.length());
		}
		return new XURL(new StringBuilder(getRoot()).append(tmp).toString());
	}

	@XAnnotations("getFilePath().endsWith(Path_SplitChar);")
	public boolean isDir() {
		return getFilePath().endsWith(PathSplitChars);
	}

	@XAnnotations("#...")
	@XExample(e = "#ref",v = "return format")
	public String getRef() {
		return this.uRef;
	}
	public boolean existRef() {
		return null != getRef();
	}


	@XExample(e = "user@host:port",v = "return format")
	public String getUserAndHostAndPort() {
		if (null != this.uAuthority)
			return this.uAuthority;
		StringBuilder buf = new StringBuilder();
		if (null != getUser())
			buf.append(getUser()).append(UserAndHostSplitChars);
		buf.append(getHostAndPort());
		this.uAuthority = buf.toString();
		buf = null;
		return this.uAuthority;
	}


	@XExample({
			@XExample(e = "http://a:b@host:80/dir/filename?param=value&..#ref",v= "example"),
			@XExample(e = "a:b@host:80/dir/filename?param=value&..#ref",v= "example"),
			@XExample(e = "host:80/dir/filename?param=value&..#ref",v= "example"),
			@XExample(e = "host/dir/filename?param=value&..",v= "example"),
			@XExample(e = "host/dir/filename",v= "example"),
			@XExample(e = "...",v= "example")
		})
	public XURL(String spec) {
		this.uUrl = spec;

		if (null == spec)
			spec = "";

		int start = 0;
		int limit = spec.length();

		while ((limit > 0) && (spec.charAt(limit - 1) <= ' '))
			limit--;
		while ((start < limit) && (spec.charAt(start) <= ' ')) 
			start++;

		//remove #
		int removeNotesStart = -1;
		if ((removeNotesStart = spec.indexOf(RefChars)) > -1) {
			uRef = spec.substring(removeNotesStart + RefChars.length(), limit);
			limit = removeNotesStart;
		}
		if (start != 0 || limit != spec.length())
			spec = spec.substring(start, limit);

		int protocolStrLen = this.HttpProtocolSplitChars.length();
		int protocolStart = spec.indexOf(this.HttpProtocolSplitChars);
		if (protocolStart > -1) {
			this.uProtocol = spec.substring(0, protocolStart);
			spec = spec.substring(protocolStrLen + protocolStart, spec.length());
		} else {
			this.uProtocol = null;
			protocolStart = 0;
		}
		int split = spec.indexOf(PathSplitChars);
		int paramSplitCharIndex = spec.indexOf(UrlAndParamSplitChars);
		if (paramSplitCharIndex > -1 && (paramSplitCharIndex < split || split <= -1)) {
			spec =
				new StringBuilder(spec.substring(0, paramSplitCharIndex))
				.append(PathSplitchar)
				.append(spec.substring(paramSplitCharIndex, spec.length()))
				.toString();
		} else {
			if (split <= -1)
				spec = new StringBuilder(spec).append(XURL.PathSplitChars).toString();
		}
		int hostAndPortEndIndex = spec.indexOf(PathSplitChars);// http://ip.cn:8080/a/b/c/1.html >>17
		int userInd = spec.lastIndexOf(UserAndHostSplitChars, hostAndPortEndIndex - PathSplitChars.length());
		if (userInd > -1)
			this.uUser = spec.substring(0, userInd);
		this.uHostAndPort = spec.substring(userInd <= -1 ?0: userInd + UserAndHostSplitChars.length(), hostAndPortEndIndex);//  >>ip.cn:8080

		StringBuilder buf;
		buf = new StringBuilder();
		if (null != this.uProtocol)
			buf.append(this.uProtocol).append(this.HttpProtocolSplitChars);
		if (null != this.uUser)
			buf.append(this.uUser).append(UserAndHostSplitChars);
		this.uRoot = buf.append(this.uHostAndPort).toString();
		buf = null;//  >>http://ip.cn:8080

		this.uPath = (spec = spec.substring(hostAndPortEndIndex, spec.length()));// /a/b/c/1.html

		int ind = spec.indexOf(UrlAndParamSplitChars);
		if (ind > -1)
			spec = spec.substring(0, ind);
		this.uDir = spec.substring(0, spec.lastIndexOf(XURL.PathSplitChars) + XURL.PathSplitChars.length()); // /a/b/c/

		this.uUrlFormat = null;
		this.uHost = null;
		this.uPort = -1;
		this.uFileNameDetailed = null;
		this.uFileName = null;
		this.uLevelDomainSplit = null;
		this.uDirName = null;

		spec = null;
	}



	@Override
	public String toString() {
		// TODO: Implement this method
		return getUrlFormat();
	}

	public XURLParam getParam() {
		return new XURLParam(this);
	}




	private static String getFormatPathUrl0(String url) {
		if (XObjects.isEmpty(url))
			return url;
		StringBuilder buf = new StringBuilder();
		int size = url.length();
		boolean lastSplitChar = false;
		boolean urlAndParamSplitChar = false;
		for (int i = 0;i < size;i++) {
			if (url.charAt(i) == PathSplitchar) {
				if (!lastSplitChar || urlAndParamSplitChar)
					buf.append(PathSplitchar);
				lastSplitChar = true;
				continue;
			} else if (url.charAt(i) == UrlAndParamSplitChar) {
				urlAndParamSplitChar = true;
				buf.append(url.charAt(i));
			} else {
				lastSplitChar = false;
				buf.append(url.charAt(i));
			}
		}
		String newUrl = buf.toString();
		buf = null;
		//System.out.println(newUrl);
		return newUrl;
	}
	/*
	 getAbsAddres("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();").equals(new File("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();").getCanonicalPath()); >> true
	 getAbsAddres("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();"); >> "/XSt/*?:]/tt/a/b/v/x/a/v**v/();"
	 */
	public static String getAbsAddres(String a) {
		if (null == a)
			return null;
		XURL xurl = new XURL(a);
		String pathDetailed = xurl.getPathDetailed();
		if (null == xurl.getProtocol()) {
			StringBuilder sb = new StringBuilder().append(HttpProtocolSplitChars)
				.append(xurl.getUserAndHostAndPort())
				.append(getAbsPath(pathDetailed));
			if (null != xurl.getRef())
				sb.append(RefChars).append(xurl.getRef());
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder(xurl.getRoot())
				.append(getAbsPath(pathDetailed));
			if (null != xurl.getRef())
				sb.append(RefChars).append(xurl.getRef());
			return sb.toString();
		}
	}
	public static String getAbsPath(String a) {
		if (null == a)
			return null;
		a = getFormatPathUrl0(a);
		if (!a.startsWith(PathSplitChars)) 
			a = new StringBuilder(PathSplitChars).append(a).toString();
		String u = a.trim(); 
		a = null;
		XURL xurl = new XURL(u);
		String param = xurl.getFileNameDetailed();
		int ind;
		if ((ind = param.indexOf(UrlAndParamSplitChars)) > -1) {
			param = param.substring(ind, param.length());
		} else {
			param = "";
		}
		StringBuilder sb = new StringBuilder(parsePath0(xurl.getFilePath())).append(param);
		if (null != xurl.getRef())
			sb.append(RefChars).append(xurl.getRef());
		return sb.toString();
	}
	private static String parsePath0(String path) {
		int i = 0;
		while ((i = path.indexOf("/./")) >= 0) {
			path = path.substring(0, i) + path.substring(i + 2);
		}
		while ((i = path.indexOf("/../")) >= 0) {
			int last = path.lastIndexOf('/', i - 1);
			if (last >= 0) 
				path = path.substring(0, last) + path.substring(i + 3, path.length());
			else
				path = path.substring(0, i) + path.substring(i + 3, path.length());
		}
		// Remove trailing .. if possible
		i = path.length() - 3;
		if (path.endsWith("/..")) {
			int last = path.lastIndexOf('/', path.length() - 3 - 1);
			if (last >= 0) 
				path = path.substring(0, last + 1);
			else
				path = "/";
		}
		// Remove trailing .
		if (path.endsWith("/."))
			path = path.substring(0, path.length() - 1);
		return path;
	}
	public static XURL abs(String url) {
		return new XURL(getAbsAddres(url));
	}
	public XURL abs() {
		return abs(getUrlFormat());
	}
}

package top.fols.box.net;

import java.net.URL;
import java.io.IOException;
import top.fols.box.lang.XString;
public class XURL
{
	private String Url = null; // url
	private String Protocol = null;// http https ...
	private String HostAndPort = null;// xxx.xxx.xxx:xx
	private String Root = null;// http://xxx.xxx.xxx
	private String Dir = null;//  /a/
	private String Path = null;// /index.html
    private String HttpprotocolStr = "://";
	private String Host = null;
	private int Port = -1;
	private String FileNameDetailed = null;
	private String FileName = null;

	private String HttpUrlFormat(String url)
	{
		if (XString.isEmpty(url))
			return url;
		while (url.indexOf("//") > -1)
			url = url.replace("//",  "/");
		return url;
	}



	public String getUrl()
	{
		return Url;
	}
	public String getUrlFormat()
	{
		return  getRoot() + getPath();
	}
	public String getProtocol()
	{
		return Protocol;
	}
	public String getHost()
	{
		if (Host != null)
			return Host;
		String host = "";
		try
		{
			int start = getHostAndPort().indexOf(":");//int start =HostAndPort.lastIndexOf(":");
			if (start > -1)
				host = getHostAndPort().substring(0, start);
			else
				host = getHostAndPort();
			Host = host;
		}
		catch (Exception e)
		{
			Host = "";
		}
		return Host;
	}
	public int getPort()
	{
		if (Port != -1)
			return Port;
		int port = -1;
		try
		{
			int start =getHostAndPort().indexOf(":");//int start =HostAndPort.lastIndexOf(":");
			if (start > -1)
			{
				String portstr = getHostAndPort().substring(start + 1, getHostAndPort().length());
				if (!portstr.equals(""))
					port = Integer.parseInt(portstr.trim());
				else
					port = -1;
			}
			Port = port;
		}
		catch (Exception e)
		{
			Port = -1;
		}
		return Port;
	}
	public String getHostAndPort()
	{
		return HostAndPort;
	}
	public String getRoot()
	{
		return Root;
	}
	public String getDir()
	{
		return Dir;
	}
	public String getPath()
	{
		return Path;
	}
	public String getFileNameDetailed()
	{
		if (FileNameDetailed != null)
			return FileNameDetailed;
		String path = getPath();
		try
		{
			if (path.startsWith("/") == false)
				path =  "/" + path;
			int start = -1;
			/*
			 //http://127.0.0.1:7777/_HM4X/u&r??_#hash.html#?a=b
			 //u&r??_#hash.html#?a=b
			 */
			if ((start = path.lastIndexOf("/")) > -1)
				path = path.substring(start + 1, path.length());
			FileNameDetailed = path;
		}
		catch (Exception e)
		{
			FileNameDetailed = "";
		}
		return FileNameDetailed;
	}

	public String getFileName()
	{
		if (FileName != null)
			return FileName;
		String path = getPath();
		try
		{
			if (path.startsWith("/") == false)
				path =  "/" + path;
			int start = -1;
			if ((start = path.lastIndexOf("/")) > -1)
				path = path.substring(start + 1, path.length());
			/*
			 //http://127.0.0.1:7777/_HM4X/u&r??_#hash.html#?a=b
			 //u&r
			 */
			while ((start = path.lastIndexOf("?")) > -1)
				path = path.substring(0, start);
			while ((start = path.lastIndexOf("#")) > -1)
				path = path.substring(0, start);
			FileName = path;
		}
		catch (Exception e)
		{
			FileName = "";
		}
		return FileName;
	}


	public XURL(String url)
	{
		this(url, true);
	}
	public XURL(String url, boolean removeNotes)
	{
		if (url == null)
			url = "";
		//remove #
		int start = -1;
		if (removeNotes && (start = url.indexOf("#")) > -1)
			url = url.substring(0, start);
		init(url);
	}
	static String format(String str)
	{
		byte[] value = str.getBytes();
		int len = value.length;
        int st = 0;
        while ((st < len) && ((value[st] & 0xff) <= ' '))
		{
            st++;
        }
        return new String(value, st, len - st);
	}
	void init(String URL)
	{
		Url = URL;
		URL = format(URL);
		int protocolStrLen = HttpprotocolStr.length();
		int Protocolstart = URL.indexOf(HttpprotocolStr);
		if (Protocolstart > -1)
		{
			Protocol = URL.substring(0, Protocolstart);
			URL = URL.substring(protocolStrLen + Protocolstart, URL.length());
		}	
		else
		{
			Protocol = null;
			Protocolstart = 0;
		}	
		// http://ip.cn>>http://ip.cn/
		if (URL.indexOf("/") < 0)
			URL +=  "/";
		URL = HttpUrlFormat(URL);
		int HostAndPortEndstart = URL.indexOf("/");// http://ip.cn:8080/a/b/c/1.html >>17
		HostAndPort = URL.substring(0, HostAndPortEndstart);//  >>ip.cn:8080
		if (Protocol == null)
			Root = HostAndPort ;//  >>http://ip.cn:8080
		else
			Root = Protocol + HttpprotocolStr + HostAndPort ;//  >>http://ip.cn:8080
		URL = URL.substring(HostAndPortEndstart, URL.length());
		Path = URL;// /a/b/c/1.html
		Dir = URL.substring(0, URL.lastIndexOf("/") + 1); // /a/b/c/
		if (! Dir.startsWith("/"))
			Dir =  "/" + Dir;
		getHost();
		getPort();
		getFileName();
		getFileName();
	}

}

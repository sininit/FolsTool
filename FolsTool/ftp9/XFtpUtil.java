package top.fols.box.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.Closeable;

import java.util.Iterator;
import java.util.List;



import top.fols.box.net.base.ftp9.FtpClient;
import top.fols.box.net.base.ftp9.FtpProtocolException;
import top.fols.box.net.base.ftp9.FtpDirEntry;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.XMap;
import top.fols.box.statics.XStaticCommonlyValue;
import top.fols.box.util.XArrays;


public class XFtpUtil implements Closeable
{
	public static final String separator = new String("/");
	private FtpClient ftp = null;
	public FtpClient login(FtpClient c) throws FtpProtocolException, IOException
	{
		if (c == null)
			throw new NullPointerException();
		this.ftp = c;
		setType(FtpClient.TransferType.BINARY);
		return c;
	}
	public FtpClient login(String url, String username,
						   String password) throws FtpProtocolException, IOException
	{
		return login(url, FtpClient.defaultPort(), username, password);
	}
	public  FtpClient login(String url, int port, String username,
							String password) throws FtpProtocolException, IOException
	{
		// 创建地址
		SocketAddress addr = new InetSocketAddress(url, port);
		// 连接
		if (ftp != null)
			ftp.close();
		ftp = FtpClient.create();
		ftp.connect(addr);
		// 登陆
		ftp.login(username, password.toCharArray());
		setType(FtpClient.TransferType.BINARY);
		return ftp;
	}
	public FtpClient setType(FtpClient.TransferType type) throws FtpProtocolException, IOException
	{
		return ftp.setType(type);
	}
	public String getWelcomeMsg()
	{
		return ftp.getWelcomeMsg();
	}
	public file getFile(String name) throws FtpProtocolException, IOException
	{
		return new file(ftp, name);
	}
	public FtpClient getFtpClient()
	{
		return ftp;
	}

	public OutputStream putFileOutputStream(file f) throws FtpProtocolException, IOException
	{
		return ftp.putFileStream(f.getPath());
	}
	public InputStream getFileInputStream(file f) throws FtpProtocolException, IOException
	{
		return ftp.getFileStream(f.getPath());
	}


	public static class file
	{
		private FtpClient FtpClient;

		private String FilePath;
		private String Dir;
		private String name;

		private Object SyncLock = new Object();

		private XMap<FtpDirEntry> List = new XMap<FtpDirEntry>();
		private file(FtpClient d, String filePath, XMap<FtpDirEntry> m)
		{
			File f = new File(filePath);
			this.FtpClient = d;
			this.Dir = getDirPath(f);
			this.FilePath = f.getAbsolutePath();
			this.name = f.getName();
			this.List = m;
		}
		private file(FtpClient d, String filePath) throws FtpProtocolException, IOException
		{
			this(d, filePath, listFiles(d, getDirPath(new File(filePath))));
		}




		public static FtpDirEntry.Permission getPermission(FtpDirEntry d)
		{
			if (d == null)
				return null;
			String user = d.getUser();
			if (FtpDirEntry.Permission.USER.ordinal() == Integer.parseInt(user))
				return FtpDirEntry.Permission.USER;
			if (FtpDirEntry.Permission.OTHERS.ordinal() == Integer.parseInt(user))
				return FtpDirEntry.Permission.OTHERS;
			if (FtpDirEntry.Permission.GROUP.ordinal() == Integer.parseInt(user))
				return FtpDirEntry.Permission.GROUP;
			return null;
		}
		public boolean canExecute()
		{
			synchronized (SyncLock)
			{
				try
				{
					return getFtpDirEntry().canExexcute(getPermission(getFtpDirEntry()));
				}
				catch (Exception e)
				{
					return false;
				}
			}
		}
		public boolean canRead()
		{
			synchronized (SyncLock)
			{
				try
				{
					return getFtpDirEntry().canRead(getPermission(getFtpDirEntry()));
				}
				catch (Exception e)
				{
					return false;
				}
			}
		}
		public boolean canWrite()
		{
			synchronized (SyncLock)
			{
				try
				{
					return getFtpDirEntry().canWrite(getPermission(getFtpDirEntry()));
				}
				catch (Exception e)
				{
					return false;
				}
			}
		}

		public boolean mkdir() throws FtpProtocolException, IOException
		{
			synchronized (SyncLock)
			{
				checkExists();
				FtpClient.makeDirectory(FilePath);
				updateList();
				return true;
			}
		}
		public String getPath()
		{
			return FilePath;
		}
		public String getParent()
		{
			return Dir;
		}
		public String getName()
		{
			return name;
		}
		public boolean delete() throws FtpProtocolException, IOException
		{
			synchronized (SyncLock)
			{
				checkNotExists();
				FtpClient.deleteFile(FilePath);
				updateList();
				return true;
			}
		}
		public boolean createNewFile() throws FtpProtocolException, IOException
		{
			synchronized (SyncLock)
			{
				if (exists())
					return true;
				FtpClient.putFileStream(FilePath).close();
				updateList();
				return true;
			}
		}
		public long length()
		{
			if (!exists())
				return 0;
			return getFtpDirEntry().getSize();
		}
		public boolean exists()
		{
			return List.containsKey(name);
		}
		public FtpDirEntry getFtpDirEntry()
		{
			return List.get(name);
		}
		

		void updateList() throws FtpProtocolException, IOException
		{
			this.List = listFiles(FtpClient, getDirPath(new File(FilePath)));
		}
		void checkExists()
		{
			if (exists())
				throw new IllegalAccessError("exists file " + FilePath);
		}
		void checkNotExists()
		{
			if (!exists())
				throw new IllegalAccessError("not exists file " + FilePath);
		}
		String getDir()
		{
			return Dir;
		}

		public file[] list() throws FtpProtocolException, IOException
		{
			return list(FilePath);
		}
		public file[] list(String FilePath) throws FtpProtocolException, IOException
		{
			XMap<FtpDirEntry> List = listFiles(FtpClient, FilePath);
			List<file> newlist = new ArrayListUtils<file>();
			List<String> keys = List.keys();
			for (String e : keys)
			{
				newlist.add(new file(FtpClient, getDir() + e, List));
			}
			return (file[])XArrays.copyOf(newlist.toArray(), file.class);
		}
		public String toString()
		{
			checkNotExists();
			return Dir +  name;
		}



		public boolean isLink()
		{
			checkNotExists();
			return getFtpDirEntry().getType() == FtpDirEntry.Type.LINK;
		}
		public boolean isFile()
		{
			checkNotExists();
			return getFtpDirEntry().getType() == FtpDirEntry.Type.FILE;
		}
		public boolean isCDir()
		{
			checkNotExists();
			return getFtpDirEntry().getType() == FtpDirEntry.Type.CDIR;
		}
		public boolean isDir()
		{
			checkNotExists();
			return getFtpDirEntry().getType() == FtpDirEntry.Type.DIR;
		}

		private static String getDirPath(File file)
		{
			File f = file;
			String Dir = f.getParent();
			if (Dir == null)
				Dir = separator;
			else
			{
				if (!Dir.startsWith(separator))
					Dir = separator + Dir;
				if (!Dir.endsWith(separator))
					Dir = Dir += separator;
			}
			return Dir;
		}
		public static XMap<FtpDirEntry> listFiles(FtpClient ftp, String filepath) throws FtpProtocolException, IOException
		{
			XMap<FtpDirEntry> List = new XMap<FtpDirEntry>();
			Iterator<FtpDirEntry> entrylist = ftp.listFiles(filepath);
			FtpDirEntry e;
			while (entrylist.hasNext())
			{
				e = entrylist.next();
				String fileName;
				fileName = getName(e);
				List.put(fileName, e);
			}
			return List;
		}
		private static String getName(FtpDirEntry FtpDirEntry)
		{
			return FtpDirEntry.getName();
		}
	}

	/**
	 * 关闭ftp
	 * 
	 * @param ftp
	 */
	public void close() throws IOException
	{
		ftp.close();
	}
}


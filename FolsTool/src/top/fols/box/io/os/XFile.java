package top.fols.box.io.os;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XOutputStreamFixedLength;
import top.fols.box.statics.XStaticFixedValue;
public class XFile
{

	private final String fileCanonicalPath;
	private RandomAccessFile r;
	public XFile(String file)
	{
		this(new File(file));
	}
	public XFile(File file)
	{
		try
		{
			this.fileCanonicalPath = file.getCanonicalPath();
		}
		catch (IOException e)
		{
			this.fileCanonicalPath = file.getAbsolutePath();
		}
	}
	void init()throws IOException
	{
		if (r == null)
			r = new RandomAccessFile(fileCanonicalPath, XStaticFixedValue.FileValue.getRandomAccessFile_Mode_RW_String());
	}



	/*
	 get Extension Name
	 得到扩展名
	 */
	public String getExtensionName()
	{ 
		if ((fileCanonicalPath != null) && (fileCanonicalPath.length() > 0))
		{ 
            int dot = fileCanonicalPath.lastIndexOf('.'); 
            if ((dot > -1) && (dot < (fileCanonicalPath.length() - 1)))
			{ 
                return fileCanonicalPath.substring(dot + 1, fileCanonicalPath.length()); 
            } 
        } 
        return null; 
    } 
	/*
	 get Name
	 获得 文件名 带后缀
	 */
	public String getName()
	{ 
        if ((fileCanonicalPath != null) && (fileCanonicalPath.length() > 0))
		{ 
            int dot = fileCanonicalPath.lastIndexOf(File.separator); 
            if ((dot > -1) && (dot < (fileCanonicalPath.length())))
			{ 
				return fileCanonicalPath.substring(dot + 1, fileCanonicalPath.length()); 
            } 
        } 
        return fileCanonicalPath; 
    } 
	/*
	 get Name No Ex
	 获得文件名不带扩展名 不带路径
	 */
	public String getNameNoExtension()
	{ 
        if ((fileCanonicalPath != null) && (fileCanonicalPath.length() > 0))
		{ 
            int dot = fileCanonicalPath.lastIndexOf('.'); 
            if ((dot > -1) && (dot < (fileCanonicalPath.length())))
			{ 
			    int dot2 = fileCanonicalPath.lastIndexOf(File.separator, dot);
				if (dot2 > -1)
					return fileCanonicalPath.substring(dot2 + 1, dot);
				return fileCanonicalPath.substring(0, dot); 
            } 
        } 
        return null; 
    } 

	public InputStream getRangeInputStream(long off, long len) throws IOException
	{
		XRandomAccessFileInputStream in = new XRandomAccessFileInputStream(fileCanonicalPath);
		in.seekIndex(off);
		return new XInputStreamFixedLength(in, len);
	}
	public OutputStream getRangeOutputStream(long off, long len) throws IOException
	{
		XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(fileCanonicalPath);
		out.seekIndex(off);
		return new XOutputStreamFixedLength(out, len);
	}


	public String getPath()
	{
		return fileCanonicalPath;
	}

	public XFile append(String bytes) throws IOException
	{
		byte[] bytes2 = bytes.getBytes();
		return append(bytes2, 0, bytes2.length);
	}
	public XFile append(String bytes, String encoding) throws IOException
	{
		byte[] bytes2 = bytes.getBytes(encoding);
		return append(bytes2, 0, bytes2.length);
	}
	public XFile append(byte[] bytes) throws  IOException
	{
		return append(bytes, 0, bytes.length);
	}
	public XFile append(byte[] bytes, int off, int len) throws IOException
	{
		init();
		if (len < 0)
			return this;
		r.seek(r.length());
		r.write(bytes, off, len);
		return this;
	}
	public XFile append(InputStream in, long len) throws IOException
	{
		init();
		if (len < 0)
			return this;
		r.seek(r.length());
		XStream.copyFixedLength(in, new XRandomAccessFileOutputStream(r), len);
		return this;
	}
	public XFile append(int p1) throws FileNotFoundException, IOException
	{
		init();
		r.seek(r.length());
		r.write(p1);
		return this;
	}





	public XFile empty() throws IOException
	{
		init();
		r.setLength(0);
		return this;
	}
	public long length() throws IOException
	{
		init();
		return r.length();
	}
	public String lengthFormat() throws IOException
	{
		return XFileTool.fileFormatSize(String.valueOf(length()));
	}
	public String toString()
	{
		try
		{
			return new String(getBytes());
		}
		catch (IOException e)
		{
			throw new ArrayStoreException("io exception");
		}
	}
	public String toString(String encoding) throws IOException
	{
		return new String(getBytes(), encoding);
	}
	public byte[] getBytes() throws IOException
	{
		InputStream in = new XRandomAccessFileInputStream(fileCanonicalPath);
		byte[] b = XStream.inputstream.toByteArray(in);
		in.close();
		return b;
	}
	public byte[] getBytes(long off, long len) throws IOException
	{
		InputStream in = getRangeInputStream(off, len);
		byte[] b = XStream.inputstream.toByteArray(in);
		in.close();
		return b;
	}
	public XFileEdit.ReadOption toFileEditReadOption() throws FileNotFoundException, IOException
	{
		return new XFileEdit.ReadOption(new File(fileCanonicalPath));
	}
	public XFileEdit.WriteOption toFileEditWriteOption() throws FileNotFoundException, IOException
	{
		return new XFileEdit.WriteOption(new File(fileCanonicalPath));
	}
	public InputStream toInputStream() throws IOException
	{
		return new XRandomAccessFileInputStream(fileCanonicalPath);
	}
	public OutputStream toOutputStream() throws IOException
	{
		return new XRandomAccessFileOutputStream(fileCanonicalPath);
	}



	public XFile copyTo(XFile f) throws IOException
	{
		return copyTo(f.fileCanonicalPath);
	}
	public XFile copyTo(File f) throws IOException
	{
		return copyTo(f.getCanonicalPath());
	}
	public XFile copyTo(String f) throws IOException
	{
		return copyTo(f, true);
	}
	public XFile copyTo(String f, boolean check) throws IOException
	{
		File originFile = new File(fileCanonicalPath);
		File copyToFile = new File(f);
		if (check && copyToFile.exists())
			throw new IOException("file exist");
		XRandomAccessFileOutputStream out = new XRandomAccessFileOutputStream(f);
		out.setLength(0);
		out.seekIndex(0);
		XStream.copy(new XRandomAccessFileInputStream(originFile), out);
		return this;
	}





}

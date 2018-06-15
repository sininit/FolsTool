package top.fols.box.io.base;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import top.fols.box.util.XCycleSpeedLimiter;
import top.fols.box.util.XMap;
public abstract class XIOLimiter
{
	private static final XMap<XCycleSpeedLimiter> listStatic = new XMap<XCycleSpeedLimiter>();
	private static final Object statico = new Object();
	public static XCycleSpeedLimiter get(String key)
	{
		synchronized (statico)
		{
			XCycleSpeedLimiter i = listStatic.get(key);
			if (i == null)
				listStatic.put(key, i = new XCycleSpeedLimiter());
			return i;
		}
	}
	public static List<String> keys()
	{
		synchronized (statico)
		{
			return listStatic.keys();
		}
	}
	public static void remove(String key)
	{
		synchronized (statico)
		{
			listStatic.remove(key);
		}
	}
	public static void clear(String key)
	{
		synchronized (statico)
		{
			listStatic.clear();
		}
	}
	public static int size()
	{
		synchronized (statico)
		{
			return listStatic.size();
		}
	}
	public static XCycleSpeedLimiter newInstance()
	{
		return new XCycleSpeedLimiter();
	}














	public static InputStream wrap(InputStream in, XCycleSpeedLimiter m)
	{
		return new XIOLimiterInputStream(in, m);
	}
	public static OutputStream wrap(OutputStream in, XCycleSpeedLimiter m)
	{
		return new XIOLimiterOutputStream(in, m);
	}
	private static class XIOLimiterInputStream extends InputStream
	{
		private InputStream stream;
		private XCycleSpeedLimiter m;
		public int read() throws java.io.IOException
		{
			if (m != null)
				m.waitForFreeInt(1);
			return stream.read();
		}
		public int read(byte[] b) throws IOException
		{
			return read(b, 0, b.length);
		}
		public int read(byte[] b, int off, int len) throws java.io.IOException
		{
			if (m != null)
				m.waitForFreeInt(len);
			return stream.read(b);
		}
		public long skip(long n) throws java.io.IOException
		{
			return stream.skip(n);
		}
		public int available() throws java.io.IOException
		{
			return stream.available();
		}
		public void close() throws java.io.IOException
		{
			stream.close();
		}
		public synchronized void mark(int readlimit)
		{
			stream.mark(readlimit);
		}
		public synchronized void reset() throws java.io.IOException
		{
			stream.reset();
		}
		public boolean markSupported()
		{
			return stream.markSupported();
		}
		
		
		
		public InputStream getStream()
		{
			return stream;
		}
		public XIOLimiterInputStream(InputStream in, XCycleSpeedLimiter m)
		{
			this.stream = in;
			this.m = m;
		}
		public XCycleSpeedLimiter getSpeedLimiter()
		{
			return this.m;
		}
	}
	private static class XIOLimiterOutputStream extends OutputStream
	{
		private OutputStream stream;
		private XCycleSpeedLimiter m;
		public void write(int p1) throws java.io.IOException
		{
			if (m != null)
				m.waitForFreeInt(1);
			stream.write(p1);
		}
		public void write(byte[] b) throws java.io.IOException
		{
			write(b, 0, b.length);
		}
		public void write(byte[] b, int off, int len) throws java.io.IOException
		{
			if (m != null)
				m.waitForFreeInt(len);
			stream.write(b, off, len);
		}
		public void flush() throws java.io.IOException
		{
			stream.flush();
		}
		public void close() throws java.io.IOException
		{
			stream.close();
		}
		
		
		
		public XIOLimiterOutputStream(OutputStream in, XCycleSpeedLimiter m)
		{
			this.stream = in;
			this.m = m;
		}
		
		public OutputStream getStream()
		{
			return stream;
		}
		public XCycleSpeedLimiter getSpeedLimiter()
		{
			return this.m;
		}
	}









}

package top.fols.box.io.base;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.io.os.XRandomAccessFileInputStream;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.XArrayPieceManager;
import top.fols.box.util.XEncodingDetect;
import top.fols.box.util.XObjects;

public class XInputStreamReaderPiece implements Closeable
{
	private void init(InputStream stream, int pieceSize, boolean cache, Charset charset) throws FileNotFoundException, IOException
	{
		this.stream = XObjects.requireNonNull(stream);
		Charset encoding;
		if (charset == null)
		{
			this.stream.reset();
			encoding = XEncodingDetect.getJavaEncode2Charset(stream);
		}
		else
		{
			encoding = charset;
		}
		this.stream.reset();
		this.streamReader = new InputStreamReader(stream, encoding);

		long Count = 0;
		char[] buff = new char[8192 * 4];
		int read;
		while ((read = this.streamReader.read(buff)) != -1)
			Count += read;

		this.pieceSize = pieceSize;
		this.charCount = Count;
		this.encoding = encoding;
		this.index = -1;
		this.xpm = new XArrayPieceManager(charCount, pieceSize);
		this.now = new char[pieceSize];
		this.stream.reset();
		this.streamReader = new InputStreamReader(this.stream, encoding);
		this.cache = cache;
		if (cache)
			cacheList = new ArrayListUtils<>();
	}
	private int pieceSize;
	private long charCount;
	private Charset encoding;

	private InputStream stream;
	private InputStreamReader streamReader;
	private long index;
	private XArrayPieceManager xpm;
	private char[] now;
	private Object sync = new Object();
	private boolean cache = false;
	private List<char[]> cacheList;
	public XInputStreamReaderPiece(@XAnnotations("need support reset and after reset index for 0")  InputStream stream) throws IOException
	{
		this(stream, XArrayPieceManager.pieceBufDefaultSize, true);
	}
	public XInputStreamReaderPiece(@XAnnotations("need support reset and after reset index for 0")  InputStream stream, int pieceSize) throws IOException
	{
		this(stream, pieceSize, true);
	}
	public XInputStreamReaderPiece(@XAnnotations("need support reset and after reset index for 0")  InputStream stream, int pieceSize, boolean cache) throws IOException
	{
		this(stream, pieceSize, cache, null);
	}
	public XInputStreamReaderPiece(@XAnnotations("need support reset and after reset index for 0")  InputStream stream, int pieceSize, boolean cache, Charset encoding) throws IOException
	{
		if (pieceSize < 1)
			throw new RuntimeException("pieceSize=" + pieceSize + ", min=1");
		this.init(stream, pieceSize, cache, encoding);
	}


	public XInputStreamReaderPiece(File stream) throws IOException
	{
		this(stream, XArrayPieceManager.pieceBufDefaultSize, true);
	}
	public XInputStreamReaderPiece(File stream, int pieceSize) throws IOException
	{
		this(stream, pieceSize, true);
	}
	public XInputStreamReaderPiece(File stream, int pieceSize, boolean cache) throws IOException
	{
		this(stream, pieceSize, cache, null);
	}
	public XInputStreamReaderPiece(File stream, int pieceSize, boolean cache, Charset encoding) throws IOException
	{
		if (pieceSize < 1)
			throw new RuntimeException("pieceSize=" + pieceSize + ", min=1");
		this.init(new XRandomAccessFileInputStream(stream), pieceSize, cache, encoding);
	}

	private void growCacheList(long newIndex)
	{
		synchronized (sync)
		{
			long len = newIndex + 1;
			if (len > Integer.MAX_VALUE)
				throw new OutOfMemoryError("cache error");
			for (long i = 0;i < len - cacheList.size();i++)
				cacheList.add(null);
		}
	}
	private void growFixedPiece(long piece) throws IOException
	{
		synchronized (sync)
		{
			if (piece == this.index)
				return;
			if (!hasPiece(piece))
			{
				now = null;
				return;
			}
			if (piece > this.index)
			{
				long forLength = piece - this.index;
				for (int i = 0;i < forLength;i++)
					nextPiece();
			}
			else
			{
				long forLength = this.index - piece;
				for (int i = 0;i < forLength;i++)
					previousPiece();
			}
		}
	}
	private void growPreviousPiece()
	{
		synchronized (sync)
		{
			if (!cache)
				throw new RuntimeException("no cache");
			if (!hasPreviousPiece())
			{
				now = null;
				return;
			}
			now = this.cacheList.get((int)this.index--);
			return;
		}
	}
	private void growNextPiece() throws IOException
	{
		synchronized (sync)
		{
			if (!hasNextPiece())
			{
				now = null;
				return;
			}
			if (cache)
			{
				growCacheList(index + 1);
				cacheList.set((int)index + 1, now);
			}
			int read;
			now = new char[pieceSize];
			read = streamReader.read(now);
			if (read != now.length)
				now = Arrays.copyOfRange(now, 0, read);
			this.index++;
			return;
		}
	}
	public Charset getEncoding()
	{
		return encoding;
	}



	public long getPieceBuffSize()
	{
		return xpm.getPieceBufSize();
	}
	public long getIndexPiece(long newPiece)
	{
		return xpm.getIndexPiece(newPiece);
	}
	public long getPieceLength(long newPiece)
	{
		return xpm.getPieceLength(newPiece);
	}
	public long getPieceIndexEnd(long newPiece)
	{
		return xpm.getPieceIndexEnd(newPiece);
	}
	public long getPieceIndexStart(long newPiece)
	{
		return xpm.getPieceIndexStart(newPiece);
	}
	public long getPieceCount()
	{
		return xpm.getPieceNumber();
	}
	public long length()
	{
		return xpm.length();
	}
	public long getNowPiece()
	{
		return index;
	}


	public boolean hasPiece(long piece)
	{
		return piece > -1 && piece  < xpm.getPieceNumber();
	}
	public char[] nextPiece(long piece) throws IOException
	{
		synchronized (sync)
		{
			growFixedPiece(piece);
			return now;
		}
	}

	public boolean hasNextPiece()
	{
		synchronized (sync)
		{
			return hasPiece(index + 1);
		}
	}
	public char[] nextPiece() throws IOException
	{
		synchronized (sync)
		{
			growNextPiece();
			return now;
		}
	}

	public boolean hasPreviousPiece()
	{
		synchronized (sync)
		{
			if (!cache)
				return false;
			return index - 1 > -1;
		}
	}
	public char[] previousPiece()
	{
		synchronized (sync)
		{
			growPreviousPiece();
			return now;
		}
	}


	public boolean cache()
	{
		return cache;
	}
	@Override
	public void close() throws IOException
	{
		stream.close();
		streamReader.close();
		cacheList.clear();
		cache = false;
	}

}


package top.fols.box.io.os;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import top.fols.box.io.interfaces.XInterfaceRandomAccessInputStream;
import top.fols.box.io.interfaces.XInterfaceRandomAccessOutputStream;
import top.fols.box.util.XArrayPieceManager;
import top.fols.box.util.sequence.interfaces.XInterfaceSequenceIOBigByte;
public class XFileEdit
{
	public static class ReadOption implements Closeable,XInterfaceSequenceIOBigByte
	{
		public static ReadOption wrap(File file) throws IOException
		{
			return new ReadOption(file);
		}
		private int pieceBufSize;//BufferSize
		private long nowpiece = -1;//当前块
		private byte[] nowpieceData;
		private XArrayPieceManager xpm;
		private XInterfaceRandomAccessInputStream randomFileStream;
		private long length;
		
		private ReadOption()
		{}
		public ReadOption(XInterfaceRandomAccessInputStream randomFileStream) throws IOException
		{
			this(randomFileStream,XArrayPieceManager.pieceBufDefaultSize);
		}
		public ReadOption(XInterfaceRandomAccessInputStream randomFileStream, int PieceBufSize) throws IOException
		{
			if (PieceBufSize < 1)
				throw new RuntimeException("pieceSize=" + PieceBufSize + ", min=1");
			this.randomFileStream = randomFileStream;
			this.pieceBufSize = PieceBufSize;
			this.nowpieceData = new byte[PieceBufSize];
			this.xpm = new XArrayPieceManager(randomFileStream.length(), PieceBufSize);
			this.length = xpm.length();
		}
		public ReadOption(File file, int PieceBufSize) throws FileNotFoundException, IOException
		{
			this(new XRandomAccessFileInputStream(file), PieceBufSize);
		}
		public ReadOption(File file) throws FileNotFoundException, IOException
		{
			this(new XRandomAccessFileInputStream(file), XArrayPieceManager.pieceBufDefaultSize);
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
			return length;
		}


		public byte[] getPieceBytes(long newPiece) throws IOException
		{
			long off = xpm.getPieceIndexStart(newPiece);
			int len = (int)xpm.getPieceLength(newPiece);
			return getBytes(off, len);
		}
		public void seekPiece(long newPiece) throws IOException
		{
			grow(xpm.getPieceIndexStart(newPiece));
		}
		public byte[] getNowPieceBytes()
		{
			return nowpieceData;
		}
		public int getNowPieceLength()
		{
			return this.nowpieceData.length;
		}
		public long getNowPiece()
		{
			return this.nowpiece;
		}
		private long nowPieceStart = -1;
		private long nowPieceEnd = -1;
		private void grow(long index) throws IOException
		{
			long newPiece = xpm.getIndexPiece(index);
			if(newPiece != nowpiece)
			{
				long start = xpm.getPieceIndexStart(newPiece);
				long end = xpm.getPieceIndexEnd(newPiece);
				randomFileStream.seekIndex(start);
				int len = (int)xpm.getPieceLength(newPiece);
				if (nowpieceData.length != len)
					nowpieceData = new byte[len];
				randomFileStream.read(nowpieceData, 0, nowpieceData.length);
				this.nowpiece = newPiece;

				nowPieceStart = start;
				nowPieceEnd = end;
			}
		}
		public byte byteAt(long index) throws IOException
		{
			if (index < 0 || index >= length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s filelength=%s", index, length));
			if (!(nowPieceStart >= index && nowPieceEnd <= index))
				grow(index);
			return nowpieceData[(int)(index % pieceBufSize)];
		}
		public byte[] getBytes(long index, int len) throws IOException
		{
			if (index + len > length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s len=%s filelength=%s", index, len, length));
			byte[] bytes;
			randomFileStream.seekIndex(index);
			randomFileStream.read(bytes = new byte[len]);
			return bytes;
		}



		public long indexOf(byte b, long startIndex, long indexRange) throws IOException
		{
			startIndex = startIndex < 0 ? 0: startIndex;
			indexRange = indexRange < length ? indexRange : length;
			for (long i = startIndex; i < indexRange; i++)
				if (byteAt(i) == b)
					return i;
			return -1;
		}
		public long indexOf(byte[] b, long startIndex, long indexRange) throws IOException
		{
			if (length == 0 || startIndex > indexRange || b == null || b.length > length || b.length == 0 || indexRange - startIndex + 1 < b.length)
				return -1;
			startIndex = startIndex < 0 ? 0: startIndex;
			indexRange = indexRange < length ? indexRange : length;
			int i2;
			for (long i = startIndex; i < indexRange; i++)
			{
				if (byteAt(i) == b[0])
				{
					if (indexRange - i < b.length)
						break;
					for (i2 = 1; i2 < b.length; i2++)
						if (byteAt(i + i2) != b[i2])
							break;
					if (i2 == b.length)
						return i;
				}
			}
			return -1;
		}
		public long lastIndexOf(byte b, long startIndex, long indexRange) throws IOException
		{
			if (length == 0 || indexRange > startIndex)
				return -1;
			indexRange = indexRange < 0 ? 0: indexRange;
			if (startIndex > length - 1) 
				startIndex = length - 1;
			while (startIndex >= indexRange)
			{
				if (byteAt(startIndex) == b)
					return startIndex;
				startIndex--;
			}
			return -1;
		}
		public long lastIndexOf(byte[] b, long startIndex, long indexRange) throws IOException 
		{
			if (length == 0 || indexRange > startIndex || b == null || b.length > length || b.length == 0 || startIndex - indexRange + 1 < b.length)
				return -1;
			indexRange = indexRange < 0 ? 0: indexRange;
			if (startIndex > length) 
				startIndex = length;
			long i;
			int i2;
			for (i = startIndex == length ?length - 1: startIndex; i >= indexRange; i--)
			{
				if (byteAt(i) == b[0])
				{
					if (i + b.length > startIndex)
						continue;
					for (i2 = 1; i2 < b.length; i2++)
						if (byteAt(i + i2) != b[i2])
							break;
					if (i2 == b.length)
						return i;
				}
			}
			return -1;
		}

		public int read(long index)throws IOException
		{
			if (index < 0 || index >= length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s filelength=%s", index, length));
			if (!(nowPieceStart >= index && nowPieceEnd <= index))
				grow(index);
			return nowpieceData[(int)(index % pieceBufSize)];
		}
		public int read(long index , byte[] b) throws IOException
		{
			return read(index, b, 0, b.length);
		}
		public int read(long index , byte[] b, int boff, int blen) throws IOException
		{
			if (index < 0 || index >= length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s filelength=%s", index, length));
			if (index + blen > length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s off=%s len=%s filelength=%s", index, boff, blen, length));
			randomFileStream.seekIndex(index);
			return randomFileStream.read(b, boff, blen);
		}
		public void close() throws IOException
		{
			randomFileStream.close();
		}
	}



	public static class WriteOption implements Closeable
	{
		public static WriteOption wrap(File file) throws IOException
		{
			return new WriteOption(file);
		}

		private int pieceBufSize;//BufferSize
		private XArrayPieceManager xpm;
		private XInterfaceRandomAccessOutputStream randomFileStream;
		private long length;
		
		public WriteOption(XInterfaceRandomAccessOutputStream randomFileStream, int PieceBufSize) throws IOException
		{
			if (PieceBufSize < 1)
				PieceBufSize = XArrayPieceManager.pieceBufDefaultSize;
			this.randomFileStream = randomFileStream;
			this.pieceBufSize = PieceBufSize;
			this.xpm = new XArrayPieceManager(randomFileStream.length(), PieceBufSize);
			this.length = xpm.length();
		}
		public WriteOption(File file) throws FileNotFoundException, IOException
		{
			this(new XRandomAccessFileOutputStream(file), XArrayPieceManager.pieceBufDefaultSize);
		}


		public void setPieceBytes(long newPiece, byte[] bytes, int boff, int blen) throws IOException
		{
			if (newPiece > xpm.getPieceNumber())
				throw new IndexOutOfBoundsException(String.format("need piece=%s, max piece=%s", newPiece, xpm.getPieceNumber()));
			if (blen != pieceBufSize)
				throw new IndexOutOfBoundsException(String.format("b.length=%s, piece.length=%s", bytes.length, pieceBufSize)); 
			long off = xpm.getPieceIndexStart(newPiece);
			write(off, bytes, boff, blen);
		}
		public void writePieceBytes(long newPiece, byte[] bytes, int boff, int blen) throws IOException
		{
			long off = xpm.getPieceIndexStart(newPiece);
			long end = xpm.getPieceIndexEnd(newPiece);
			if (blen + off >= end)
				blen = (int)xpm.getPieceLength(newPiece);
			write(off, bytes, boff, blen);
		}
		public int write(long index, int b) throws IOException
		{
			if (!(index < length))
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s, filelength=%s", index, length));
			randomFileStream.seekIndex(index);
			randomFileStream.write(b);
			return 1;
		}
		public int write(long index, byte[] b) throws IOException
		{
			return write(index, b, 0, b.length);
		}
		public int write(long index, byte[] b, int boff, int blen) throws IOException
		{
			if (index + blen > length)
				throw new ArrayIndexOutOfBoundsException(String.format("index=%s, b.length=%s, off=%s, len=%s, filelength=%s", index, b.length, boff, blen, length));
			randomFileStream.seekIndex(index);
			randomFileStream.write(b, boff, blen);
			return blen;
		}

		public void close() throws IOException
		{
			randomFileStream.close();
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
			return length;
		}
	}
}

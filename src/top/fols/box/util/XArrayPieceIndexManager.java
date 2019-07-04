package top.fols.box.util;
import java.io.Serializable;

public class XArrayPieceIndexManager implements Serializable{
	private static final long serialVersionUID = 1270505843449124105L;

	private long length;
	private long perPieceSize;
	
	private long pieceCount;
	
	
	public XArrayPieceIndexManager(long length, long PerPieceSize) {
		updatepPieceInfo(length, PerPieceSize);
	}
	public XArrayPieceIndexManager(long PerPieceSize) {
		updatepPieceInfo(Long.MAX_VALUE, PerPieceSize);
	}
	
	
	
	public XArrayPieceIndexManager updatepPieceInfo(long PerPieceSize) {
		return updatepPieceInfo(this.length, PerPieceSize);
	}
	public XArrayPieceIndexManager updatepPieceInfo(long Length, long PerPieceSize) {
		if (PerPieceSize < 1)
			throw new RuntimeException("perPieceSize=" + PerPieceSize + ", min=1");
		if (Length < 0)
			throw new RuntimeException("length=" + Length + ", min=0");

		this.perPieceSize = PerPieceSize;
		this.length = Length;
		this.pieceCount = getIndexPiece(Length);
		if (this.pieceCount == 0 && Length > 0)
			++this.pieceCount;
		else if (((this.pieceCount - 1) * this.perPieceSize) + this.perPieceSize != Length)
			++this.pieceCount;
		return this;
	}
	
	public long getPieceIndexStart(long newPiece) {
		if (newPiece < 0 || newPiece >= pieceCount)
			throw new IndexOutOfBoundsException(String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", newPiece, 0, pieceCount));
		return newPiece * this.perPieceSize;
	}
	public long getPieceIndexEnd(long newPiece) {
		if (newPiece < 0 || newPiece >= pieceCount)
			throw new IndexOutOfBoundsException(String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", newPiece, 0, pieceCount));
		long end = (newPiece * this.perPieceSize) + this.perPieceSize;
		if (end < 1)
			end = Long.MAX_VALUE;
		if (end >= length)
			return length - 1;
		return end - 1;
	}


	public long getPieceLength(long newPiece) {
		return (getPieceIndexEnd(newPiece) + 1) - getPieceIndexStart(newPiece);
	}
	public long getPieceNumber() {
		return this.pieceCount;
	}
	public long getPieceBufSize() {
		return this.perPieceSize;
	}
	public long getIndexPiece(long index) {
		long newPiece = index / this.perPieceSize;
		return newPiece;
	}
	public long length() {
		return length;
	}
}

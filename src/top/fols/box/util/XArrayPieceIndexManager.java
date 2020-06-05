package top.fols.box.util;

import java.io.Serializable;

public class XArrayPieceIndexManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private long length;
	private long eachPieceSize;

	private long pieceCount;

	public XArrayPieceIndexManager(long eachPieceSize) {
		this.updatepPieceInfo(Long.MAX_VALUE, eachPieceSize);
	}

	public XArrayPieceIndexManager(long length, long eachPieceSize) {
		this.updatepPieceInfo(length, eachPieceSize);
	}

	public XArrayPieceIndexManager updatepPieceInfo(long eachPieceSize) {
		return this.updatepPieceInfo(this.length, eachPieceSize);
	}

	/**
	 * @param len           array.length
	 * @param eachPieceSize each block size
	 */
	public XArrayPieceIndexManager updatepPieceInfo(long len, long eachPieceSize) {
		if (len < 0) {
			throw new RuntimeException("length=" + len + ", min=0");
		}
		if (eachPieceSize < 1) {
			throw new RuntimeException("perPieceSize=" + eachPieceSize + ", min=1");
		}

		this.eachPieceSize = eachPieceSize;
		this.length = len;
		this.pieceCount = getIndexPiece(len);
		if (this.pieceCount == 0 && len > 0) {
			++this.pieceCount;
		} else if (((this.pieceCount - 1) * this.eachPieceSize) + this.eachPieceSize != len) {
			++this.pieceCount;
		}
		return this;
	}

	/**
	 * @return start pointer of the block, starting from 0
	 */
	public long getPieceIndexStart(long newPiece) {
		if (newPiece < 0 || newPiece >= pieceCount) {
			throw new IndexOutOfBoundsException(
					String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", newPiece, 0, pieceCount));
		}
		return newPiece * this.eachPieceSize;
	}

	/**
	 * @return end pointer of the block, (piece index) + (piece len) - 1
	 */
	public long getPieceIndexEnd(long newPiece) {
		if (newPiece < 0 || newPiece >= pieceCount) {
			throw new IndexOutOfBoundsException(
					String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", newPiece, 0, pieceCount));
		}
		long end = (newPiece * this.eachPieceSize) + this.eachPieceSize;
		if (end < 1) {
			end = Long.MAX_VALUE;
		}
		if (end >= length) {
			return length - 1;
		}
		return end - 1;
	}

	/**
	 * @return piece size, starting from 0
	 */
	public long getPieceSize(long newPiece) {
		return (getPieceIndexEnd(newPiece) + 1) - getPieceIndexStart(newPiece);
	}

	/**
	 * @return piece count
	 */
	public long getPieceCount() {
		return this.pieceCount;
	}

	/**
	 * @return each piece size
	 */
	public long getEachPieceSize() {
		return this.eachPieceSize;
	}

	/**
	 * get the block where the pointer is, starting from 0
	 */
	public long getIndexPiece(long index) {
		long newPiece = index / this.eachPieceSize;
		return newPiece;
	}

	public long length() {
		return length;
	}
}

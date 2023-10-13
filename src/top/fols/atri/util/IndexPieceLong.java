package top.fols.atri.util;

import java.io.Serializable;
import top.fols.atri.util.IndexPieces;

public class IndexPieceLong implements Serializable {
	private static final long serialVersionUID = 1L;


	private long length;
	private long eachPieceSize;
	private long pieceCount;

	public IndexPieceLong() {}
	public IndexPieceLong(long eachPieceSize) {
		this.setEachPieceSize(eachPieceSize);
	}
	public IndexPieceLong(long length, long eachPieceSize) {
		this.setLength(length);
		this.setEachPieceSize(eachPieceSize);
	}




	public void setLength(long length) {
		this.length     = length;
		this.pieceCount = IndexPieces.getLongPieceCount(this.length, this.eachPieceSize);
	}
	public void setEachPieceSize(long eachPieceSize) {
		this.eachPieceSize = eachPieceSize;
		this.pieceCount = IndexPieces.getLongPieceCount(this.length, this.eachPieceSize);
	}



	/**
	 * @return start pointer of the block, starting from 0
	 */
	public long getPieceIndexStart(long piece) {
		return IndexPieces.getLongPieceIndexStart(piece, pieceCount,
												  length, eachPieceSize);
	}

	/**
	 * @return end pointer of the block, (piece index) + (piece len) - 1
	 */
	public long getPieceIndexEnd(long piece) {
		return IndexPieces.getLongPieceIndexEnd(piece, pieceCount,
												length, eachPieceSize);
	}

	/**
	 * @return piece size, starting from 0
	 */
	public long getPieceSize(long piece) {
		return IndexPieces.getLongPieceSize(piece, pieceCount,
											length, eachPieceSize);
	}

	/**
	 * get the block where the pointer is, starting from 0
	 */
	public long getPieceFromIndex(long index) {
		return IndexPieces.getLongPieceFromIndex(index, eachPieceSize);
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

	public long getLength() {
		return length;
	}



	public void ergodic(PieceErgodic ergodic) {
		ergodic(IndexPieces.PIECE_OFFSET, getPieceCount(), ergodic);
	}
	public void ergodic(long offsetPiece, long pcount, PieceErgodic ergodic) {
		for (long pi = 0; pi < pcount; pi++) {
			long piece = offsetPiece + pi;
			long start = getPieceIndexStart(piece);
			long size  = getPieceSize(piece);
			ergodic.ergodic(this,
							piece, 
							start, size);
		}
	}
	public static interface PieceErgodic {
		public void ergodic(IndexPieceLong ip,
                            long piece,
                            long pieceOffset, long pieceSize);
	}
}



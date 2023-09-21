package top.fols.atri.util;

import java.io.Serializable;

@SuppressWarnings("UnnecessaryLocalVariable")
public class IndexPiece implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	
	
	private int length;
	private int eachPieceSize;
	private int pieceCount;

	public IndexPiece() {}
	public IndexPiece(int eachPieceSize) {
		this.setEachPieceSize(eachPieceSize);
	}
	public IndexPiece(int length, int eachPieceSize) {
		this.setLength(length);
		this.setEachPieceSize(eachPieceSize);
	}




	public void setLength(int length) {
		this.length     = length;
		this.pieceCount = IndexPieces.getPieceCount(this.length, this.eachPieceSize);
	}
	public void setEachPieceSize(int eachPieceSize) {
		this.eachPieceSize = eachPieceSize;
		this.pieceCount = IndexPieces.getPieceCount(this.length, this.eachPieceSize);
	}



	/**
	 * @return start pointer of the block, starting from 0
	 */
	public int getPieceIndexStart(int piece) {
		return IndexPieces.getPieceIndexStart(piece, pieceCount,
											  length, eachPieceSize);
	}

	/**
	 * @return end pointer of the block, (piece index) + (piece len) - 1
	 */
	public int getPieceIndexEnd(int piece) {
		return IndexPieces.getPieceIndexEnd(piece, pieceCount,
											length, eachPieceSize);
	}

	/**
	 * @return piece size, starting from 0
	 */
	public int getPieceSize(int piece) {
		return IndexPieces.getPieceSize(piece, pieceCount,
										length, eachPieceSize);
	}

	/**
	 * get the block where the pointer is, starting from 0
	 */
	public int getPieceFromIndex(int index) {
		return IndexPieces.getPieceFromIndex(index, eachPieceSize);
	}



	
	/**
	 * @return piece count
	 */
	public int getPieceCount() {
		return this.pieceCount;
	}

	/**
	 * @return each piece size
	 */
	public int getEachPieceSize() {
		return this.eachPieceSize;
	}

	public long getLength() {
		return length;
	}

	
	
	public void ergodic(PieceErgodic ergodic) {
		ergodic(IndexPieces.PIECE_OFFSET, getPieceCount(), ergodic);
	}
	public void ergodic(int offsetPiece, int pcount, PieceErgodic ergodic) {
		for (int pi = 0; pi < pcount; pi++) {
			int piece = offsetPiece + pi;
			int start = getPieceIndexStart(piece);
			int size  = getPieceSize(piece);
			ergodic.ergodic(this,
							piece, 
							start, size);
		}
	}
	public static interface PieceErgodic {
		public void ergodic(IndexPiece ip,
                            int piece,
                            int pieceOffset, int pieceSize);
	}
}


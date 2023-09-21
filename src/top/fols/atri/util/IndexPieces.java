package top.fols.atri.util;

public class IndexPieces {
	public static final int PIECE_OFFSET = 0;
	
	public static int getPieceCount(int len, int eachPieceSize) {
		if (len < 0) 
			throw new RuntimeException("length=" + len + ", min=0");

		if (eachPieceSize == 0)
			return 0;
		if (eachPieceSize < 0) 
			throw new RuntimeException("perPieceSize=" + eachPieceSize);

		int pieceCount = len / eachPieceSize;
		if (pieceCount == 0 && len > 0) {
			++pieceCount;
		} else if (((pieceCount - 1) * eachPieceSize) + eachPieceSize != len) {
			++pieceCount;
		}
		return pieceCount;
	}

	/**
	 * get the block where the pointer is, starting from 0
	 */
	public static int getPieceFromIndex(int index, int eachPieceSize) {
		int    newPiece = index / eachPieceSize;
		return newPiece;
	}


	/**
	 * @return piece size, starting from 0
	 */
	public static int getPieceSize(int piece,  int pieceCount,
								   int length, int eachPieceSize) {
		return (getPieceIndexEnd(piece, pieceCount,
								 length, eachPieceSize) + 1) - getPieceIndexStart(piece, pieceCount,
																				  length, eachPieceSize);
	}


	/**
	 * @return start pointer of the block, starting from 0
	 */
	public static int getPieceIndexStart(int piece, int pieceCount,
										 int length, int eachPieceSize) {
		if (piece < 0 || piece >= pieceCount) 
			throw new IndexOutOfBoundsException(
				String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", piece, 0, pieceCount));
		return piece * eachPieceSize;
	}

	/**
	 * @return end pointer of the block, (piece index) + (piece len) - 1
	 */
	public static int getPieceIndexEnd(int piece, int pieceCount, 
									   int length, int eachPieceSize) {
		if (piece < 0 || piece >= pieceCount) 
			throw new IndexOutOfBoundsException(
				String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", piece, 0, pieceCount));
		if (eachPieceSize == 0) 
			throw new RuntimeException("perPieceSize=" + eachPieceSize);

		int end = (piece * eachPieceSize) + eachPieceSize;
		if (end < 1) {
			end = Integer.MAX_VALUE;
		}
		if (end >= length) {
			return length - 1;
		}
		return end - 1;
	}
	
	
	
	
	
	
	
	
	
	/* ----copy----  */
	

	public static long getLongPieceCount(long len, long eachPieceSize) {
		if (len < 0) 
			throw new RuntimeException("length=" + len + ", min=0");

		if (eachPieceSize == 0)
			return 0;
		if (eachPieceSize < 0) 
			throw new RuntimeException("perPieceSize=" + eachPieceSize);

		long pieceCount = len / eachPieceSize;
		if (pieceCount == 0 && len > 0) {
			++pieceCount;
		} else if (((pieceCount - 1) * eachPieceSize) + eachPieceSize != len) {
			++pieceCount;
		}
		return pieceCount;
	}
	/**
	 * get the block where the pointer is, starting from 0
	 */
	public static long getLongPieceFromIndex(long index, long eachPieceSize) {
		long   newPiece = index / eachPieceSize;
		return newPiece;
	}
	/**
	 * @return piece size, starting from 0
	 */
	public static long getLongPieceSize(long piece, long pieceCount,
									long length, long eachPieceSize) {
		return (getLongPieceIndexEnd(piece, pieceCount,
								 length, eachPieceSize) + 1) - getLongPieceIndexStart(piece, pieceCount,
																				  length, eachPieceSize);
	}
	/**
	 * @return start pointer of the block, starting from 0
	 */
	public static long getLongPieceIndexStart(long piece, long pieceCount,
										  long length, long eachPieceSize) {
		if (piece < 0 || piece >= pieceCount) 
			throw new IndexOutOfBoundsException(
				String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", piece, 0, pieceCount));

		return piece * eachPieceSize;
	}

	/**
	 * @return end pointer of the block, (piece index) + (piece len) - 1
	 */
	public static long getLongPieceIndexEnd(long piece, long pieceCount, 
										long length, long eachPieceSize) {
		if (piece < 0 || piece >= pieceCount) 
			throw new IndexOutOfBoundsException(
				String.format("hopePiece=%s, minPiece=%s, pieceCount=%s", piece, 0, pieceCount));
		if (eachPieceSize == 0) 
			throw new RuntimeException("perPieceSize=" + eachPieceSize);

		long end = (piece * eachPieceSize) + eachPieceSize;
		if (end < 1) {
			end = Long.MAX_VALUE;
		}
		if (end >= length) {
			return length - 1;
		}
		return end - 1;
	}

	
	
	





	
}

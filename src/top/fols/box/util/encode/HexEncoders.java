package top.fols.box.util.encode;


/**
 * hex occupies 2 bytes
 */
public class HexEncoders {

	private static final byte[] HEX_CHAR_BYTES_LOWERCASE = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
		'9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final byte[] HEX_CHAR_BYTES_UPPERCASE = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
		'9', 'A', 'B', 'C', 'D', 'E', 'F' };



	public static long hexLenToBytesLen(long length) {
		return length / 2;
	}
	public static int  hexLenToBytesLen(int length) {
		return length / 2;
	}

	public static long bytesLenToHexLen(long length) {
		return length * 2;
	}
	public static int  bytesLenToHexLen(int length) {
		return length * 2;
	}
	
	
	
	
	
	public static String encodeToString(byte[] array) throws OutOfMemoryError, ArrayIndexOutOfBoundsException {
		return new String(encode(array));
	}

	public static byte[] encode(byte[] array) throws OutOfMemoryError, ArrayIndexOutOfBoundsException {
		return encode(array, 0, array.length, true);
	}
	public static byte[] encode(byte[] array, int off, int len, boolean upperCase) throws OutOfMemoryError, ArrayIndexOutOfBoundsException {
		byte[] result = new byte[len * 2];
		putBytesToHex(array, off, len, result, 0, upperCase);
		return result;
	}

	public static void putBytesToHex(byte[] array, int off, int len, 
									 byte[] putArray, int putArrayOff,
									 boolean upperCase) throws OutOfMemoryError, ArrayIndexOutOfBoundsException {
		if (len < 0 || off + len > array.length) {
			throw new ArrayIndexOutOfBoundsException(String.format("array.length=%s, off=%s, len=%s", array.length, off, len));
		}
		if (putArray.length - putArrayOff < bytesLenToHexLen(len)) {
			throw new ArrayIndexOutOfBoundsException(
				String.format("array.length=%s, off=%s, len=%s, " +
					"putArray.length=%s, putArray.off=%s, needMinPutArray.length>=%s",
					array.length, off, len,
					putArray.length, putArrayOff, putArrayOff + bytesLenToHexLen(len)));
		}
		byte[] HEX_CHAR = upperCase ? HexEncoders.HEX_CHAR_BYTES_UPPERCASE: HexEncoders.HEX_CHAR_BYTES_LOWERCASE;
		for (int i = 0;i < len;i++) {
			byte b = array[i + off];
			int a;
			if (b < 0) {
				a = 256 + b;
			} else {
				a = b;
			}
			putArray[putArrayOff++] = HEX_CHAR[a / 16];
			putArray[putArrayOff++] = HEX_CHAR[a % 16];
		}
	}
	
	
	
	
	
	
	public static byte[] decode(String array) throws ArrayIndexOutOfBoundsException, RuntimeException {
		return decode(array.getBytes());
	}

	public static byte[] decode(byte[] array) throws ArrayIndexOutOfBoundsException, RuntimeException {
		return decode(array, 0, array.length);
	}

	public static byte[] decode(byte[] array, int off, int len) throws ArrayIndexOutOfBoundsException, RuntimeException {
		byte[] result = new byte[array.length / 2];
		putHexToBytes(array, off, len, result, 0);
		return result;
	}


	public static void putHexToBytes(byte[] array, int off, int len,
									 byte[] putarray, int putarrayoff) throws ArrayIndexOutOfBoundsException, RuntimeException {
		if (len < 0 || off + len > array.length) {
			throw new ArrayIndexOutOfBoundsException(String.format("array.length=%s, off=%s, len=%s", array.length, off, len));
		}
		if (putarray.length - putarrayoff < hexLenToBytesLen(len)) {
			throw new ArrayIndexOutOfBoundsException(
				String.format("array.length=%s, off=%s, len=%s, " +
					"putArray.length=%s, putArray.off=%s, needMinPutArray.length>=%s",
					array.length, off, len,
					putarray.length, putarrayoff, putarrayoff + hexLenToBytesLen(len)));
		}
		int pos = off;
		for (int i = 0; i < len && pos < off + len; i++) {
			// b[i] = (byte) ((byte)(DecInputStream.hexString.indexOf(cbuffered[Pos])) << 4 | (byte)(DecInputStream.hexString.indexOf(cbuffered[Pos + 1])));
			int b0 = readHexByte0(array[pos]);
			int b1 = readHexByte0(array[pos + 1]);
			if (b0 == -1 || b1 == -1) {
				throw new RuntimeException(String.format("unknown hex:[%s%s], index:[%s-%s]", (char)array[pos], (char)array[pos + 1], pos, pos + 1));
			}
			pos += 2;
			putarray[putarrayoff++] = (byte) (b0 << 4 | b1);
		}
	}
	private static int readHexByte0(byte b) {
		if (b >= '0' && b <= '9') {
		 	return b - '0';
		} else if (b >= 'a' && b <= 'f') {
			return 10 + (b - 'a');
		} else if (b >= 'A' && b <= 'F') {
			return 10 + (b - 'A');
		} else {
			return -1;
		}
	}


}

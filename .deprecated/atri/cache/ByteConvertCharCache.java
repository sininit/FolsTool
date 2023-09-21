package top.fols.atri.cache;

import java.nio.charset.Charset;
import top.fols.atri.io.Delimiter;
import top.fols.box.util.encode.ByteEncoders;

@SuppressWarnings("SpellCheckingInspection")
public class 	ByteConvertCharCache {
	public static class BytesCache extends WeakLevel3AndMapCache<String, char[], RuntimeException> {
		byte[] oldData;
		
		final Charset defaultCharset; 
		final String  defaultCharsetName;
		final char[]  defaultCharsetConvert;
		
		public BytesCache(byte[] data) {
			this.oldData = data = data.clone();
			this.defaultCharset     = Charset.defaultCharset();
			this.defaultCharsetName = defaultCharset.name();
			this.defaultCharsetConvert = convert(data, defaultCharset);
		}


		public Charset defaultCharset() {
			return defaultCharset;
		}

		public byte[] innerOldData() { return oldData; }
		public char[] innerDefaultCharsetCacheValue() {
			return defaultCharsetConvert;
		}
		public char[] innerCacheValue(Charset charset) {
			return lookup(charset.name());
		}
		public char[] innerCacheValue(String charset) {
			return lookup(charset);
		}
		
		public byte[] oldData()              { return innerOldData().clone(); }
		public char[] defaultCharsetValue()  { return innerDefaultCharsetCacheValue().clone(); }
		public char[] value(Charset charset) { return innerCacheValue(charset).clone(); }

		public static char[] convert(byte[] data, Charset charset) {
			return ByteEncoders.bytesToChars(data, 0, data.length, charset);
		}

		@Override
		protected char[] newMapValueCache(String key) throws RuntimeException {
			// TODO: Implement this method
			return convert(oldData, Charset.forName(key));
		}
	}
	public static class BytessCache extends WeakLevel3AndMapCache<String, char[][], RuntimeException> {
		byte[][] oldData;
		
		final Charset   defaultCharset; 
		final String    defaultCharsetName;
		final char[][]  defaultCharsetConvert;
		
		public BytessCache(byte[][] data) {
			this.oldData = data = Delimiter.clone(data);
			this.defaultCharset     = Charset.defaultCharset();
			this.defaultCharsetName = defaultCharset.name();
			this.defaultCharsetConvert = convert(data, defaultCharset);
		}


		public Charset defaultCharset() {
			return defaultCharset;
		}

		public byte[][] innerOldData() { return oldData; }
		public char[][] innerDefaultCharsetCacheValue() {
			return defaultCharsetConvert;
		}
		public char[][] innerCacheValue(Charset charset) {
			return lookup(charset.name());
		}
		public char[][] innerCacheValue(String charset) {
			return lookup(charset);
		}

		public byte[][] oldData()              { return Delimiter.clone(innerOldData()); }
		public char[][] defaultCharsetValue()  { return Delimiter.clone(innerDefaultCharsetCacheValue()); }
		public char[][] value(Charset charset) { return Delimiter.clone(innerCacheValue(charset)); }

		public static char[][] convert(byte[][] data, Charset charset) {
			char[][] result = new char[data.length][];
			for (int i =0;i < data.length;i++) {
				result[i] = BytesCache.convert(data[i], charset);
			}
			return result;
		}

		@Override
		protected char[][] newMapValueCache(String key) throws RuntimeException {
			// TODO: Implement this method
			return convert(oldData, Charset.forName(key));
		}
	}








//--------<copy>-------

	public static class CharsCache extends WeakLevel3AndMapCache<String, byte[], RuntimeException> {
		char[] oldData;

		final Charset defaultCharset; 
		final String  defaultCharsetName;
		final byte[]  defaultCharsetConvert;
		
		public CharsCache(char[] data) {
			this.oldData = data = data.clone();
			this.defaultCharset     = Charset.defaultCharset();
			this.defaultCharsetName = defaultCharset.name();
			this.defaultCharsetConvert = convert(data, defaultCharset);
		}


		public Charset defaultCharset() {
			return defaultCharset;
		}

		public char[] innerOldData() { return oldData; }
		public byte[] innerDefaultCharsetCacheValue() {
			return defaultCharsetConvert;
		}
		public byte[] innerCacheValue(Charset charset) {
			return lookup(charset.name());
		}
		public byte[] innerCacheValue(String charset) {
			return lookup(charset);
		}
		

		public char[] oldData() { return innerOldData().clone(); }
		public byte[] defaultCharsetValue()  { return innerDefaultCharsetCacheValue().clone(); }
		public byte[] value(Charset charset) { return innerCacheValue(charset).clone();}

		public static byte[] convert(char[] data, Charset charset) {
			return ByteEncoders.charsToBytes(data, 0, data.length, charset);
		}
		
		@Override
		protected byte[] newMapValueCache(String key) throws RuntimeException {
			// TODO: Implement this method
			return convert(oldData, Charset.forName(key));
		}
	}
	public static class CharssCache extends WeakLevel3AndMapCache<String, byte[][], RuntimeException> {
		char[][] oldData;


		final Charset   defaultCharset; 
		final String    defaultCharsetName;
		final byte[][]  defaultCharsetConvert;
		
		public CharssCache(char[][] data) {
			this.oldData = data = Delimiter.clone(data);
			this.defaultCharset     = Charset.defaultCharset();
			this.defaultCharsetName = defaultCharset.name();
			this.defaultCharsetConvert = convert(data, defaultCharset);
		}


		public Charset defaultCharset() {
			return defaultCharset;
		}

		public char[][] innerOldData() { return oldData; }
		public byte[][] innerDefaultCharsetCacheValue() {
			return defaultCharsetConvert;
		}
		public byte[][] innerCacheValue(Charset charset) {
			return lookup(charset.name());
		}
		public byte[][] innerCacheValue(String charset) {
			return lookup(charset);
		}
		
		

		public char[][] oldData()              { return Delimiter.clone(innerOldData()); }
		public byte[][] defaultCharsetValue()  { return Delimiter.clone(innerDefaultCharsetCacheValue()); }
		public byte[][] value(Charset charset) { return Delimiter.clone(innerCacheValue(charset)); }

		public static byte[][] convert(char[][] data, Charset charset) {
			byte[][] result = new byte[data.length][];
			for (int i = 0; i < data.length; i++) {
				result[i] = CharsCache.convert(data[i], charset);
			}
			return result;
		}
		
		@Override
		protected byte[][] newMapValueCache(String key) throws RuntimeException {
			// TODO: Implement this method
			return convert(oldData, Charset.forName(key));
		}
	}
}

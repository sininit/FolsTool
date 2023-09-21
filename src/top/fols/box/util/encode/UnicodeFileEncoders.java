package top.fols.box.util.encode;

import top.fols.atri.interfaces.abstracts.BitsOptions;

public abstract class UnicodeFileEncoders {
    //No reference will definitely not deadlock
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public static final BigEndianUnicode BIG_ENDIAN       = new BigEndianUnicode();//java default
    //No reference will definitely not deadlock
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public static final LittleEndianUnicode LITTLE_ENDIAN = new LittleEndianUnicode();

    public abstract byte[] getFileHeader();

    public abstract long mathCharsLenToBytesLen(long length);
    public abstract int  mathCharsLenToBytesLen(int length);

    public abstract long mathBytesLenToCharsLen(long length);
    public abstract int  mathBytesLenToCharsLen(int length);

    public abstract void putCharsToBytes(char[] chars, int charsOff, int charsLen, byte[] bytes, int bytesOff);
    public abstract void putCharsToBytes(CharSequence chars, int charsOff, int charsLen, byte[] bytes, int bytesOff);

    public abstract void putBytesToChars(byte[] bytes, int bytesOff, char[] chars, int charsOff, int charsLen);

    public abstract void putChar(byte[] bytes, int bytesOff, char ch);
    public abstract char getChar(byte[] bytes, int bytesOff);



    /**
     * java char is unicode
     */
    @SuppressWarnings("AccessStaticViaInstance")
    public static class BigEndianUnicode extends UnicodeFileEncoders {
        /**
         * Unicode file header for [FE, FF]
         * using big-endian byte ordering.
         */
        @Override
        public final byte[] getFileHeader() {
            return new byte[] { (byte) 0xFE, (byte) 0xFF };
        }

        /*
         * data
         */
        @Override
        public long mathCharsLenToBytesLen(long length) { return length * BitsOptions.BIG_ENDIAN.CHAR_BYTE_LENGTH; }
        @Override
        public int mathCharsLenToBytesLen(int length) { return length * BitsOptions.BIG_ENDIAN.CHAR_BYTE_LENGTH; }

        @Override
        public long mathBytesLenToCharsLen(long length) { return length / BitsOptions.BIG_ENDIAN.CHAR_BYTE_LENGTH; }
        @Override
        public int mathBytesLenToCharsLen(int length) { return length / BitsOptions.BIG_ENDIAN.CHAR_BYTE_LENGTH; }

        @Override
        public void putCharsToBytes(char[] chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
            int coff = charsOff;
            int boff = bytesOff;
            for (int i = 0; i < charsLen; i++) {
                BitsOptions.BIG_ENDIAN.putBytes(bytes, boff, chars[coff++]);
                boff += BitsOptions.BIG_ENDIAN.CHAR_BYTE_LENGTH;
            }
        }

        @Override
        public void putCharsToBytes(CharSequence chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
            int coff = charsOff;
            int boff = bytesOff;
            for (int i = 0; i < charsLen; i++) {
                BitsOptions.BIG_ENDIAN.putBytes(bytes, boff, chars.charAt(coff++));
                boff += BitsOptions.BIG_ENDIAN.CHAR_BYTE_LENGTH;
            }
        }

        @Override
        public void putBytesToChars(byte[] bytes, int bytesOff, char[] chars, int charsOff, int charsLen) {
            int coff = charsOff;
            int boff = bytesOff;
            for (int i = 0; i < charsLen; i++) {
                chars[coff++] = BitsOptions.BIG_ENDIAN.getChar(bytes, boff);
                boff += BitsOptions.BIG_ENDIAN.CHAR_BYTE_LENGTH;
            }
        }

        @Override
        public void putChar(byte[] bytes, int bytesOff, char ch) {
            BitsOptions.BIG_ENDIAN.putBytes(bytes, bytesOff, ch);
        }

        @Override
        public char getChar(byte[] bytes, int bytesOff) {
            return BitsOptions.BIG_ENDIAN.getChar(bytes, bytesOff);
        }
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public static class LittleEndianUnicode extends UnicodeFileEncoders {
        /**
         * Unicode file header for [FF, FE]
         * using little-endian byte ordering.
         */

        @Override
        public final byte[] getFileHeader() { return new byte[] { (byte) 0xFF, (byte) 0xFE }; }

        /*
         * data
         */
        @Override
        public long mathCharsLenToBytesLen(long length) { return length * BitsOptions.LITTLE_ENDIAN.CHAR_BYTE_LENGTH; }
        @Override
        public int mathCharsLenToBytesLen(int length) { return length * BitsOptions.LITTLE_ENDIAN.CHAR_BYTE_LENGTH; }
        @Override
        public long mathBytesLenToCharsLen(long length) { return length / BitsOptions.LITTLE_ENDIAN.CHAR_BYTE_LENGTH; }
        @Override
        public int mathBytesLenToCharsLen(int length) { return length / BitsOptions.LITTLE_ENDIAN.CHAR_BYTE_LENGTH; }

        @Override
        public void putCharsToBytes(char[] chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
            int coff = charsOff;
            int boff = bytesOff;
            for (int i = 0; i < charsLen; i++) {
                BitsOptions.LITTLE_ENDIAN.putBytes(bytes, boff, chars[coff++]);
                boff += BitsOptions.LITTLE_ENDIAN.CHAR_BYTE_LENGTH;
            }
        }

        @Override
        public void putCharsToBytes(CharSequence chars, int charsOff, int charsLen, byte[] bytes, int bytesOff) {
            int coff = charsOff;
            int boff = bytesOff;
            for (int i = 0; i < charsLen; i++) {
                BitsOptions.LITTLE_ENDIAN.putBytes(bytes, boff, chars.charAt(coff++));
                boff += BitsOptions.LITTLE_ENDIAN.CHAR_BYTE_LENGTH;
            }
        }

        @Override
        public void putBytesToChars(byte[] bytes, int bytesOff, char[] chars, int charsOff, int charsLen) {
            int coff = charsOff;
            int boff = bytesOff;
            for (int i = 0; i < charsLen; i++) {
                chars[coff++] = BitsOptions.LITTLE_ENDIAN.getChar(bytes, boff);
                boff += BitsOptions.LITTLE_ENDIAN.CHAR_BYTE_LENGTH;
            }
        }

        @Override
        public void putChar(byte[] bytes, int bytesOff, char ch) {
            BitsOptions.LITTLE_ENDIAN.putBytes(bytes, bytesOff, ch);
        }

        @Override
        public char getChar(byte[] bytes, int bytesOff) {
            return BitsOptions.LITTLE_ENDIAN.getChar(bytes, bytesOff);
        }
    }
}

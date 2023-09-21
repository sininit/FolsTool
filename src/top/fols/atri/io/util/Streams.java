package top.fols.atri.io.util;

import top.fols.atri.io.BytesInputStreams;
import top.fols.atri.io.BytesOutputStreams;
import top.fols.atri.io.CharsReaders;
import top.fols.atri.io.CharsWriters;
import top.fols.atri.lang.Finals;
import top.fols.box.net.URLConnections;
import top.fols.atri.interfaces.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;

public class Streams {


    public static BytesInputStreams input(byte[] file) {
        return new BytesInputStreams(file);
    }
    public static FileInputStream input(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public static BytesOutputStreams output() {
        return new BytesOutputStreams();
    }
    public static FileOutputStream output(File file) {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }



    public static CharsReaders reader(char[] file) {
        return new CharsReaders(file);
    }
    public static CharsWriters writer() {
        return new CharsWriters();
    }











    public static boolean close(Closeable c) {
        try {
            if (null != c) c.close();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    public static boolean close(URLConnection urlConnection) {
        try {
            if (null != urlConnection) URLConnections.close(urlConnection);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    public static boolean close(Socket s) {
        boolean b = true;

        try {
            Streams.close(s.getInputStream());
        } catch (Throwable e) {
            b = false;
        }
        try {
            Streams.close(s.getOutputStream());
        } catch (Throwable e) {
            b = false;
        }

        try {
            s.close();
        } catch (Throwable e) {
            b = false;
        }

//		try {
//			s.shutdownInput();
//		} catch (Throwable e) {
//			b = false;
//		}
//		try {
//			s.shutdownOutput();
//		} catch (Throwable e) {
//			b = false;
//		}

        return b;
    }


    public static final int DEFAULT_BYTE_BUFF_SIZE = 8192;
    public static final int DEFAULT_CHAR_BUFF_SIZE = 8192;

    public static final int COPY_UNLIMITED_COPY_LENGTH = -1;


    public static int copy(byte[] input, OutputStream output) throws IOException {
        return copy(input, output, false);
    }
    public static int copy(byte[] input, OutputStream output, boolean autoflush) throws IOException {
        if (input == null)
            input = Finals.EMPTY_BYTE_ARRAY;

        output.write(input);
        if (autoflush) {
            output.flush();
        }
        return input.length;
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, Streams.DEFAULT_BYTE_BUFF_SIZE);
    }
    public static void copy(InputStream input, OutputStream output, int bufflen) throws IOException {
        copy(input, output, bufflen, false);
    }
    public static void copy(InputStream input, OutputStream output, int bufflen, boolean autoflush) throws IOException {
        copyFixedLength(input, output, bufflen, Streams.COPY_UNLIMITED_COPY_LENGTH, autoflush);
    }




    public static long copyFixedLength(InputStream input, OutputStream output, long copyLength) throws IOException {
        return copyFixedLength(input, output, Streams.DEFAULT_BYTE_BUFF_SIZE, copyLength);
    }
    public static long copyFixedLength(InputStream input, OutputStream output, int bufflen, long copyLength)
            throws IOException {
        return copyFixedLength(input, output, bufflen, copyLength, false);
    }
    /**
     *
     * @param input      off
     * @param output     to
     * @param bufflen    buff size
     * @param copyLength <= -1, no limit, == 0 not execution
     * @param autoflush  w buff after flush();
     * @return already copy length
     */
    public static long copyFixedLength(InputStream input, OutputStream output, int bufflen, long copyLength,
                                       boolean autoflush) throws IOException {
        if (null == input)   { return 0; }
        if (copyLength == 0) { return 0; }
        if (bufflen <= 0)    { throw new IOException("buffer lenth cannot <= 0"); }

        byte[] buff = new byte[bufflen];
        int read;
        int tmpbufflen;
        long already = 0;
        boolean unlimited = copyLength <= Streams.COPY_UNLIMITED_COPY_LENGTH;
        while (true) {
            if (unlimited) {
                tmpbufflen = bufflen;
            } else {
                tmpbufflen = copyLength - already < bufflen ? (int) (copyLength - already) : bufflen;
                if (tmpbufflen <= 0) {
                    break;
                }
            }
            if ((read = input.read(buff, 0, tmpbufflen)) == -1) {
                break;
            }
            already += read;

            if (null != output) {
                output.write(buff, 0, read);
                if (autoflush) {
                    output.flush();
                }
            }
        }
        return already;
    }








    public static int copy(char[] input, Writer output) throws IOException {
        return copy(input, output, false);
    }

    public static int copy(char[] input, Writer output, boolean autoflush) throws IOException {
        if (input == null)
            input = Finals.EMPTY_CHAR_ARRAY;

        output.write(input);
        if (autoflush) {
            output.flush();
        }
        return input.length;
    }


    public static void copy(Reader input, Writer output) throws IOException {
        copy(input, output, Streams.DEFAULT_CHAR_BUFF_SIZE);
    }

    public static void copy(Reader input, Writer output, int bufflen) throws IOException {
        copy(input, output, bufflen, false);
    }

    public static void copy(Reader input, Writer output, int bufflen, boolean autoflush) throws IOException {
        copyFixedLength(input, output, bufflen, Streams.COPY_UNLIMITED_COPY_LENGTH, autoflush);
    }





    public static long copyFixedLength(Reader input, Writer output, long copyLength) throws IOException {
        return copyFixedLength(input, output, Streams.DEFAULT_CHAR_BUFF_SIZE, copyLength);
    }

    public static long copyFixedLength(Reader input, Writer output, int bufflen, long copyLength) throws IOException {
        return copyFixedLength(input, output, bufflen, copyLength, false);
    }




    /**
     *
     * @param input      off
     * @param output     to
     * @param bufflen    buff size
     * @param copyLength <= -1, no limit, == 0 not execution
     * @param autoflush  w buff after flush();
     * @return already copy length
     */
    public static long copyFixedLength(Reader input, Writer output, int bufflen, long copyLength, boolean autoflush)
            throws IOException {
        if (null == input)   { return 0; }
        if (copyLength == 0) { return 0; }
        if (bufflen <= 0)    { throw new IOException("buffer lenth cannot <= 0"); }

        char[] buff = new char[bufflen];
        int read;
        int tmpbufflen;
        long already = 0;
        boolean unlimited = copyLength <= Streams.COPY_UNLIMITED_COPY_LENGTH;
        while (true) {
            if (unlimited) {
                tmpbufflen = bufflen;
            } else {
                tmpbufflen = copyLength - already < bufflen ? (int) (copyLength - already) : bufflen;
                if (tmpbufflen <= 0) {
                    break;
                }
            }
            if ((read = input.read(buff, 0, tmpbufflen)) == -1) {
                break;
            }
            already += read;

            if (null != output) {
                output.write(buff, 0, read);
                if (autoflush) {
                    output.flush();
                }
            }
        }
        return already;
    }




    public static String toString(InputStream input, String encoding) throws IOException {
        return new String(toBytes(input), encoding);
    }
    public static String toString(InputStream input) throws IOException {
        return new String(toBytes(input));
    }



    public static byte[] toBytes(InputStream input) throws IOException {
        if (null != input) {
            BytesOutputStreams stream = new BytesOutputStreams();
            Streams.copy(input, stream);
            byte[] bs = stream.toByteArray();
            stream.release();
            return bs;
        }
        return null;
    }

    @Nullable
    public static byte[] toBytes(InputStream input, int length) throws IOException {
        if (null == input)   { return null; }
        if (length < 0)  	 { return null; }

        byte[] bytes = new byte[length];
        int read = input.read(bytes, 0, bytes.length);
        if (read == -1) {
            return null;
        } else if (read == length) {
            return bytes;
        } else {
            int    already = read;
            while (already < length) {
                if ((read = input.read(bytes, already, length - already)) == -1) {
                    break;
                }
                already += read;
            }
            if (already == length) {
                return bytes;
            } else {
                byte[] r = new byte[already];
                System.arraycopy(bytes, 0, r, 0, r.length);
                return r;
            }
        }
    }


    public static String toString(Reader input) throws IOException {
        return new String(toChars(input));
    }
    public static char[] toChars(Reader input) throws IOException {
        if (null != input) {
            CharsWriters stream = new CharsWriters();
            Streams.copy(input, stream);
            char[] cs = stream.toCharArray();
            stream.release();
            return cs;
        }
        return null;
    }
    @Nullable
    public static char[] toChars(Reader input, int length) throws IOException {
        if (null == input)   { return null; }
        if (length < 0)  	 { return null; }

        char[] bytes = new char[length];
        int read = input.read(bytes, 0, bytes.length);
        if (read == -1) {
            return null;
        } else if (read == length) {
            return bytes;
        } else {
            int    already = read;
            while (already < length) {
                if ((read = input.read(bytes, already, length - already)) == -1) {
                    break;
                }
                already += read;
            }
            if (already == length) {
                return bytes;
            } else {
                char[] r = new char[already];
                System.arraycopy(bytes, 0, r, 0, r.length);
                return r;
            }
        }
    }





    public static class ObjectTool {
        public static void writeObject(OutputStream out, Object obj) throws IOException {
            new ObjectOutputStream(out).writeObject(obj);
        }

        public static Object readObject(InputStream in) throws IOException, ClassNotFoundException {
            return new ObjectInputStream(in).readObject();
        }




        public static byte[] toBytes(Object obj) throws IOException {
            BytesOutputStreams out = new BytesOutputStreams();
            writeObject(out, obj);
            byte[] bs = out.toByteArray();
            out.release();
            return bs;
        }
        public static Object toObject(byte[] bytes) throws ClassNotFoundException, IOException {
            BytesInputStreams in = new BytesInputStreams(bytes);
            Object o = readObject(in);
            in.release();
            return o;
        }
    }

}

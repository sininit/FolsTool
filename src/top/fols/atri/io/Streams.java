package top.fols.atri.io;

import top.fols.atri.net.URLConnections;
import top.fols.atri.net.XSocket;
import top.fols.box.io.base.XByteArrayInputStream;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XCharArrayWriter;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;

public class Streams {


    public static  ByteBufferInputStream input(byte[] file) {
        return new ByteBufferInputStream(file);
    }
    public static InputStream input(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public static  ByteBufferOutputStream output() {
        return new ByteBufferOutputStream();
    }
    public static OutputStream output(File file) {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }



    public static  CharBufferReader reader(char[] file) {
        return new CharBufferReader(file);
    }
    public static  CharBufferWriter writer() {
        return new CharBufferWriter();
    }











    public static boolean close(Closeable c) {
        try {
            c.close();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    public static boolean close(URLConnection urlConnection){
        try {
            URLConnections.close(urlConnection);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    public static boolean close(Socket socket) {
        try {
            XSocket.close(socket);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }



    public static final int DEFAULT_BYTE_BUFF_SIZE = 8192;
    public static final int DEFAULT_CHAR_BUFF_SIZE = 8192;

    public static final int COPY_UNLIMIT_COPYLENGTH = -1;

    public static int copy(byte[] input, OutputStream output) throws IOException {
        return copy(input, output, false);
    }

    public static int copy(byte[] input, OutputStream output, boolean autoflush) throws IOException {
        if (null == input) {
            return 0;
        }
        output.write(input);
        if (autoflush) {
            output.flush();
        }
        return input.length;
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, Streams.DEFAULT_BYTE_BUFF_SIZE);
    }

    public static long copy(InputStream input, OutputStream output, int bufflen) throws IOException {
        return copy(input, output, bufflen, false);
    }

    public static long copy(InputStream input, OutputStream output, int bufflen, boolean autoflush) throws IOException {
        return copyFixedLength(input, output, bufflen, Streams.COPY_UNLIMIT_COPYLENGTH, autoflush);
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
     * @param autoflush  write buff after flush();
     * @return already copy length
     * @throws IOException
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
        boolean unlimited = copyLength <= Streams.COPY_UNLIMIT_COPYLENGTH;
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
        if (null == input) {
            return 0;
        }
        output.write(input);
        if (autoflush) {
            output.flush();
        }
        return input.length;
    }

    public static long copy(Reader input, Writer output) throws IOException {
        return copy(input, output, Streams.DEFAULT_CHAR_BUFF_SIZE);
    }

    public static long copy(Reader input, Writer output, int bufflen) throws IOException {
        return copy(input, output, bufflen, false);
    }

    public static long copy(Reader input, Writer output, int bufflen, boolean autoflush) throws IOException {
        return copyFixedLength(input, output, bufflen, Streams.COPY_UNLIMIT_COPYLENGTH, autoflush);
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
     * @param autoflush  write buff after flush();
     * @return already copy length
     * @throws IOException
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
        boolean unlimited = copyLength <= Streams.COPY_UNLIMIT_COPYLENGTH;
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
            XByteArrayOutputStream stream = new XByteArrayOutputStream();
            Streams.copy(input, stream);
            byte[] bs = stream.toByteArray();
            stream.releaseBuffer();
            return bs;
        }
        return null;
    }
    public static byte[] toBytes(InputStream input, int length) throws IOException {
        if (null != input) {
            XByteArrayOutputStream stream = new XByteArrayOutputStream();
            Streams.copyFixedLength(input, stream, length);
            byte[] bs = stream.toByteArray();
            stream.releaseBuffer();
            return bs;
        }
        return null;
    }


    public static String toString(Reader input) throws IOException {
        return new String(toChars(input));
    }
    public static char[] toChars(Reader input) throws IOException {
        if (null != input) {
            XCharArrayWriter Arrayout = new XCharArrayWriter();
            Streams.copy(input, Arrayout);
            char[] cs = Arrayout.toCharArray();
            Arrayout.releaseBuffer();
            return cs;
        }
        return null;
    }

    public static char[] toChars(Reader input, int length) throws IOException {
        if (null != input) {
            XCharArrayWriter Arrayout = new XCharArrayWriter();
            Streams.copyFixedLength(input, Arrayout, length);
            char[] cs = Arrayout.toCharArray();
            Arrayout.releaseBuffer();
            return cs;
        }
        return null;
    }

    public static class ObjectTool {
        public static void writeObject(OutputStream out, Object obj) throws IOException {
            new ObjectOutputStream(out).writeObject(obj);
        }

        public static Object readObject(InputStream in) throws IOException, ClassNotFoundException {
            return new ObjectInputStream(in).readObject();
        }




        public static byte[] toBytes(Object obj) throws IOException {
            XByteArrayOutputStream out = new XByteArrayOutputStream();
            writeObject(out, obj);
            byte[] bs = out.toByteArray();
            out.releaseBuffer();
            return bs;
        }
        public static Object toObject(byte[] bytes) throws ClassNotFoundException, IOException {
            XByteArrayInputStream in = new XByteArrayInputStream(bytes);
            return readObject(in);
        }
    }

}

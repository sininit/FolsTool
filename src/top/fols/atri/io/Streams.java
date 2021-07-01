package top.fols.atri.io;

import top.fols.box.io.XStream;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XCharArrayWriter;

import java.io.*;

public class Streams {
    public static boolean tryClose(Closeable c) {
        try {
            c.close();
            return true;
        } catch (Throwable e) {
            return false;
        }
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
        boolean unlimited = copyLength <= XStream.COPY_UNLIMIT_COPYLENGTH;
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
        boolean unlimited = copyLength <= XStream.COPY_UNLIMIT_COPYLENGTH;
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
            XStream.copy(input, stream);
            byte[] bs = stream.toByteArray();
            stream.releaseBuffer();
            return bs;
        }
        return null;
    }
    public static byte[] toBytes(InputStream input, int length) throws IOException {
        if (null != input) {
            XByteArrayOutputStream stream = new XByteArrayOutputStream();
            XStream.copyFixedLength(input, stream, length);
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
            XStream.copy(input, Arrayout);
            char[] cs = Arrayout.toCharArray();
            Arrayout.releaseBuffer();
            return cs;
        }
        return null;
    }

    public static char[] toChars(Reader input, int length) throws IOException {
        if (null != input) {
            XCharArrayWriter Arrayout = new XCharArrayWriter();
            XStream.copyFixedLength(input, Arrayout, length);
            char[] cs = Arrayout.toCharArray();
            Arrayout.releaseBuffer();
            return cs;
        }
        return null;
    }


}

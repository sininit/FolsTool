package top.fols.atri.lang;

import top.fols.atri.buffer.ByteBufferOutputStream;
import top.fols.atri.buffer.bytes.ByteBufferFilter;
import top.fols.atri.buffer.bytes.ByteBufferOption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

public class ProcessExecutor {
    String[] command;
    public String[] command() { return null == command?new String[]{}:command; }
    public void command(String... command) { this.command = command; }
    public void command(String command) {
        if (command.length() == 0) {
            throw new IllegalArgumentException("Empty command");
        }
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
        }
        this.command = cmdarray;
    }

    String[] envp;
    public String[] envp() { return null == envp ? new String[]{} : envp; }
    public void envp(String[] envp) { this.envp = envp; }

    File dir;
    public File directory() { return dir; }
    public void directory(File dir) { this.dir = dir; }
    public void directory(String dir) { this.dir = new File(dir); }

    public java.lang.Process exec() throws IOException {
        return this.exec(Runtime.getRuntime());
    }
    public java.lang.Process exec(Runtime runtime) throws IOException {
        return runtime.exec(this.command(), envp(), directory());
    }








    public static final String LINE_SEPARATOR = System.lineSeparator();//write command line spearator
    public static final byte[] PROCESS_LINE_SEPARATOR = new byte[] {'\n'};
    static final ByteBufferFilter readLine = new ByteBufferFilter() {
        { this.addSeparator(PROCESS_LINE_SEPARATOR); }
        @Override
        protected boolean accept(int last, int search, byte[] split, boolean readEnd) {
            return super.accept(last, search, split, readEnd);
        }
    };
    public static int getProcessResult(java.lang.Process proc, Charset charset, OutputStream resultOutput, OutputStream errorOutput) throws IOException, InterruptedException {
        InputStream in = proc.getInputStream();
        InputStream error = proc.getErrorStream();
        if (proc.exitValue() != 0 && error.available() > 0) {
            ByteBufferOutputStream buf = new ByteBufferOutputStream();
            ByteBufferOption row = new ByteBufferOption() {
                @Override
                public int stream_read(byte[] buf, int off, int len) throws IOException {
                    return error.read(buf, off, len);
                }
            };
            byte[] bufferarray;
            while (!row.readFilterIFEnd(readLine)) {
                buf.write(bufferarray= readLine.result(true));
            }
            byte[] errorMessag = buf.toByteArray();
            buf.close();
            if (null != errorOutput) {
                errorOutput.write(errorMessag);
            } else {
                throw new IOException(new String(errorMessag, charset));
            }
        } else {
            ByteBufferOption row = new ByteBufferOption() {
                @Override
                public int stream_read(byte[] buf, int off, int len) throws IOException { return in.read(buf, off, len); }
            };
            byte[] bufferarray;
            if (null != resultOutput) {
                while (!row.readFilterIFEnd(readLine)) {
                     resultOutput.write(bufferarray= readLine.result(true));
                }
            }
        }
        return proc.exitValue();
    }
    public static byte[] getProcessResultBytes(java.lang.Process proc, Charset charset) throws IOException, InterruptedException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        int exitValue = getProcessResult(proc, charset, result, error);
        if (error.size() > 0) {
            String errorMessage = new String(error.toByteArray(), charset);
            throw new RuntimeException(errorMessage);
        }
        return result.toByteArray();
    }
    public static String getProcessResultString(java.lang.Process proc, Charset charset) throws IOException, InterruptedException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        int exitValue = getProcessResult(proc, charset, result, error);
        if (error.size() > 0) {
            String errorMessage = new String(error.toByteArray(), charset);
            throw new RuntimeException(errorMessage);
        }
        return new String(result.toByteArray(), charset);
    }




}

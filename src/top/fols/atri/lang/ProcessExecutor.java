package top.fols.atri.lang;

import top.fols.atri.io.ByteBufferOutputStream;
import top.fols.atri.io.buffer.BufferFilter;
import top.fols.atri.io.buffer.bytes.ByteBufferFilter;
import top.fols.atri.io.buffer.bytes.ByteBufferOperate;
import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XInputStreamLine;
import top.fols.box.io.base.XStringWriter;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.statics.XStaticSystem;
import top.fols.box.util.XArrays;
import top.fols.box.util.encode.XHexEncoder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class ProcessExecutor {
    public ProcessExecutor() {
    }






    public static final String EV_PATH = "PATH";
    public static File[] envPATH() {
        List<String> ps = Strings.split(System.getenv(EV_PATH), File.pathSeparator);
        List<File> pfs = new ArrayList<>();
        for (String path : ps) {
            File f = new File(path);
            pfs.add(f);
        }
        File[] newArray = pfs.toArray(new File[pfs.size()]);
        ps = null;
        pfs = null;
        return newArray;
    }

    public static String envPATHCanExecuteCommandFilePathTry(String command) {
        File[] fs = envPATH();
        for (File f : fs) {
            File cf = new File(f, command);
            if (cf.canExecute()) {
                return cf.getAbsolutePath();
            }
        }
        return null;
    }

    public static String envPATHCanExecuteCommandFilePath(String command) throws FileNotFoundException {
        String fp = envPATHCanExecuteCommandFilePathTry(command);
        if (null == fp) {
            throw new FileNotFoundException(fp);
        }
        return fp;
    }

    public static final String LINE_SEPARATOR = XStaticSystem.getLineSeparator();//write command line spearator
    public static final String DEFAULT_EXIT_COMMAND = "exit";

    public static final byte[] PROCESS_LINE_SEPARATOR = XStaticFixedValue.Bytes_NextLineN();


    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, Charset charset)
            throws IOException {
        return ProcessExecutor.execCommandToString(runDir, createCommand, commands, ProcessExecutor.DEFAULT_EXIT_COMMAND, charset,
                null);
    }

    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, Charset charset,
                                             OutputStream errorOutput) throws IOException {
        return ProcessExecutor.execCommandToString(runDir, createCommand, commands, ProcessExecutor.DEFAULT_EXIT_COMMAND, charset,
                errorOutput);
    }

    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, String exit,
                                             Charset charset, OutputStream errorOutput) throws IOException {
        XByteArrayOutputStream res = new XByteArrayOutputStream();
        ProcessExecutor.execCommand(runDir, createCommand, commands, exit, charset, res, errorOutput);
        String str = new String(res.toByteArray(), charset);
        res.close();
        return str;
    }

    public static int execCommand(String runDir, String[] createCommand, String[] commands, String exit,
                                  Charset charset, OutputStream outputresult, OutputStream errorOutput) throws IOException {
        try {
            ProcessBuilder pb = new ProcessBuilder(createCommand);
            if (null != runDir) {
                pb.directory(new File(runDir));
            }
            pb.redirectErrorStream(false);
            Process proc = pb.start();
            OutputStream out = proc.getOutputStream();
            for (String command : commands) {
                out.write(command.getBytes(charset));
                out.write(LINE_SEPARATOR.getBytes(charset));
                out.flush();
            }
            return getProcessResult(proc, out, exit, charset, outputresult, errorOutput);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    private static int getProcessResult(Process proc, OutputStream out, String exit, Charset charset,
                                        OutputStream resultOutput, OutputStream errorOutput) throws IOException, InterruptedException {
        if (null != exit) {
            out.write(exit.getBytes(charset));
            out.write(LINE_SEPARATOR.getBytes(charset));
            out.flush();
            proc.waitFor();
        } else {
            return -1;
        }
        InputStream in = proc.getInputStream();
        InputStream error = proc.getErrorStream();
        if (proc.exitValue() != 0 && error.available() > 0) {
            XByteArrayOutputStream buf = new XByteArrayOutputStream();
            XInputStreamLine<InputStream> row = new XInputStreamLine<>(error);
            byte[] bufferarray;
            while (null != (bufferarray = row.readLine(PROCESS_LINE_SEPARATOR))) {
                buf.write(bufferarray);
            }
            if (XArrays.startsWith(buf.getBuff(), PROCESS_LINE_SEPARATOR, buf.size() - PROCESS_LINE_SEPARATOR.length)) {
                buf.setBuffSize(buf.size() - PROCESS_LINE_SEPARATOR.length);
            }

            byte[] errorMessag = buf.toByteArray();
            buf.close();
            row.close();

            if (null != errorOutput) {
                errorOutput.write(errorMessag);
            } else {
                throw new IOException(new String(errorMessag, charset));
            }
        } else {
            XInputStreamLine<InputStream> row = new XInputStreamLine<>(in);
            byte[] bufferarray;
            while (null != (bufferarray = row.readLine(PROCESS_LINE_SEPARATOR, true))) {
                if (null != resultOutput) {
                    resultOutput.write(bufferarray);
                }
            }
            row.close();
        }
        return proc.exitValue();
    }

    public static String escapeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer();
        String hex = XHexEncoder.encodeToString(bytes);
        int length = hex.length();
        for (int i = 0; i < length / 2; i++) {
            buf.append("\\x");
            buf.append(hex.charAt(i * 2));
            buf.append(hex.charAt(i * 2 + 1));
        }
        return buf.toString();
    }

    public static String formatString(String str) {
        if (null == str)
            return null;
        return new StringBuilder().append('\'').append(escapeCommandMonotonous0(str)).append('\'').toString();
    }

    private static String escapeCommandMonotonous0(String str) {
        if (null == str)
            return null;
        XStringWriter writer = new XStringWriter(str.length() * 2);
        escapeCommandMonotonousStyleString(writer, str);
        return writer.toString();
    }

    private static void escapeCommandMonotonousStyleString(XStringWriter out, String str) {
        if (null == out)
            throw new IllegalArgumentException("The Writer must not be null");
        if (null == str)
            return;
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '\b':
                    out.write('\\');
                    out.write('b');
                    break;
                case '\n':
                    out.write('\\');
                    out.write('n');
                    break;
                case '\t':
                    out.write('\\');
                    out.write('t');
                    break;
                case '\f':
                    out.write('\\');
                    out.write('f');
                    break;
                case '\r':
                    out.write('\\');
                    out.write('r');
                    break;
                case '\'':
                    out.write('\'');
                    out.write('"');
                    out.write('\'');
                    out.write('"');
                    out.write('\'');
                    break;
                case '\\':
                    out.write('\\');
                    out.write('\\');
                    break;
                default:
                    out.write(ch);
                    break;
            }
        }
    }

    public static String unFormatString(String str) {
        if (null == str)
            return null;
        return unEscapeCommandMonotonous0(str.substring(1, str.length() - 1));
    }

    private static String unEscapeCommandMonotonous0(String str) {
        if (null == str)
            return null;
        XStringWriter writer = new XStringWriter(str.length() * 2);
        unEscapeCommandMonotonousStyleString(writer, str);
        return writer.toString();
    }

    private static void unEscapeCommandMonotonousStyleString(XStringWriter out, String str) {
        if (null == out)
            throw new IllegalArgumentException("The Writer must not be null");
        if (null == str)
            return;
        int sz = str.length();
        boolean hadSlash = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (ch == '\'') {
                out.write('\'');
                i += 4;
                continue;
            } else {
                if (hadSlash) {
                    // handle an escaped value
                    hadSlash = false;
                    switch (ch) {
                        case '\\':
                            out.write('\\');
                            break;
                        case 'r':
                            out.write('\r');
                            break;
                        case 'f':
                            out.write('\f');
                            break;
                        case 't':
                            out.write('\t');
                            break;
                        case 'n':
                            out.write('\n');
                            break;
                        case 'b':
                            out.write('\b');
                            break;
                        default:
                            out.write(ch);
                            break;
                    }
                    continue;
                } else if (ch == '\\') {
                    hadSlash = true;
                    continue;
                }
            }
            out.write(ch);
        }
    }
















    protected String[] command;
    public String[] command() { return null == command?new String[]{}:command; }
    public ProcessExecutor command(String... command) {
        this.command = command;
        return this;
    }
    public ProcessExecutor command(String command) {
        if (command.length() == 0) {
            throw new IllegalArgumentException("Empty command");
        }
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
        }
        this.command = cmdarray;
        return this;
    }

    protected Charset charset;
    public Charset charset() {
        if (null == this.charset) {
            this.charset = Charset.forName(Finals.getOperateSystemCharsetOrDefaultCharset());
        }
        return charset;
    }
    public ProcessExecutor charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    protected String[] environment;
    public String[] environment() {
        return null == environment ? Finals.getOperateSystemEnvironment() : environment;
    }
    public ProcessExecutor environment(String[] environment) {
        if (null == environment) { throw new NullPointerException("environment"); }
        this.environment = environment;
        return this;
    }


    protected File dir;
    public File directory() { return dir; }
    public ProcessExecutor directory(File dir) { this.dir = dir; return this; }
    public ProcessExecutor directory(String dir) { this.dir = new File(dir); return this; }


    public ProcessObject exec() throws IOException {
        return this.exec(Runtime.getRuntime());
    }
    public ProcessObject exec(Runtime runtime) throws IOException {
        Process process = runtime.exec(this.command(), this.environment(), this.directory());
        return new ProcessObject(this, process);
    }





    static final ByteBufferFilter getReadLineFilter() {
        return ByteBufferFilter.getReadLineFilter();
    }


    public static class ProcessObject {
        ProcessExecutor processExecutor;
        Process process;

        Charset charset;
        public ProcessObject(ProcessExecutor processExecutor, Process process) {
            this.processExecutor = processExecutor;
            this.process = process;
            this.charset = processExecutor.charset();
        }

        public ProcessExecutor getProcessExecutor() {
            return this.processExecutor;
        }
        public Process process() {
            return this.process;
        }


        InputStream inputStream;
        InputStream errorStream;
        InputStream getInputStream() {
            return null == this.inputStream?this.inputStream = this.process.getInputStream():this.inputStream;
        }
        InputStream getErrorStream() {
            return null == this.errorStream?this.errorStream = this.process.getErrorStream():this.errorStream;
        }
        OutputStream outputStream;
        OutputStream getOutputStream() {
            return null == this.outputStream?this.outputStream = this.process.getOutputStream():this.outputStream;
        }


        public String toString(byte[] bytes) {
            if (null == bytes) { return null; }
            if (null == this.charset) { return new String(bytes); }

            return new String(bytes, this.charset);
        }
        public byte[] toBytes(String str) {
            if (null == str) { return null; }
            if (null == this.charset) { return str.getBytes(); }

            return str.getBytes(this.charset);
        }




        ByteBufferInputStream<InputStream> bufferInputStream;
        public ByteBufferInputStream<InputStream> getBufferInputStream() { return null == this.bufferInputStream?this.bufferInputStream = new ByteBufferInputStream(this.getInputStream()): this.bufferInputStream; }
        public int getInputStreamAvailable() { try { return this.getBufferInputStream().available(); } catch (IOException e) { return 0; } }

        public byte[] readInputLine() throws IOException {
            ByteBufferInputStream<InputStream> bufferInputStream = this.getBufferInputStream();
            byte[] bytes = bufferInputStream.readLine();
            return bytes;
        }
        public String readInputLineString() throws IOException {
            byte[] bytes = this.readInputLine();
            return toString(bytes);
        }
        public byte[] readInputAll() throws IOException {
            ByteBufferInputStream<InputStream> b = this.getBufferInputStream();
            ByteBufferOperate byteArrayBuffer = new ByteBufferOperate() {
                @Override
                public int stream_read(byte[] buf, int off, int len) throws IOException {
                    return b.read(buf, off, len);
                }
            };
            boolean read = false;
            while (true){
                if (byteArrayBuffer.append_from_stream_read(8192) == -1) {
                    break;
                } else {
                    read = true;
                }
            }
            byte[] bytes = read?byteArrayBuffer.toArray():null; byteArrayBuffer.remove();
            return bytes;
        }
        public String readInputAllString() throws IOException {
            byte[] bytes = this.readInputAll();
            return toString(bytes);
        }


        ByteBufferInputStream<InputStream> bufferErrorStream;
        public ByteBufferInputStream<InputStream> getBufferErrorStream() {return null == this.bufferErrorStream?this.bufferErrorStream = new ByteBufferInputStream<InputStream>(this.getErrorStream()): this.bufferErrorStream; }
        public int getErrorStreamAvailable() { try { return this.getBufferErrorStream().available(); } catch (IOException e) { return 0; }}


        public byte[] readErrorLine() throws IOException {
            ByteBufferInputStream<InputStream> bufferInputStream = this.getBufferErrorStream();
            byte[] bytes = bufferInputStream.readLine();
            return bytes;
        }
        public String readErrorLineString() throws IOException {
            byte[] bytes = this.readErrorLine();
            return toString(bytes);
        }
        public byte[] readErrorAll() throws IOException {
            ByteBufferInputStream<InputStream> b = this.getBufferErrorStream();
            ByteBufferOperate byteArrayBuffer = new ByteBufferOperate() {
                @Override
                public int stream_read(byte[] buf, int off, int len) throws IOException {
                    return b.read(buf, off, len);
                }
            };
            boolean read = false;
            while (true){
                if (byteArrayBuffer.append_from_stream_read(8192) == -1) {
                    break;
                } else {
                    read = true;
                }
            }
            byte[] bytes = read?byteArrayBuffer.toArray():null; byteArrayBuffer.remove();
            return bytes;
        }
        public String readErrorAllString() throws IOException {
            byte[] bytes = this.readErrorAll();
            return toString(bytes);
        }


        BufferedOutputStream bufferedOutputStream;
        public BufferedOutputStream getBufferedOutputStream() {
            return null == this.bufferedOutputStream?this.bufferedOutputStream = new BufferedOutputStream(this.getOutputStream()): this.bufferedOutputStream;
        }
        public ProcessObject appendOutput(byte[] bytes) throws IOException {
            BufferedOutputStream bufferedOutputStream = this.getBufferedOutputStream();
            bufferedOutputStream.write(bytes);
            return this;
        }
        public ProcessObject appendOutput(String bytes) throws IOException {
            return this.appendOutput(toBytes(bytes));
        }
        public ProcessObject flashOutput() throws IOException {
            BufferedOutputStream bufferedOutputStream = this.getBufferedOutputStream();
            bufferedOutputStream.flush();
            getOutputStream().flush();
            return this;
        }


        public int waitFor() throws InterruptedException {
            return this.process.waitFor();
        }
        public boolean waitFor(long timeout, TimeUnit unit) throws InterruptedException {
            return this.process.waitFor(timeout, unit);
        }


        public boolean close() {
            try {
                this.process.destroyForcibly();
                return true;
            } catch (Throwable throwable){
                return false;
            }
        }





        @SuppressWarnings("all")
        /**
         * @see ProcessImpl#exitValue()
         * @return exited ? exitCode : null
         */
        public Integer getExitValue() {
            try {
                return this.process.exitValue();
            } catch (IllegalThreadStateException illegalThreadStateException){
                return null;
            }
        }


        public boolean isExited() { return null != this.getExitValue(); }
        public boolean isActive() { return !this.isExited(); }

        public boolean isNormalExit() {
            Integer code = this.getExitValue();
            return null != code && ProcessExecutor.isNormalExit(code);
        }
        public boolean isErrorExit() {
            Integer code = this.getExitValue();
            return null != code && ProcessExecutor.isErrorExit(code);
        }







        public static class ByteBufferInputStream<T extends InputStream> extends InputStream implements top.fols.box.io.interfaces.XInterfaceGetInnerStream<T> {
            private int mark;
            private int markLimit;
            private ByteBufferOperate buffer;
            private T inputStream;

            BufferFilter<byte[]> READ_LINE_FILTER = getReadLineFilter();

            public ByteBufferInputStream(T inputStream) {
                this.inputStream = inputStream;
                this.buffer = new ByteBufferOperate() {
                    @Override
                    public int stream_read(byte[] buf, int off, int len) throws IOException { return ByteBufferInputStream.this.inputStream.read(buf, off, len); }
                };
                this.mark = -1;
                this.markLimit = 8192;
            }
            
            public byte[] readLine() throws IOException {
                return this.readLine(true);
            }
            public byte[] readLine(boolean addSeparator) throws IOException {
                byte[] buffer = null;
                if (!this.buffer.readFilterIFEnd(READ_LINE_FILTER)) {
                    buffer = READ_LINE_FILTER.result(addSeparator);
                }
                if (this.buffer.removeIfOverflow(markLimit)) {
                    this.mark = -1;
                }
                return buffer;
            }


            public int read() throws IOException {
                int read = this.buffer.read();
                if (read > -1) {
                    if (this.buffer.removeIfOverflow(markLimit)) {
                        this.mark = -1;
                    }
                }
                return read;
            }
            public int read(byte[] b) throws IOException {
                return this.read(b, 0, b.length);
            }
            public int read(byte[] b, int off, int len) throws IOException {
                int read = this.buffer.read(b, off, len);
                if (read > -1) {
                    if (this.buffer.removeIfOverflow(markLimit)) {
                        this.mark = -1;
                    }
                }
                return read;
            }

            public long skip(long n) throws IOException {
                if (n < 0) { return 0; }
                long ki = Math.min(this.buffer.available(), (int) Math.min(n, Integer.MAX_VALUE));
                this.buffer.positionSkip((int) ki);
                if (ki != n) {
                    long s_skip = this.inputStream.skip(n - ki);
                    ki += s_skip;
                }
                return ki;
            }

            public void close() { this.buffer.remove(); }
            public void mark(int readlimit) {
                int position = this.buffer.position();
                this.markLimit =  position + readlimit;
                this.mark = position;
            }
            public void reset() throws IOException {
                if (this.mark < 0) {
                    throw new IOException("Resetting to invalid mark");
                }
                this.buffer.position(this.mark);
            }
            public boolean markSupported() {
                return true;
            }


            public int available() throws IOException { return this.buffer.available() + this.inputStream.available(); }

            public ByteBufferOperate buffer() { return this.buffer; }
            public void releaseBuffer() {
                // TODO: Implement this method
                this.buffer.remove();
            }

            @Override
            public T getInnerStream() {
                return this.inputStream;
            }
        }



    }






    public static boolean isNormalExit(int exitValue) { return exitValue == 0; }
    public static boolean isErrorExit(int exitValue) { return exitValue != 0; }


    public static int getProcessResult(java.lang.Process proc, OutputStream resultOutput, OutputStream errorOutput) throws IOException, InterruptedException {
        BufferFilter<byte[]> READ_LINE_FILTER = getReadLineFilter();

        InputStream in = proc.getInputStream();
        InputStream error = proc.getErrorStream();

        /**
         * System.exit(0)：正常退出
         * System.exit(1)：异常退出
         */

        int exitValue = proc.exitValue();
        int error_length = 0;
        if (error.available() > 0 || ProcessExecutor.isErrorExit(exitValue)) {
            ByteBufferOutputStream buf = new ByteBufferOutputStream();
            ByteBufferOperate row = new ByteBufferOperate() {
                @Override
                public int stream_read(byte[] buf, int off, int len) throws IOException { return error.read(buf, off, len); }
            };
            byte[] buffer;
            while (!row.readFilterIFEnd(READ_LINE_FILTER)) {
                buffer = READ_LINE_FILTER.result(true);
                buf.write(buffer);
                error_length += buffer.length;
            }

            byte[] errorMessage = buf.toByteArray(); buf.close();
            errorOutput.write(errorMessage);
            errorOutput.flush();
        }
        int normal_length = 0;
        ByteBufferOperate inRow = new ByteBufferOperate() {
            @Override
            public int stream_read(byte[] buf, int off, int len) throws IOException { return in.read(buf, off, len); }
        };
        byte[] buffer;
        while (!inRow.readFilterIFEnd(READ_LINE_FILTER)) {
            buffer = READ_LINE_FILTER.result(true);
            resultOutput.write(buffer);
            normal_length += buffer.length;
        }
        resultOutput.flush();

        return exitValue;
    }
    public static byte[] getProcessResultBytes(java.lang.Process process, Charset charset) throws IOException, InterruptedException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        int exitValue = getProcessResult(process, result, error);
        if (isErrorExit(exitValue) && error.size() > 0) {
            String errorMessage = new String(error.toByteArray(), charset);
            throw new RuntimeException(errorMessage);
        }
        return result.toByteArray();
    }
    public static String getProcessResultString(java.lang.Process process, Charset charset) throws IOException, InterruptedException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        int exitValue = getProcessResult(process, result, error);
        if (isErrorExit(exitValue) && exitValue != 0) {
            String errorMessage = new String(error.toByteArray(), charset);
            throw new RuntimeException(errorMessage);
        }
        return new String(result.toByteArray(), charset);
    }




}

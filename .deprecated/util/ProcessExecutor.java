package top.fols.box.lang;

import top.fols.atri.io.BufferInputStreams;
import top.fols.atri.io.BytesOutputStreams;
import top.fols.atri.io.CodeDelimiter;
import top.fols.box.io.buffer.ByteBufferLinesInputStream;
import top.fols.box.io.buffer.BufferFilter;
import top.fols.box.io.buffer.bytes.ByteBufferFilter;
import top.fols.box.io.buffer.bytes.ByteBufferOperate;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.io.StringWriters;
import top.fols.box.util.encode.HexEncoders;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Deprecated
//test
public class ProcessExecutor {
    public ProcessExecutor() {}




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

    public static final String LINE_SEPARATOR = Finals.LineSeparator.getSystemLineSeparator();//w command line spearator
    public static final String DEFAULT_EXIT_COMMAND = "exit";



    static CodeDelimiter.IBytesSeparatorDelimiter COMMAND_READ_LINE_DELIMITER = CodeDelimiter.build(new byte[][]{
            Finals.LineSeparator.LINE_SEPARATOR_STRING_N.getBytes(Finals.Charsets.getOperateSystemCharsetOrDefaultCharset())
    });
    static final byte[][] COMMAND_READ_LINE_SEPARATOR = COMMAND_READ_LINE_DELIMITER.cloneSeparators();


    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, Charset charset)
            throws IOException, InterruptedException {
        return ProcessExecutor.execCommandToString(runDir, createCommand, commands, ProcessExecutor.DEFAULT_EXIT_COMMAND, charset,
                null);
    }

    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, Charset charset,
                                             OutputStream errorOutput) throws IOException, InterruptedException {
        return ProcessExecutor.execCommandToString(runDir, createCommand, commands, ProcessExecutor.DEFAULT_EXIT_COMMAND, charset,
                errorOutput);
    }

    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, String exit,
                                             Charset charset, OutputStream errorOutput) throws IOException, InterruptedException {
        BytesOutputStreams res = new BytesOutputStreams();
        ProcessExecutor.execCommand(runDir, createCommand, commands, exit, charset, res, errorOutput);
        String str = new String(res.toByteArray(), charset);
        res.close();
        return str;
    }

    public static int execCommand(String runDir, String[] createCommand, String[] commands, String exit,
                                  Charset charset, OutputStream outputresult, OutputStream errorOutput) throws IOException, InterruptedException {
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
            BytesOutputStreams buf = new BytesOutputStreams();
            BufferInputStreams<InputStream> row = new BufferInputStreams<>(error);
            row.setDelimiter(COMMAND_READ_LINE_DELIMITER);
            byte[] bufferarray;
            while (null != (bufferarray = row.readNextLine(true))) {
                buf.write(bufferarray);
            }
            if (Arrayy.startsWith(buf.buffer(), COMMAND_READ_LINE_SEPARATOR, buf.size() - COMMAND_READ_LINE_SEPARATOR.length)) {
                buf.buffer_length(buf.size() - COMMAND_READ_LINE_SEPARATOR.length);
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
            BufferInputStreams<InputStream> row = new BufferInputStreams<>(in);
            row.setDelimiter(COMMAND_READ_LINE_DELIMITER);
            byte[] bufferarray;
            while (null != (bufferarray = row.readNextLine(true))) {
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
        String hex = HexEncoders.encodeToString(bytes);
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
        StringWriters writer = new StringWriters(str.length() * 2);
        escapeCommandMonotonousStyleString(writer, str);
        return writer.toString();
    }

    private static void escapeCommandMonotonousStyleString(StringWriters out, String str) {
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
        StringWriters writer = new StringWriters(str.length() * 2);
        unEscapeCommandMonotonousStyleString(writer, str);
        return writer.toString();
    }

    private static void unEscapeCommandMonotonousStyleString(StringWriters out, String str) {
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
                    // handle an escaped tip
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
            this.charset = Charset.forName(Finals.Charsets.getOperateSystemCharsetOrDefaultCharsetName());
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
        return ByteBufferFilter.lineFilterBytes();
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




        ByteBufferLinesInputStream<InputStream> bufferInputStream;
        public ByteBufferLinesInputStream<InputStream> getBufferInputStream() { return null == this.bufferInputStream?this.bufferInputStream = new ByteBufferLinesInputStream(this.getInputStream()): this.bufferInputStream; }
        public int getInputStreamAvailable() { try { return this.getBufferInputStream().available(); } catch (IOException e) { return 0; } }

        public byte[] readInputLine() throws IOException {
            ByteBufferLinesInputStream<InputStream> bufferInputStream = this.getBufferInputStream();
            byte[] bytes = bufferInputStream.readLine();
            return bytes;
        }
        public String readInputLineString() throws IOException {
            byte[] bytes = this.readInputLine();
            return toString(bytes);
        }
        public byte[] readInputAll() throws IOException {
            final ByteBufferLinesInputStream<InputStream> b = this.getBufferInputStream();
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


        ByteBufferLinesInputStream<InputStream> bufferErrorStream;
        public ByteBufferLinesInputStream<InputStream> getBufferErrorStream() {return null == this.bufferErrorStream?this.bufferErrorStream = new ByteBufferLinesInputStream<InputStream>(this.getErrorStream()): this.bufferErrorStream; }
        public int getErrorStreamAvailable() { try { return this.getBufferErrorStream().available(); } catch (IOException e) { return 0; }}


        public byte[] readErrorLine() throws IOException {
            ByteBufferLinesInputStream<InputStream> bufferInputStream = this.getBufferErrorStream();
            byte[] bytes = bufferInputStream.readLine();
            return bytes;
        }
        public String readErrorLineString() throws IOException {
            byte[] bytes = this.readErrorLine();
            return toString(bytes);
        }
        public byte[] readErrorAll() throws IOException {
            final ByteBufferLinesInputStream<InputStream> b = this.getBufferErrorStream();
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
        public ProcessObject append(byte[] bytes) throws IOException {
            BufferedOutputStream bufferedOutputStream = this.getBufferedOutputStream();
            bufferedOutputStream.write(bytes);
            return this;
        }
        public ProcessObject append(String bytes) throws IOException {
            return this.append(toBytes(bytes));
        }
        public ProcessObject appendLine() throws IOException {
            return this.append(toBytes(Finals.LineSeparator.getSystemLineSeparator()));
        }
        public ProcessObject flash() throws IOException {
            BufferedOutputStream bufferedOutputStream = this.getBufferedOutputStream();
            bufferedOutputStream.flush();
            getOutputStream().flush();
            return this;
        }


        public int waitFor() throws InterruptedException {
            return this.process.waitFor();
        }



        static final Method destroyForcibly;
        static {
            destroyForcibly = Reflects.method(Process.class, "destroyForcibly", Finals.EMPTY_CLASS_ARRAY);
        }


        public boolean close() {
            //jdk1.8
            try {
                if (null != destroyForcibly)
                    destroyForcibly.invoke(this.process);
                return true;
            } catch (Throwable ignored) {}
            try {
                this.process.destroy();
                return true;
            } catch (Throwable ignored) {}
            return false;
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
            Integer var = this.getExitValue();
            return null != var && ProcessExecutor.isNormalExit(var);
        }
        public boolean isErrorExit() {
            Integer var = this.getExitValue();
            return null != var && ProcessExecutor.isErrorExit(var);
        }


    }






    public static boolean isNormalExit(int exitValue) { return exitValue == 0; }
    public static boolean isErrorExit(int exitValue) { return exitValue != 0; }


    public static int getProcessResult(java.lang.Process proc, OutputStream resultOutput, OutputStream errorOutput) throws IOException, InterruptedException {
        BufferFilter<byte[]> READ_LINE_FILTER = getReadLineFilter();

        final InputStream in = proc.getInputStream();
        final InputStream error = proc.getErrorStream();

        /**
         * System.exit(0)：正常退出
         * System.exit(1)：异常退出
         */

        int exitValue = proc.exitValue();
        int error_length = 0;
        if (error.available() > 0 || ProcessExecutor.isErrorExit(exitValue)) {
            BytesOutputStreams buf = new BytesOutputStreams();
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

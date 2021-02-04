package top.fols.atri.lang;

import top.fols.atri.io.ByteBufferOutputStream;
import top.fols.atri.io.buffer.bytes.ByteBufferFilter;
import top.fols.atri.io.buffer.bytes.ByteBufferOperate;

import java.io.*;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class ProcessExecutor {
    public ProcessExecutor() {
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





    static final ByteBufferFilter READ_LINE_FILTER = new ByteBufferFilter() {{
            this.addSeparator(new byte[]{'\r', '\n'});
            this.addSeparator(new byte[]{'\r'});
            this.addSeparator(new byte[]{'\n'});
        }
        @Override
        protected boolean accept(int last, int search, byte[] split, boolean readEnd) {
            return super.accept(last, search, split, readEnd);
        }
    };

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


    public static int getProcessResult(java.lang.Process proc, Charset charset, OutputStream resultOutput, OutputStream errorOutput) throws IOException, InterruptedException {
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
        int exitValue = getProcessResult(process, charset, result, error);
        if (isErrorExit(exitValue) && error.size() > 0) {
            String errorMessage = new String(error.toByteArray(), charset);
            throw new RuntimeException(errorMessage);
        }
        return result.toByteArray();
    }
    public static String getProcessResultString(java.lang.Process process, Charset charset) throws IOException, InterruptedException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        int exitValue = getProcessResult(process, charset, result, error);
        if (isErrorExit(exitValue) && exitValue != 0) {
            String errorMessage = new String(error.toByteArray(), charset);
            throw new RuntimeException(errorMessage);
        }
        return new String(result.toByteArray(), charset);
    }




}

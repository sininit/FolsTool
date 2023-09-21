package top.fols.box.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import top.fols.atri.assist.util.OSInfo;
import top.fols.atri.io.BufferInputStreams;
import top.fols.atri.io.Delimiter;
import top.fols.atri.io.StringWriters;
import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;
import top.fols.atri.reflect.Reflects;
import top.fols.box.util.encode.HexEncoders;

@Deprecated
//test
public class ProcessExecutors {
    public ProcessExecutors() {}


	public static final String LINE_SEPARATOR = Finals.LineSeparator.getSystemLineSeparator();//w command line spearator
    public static final String DEFAULT_EXIT_COMMAND = "exit";

    static final String   EXTENSION_SEPARATOR = Filex.FILE_EXTENSION_NAME_SEPARATORS;
    static final String[] WINDOWS_EXECUTABLE_EXTENSIONS = {"exe","com","bat","cmd"};
	
    public static final String ENV_PATH = "PATH"; //env ignoreCase
    public static File[] envPATH() {
        List<String> ps = Strings.splitSkipEmpty(System.getenv(ENV_PATH), File.pathSeparator);
        List<File> pfs = new ArrayList<>();
        for (String path : ps) {
            File f = new File(path);
            pfs.add(f);
        }
        return pfs.toArray(new File[]{});
    }

    public static String findEnvPATHCanExecuteCommandFilePath(String command) {
        return findEnvPATHCanExecuteCommandFilePath(envPATH(), command);
    }
    public static String findEnvPATHCanExecuteCommandFilePath(File[] pathDirectors, String command) {
        for (File file : pathDirectors) {
            File cf = new File(file, command);
            if (cf.canExecute()) {
                return cf.getAbsolutePath();
            }
        }
        OSInfo.OSType osType = OSInfo.getOSType();
        if (OSInfo.isWindows(osType)) {
            String fi = findEnvPATHFromWindows(pathDirectors, command);
            if (fi != null) return fi;
        }

        String fi = findEnvPATHFromUnix(pathDirectors, command);
        if (fi != null) return fi;

        return null;
    }

    private static String findEnvPATHFromWindows(File[] pathDirectors, String command) {
        for (File file : pathDirectors) {
            File[] listFiles = file.listFiles();
            if (null != listFiles) {
                for (File fi: listFiles) {
                    for (String ex: WINDOWS_EXECUTABLE_EXTENSIONS) {
                        if ((command + EXTENSION_SEPARATOR + ex).equalsIgnoreCase(fi.getName())) {
                            if (fi.canExecute()) {
                                return fi.getAbsolutePath();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    private static String findEnvPATHFromUnix(File[] pathDirectors, String command) {
        for (File file : pathDirectors) {
            File[] listFiles = file.listFiles();
            if (null != listFiles) {
                for (File fi: listFiles) {
                    //It's better not to, because Unix should be case-sensitive
                    if (command.equalsIgnoreCase(fi.getName())) {
                        if (fi.canExecute()) {
                            return fi.getAbsolutePath();
                        }
                    }
                }
            }
        }
        return null;
    }


    public static String requireEnvPATHCanExecuteCommandFilePath(String command) throws FileNotFoundException {
        String fp = findEnvPATHCanExecuteCommandFilePath(command);
        if (null == fp) {
            throw new FileNotFoundException(fp);
        }
        return fp;
    }



    















    protected String[] command;
    public String[] command() { return null == command ?new String[]{}: command; }
    public ProcessExecutors command(String... command) {
        this.command = command;
        return this;
    }
    public ProcessExecutors command(String command) {
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
    public ProcessExecutors charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    protected String[] environment;
    public String[] environment() {
        return null == environment ? Finals.getOperateSystemEnvironment() : environment;
    }
    public ProcessExecutors environment(String[] environment) {
        if (null == environment) { throw new NullPointerException("environment"); }
        this.environment = environment;
        return this;
    }


    protected File dir;
    public File directory() { return dir; }
    public ProcessExecutors directory(File dir) { this.dir = dir; return this; }
    public ProcessExecutors directory(String dir) { this.dir = new File(dir); return this; }


    public ProcessObject exec() throws IOException {
        return this.exec(Runtime.getRuntime());
    }
    public ProcessObject exec(Runtime runtime) throws IOException {
		Process process = runtime.exec(this.command(), this.environment(), this.directory());
        return new ProcessObject(this, process);
    }








    public static class ProcessObject {
        ProcessExecutors processExecutor;
        Process process;
        Charset charset;
		Delimiter.IBytesDelimiter lineDelimiter;

        public ProcessObject(ProcessExecutors processExecutor, Process process) {
            this.processExecutor = processExecutor;
            this.process = process;
            this.charset = processExecutor.charset();
			this.lineDelimiter = Delimiter.build(Finals.LineSeparator.getAllSystemLineSeparatorBytesSortedMaxToMin(charset));
        }

        public ProcessExecutors getProcessExecutor() {
            return this.processExecutor;
        }
        public Process process() {
            return this.process;
        }


        InputStream inputStream;
        InputStream getInputStream() {
            return null == this.inputStream ?this.inputStream = this.process.getInputStream(): this.inputStream;
        }
        InputStream errorStream;
		InputStream getErrorStream() {
            return null == this.errorStream ?this.errorStream = this.process.getErrorStream(): this.errorStream;
        }
        OutputStream outputStream;
        OutputStream getOutputStream() {
            return null == this.outputStream ?this.outputStream = this.process.getOutputStream(): this.outputStream;
        }

        public Charset charset(){ return charset; }

        public String convertToString(byte[] bytes) {
            if (null == bytes) { return null; }
            if (null == this.charset) { return new String(bytes); }

            return new String(bytes, this.charset);
        }
        public byte[] convertToBytes(String str) {
            if (null == str) { return null; }
            if (null == this.charset) { return str.getBytes(); }

            return str.getBytes(this.charset);
        }


		public static class Input extends BufferInputStreams<InputStream> {
			private Input(InputStream in, Delimiter.IBytesDelimiter b, Charset charset) {
				super(in);
				super.setDelimiter(b);
				super.setDelimiterAsStringCharset(charset);
			}
			@Override public void setDelimiter(byte[][] p1) { throw new UnsupportedOperationException(); }
			@Override public void setDelimiter(Delimiter.IBytesDelimiter p1) { throw new UnsupportedOperationException(); }
			@Override public void setDelimiterAsStringCharset(Charset p1) { throw new UnsupportedOperationException(); }
		}


		Input bufferIn;
		public Input getBufferInputStream() {
			return null == bufferIn ? bufferIn = new Input(getInputStream(), lineDelimiter, charset) : bufferIn;
		}
		Input bufferEr;
		public Input getBufferErrorStream() {
			return null == bufferEr ? bufferEr = new Input(getErrorStream(), lineDelimiter, charset) : bufferEr; 
		}




        BufferedOutputStream bufferedOutputStream;
        public BufferedOutputStream getBufferedOutputStream() {
            return null == this.bufferedOutputStream ? this.bufferedOutputStream = new BufferedOutputStream(this.getOutputStream()): this.bufferedOutputStream;
        }
		public ProcessObject append(String bytes) throws IOException {
            return this.append(convertToBytes(bytes));
        }
        public ProcessObject appendLine() throws IOException {
            return this.append(convertToBytes(Finals.LineSeparator.getSystemLineSeparator()));
        }
        public ProcessObject append(byte[] bytes) throws IOException {
            this.getBufferedOutputStream().write(bytes);
            return this;
        }
        public ProcessObject flash() throws IOException {
			this.getBufferedOutputStream().flush();
			this.getOutputStream().flush();
            return this;
        }


        public int waitFor() throws InterruptedException {
			try {
				this.flash();
			} catch (IOException ignored) {}
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
            } catch (IllegalThreadStateException illegalThreadStateException) {
                return null;
            }
        }


        public boolean isExited() { return null != this.getExitValue(); }
        public boolean isActive() { return !this.isExited(); }

        public boolean isNormalExit() {
            Integer v = this.getExitValue();
            return null != v && ProcessExecutors.isNormalExit(v);
        }
        public boolean isErrorExit() {
            Integer v = this.getExitValue();
            return null != v && ProcessExecutors.isErrorExit(v);
        }
    }
    public static boolean isNormalExit(int exitValue) { return exitValue == 0; }
    public static boolean isErrorExit(int exitValue) { return exitValue != 0; }






	//*************************************************************





	@Deprecated
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

    @Deprecated
	public static String formatString(String str) {
        if (null == str)
            return null;
        return '\'' + escapeCommandMonotonous0(str) + '\'';
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
	
	@Deprecated
    public static String unformedString(String str) {
        if (null == str)
            return null;
        return unescapeCommandMonotonous0(str.substring(1, str.length() - 1));
    }

    private static String unescapeCommandMonotonous0(String str) {
        if (null == str)
            return null;
        StringWriters writer = new StringWriters(str.length() * 2);
        unescapeCommandMonotonousStyleString(writer, str);
        return writer.toString();
    }
    private static void unescapeCommandMonotonousStyleString(StringWriters out, String str) {
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

}
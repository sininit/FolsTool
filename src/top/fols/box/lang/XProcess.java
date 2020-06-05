package top.fols.box.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import top.fols.box.io.base.XByteArrayOutputStream;
import top.fols.box.io.base.XInputStreamLine;
import top.fols.box.io.base.XStringWriter;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.statics.XStaticSystem;
import top.fols.box.util.XArrays;
import top.fols.box.util.encode.XHexEncoder;

public class XProcess {
    public static final String EV_PATH = "PATH";

    public static File[] envPATH() {
        List<String> ps = XString.split(System.getenv(XProcess.EV_PATH), File.pathSeparator);
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
        return XProcess.execCommandToString(runDir, createCommand, commands, XProcess.DEFAULT_EXIT_COMMAND, charset,
                null);
    }

    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, Charset charset,
            OutputStream errorOutput) throws IOException {
        return XProcess.execCommandToString(runDir, createCommand, commands, XProcess.DEFAULT_EXIT_COMMAND, charset,
                errorOutput);
    }

    public static String execCommandToString(String runDir, String[] createCommand, String[] commands, String exit,
            Charset charset, OutputStream errorOutput) throws IOException {
        XByteArrayOutputStream res = new XByteArrayOutputStream();
        XProcess.execCommand(runDir, createCommand, commands, exit, charset, res, errorOutput);
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

}

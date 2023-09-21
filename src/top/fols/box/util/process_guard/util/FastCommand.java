package top.fols.box.util.process_guard.util;

import java.io.*;
import java.nio.charset.*;
import top.fols.atri.assist.encode.*;
import top.fols.atri.assist.util.*;
import top.fols.atri.io.*;
import top.fols.atri.io.util.*;
import top.fols.atri.lang.*;
import top.fols.box.util.*;

@SuppressWarnings("rawtypes")
@Deprecated
public class FastCommand {
	public FastCommand(ProcessExecutors.ProcessObject po) {
		this.po = Objects.requireNonNull(po, "parameter");

		/*
		    System.out.println(Arrays.toString("\r\n".getBytes("GBK")));
			System.out.println(Arrays.toString("\r\n".getBytes("UTF-8")));
			System.out.println(Arrays.toString("\r\n".getBytes("Big5")));
			System.out.println(Arrays.toString("\r\n".getBytes("ISO-8859-1")));
			System.out.println(Arrays.toString("\r\n".getBytes("ISO-8859-5")));
			System.out.println(Arrays.toString("\r\n".getBytes("ISO-8859-7")));
			System.out.println(Arrays.toString("\r\n".getBytes("ISO-8859-9")));
			System.out.println(Arrays.toString("\r\n".getBytes("Shift-JIS")));
			System.out.println(Arrays.toString("\r\n".getBytes("EUC-KR")));
			System.out.println(Arrays.toString("\r\n".getBytes("Windows-1251")));
			System.out.println(Arrays.toString("\r\n".getBytes("Windows-1256")));
			System.out.println(Arrays.toString("\r\n".getBytes("Windows-1258")));
			System.out.println(Arrays.toString("\r\n".getBytes("KOI8-R")));
			System.out.println(Arrays.toString("\r\n".getBytes("TIS-620")));

			[13, 10]
		 */
		this.READ_LINE_FILTER = Delimiter.build(Delimiter.lineCharDelimit().cloneSeparators(), po.charset());

		try {
			this.checkEcho();
		} catch (FileNotFoundException e) {
			throw new UnsupportedOperationException(e);
		}
	}



	static OSInfo.OSType system = OSInfo.getOSType();

	String session_start = Base64Encoder.getEncoder().encodeToString((System.currentTimeMillis() + ":" + System.nanoTime() + ":" + Strings.random("abcdefghijklnmopqrstuvwxyz0123456789", 12)).getBytes()).toLowerCase();
	String session_end   = Base64Encoder.getEncoder().encodeToString((System.currentTimeMillis() + ":" + System.nanoTime() + ":" + Strings.random("abcdefghijklnmopqrstuvwxyz0123456789", 12)).getBytes()).toLowerCase();


	protected String commandJoin(String... commands) {
		Objects.requireNonNull(commands, "command");

		String separator = "\u0020";
		StringBuilder buffer = new StringBuilder();
		for (String command: commands) {
			buffer.append(command).append(separator);
		}
		buffer.setLength(buffer.length() > separator.length() ? buffer.length() - separator.length(): buffer.length());
		return buffer.toString();
	}



	protected void checkEcho() throws FileNotFoundException {
		if (OSInfo.isWindows(system)) {
		} else if (OSInfo.isLinux(system) || OSInfo.isUnix(system)) {
			findCommand("echo");
		} else {
			throw new UnsupportedOperationException(String.valueOf(system));
		}
	}
	protected String wrapEchoSession(String command) {
		Objects.requireNonNull(command, "command");

		String commandSeparator;
		if (OSInfo.isWindows(system)) {
			commandSeparator = "&";
		} else if (OSInfo.isLinux(system) || OSInfo.isUnix(system)) {
			commandSeparator = ";";
		} else {
			throw new UnsupportedOperationException(String.valueOf(system) + " command: " + command);
		}
		return commandJoin("echo", 
						   session_start + commandSeparator, 
						   command + commandSeparator, 
						   "echo", session_end);
	}

	public static FastCommand openProcess() { return openProcess(null, null); }
	public static FastCommand openProcess(File directory, String cmd) {
		ProcessExecutors pe = new ProcessExecutors();
		pe.directory(directory);
		String command;
		if (null == cmd) {
			if (OSInfo.isWindows(system)) {
				command = ("cmd");
			} else if (OSInfo.isLinux(system) || OSInfo.isUnix(system)) {
				String s = "sh";
				String cmd2;
				if (null != (cmd2 = ProcessExecutors.findEnvPATHCanExecuteCommandFilePath("su"))) {
					s = cmd2;
				}
				command = (s);
			} else {
				throw new UnsupportedOperationException(String.valueOf(system));
			}
		} else {
			command = (cmd);
		}
		pe.command(command);

		try {
			ProcessExecutors.ProcessObject po = pe.exec();

			FastCommand fc = new FastCommand(po);
			return fc;
		} catch (Throwable e) { throw new RuntimeException(e); }
	}





	void writeCommand(String command) throws IOException {
		BufferedOutputStream out = (BufferedOutputStream) this.getOutputStream();
		out.write(command.getBytes(charset()));
	}
	void writeLine() throws IOException {
		BufferedOutputStream out = (BufferedOutputStream) this.getOutputStream();
		out.write(System.lineSeparator().getBytes(charset()));
	}
	void writeFlash() throws IOException {
		BufferedOutputStream out = (BufferedOutputStream) this.getOutputStream();
		out.flush();
	}






	/**
	 * Windows system "echo" command comes with cmd
	 */
	public static String findCommand(String command) throws FileNotFoundException {
		return ProcessExecutors.requireEnvPATHCanExecuteCommandFilePath(command);
	}


	ProcessExecutors.ProcessObject po;
	public ProcessExecutors.ProcessObject getProcessObject() { return po; }

	public File directory()  { return this.po.getProcessExecutor().directory(); }
	public Charset charset() { return this.po.charset(); }
	public Process process() { return this.po.process(); }


	public boolean isOpen() { return !(null == process()); }



	public String toString(byte[] bytes) {
		if (null == bytes) { return null; }
		if (null == this.charset()) { return new String(bytes); }

		return new String(bytes, this.charset());
	}
	public byte[] toBytes(String str) {
		if (null == str) { return null; }
		if (null == this.charset()) { return str.getBytes(); }

		return str.getBytes(this.charset());
	}
	public boolean close() {
		final Process p = process();
		boolean c = Streams.close(new Closeable() {
				@Override
				public void close()  {
					// TODO: Implement this method
					p.destroyForcibly();
				}
			});
		if (c)
			return true;
		return Streams.close(new Closeable() {
				@Override
				public void close()  {
					// TODO: Implement this method
					p.destroy();
				}
			});
	}


	final Delimiter.IBytesDelimiter READ_LINE_FILTER;



	InputStream inputStream;
	InputStream errorStream;
	InputStream getInputStream() {
		return null == this.inputStream ?this.inputStream   = this.process().getInputStream(): this.inputStream;
	}
	InputStream getErrorStream() {
		return null == this.errorStream ?this.errorStream   = this.process().getErrorStream(): this.errorStream;
	}
	OutputStream outputStream;
	OutputStream getOutputStream() {
		return null == this.outputStream ?this.outputStream = this.process().getOutputStream(): this.outputStream;
	}


	BufferInputStreams bufferInputStream;
	BufferInputStreams getBufferInputStream() {
		if (this.bufferInputStream == null) {
			this.bufferInputStream = new BufferInputStreams(this.getInputStream());
			this.bufferInputStream.setDelimiter(READ_LINE_FILTER);
		}
		return this.bufferInputStream;
	}
	int getInputStreamAvailable() { try { return this.getBufferInputStream().available(); } catch (IOException e) { return 0; } }

	byte[] readInputLine() throws IOException {
		BufferInputStreams bufferInputStream = this.getBufferInputStream();
		byte[] bytes = bufferInputStream.readNextLine(true);
		return bytes;
	}
	String readInputLineString() throws IOException {
		byte[] bytes = this.readInputLine();
		return toString(bytes);
	}
	byte[] readInputAll() throws IOException {
		final BufferInputStreams b = this.getBufferInputStream();
		return Streams.toBytes(b);
	}
	String readInputAllString() throws IOException {
		byte[] bytes = this.readInputAll();
		return toString(bytes);
	}


	BufferInputStreams bufferErrorStream;
	BufferInputStreams getBufferErrorStream() {
		if (this.bufferErrorStream == null) {
			this.bufferErrorStream = new BufferInputStreams(this.getErrorStream());
			this.bufferErrorStream.setDelimiter(READ_LINE_FILTER);
		}
		return this.bufferErrorStream;
	}
	int getErrorStreamAvailable() { try { return this.getBufferErrorStream().available(); } catch (IOException e) { return 0; }}


	byte[] readErrorLine() throws IOException {
		BufferInputStreams bufferInputStream = this.getBufferErrorStream();
		byte[] bytes = bufferInputStream.readNextLine(true);
		return bytes;
	}
	String readErrorLineString() throws IOException {
		byte[] bytes = this.readErrorLine();
		return toString(bytes);
	}
	byte[] readErrorAll() throws IOException {
		final BufferInputStreams b = this.getBufferErrorStream();
		return Streams.toBytes(b);
	}
	String readErrorAllString() throws IOException {
		byte[] bytes = this.readErrorAll();
		return toString(bytes);
	}





	BufferedOutputStream bufferedOutputStream;
	BufferedOutputStream getBufferedOutputStream() {
		return null == this.bufferedOutputStream ?this.bufferedOutputStream = new BufferedOutputStream(this.getOutputStream()): this.bufferedOutputStream;
	}










	transient final Object executeLock = new Object();



	public String execute(String command) throws RuntimeException {
		byte[] temp = null;
		ByteArrayOutputStream error = null;
		int err;

		synchronized (executeLock) {
			try {
				String wrap = wrapEchoSession(command);

				this.writeCommand(wrap);
				this.writeLine();
				this.writeFlash();

				StringBuilder buffer = new StringBuilder();
				boolean join = false;
				String line;
				while (null != (line = this.readInputLineString())) {
					if (join) {
						if (line.contains(session_start)) { continue; }
						if (line.contains(session_end))    { break; }
						buffer.append(line);
					} else {
						if (line.contains(session_start)) {
							join = true;
						}
					}
				}

				while ((err = this.getErrorStreamAvailable()) > 0) {
					InputStream input = this.getErrorStream();
					temp = (null == temp || temp.length < err) ?new byte[err]: temp;
					int read = input.read(temp);

					error = null == error ? new ByteArrayOutputStream(): error;
					error.write(temp, 0, read);
				}
				if (!Objects.isEmpty(error)) {
					throw new RuntimeException(toString(error.toByteArray()).trim());
				}
				return buffer.toString();
			} catch (RuntimeException e) {
				throw e;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			} finally {
			}
		}
	}

}


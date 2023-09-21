package app.utils;

import top.fols.atri.assist.Base64Encoder;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Strings;
import top.fols.atri.interfaces.interfaces.IInnerStream;
import top.fols.box.io.buffer.BufferFilter;
import top.fols.box.io.buffer.bytes.ByteBufferFilter;
import top.fols.box.io.buffer.bytes.ByteBufferOperate;
import top.fols.box.lang.ProcessExecutor;

import java.io.*;
import java.nio.charset.Charset;


/**
 * can only execute commands that return results immediately
 */
// java.lang.ArrayIndexOutOfBoundsException: buffer.length=12288, buffer.limit=151, position=84, length=256
//	at top.fols.atri.io.buffer.BufferOperate.arraycopy(BufferOperate.java:287)
//	at top.fols.atri.io.buffer.BufferFilter.result(BufferFilter.java:88)
//	at top.fols.winapp.we_media.util.FastCommand$ByteBufferInputStream.readLine(FastCommand.java:269)
//	at top.fols.winapp.we_media.util.FastCommand$ByteBufferInputStream.readLine(FastCommand.java:264)
//	at top.fols.winapp.we_media.util.FastCommand.readInputLine(FastCommand.java:175)
//	at top.fols.winapp.we_media.util.FastCommand.readInputLineString(FastCommand.java:179)
//	at top.fols.winapp.we_media.util.FastCommand.execute(FastCommand.java:417)
//	at top.fols.winapp.we_media.automation.selenium.GlobalSelenium$Chrome$ChromeDriverInstanceManager.lambda$newAdapterInstanceFromFreePort$1(GlobalSelenium.java:402)
//	at top.fols.atri.lock.LockThread$1.invoke(LockThread.java:48)
//	at top.fols.atri.lock.LockThread$1.invoke(LockThread.java:44)
//	at top.fols.atri.lock.LockThread$Lock.run(LockThread.java:220)
//java.lang.NullPointerException
//	at top.fols.winapp.we_media.windows.Main.lambda$initWindowsMenu$5(Main.java:355)
//	at javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)
//	at javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348)
//	at javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402)
//	at javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259)
//	at javax.swing.AbstractButton.doClick(AbstractButton.java:376)
//	at javax.swing.plaf.basic.BasicMenuItemUI.doClick(BasicMenuItemUI.java:833)
//	at javax.swing.plaf.basic.BasicMenuItemUI$Handler.mouseReleased(BasicMenuItemUI.java:877)
//	at java.awt.Component.processMouseEvent(Component.java:6533)
//	at javax.swing.JComponent.processMouseEvent(JComponent.java:3324)
//	at java.awt.Component.processEvent(Component.java:6298)
//	at java.awt.Container.processEvent(Container.java:2236)
//	at java.awt.Component.dispatchEventImpl(Component.java:4889)
//	at java.awt.Container.dispatchEventImpl(Container.java:2294)
//	at java.awt.Component.dispatchEvent(Component.java:4711)
//	at java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4888)
//	at java.awt.LightweightDispatcher.processMouseEvent(Container.java:4525)
//	at java.awt.LightweightDispatcher.dispatchEvent(Container.java:4466)
//	at java.awt.Container.dispatchEventImpl(Container.java:2280)
//	at java.awt.Window.dispatchEventImpl(Window.java:2746)
//	at java.awt.Component.dispatchEvent(Component.java:4711)
//	at java.awt.EventQueue.dispatchEventImpl(EventQueue.java:758)
//	at java.awt.EventQueue.access$500(EventQueue.java:97)
//	at java.awt.EventQueue$3.run(EventQueue.java:709)
//	at java.awt.EventQueue$3.run(EventQueue.java:703)
//	at java.security.AccessController.doPrivileged(Native Method)
//	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:76)
//	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:86)
//	at java.awt.EventQueue$4.run(EventQueue.java:731)
//	at java.awt.EventQueue$4.run(EventQueue.java:729)
//	at java.security.AccessController.doPrivileged(Native Method)
//	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:76)
//	at java.awt.EventQueue.dispatchEvent(EventQueue.java:728)
//	at java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:201)
//	at java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:116)
//	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:105)
//	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
//	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:93)
//	at java.awt.EventDispatchThread.run(EventDispatchThread.java:82)

public class FastCommand {
	private FastCommand() {
		this.checkEcho();
	}






	void writeCommand(String command) throws IOException {
		BufferedOutputStream out = (BufferedOutputStream) this.getOutputStream();
		out.write(command.getBytes(charset));
	}
	void writeLine() throws IOException {
		BufferedOutputStream out = (BufferedOutputStream) this.getOutputStream();
		out.write(System.lineSeparator().getBytes(charset));
	}
	void writeFlash() throws IOException {
		BufferedOutputStream out = (BufferedOutputStream) this.getOutputStream();
		out.flush();
	}



	public enum OperatingSystem {
		Windows,
		Linux,
		Mac;

		public static OperatingSystem getOperatingSystem() {
			String value = Objects.requireNonNull(System.getProperty("os.name"), "os.name").toUpperCase().toLowerCase();
			if (value.contains("Windows".toLowerCase())) { return Windows; }
			if (value.contains("Linux".toLowerCase())) { return Linux; }
			if (value.contains("Mac".toLowerCase())&&
				value.contains("Os".toLowerCase())) { return Mac; }
			throw new RuntimeException("cannot parse: "+value);
		}
	}



	/**
	 * Windows system "echo" command comes with cmd
	 */


	public static String file_name(String name, String extensionName) {
		return name + (null == extensionName ?"": "." + extensionName);
	}

	public static String findCommand(String command) {
		OperatingSystem system = OperatingSystem.getOperatingSystem();
		if (system == OperatingSystem.Windows) {
			String path = ProcessExecutor.envPATHCanExecuteCommandFilePathTry(file_name(command, "exe"));
			if (null != path) {
				return path;
			}
		} else if (system == OperatingSystem.Linux || system == OperatingSystem.Mac) {
			String path = ProcessExecutor.envPATHCanExecuteCommandFilePathTry(command);
			if (null != path) {
				return path;
			}
		}
		throw new RuntimeException("cannot found command: " + command);
	}


	public static FastCommand openProcess() { return openProcess(null); }
	public static FastCommand openProcess(File directory) {
		ProcessExecutor pe = new ProcessExecutor();
		pe.directory(directory);
		OperatingSystem system = OperatingSystem.getOperatingSystem();
		if (system == OperatingSystem.Windows) {
			pe.command("cmd");
		} else if (system == OperatingSystem.Linux || system == OperatingSystem.Mac) {
			pe.command("sh");
		}
		Charset charset = pe.charset();

		try {
			Process process = pe.exec().process();

			FastCommand fc = new FastCommand();
			fc.charset = charset;
			fc.process = process;
			fc.directory = directory;

			return fc;
		} catch (Throwable e) { throw new RuntimeException(e); }
	}

	Charset charset;
	Process process;
	File directory;


	public File directory() { return this.directory; }
	public Charset charset() { return this.charset; }
	public Process process() { return this.process; }


	public boolean isOpen() { return !(null == process); }



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
	public boolean close() {
		try {
			this.process.destroyForcibly();
			return true;
		} catch (Throwable throwable) {
			return false;
		}
	}


	final ByteBufferFilter READ_LINE_FILTER = BufferFilter.lineFilterBytes();
	InputStream inputStream;
	InputStream errorStream;
	InputStream getInputStream() {
		return null == this.inputStream ?this.inputStream = this.process.getInputStream(): this.inputStream;
	}
	InputStream getErrorStream() {
		return null == this.errorStream ?this.errorStream = this.process.getErrorStream(): this.errorStream;
	}
	OutputStream outputStream;
	OutputStream getOutputStream() {
		return null == this.outputStream ?this.outputStream = this.process.getOutputStream(): this.outputStream;
	}


	ByteBufferInputStream<InputStream> bufferInputStream;
	ByteBufferInputStream<InputStream> getBufferInputStream() { return null == this.bufferInputStream ?this.bufferInputStream = new ByteBufferInputStream(this.getInputStream()): this.bufferInputStream; }
	int getInputStreamAvailable() { try { return this.getBufferInputStream().available(); } catch (IOException e) { return 0; } }

	byte[] readInputLine() throws IOException {
		ByteBufferInputStream bufferInputStream = this.getBufferInputStream();
		byte[] bytes = bufferInputStream.readLine();
		return bytes;
	}
	String readInputLineString() throws IOException {
		byte[] bytes = this.readInputLine();
		return toString(bytes);
	}
	byte[] readInputAll() throws IOException {
		final ByteBufferInputStream<InputStream> b = this.getBufferInputStream();
		ByteBufferOperate byteArrayBuffer = new ByteBufferOperate() {
			@Override
			public int stream_read(byte[] buf, int off, int len) throws IOException {
				return b.read(buf, off, len);
			}
		};
		boolean read = false;
		while (true) {
			if (byteArrayBuffer.append_from_stream_read(8192) == -1) {
				break;
			} else {
				read = true;
			}
		}
		byte[] bytes = read ?byteArrayBuffer.toArray(): null; byteArrayBuffer.remove();
		return bytes;
	}
	String readInputAllString() throws IOException {
		byte[] bytes = this.readInputAll();
		return toString(bytes);
	}


	ByteBufferInputStream<InputStream> bufferErrorStream;
	ByteBufferInputStream<InputStream> getBufferErrorStream() {return null == this.bufferErrorStream ?this.bufferErrorStream = new ByteBufferInputStream<InputStream>(this.getErrorStream()): this.bufferErrorStream; }
	int getErrorStreamAvailable() { try { return this.getBufferErrorStream().available(); } catch (IOException e) { return 0; }}


	byte[] readErrorLine() throws IOException {
		ByteBufferInputStream<InputStream> bufferInputStream = this.getBufferErrorStream();
		byte[] bytes = bufferInputStream.readLine();
		return bytes;
	}
	String readErrorLineString() throws IOException {
		byte[] bytes = this.readErrorLine();
		return toString(bytes);
	}
	byte[] readErrorAll() throws IOException {
		final ByteBufferInputStream<InputStream> b = this.getBufferErrorStream();
		ByteBufferOperate byteArrayBuffer = new ByteBufferOperate() {
			@Override
			public int stream_read(byte[] buf, int off, int len) throws IOException {
				return b.read(buf, off, len);
			}
		};
		boolean read = false;
		while (true) {
			if (byteArrayBuffer.append_from_stream_read(8192) == -1) {
				break;
			} else {
				read = true;
			}
		}
		byte[] bytes = read ?byteArrayBuffer.toArray(): null; byteArrayBuffer.remove();
		return bytes;
	}
	String readErrorAllString() throws IOException {
		byte[] bytes = this.readErrorAll();
		return toString(bytes);
	}



	public class ByteBufferInputStream<T extends InputStream> extends InputStream implements IInnerStream<T> {
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

	BufferedOutputStream bufferedOutputStream;
	BufferedOutputStream getBufferedOutputStream() {
		return null == this.bufferedOutputStream ?this.bufferedOutputStream = new BufferedOutputStream(this.getOutputStream()): this.bufferedOutputStream;
	}










	transient final Object executeLock = new Object();

	String session_start =
			Base64Encoder.getEncoder()
					.encodeToString((System.currentTimeMillis() + ":" + System.nanoTime() + ":" + Strings.random("abcdefghijklnmopqrstuvwxyz0123456789", 12)).getBytes()).toLowerCase();
	String session_end =
			Base64Encoder.getEncoder()
					.encodeToString((System.currentTimeMillis() + ":" + System.nanoTime() + ":" + Strings.random("abcdefghijklnmopqrstuvwxyz0123456789", 12)).getBytes()).toLowerCase();



	static String commandJoin(String... commands) {
		Objects.requireNonNull(commands, "command");

		String separator = "\u0020";
		StringBuilder buffer = new StringBuilder();
		for (String command: commands) {
			buffer.append(command).append(separator);
		}
		buffer.setLength(buffer.length() > separator.length() ? buffer.length() - separator.length(): buffer.length());
		return buffer.toString();
	}

	static void checkEcho() {
		OperatingSystem system = OperatingSystem.getOperatingSystem();
		if (system == OperatingSystem.Windows) {
		} else if (system == OperatingSystem.Linux || system == OperatingSystem.Mac) {
			findCommand("echo");
		} 
	}


	String wrapEchoSession(String command) {
		Objects.requireNonNull(command, "command");

		String commandSeparator;
		OperatingSystem system = OperatingSystem.getOperatingSystem();
		if (system == OperatingSystem.Windows) {
			commandSeparator = "&";
		} else if (system == OperatingSystem.Linux || system == OperatingSystem.Mac) {
			commandSeparator = ";";
		} else {
			throw new UnsupportedOperationException(command);
		}
		return commandJoin("echo", session_start + commandSeparator,  command + commandSeparator, "echo", session_end);
	}


	
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
						if (line.contains(session_end)) { break; }
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
				if (!Objects.empty(error)) {
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

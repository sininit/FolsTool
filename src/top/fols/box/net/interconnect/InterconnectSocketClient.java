package top.fols.box.net.interconnect;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import top.fols.atri.interfaces.abstracts.BitsOptions;
import top.fols.atri.interfaces.interfaces.ICallbackOneParam;
import top.fols.atri.interfaces.interfaces.ICaller;
import top.fols.atri.io.util.Streams;
import top.fols.atri.util.bits.BitsBigEndian;
import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.box.util.encode.ByteEncoders;

@Deprecated
@SuppressWarnings("SameParameterValue")
public class InterconnectSocketClient implements Closeable {

	static public final Charset STRING_CHARSET = Finals.Charsets.UTF_8;

	static public byte[] stringAsBytes(String s) {
		if (null == s || s.length() == 0)
			return new byte[]{};
		return ByteEncoders.charsToBytes(s, 0, s.length(), STRING_CHARSET);
	}
	static public String bytesAsString(byte[] s) {
		if (null == s || s.length == 0)
			return "";
		return new String(ByteEncoders.bytesToChars(s, 0, s.length, STRING_CHARSET));
	}

	static byte[] readBytes(InputStream stream, int len) throws IOException {
		byte[] bytes = new byte[len];
		int read = read(stream, bytes, 0, len);
		if (read != len)
			throw new IOException("read data error, read=" + read + ", need=" + len);
		return bytes;
	}
	static int read(InputStream stream, byte[] buff, int off, int max) throws IOException {
		int remaining = max;
		do {
			int read = stream.read(buff, off, remaining);
			if (read <= -1) {
				if (stream.available() <= 0)
					break;
			} else {
				off += read;
				remaining -= read;
			}
		} while (remaining > 0);
		return max - remaining;
	}






	Socket clientToServer;
	Socket serverToClient;
	String id = createID();

	Recipient recipient;

	final Object cslock  = new Object();
	final Object sclock  = new Object();


	final Object statuslock = new Object();

	Map<String, ICaller<TakeoverData<InterconnectSocketClient>, byte[]>> typeReceiver = new ConcurrentHashMap<>();

//	int heartbeat_retries = 3;



	static String createID() {
		UUID uuid = UUID.randomUUID();
		return 
		    System.currentTimeMillis() 
			+ "_" 
			+ System.nanoTime()
		    + "_"
			+ uuid.hashCode()
			+ "_"
			+ uuid;
	}

	InputStream clientToServerInputStream;
	InputStream getClientToServerInputStream() {
		try {
			return null == clientToServer ? null: (null == clientToServerInputStream ?clientToServerInputStream = new BufferedInputStream(clientToServer.getInputStream()): clientToServerInputStream);
		} catch (IOException e) {return null;}
	}
	OutputStream clientToServerOutputStream;
	OutputStream getClientToServerOutputStream() {
		try {
			return null == clientToServer ? null: (null == clientToServerOutputStream ?clientToServerOutputStream = new BufferedOutputStream(clientToServer.getOutputStream()): clientToServerOutputStream);
		} catch (IOException e) {return null;}
	}


	InputStream serverToClientInputStream;
	InputStream getServerToClientInputStream() {
		try {
			return null == serverToClient ? null: (null == serverToClientInputStream ?serverToClientInputStream = new BufferedInputStream(serverToClient.getInputStream()): serverToClientInputStream);
		} catch (IOException e) {return null;}
	}
	OutputStream serverToClientOutputStream;
	OutputStream getServerToClientOutputStream() {
		try {
			return null == serverToClient ? null: (null == serverToClientOutputStream ?serverToClientOutputStream = new BufferedOutputStream(serverToClient.getOutputStream()): serverToClientOutputStream);
		} catch (IOException e) {return null;}
	}


	ICallbackOneParam<InterconnectSocketClient> onConnect;
	ICallbackOneParam<InterconnectSocketClient> onClose;




	public String getID() {
		return id;
	}




	@SuppressWarnings("RedundantIfStatement")
	public boolean isConnect() {
		synchronized (statuslock) {
			if (null != clientToServer &&
				null != serverToClient) {
				if (!clientToServer.isClosed() && 
					!serverToClient.isClosed()) {
					if (clientToServer.isConnected() &&
						serverToClient.isConnected()) {
						return true;
					}
				}
			}
			return false;
		}
	}


	public void close() {
		synchronized (statuslock) {
			Streams.close(clientToServer);
			Streams.close(getClientToServerInputStream());
			Streams.close(getClientToServerOutputStream());

			Streams.close(serverToClient);
			Streams.close(getServerToClientInputStream());
			Streams.close(getServerToClientOutputStream());

			if (null != recipient) {
				recipient.interrupt();
				recipient.serverToClient = null;
				recipient = null;
			}

			clientToServer = null;
			clientToServerInputStream  = null;
			clientToServerOutputStream = null;

			serverToClient = null;
			serverToClientInputStream  = null;
			serverToClientOutputStream = null;

			this.onClose();
		}
	}

	/**
	 * isClose
	 */
	public boolean isClose(){
		synchronized (statuslock) {
			return  null == clientToServer ||
					null == serverToClient;
		}
	}


//	static final int SO_TIMEOUT = 6000;
	static final int SO_TIMEOUT_UNLIMITED = 0;

	public static final String TYPE_CONNECT = "__connect";
	public static final byte[] TYPE_CONNECT_CS = {0x1};
	public static final byte[] TYPE_CONNECT_SC = {0x0};
	public static final byte[] TYPE_CONNECT_RESULT_TRUE  = BitsOptions.BIG_ENDIAN.getBytes(200);
	public static final byte[] TYPE_CONNECT_RESULT_FALSE = BitsOptions.BIG_ENDIAN.getBytes(404);

//	public static final String TYPE_HEARTBEAT = "__heartbeat";
//	public static final byte[] TYPE_HEARTBEAT_RESULT_TRUE  = BitsOptions.BIG_ENDIAN.getBytes(200);

	public InterconnectSocketClient connect(InetAddress ip, int port) {
		synchronized (statuslock) {
			if (isConnect()) {
				return this;
			}

			try {
				clientToServer = new Socket(ip, port);
				clientToServer.setSoTimeout(SO_TIMEOUT_UNLIMITED);
				byte[] cs = SendClientToServerAndReadResult(TYPE_CONNECT, TYPE_CONNECT_CS);

				serverToClient = new Socket(ip, port);
				serverToClient.setSoTimeout(SO_TIMEOUT_UNLIMITED);
				byte[] sc = SendServerToClientAndReadResult(TYPE_CONNECT, TYPE_CONNECT_SC);

				if (Arrays.equals(cs, TYPE_CONNECT_RESULT_TRUE)  && 
					Arrays.equals(sc, TYPE_CONNECT_RESULT_TRUE)) {

					recipient = new Recipient(serverToClient);
					recipient.start();

					this.onConnect();

					return this;
				} else {
					throw new IOException("link fail: " + Arrays.toString(cs) + " " + Arrays.toString(sc));
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} 
		}
	}


	byte[] SendClientToServerAndReadResult(String type, byte[] data) throws IOException {
		synchronized (cslock) {
			return SendAndReadResult(getClientToServerOutputStream(),
									 getClientToServerInputStream(),
									 id, type, data);
		}
	}


	byte[] SendServerToClientAndReadResult(String type, byte[] data) throws IOException {
		synchronized (sclock) {
			return SendAndReadResult(getServerToClientOutputStream(),
									 getServerToClientInputStream(),
									 id, type, data);
		}
	}

	void TakeoverServerToClientAndSendResult(ICaller<TakeoverData<InterconnectSocketClient>, byte[]> calling) throws IOException {
		synchronized (sclock) {
			TakeoverAndReturn(getServerToClientInputStream(),
							  getServerToClientOutputStream(),
							  this,
							  calling);
		}
	}



	static protected byte[] SendAndReadResult(OutputStream out, InputStream inp, 
											  String id, String type, @Nullable byte[] data) throws IOException {
		byte[] cache;

		out.write(BitsOptions.BIG_ENDIAN.getBytes((cache = stringAsBytes(id)).length));
		out.write(cache);

		out.write(BitsOptions.BIG_ENDIAN.getBytes((cache = stringAsBytes(type)).length));
		out.write(cache);

		if (null == data) {
			cache = Finals.EMPTY_BYTE_ARRAY;
			out.write(BitsOptions.BIG_ENDIAN.getBytes(-1));

		} else {
			cache = data;
			out.write(BitsOptions.BIG_ENDIAN.getBytes(cache.length));
		}
		out.write(cache);
		out.flush();


		int len = BitsOptions.BIG_ENDIAN.getInt(readBytes(inp, BitsBigEndian.INT_BYTE_LENGTH), 0);
		if (len < 0) {
			return null;
		} else {
			return readBytes(inp, len);
		}
	}


	static public class TakeoverData<T> {
		public final T subject;

		String id;
		String type;
		byte[] data;

		public TakeoverData(T subject,
							String id, String type, byte[] data) {
			this.subject = subject;

			this.id = id;
			this.type = type;
			this.data = data;
		}


		public String getId() {
			return id;
		}
		public String getType() {
			return type;
		}
		public byte[] getData() {
			return data;
		}
	}
	static protected <T> void TakeoverAndReturn(InputStream inp, OutputStream out,
											T subject,
											ICaller<TakeoverData<T>, byte[]> calling) throws IOException {
		int idlen = BitsOptions.BIG_ENDIAN.getInt(readBytes(inp, BitsBigEndian.INT_BYTE_LENGTH), 0);
		String id = bytesAsString(readBytes(inp, idlen));

		int typelen = BitsOptions.BIG_ENDIAN.getInt(readBytes(inp, BitsBigEndian.INT_BYTE_LENGTH), 0);
		String type = bytesAsString(readBytes(inp, typelen));

		int datalen = BitsOptions.BIG_ENDIAN.getInt(readBytes(inp, BitsBigEndian.INT_BYTE_LENGTH), 0);
		byte[] data;
		if (datalen < 0) {
			data = null;
		} else {
			data = readBytes(inp, datalen);
		}

		TakeoverData<T> taskover = new TakeoverData<T>(subject, id, type, data);
		byte[] deal;
		try {
			deal = calling.next(taskover);
		} catch (Throwable ignored) {
			deal = null;
		}


		byte[] cache;
		if (null == deal) {
			cache = Finals.EMPTY_BYTE_ARRAY;
			out.write(BitsOptions.BIG_ENDIAN.getBytes(-1));
		} else {
			cache = deal;
			out.write(BitsOptions.BIG_ENDIAN.getBytes(cache.length));
		}
		out.write(cache);
		out.flush();
	}









	class Recipient extends Thread {
		public void remove() {
			Streams.close(serverToClient);
		}

		volatile Socket serverToClient;
		public Recipient(Socket serverToClient) {
			this.serverToClient = serverToClient;
		}

		@Override
		public void run() {
			// TODO: Implement this method
			try {
				while (null != serverToClient) {
					try {
						TakeoverServerToClientAndSendResult(new ICaller<TakeoverData<InterconnectSocketClient>, byte[]>() {
							@Override
							public byte[] next(TakeoverData<InterconnectSocketClient> p1) {
								// TODO: Implement this method
								String type = p1.getType();
//									if (TYPE_HEARTBEAT.equals(type)) {
//										return TYPE_HEARTBEAT_RESULT_TRUE;
//									}
								ICaller<TakeoverData<InterconnectSocketClient>, byte[]> call = getTypeReceiver(p1.getType());
								if (null == call) {
									return null;
								} else {
									return call.next(p1);
								}
							}
						});
					} catch (SocketTimeoutException ignored) {
					} catch (IOException e) {
						if (!isConnect()) {
							break;
						}
//						if (!heartbeat()) {
//							break;
//						}
						if (isConnect()) {
							e.printStackTrace();
						}
						break;
					}
				}
			} finally {
				InterconnectSocketClient.this.close();
			}
		}
	}

//	boolean heartbeat() {
//		for (int i = 0; i < heartbeat_retries; i++) {
//			try {
//				byte[] send = send(TYPE_HEARTBEAT, null);
//				if (Arrays.equals(TYPE_HEARTBEAT_RESULT_TRUE, send)) {
//					return true;
//				}
//			} catch (Throwable ignored) {}
//		}
//		return false;
//	}
//




	public byte[] send(String type, byte[] data) {
		try {
			if (isConnect()) {
				return SendClientToServerAndReadResult(type, data);
			} else {
				throw new IOException("no connect server: " + id);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	public void onTypeReceiver(String type, ICaller<TakeoverData<InterconnectSocketClient>, byte[]> call) {
		typeReceiver.put(type, call);
	}
	protected ICaller<TakeoverData<InterconnectSocketClient>, byte[]> getTypeReceiver(String type) {
		return typeReceiver.get(type);
	}



	public void onConnect(ICallbackOneParam<InterconnectSocketClient> callback) {
		this.onConnect = callback;
	}
	void onConnect() {
		if (null != onConnect) {
			try {
				onConnect.callback(this);
			} catch (Throwable ignored) {}
		}
	}

	public void onClose(ICallbackOneParam<InterconnectSocketClient> callback) {
		this.onClose = callback;
	}
	void onClose() {
		if (null != onClose) {
			try {
				onClose.callback(this);
			} catch (Throwable ignored) {}
		}
	}
}

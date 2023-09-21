package top.fols.box.net.interconnect;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import top.fols.atri.interfaces.interfaces.ICallbackOneParam;
import top.fols.atri.interfaces.interfaces.ICaller;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.box.thread.FixedThreadPool;
import top.fols.atri.time.Times;

import static top.fols.box.net.interconnect.InterconnectSocketClient.*;

@Deprecated
public class InterconnectSocketServer implements Closeable {


	public static final int PORT_DEFAULT_RANDOM = 0;


	InetAddress address;
	Integer localport;
    final Object statuslock = new Object();

    SocketServerManager manager;

	long clientConnectOvertime = Times.TIME_1_M * 3;

	Map<String, ICaller<TakeoverData<InterconnectSocketServer>, byte[]>> typeReceiver = new ConcurrentHashMap<>();

	int startPort = PORT_DEFAULT_RANDOM;

//	int heartbeat_retries = 3;


	ICallbackOneParam<InterconnectSocketServer> onStart;
	ICallbackOneParam<InterconnectSocketServer> onClose;

	ICallbackOneParam<Client> onClientConnect;
	ICallbackOneParam<Client> onClientClose;



	Map<String, Client> waitClients    = new ConcurrentHashMap<>();
	Map<String, Client> connectClients = new ConcurrentHashMap<>();




	public InetAddress getAddress() {
		synchronized (statuslock) {
            if (null != address) {
				return  address;
			} else {
				throw new RuntimeException("server no run");
			}
        }
	}
    public Integer getPort() {
        synchronized (statuslock) {
            if (null != localport) {
				return  localport;
			} else {
				throw new RuntimeException("server no run");
			}
        }
    }
	public void setBootPort(int startPort0) {
		synchronized (statuslock) {
            if (null == localport) {
				startPort = startPort0;
			} else {
				throw new RuntimeException("server in run");
			}
        }
	}
	public Integer getBootPort() {
		return startPort;
	}

    public boolean isRunning() {
		synchronized (statuslock) {
			return  null != localport;
		}
    }

    public void start() throws IOException {
        synchronized (statuslock) {
            if (null != localport) {
                return;
			}

            manager = new SocketServerManager(startPort);

            localport = manager.getLocalPort();
			address = manager.getLocalAddress();

			this.onStart();
        }
    }
    public void close() {
        synchronized (statuslock) {
            Streams.close(manager);

			address = null;
			localport = null;

			for (Client client: waitClients.values()) {
				client.close();
			}
			for (Client client: connectClients.values()) {
				client.close();
			}

			this.onClose();
        }
    }




	void autoCloseWaitClient() {
		synchronized (statuslock) {
			for (Client client: waitClients.values()) {
				if (client.overtime() > clientConnectOvertime) {
					client.close();
				}
			}
		}
	}






	public class Client implements Closeable {
		public Client(String id) {
			this.id = id;
		}

		@Override
		public void close() {
			// TODO: Implement this method
			Streams.close(clientToServer);
			Streams.close(serverToClient);

			clientToServer = null;
			serverToClient = null;

			synchronized (statuslock) {
				waitClients.remove(id);
				connectClients.remove(id);
			}

			onClientClose(this);

			cache.clear();
		}

		public final Map<String, Object> cache = new HashMap<>();

		final Object sclock  = new Object();
		final Object cslock  = new Object();


		long firstTime = System.currentTimeMillis();
		String id;
		volatile Socket clientToServer;
		volatile Socket serverToClient;


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



		public boolean isConnect() {
			return null != clientToServer &&
				null != serverToClient ;
		}

		//距离第一次连接过去的时间
		public long overtime() {
			if (isConnect()) return -1;
			return System.currentTimeMillis() - firstTime;
		}


		byte[] SendServerToClientAndReadResult(String type, byte[] data) throws IOException {
			synchronized (sclock) {
				return SendAndReadResult(getServerToClientOutputStream(),
										 getServerToClientInputStream(),
										 id, type, data);
			}
		}

		void TakeoverClientToServerAndSendResult(ICaller<TakeoverData<InterconnectSocketServer>, byte[]> calling) throws IOException {
			synchronized (cslock) {
				TakeoverAndReturn(getClientToServerInputStream(),
								  getClientToServerOutputStream(),
						InterconnectSocketServer.this,
								  calling);
			}
		}

		public String getID() {
			return id;
		}



//		boolean heartbeat() {
//			for (int i = 0;i < heartbeat_retries;i++) {
//				try {
//					if (Arrays.equals(TYPE_HEARTBEAT_RESULT_TRUE, send(getID(), TYPE_HEARTBEAT, null))) {
//						return true;
//					}
//				} catch (Throwable ignored) {}
//			}
//			return false;
//		}



		public Object getCache(String key) {
			return cache.get(key);
		}
		public void setCache(String key, Object value) {
			cache.put(key, value);
		}
		public void removeCache(String key) {
			cache.remove(key);
		}
	}



    //only up or down
    class SocketServerManager implements Closeable {
		final ClientAcceptThread socketAcceptThread;
        final ServerSocket serverSocket;
		final FixedThreadPool pool;


		final Object lock = new Object();


        public SocketServerManager(int port) throws IOException {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(SO_TIMEOUT_UNLIMITED); //unlimited
            serverSocket.setReuseAddress(true);

            socketAcceptThread = new ClientAcceptThread();
            socketAcceptThread.start();

			pool = new FixedThreadPool().setMaxRunningCount(32);
        }

        public int getLocalPort() {
            return serverSocket.getLocalPort();
        }
		public InetAddress getLocalAddress() {
            return serverSocket.getInetAddress();
        }


        @Override
        public void close() {
			Streams.close(serverSocket);
            socketAcceptThread.interrupt();
            pool.removeAllAndWaitEnd();
        }


        class ClientAcceptThread extends Thread {
            @Override
            public void run() {
                try {
                    while (isRunning()) {
                        try {
							synchronized (lock) {
								final Socket socket = serverSocket.accept();
								final InputStream inputStream   = socket.getInputStream();
								final OutputStream outputStream = socket.getOutputStream();

								InterconnectSocketClient.TakeoverAndReturn(inputStream, outputStream, InterconnectSocketServer.this, new ICaller<TakeoverData<InterconnectSocketServer>, byte[]>(){
										@Override
										public byte[] next(TakeoverData<InterconnectSocketServer> p1) {
											// TODO: Implement this method
											//System.out.println(p1);

											String id = p1.getId();
											String type = p1.getType();
											byte[] data = p1.getData();

											if (TYPE_CONNECT.equals(type)) {
												Client client = waitClients.get(id);
												if (null == client) {
													waitClients.put(id, client = new Client(id));
												}

												if (client.isConnect()) {
													return TYPE_CONNECT_RESULT_FALSE;
												} else {
													if (Arrays.equals(data, TYPE_CONNECT_CS)) {
														client.clientToServer = socket;
													} else if (Arrays.equals(data, TYPE_CONNECT_SC)) {
														client.serverToClient = socket;
													} else {
														return TYPE_CONNECT_RESULT_FALSE;
													}
												}

												if (client.isConnect()) {
													ClientThread runInterface = new ClientThread(client);
													pool.post(runInterface);

													waitClients.remove(id);
													connectClients.put(id, client);
												}

												return TYPE_CONNECT_RESULT_TRUE;
											} else {
												return TYPE_CONNECT_RESULT_FALSE;
											}
										}
									});
							}
                        } catch (SocketTimeoutException ignored) {
							try {
								autoCloseWaitClient(); //自动清空等待中的客户
							} catch (Throwable ignored1) {}
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } finally {
                    InterconnectSocketServer.this.close();
                }
            }
        }

		//因为每个用户对应一个线程
		class ClientThread extends FixedThreadPool.Run {
			@Override
            public void remove() {
                super.remove();

				Streams.close(client);
            }


            Client client;
            public ClientThread(Client client) {
                this.client = client;
            }


            @Override
            public void run() {
				onClientConnect(client);

                try {
                    while  (client.isConnect() && isRunning()) {
						try {
							client.TakeoverClientToServerAndSendResult(new ICaller<TakeoverData<InterconnectSocketServer>, byte[]>() {
									@Override
									public byte[] next(TakeoverData<InterconnectSocketServer> p1) {
										// TODO: Implement this method
										String type = p1.getType();
//										if (TYPE_HEARTBEAT.equals(type)) {
//											return TYPE_HEARTBEAT_RESULT_TRUE;
//										}
										ICaller<TakeoverData<InterconnectSocketServer>, byte[]> call = getTypeReceiver(p1.getType());
										if (null == call) {
											return null;
										} else {
											return call.next(p1);
										}
									}
								});
						} catch (SocketTimeoutException ignored) {
						} catch (IOException e) {
							if (!client.isConnect()) {
								break;
							}
//							if (!client.heartbeat()) {
//								break;
//							}
							if (InterconnectSocketServer.this.isRunning()) {
								e.printStackTrace();
							}
							break;
						}
					}
                } finally {
					remove();
				}
            }
        }
    }





	public boolean isConnect(String id) {
		Client client = getClient(id);
		if (null == client) {
			return false;
		}
		return client.isConnect();
	}

	
	public void closeClient(String id) {
		Client client = getClient(id);
		if (null == client) {
			throw new RuntimeException("no client connect: " + id);
		}
		client.close();
	}
	
	
	public Client getClient(TakeoverData<?> id) {
		return getClient(id.getId());
	}
	public Client getClient(String id) {
		return null == id ?null: connectClients.get(id);
	}
	public String[] getClients() {
		return connectClients.keySet().toArray(Finals.EMPTY_STRING_ARRAY);
	}


	public byte[] send(String id, String type, byte[] data) {
		Client client = getClient(id);
		if (null == client || !client.isConnect()) {
			throw new RuntimeException("no client connect: " + id);
		}
		try {
			return client.SendServerToClientAndReadResult(type, data);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	public void send(String type, byte[] data) {
		for (String id: getClients()) {
			try {
				send(id, type, data);
			} catch (Throwable ignored) {}
		}
	}

	public void onTypeReceiver(String type, ICaller<TakeoverData<InterconnectSocketServer>, byte[]> call) {
		typeReceiver.put(type, call);
	}
	protected ICaller<TakeoverData<InterconnectSocketServer>, byte[]> getTypeReceiver(String type) {
		return typeReceiver.get(type);
	}

	public void onStart(ICallbackOneParam<InterconnectSocketServer> callback) {
		this.onStart = callback;
	}
	void onStart() {
		if (null != onStart) {
			try {
				onStart.callback(this);
			} catch (Throwable ignored) {}
		}
	}

	public void onClose(ICallbackOneParam<InterconnectSocketServer> callback) {
		this.onClose = callback;
	}
	void onClose() {
		if (null != onClose) {
			try {
				onClose.callback(this);
			} catch (Throwable ignored) {}
		}
	}

	public void onClientConnect(ICallbackOneParam<Client> callback) {
		this.onClientConnect = callback;
	}
	void onClientConnect(Client client) {
		if (null != onClientConnect) {
			try {
				onClientConnect.callback(client);
			} catch (Throwable ignored) {}
		}
	}

	public void onClientClose(ICallbackOneParam<Client> callback) {
		this.onClientClose = callback;
	}
	void onClientClose(Client client) {
		if (null != onClientClose) {
			try {
				onClientClose.callback(client);
			} catch (Throwable ignored) {}
		}
	}

}

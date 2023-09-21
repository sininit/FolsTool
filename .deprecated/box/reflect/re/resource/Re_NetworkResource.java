package top.fols.box.reflect.re.resource;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import top.fols.atri.io.util.Streams;
import top.fols.atri.io.file.Filez;
import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Objects;
import top.fols.atri.net.URLBuilder;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.box.net.interconnect.InterconnectSocketClient;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver;
import top.fols.box.net.interconnect.InterconnectSocketServer;
import top.fols.box.reflect.re.Re_ClassLoader;

import static top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver.*;

@SuppressWarnings("rawtypes")
public class Re_NetworkResource implements Re_IReResource, Closeable {
	static class AInterconnectSocketSerializeReceiver extends InterconnectSocketSerializeReceiver {
		Re_NetworkResource nr;
		public AInterconnectSocketSerializeReceiver(Re_NetworkResource nr) {
			this.nr = nr;
		}
		@Override
		public byte[] _sendOriginalData(String receiver,
                                        String type, byte[] data) {
			// TODO: Implement this method
            return nr.client.send(type, data);
		}
		public <DATA extends Serializable, RET extends Serializable> RET send(AServerReply<DATA, RET> data) throws InterconnectSocketSerializeReceiver.RemoteThrowException, ClassNotFoundException, IOException, RemoteThrowException {
			// TODO: Implement this method
			return super.send(null, data);
		}
	}
	AInterconnectSocketSerializeReceiver receiver = new AInterconnectSocketSerializeReceiver(this) {{
			Class<?>[] classes = Re_NetworkResource.class.getClasses();
			for (Class c: classes) {
				if (Classz.isInstanceNullable(c, AServerReply.class)) {
					try {
						//noinspection unchecked
						addServerReply((AServerReply) c.getDeclaredConstructor().newInstance());
					} catch (Throwable e) {
						throw new RuntimeException(e);
					}
				}
			}
		}};
	InterconnectSocketClientImpl client = receiver.createClient();

	@Override
	public void close() {
		// TODO: Implement this method
		Streams.close(new Closeable(){
				@Override
				public void close() {
					// TODO: Implement this method
					client.close();
				}
			});
	}

	String simpleName = getClass().getSimpleName();
	public String pathPrefix() {
		return simpleName + "[" + u + "]";
	}

	InetAddress ipaddress;
	int         ipport;
	String      u;;
	public Re_NetworkResource(String ip, int port) throws UnknownHostException {
		this(InetAddress.getByName(ip), port);
	}
	public Re_NetworkResource(InetAddress ip, int port) {
		this.ipaddress  = ip;
		this.ipport     = port;
		this.u          = new URLBuilder()
			.host(ip.getHostName())
			.port(port)
			.build();
	}

	public Re_NetworkResource connect() {
		client.connect(ipaddress, ipport);
		return this;
	}




	Charset queryCharset() {
		try {
			String charsetName = receiver.send(new Server.GetCharset());
			return Charset.forName(charsetName);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	RemoteBytesContent queryFile(String relativePath) {
		try {
			return receiver.send(new Server.FindFile(relativePath));
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
    Long queryFileSize(String relativePath) {
        try {
            return receiver.send(new Server.FindFileSize(relativePath));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    Long queryFileLastModified(String relativePath) {
        try {
            return receiver.send(new Server.FindFileLastModified(relativePath));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
	RemoteBytesContent queryClassFile(String name) {
		try {
			return receiver.send(new Server.FindClassFile(name));
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}



	public InetAddress getInetAddress() { return ipaddress; }
	public int getPort() {return ipport;}

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Re_NetworkResource))
            return false;

        Re_NetworkResource other = (Re_NetworkResource) obj;
        return Objects.equals(other.u, this.u);
    }

	@Override
	public Charset contentCharset() {
		// TODO: Implement this method
		return queryCharset();
	}

	@Override
	public Re_IReResourceFile getFileResource(String relativePath) {
		// TODO: Implement this method
		RemoteBytesContent remote = queryFile(relativePath);
		return null == remote ? null : new TargetFile(remote);
	}
    @Override
    public Long getFileSize(String relativePath) {
        return queryFileSize(relativePath);
    }
    @Override
    public Long getFileLastModified(String relativePath) {
        return queryFileLastModified(relativePath);
    }
    @Override
    public InputStream getFileInputStream(String relativePath) {
        RemoteBytesContent remote = queryFile(relativePath);
        return null == remote ? null : new TargetFile(remote).asStream();
    }

	@Override
	public Re_IReResourceFile findClassResource(String fullName) {
		// TODO: Implement this method
		RemoteBytesContent remote = queryClassFile(fullName);
		return null == remote ? null : new TargetFile(remote);
	}




    public static class RemoteBytesContent implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;

        String name;
        File   absoluteFile;
        String relativePath;
        byte[] content;
        String charset;
        RemoteBytesContent(String name,
                           File absoluteFile, String relativePath,
                           Charset charset) {
            byte[] chars = Filez.wrap(absoluteFile).readBytes();

            this.name = name;
            this.absoluteFile = absoluteFile;
            this.relativePath = relativePath;
            this.content = chars;
            this.charset = charset.name();
        }
        public Charset charset()    { return Charset.forName(charset); }
        public String  name()       { return name; }
        public File    file()       { return absoluteFile; }
        public byte[] content()     { return content; }
    }

    public class TargetFile implements Re_IReResourceFile {
        RemoteBytesContent remoteBytesContent;
        public TargetFile(RemoteBytesContent remoteBytesContent) {
            this.remoteBytesContent = remoteBytesContent;
        }

        @Override
        public String name() {
            // TODO: Implement this method
            return remoteBytesContent.name();
        }

        @Override
        public String absolutePath() {
            // TODO: Implement this method
            return pathPrefix() + remoteBytesContent.file().getPath();
        }
        @Override
        public String path() {
            return remoteBytesContent.relativePath;
        }

        @Override
        public Charset charset() {
            // TODO: Implement this method
            return remoteBytesContent.charset();
        }

        @Override
        public InputStream asStream() {
            // TODO: Implement this method
            return new ByteArrayInputStream(remoteBytesContent.content());
        }

        @Override
        public byte[] asBytes() {
            // TODO: Implement this method
            return remoteBytesContent.content();
        }

        @Override
        public String asString() {
            // TODO: Implement this method
            return new String(asBytes(), charset());
        }
    }





	public static class PrintByte extends AServerReply<PrintByte, Boolean> implements Serializable {
		private static final long serialVersionUID = -3042686055658047285L;
		PrintByte() {}

		byte[] content;
		public PrintByte(byte[] content) {
			this.content = content;
		}



		@Override
		public Boolean callback(InterconnectSocketSerializeReceiver server, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
								PrintByte data) {
			Re_NetworkResource nrs = ((AInterconnectSocketSerializeReceiver) server).nr;
			nrs.receive_print(data.content);
			return null;
		}
	}
	public void send_print(byte[] data, int off, int len) {
		byte[] buf;
		if (off == 0 && len == data.length) {
			buf = data;
		} else {
			buf = new byte[len];
			System.arraycopy(data, off, buf, 0, len);
		}
		try {
			receiver.send(new Server.PrintByte(buf));
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	public void receive_print(byte[] content) {
		try {
			PrintStream out = System.out;
			out.write(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}








    @SuppressWarnings("rawtypes")
    public static class Server implements Closeable {
        static class AInterconnectSocketSerializeReceiver extends InterconnectSocketSerializeReceiver {
            Server nrs;
            public AInterconnectSocketSerializeReceiver(Server nrs) {
                this.nrs = nrs;
            }
            @Override
            public byte[] _sendOriginalData(String receiver,
                                            String type, byte[] data) {
                // TODO: Implement this method
                InterconnectSocketServer server = nrs.server;
                return server.send(receiver, type, data);
            }

            public <DATA extends Serializable, RET extends Serializable> void send(AServerReply<DATA, RET> data) throws IOException, ClassNotFoundException, RemoteThrowException {
                String[] clients = nrs.server.getClients();
                for (String client : clients) {
                    try {
                        super.send(client, data);
                    } catch (Throwable ignored) {}
                }
            }
        }
        AInterconnectSocketSerializeReceiver receiver = new AInterconnectSocketSerializeReceiver(this) {{
                Class<?>[] classes = Re_NetworkResource.Server.class.getClasses();
                for (Class c: classes) {
                    if (Classz.isInstanceNullable(c, AServerReply.class)) {
                        try {
                            //noinspection unchecked
                            addServerReply((AServerReply) c.getDeclaredConstructor().newInstance());
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }};
        InterconnectSocketServer server = receiver.createServer();



        public void close() {
            Streams.close(new Closeable(){
                    @Override
                    public void close() {
                        // TODO: Implement this method
                        server.close();
                    }
                });
        }

        Charset contentCharset;
        String directory;
        public Server(String localDirectory) {
            this(localDirectory, null);
        }
        public Server(String localDirectory, Charset contentCharset) {
            this.directory = localDirectory;
            this.contentCharset = null == contentCharset ?DEFAULT_CONTENT_CHARSET: contentCharset;
        }



        public Server setBootPort(int startPort0) {
            this.server.setBootPort(startPort0);
            return this;
        }
        public Integer getBootPort() {
            return this.server.getBootPort();
        }

        public InetAddress getAddress() {
            return server.getAddress();
        }
        public Integer getPort()        {
            return server.getPort();
        }


        public Server start() {
            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }



        public void contentCharset(Charset charset) {
            this.contentCharset = null == charset ?DEFAULT_CONTENT_CHARSET: charset;
        }
        public Charset contentCharset() {
            return this.contentCharset;
        }

        @Nullable
        public RemoteBytesContent getFileResource(final String relativePath) {
            if (null == this.directory)
                return null;
            final File absolute = new File(this.directory, relativePath);
            if (absolute.exists()) {
                return new RemoteBytesContent(relativePath,
                        absolute, relativePath,
                        contentCharset());
            }
            return null;
        }
        public Long getSize(String relativePath) {
            if (null == this.directory)
                return null;
            final File absolute = new File(this.directory, relativePath);
            if (absolute.exists()) {
                return absolute.length();
            }
            return null;
        }
        public Long getLastModified(String relativePath) {
            if (null == this.directory)
                return null;
            final File absolute = new File(this.directory, relativePath);
            if (absolute.exists()) {
                return absolute.lastModified();
            }
            return null;
        }
        public InputStream getInputStream(String relativePath) {
            if (null == this.directory)
                return null;
            final File absolute = new File(this.directory, relativePath);
            try {
                return new FileInputStream(absolute);
            } catch (Throwable e) {
                return null;
            }
        }

        @Nullable
        public RemoteBytesContent findClassResource(String className) {
            if (null == this.directory)
                return null;
            String relativePath = Re_ClassLoader.classNameToPath(className);
            if (null == relativePath)
                return null;
            final File absolute = new File(this.directory, relativePath);
            if (absolute.exists()) {
                return new RemoteBytesContent(className,
                        absolute, relativePath,
                        contentCharset());
            }
            return null;
        }





        public static class FindFile extends AServerReply<FindFile, RemoteBytesContent> implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;
            FindFile() {}

            String path;
            public FindFile(String path) {
                this.path = path;
            }



            @Override
            public RemoteBytesContent callback(InterconnectSocketSerializeReceiver server, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
                                               FindFile data) {
                Server nrs = ((AInterconnectSocketSerializeReceiver) server).nrs;
                return nrs.getFileResource(data.path);
            }
        }
        public static class FindFileSize extends AServerReply<FindFileSize, Long> implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;
            FindFileSize() {}

            String path;
            public FindFileSize(String path) {
                this.path = path;
            }



            @Override
            public Long callback(InterconnectSocketSerializeReceiver server, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
                                               FindFileSize data) {
                Server nrs = ((AInterconnectSocketSerializeReceiver) server).nrs;
                return nrs.getSize(data.path);
            }
        }
        public static class FindFileLastModified extends AServerReply<FindFileLastModified, Long> implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;
            FindFileLastModified() {}

            String path;
            public FindFileLastModified(String path) {
                this.path = path;
            }


            @Override
            public Long callback(InterconnectSocketSerializeReceiver server, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
                                 FindFileLastModified data) {
                Server nrs = ((AInterconnectSocketSerializeReceiver) server).nrs;
                return nrs.getLastModified(data.path);
            }
        }


        public static class GetCharset extends AServerReply<GetCharset, String> implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;
            GetCharset() {}



            @Override
            public String callback(InterconnectSocketSerializeReceiver server, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
                                   GetCharset data) {
                Server nrs = ((AInterconnectSocketSerializeReceiver) server).nrs;
                return nrs.contentCharset.name();
            }
        }

        public static class FindClassFile extends AServerReply<FindClassFile, RemoteBytesContent> implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;
            FindClassFile() {}

            String className;
            public FindClassFile(String className) {
                this.className = className;
            }



            @Override
            public RemoteBytesContent callback(InterconnectSocketSerializeReceiver server, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
                                               FindClassFile data) {
                Server nrs = ((AInterconnectSocketSerializeReceiver) server).nrs;
                return nrs.findClassResource(data.className);
            }
        }






        public static class PrintByte extends AServerReply<PrintByte, Boolean> implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;
            PrintByte() {}

            byte[] content;
            public PrintByte(byte[] content) {
                this.content = content;
            }



            @Override
            public Boolean callback(InterconnectSocketSerializeReceiver server, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
                                    PrintByte data) {
                Server nrs = ((AInterconnectSocketSerializeReceiver) server).nrs;
                nrs.receive_print(data.content);
                return null;
            }
        }
        public void send_print(byte[] data, int off, int len) {
            byte[] buf;
            if (off == 0 && len == data.length) {
                buf = data;
            } else {
                buf = new byte[len];
                System.arraycopy(data, off, buf, 0, len);
            }
            try {
                receiver.send(new Re_NetworkResource.PrintByte(buf));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        public void receive_print(byte[] content) {
            try {
                PrintStream out = System.out;
                out.write(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

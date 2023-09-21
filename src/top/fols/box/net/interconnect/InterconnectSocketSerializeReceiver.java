package top.fols.box.net.interconnect;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import top.fols.atri.interfaces.interfaces.ICaller;
import top.fols.atri.io.util.Streams;

@Deprecated
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class InterconnectSocketSerializeReceiver {
	static byte[] toSerialBytes(Object o) throws IOException {
        return Streams.ObjectTool.toBytes(o);
    }
    static Object toSerialObject(byte[] o) throws IOException, ClassNotFoundException {
        return Streams.ObjectTool.toObject(o);
    }

	@SuppressWarnings("rawtypes")
    static ICaller<InterconnectSocketClient.TakeoverData, byte[]> NOT_FOUND_TYPE_CALL = new ICaller<InterconnectSocketClient.TakeoverData, byte[]>() {
        @Override
		public byte[] next(InterconnectSocketClient.TakeoverData param) {
			try {
				return toSerialBytes(new RemoteThrowExceptionWrap(new RuntimeException("not found type receiver: " + param.getType())));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	};


	


	public InterconnectSocketClientImpl createClient() {
		return new InterconnectSocketClientImpl(this);
	}
	public static class InterconnectSocketClientImpl extends InterconnectSocketClient {
		InterconnectSocketSerializeReceiver sb;
		public InterconnectSocketClientImpl(InterconnectSocketSerializeReceiver sb) {
			this.sb = sb;
		}


        @Override
        public void onTypeReceiver(String type, final ICaller<TakeoverData<InterconnectSocketClient>, byte[]> call) {
            throw new UnsupportedOperationException("use: " + AServerReply.class);
        }


        ICaller<TakeoverData<InterconnectSocketClient>, byte[]> toCall(final AServerReply serverReply) {
            return new ICaller<TakeoverData<InterconnectSocketClient>, byte[]>() {
                @SuppressWarnings({"unchecked"})
                @Override
                public byte[] next(InterconnectSocketClient.TakeoverData<InterconnectSocketClient> param) {
                    try {
                        Object p = toSerialObject(param.getData());
                        Object r = serverReply.callback(sb, param, p);
                        return toSerialBytes(r);
                    } catch (Throwable e) {
                        try {
                            return toSerialBytes(new RemoteThrowExceptionWrap(e));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            };
        }
		
        @Override
        protected ICaller<TakeoverData<InterconnectSocketClient>, byte[]> getTypeReceiver(final String type) {
            AServerReply   serverReply = sb.findServerReply(type);
            return null == serverReply ? (ICaller) NOT_FOUND_TYPE_CALL: toCall(serverReply);
        }
    }

	public InterconnectSocketServerImpl createServer() {
		return new InterconnectSocketServerImpl(this);
	}
    public static class InterconnectSocketServerImpl extends InterconnectSocketServer {
        InterconnectSocketSerializeReceiver sb;
		public InterconnectSocketServerImpl(InterconnectSocketSerializeReceiver sb) {
			this.sb = sb;
		}

		@Override
        public void onTypeReceiver(String type, final ICaller<InterconnectSocketClient.TakeoverData<InterconnectSocketServer>, byte[]> call) {
            throw new UnsupportedOperationException("use: " + AServerReply.class);
        }


        ICaller<InterconnectSocketClient.TakeoverData<InterconnectSocketServer>, byte[]> toCall(final AServerReply serverReply) {
            return new ICaller<InterconnectSocketClient.TakeoverData<InterconnectSocketServer>, byte[]>() {
                @SuppressWarnings({"unchecked"})
                @Override
                public byte[] next(InterconnectSocketClient.TakeoverData<InterconnectSocketServer> param) {
                    try {
                        Object p = toSerialObject(param.getData());
                        Object r = serverReply.callback(sb, param, p);
                        return toSerialBytes(r);
                    } catch (Throwable e) {
                        try {
                            return toSerialBytes(new RemoteThrowExceptionWrap(e));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            };
        }
		


        @Override
        protected ICaller<InterconnectSocketClient.TakeoverData<InterconnectSocketServer>, byte[]> getTypeReceiver(final String type) {
            AServerReply   serverReply = sb.findServerReply(type);
            return null == serverReply ? (ICaller) NOT_FOUND_TYPE_CALL: toCall(serverReply);
        }
    }


	interface IServerReply<DATA, RET> {
        RET callback(InterconnectSocketSerializeReceiver reply,
                     InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData, DATA dataObject) throws Throwable;
    }

    public static abstract class AServerReply<DATA extends Serializable, RET extends Serializable>implements Serializable, IServerReply<DATA, RET> {
        private static final long serialVersionUID = -3042686055658047285L;

        public String name() {
            return getClass().getSimpleName();
        }
    }


	protected final Map<String, AServerReply> STRING_SERVER_REPLY_HASH_MAP = new HashMap<>();



    protected <DATA extends Serializable, RET extends Serializable> void addServerReply(AServerReply<DATA, RET> reply) {
        addServerReply(reply.name(), reply);
    }
    protected <DATA extends Serializable, RET extends Serializable> void addServerReply(String type, AServerReply<DATA, RET> reply) {
        STRING_SERVER_REPLY_HASH_MAP.put(type, reply);
    }
	protected void removeServerReply(AServerReply reply) {
        removeServerReply(reply.name());
	}
	protected void removeServerReply(String name) {
		STRING_SERVER_REPLY_HASH_MAP.remove(name);
	}

    protected <DATA extends Serializable, RET extends Serializable> AServerReply<DATA, RET> findServerReply(String name) {
        return STRING_SERVER_REPLY_HASH_MAP.get(name);
    }



	public static class RemoteThrowException extends Exception {
        public RemoteThrowException(Throwable cause) {
            super(cause);
        }
    }
    static class RemoteThrowExceptionWrap implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;

        Throwable throwable;
        public RemoteThrowExceptionWrap(Throwable throwable) {
            this.throwable = throwable;
        }
    }

	public abstract byte[] _sendOriginalData(String target, String type, byte[] data);


	public <DATA extends Serializable, RET extends Serializable> RET send(final String target, final AServerReply<DATA, RET> data) throws IOException, ClassNotFoundException, RemoteThrowException {
        if (data.getClass().isAnonymousClass()) {
            throw new RuntimeException("anonymous type");
        }
        Object send = send0(target,  data.name(), data);
        return (RET)  send;
    }
	private Object send0(String target,
                         String type, Object data) throws IOException, ClassNotFoundException, RemoteThrowException {
        byte[] dataBytes    = toSerialBytes(data);
        byte[] resultBytes  = _sendOriginalData(target, type, dataBytes);
        Object resultObject = toSerialObject(resultBytes);
        if (resultObject instanceof RemoteThrowExceptionWrap) {
            throw new RemoteThrowException(((RemoteThrowExceptionWrap)resultObject).throwable);
        }
        return resultObject;
    }
}

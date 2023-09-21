package top.fols.box.reflect.re;

import top.fols.atri.lang.Classz;
import top.fols.box.net.interconnect.InterconnectSocketClient;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver.AServerReply;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver.InterconnectSocketClientImpl;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver.RemoteThrowException;
import top.fols.box.net.interconnect.InterconnectSocketServer;

@Deprecated
@SuppressWarnings({"rawtypes", "unchecked"})
public class _Re_TestDebuggerClient implements Closeable {
    static protected final Map<String, AServerReply> STRING_SERVER_REPLY_HASH_MAP = new HashMap<>();
    static {
        Class<?>[] classes = _Re_TestDebuggerClient.class.getClasses();
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
    }
    protected static <DATA extends Serializable, RET extends Serializable> void addServerReply(AServerReply<DATA, RET> reply) {
        addServerReply(reply.name(), reply);
    }
    protected static <DATA extends Serializable, RET extends Serializable> void addServerReply(String type, AServerReply<DATA, RET> reply) {
        STRING_SERVER_REPLY_HASH_MAP.put(type, reply);
    }
    protected static void removeServerReply(AServerReply reply) {
        removeServerReply(reply.name());
    }
    protected static void removeServerReply(String name) {
        STRING_SERVER_REPLY_HASH_MAP.remove(name);
    }
    protected static <DATA extends Serializable, RET extends Serializable> AServerReply<DATA, RET> findServerReply(String name) {
        return STRING_SERVER_REPLY_HASH_MAP.get(name);
    }

    static class AInterconnectSocketSerializeReceiver extends InterconnectSocketSerializeReceiver {
        _Re_TestDebuggerClient client;
        public AInterconnectSocketSerializeReceiver(_Re_TestDebuggerClient client) {
            this.client = client;
        }

        @Override
        public byte[] _sendOriginalData(String target, String type, byte[] data) {
            return client.client.send(type, data);
        }


        @Override protected <DATA extends Serializable, RET extends Serializable> void addServerReply(AServerReply<DATA, RET> reply) {
            _Re_TestDebuggerServer.addServerReply(reply);
        }
        @Override
        protected <DATA extends Serializable, RET extends Serializable> void addServerReply(String type, AServerReply<DATA, RET> reply) {
            _Re_TestDebuggerServer.addServerReply(type, reply);
        }
        @Override
        protected void removeServerReply(AServerReply reply) {
            _Re_TestDebuggerServer.removeServerReply(reply);
        }
        @Override
        protected void removeServerReply(String name) {
            _Re_TestDebuggerServer.removeServerReply(name);
        }
        @Override
        protected <DATA extends Serializable, RET extends Serializable> AServerReply<DATA, RET> findServerReply(String name) {
            return _Re_TestDebuggerServer.findServerReply(name);
        }

        <DATA extends Serializable, RET extends Serializable> RET send(AServerReply<DATA, RET> data) throws IOException, ClassNotFoundException, RemoteThrowException {
            return super.send(null, data);
        }
    };


    AInterconnectSocketSerializeReceiver  receiver = new AInterconnectSocketSerializeReceiver(this);
    InterconnectSocketClientImpl client = receiver.createClient();


    public _Re_TestDebuggerClient(InetAddress inetAddress, int port) {
        client.connect(inetAddress, port);
    }







    public static _Re_TestDebuggerClient link(int port) throws IOException, InterruptedException {
        return link(InetAddress.getLocalHost(), port);
    }

    public static _Re_TestDebuggerClient link(Re re) throws IOException, InterruptedException {
        _Re_TestDebuggerServer debuggerServer = re.open_debugger();
        InetAddress address = debuggerServer.getAddress();
        Integer p = debuggerServer.getPort();
        return link(address, p);
    }
    public static _Re_TestDebuggerClient link(InetAddress ip, int port) throws IOException {
        return new _Re_TestDebuggerClient(ip, port);
    }


    public boolean isConnect() {
        return client.isConnect();
    }
    @Override
    public void close() {
        client.close();
    }




    public _Re_TestDebuggerServer.FindDebuggerStack.NativeStack findDebuggerStack() throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.FindDebuggerStack());
    }
    public Boolean recoveryDebuggerStack() throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.RecoveryDebugger());
    }

    public _Re_TestDebuggerServer.GetObjectVariableList.VarElementList getObjectVariableList(_Re_TestDebuggerServer.IGetObjectID element) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.GetObjectVariableList(element));
    }

    public _Re_TestDebuggerServer.GetObjectData.ObjectData     getObjectData(_Re_TestDebuggerServer.IGetObjectID element) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.GetObjectData(new _Re_TestDebuggerServer.IGetObjectID[]{element})).first();
    }
    public _Re_TestDebuggerServer.GetObjectData.ObjectDataList getObjectData(_Re_TestDebuggerServer.IGetObjectID[] element) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.GetObjectData(element));
    }

    public Integer getObjectVariableCount(_Re_TestDebuggerServer.IGetObjectID objectID) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.GetObjectVariableCount(objectID));
    }
    public _Re_TestDebuggerServer.GetObjectData.ObjectData getObjectVariableValue(_Re_TestDebuggerServer.IGetObjectID objectID, _Re_TestDebuggerServer.IGetObjectID keyID) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.GetObjectVariableValue(objectID, null, keyID));
    }
    public _Re_TestDebuggerServer.GetObjectData.ObjectData getObjectVariableValue(_Re_TestDebuggerServer.IGetObjectID objectID, Object key) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.GetObjectVariableValue(objectID, key, null));
    }

    public _Re_TestDebuggerServer.GetObjectData.ObjectData setObjectVariableValueFromCodeResult(_Re_TestDebuggerServer.IGetObjectID objectID, _Re_TestDebuggerServer.IGetObjectID keyID, String code) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.SetObjectVariableValueFromCodeResult(objectID, null, keyID, code));
    }
    public _Re_TestDebuggerServer.GetObjectData.ObjectData setObjectVariableValueFromCodeResult(_Re_TestDebuggerServer.IGetObjectID objectID, Object key, String code) throws RemoteThrowException, IOException, ClassNotFoundException {
        return receiver.send(new _Re_TestDebuggerServer.SetObjectVariableValueFromCodeResult(objectID, key, null, code));
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
            _Re_TestDebuggerClient nrs = ((AInterconnectSocketSerializeReceiver) server).client;
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
            receiver.send(new _Re_TestDebuggerServer.PrintByte(buf));
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

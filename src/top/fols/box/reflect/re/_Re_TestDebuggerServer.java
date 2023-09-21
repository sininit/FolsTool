package top.fols.box.reflect.re;

import top.fols.atri.lang.*;
import top.fols.atri.lang.Objects;
import top.fols.atri.lock.LockAwait;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.box.net.interconnect.InterconnectSocketClient;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver.AServerReply;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver.InterconnectSocketServerImpl;
import top.fols.box.net.interconnect.InterconnectSocketServer;

import java.io.*;
import java.net.InetAddress;
import java.util.*;

import static top.fols.box.reflect.re.Re_Utilities.objectAsName;


@Deprecated
@SuppressWarnings({"rawtypes", "unchecked"})
public class _Re_TestDebuggerServer implements Closeable{
    static protected final Map<String, AServerReply> STRING_SERVER_REPLY_HASH_MAP = new HashMap<>();
    static {
        Class<?>[] classes = _Re_TestDebuggerServer.class.getClasses();
        for (Class c: classes){
            if (Classz.isInstanceNullable(c, AServerReply.class)) {
                try {
                    //noinspection unchecked
                    addServerReply((AServerReply) c.getDeclaredConstructor().newInstance());
                } catch (Throwable e) {
                    throw new RuntimeException(e);//不太可能
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
        _Re_TestDebuggerServer server;
        public AInterconnectSocketSerializeReceiver(_Re_TestDebuggerServer server) {
            this.server = server;
        }

        @Override
        public byte[] _sendOriginalData(String target, String type, byte[] data) {
            return server.server.send(target, type, data);
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


        protected <DATA extends Serializable, RET extends Serializable> void send(@NotNull InterconnectSocketSerializeReceiver.AServerReply<DATA, RET> data) throws IOException, InterconnectSocketSerializeReceiver.RemoteThrowException, ClassNotFoundException {
            String[] clients = server.server.getClients();
            for (String client : clients) {
                try {
                    super.send(client, data);
                } catch (Throwable ignored) {}
            }
        }
    };


    AInterconnectSocketSerializeReceiver receiver = new AInterconnectSocketSerializeReceiver(this);
    InterconnectSocketServerImpl server = receiver.createServer();

    final Re host;
    final Object debuggerLock = new Object();

    volatile Re_NativeStack nativeStack;
    volatile LockAwait locks;

    public _Re_TestDebuggerServer(Re host) {
        this.host = host;
    }




    public void debugger(Re_NativeStack nativeStack) throws InterruptedException {
        synchronized (debuggerLock) {
            this.nativeStack = nativeStack;

            try {
                locks = new LockAwait();
                locks.await();
            } finally {
                this.nativeStack = null;
                this.locks = null;
            }
        }
    }
    public void recovery() {
        this.nativeStack = null;
        if (null != locks) {
            this.locks.unlock();
        }
    }
    public void waitRecovery() throws InterruptedException {
        if (null != locks) {
            locks.await();
        }
    }


    public InetAddress getAddress() {
        return server.getAddress();
    }
    public Integer getPort() {
        return server.getPort();
    }

    public void setBootPort(int startPort0) {
        server.setBootPort(startPort0);
    }
    public Integer getBootPort() {
        return server.getBootPort();
    }


    public boolean isRunning() {
        return null != getPort();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
    }
    @Override
    public void close() {
        server.close();
    }





    @SuppressWarnings({"UnnecessaryModifier", "UnnecessaryInterfaceModifier"})
    public static interface IGetObjectID {
        public String getID();
    }
    public static class ObjectID implements Serializable, IGetObjectID {
        private static final long serialVersionUID = -3042686055658047285L;
        String id;
        public ObjectID(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ObjectID objectID = (ObjectID) o;
            return java.util.Objects.equals(id, objectID.id);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(id);
        }
    }
    public static boolean isEmptyID(String id) {
        return null == id || id.length() == 0;
    }
    public static boolean isEmptyID(IGetObjectID id) {
        return null == id || isEmptyID(id.getID());
    }


    public LocalObjectMap getLocalObjectMap(InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData) {
        String id = takeoverData.getId();
        InterconnectSocketServer.Client client = server.getClient(id);
        Object lom = client.getCache("lom");
        if (null == lom) {
            client.setCache("lom", lom = new LocalObjectMap());
        }
        return (LocalObjectMap) lom;
    }



    /**
     * 获取当前执行器ID
     */
    public static class FindDebuggerStack extends AServerReply<FindDebuggerStack, FindDebuggerStack.NativeStack> implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;
        FindDebuggerStack(){}

        public static class NativeStack implements Serializable, IGetObjectID  {
            private static final long serialVersionUID = -3042686055658047285L;


            String nativeStackID;
            NativeStackElement[] nativeStackElements;
            NativeStack(String nativeStackID) {
                this.nativeStackID = nativeStackID;
            }

            @Override
            public String getID() {
                return nativeStackID;
            }

            public NativeStackElement[] getNativeStackElements(){return nativeStackElements;}
        }

        public static class NativeStackElement implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;

            String nativeStackElementAsString;
            String nativeStackElementExecutorAsString;

            String nativeStackElementID;
            String nativeStackElementExecutorID;
            NativeStackElement(String nativeStackElementAsString, String nativeStackElementExecutorAsString,
                               String nativeStackElementID, String nativeStackElementExecutorID) {
                this.nativeStackElementAsString=nativeStackElementAsString;
                this.nativeStackElementExecutorAsString = nativeStackElementExecutorAsString;

                this.nativeStackElementID = nativeStackElementID;
                this.nativeStackElementExecutorID = nativeStackElementExecutorID;
            }

            public String getNativeStackElementExecutorAsString() {
                return nativeStackElementExecutorAsString;
            }
            public String getNativeStackElementAsString(){return nativeStackElementAsString;}

            public String getNativeStackElementID() {
                return nativeStackElementID;
            }
            public String getExecutorID() {
                return nativeStackElementExecutorID;
            }
        }

        @Override
        public NativeStack callback(InterconnectSocketSerializeReceiver reply, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData, FindDebuggerStack data) {
            _Re_TestDebuggerServer debuggers = ((AInterconnectSocketSerializeReceiver)reply).server;
            Re_NativeStack nativeStack = debuggers.nativeStack;
            if (null ==    nativeStack) {
                return null;
            }
            LocalObjectMap localObjectMap = debuggers.getLocalObjectMap(takeoverData);

            Re_NativeStack.ReNativeTraceElement[] nativeStackTraces = nativeStack.getReNativeTraceElements();
            String id = localObjectMap.createID(nativeStack);

            NativeStack stack = new NativeStack(id);
            stack.nativeStackElements = new NativeStackElement[nativeStackTraces.length];
            for (int i = 0; i < nativeStackTraces.length; i++) {
                Re_NativeStack.ReNativeTraceElement nativeTrace = nativeStackTraces[i];
                Re_Executor executor = nativeTrace.executor;

                String nativeTraceID = localObjectMap.createID(nativeTrace);
                String executorID    = localObjectMap.createID(executor);

                stack.nativeStackElements[i] = new NativeStackElement(
                        nativeTrace.toString(), objectAsName(executor),
                        nativeTraceID, executorID);
            }
            return stack;
        }
    }
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static class RecoveryDebugger extends AServerReply<RecoveryDebugger, Boolean> implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;

        RecoveryDebugger() {
        }

        @Override
        public Boolean callback(InterconnectSocketSerializeReceiver reply, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData, RecoveryDebugger findDebuggerStack) throws Throwable {
            _Re_TestDebuggerServer debuggers = ((AInterconnectSocketSerializeReceiver)reply).server;
            Re re = debuggers.host;

            _Re_TestDebuggerServer debuggerServer = re.open_debugger();
            debuggerServer.recovery();

            LocalObjectMap localObjectMap = debuggers.getLocalObjectMap(takeoverData);
            localObjectMap.clear();
            return true;
        }
    }



    /**
     * 获取对象值
     */
    public static class GetObjectData extends AServerReply<GetObjectData, GetObjectData.ObjectDataList> implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;
        GetObjectData(){}

        public IGetObjectID[] objectIDS;
        public GetObjectData(IGetObjectID[] objectIDS) {
            this.objectIDS = objectIDS;
        }


        public static class ObjectDataList implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;

            ObjectData[] data;
            public ObjectDataList(ObjectData[] data) {
                this.data = data;
            }

            public ObjectData first() {
                return Objects.first(data);
            }

            public ObjectData[] getData(){
                return data;
            }
        }
        public static class ObjectData implements Serializable, IGetObjectID {
            private static final long serialVersionUID = -3042686055658047285L;
            public static final ObjectData[] EMPTY_ARRAY = {};

            String  id;         //对象的ID, 基础数据没有ID
            String  name;       //对象名称类似于toString
            Object  valueFromBaseType;       //如果valueIsBaseType 为true 则直接输出对象的值否则始终为null
            String  javaClass;  //对象的类名
            boolean baseType;   //对象是否为Java基本类型（加上字符串）
            boolean arrayType;  //对象是否为Java数组

            public String getName()         {return name;}

            public boolean isBaseType()     {
                return baseType;
            }
            public boolean isStringType()   {
                return Re_Utilities.isJString(valueFromBaseType);
            }
            public boolean isArray()        {
                return arrayType;
            }

            public Object getValueFromBaseType()        {return valueFromBaseType;}
            public Class findValueClass() throws ClassNotFoundException {
                if (null == javaClass)
                    return null;
                return Class.forName(javaClass);
            }
            public String getJavaClass() {
                return javaClass;
            }


            public IGetObjectID getObjectID() {
                return new ObjectID(id);
            }

            @Override
            public String toString() {
                return "ObjectElement{" +
                        "objectID='" + id + '\'' +
                        ", objectName='" + name + '\'' +
                        ", objectIsBaseType=" + baseType +
                        ", objectIsArrayType=" + arrayType +
                        '}';
            }

            @Override
            public String getID() {
                return id;
            }
        }


        /**
         * 基础数据没有ID
         */
        public static ObjectData toObjectData(LocalObjectMap localObjectMap, Object value) {
            ObjectData objectData        = new ObjectData();
            objectData.baseType          = Re_Utilities.isJBaseData(value);
            objectData.arrayType         = Re_Utilities.isJArray(value);
            objectData.valueFromBaseType = objectData.baseType ? value:null;
            objectData.javaClass         = Re_Utilities.getObjectJavaClassName(value);
            objectData.name              = objectAsName(value);
            objectData.id                = objectData.baseType?null:localObjectMap.createID(value);
            return objectData;
        }


        @Override
        public ObjectDataList callback(InterconnectSocketSerializeReceiver reply, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData, GetObjectData data) {
            _Re_TestDebuggerServer debuggers = ((AInterconnectSocketSerializeReceiver)reply).server;
            Re re = debuggers.host;

            LocalObjectMap localObjectMap = debuggers.getLocalObjectMap(takeoverData);

            IGetObjectID[] objectIDS = data.objectIDS;
            List<ObjectData> objectDataList = new ArrayList<>();

            for (IGetObjectID objectID : objectIDS) {
                String id = objectID.getID();
                Object value = localObjectMap.getObject(id);

                ObjectData objectData = toObjectData(localObjectMap, value);

                objectDataList.add(objectData);
            }

            return new ObjectDataList(objectDataList.toArray(ObjectData.EMPTY_ARRAY));
        }
    }




    /**
     * 获取所有对象键值
     */
    public static class GetObjectVariableList extends AServerReply<GetObjectVariableList, GetObjectVariableList.VarElementList> implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;
        GetObjectVariableList() {}

        public IGetObjectID objectID;
        public GetObjectVariableList(IGetObjectID objectID) {
            this.objectID = objectID;
        }


        public static class VarElementList implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;


            VarElement[] data;

            public VarElementList(VarElement[] data) {
                this.data = data;
            }

            public  VarElement[] getData(){
                return data;
            }
        }


        public static class VarElement implements Serializable {
            private static final long serialVersionUID = -3042686055658047285L;
            public static final VarElement[] EMPTY_ARRAY = {};

            String  keyID;
            String  keyToString;
            boolean keyIsBaseType;
            Object  keyFromBaseType; //只有 keyIsBaseType 为true的情况下才会输出


            GetObjectData.ObjectData valueData;

            public Object getKeyFromBaseType(){return keyFromBaseType;}
            public String getKeyToString()   {return keyToString;}
            public boolean isKeyIsBaseType() {return keyIsBaseType;}

            public GetObjectData.ObjectData getValueData(){
                return valueData;
            }


            @Override
            public String toString() {
                return "VarElement{" +
                        "key='" + keyToString + '\'' +
                        ", keyIsBaseType=" + keyIsBaseType +
                        ", valueData=" + valueData +
                        '}';
            }


            public IGetObjectID getKeyID() {
                return new ObjectID(keyID);
            }
            public IGetObjectID getValueID() {
                return valueData.getObjectID();
            }
        }


        public VarElement toVarElement(LocalObjectMap localObjectMap, Object key, Object value) {
            VarElement varElement = new VarElement();

            varElement.keyIsBaseType   = Re_Utilities.isJBaseData(key);
            varElement.keyFromBaseType = varElement.keyIsBaseType ? key:null;
            varElement.keyID           = varElement.keyIsBaseType ? null : localObjectMap.createID(key);
            varElement.keyToString     = Re_Utilities.objectAsString(key);

            varElement.valueData = GetObjectData.toObjectData(localObjectMap, value);

            return varElement;
        }

        @Override
        public VarElementList callback(InterconnectSocketSerializeReceiver reply, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData, GetObjectVariableList data) {
            _Re_TestDebuggerServer debuggers = ((AInterconnectSocketSerializeReceiver)reply).server;
            Re re = debuggers.host;

            LocalObjectMap localObjectMap = debuggers.getLocalObjectMap(takeoverData);

            Object object = localObjectMap.getObject(data.objectID.getID());

            List<VarElement> varElementList = new ArrayList<>();
            if (Re_Utilities.isSpace(object)) {
                Re_Executor executor = (Re_Executor) object;
                varElementList.add(toVarElement(localObjectMap, Re_Keywords.INNER_VAR__STATIC, executor.reClass));
                varElementList.add(toVarElement(localObjectMap, Re_Keywords.INNER_VAR__THIS,   executor.reClassInstance));
            }
            Iterable keystore = Re_Utilities.UnsafeDebugger.getObjectKeys(object);
            for (Object key: keystore) {
                Object value = Re_Utilities.UnsafeDebugger.getObjectFieldValue(object, key);
                VarElement varElement = toVarElement(localObjectMap, key, value);
                varElementList.add(varElement);
            }
            return new VarElementList(varElementList.toArray(VarElement.EMPTY_ARRAY));
        }
    }





    /**
     * 获取对象键值
     */
    public static class GetObjectVariableCount extends AServerReply<GetObjectVariableCount, Integer> implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;
        GetObjectVariableCount(){}

        IGetObjectID objectID;

        public GetObjectVariableCount(IGetObjectID objectID) {
            this.objectID = objectID;
        }


        @Override
        public Integer callback(InterconnectSocketSerializeReceiver reply, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData,
                                GetObjectVariableCount data) {
            _Re_TestDebuggerServer debuggers = ((AInterconnectSocketSerializeReceiver)reply).server;
            Re re = debuggers.host;

            LocalObjectMap localObjectMap = debuggers.getLocalObjectMap(takeoverData);

            Object object = localObjectMap.getObject(data.objectID.getID());
            return Re_Utilities.UnsafeDebugger.getObjectLength(object);
        }
    }
    /**
     * 获取对象键值
     */
    public static class GetObjectVariableValue extends AServerReply<GetObjectVariableValue, GetObjectData.ObjectData> implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;
        GetObjectVariableValue(){}

        IGetObjectID objectID;
        Object       keyFromBase;
        IGetObjectID keyID;

        public GetObjectVariableValue(IGetObjectID objectID, Object keyFromBase, IGetObjectID keyID) {
            this.objectID = objectID;

            this.keyFromBase = keyFromBase;
            this.keyID    = keyID;
        }


        @Override
        public GetObjectData.ObjectData callback(InterconnectSocketSerializeReceiver reply, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData, GetObjectVariableValue data) {
            _Re_TestDebuggerServer debuggers = ((AInterconnectSocketSerializeReceiver)reply).server;
            Re re = debuggers.host;

            LocalObjectMap localObjectMap = debuggers.getLocalObjectMap(takeoverData);

            Object object = localObjectMap.getObject(data.objectID.getID());
            Object k;
            if (null == data.keyID) {
                k = data.keyFromBase;
            } else {
                k = localObjectMap.getObject(data.keyID.getID());
            }
            Object value  = Re_Utilities.UnsafeDebugger.getObjectFieldValue(object, k);

            return GetObjectData.toObjectData(localObjectMap, value);
        }
    }

    /**
     * 设置值，从代码执行结果
     */
    public static class SetObjectVariableValueFromCodeResult extends AServerReply<SetObjectVariableValueFromCodeResult, GetObjectData.ObjectData> implements Serializable {
        private static final long serialVersionUID = -3042686055658047285L;
        SetObjectVariableValueFromCodeResult(){}

        IGetObjectID objectID;

        Object       keyFromBase;
        IGetObjectID keyID;
        String code;    //执行的代码

        public SetObjectVariableValueFromCodeResult(IGetObjectID objectID, Object keyFromBase, IGetObjectID keyID, String code) {
            this.objectID = objectID;

            this.keyFromBase = keyFromBase;
            this.keyID    = keyID;
            this.code     = code;
        }


        @Override
        public GetObjectData.ObjectData callback(InterconnectSocketSerializeReceiver reply, InterconnectSocketClient.TakeoverData<InterconnectSocketServer> takeoverData, SetObjectVariableValueFromCodeResult data) throws Throwable {
            _Re_TestDebuggerServer debuggers = ((AInterconnectSocketSerializeReceiver)reply).server;
            Re re = debuggers.host;

            LocalObjectMap localObjectMap = debuggers.getLocalObjectMap(takeoverData);

            String expression = data.code;

            Object object = localObjectMap.getObject(data.objectID.getID());
            Object k;
            Object value;
            Object[] params;
            if (null == data.keyID) {
                k = data.keyFromBase;
            } else {
                k = localObjectMap.getObject(data.keyID.getID());
            }

            if (Re_Utilities.isSpace(object)) {
                Re_Executor executor = (Re_Executor) object;
                Re_NativeStack stack = re.newStack();
                params = new Object[]{object, executor.getReClass(), executor.getReClassInstance()};
                value = Re_Class.Unsafes.runOnReClassOrThrowEx(executor,
                            re, stack,
                            executor.getReClass(), executor.getReClassInstance(),
                            expression, params);
                Re.throwStackException(stack);
            } else {
                params = new Object[]{object};
                value = re.execute(expression, params);
            }
            Re_Utilities.UnsafeDebugger.setObjectFieldValue(object, k, value);

            return GetObjectData.toObjectData(localObjectMap, value);
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
			_Re_TestDebuggerServer nrs = ((AInterconnectSocketSerializeReceiver) server).server;
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
			receiver.send(new _Re_TestDebuggerClient.PrintByte(buf));
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








    public static class LocalObjectMap {
        final WeakHashMap<String, Object> kv = new WeakHashMap<>();
        final WeakHashMap<Object, String> vk = new WeakHashMap<>();

        void clear() {
            synchronized (kv) {
                kv.clear();
                vk.clear();
            }
        }



        private void remove0(String id) {
            if (kv.containsKey(id)) {
                Object o = kv.get(id);
                vk.remove(o);
                kv.remove(id);
            }
        }


        public void remove(String... ids) {
            synchronized (kv) {
                if (null != ids) {
                    for (String id : ids) {
                        remove0(id);
                    }
                }
            }
        }
        public void remove(String id) {
            synchronized (kv) {
                remove0(id);
            }
        }

        public int getCount() {
            return kv.size();
        }

        public Object getObject(String id) {
            synchronized (kv) {
                return kv.get(id);
            }
        }

        public String getID(Object v) {
            synchronized (kv) {
                return vk.get(v);
            }
        }

        public String createID(Object v) {
            synchronized (kv) {
                String s = vk.get(v);
                if (null == s) {
                    String s1 = System.nanoTime() + "_" + System.identityHashCode(v); //

                    vk.put(v, s1);
                    kv.put(s1, v);

                    return s1;
                } else {
                    return s;
                }
            }
        }

    }
}

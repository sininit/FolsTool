package top.fols.box.reflect.re;

import top.fols.atri.lang.Objects;
import top.fols.box.reflect.re.resource.Re_IReResource;
import top.fols.box.reflect.re.resource.Re_IReResourceFile;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

/**
 *
 * 2021/11/12 - 2021/11/30
 * @author GXin <a href="http://github.com/sininit">http://github.com/sininit</a>
 *
 *
 *
 *
 *
 * thread safe
 *
 * $("$1.out.println($2)", import_java_class("java.lang.System"), 666)    >> System.out.println(666)
 */
@SuppressWarnings("SameParameterValue")
public class Re implements Closeable {
    /*@Override */protected void finalize() {this.close();}


    @Override
    public void close() {
        this.close_debugger();
    }


    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static Object $(String expression, Object... params) throws Re_Accidents.CompileTimeGrammaticalException, Re_Accidents.ExecuteException {
        Re re = new Re();
        try {
            return re.execute(expression, params);
        } finally {
            re.close();
        }
    }
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static Object $(String expression, Re_IRe_VariableMap variableMap, Object... params) throws Re_Accidents.CompileTimeGrammaticalException, Re_Accidents.ExecuteException {
        Re re = new Re();
        try {
            return re.execute(expression, variableMap, params);
        } finally {
            re.close();
        }
    }








    public static void throwStackException(Re_Executor executor) throws Re_Accidents.ExecuteException {
        throwStackException(executor.getStack());
    }
    public static void throwStackException(Re_NativeStack reStack) throws Re_Accidents.ExecuteException {
        if (reStack.isThrow()) {
            Re_PrimitiveClass_exception.Instance aThrow = reStack.getThrow();
            throw new Re_Accidents.ReExecuteException(aThrow, true);
        }
    }










    private static final Re_IJavaReflector DEFAULT_REFLECTOR        = new Re_ReflectJava();


    //----------------------------------------------------------------
    public final Rez.Safes                          java = new Rez.Safes(this);
    protected final Re_ClassInstance                environment;

    protected Re_IJavaReflector                     reflector;          //Java反射器, 不要返回任何对象（Field,Method,Constructor）给用户,仅用于内部执行,防止用户setAccessible
    protected volatile _Re_TestDebuggerServer       debuggerServer;
    private NativeBootstrapReClassLoader bootstrapClassLoader;




    public Re() {
        this(DEFAULT_REFLECTOR);
    }
    public Re(Re_IJavaReflector reflector) {
        this.reflector   = Objects.requireNonNull(reflector, "reflector");
        this.environment = java.createInstanceOrThrowEx_object(this);
    }




    /**
     * env.xxx
     */
    public Object getEnvironment(String name)               { return Re_Variable.Unsafes.fromUnsafeAccessorGetValueOrThrowEx(name, environment); }
    public void setEnvironment(String name, Object value)   { Re_Variable.Unsafes.fromUnsafeAccessorSetValueInternOrThrowEx(name, value, environment); }
    public boolean hasEnvironment(String name)              { return Re_Variable.has(name, environment); }
    public Re_ClassInstance getEnvironmentMap()             { return environment; }



    static Re_CodeFile compile(String filePath,
                                         String version,
                                         String methodName,
                                         int lineOffset,
                                         String expression) throws Re_Accidents.CompileTimeGrammaticalException {
        Re_CodeLoader re_codeLoader = new Re_CodeLoader();
        return re_codeLoader.load(expression, version, methodName, filePath ,lineOffset);
    }


    private final Map<String, Re_CodeFile> COMPILED_EVAL = new WeakHashMap<>();
    public Re_CodeFile compileEval(String expression) throws Re_Accidents.CompileTimeGrammaticalException {
        Re_CodeFile query = COMPILED_EVAL.get(expression);
        if (null == query) {
            synchronized (COMPILED_EVAL) {
                COMPILED_EVAL.put(expression, query = compile(Re_CodeFile.FILE_NAME__INNER_EVAL,
                        null,
                        Re_CodeFile.METHOD_NAME__EMPTY,
                        1, expression));
            }
        }
        return query;
    }

    protected Re_CodeFile compileClass(String filePath, String expression) throws Re_Accidents.CompileTimeGrammaticalException {
        return compile0(filePath, Re_CodeLoader.version(expression),
                Re_CodeFile.METHOD_NAME__CLASS_INIT, 1, expression);
    }
    public Re_CodeFile compileJavaSource(String expression) throws Re_Accidents.CompileTimeGrammaticalException {
        return compile0(Re_CodeFile.FILE_NAME__JAVA_SOURCE, null,
                Re_CodeFile.METHOD_NAME__EMPTY, 1, expression);
    }
    private Re_CodeFile compile0(String filePath, String version, String methodName, int lineOffset, String expression) throws Re_Accidents.CompileTimeGrammaticalException {
        return compile(filePath, version, methodName, lineOffset, expression);
    }




    public Re_NativeStack newStack() {
        return Re_NativeStack.newStack(this);
    }



    public Object execute(final String expression, Object... params) throws Re_Accidents.CompileTimeGrammaticalException, Re_Accidents.ExecuteException {
        Re_CodeFile block = compileJavaSource(expression);
        return execute(block, this.newStack(), Re.newLocalVariableMap(), params);
    }
    public Object execute(final Re_CodeFile block, Object... params) throws Re_Accidents.CompileTimeGrammaticalException, Re_Accidents.ExecuteException {
        return execute(block, this.newStack(), Re.newLocalVariableMap(), params);
    }
    public Object execute(final String expression, Re_IRe_VariableMap variableMap, Object... params) throws Re_Accidents.CompileTimeGrammaticalException, Re_Accidents.ExecuteException {
        Re_CodeFile block = compileJavaSource(expression);
        return execute(block, this.newStack(), variableMap, params);
    }
    public Object execute(final Re_CodeFile block, Re_IRe_VariableMap variableMap, Object... params) throws Re_Accidents.CompileTimeGrammaticalException, Re_Accidents.ExecuteException {
        return execute(block, this.newStack(), variableMap, params);
    }
    public Object execute(final Re_CodeFile block, Re_NativeStack stack, Re_IRe_VariableMap variableMap, Object... params) throws Re_Accidents.CompileTimeGrammaticalException, Re_Accidents.ExecuteException {
        Re_Executor executor = Re_Executor.createReRootExecutor(this, stack , block, params, variableMap);
        Re.throwStackException(stack);

        Object result  = executor.run();
        Re.throwStackException(stack);
        return result;
    }








    @SuppressWarnings("unused")
    public NativeBootstrapReClassLoader openBootstrapClassLoader() {
        NativeBootstrapReClassLoader loader = this.bootstrapClassLoader;
        if (null != loader)
            return  loader;
        synchronized (java) {
            loader = this.bootstrapClassLoader;
            if (null != loader)
                return  loader;
            return this.bootstrapClassLoader = createBootstrapReClassLoader();
        }
    }
    public void addBootstrapClassLoaderResource(Re_IReResource resource) {
        if (null != resource) {
            openBootstrapClassLoader()
                    .addSourceManager(resource);
        }
    }
    protected NativeBootstrapReClassLoader createBootstrapReClassLoader() {
        return new NativeBootstrapReClassLoader(this);
    }


    //----------------------------------------------------------------

    static Re_IRe_VariableMap newPrimitiveObjectVariableMap()    { return new Re_VariableMapAsObjectSynchronized(); }
    static Re_IRe_VariableMap newPrimitiveListVariableMap()      { return new Re_VariableMapAsObjectSynchronized(); }
    static Re_IRe_VariableMap newPrimitiveJsonVariableMap()      { return new Re_VariableMapAsObjectSynchronizedLinked(); }

    static public Re_IRe_VariableMap newObjectVariableMap()             { return new Re_VariableMapAsObject(); }
    static public Re_IRe_VariableMap newLocalVariableMap()              { return new Re_VariableMapAsObject(); }



    //----------------------------------------------------------------



    public boolean is_debugger() {
        return null != debuggerServer;
    }
    public _Re_TestDebuggerServer get_debugger() {
        _Re_TestDebuggerServer server = debuggerServer;
        if (null != server)
            return  server;
        synchronized (java) {
            server = debuggerServer;
            if (null != server)
                return  server;
            return debuggerServer = new _Re_TestDebuggerServer(this);
        }
    }
    public void close_debugger() {
        synchronized (java) {
            if (null != debuggerServer) {
                debuggerServer.close();
                debuggerServer = null;
            }
        }
    }
    public _Re_TestDebuggerServer open_debugger() throws IOException, InterruptedException {
        _Re_TestDebuggerServer debugger = get_debugger();
        debugger.start();
        return debugger;
    }


    //----------------------------------------------------------------
    private Re_PrimitiveClass_exception         class_exception;
    public final   Re_PrimitiveClass_exception  class_exception() {
        Re_PrimitiveClass_exception c = class_exception;
        if (null != c)
            return  c;
        synchronized (java) {
            c = class_exception;
            if (null != c)
                return  c;
            return class_exception = new Re_PrimitiveClass_exception(this, Re_Keywords.INNER_CLASS__EXCEPTION);
        }
    }
    private Re_PrimitiveClass_json        class_json;
    public final   Re_PrimitiveClass_json class_json() {
        Re_PrimitiveClass_json c = class_json;
        if (null != c)
            return  c;
        synchronized (java) {
            c = class_json;
            if (null != c)
                return  c;
            return class_json = new Re_PrimitiveClass_json(this, Re_Keywords.INNER_CLASS__JSON);
        }
    }
    private Re_PrimitiveClass_list        class_list;
    public final   Re_PrimitiveClass_list class_list() {
        Re_PrimitiveClass_list c = class_list;
        if (null != c)
            return  c;
        synchronized (java) {
            c = class_list;
            if (null != c)
                return  c;
            return class_list = new Re_PrimitiveClass_list(this, Re_Keywords.INNER_CLASS__LIST);
        }
    }
    private Re_PrimitiveClass_object        class_object;
    public final   Re_PrimitiveClass_object class_object() {
        Re_PrimitiveClass_object c = class_object;
        if (null != c)
            return  c;
        synchronized (java) {
            c = class_object;
            if (null != c)
                return  c;
            return class_object = new Re_PrimitiveClass_object(this, Re_Keywords.INNER_CLASS__OBJECT);
        }
    }
    private Re_PrimitiveClass_reflect        class_reflect;
    public final   Re_PrimitiveClass_reflect class_reflect() {
        Re_PrimitiveClass_reflect c = class_reflect;
        if (null != c)
            return  c;
        synchronized (java) {
            c = class_reflect;
            if (null != c)
                return  c;
            return class_reflect = new Re_PrimitiveClass_reflect(this, Re_Keywords.INNER_CLASS__REFLECT);
        }
    }
    private Re_PrimitiveClass_thread        class_thread;
    public final   Re_PrimitiveClass_thread class_thread() {
        Re_PrimitiveClass_thread c = class_thread;
        if (null != c)
            return  c;
        synchronized (java) {
            c = class_thread;
            if (null != c)
                return  c;
            return class_thread = new Re_PrimitiveClass_thread(this, Re_Keywords.INNER_CLASS__REFLECT);
        }
    }
    //----------------------------------------------------------------




    protected void printErr(String content) { System.err.print(content); }
    protected void printlnErr() { printlnErr(""); }
    protected void printlnErr(String content) { printErr(content + "\n"); }

    protected void print(String str)  { System.out.print(str); }
    protected void println() { println(""); }
    protected void println(String content) { print(content + "\n"); }



    //----------------------------------------------------------------










    public static class NativeDynamicCompileClassLoader extends Re_NativeClassLoader {
        private NativeDynamicCompileClassLoader(Re re, Re_NativeClassLoader parent) {
            super(re, parent);
        }

        //顶级类
        @SuppressWarnings("SpellCheckingInspection")
        @Override
        protected final Re_CodeFile compileReClassFile(Re_NativeStack stack,
                                                       String filePath, String expression) {
            if (stack.isThrow()) return null;

            try {
                return re.compileClass(filePath, expression);
            } catch (Re_Accidents.CompileTimeGrammaticalException e) {
                stack.setThrow("compiletime expression error: " + e.getMessage()
                        + " in " + Re_NativeStack.toLineAddressString(e.getFilePath(), e.getLine()) + "");
                return null;
            }
        }

        @Override
        protected void initReClass(Re_NativeStack stack,
                                   Re_Class define) {
            if (stack.isThrow()) return;

            Re_Class.runReClassInitialize0(re, stack, define.getCodeBlock(), define);
        }


        @Override
        public void addSourceManager(Re_IReResource resource) {
            super.addSourceManager(resource);
        }

        @Override
        public boolean removeSourceManager(Re_IReResource resource) {
            return super.removeSourceManager(resource);
        }

        @Override
        public boolean hasSourceManager(Re_IReResource resource) {
            return super.hasSourceManager(resource);
        }

        @Override
        public Re_IReResourceFile getClassResource(String className) {
            return super.getClassResource(className);
        }

        @Override
        public Re_IReResourceFile getFileResource(String filePath) {
            return super.getFileResource(filePath);
        }

        @Override
        public Re_IReResource[] getSources() {
            return super.getSources();
        }
    }


    //默认引导类加载器
    public static class NativeBootstrapReClassLoader extends NativeDynamicCompileClassLoader {
        protected NativeBootstrapReClassLoader(Re re) {
            super(re, null);
        }
    }

    /**
     * 使用re 进行类编译
     */
    public static class NativeApplicationReClassLoader extends NativeDynamicCompileClassLoader {
        public NativeApplicationReClassLoader(Re re, Re_NativeClassLoader parent) {
            super(re, parent);
        }
    }
}

/**
 * all unsafe
 * {@link top.fols.box.reflect.re.Re_Class.UnsafesRe}
 * {@link top.fols.box.reflect.re.Re_Class.SafesRe}
 *
 * {@link top.fols.box.reflect.re.Re_Class.Unsafes}
 * {@link top.fols.box.reflect.re.Re_Class.Safes}
 *
 *
 * {@link top.fols.box.reflect.re.Re_ClassFunction.Unsafes}
 *
 * {@link top.fols.box.reflect.re.Re_Variable.UnsafesRe}
 * {@link top.fols.box.reflect.re.Re_Variable.Unsafes}
 */

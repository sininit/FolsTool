import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.io.BytesInputStreams;
import top.fols.atri.io.Delimiter;
import top.fols.atri.io.file.Filez;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;
import top.fols.atri.reflect.ReflectMatcher;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.time.Times;
import top.fols.box.lang.Classx;
import top.fols.box.reflect.ClassProperties;
import top.fols.box.reflect.ReflectMatcherAsPeak;
import top.fols.box.reflect.re.*;
import top.fols.box.reflect.re.resource.Re_DirectoryResource;
import top.fols.box.util.EntityTable;
import top.fols.box.util.process_guard.ProcessGuard;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


@SuppressWarnings({"ConstantConditions", "rawtypes"})
public class Main extends k {
    static final File  		      logDirectory   = new File(Filez.RUN_CANONICAL.getPath(), "/log/");
    static final String 		  logFile        = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
    static final SimpleDateFormat logPrefix 	 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static boolean isReplaceSystemStream;
    static final OutputStream logStream 	 = new OutputStream() {
        final Charset CHARSET = Charset.defaultCharset();
        final Delimiter.IBytesDelimiter lineDelimiter = Delimiter.build(Finals.LineSeparator.getAllSystemLineSeparatorSortedMaxToMin(), CHARSET);

        @Override
        public void write(int p1) throws IOException {
            // TODO: Implement this method
            write(new byte[]{(byte) p1});
        }
        public void write(byte[] b, int off, int len) throws java.io.IOException {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(logDirectory, logFile), true);
                fos.write(logPrefix.format(System.currentTimeMillis()).getBytes(CHARSET));
                fos.write(":    ".getBytes(CHARSET));

                if (len > 0) {
                    fos.write(b, off, len);

                    byte[][] split = Delimiter.split(b, off, len, lineDelimiter).toArray(new byte[][]{});
                    byte[] last = split[split.length - 1];
                    if (last.length <= 0) {
                        fos.write("\n".getBytes(CHARSET));
                    }
                }
            } finally {
                Streams.close(fos);
            }
        }
    };
    static public synchronized void loadReplaceSystemStream() {
        if (!isReplaceSystemStream) {
            logDirectory.mkdirs();
            Systems.ReplaceOutputStream.replaceSout(logStream);
            Systems.ReplaceOutputStream.replaceSerr(logStream);
            isReplaceSystemStream = true;
        }
    }
    static {
        loadReplaceSystemStream();
    }

    static {
        EntityTable et = new EntityTable(){};
        EntityTable.Entity e1 = et.newEntity("1");
        EntityTable.Entity e2 = et.newEntity("2");
        et.appendEntity(et.getRootEntity(), e1);
        et.appendEntity(et.getRootEntity(), e2);
        System.out.println(et);

        et.appendEntity(e1, e2);
        System.out.println(et);

//  et.appendChild(e2, e1);
//  System.out.println(et.toString());

        System.out.println(et.fromMap(et.toMap()));



        System.out.println(et);

        System.out.println("=======");
        EntityTable.Entity e3 = et.newEntity("3");
        e3.append(e1);
        e3.append(e2);
        System.out.println(e3);
        System.out.println(et);

        System.out.println(Re.$("$(0).entityMap", et));
    }

    public static void main(String[] args) throws Throwable {
        Re re = new Re();
        re.setEnvironment("test", new Re_IRe_Object.IPrimitiveCall("") {
            @Override
            public Object executeThis(Re_Executor executor, Re_CodeLoader.Call call) {
                throw new RuntimeException("exception");
            }
        });
        re.addBootstrapClassLoaderResource(new Re_DirectoryResource(new File(
                "C:\\documents\\360Sync\\Programming\\JAVA\\fols\\folsTool\\document\\re\\例子\\bootstrap")));

        Re.NativeApplicationReClassLoader defaultClassLoader;
        defaultClassLoader = new Re.NativeApplicationReClassLoader(re, re.openBootstrapClassLoader());
        defaultClassLoader.addSourceManager(new Re_DirectoryResource(new File(
                "C:\\documents\\360Sync\\Programming\\JAVA\\fols\\folsTool\\document\\re\\例子\\loader\\")));


//        final _Re_TestDebuggerClient link = _Re_TestDebuggerClient.link(re);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        _Re_TestDebuggerServer.FindDebuggerStack.NativeStack executorObjectID = link.findDebuggerExecutorObjectID();
//                        if (null != executorObjectID) {
//                            _Re_TestDebuggerServer.GetObjectData.ObjectData objectData = link.getObjectData(executorObjectID);
//                            _Re_TestDebuggerServer.GetObjectVars.VarElement[] varElements = link.listObjectKeys(executorObjectID);
//                            System.out.println("-----");
//                            System.out.println("object: " + objectData);
//                            System.out.println("key-count: " + varElements.length);
//                            for (_Re_TestDebuggerServer.GetObjectVars.VarElement varElement : varElements) {
//                                _Re_TestDebuggerServer.IGetObjectID keyID    = varElement.getKeyID();
//                                _Re_TestDebuggerServer.IGetObjectID valueID  = varElement.getValueID();
//
//                                _Re_TestDebuggerServer.GetObjectData.ObjectData v = link.getObjectData(valueID);
//                                System.out.println(varElement.getKeyAsString() + "=" + v.getValue());
//
//                                v = link.setObjectData(executorObjectID, objectData.getObjectID(), keyID, "throw(666)");
//                                System.out.println(varElement.getKeyAsString() + "=" + v.getValue());
//                            }
//                            System.out.println("-----");
//                            System.out.println();
//                        }
//                    } catch (Throwable ex) {
//                        ex.printStackTrace();
//                    }
//                    Times.sleep(1000);
//                }
//            }
//        }).start();

        System.out.println("re debugger port: " + re.open_debugger().getPort());

        Re_Class re_class = defaultClassLoader.loadClassOrThrowEx("main");




        String expression = Filez.wrap("C:\\documents\\360Sync\\Programming\\JAVA\\fols\\folsTool\\document\\re\\例子\\loader\\test_math.re").readUTF8String();
        System.out.println(re.execute(expression, "784920843"));




        System.out.println("--------------------------------------");


//        long x;
//        for (int i2 = 0; i2<10; i2++) {
//            x = System.currentTimeMillis();
//            for (int i = 0;i<3000*10000;i++){
//                re.execute("aDADSA");
//            }
//            System.out.println("耗时："+(System.currentTimeMillis()-x));
//        }



        Compiler.start(
                "C:\\Program Files\\Java\\jdk1.8.0_171\\bin"
                , "src"
                , "libs"
                , "top.fols.box.jar"
        );
        Compiler.runBat(
                "mvn install:install-file -DgroupId=top.fols -DartifactId=box -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar -Dfile=" + Filez.RUN_CANONICAL.file("top.fols.box.jar").getPath());
        if (true) { return; }

        //

        // TestA.class.getMethod("a").invoke(new TestA());
        // TestA.class.getMethod("a").invoke(new TestB());
        // TestA.class.getMethod("a").invoke(new TestC());
        // System.out.println();

        // System.out.println(Strings.join()(XReflectCacheFast.defaultInstance.getAllInheritMethods(TestC.class),
        // "\n"));
        // System.out.println();
        // System.out.println(Strings.join()(XReflectCacheFast.defaultInstance.getAllInheritMethodsFast(TestC.class),
        // "\n"));

        // System.out.println();
        // System.out.println(XReflectCache.defaultInstance.getMethod(TestC.class,
        // "a"));
        // System.out.println(XReflectCacheFast.defaultInstance.getMethod(TestC.class,
        // "a"));
        // System.out.println();


        System.out.println(ReflectMatcherAsPeak.DEFAULT.requireMethod(Main.class, null,"main1",
                new Class[]{BytesInputStreams[].class}));

        if (true) {
            return;
        }
        Class searchClass = Main.class;
        Class[] searchConstructorClass;

        System.out.println("----list---");
//        System.out.println(XString
//                .join(ReflectMatcherAsPeak.DEFAULT.cacher().getConstructors(searchClass).list(), "\n"));

        System.out.println("-------");
        System.out.println("search-list: "
                + (Classx.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{int.class, String.class}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (Classx.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{Integer.class, String.class}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (Classx.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[]{Integer.class, null}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (Classx.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[]{int[][].class}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (Classx.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{int.class, CharSequence.class}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (Classx.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[]{String[].class}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (Classx.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{int.class, JSONObject.class}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (Classx.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{Integer.class, Object.class}))
                + " result="
                + Strings.join(ReflectMatcherAsPeak.DEFAULT.requireConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("-------");

        Class returnType = void.class;
        Class mcc = Main.class;
        Class[] pcc = new Class[]{String[].class};
        Times jsq00 = new Times();
        for (int i = 0; i < 100 * 10000; i++) {
            ReflectMatcherAsPeak.DEFAULT.constructor(mcc, pcc);
        }
        System.out.println("time: "+jsq00.passed());

        if (true) {
            return;
        }




        /* test reflect */
        try {
            System.out.println("______test xreflectoptdeclared start");

            Class<?> mainclass = Main.class;
            Method[] ms0 = Reflects.methods(mainclass);
            for (Method m : ms0)
                System.out.println(m);
            System.out.println();
            System.out.println(Arrays.toString(mainclass.getMethods()));
            System.out.println(Arrays.toString(mainclass.getDeclaredMethods()));
            System.out.println();

            ReflectMatcher.DEFAULT.requireMethod(mainclass, null, "main", new Object[]{new String[]{}});
            System.out.println(ReflectMatcher.DEFAULT.requireField(mainclass, null, "k"));

            Times jsq0 = new Times();
            for (int i = 0; i < 1000000; i++) {
                ReflectMatcher.DEFAULT.constructor(mainclass, new String[]{}, new Main(), "", 1,
                        new Finals());
            }
            System.out.println("time: "+jsq0.passed());
            System.out.println("______test xreflectoptdeclared end");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






    
    @Override
    public void v() {
        // TODO: Implement this method
    }

    @Override
    protected void dddd() {
        // TODO: Implement this method
        super.dddd();
    }

    public Main() {
    }

    public Main(CharSequence[] s, Main t, CharSequence tt, int gggg, Finals ooh) {
    }

    public static void main() {
        System.out.println("=");
    }

    private static int k = 8;

    public static class v2 {
        protected void k() {
        }
    }

    public static class v1 extends v2 {

    }

    public static void name() {
        System.out.println("sb");
    }

    public void name2() {
        System.out.println("sb");
    }

    // public interface b {
    // default void l() {
    //
    // }
    //
    // public static void b() {
    // }
    // }

    // public class a implements b {
    // public void a() {
    // System.out.println("sb");
    // }
    // }

    public class BB {
        public void b() {

        }
    }

    public class AA extends BB {
        public void a() {

        }

        @Override
        public void b() {

        }
    }

    public static class TestA {
        public void a() {
            System.out.println("a");
        }

        public void a2() {
            System.out.println("a");
        }
    }

    public static class TestB extends TestA {
        public void a() {
            System.out.println("b");
        }
    }

    public static class TestC extends TestB {
        public void a() {
            System.out.println("c");
        }
    }

    private int a, b, c, d, e, f, g, h, r, p, esc, eg, v;

    public Main(Integer p, String dd) {
        System.out.println(1);
    }

    public Main(int p, String dd) {
        System.out.println(2);
    }

    public Main(Integer p, CharSequence ff) {
        System.out.println(3);
    }

    public Main(Integer p, Object ff) {
        System.out.println(4);
    }

    public Main(CharSequence[] p) {
        System.out.println(5);
    }

    public Main(String[] p) {
        System.out.println(6);
    }

    public Main(Object[] p) {
        System.out.println(7);
    }

    public static void main(CharSequence ss) {
    }

    public static void main(Object[][] f) {
    }


    public static void main1(int a) {
    }

    public static void main1(Object args) {
    }

    public static void main1(Object[] args) {
    }

    public static void main1(Object[][] args) {
    }

    public static void main1(int[] args) {
    }

    public static void main1(InputStream[] args) throws Throwable {
    }

    public static void main1(int[][] args) {
    }

    public static void main1(InputStream[][] args) throws Throwable {
    }



    public interface Size {
        int size();
    }



}


abstract class k {

    int df, tt, yh, dd, ghji, ttfgg, fryh, dedcvhu, rrtgv;

    public abstract void v();

    protected void dddd() {
        System.out.println();
    }

    public interface Size{}
}
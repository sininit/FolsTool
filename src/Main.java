
import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;

import top.fols.atri.array.ArrayObject;
import top.fols.atri.array.ArrayObjects;
import top.fols.box.io.base.XByteArrayInputStream;
import top.fols.box.lang.XClass;
import top.fols.box.lang.XString;
import top.fols.box.lang.reflect.XReflectMatcher;
import top.fols.box.lang.reflect.XReflectPeakMatcher;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;
import top.fols.box.statics.XStaticFixedValue;
import top.fols.box.time.XTimeConsum;
import top.fols.box.util.json.JSONObject;

abstract class k {

    int df, tt, yh, dd, ghji, ttfgg, fryh, dedcvhu, rrtgv;

    public abstract void v();

    protected void dddd() {
        System.out.println();
    }
}

public class Main extends k {

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

    public Main(CharSequence[] s, Main t, CharSequence tt, int gggg, XStaticFixedValue ooh) {
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

    public static void main(String[] args) throws Throwable {
//        while (0 <= System.currentTimeMillis()) {
//            System.out.println(XTimeTool.currentTimeMillis());
//            System.out.println(System.currentTimeMillis());
//            System.out.println("=============================");
//        }

        Compiler.start(
                "C:\\Program Files\\Java\\jdk1.8.0_212\\bin"
                , "src"
                , "libs"
                , "top.fols.box.jar"
        );
        if (true) { return; }


        //

        // TestA.class.getMethod("a").invoke(new TestA());
        // TestA.class.getMethod("a").invoke(new TestB());
        // TestA.class.getMethod("a").invoke(new TestC());
        // System.out.println();

        // System.out.println(XString.join(XReflectCacheFast.defaultInstance.getAllInheritMethods(TestC.class),
        // "\n"));
        // System.out.println();
        // System.out.println(XString.join(XReflectCacheFast.defaultInstance.getAllInheritMethodsFast(TestC.class),
        // "\n"));

        // System.out.println();
        // System.out.println(XReflectCache.defaultInstance.getMethod(TestC.class,
        // "a"));
        // System.out.println(XReflectCacheFast.defaultInstance.getMethod(TestC.class,
        // "a"));
        // System.out.println();

        // if (true) {
        // return;
        // }


        System.out.println(XReflectPeakMatcher.defaultInstance.getMethod(Main.class, "main1",
                new Class[]{XByteArrayInputStream[].class}));

        if (true) {
            return;
        }
        Class searchClass = Main.class;
        Class[] searchConstructorClass;

        System.out.println("----list---");
        System.out.println(XString
                .join(XReflectPeakMatcher.defaultInstance.getCacher().getConstructors(searchClass).list(), "\n"));

        System.out.println("-------");
        System.out.println("search-list: "
                + (XClass.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{int.class, String.class}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (XClass.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{Integer.class, String.class}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (XClass.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[]{Integer.class, null}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (XClass.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[]{int[][].class}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (XClass.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{int.class, CharSequence.class}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (XClass.joinParamJavaClassCanonicalName(searchConstructorClass = new Class[]{String[].class}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (XClass.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{int.class, JSONObject.class}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("search: "
                + (XClass.joinParamJavaClassCanonicalName(
                searchConstructorClass = new Class[]{Integer.class, Object.class}))
                + " result="
                + XString.join(XReflectPeakMatcher.defaultInstance.getConstructors(searchClass, searchConstructorClass),
                "\n"));

        System.out.println("-------");
        System.out.println("-------");

        Class returnType = void.class;
        Class mcc = Main.class;
        Class[] pcc = new Class[]{String[].class};
        XTimeConsum jsq00 = XTimeConsum.newAndStart();
        for (int i = 0; i < 100 * 10000; i++) {
            XReflectPeakMatcher.defaultInstance.getConstructor(mcc, pcc);
        }
        System.out.println(jsq00.endAndGetEndLessStart());

        if (true) {
            return;
        }




        /* test reflect */
        try {
            System.out.println("______test xreflectoptdeclared start");

            Class<?> mainclass = Main.class;
            Method[] ms0 = XReflectAccessible.getMethodsAll(mainclass);
            for (Method m : ms0)
                System.out.println(m);
            System.out.println();
            System.out.println(Arrays.toString(mainclass.getMethods()));
            System.out.println(Arrays.toString(mainclass.getDeclaredMethods()));
            System.out.println();

            XReflectMatcher.defaultInstance.getMethod(mainclass, "main", new Object[]{new String[]{}});
            System.out.println(XReflectMatcher.defaultInstance.getField(mainclass, "k"));

            XTimeConsum jsq0 = XTimeConsum.newAndStart();
            for (int i = 0; i < 1000000; i++) {
                XReflectMatcher.defaultInstance.getConstructor(mainclass, new String[]{}, new Main(), "", 1,
                        new XStaticFixedValue());
            }
            System.out.println(jsq0.endAndGetEndLessStart());
            System.out.println("______test xreflectoptdeclared end");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

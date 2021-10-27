package top.fols.box.lang;

public class XClassTest {
    static class A{}
    public static void main(String[] args) {
        System.out.println(XClass.toAbsCanonicalName(short[].class));
        System.out.println(XClass.toAbsCanonicalName(float[].class));
        System.out.println(XClass.toAbsCanonicalName(long[].class));
        System.out.println(XClass.toAbsCanonicalName(char[].class));
        System.out.println(XClass.toAbsCanonicalName(double[].class));
        System.out.println(XClass.toAbsCanonicalName(boolean[].class));
        System.out.println(XClass.toAbsCanonicalName(byte[].class));
        System.out.println(XClass.toAbsCanonicalName(int[].class));




        System.out.println(XClass.toAbsCanonicalName(A[].class));
        System.out.println(XClass.toAbsCanonicalName(A.class));

        System.out.println((A[].class).getCanonicalName());
        System.out.println((A.class).getCanonicalName());
    }
}
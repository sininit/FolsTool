package top.fols.box.lang;

import top.fols.atri.lang.Classz;

public class XClassTest {
    static class A{}
    public static void main(String[] args) {
        System.out.println(Classx.getClassGetNameToCanonicalName(short[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(float[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(long[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(char[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(double[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(boolean[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(byte[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(int[].class));




        System.out.println(Classx.getClassGetNameToCanonicalName(A[].class));
        System.out.println(Classx.getClassGetNameToCanonicalName(A.class));

        System.out.println((A[].class).getCanonicalName());
        System.out.println((A.class).getCanonicalName());
    }
}
package top.fols.box.io.os;

import java.io.File;

public class XFileTest {
    public static void main(String[] args) throws Throwable {


        System.out.println(new File("a/b", "../").getCanonicalPath());
        System.out.println(XFile.dealRelativePath(XFile.normalizePath("a/b", "../", '/'), '/'));


        System.out.println(new File("a/b", "/../").getCanonicalPath());
        System.out.println(XFile.dealRelativePath(XFile.normalizePath("a/b", "/../", '/'), '/'));


        System.out.println(new File(" /"));
        System.out.println(new File("/sdcard/../../..//.\\../swcvf/../..").getPath());



        System.out.println(new File("~/4").getCanonicalPath());
        System.out.println(new File("/.").getCanonicalPath());
        System.out.println(new File("/..").getCanonicalPath());




        System.out.println("------1");

        System.out.println(XFile.normalizePath("", '/'));
        System.out.println(XFile.normalizePath("/", '/'));
        System.out.println(XFile.normalizePath("//////", '/'));
        System.out.println(XFile.normalizePath("a", '/'));
        System.out.println(XFile.normalizePath("a/", '/'));
        System.out.println(XFile.normalizePath("a/b/c/", '/'));
        System.out.println(XFile.normalizePath("/a/b/c/", '/'));

        System.out.println("------2");

        System.out.println(XFile.normalizePath("", "", '/'));
        System.out.println(XFile.normalizePath("/", "", '/'));
        System.out.println(XFile.normalizePath("/a", "", '/'));
        System.out.println(XFile.normalizePath("/a", "/", '/'));
        System.out.println(XFile.normalizePath("a", "/", '/'));
        System.out.println(XFile.normalizePath("", "/a", '/'));
        System.out.println(XFile.normalizePath("/", "a/b", '/'));
        System.out.println(XFile.normalizePath("/a", "/b", '/'));
        System.out.println(XFile.normalizePath("a", "b", '/'));
        System.out.println(XFile.normalizePath("a/", "b", '/'));

        System.out.println("------3");

        System.out.println(XFile.getCanonicalRelativePath(""));
        System.out.println(XFile.getCanonicalRelativePath("/./../../../b"));

        String testpath = "//XSt/]/tt/////.//./././a/b/v//x//a/vv//n///...//../../();".replace("/", File.separator);
        System.out.println("path: " + testpath);
        System.out.print(XFile.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        System.out.println("------4");

        testpath = "/XSt/]/tt/////.//././///////../.././a/b/v//x//a/vv//n///...//../../();".replace("/",
                File.separator);
        System.out.println("path: " + testpath);
        System.out.print(XFile.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        System.out.println("------5");


        testpath = XFile.getCanonicalRelativePath("hhh*/.././//////gggghjj/../..ggg/rrrf".replace("/",
                File.separator));
        System.out.println("path: " + testpath);
        System.out.print(XFile.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        /*
         * true /XSt/a/b/v/x/a/v**v/();
         * ../XSt/*?:]/tt/./././../.././a/b/v/x/a/v**v/n/.../../../(); /..ggg/rrrf
         */
        System.out.println("------6");

        System.out.println(XFile.dealRelativePath("a/b/c/../../d", '/'));
        System.out.println(XFile.dealRelativePath("/a/b/c/../../d", '/'));


        System.out.println(XFile.dealRelativePath("a/../../b/", '/'));
        System.out.println(XFile.dealRelativePath("/a/../../b/", '/'));



        System.out.println(XFile.dealRelativePath("/../../a/", '/'));
        System.out.println(XFile.dealRelativePath("../../a/", '/'));

        System.out.println(XFile.dealRelativePath("" , '/'));
        System.out.println(XFile.dealRelativePath("/.." , '/'));
        System.out.println(XFile.dealRelativePath("..", '/'));



        System.out.println(XFile.dealRelativePath("a/b/c", '/'));
        System.out.println(XFile.dealRelativePath("a/b/c/", '/'));

        System.out.println(XFile.dealRelativePath("/a/b/c", '/'));
        System.out.println(XFile.dealRelativePath("/a/b/c/", '/'));


        System.out.println(XFile.dealRelativePath("a/b/c/..", '/'));
        System.out.println(XFile.dealRelativePath("/a/b/c/..", '/'));



        System.out.println(XFile.dealRelativePath("a/..", '/'));
        System.out.println(XFile.dealRelativePath("a/../", '/'));
        System.out.println(XFile.dealRelativePath("/a/..", '/'));
        System.out.println(XFile.dealRelativePath("/a/../" , '/'));




        System.out.println(XFile.dealRelativePath("a/./b", '/'));
        System.out.println(XFile.dealRelativePath("a/../b", '/'));
        System.out.println(XFile.dealRelativePath("a/b/../c", '/'));


        System.out.println(XFile.dealRelativePath("/./a", '/'));
        System.out.println(XFile.dealRelativePath("/../a", '/'));


        System.out.println(XFile.dealRelativePath("/a/../", '/'));
        System.out.println(XFile.dealRelativePath("/a/../", '/'));

        System.out.println("------");


        System.out.println(XFile.getCanonicalRelativePath("////..///", '/'));
        System.out.println(XFile.getCanonicalRelativePath("////..", '/'));
        System.out.println(XFile.getCanonicalRelativePath("..////", '/'));
        System.out.println(XFile.getCanonicalRelativePath("///\\\\/../", '/'));
        System.out.println(XFile.getCanonicalRelativePath("/1/2/../", '/'));

        System.out.println("------");

    }
}
package top.fols.box.io.os;

import top.fols.atri.io.Filex;

import java.io.File;

public class XFileTest {
    public static void main(String[] args) throws Throwable {


        System.out.println(new File("a/b", "../").getCanonicalPath());
        System.out.println(Filex.dealRelativePath(Filex.normalizePath("a/b", "../", '/'), '/'));


        System.out.println(new File("a/b", "/../").getCanonicalPath());
        System.out.println(Filex.dealRelativePath(Filex.normalizePath("a/b", "/../", '/'), '/'));


        System.out.println(new File(" /"));
        System.out.println(new File("/sdcard/../../..//.\\../swcvf/../..").getPath());



        System.out.println(new File("~/4").getCanonicalPath());
        System.out.println(new File("/.").getCanonicalPath());
        System.out.println(new File("/..").getCanonicalPath());




        System.out.println("------1");

        System.out.println(Filex.normalizePath("", '/'));
        System.out.println(Filex.normalizePath("/", '/'));
        System.out.println(Filex.normalizePath("//////", '/'));
        System.out.println(Filex.normalizePath("a", '/'));
        System.out.println(Filex.normalizePath("a/", '/'));
        System.out.println(Filex.normalizePath("a/b/c/", '/'));
        System.out.println(Filex.normalizePath("/a/b/c/", '/'));

        System.out.println("------2");

        System.out.println(Filex.normalizePath("", "", '/'));
        System.out.println(Filex.normalizePath("/", "", '/'));
        System.out.println(Filex.normalizePath("/a", "", '/'));
        System.out.println(Filex.normalizePath("/a", "/", '/'));
        System.out.println(Filex.normalizePath("a", "/", '/'));
        System.out.println(Filex.normalizePath("", "/a", '/'));
        System.out.println(Filex.normalizePath("/", "a/b", '/'));
        System.out.println(Filex.normalizePath("/a", "/b", '/'));
        System.out.println(Filex.normalizePath("a", "b", '/'));
        System.out.println(Filex.normalizePath("a/", "b", '/'));

        System.out.println("------3");

        System.out.println(Filex.getCanonicalRelativePath(""));
        System.out.println(Filex.getCanonicalRelativePath("/./../../../b"));

        String testpath = "//XSt/]/tt/////.//./././a/b/v//x//a/vv//n///...//../../();".replace("/", File.separator);
        System.out.println("path: " + testpath);
        System.out.print(Filex.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        System.out.println("------4");

        testpath = "/XSt/]/tt/////.//././///////../.././a/b/v//x//a/vv//n///...//../../();".replace("/",
                File.separator);
        System.out.println("path: " + testpath);
        System.out.print(Filex.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        System.out.println("------5");


        testpath = Filex.getCanonicalRelativePath("hhh*/.././//////gggghjj/../..ggg/rrrf".replace("/",
                File.separator));
        System.out.println("path: " + testpath);
        System.out.print(Filex.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        /*
         * true /XSt/a/b/v/x/a/v**v/();
         * ../XSt/*?:]/tt/./././../.././a/b/v/x/a/v**v/n/.../../../(); /..ggg/rrrf
         */
        System.out.println("------6");

        System.out.println(Filex.dealRelativePath("a/b/c/../../d", '/'));
        System.out.println(Filex.dealRelativePath("/a/b/c/../../d", '/'));


        System.out.println(Filex.dealRelativePath("a/../../b/", '/'));
        System.out.println(Filex.dealRelativePath("/a/../../b/", '/'));



        System.out.println(Filex.dealRelativePath("/../../a/", '/'));
        System.out.println(Filex.dealRelativePath("../../a/", '/'));

        System.out.println(Filex.dealRelativePath("" , '/'));
        System.out.println(Filex.dealRelativePath("/.." , '/'));
        System.out.println(Filex.dealRelativePath("..", '/'));



        System.out.println(Filex.dealRelativePath("a/b/c", '/'));
        System.out.println(Filex.dealRelativePath("a/b/c/", '/'));

        System.out.println(Filex.dealRelativePath("/a/b/c", '/'));
        System.out.println(Filex.dealRelativePath("/a/b/c/", '/'));


        System.out.println(Filex.dealRelativePath("a/b/c/..", '/'));
        System.out.println(Filex.dealRelativePath("/a/b/c/..", '/'));



        System.out.println(Filex.dealRelativePath("a/..", '/'));
        System.out.println(Filex.dealRelativePath("a/../", '/'));
        System.out.println(Filex.dealRelativePath("/a/..", '/'));
        System.out.println(Filex.dealRelativePath("/a/../" , '/'));




        System.out.println(Filex.dealRelativePath("a/./b", '/'));
        System.out.println(Filex.dealRelativePath("a/../b", '/'));
        System.out.println(Filex.dealRelativePath("a/b/../c", '/'));


        System.out.println(Filex.dealRelativePath("/./a", '/'));
        System.out.println(Filex.dealRelativePath("/../a", '/'));


        System.out.println(Filex.dealRelativePath("/a/../", '/'));
        System.out.println(Filex.dealRelativePath("/a/../", '/'));

        System.out.println("------");


        System.out.println(Filex.getCanonicalRelativePath("////..///", '/'));
        System.out.println(Filex.getCanonicalRelativePath("////..", '/'));
        System.out.println(Filex.getCanonicalRelativePath("..////", '/'));
        System.out.println(Filex.getCanonicalRelativePath("///\\\\/../", '/'));
        System.out.println(Filex.getCanonicalRelativePath("/1/2/../", '/'));

        System.out.println("------");

    }
}
package top.fols.box.io.os;

import java.io.File;

public class XFileTest {
    public static void main(String[] args) throws Throwable {


        String testpath = "//XSt/]/tt/////.//./././a/b/v//x//a/vv//n///...//../../();".replace("/", File.separator);
        System.out.println("path: " +testpath);
        System.out.print(XFile.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        System.out.println("------");

        testpath = "/XSt/]/tt/////.//././///////../.././a/b/v//x//a/vv//n///...//../../();".replace("/",
                File.separator);
        System.out.println("path: " +testpath);
        System.out.print(XFile.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        System.out.println("------");


        testpath = XFile.getCanonicalRelativePath("hhh*/.././//////gggghjj/../..ggg/rrrf".replace("/",
                File.separator));
        System.out.println("path: " +testpath);
        System.out.print(XFile.getCanonicalRelativePath(testpath));
        System.out.println(" ==? " + new File(testpath).getCanonicalPath());

        /*
         * true /XSt/a/b/v/x/a/v**v/();
         * ../XSt/*?:]/tt/./././../.././a/b/v/x/a/v**v/n/.../../../(); /..ggg/rrrf
         */
        System.out.println("------");

        System.out.println(XFile.dealRelativePath("a/..", '/'));
        System.out.println(XFile.dealRelativePath("a/../", '/'));
        System.out.println(XFile.dealRelativePath("/a/..", '/'));
        System.out.println(XFile.dealRelativePath("a/b/..", '/'));
        System.out.println(XFile.dealRelativePath("a/../../a/", '/'));
        System.out.println(XFile.dealRelativePath("/a/..", '/'));
        System.out.println(XFile.dealRelativePath("..", '/'));

        System.out.println("------");

        System.out.println(XFile.getCanonicalRelativePath("////..///", '/'));
        System.out.println(XFile.getCanonicalRelativePath("////..", '/'));
        System.out.println(XFile.getCanonicalRelativePath("..////", '/'));
        System.out.println(XFile.getCanonicalRelativePath("///\\\\/../", '/'));
        System.out.println(XFile.getCanonicalRelativePath("/1/2/../", '/'));

        System.out.println("------");

        System.out.println("(" + XFile.normalizePath("/1/2//3/\\4/5///..", File.separatorChar) + ")");
        System.out.println("(" + XFile.normalizePath("/1/2//3/\\4/5//", File.separatorChar) + ")");
        System.out.println("(" + XFile.normalizePath("", File.separatorChar) + ")");
        System.out.println("(" + XFile.normalizePath("/////", File.separatorChar) + ")");
        System.out.println("(" + XFile.normalizePath("/1/2//3/\\4/5/..", "../5/6/4/", File.separatorChar) + ")");

        System.out.println("------");


        System.out.println(new File("/sdcard/../../..//.\\../swcvf/../..").getPath());

        System.out.println("------");


        System.out.println(new File("/a/\\\\/\\/..").getCanonicalPath());
        System.out.println(XFile.normalizePath("/a/\\\\/\\/..", File.separatorChar));
        System.out.println(XFile.dealRelativePath(XFile.normalizePath("/a/\\\\/\\/..", File.separatorChar), File.separatorChar));
        System.out.println(XFile.dealRelativePath(XFile.normalizePath("/a/\\\\/\\/../", File.separatorChar), File.separatorChar));
        System.out.println(XFile.getCanonicalRelativePath(""));
        System.out.println(XFile.getCanonicalRelativePath("/a/8/8/.."));
        System.out.println(XFile.getCanonicalRelativePath("/a/8/8"));
        System.out.println(XFile.getCanonicalRelativePath("/a/8/8/"));
        System.out.println(XFile.getCanonicalRelativePath("a/.."));
        System.out.println(XFile.getCanonicalRelativePath("/.."));
        System.out.println(XFile.getCanonicalRelativePath("/a/../"));


        System.out.println("------");


        System.out.println(XFile.getCanonicalRelativePath("/1/2//3/\\4/5/..", "../5/6/4/"));
        System.out.println(XFile.getCanonicalRelativePath("////a/", File.separatorChar));
        System.out.println(XFile.getCanonicalRelativePath("/a/b/////c/", File.separatorChar));
        System.out.println(XFile.getCanonicalRelativePath("/a/b/c", File.separatorChar));
        System.out.println(XFile.getCanonicalRelativePath("a/..", File.separatorChar));
        System.out.println(XFile.getCanonicalRelativePath("a/b/c/../../y", File.separatorChar));
        System.out.println(XFile.getCanonicalRelativePath("a/b/c", File.separatorChar));


        System.out.println("------");

        System.out.println(new File("/a/").getPath());
        System.out.println(new File("/a//b//c/////ghhh////").getPath());
        System.out.println(new File("a//b//c/////ghhh////").getPath());
        System.out.println(new File("a/b/c//", "///ghhh/").getPath());
        System.out.println(new File("a/b/c//", "////").getPath());
        System.out.println(new File("/a/", "////").getPath());
        System.out.println(new File("//", "/ghhh").getPath());

        System.out.println("------");

        System.out.println(new XFile("/a/").getPath());
        System.out.println(new XFile("a/b/c//", "///ghhh").getPath());
        System.out.println(new XFile("a/b/c//", "").getPath());
        System.out.println(new XFile("a/b/c", "ghhh").getPath());
        System.out.println(new XFile("a/b/c", "/ghhh").getPath());
        System.out.println(new XFile("a/b/c", "//").getPath());
        System.out.println(new XFile("a/b/c", "").getPath());
        System.out.println(new XFile("/a/", "////").getPath());
        System.out.println(new XFile("/", "/ghhh").getPath());

        System.out.println("------");

    }
}
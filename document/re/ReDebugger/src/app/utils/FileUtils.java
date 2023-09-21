package app.utils;

import top.fols.atri.io.Streams;
import top.fols.atri.io.file.Filez;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Strings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileUtils {


    @SuppressWarnings("SpellCheckingInspection")
    public static File findTemporaryDirectory() {
        File dir = new File(System.getProperty("java.io.tmpdir"));
        File file;
        do {
            String random_name = "scoped_dir" + Strings.random("0123456789abcdef", 16);
            file = new File(dir, random_name);
        } while (file.exists());
        if (file.mkdirs()) {
            return file;
        }
        throw new RuntimeException("cannot create directory: "+file);
    }

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    public static boolean setContent(File file, String str) throws UnsupportedEncodingException {
        Filez wrap = Filez.wrap(file);
        byte[] bytes = str.getBytes(FileUtils.DEFAULT_CHARSET_NAME);
        boolean b = wrap.writeBytes(bytes);
        return b;
    }

    public static String getContent(File file) {
        try {
            byte[] bytes = Filez.wrap(file).readBytes();
            return new String(bytes, FileUtils.DEFAULT_CHARSET_NAME);
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean copyFile(File from, File to) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            if (!from.exists()) { return false; }
            fileInputStream = new FileInputStream(from);

            if (!Objects.empty(to.getParentFile()))
            if (!to.getParentFile().exists() && !to.getParentFile().mkdirs()){ return false; }
            fileOutputStream = new FileOutputStream(to);

            long fileLength = from.length();
            if (fileLength == 0) { return true; }
            Streams.copy(fileInputStream, fileOutputStream);
            fileOutputStream.flush();

            return to.length() == fileLength;
        } finally {
            Streams.close(fileInputStream);
            Streams.close(fileOutputStream);
        }

    }



























    public static String[] listDirectorySortFromCreateTime(File directory) {
        if (null == directory){
            directory = new File(File.separator);
        }
        File[] files = directory.listFiles();
        if (null == files){
            return Finals.EMPTY_STRING_ARRAY;
        }
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long createTime1 = FileUtils.getFileCreateTime(o1.getPath());
                long createTime2 = FileUtils.getFileCreateTime(o2.getPath());
                if (createTime1 > createTime2) {
                    return 1;
                } else if (createTime1 == createTime2) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        List<String> names = new ArrayList<>(files.length);
        for (int i = 0; i < files.length; i++) {
            File projectDir = files[i];
            if (!projectDir.isDirectory()) { //不是一个目录
                continue;
            }
            names.add(files[i].getName());
        }
        return names.toArray(new String[names.size()]);
    }



















    public static File getOrCreateFile(File file) {
        File parent = file.getParentFile();
        if (null != parent && parent.exists()) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                }
            }
        }
        return file;
    }

    public static File getOrCreateDirectory(File file) {
        File parent = file.getParentFile();
        if (null != parent && parent.exists()) {
            if (!file.exists()) {
                try {
                    if (!file.mkdir()) {
                        throw new IOException("create directory: " + file);
                    }
                } catch (IOException ignored) {
                }
            }
        }
        return file;
    }



    private static final Class BASIC_FILE_ATTRIBUTES_CLASS = BasicFileAttributes.class;
    public static long getFileCreateTime(String file) {
        try {
            FileTime t = Files.readAttributes(Paths.get(file), BASIC_FILE_ATTRIBUTES_CLASS).creationTime();
            return t.toMillis();
        } catch (Throwable e) {
            throw new RuntimeException(e);
            //return file.lastModified();
        }
    }



    public static boolean setLastModified(File file, long millis) {
        FileTime fileTime = FileTime.fromMillis(millis);
        try {
            Files.setLastModifiedTime(file.toPath(), fileTime);
            return true;
        } catch (IOException e) {
            return file.setLastModified(millis);
        }
    }



    public static boolean deleteFileOrDirectory(File file) {
        if (null == file){
            return false;
        }
        if (file.isFile()){
            return file.delete();
        }else {
            boolean result = true;
            File[] files = file.listFiles();
            if (null!=files){
                for (int i = 0; i < files.length; i++) {
                    File filei = files[i];
                    result &= deleteFileOrDirectory(filei);
                }
            }
            result &= file.delete();
            return result;
        }

    }

    public static long length(File file) {
        if (null == file){
            return 0;
        }
        if (file.isFile()) {
            return file.length();
        }else {
            long size = 0;
            File[] files = file.listFiles();
            if (null != files){
                for (int i = 0; i < files.length; i++) {
                    File filei = files[i];
                    size += length(filei);
                }
            }
            size += file.length();
            return size;
        }

    }







    public static File requireFile(File file, String message) {
        File get = getFile(file);
        if (null == get) {
            throw new RuntimeException(message+": "+file);
        } else {
            return get;
        }
    }
    public static File getFile(File file) {
        if (null == file) { return null; }
        return file.exists() && file.isFile() ?file:null;
    }

    public static File requireDirectory(File file, String message) {
        File get = getDirectory(file);
        if (null == get) {
            throw new RuntimeException(message+": "+file);
        } else {
            return get;
        }
    }
    public static File getDirectory(File directory) {
        if (null == directory) { return null; }
        boolean exists = directory.exists() && directory.isDirectory();
        return exists ? directory : null;
    }




    public static File findDirectoryFromDirectory(File file, String fileName) {
        if (null == file){
            return null;
        }
        if (file.isDirectory() && fileName.equals(file.getName())) {
            return file;
        } else {
            File[] files = file.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    File filei = files[i];
                    if(null!=(filei= findDirectoryFromDirectory(filei,fileName))){
                        return filei;
                    }
                }
            }
            return null;
        }
    }
    public static File findFileFromDirectory(File file, String fileName) {
        if (null == file){
            return null;
        }
        if (file.isFile() && fileName.equals(file.getName())) {
            return file;
        } else {
            File[] files = file.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    File filei = files[i];
                    if(null!=(filei= findFileFromDirectory(filei,fileName))){
                        return filei;
                    }
                }
            }
            return null;
        }

    }


    public static File      getPathFile(File file) {
        return null == file? null:new File(getPath(file));
    }
    public static String    getPath(File file) {
        if (null == file) { return null; }
        String realDirectory;
        try {
            realDirectory = file.getCanonicalPath();
        } catch (IOException ioException) {
            try {
                realDirectory = file.getAbsolutePath();
            } catch (SecurityException securityException) {
                realDirectory = file.getPath();
            }
        }
        return realDirectory;
    }



}

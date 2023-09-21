package top.fols.box.io.os;

import top.fols.atri.util.DoubleLinked;

import java.io.File;
import java.io.IOException;

@Deprecated
public class FilePermissions {


    public static File setFilePermission(File file, Boolean readable, Boolean writeable, Boolean executeable,
                                         Boolean ownerOnly) {
        if (null == ownerOnly) {
            if (null != readable) {
                file.setReadable(readable);
            }
            if (null != writeable) {
                file.setWritable(writeable);
            }
            if (null != executeable) {
                file.setExecutable(executeable);
            }
        } else {
            if (null != readable) {
                file.setReadable(readable, ownerOnly);
            }
            if (null != writeable) {
                file.setWritable(writeable, ownerOnly);
            }
            if (null != executeable) {
                file.setExecutable(executeable, ownerOnly);
            }
        }
        return file;
    }

    public static File setFilePermissions(File file, Boolean readable, Boolean writeable, Boolean executeable,
                                          Boolean ownerOnly) {
        file = file.getAbsoluteFile();

        DoubleLinked<File> files = new DoubleLinked<>(null);
        File pn = file;
        while (null != (pn = pn.getParentFile())) {
            files.addNext(new DoubleLinked<>(pn));
        }
        DoubleLinked<File> now = files;
        while (null != now && null != (now = (DoubleLinked<File>) now.getNext())) {
            File f = now.content();
            setFilePermission(f, readable, writeable, executeable, ownerOnly);
        }
        setFilePermission(file, readable, writeable, executeable, ownerOnly);
        return file;
    }






    public static boolean mkdirs(File file) {
        return FilePermissions.mkdirs(file, true);
    }

    public static boolean mkdirs(File file, Boolean ownerOnly) {
        file = file.getAbsoluteFile();
        if (file.exists()) {
            FilePermissions.setFilePermissions(file, true, true, true, ownerOnly);
            return true;
        } else {
            boolean b = false;
            DoubleLinked<File> files = new DoubleLinked<>(null);
            files.addNext(new DoubleLinked<>(file));

            File pn = file;
            while (null != (pn = pn.getParentFile())) {
                files.addNext(new DoubleLinked<>(pn));
            }
            DoubleLinked<File> now = files;
            while (null != now && null != (now = (DoubleLinked<File>) now.getNext())) {
                File dir = now.content();
                if (!dir.exists()) {
                    boolean result = dir.mkdir();
                    b = b & result;
                }
                FilePermissions.setFilePermission(dir, true, true, true, ownerOnly);
            }
            return b;
        }
    }

    public static File openFile(File file) {
        return FilePermissions.openFile(file, true);
    }

    public static File openFile(File file, Boolean ownerOnly) {
        return FilePermissions.setFilePermissions(file, true, true, true, ownerOnly);
    }

    public static boolean createNewFile(File file) throws IOException {
        return FilePermissions.createNewFile(file, true);
    }

    public static boolean createNewFile(File file, Boolean ownerOnly) throws IOException {
        file = file.getAbsoluteFile();
        try {
            boolean b0 = false;
            try {
                b0 = file.createNewFile();
            } catch (IOException e) {
                b0 = false;
            }
            if (b0) {
                return true;
            } else {
                File parent = file.getParentFile();
                if (null != parent) {
                    FilePermissions.mkdirs(parent, ownerOnly);
                    return new File(parent, file.getName()).createNewFile();
                } else {
                    return file.createNewFile();
                }
            }
        } finally {
            FilePermissions.setFilePermission(file, true, true, true, ownerOnly);
        }
    }

    public static boolean delete(File file) {
        return FilePermissions.delete(file, true);
    }

    public static boolean delete(File file, Boolean ownerOnly) {
        boolean b0 = file.delete();
        if (b0) {
            return true;
        } else {
            FilePermissions.setFilePermissions(file, true, true, true, ownerOnly);
            if (!file.exists()) {
                return false;
            } else {
                if (file.isFile()) {
                    boolean result = file.delete();
                    return result;
                } else {
                    boolean b1 = file.delete();
                    if (b1) {
                        return true;
                    }
                    boolean b = delete0(file, ownerOnly) & file.delete();
                    return b;
                }
            }
        }
    }

    private static boolean delete0(File file, Boolean ownerOnly) {
        boolean b = true;
        File[] files = file.listFiles();
        if (null != files) {
            for (File f : files) {
                FilePermissions.setFilePermission(f, true, true, true, ownerOnly);
                if (f.isFile()) {
                    b = b & f.delete();
                } else if (f.isDirectory()) {
                    b = b & delete0(f, ownerOnly) & f.delete();
                } else {
                    b = false;
                }
            }
        }
        return b;
    }


}

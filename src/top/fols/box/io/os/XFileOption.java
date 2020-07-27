package top.fols.box.io.os;

import java.io.File;
import java.io.IOException;

public class XFileOption {
    private File rootDirectory;
    private File subpath;

    public XFileOption(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.subpath = null;
    }

    public XFileOption(XFileOption ad) {
        this.rootDirectory = ad.rootDirectory;
        this.subpath = ad.subpath;
    }

    public File getRootDirectory() {
        return this.rootDirectory;
    }

    /**
     * @return newinstance
     */
    public XFileOption setRootDirectory(File file) {
        XFileOption newObject = this.clone();
        newObject.rootDirectory = file;
        // newObject.subpath = newObject.subpath;
        return newObject;
    }

    /**
     * @return newinstance
     */
    public XFileOption root() {
        XFileOption newObject = this.clone();
        // newObject.rootDirectory = newObject.rootDirectory;
        newObject.subpath = null;
        return newObject;
    }

    /**
     * @return newinstance
     */
    @Override
    public XFileOption clone() {
        return new XFileOption(this);
    }

    /**
     * @return newinstance
     */
    public XFileOption sub(String subfilename) {
        XFileOption newObject = this.clone();
        if (null == newObject.subpath) {
            newObject.subpath = new File(subfilename);
        } else {
            newObject.subpath = new File(newObject.subpath, subfilename);
        }
        return newObject;
    }

    /**
     * @return newinstance
     */
    public XFileOption setSub(String subfilepath) {
        XFileOption newObject = this.clone();
        if (null == subfilepath) {
            newObject.subpath = null;
        } else {
            newObject.subpath = new File(subfilepath);
        }
        return newObject;
    }

    /**
     * @return newinstance
     */
    public XFileOption parent() {
        XFileOption newObject = this.clone();
        if (null == newObject.subpath) {
            return newObject;
        }
        File parent = newObject.subpath.getParentFile();
        newObject.subpath = parent;
        return newObject;
    }

    public File getSubPathFile() {
        return null == this.subpath ? null : this.subpath;
    }

    public String getSubPath() {
        File file = this.getSubPathFile();
        return null == file ? null : file.getPath();
    }

    public File getAbsolutePathFile() {
        if (null == this.rootDirectory) {
            if (null == this.subpath) {
                return new File("");
            } else {
                return this.subpath;
            }
        } else {
            if (null == this.subpath) {
                return this.rootDirectory;
            } else {
                return new File(this.rootDirectory, this.subpath.getPath());
            }
        }
    }

    public String getAbsolutePath() {
        return getAbsolutePathFile().getPath();
    }

    File getSubFileAbsoluteFile(String subFile) {
        File now = this.getAbsolutePathFile();
        return null == subFile ? now : new File(now, subFile);
    }

    private boolean createFile0(File file) throws IOException {
        return file.createNewFile();
    }

    private boolean createDirectory0(File file) {
        return file.mkdirs();
    }

    private boolean deleteFileOrDirectory0(File file) {
        boolean b0 = file.delete();
        if (b0) {
            return true;
        } else {
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
                    boolean b = deleteFileOrDirectory00(file) & file.delete();
                    return b;
                }
            }
        }
    }

    private static boolean deleteFileOrDirectory00(File file) {
        boolean b = true;
        File[] files = file.listFiles();
        if (null != files) {
            for (File f : files) {
                if (f.isFile()) {
                    b = b & f.delete();
                } else if (f.isDirectory()) {
                    b = b & deleteFileOrDirectory00(f) & f.delete();
                } else {
                    b = false;
                }
            }
        }
        return b;
    }

    public boolean createFile() throws IOException {
        File file = this.getSubFileAbsoluteFile(null);
        return createFile0(file);
    }

    public boolean createFile(String filename) throws IOException {
        File file = this.getSubFileAbsoluteFile(filename);
        return createFile0(file);
    }

    public boolean createDirectory() {
        File file = this.getSubFileAbsoluteFile(null);
        return createDirectory0(file);
    }

    public boolean createDirectory(String filename) {
        File file = this.getSubFileAbsoluteFile(filename);
        return createDirectory0(file);
    }

    public File getFile() {
        File file = this.getSubFileAbsoluteFile(null);
        return file;
    }

    public File getFile(String subName) {
        File file = this.getSubFileAbsoluteFile(subName);
        return file;
    }

    public boolean delete() {
        File file = this.getSubFileAbsoluteFile(null);
        return deleteFileOrDirectory0(file);
    }

    public boolean delete(String subName) {
        File file = this.getSubFileAbsoluteFile(subName);
        return deleteFileOrDirectory0(file);
    }

}

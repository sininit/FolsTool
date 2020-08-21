import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Compiler {

    public static List<String> getDirList(String filePath, boolean recursion) {
        List<String> List = new ArrayList<String>();
        return getDirList0(List, new File(filePath), recursion, new StringBuilder());
    }

    public static int getDirFileCount(File file) {
        File[] files = file.listFiles();
        int count = 0;
        if (null != files) {
            for (File f : files) {
                if (f.isFile()) {
                    count++;
                }
            }
        }
        return count;
    }

    private static List<String> getDirList0(List<String> list, File filePath, boolean recursion,
            StringBuilder baseDir) {
        File[] files = filePath.listFiles();
        if (null != files)
            for (File file : files) {
                if (null == file)
                    continue;
                String name = file.getName();
                if (file.isDirectory()) {
                    if (recursion) {
                        getDirList0(list, file, true, new StringBuilder(baseDir).append(name).append(File.separator));
                    }
                    if (getDirFileCount(file) > 0) {
                        list.add(new StringBuilder(baseDir).append(name).append(File.separator).toString());
                    }
                }
            }
        return list;
    }

    private static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

    private static final String runDir = System.getProperty("user.dir");

    public static void start(String javaBinDir, String srcDirName, String jarDirName, String jarName)
            throws IOException {
        // String javaBinDir = "C:\\Program Files\\Java\\jdk1.8.0_212\\bin";// jdk目录
        String jar_exe = javaBinDir + "\\" + "jar";
        String javac_exe = javaBinDir + "\\" + "javac";

        // String jarName = "top.fols.box.jar"; //编译打包后的jar名称
        new File(runDir + "\\" + jarName).delete();

        String classesDirName = "classes";
        // String srcDirName = "src";// jar的项目源码
        // String jarDirName = "libs";// 依赖的jar目录

        /**
         * 
         * 
         * process
         * 
         */
        delFile(new File(runDir + "\\" + classesDirName));
        new File(runDir + "\\" + classesDirName).mkdirs();

        String[] cpNames = new File(runDir + "\\" + jarDirName).list();
        StringBuilder cp = new StringBuilder();
        if (null != cpNames) {
            cp.append(" -cp .;");
            StringJoiner xsj = new StringJoiner(";");
            for (String string : cpNames) {
                xsj.add(".\\" + jarDirName + "\\" + string);
            }
            cp.append(xsj);
        }

        StringBuilder command;

        List<String> filelist = getDirList(new File(runDir + "\\" + srcDirName).getAbsolutePath(), true);
        command = new StringBuilder();
        for (int i = 0; i < filelist.size(); i++) {
            command.append(String.format("\"%s\" -parameters -encoding UTF-8 %s -d %s -sourcepath %s %s\\%s*.java",
                    javac_exe, cp, classesDirName, srcDirName, srcDirName, filelist.get(i))).append("\n");
        }
        command.append("echo Comilper Complete").append("\n");

        File bat;
        FileOutputStream fos;
        // javac
        bat = new File("." + "\\" + "comilperSrc.bat").getAbsoluteFile();
        fos = new FileOutputStream(bat);
        fos.write(command.toString().getBytes());
        fos.close();
        command = null;
        Compiler.runBat(bat);
        bat.delete();

        // jar cvf lib.jar -C classes/ .
        String wrapJarName = String.format(jarName);
        command = new StringBuilder();
        command.append(String.format("\"%s\" cvf \"%s\" -C %s\\ .", jar_exe, wrapJarName, classesDirName)).append("\n");
        bat = new File("." + "\\" + "wrapJar.bat").getAbsoluteFile();
        fos = new FileOutputStream(bat);
        fos.write(command.toString().getBytes());
        fos.close();
        command = null;
        Compiler.runBat(bat);
        bat.delete();
    }

    public static void runBat(File bat) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cmd");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        OutputStream os = process.getOutputStream();
        os.write(bat.getName().getBytes());
        os.write("\n".getBytes());
        os.write("exit".getBytes());
        os.write("\n".getBytes());

        os.flush();

        InputStream is = process.getInputStream();
        int read;
        byte[] line = new byte[8192];
        while ((read = is.read(line)) != -1) {
            System.out.println(new String(line, 0, read, "GBK"));
        }
        try {
            process.waitFor();
            process.exitValue();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        is.close();
    }
}

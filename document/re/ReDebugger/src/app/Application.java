package app;

import app.page.IWindowsInstance;
import app.page.Main;
import app.utils.*;
import app.utils.swing.input.InputTextBox;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import top.fols.atri.io.file.Filez;
import top.fols.atri.util.Throwables;
import top.fols.box.array.ArrayObject;
import app.utils.lock.ExecutorLock;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Application {


    private static FileLock processLock;
    private static File processLockFile = getProcessLockFileFile();
    private static FileChannel processLockFileChannel = null;
    public static void checkProcess() throws IOException {
        processLockFileChannel = new FileOutputStream(processLockFile).getChannel();
        processLock = processLockFileChannel.tryLock();
        if (processLock == null) {
            JOptionPane.showMessageDialog(null, "程序已经运行");
            System.exit(1);
        }
    }


    public static File getProcessLockFileFile() { return new File(Filez.RUN.innerFile(), ".lock"); }
    public static File getLogFile() { return new File(Filez.RUN.innerFile(), "log.txt"); }


    public static File directory() { return Filez.RUN.innerFile(); }






    /**
     *
     * @return
     */
    @SuppressWarnings("all")
    public static boolean saveStatus() {
        boolean result;
        result = true;
        result &= main.save();
        for (IWindowsInstance windowsInstance: main.windowsInstances) {
            result &= windowsInstance.save();
        }
        return result;
    }


    static Main main;
    public static Main getMain() {
        return main;
    }

    final static Object logLock = new Object();

    /**
     * windows main, log-view write log
     */
    public static void logln() {
        logln(null);
    }

    /**
     * windows main, log-view write log
     */
    public static void logln(Object text) {
        JTextArea jTextArea;
        if (null == main || null == (jTextArea = main.首页_log_textArea1)) return;

        synchronized (logLock) {
            logln(jTextArea, text);
        }
    }



    public static void logln(JTextArea jTextArea, Object text) {
        if (jTextArea.getText().length() > 1024L * 1024L * 4L) {
            jTextArea.setText(null);
        }


        int start  = jTextArea.getSelectionStart();
        int end    = jTextArea.getSelectionEnd();
        int length = jTextArea.getText().length();

        String s = toString(text);
        String content = (null == text?"": s + "\n");
        jTextArea.append(content);

        int newLength = jTextArea.getText().length();

        if (start == length || end == length) {
            jTextArea.setSelectionStart(newLength);
            jTextArea.setSelectionEnd(newLength);
        }

        if (isError(text)) {
            System.err.println(s);
        } else {
            System.out.println(s);
        }
    }
    public static boolean isError(Object text) {
        return text instanceof Throwable;
    }
    public static String toString(Object text) {
        if (null == text) return String.valueOf((Object) null);
        if (isError(text)) {
            return Throwables.toString((Throwable) text);
        } else {
            Class<?> cls = text.getClass();
            if (cls.isArray()) {
                return ArrayObject.wrap(text).toString();
            }
            return String.valueOf(text);
        }
    }





    public static boolean isAdmin() {
        String[] groups = (new com.sun.security.auth.module.NTSystem()).getGroupIDs();
        for (String group : groups) {
            if (group.equals("S-1-5-32-544"))
                return true;
        }
        return false;
    }



    public static void checkNormalExit() {
        if (!Setting.normal_exit.getBoolean(true)) {
            JOptionPane.showMessageDialog(null, "上次未正常关闭程序, 部分数据可能无法正常保存.");
        }
    }
    public static void normalExit() {
        Setting.normal_exit.setBoolean(true); }










    public static class AutoSave implements Runnable {
        public static final LockThread LOCK = new LockThread();

        AtomicBoolean start = new AtomicBoolean(false);

        long lastUpdate = System.currentTimeMillis();
        long overTime = TimeUnit.MINUTES.toMillis(1);

        @Override
        public void run() {
            while (start.get()) {
                long current = System.currentTimeMillis();
                if (current - lastUpdate >= overTime) {
                    lastUpdate = current;

                    if (start.get()) {
                        Application.logln(Application.saveStatus() ? "自动保存成功" : "自动保存失败");
                    } else {
                        break;
                    }
                }

                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                } catch (InterruptedException ignored) {
                }
            }
        }

        public static AutoSave autoSave = null;
        public static void threadAutoSave(boolean save) {
            if (save) {
                if (null == autoSave || !autoSave.start.get()) {
                    autoSave = new AutoSave();
                    autoSave.start.set(true);
                    LOCK.execute(param -> {
                        try {
                            autoSave.run();
                        } catch (Throwable e){
                            e.printStackTrace();
                        }
                        return null;
                    });
                }
            } else {
                if (null == autoSave) {
                    return;
                } else {
                    exit();
                }
            }
            Setting.auto_save.setBoolean(save);
        }

        public static void exit() {
            if (null != autoSave) {
                autoSave.start.set(false);
                LOCK.joins();
            }
        }
    }






    public static void initSystemStream() {
        FileLogStream logStream = new FileLogStream(getLogFile());

        OutputStream out1 = System.out;
        OutputStream out2 = new OutputStream() {
            OutputStream out = out1;
            @Override public void write(int b) throws IOException { out.write(b); logStream.write(b); }
            @Override public void write(byte[] b, int off, int len) throws IOException { out.write(b, off, len); logStream.write(b, off, len); }
            @Override public void flush() throws IOException { out.flush(); logStream.flush(); }
            @Override public void close() throws IOException { out.close(); logStream.close(); }
        };

        OutputStream err1 = System.err;
        OutputStream err2 = new OutputStream() {
            OutputStream out = err1;
            @Override public void write(int b) throws IOException { out.write(b); logStream.write(b); }
            @Override public void write(byte[] b, int off, int len) throws IOException { out.write(b, off, len); logStream.write(b, off, len); }
            @Override public void flush() throws IOException { out.flush(); logStream.flush(); }
            @Override public void close() throws IOException { out.close(); logStream.close(); }
        };

        System.setOut(new PrintStream(out2));
        System.setErr(new PrintStream(err2));
    }






    public static boolean NORMAL_START; //程序是否正常启动
    public static void initApplication() throws IOException {
        Application.checkProcess();//单实例

        Application.initSystemStream();//重定义系统流
        Application.checkNormalExit();//上次是否正常退出
        Application.hookConsoleKillHandler();//拦截ctrl+c 以及kill 执行 Application::exit

        Runtime.getRuntime().addShutdownHook(new Thread(Application::exit));

        //修改UI风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Throwable {
        try {
            Application.initApplication();

            main = Main.newInstance();
            main.initWindows();

            System.out.println("admin: " + isAdmin());



            NORMAL_START = true;

            System.out.println("start success");
        } catch (Throwable throwable) {
            System.out.println("start fail");
            throwable.printStackTrace();

            System.exit(0);
        }

        /*
          自动保存程序
          @see Main#initWindowsMenu()
         */
        AutoSave.threadAutoSave(Setting.auto_save.getBoolean());

//        XlsProperties.main(args);


        logln("time: "  + TimeUtils.getTime_yyyy_MM_dd_HH_mm_ss_toString(System.currentTimeMillis()));
        logln("admin: " + isAdmin());

        InputTextBox.newInstance();
    }




    //额外的退出回调
    public static Set<Runnable> exitCallbacks = new LinkedHashSet<>();

    public static void exit() {
        System.out.println("Application.AutoSave.exit()");
        AutoSave.exit();
        System.out.println("Application.AutoSave.exit()");

        System.out.println("ExecutorLock.FILE_LOCK.joins()");
        ExecutorLock.FILE_LOCK.joins();
        System.out.println("ExecutorLock.FILE_LOCK.joins()");

        if (NORMAL_START) {
            System.out.println("normalExit()");
            normalExit();
            System.out.println("normalExit()");

            System.out.println("saveStatus()");
            saveStatus();
            System.out.println("saveStatus()");
        }

        /*
          退出时停止所有进程
          @see GlobalSelenium#exit()
         */
//        GlobalSelenium.stopAll();

        //remove process lock
        try { processLockFileChannel.close(); } catch (Throwable ignored) { }
        try { Files.delete(Paths.get(processLockFile.getPath())); } catch (Throwable ignored) { }


        try {
            for (Runnable exitCallback : exitCallbacks)
                exitCallback.run();
        } catch (Throwable ignored) { }

        System.out.println("exit()");
    }







    public static void hookConsoleKillHandler() {
        new MqKillHandler().registerSignal("TERM");
        new MqKillHandler().registerSignal(System.getProperties().getProperty("os.name").toLowerCase().startsWith("win") ? "INT" : "USR2");
    }
    static class MqKillHandler  implements SignalHandler {
        public MqKillHandler() { }

        /**
         * 注册信号
         */
        public void registerSignal(String signalName) {
            Signal signal = new Signal(signalName);
            Signal.handle(signal, this);
        }
        @Override
        public void handle(Signal signal) {
            // 程序关闭
            try {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }










    public static class Setting {
        public static class ConfigurationKeys extends MyConfigurationKeys {
            public static final String normal_exit = "normal_exit";//是否正常关闭
            public static final String main_size = "main_size";//窗口大小
            public static final String auto_save = "auto_save";//自动保存
            public static final String select = "select";//选择窗口


            public ConfigurationKeys() {
                add(normal_exit);//是否正常关闭
                add(main_size);//窗口大小
                add(auto_save);//自动保存
                add(select);//选择窗口
            }

            static final MyConfigurationKeys CONFIGURATION_KEY = new ConfigurationKeys();
        }
        public static final Configuration settingConfiguration = config.getConfiguration(Setting.class, ConfigurationKeys.CONFIGURATION_KEY);
        public static final Configuration.Element<Integer>      select          = settingConfiguration.createElement(ConfigurationKeys.select, Integer.class);


        public static final Configuration.Element<Boolean>      auto_save       = settingConfiguration.createElement(ConfigurationKeys.auto_save, Boolean.class);
        public static final Configuration.Element<Rectangle>    main_size       = settingConfiguration.createElement(ConfigurationKeys.main_size, Rectangle.class);
        public static final Configuration.Element<Boolean>      normal_exit     = settingConfiguration.createElement(ConfigurationKeys.normal_exit, Boolean.class);
    }

}

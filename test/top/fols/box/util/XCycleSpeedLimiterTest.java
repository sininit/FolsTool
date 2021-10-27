package top.fols.box.util;

import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.os.XFile;
import top.fols.box.io.os.XRandomAccessFileInputStream;
import top.fols.box.io.os.XRandomAccessFileOutputStream;
import top.fols.box.time.XTimeTool;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class XCycleSpeedLimiterTest {
    public static void main(String[] args) throws Throwable {
        long start = XTimeTool.currentTimeMillis();

        XCycleSpeedLimiter xioread = new XCycleSpeedLimiter();
        xioread.setCycleAccessMax(24 * 1024 * 1024);// 限制 每个周期时间内 最多读24M
        xioread.setCycle(500);
        xioread.limit(true);
        XCycleAvgergeSpeedGet xioreadspeed = new XCycleAvgergeSpeedGet();
        xioreadspeed.setCycle(1000);


        System.out.println(xioread.isLimit());
        System.out.println(xioread);
        XFileThreadCopy tread = null;
        for (int i = 0; i < 10; i++) {
            tread = new XFileThreadCopy(
                    "/sdcard/AppProjects/android-22.jar",
                    "/sdcard/" + i + ".test",
                    xioread,
                    xioreadspeed);
            tread.start();
        }
        while (true) {
            if (false)
                break;
            // Thread.sleep(1000);
            // System.out.println("当前FileIO读写取速度:" +
            // XFileTool.FileFormatSize(xioread.getCycleUseSpeedEverySecondMax()) + " /s
            // 剩余:" + XFileTool.FileFormatSize(xioread.getCycleFreeSpeed()));
            // if(xioread.getEverySecondAverageSpeed()>0)
            System.out.println("当前平均速度:" + XFile.fileUnitFormat(xioreadspeed.getAvgergeValue()) + "/S");
        }

        System.out.println();
        System.out.println("\t耗时:" + (new Date().getTime() - start));

    }





    public static class XFileThreadCopy extends Thread {
        private String srcPath;// 原文件地址
        private String destPath;// 目标文件地址
        private long start, end;// start指定起始位置，end指定结束位置
        private XCycleSpeedLimiter Xiolimit = null;
        private XCycleAvgergeSpeedGet XiolimitSpeed;

        // 构造CopyThread方法
        public XFileThreadCopy(String srcPath, String destPath, long start, long end) {
            this(srcPath, destPath, start, end, null, null);
        }
        public XFileThreadCopy(String srcPath, String destPath) {
            this(srcPath, destPath, 0, new File(srcPath).length());
        }

        public XFileThreadCopy(String srcPath, String destPath, XCycleSpeedLimiter xio, XCycleAvgergeSpeedGet xiospeed) {
            this(srcPath, destPath, 0, new File(srcPath).length(), xio, xiospeed);
        }


        public XFileThreadCopy(String srcPath, String destPath, long start, long end, XCycleSpeedLimiter xio, XCycleAvgergeSpeedGet xiospeed) {
            this.srcPath = srcPath;// 要复制的源文件路径
            this.destPath = destPath;// 复制到的文件路径
            this.start = start;// 复制起始位置
            this.end = end;// 复制结束位置
            this.Xiolimit = xio;// 数据流速度限制器
            this.XiolimitSpeed = xiospeed;
        }


        public final static int state_copying = 1;
        public final static int state_copyComplete = 2;
        public final static int state_copyException = 4;
        private double copyPercentage = 0;
        private int state = 0;
        private Exception e;

        public int getCopyState() {
            return state;
        }

        public Exception getCopyException() {
            return e;
        }

        public double getCopyPercentage() {
            return copyPercentage;
        }

        public void run() {
            try {
                state = state_copying;
                copyPercentage = 0;
                e = null;


                long copylength = end - start;

                InputStream in;
                in = new XRandomAccessFileInputStream(srcPath, start);
                in = new XInputStreamFixedLength<InputStream>(in, copylength);
                in = XCycleSpeedLimiter.wrap(in, Xiolimit);

                OutputStream out;
                out = new XRandomAccessFileOutputStream(destPath, start);
                // out = XCycleSpeedLimiter.wrap(out, Xiolimit);

                byte[] buffer = new byte[8192];
                int read = -1;
                long length = 0;
                while (true) {
                    if ((read = in.read(buffer)) == -1)
                        break;
                    this.XiolimitSpeed.access(read);

                    out.write(buffer, 0, read);
                    length += read;
                    copyPercentage = ((double) length / (double) copylength) * 100;
                }
                out.close();// 从里到外关闭文件
                in.close();// 关闭文件
                state = state_copyComplete;
            } catch (Exception e) {
                this.e = e;
                state = state_copyException;
            }
        }
    }



}
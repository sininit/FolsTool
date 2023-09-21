package app.utils;

import top.fols.atri.lang.Value;
import top.fols.box.io.buffer.ByteBufferOutputStream;
import top.fols.box.lang.ProcessExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ProcessUtils {
    public static Charset getDefaultCharset() {
        return new ProcessExecutor().charset();
    }

    /**
     * Note that you need to exit
     * I don't know why it can be read again
     * This is a strange question
     *
     * @param process
     * @param processInputStream
     * @param writeTo
     * @param overtime -1 for unlimited waiting
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean readAsync(
            Process process, InputStream processInputStream
            , OutputStream writeTo
            , long overtime) throws InterruptedException, IOException {
        Value<Boolean> over = new Value<>(false);
        Value<Boolean> exit = new Value<>(false);
        ByteBufferOutputStream outputStream = new ByteBufferOutputStream();
        long start = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        new Thread(() -> {
            while (true)
                try {
                    if (exit.get()) { return; }

                    int read = processInputStream.read();
                    if (read != -1) {
                        outputStream.write(read);
                    } else {
                        if (process.isAlive()) {
                            if (overtime >= 0) {
                                long current = System.currentTimeMillis();
                                if (current - start >= overtime) {
                                    over.set(true);
                                    currentThread.interrupt();//stop
                                    return ;//overtime
                                }
                            }
                        }
                    }
                } catch (Throwable throwable) {
                    try { Thread.sleep(1); } catch (InterruptedException ignore) {}
                }
        }).start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            if (over.get()) {
                throw new InterruptedException("overtime"+"("+overtime+")"+": "+new String(outputStream.toByteArray(), getDefaultCharset()));
            } else throw e;
        }
        exit.set(true);

        writeTo.write(outputStream.toByteArray());
        writeTo.flush();

        return true;
    }

}

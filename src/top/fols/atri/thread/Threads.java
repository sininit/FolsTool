package top.fols.atri.thread;

import top.fols.atri.lang.Value;
import top.fols.atri.lang.Objects.CallbackValue;

public class Threads {
    public static FixedThreadPool fixed_pool(int size) {
        return new FixedThreadPool().setMaxRunningCount(size);
    }



    public static void thread(CallbackValue<Thread> threadCallbackValue) {
        Value<Thread> threadValue = new Value<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadCallbackValue.callback(threadValue.get());
            }
        });
        threadValue.set(thread);
        thread.start();
    }


}

package top.fols.box.time;

import top.fols.box.lang.Result;

import java.util.concurrent.Callable;

public class Trials {


    @SuppressWarnings({"BusyWait", "StatementWithEmptyBody", "MethodDoesntCallSuperMethod"})
    public static class Try<V> {
        public interface Executor<V> {
            V apply(Controller<V> execute) throws Throwable;
        }

        public Try(long overtime, long sleep) {
            this.overtime = overtime;
            this.sleep    = sleep;
        }

        private final long     overtime;
        private final long     sleep;

        @Override
        public Try<V> clone() {
            Try<V> clone;
            clone = new Try<>(overtime, sleep);
            return  clone;
        }


        public static class Controller<V> {
            public Controller(long sleep) {
                this.sleep = sleep;
            }

            private long          sleep;
            private long          endTime;


            private boolean stop, pass;
            public V pass()                          { this.pass = true;         return null; }
            public V stop()                          { this.stop = true;         return null; }
            private void init() {
                pass = false;
                stop = false;
            }


            public long     sleep()      {
                return this.sleep;
            }
            public void     sleep(long sleep)      {
                this.sleep = sleep;
            }

            public long     endTime()      {
                return this.endTime;
            }
            public void     endTime(long endTime)      {
                this.endTime = endTime;
            }
        }

        public Result<V, Throwable> where(Executor<V> executor) throws InterruptedException {
            Controller<V> controllers   = new Controller<>(sleep);
            Result<V, Throwable> result = new Result<>();
            A: if (overtime <= 0) {
                try {
                    controllers.init();
                    V v = executor.apply(controllers);
                    if (controllers.stop) { break A; }
                    result.set(v);
                } catch (Throwable e) {
                    result.setError(e);
                }
            } else {
                controllers.endTime = System.currentTimeMillis() + (overtime);
                while (true) {
                    try {
                        controllers.init();
                        V v = executor.apply(controllers);
                        if (controllers.stop) { break A; }
                        if (controllers.pass) {
                        } else {
                            result.set(v);
                            break;
                        }
                    } catch (Throwable e) {
                        result.setError(e);
                    }

                    if (System.currentTimeMillis() >= controllers.endTime)  { break; } else { Thread.sleep(controllers.sleep); }
                }
            }
            return result;
        }
    }
    public static <V> V trial(long overtime, long sleep, Try.Executor<V> executor) throws InterruptedException {
        return new Try<V>(overtime, sleep).where(executor).get();
    }







    @SuppressWarnings({"StatementWithEmptyBody", "MethodDoesntCallSuperMethod"})
    public static class TryFrequency<V> {
        public interface Executor<V> {
            V apply(Controller<V> execute) throws Throwable;
        }

        public TryFrequency(long frequency) {
            this.frequency = frequency;
        }

        private final long     frequency;


        @Override
        public TryFrequency<V> clone() {
            TryFrequency<V> clone;
            clone = new TryFrequency<>(frequency);
            return  clone;
        }





        public static class Controller<V> {
            public Controller(long frequency) {
                this.frequency = frequency;
            }

            private long          frequency;
            private boolean stop, pass;

            public V pass()                          { this.pass = true;         return null; }
            public V stop()                          { this.stop = true;         return null; }

            private void init() {
                pass = false;
                stop = false;
            }

            long current = 0;
            public long current() {
                return current;
            }
            public void     frequency(long frequency)      {
                this.frequency = frequency;
            }
        }

        public Result<V, Throwable> where(Executor<V> executor) throws InterruptedException {
            Controller<V> controllers   = new Controller<>(frequency);
            Result<V, Throwable> result = new Result<>();
            controllers.current = 1;
            while (true) {
                try {
                    controllers.init();
                    V v = executor.apply(controllers);
                    if (controllers.stop) { break; }
                    if (controllers.pass) {
                    } else {
                        result.set(v);
                        break;
                    }
                } catch (Throwable e) {
                    result.setError(e);
                }

                if (controllers.current++ >= controllers.frequency)  { break; }
            }
            return result;
        }
    }
    public static <V> V trialFrequency(long frequency, TryFrequency.Executor<V> executor) throws InterruptedException {
        return new TryFrequency<V>(frequency).where(executor).get();
    }







    public static <T> T execute(Callable<T> executor) throws RuntimeException {
        try {
            return executor.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 如果正常执行则直接返回 否则抛出最后一个异常
     */
    public static <T> T execute(int num, Callable<T> executor) throws RuntimeException {
        if (null == executor)
            throw new NullPointerException("executor");
        if (num <= 0)
            throw new IllegalArgumentException("num <= 0");
        Throwable last = null;
        for (int i = 0; i < num; i++) {
            try {
                return executor.call();
            } catch (Throwable e) {
                last = e;
            }
        }
        if (last instanceof RuntimeException)
            throw (RuntimeException) last;
        throw new RuntimeException(last);
    }
    public static <T> T execute(long time, long sleep, Callable<T> executor) throws RuntimeException, InterruptedException {
        if (null == executor)
            throw new NullPointerException("executor");
        if (time <= 0)
            throw new IllegalArgumentException("time <= 0");
        if (sleep <= 0)
            sleep = 0;
        Throwable last = null;
        for (long current = System.currentTimeMillis(), overtime = current + time; System.currentTimeMillis() < overtime;) {
            try {
                return executor.call();
            } catch (Throwable e) {
                last = e;
            }
            Thread.sleep(sleep);
        }
        if (last instanceof RuntimeException)
            throw (RuntimeException) last;
        throw new RuntimeException(last);
    }




    @SuppressWarnings("UnnecessaryLocalVariable")
    public static <V> V trial(Callable<V> executor) {
        try {
            V execute = executor.call();
            return execute;
        } catch (Throwable e) {
            return null;
        }
    }
    public static void trial(Runnable executor) {
        try {
            executor.run();
        } catch (Throwable ignored) {}
    }

}

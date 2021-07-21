package top.fols.atri.time;

import top.fols.atri.lang.Result;

public class Times {



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
            private boolean stop, pass;

            public V pass()                          { this.pass = true;         return null; }
            public V stop()                          { this.stop = true;         return null; }

            private void init() {
                pass = false;
                stop = false;
            }

            private boolean ignoredInterruptedException = false;
            public void     ignoredInterruptedException(boolean ignoredInterruptedException) {
                this.ignoredInterruptedException = ignoredInterruptedException;
            }
            public boolean  ignoredInterruptedException() {
                return ignoredInterruptedException;
            }


            public void     sleep(long sleep)      {
                this.sleep = sleep;
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
                } catch (InterruptedException stop) {
                    result.setError(stop);
                    if (!controllers.ignoredInterruptedException) {
                        throw stop;
                    }
                } catch (Throwable e) {
                    result.setError(e);
                }
            } else {
                long endTime = System.currentTimeMillis() + (overtime);
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
                    } catch (InterruptedException stop) {
                        result.setError(stop);
                        if (!controllers.ignoredInterruptedException) {
                            throw stop;
                        }
                    } catch (Throwable e) {
                        result.setError(e);
                    }

                    if (System.currentTimeMillis() >= endTime)  { break; } else { Thread.sleep(controllers.sleep); }
                }
            }
            return result;
        }
    }
    public static <V> V where(long overtime, long sleep, Try.Executor<V> executor) throws InterruptedException {
        return new Try<V>(overtime, sleep).where(executor).get();
    }
}

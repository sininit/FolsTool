package top.fols.atri.cache;

import top.fols.atri.interfaces.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 每次都可以生成同样的缓存
 * 并且缓存可以随时被清理
 * 并且不会出现线程不安全的情况
 */
@SuppressWarnings({"SpellCheckingInspection"})
public class FinalizeRegenerableCaches {


    /**
     * 如果有程序短时间申请大量的内存空间则他会延时
     */
    public static abstract class GCOver {
        public void putCacheBefore() {
            if (reference.get() == null) {
                reference = new WeakReference<>(new Finalize());
            }
        }

        public abstract void release();

        WeakReference<Finalize> reference = new WeakReference<>(new Finalize());

        class Finalize {
            @Override
            protected void finalize() {
                gcCallback();
            }
        }


        //auto gc


        protected static final long GC_RELEASE_INTERVAL_TIME_UNLIMITED = 0;
        /**
         * If the cache can be regenerated randomly, we can use GC to clean it up automatically,
         * Release all data each time GC is triggered
         * Two consecutive releases shall not be less than this time
         * use GC_RELEASE_INTERVAL_TIME = 500L ?
         */
        protected long GC_RELEASE_INTERVAL_TIME = GC_RELEASE_INTERVAL_TIME_UNLIMITED;
        protected long gcLastTime;

        protected void setGcReleaseIntervalTime(long time) {
            GC_RELEASE_INTERVAL_TIME = time;
        }
        protected long getGcReleaseIntervalTime() {
            return GC_RELEASE_INTERVAL_TIME;
        }

        /**
         * defalut is time
         */
        protected void gcCallback() {
            if (GC_RELEASE_INTERVAL_TIME_UNLIMITED >= GC_RELEASE_INTERVAL_TIME) { //0 >= ?
                release();
            } else {
                synchronized (this) {
                    long l;
                    if ((l = System.currentTimeMillis()) - gcLastTime > GC_RELEASE_INTERVAL_TIME) {
                        release();
                        gcLastTime = l;
                    }
                }
            }
        }
    }


}

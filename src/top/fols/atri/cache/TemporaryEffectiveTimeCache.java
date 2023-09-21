package top.fols.atri.cache;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IReleasable;

/**
 * get a new cache every valid time
 */
@ThreadSafe
public abstract class TemporaryEffectiveTimeCache<T, Ex extends Throwable> implements IReleasable {

	long effectiveTime;
	public void setEffectiveTime(long time) {
		if (time < 0) {
			time = 0;
		}
		this.effectiveTime = time;
	}

	public TemporaryEffectiveTimeCache(long effectiveTime) {
		this.setEffectiveTime(effectiveTime);
	}

	long lastAccessTime;
	T    lastCache;

	public abstract T createCache() throws Ex;

	public T getOrCreateCache() throws Ex {
		long time = System.currentTimeMillis();
		if  (time - lastAccessTime > effectiveTime) {
			this.lastAccessTime = time;
			return this.lastCache = createCache();
		} else {
			return this.lastCache;
		}
	}
	@Override
	public boolean release() {
		lastAccessTime = 0;
		lastCache = null;
		return true;
	}
	@Override
	public boolean released() {
		return  lastAccessTime == 0 &&
				lastCache == null;
	}

}


package top.fols.atri.cache;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.interfaces.IReleasable;

@SuppressWarnings({"UnusedReturnValue", "PointlessBooleanExpression"})
@ThreadSafe
public abstract class TemporaryChangedCache<T, Ex extends Throwable> implements IReleasable {
	public void 	setChanged() {
		lastAccessed = false;
	}
	public boolean isChanged() {
		return !(lastAccessed);
	}

	public abstract T createCache() throws Ex;

	boolean lastAccessed;
	T       lastCache;
	public T getOrCreateCache() throws Ex {
		if (lastAccessed) {
			return lastCache;
		} else {
			T cache = createCache();
			this.lastCache    = cache;
			this.lastAccessed = true;
			return cache;
		}
	}
	@Override
	public boolean release() {
		lastAccessed = false;
		lastCache = null;
		return true;
	}
	@Override
	public boolean released() {
		return  lastAccessed == false &&
				lastCache == null;
	}

	public TemporaryChangedCache() {
		this.setChanged();
	}
}

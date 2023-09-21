package top.fols.atri.cache;

@SuppressWarnings("UnusedReturnValue")
public abstract class TemporaryModifyCache<T, Ex extends Throwable> {
	long modCount = 0;
	public long modCountAdd() { return ++this.modCount; }
	public long modCount()    { return modCount;        }

	public boolean isChanged(long modCount) {
		return modCount() != modCount;
	}

	public abstract T createCache() throws Ex;

	long lastModCount;
	T    lastCache;
	public T getOrCreateCache() throws Ex {
		long currentMod = this.modCount;
		if  (currentMod != lastModCount) {
			this.lastModCount = currentMod;
			return this.lastCache = createCache();
		} else {
			return this.lastCache;
		}
	}

	public TemporaryModifyCache() {
		this.modCountAdd();
	}
}

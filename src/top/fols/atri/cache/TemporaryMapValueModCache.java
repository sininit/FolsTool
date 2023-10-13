package top.fols.atri.cache;

import top.fols.atri.interfaces.annotations.ThreadSafe;
import top.fols.atri.interfaces.annotations.Private;
import top.fols.atri.interfaces.interfaces.IReleasable;

@ThreadSafe
public abstract class TemporaryMapValueModCache<K, V, Ex extends Throwable> {

	@Private
	transient long mod;
	
	public void addMod() { mod ++; }

	public abstract V fromMapGetValue(K k) throws Ex;


	public static abstract class Updated<K, V, Ex extends Throwable> {
		public abstract void updated(K k, V v) throws Ex;
	}

	public ValueGetter createValueGetter(K k) {
		return new ValueGetter(k);
	}
	public class ValueGetter implements IReleasable {
		@Private
		@Override
		public boolean release() {
			// TODO: Implement this method
			this.lastMod = (TemporaryMapValueModCache.this.mod - 1);
			this.v = null;
			return true;
		}

		@Override
		public boolean released() {
			// TODO: Implement this method
			return this.lastMod != TemporaryMapValueModCache.this.mod &&
			       this.v == null;
		}
		
		
		@Private
		long lastMod = (TemporaryMapValueModCache.this.mod - 1);

		final K k;
		V v;

		ValueGetter(K k) {
			this.k = k;
		}

		public V getValue() throws Ex {
			if (this.lastMod == TemporaryMapValueModCache.this.mod) {
				return v;
			} else {
				this.lastMod = TemporaryMapValueModCache.this.mod;
				this.v = TemporaryMapValueModCache.this.fromMapGetValue(k);
				return v;
			}
		}

		public V getValue(Updated<K,V,Ex> updated) throws Ex {
			if (this.lastMod == TemporaryMapValueModCache.this.mod) {
				return v;
			} else {
				this.lastMod = TemporaryMapValueModCache.this.mod;
				this.v = TemporaryMapValueModCache.this.fromMapGetValue(k);
				if (null != updated)
					updated.updated(k, v);
				return v;
			}
		}
	}

}

package top.fols.atri.io;

import java.io.Closeable;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import top.fols.atri.lang.Objects;
import top.fols.atri.util.Releasable;

public class StreamList implements Releasable {

	@Override
	public boolean release() {
		// TODO: Implement this method
		synchronized (lock) {
			objects = null;
		}
		return true;
	}

	@Override
	public boolean released() {
		// TODO: Implement this method
		return (null == objects);
	}



	static Closeable pack(final URLConnection instance) {
		return new Closeable() {
			@Override
			public void close() {
				// TODO: Implement this method
				Streams.close(instance);
			}
		};
	}



	private final Object lock = new Object();

	private Map<Object, Closeable> objects = new IdentityHashMap<>();





	public <T extends Closeable> T add(T object) {
		if (null == object) { return null; }
		synchronized (lock) {
			Objects.requireFalse(this.released(), "released");

			this.objects.put(object, object);
			return object;
		}
	}
	public <T extends URLConnection> T add(T object) {
		if (null == object) { return null; }
		synchronized (lock) {
			Objects.requireFalse(this.released(), "released");

			this.objects.put(object, pack(object));
			return object;
		}
	}


	public <T extends Closeable> boolean remove(T object) {
		synchronized (lock) {
			return null != this.objects.remove(object);
		}
	}
	public <T extends URLConnection> boolean remove(T object) {
		synchronized (lock) {
			return null != this.objects.remove(object);
		}
	}

	public <T extends Closeable> boolean close(T object) {
		synchronized (lock) {
			Closeable closeable = this.objects.remove(object);
			Streams.close(closeable);
			return null != closeable;
		}
	}
	public <T extends URLConnection> boolean close(T object) {
		synchronized (lock) {
			Closeable closeable = this.objects.remove(object);
			Streams.close(closeable);
			return null != closeable;
		}
	}





	public boolean contains(Object object) {
		synchronized (lock) {
			return this.objects.containsKey(object);
		}
	}
	public StreamList close() {
		synchronized (lock) {
			for (Object key: new ArrayList<>(this.objects.keySet())) {
				Closeable closeable = this.objects.remove(key);
				Streams.close(closeable);
			}
			this.objects.clear();
		}
		return this;
	}

}

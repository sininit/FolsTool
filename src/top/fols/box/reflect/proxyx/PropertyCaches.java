package top.fols.box.reflect.proxyx;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import top.fols.atri.reflect.Reflects;

public class PropertyCaches {
	protected Map<Object, Object> values = new ConcurrentHashMap<>();
	protected Map<Object, Object> getInnerMap() { return values; }
	
	public <T> T newInstance(Class<T> type) {
		return Reflects.newInstance(type);
	}
	 
	
	
	
	public <T> T getValue(Property<T> p) {
		Map<Object, Object> map = getInnerMap();
		Class<T> type = p.getType();
		T   value = (T) map.get(type);
		if (value == null && !map.containsKey(type)) {
			Initializer<T> initializer = p.getInitializer();
			map.put(type, value = initializer.initialize());
		}
		return value;
	}
	
	public <T> Property<T> newProperty(Class<T> type) { 
		return newProperty(type, null); 
	}
	public <T> Property<T> newProperty(final Class<T> type, Initializer<T> initializer) { 
		return new Property<T>(type, null != initializer ? initializer :
						   new Initializer<T>() {
							   @Override
							   public T initialize() {
								   // TODO: Implement this method
								   return newInstance(type);
							   }
						   }); 
	}
	

	public static interface Initializer<T> {
		public T initialize();
	}
	
	public static class Property<T> {
		private Class<T> type;
		private Initializer<T> initializer;

		Property(Class<T> type, Initializer<T> initializer) {
			this.type = Objects.requireNonNull(type, "type");
			this.initializer = Objects.requireNonNull(initializer, "initializer");
		}

		public    Class<T>       getType() { return type; }
		protected Initializer<T> getInitializer() { return initializer; }
		
		public T getValue(PropertyCaches master)  { return master.getValue(this); }
	}
}

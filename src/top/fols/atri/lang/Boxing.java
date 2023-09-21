package top.fols.atri.lang;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.ArrayList;

public class Boxing {

	static class Mapping {
		Class<?> k;
		Class<?> v;

		public Mapping(Class<?> origin, Class<?> correspond) {
			this.k = origin;
			this.v = correspond;
		}


		static Class<?>[] keyArray(Map<Class<?>, Class<?>> map) {
			Class<?>[] array = new Class<?>[map.size()];
			int i = 0;

			for (Class<?>  key: map.keySet()) {
				array[i] = key;
				i++;
			}
			return array;
		}
		static Class<?>[] keyArray(Mapping[] map) {
			Class<?>[] array = new Class<?>[map.length];
			int i = 0;

			for (Mapping  key: map) {
				array[i] = key.k;
				i++;
			}
			return array;
		}



		static Mapping[] toArray(Map<Class<?>, Class<?>> map) {
			Mapping[] array = new Mapping[map.size()];
			int i = 0;

			for (Class<?> key: map.keySet()) {
				Class<?> value = map.get(key);

				Mapping element = new Mapping(key, value);
				array[i] = element;
				i++;
			}
			return array;
		}
		static Map<Class<?>, Class<?>> toMap(Mapping[] arrays) {
			Map<Class<?>, Class<?>> instance = new IdentityHashMap<Class<?>, Class<?>>();
			for (Mapping element: arrays) {
				instance.put(element.k, element.v);
			}
			return instance;
		}


		static Map<Class<?>, Class<?>> reverseKV(Map<Class<?>, Class<?>> map) {
			Map<Class<?>, Class<?>> instance = new IdentityHashMap<Class<?>, Class<?>>();
			for (Class<?> key   : map.keySet()) {
				Class<?> value = map.get(key);
				instance.put(value, key);
			}
			return instance;
		}
		static Mapping[] reverseKV(Mapping[] map) {
			Mapping[] array = map.clone();
			for (Mapping element: array) {
				Class<?>
					key = element.k, 
					value = element.v;
				element.k = value;
				element.v = key;
			}
			return array;
		}
	}



	
	static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAPPING = new IdentityHashMap<Class<?>, Class<?>>() {{//不会增量
			put(boolean.class, 		Boolean.class);

			put(byte.class,    		Byte.class);
			put(char.class,    		Character.class);

			put(double.class,  		Double.class);
			put(float.class,   		Float.class);

			put(short.class,   		Short.class);
			put(long.class,         Long.class);
			put(int.class,      	Integer.class);
		}};
	static final Class<?>[] PRIMITIVE_TYPE_ALL      = Mapping.keyArray(PRIMITIVE_TYPE_MAPPING);
	
	
	//**
	public static Class<?> toWrapperType(Class<?> type)	{
		return Boxing.PRIMITIVE_TYPE_MAPPING.get(type); 
	}

	public static Class<Boolean> toWrapperType(boolean object) 			{ return Finals.BOOLEAN_PACKAGE_CLASS; }

	public static Class<Byte> toWrapperType(byte object) 				{ return Finals.BYTE_PACKAGE_CLASS; }
	public static Class<Character> toWrapperType(char object) 			{ return Finals.CHAR_PACKAGE_CLASS; }

	public static Class<Double> toWrapperType(double object) 			{ return Finals.DOUBLE_PACKAGE_CLASS; }
	public static Class<Float> toWrapperType(float object) 				{ return Finals.FLOAT_PACKAGE_CLASS; }

	public static Class<Short> toWrapperType(short object) 				{ return Finals.SHORT_PACKAGE_CLASS; }
	public static Class<Long> toWrapperType(long object) 				{ return Finals.LONG_PACKAGE_CLASS; }
	public static Class<Integer> toWrapperType(int object) 				{ return Finals.INT_PACKAGE_CLASS; }


	//**
	public static boolean isWrapperType(Class<?> object) {
		return
				! (null == object) &&
				! (null == Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.get(object));
	}
	//**
	public static boolean isWrapperType(Object object) {
		return
				! (null == object) && (Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.containsKey(object.getClass()));
	}
	public static boolean isWrapperType(boolean object) 	{ return false; }

	public static boolean isWrapperType(byte object) 		{ return false; }
	public static boolean isWrapperType(char object) 		{ return false; }

	public static boolean isWrapperType(double object) 		{ return false; }
	public static boolean isWrapperType(float object) 		{ return false; }

	public static boolean isWrapperType(short object) 		{ return false; }
	public static boolean isWrapperType(long object) 		{ return false; }
	public static boolean isWrapperType(int object) 		{ return false; }



	public static boolean isWrapperType(Boolean object) 	{ return !(null == object); }

	public static boolean isWrapperType(Byte object) 		{ return !(null == object); }
	public static boolean isWrapperType(Character object) 	{ return !(null == object); }

	public static boolean isWrapperType(Double object) 		{ return !(null == object); }
	public static boolean isWrapperType(Float object) 		{ return !(null == object); }

	public static boolean isWrapperType(Short object) 		{ return !(null == object); }
	public static boolean isWrapperType(Long object) 		{ return !(null == object); }
	public static boolean isWrapperType(Integer object) 	{ return !(null == object); }

	public static Class<?>[] listWrapperType() {
		return Boxing.PRIMITIVE_BOXING_TYPE_CLASS.clone();
	}







	static final Map<Class<?>, Class<?>> PRIMITIVE_BOXING_TYPE_MAPPING = Mapping.reverseKV(PRIMITIVE_TYPE_MAPPING);
	static final Class<?>[] 			 PRIMITIVE_BOXING_TYPE_CLASS   = Mapping.keyArray(PRIMITIVE_BOXING_TYPE_MAPPING);
	


	//**
	public static Class<?> toPrimitiveType(Class<?> type) {
		return Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.get(type);
	}
	//**
	public static Class<?> toPrimitiveType(Object type) {
		return null == type ?null: Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.get(type.getClass());
	}

	public static Class<?> toPrimitiveType(boolean object) 	{ return null; }

	public static Class<?> toPrimitiveType(byte object) 		{ return null; }
	public static Class<?> toPrimitiveType(char object) 		{ return null; }

	public static Class<?> toPrimitiveType(double object) 	{ return null; }
	public static Class<?> toPrimitiveType(float object) 	{ return null; }

	public static Class<?> toPrimitiveType(short object) 	{ return null; }
	public static Class<?> toPrimitiveType(long object) 		{ return null; }
	public static Class<?> toPrimitiveType(int object) 		{ return null; }



	public static Class<?> toPrimitiveType(Boolean object) 	{ return (null == object) ?null: Finals.BOOLEAN_CLASS; }

	public static Class<?> toPrimitiveType(Byte object) 		{ return (null == object) ?null: Finals.BYTE_CLASS; }
	public static Class<?> toPrimitiveType(Character object) { return (null == object) ?null: Finals.CHAR_CLASS; }

	public static Class<?> toPrimitiveType(Double object) 	{ return (null == object) ?null: Finals.DOUBLE_CLASS; }
	public static Class<?> toPrimitiveType(Float object) 	{ return (null == object) ?null: Finals.FLOAT_CLASS; }

	public static Class<?> toPrimitiveType(Short object) 	{ return (null == object) ?null: Finals.SHORT_CLASS; }
	public static Class<?> toPrimitiveType(Long object) 		{ return (null == object) ?null: Finals.LONG_CLASS; }
	public static Class<?> toPrimitiveType(Integer object) 	{ return (null == object) ?null: Finals.INT_CLASS; }




	public static boolean isPrimitiveType(Class<?> object) {
		if (null == object) { return false; }
		return object.isPrimitive();
	}
	public static boolean isPrimitiveType(Object object) 	{ return false; /* auto boxing */}

	public static boolean isPrimitiveType(boolean object) 	{ return true; }

	public static boolean isPrimitiveType(byte object) 		{ return true; }
	public static boolean isPrimitiveType(char object) 		{ return true; }

	public static boolean isPrimitiveType(double object) 	{ return true; }
	public static boolean isPrimitiveType(float object) 	{ return true; }

	public static boolean isPrimitiveType(short object) 	{ return true; }
	public static boolean isPrimitiveType(long object) 		{ return true; }
	public static boolean isPrimitiveType(int object) 		{ return true; }


	public static Class<?>[] listPrimitiveType() {
		return Boxing.PRIMITIVE_TYPE_ALL.clone();
	}







	/**
	 * boxing type or primitive type
	 */
	public static boolean isPrimitiveTypeOrWrapperType(Class<?> object) {
		return !(null == object) && (object.isPrimitive() || isWrapperType(object));
	}

	public static boolean isPrimitiveTypeOrWrapperType(Object object) {
		return !(null == object) && (isWrapperType(object));
	}

	public static boolean isPrimitiveTypeOrWrapperType(boolean object) 	{ return true; }

	public static boolean isPrimitiveTypeOrWrapperType(byte object) 		{ return true; }
	public static boolean isPrimitiveTypeOrWrapperType(char object) 		{ return true; }

	public static boolean isPrimitiveTypeOrWrapperType(double object) 	{ return true; }
	public static boolean isPrimitiveTypeOrWrapperType(float object) 	{ return true; }

	public static boolean isPrimitiveTypeOrWrapperType(short object) 	{ return true; }
	public static boolean isPrimitiveTypeOrWrapperType(long object) 		{ return true; }
	public static boolean isPrimitiveTypeOrWrapperType(int object) 		{ return true; }


	public static boolean isPrimitiveTypeOrWrapperType(Boolean object) 	{ return !(null == object); }

	public static boolean isPrimitiveTypeOrWrapperType(Byte object) 		{ return !(null == object); }
	public static boolean isPrimitiveTypeOrWrapperType(Character object) 	{ return !(null == object); }

	public static boolean isPrimitiveTypeOrWrapperType(Double object) 		{ return !(null == object); }
	public static boolean isPrimitiveTypeOrWrapperType(Float object) 		{ return !(null == object); }

	public static boolean isPrimitiveTypeOrWrapperType(Short object) 		{ return !(null == object); }
	public static boolean isPrimitiveTypeOrWrapperType(Long object) 		{ return !(null == object); }
	public static boolean isPrimitiveTypeOrWrapperType(Integer object) 	{ return !(null == object); }

	
	
	
	
	
	
	
	
	
	
	
	static final Map<Class<?>, Object> DEFAULT_VALUE = new IdentityHashMap<Class<?>, Object>() {{//不会增量
			put(boolean.class, 		false);

			put(byte.class,    		(byte)0);
			put(char.class,    		(char)0);

			put(double.class,  		(double)0);
			put(float.class,   		(float)0);

			put(short.class,   		(short)0);
			put(long.class,         (long)0);
			put(int.class,      	(Integer)0);
		}};
	static {
		for (Class<?> type: new ArrayList<>(DEFAULT_VALUE.keySet())) {
			DEFAULT_VALUE.put(toWrapperType(type), DEFAULT_VALUE.get(type));
		}
	}
	
	public static Object getDefaultValue(Class<?> type) {
		return DEFAULT_VALUE.get(type);
	}
	
}

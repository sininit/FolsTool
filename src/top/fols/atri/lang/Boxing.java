package top.fols.atri.lang;

import java.util.IdentityHashMap;
import java.util.Map;
import top.fols.atri.lang.Finals;
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



	
	static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAPPING = new IdentityHashMap<Class<?>, Class<?>>() {{
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
	public static Class<?> 			getPrimitiveTypeBoxingType(Class<?> type)				{ 
		return Boxing.PRIMITIVE_TYPE_MAPPING.get(type); 
	}

	public static Class<?> 			getPrimitiveTypeBoxingType(Object object) 				{ return null; }

	public static Class<Boolean> 	getPrimitiveTypeBoxingType(boolean object) 				{ return Finals.BOOLEAN_PACKAGE_CLASS; }

	public static Class<Byte> 		getPrimitiveTypeBoxingType(byte object) 				{ return Finals.BYTE_PACKAGE_CLASS; }
	public static Class<Character> 	getPrimitiveTypeBoxingType(char object) 				{ return Finals.CHAR_PACKAGE_CLASS; }

	public static Class<Double> 	getPrimitiveTypeBoxingType(double object) 				{ return Finals.DOUBLE_PACKAGE_CLASS; }
	public static Class<Float> 		getPrimitiveTypeBoxingType(float object) 				{ return Finals.FLOAT_PACKAGE_CLASS; }

	public static Class<Short> 		getPrimitiveTypeBoxingType(short object) 				{ return Finals.SHORT_PACKAGE_CLASS; }
	public static Class<Long> 		getPrimitiveTypeBoxingType(long object) 				{ return Finals.LONG_PACKAGE_CLASS; }
	public static Class<Integer> 	getPrimitiveTypeBoxingType(int object) 					{ return Finals.INT_PACKAGE_CLASS; }




	public static boolean isPrimitive(Class<?> object) {
		if (null == object) { return false; }
		return object.getClass().isPrimitive();
	}
	public static boolean isPrimitive(Object object) 	{ return false; /* auto boxing */}

	public static boolean isPrimitive(boolean object) 	{ return true; }

	public static boolean isPrimitive(byte object) 		{ return true; }
	public static boolean isPrimitive(char object) 		{ return true; }

	public static boolean isPrimitive(double object) 	{ return true; }
	public static boolean isPrimitive(float object) 	{ return true; }

	public static boolean isPrimitive(short object) 	{ return true; }
	public static boolean isPrimitive(long object) 		{ return true; }
	public static boolean isPrimitive(int object) 		{ return true; }


	public static Class<?>[] listPrimitive() {
		return Boxing.PRIMITIVE_TYPE_ALL.clone();
	}





	static final Map<Class<?>, Class<?>> PRIMITIVE_BOXING_TYPE_MAPPING = Mapping.reverseKV(PRIMITIVE_TYPE_MAPPING);
	static final Class<?>[] 			 PRIMITIVE_BOXING_TYPE_CLASS   = Mapping.keyArray(PRIMITIVE_BOXING_TYPE_MAPPING);
	


	//**
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Class<?> type) {
		return Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.get(type);
	}
	//**
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Object type) {
		return null == type ?null: Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.get(type.getClass());
	}

	public static Class<?> getPrimitiveBoxingTypeUnboxingType(boolean object) 	{ return null; }

	public static Class<?> getPrimitiveBoxingTypeUnboxingType(byte object) 		{ return null; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(char object) 		{ return null; }

	public static Class<?> getPrimitiveBoxingTypeUnboxingType(double object) 	{ return null; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(float object) 	{ return null; }

	public static Class<?> getPrimitiveBoxingTypeUnboxingType(short object) 	{ return null; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(long object) 		{ return null; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(int object) 		{ return null; }



	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Boolean object) 	{ return (null == object) ?null: Finals.BOOLEAN_CLASS; }

	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Byte object) 		{ return (null == object) ?null: Finals.BYTE_CLASS; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Character object) { return (null == object) ?null: Finals.CHAR_CLASS; }

	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Double object) 	{ return (null == object) ?null: Finals.DOUBLE_CLASS; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Float object) 	{ return (null == object) ?null: Finals.FLOAT_CLASS; }

	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Short object) 	{ return (null == object) ?null: Finals.SHORT_CLASS; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Long object) 		{ return (null == object) ?null: Finals.LONG_CLASS; }
	public static Class<?> getPrimitiveBoxingTypeUnboxingType(Integer object) 	{ return (null == object) ?null: Finals.INT_CLASS; }





	//**
	public static boolean isPrimitiveBoxing(Class<?> object) {
		return 
			! (null == object) &&
			! (null == Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.get(object));
	}
	//**
	public static boolean isPrimitiveBoxing(Object object) {
		return 
			! (null == object) && (Boxing.PRIMITIVE_BOXING_TYPE_MAPPING.containsKey(object.getClass()));
	}
	public static boolean isPrimitiveBoxing(boolean object) 	{ return false; }

	public static boolean isPrimitiveBoxing(byte object) 		{ return false; }
	public static boolean isPrimitiveBoxing(char object) 		{ return false; }

	public static boolean isPrimitiveBoxing(double object) 		{ return false; }
	public static boolean isPrimitiveBoxing(float object) 		{ return false; }

	public static boolean isPrimitiveBoxing(short object) 		{ return false; }
	public static boolean isPrimitiveBoxing(long object) 		{ return false; }
	public static boolean isPrimitiveBoxing(int object) 		{ return false; }



	public static boolean isPrimitiveBoxing(Boolean object) 	{ return !(null == object); }

	public static boolean isPrimitiveBoxing(Byte object) 		{ return !(null == object); }
	public static boolean isPrimitiveBoxing(Character object) 	{ return !(null == object); }

	public static boolean isPrimitiveBoxing(Double object) 		{ return !(null == object); }
	public static boolean isPrimitiveBoxing(Float object) 		{ return !(null == object); }

	public static boolean isPrimitiveBoxing(Short object) 		{ return !(null == object); }
	public static boolean isPrimitiveBoxing(Long object) 		{ return !(null == object); }
	public static boolean isPrimitiveBoxing(Integer object) 	{ return !(null == object); }



	public static Class<?>[] listPrimitiveBoxing() {
		return Boxing.PRIMITIVE_BOXING_TYPE_CLASS.clone();
	}








	/**
	 * boxing type or primitive type
	 */
	public static boolean isPrimitiveOrBoxing(Class<?> object) {
		return !(null == object) && (object.isPrimitive() || isPrimitiveBoxing(object));
	}

	public static boolean isPrimitiveOrBoxing(Object object) {
		return !(null == object) && (isPrimitiveBoxing(object));
	}

	public static boolean isPrimitiveOrBoxing(boolean object) 	{ return true; }

	public static boolean isPrimitiveOrBoxing(byte object) 		{ return true; }
	public static boolean isPrimitiveOrBoxing(char object) 		{ return true; }

	public static boolean isPrimitiveOrBoxing(double object) 	{ return true; }
	public static boolean isPrimitiveOrBoxing(float object) 	{ return true; }

	public static boolean isPrimitiveOrBoxing(short object) 	{ return true; }
	public static boolean isPrimitiveOrBoxing(long object) 		{ return true; }
	public static boolean isPrimitiveOrBoxing(int object) 		{ return true; }



	public static boolean isPrimitiveOrBoxing(Boolean object) 		{ return !(null == object); }

	public static boolean isPrimitiveOrBoxing(Byte object) 			{ return !(null == object); }
	public static boolean isPrimitiveOrBoxing(Character object) 	{ return !(null == object); }

	public static boolean isPrimitiveOrBoxing(Double object) 		{ return !(null == object); }
	public static boolean isPrimitiveOrBoxing(Float object) 		{ return !(null == object); }

	public static boolean isPrimitiveOrBoxing(Short object) 		{ return !(null == object); }
	public static boolean isPrimitiveOrBoxing(Long object) 			{ return !(null == object); }
	public static boolean isPrimitiveOrBoxing(Integer object) 		{ return !(null == object); }

	
	
	
	
	
	
	
	
	
	
	
	static final Map<Class<?>, Object> DEFAULT_VALUE = new IdentityHashMap<Class<?>, Object>() {{
			put(boolean.class, 		false);

			put(byte.class,    		(byte)0);
			put(char.class,    		(char)0);

			put(double.class,  		(double)0);
			put(float.class,   		(float)0);

			put(short.class,   		(short)0);
			put(long.class,         (long)0);
			put(int.class,      	(Integer)0);
		}};
	static{
		for (Class<?> type: new ArrayList<>(DEFAULT_VALUE.keySet())) {
			DEFAULT_VALUE.put(getPrimitiveTypeBoxingType(type), DEFAULT_VALUE.get(type));
		}
	}
	
	public static Object getDefault(Class<?> type) {
		return DEFAULT_VALUE.get(type);
	}
	
}

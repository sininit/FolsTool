package top.fols.box.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import top.fols.atri.assist.util.StringJoiner;
import top.fols.atri.cache.WeakMapCache;
import top.fols.atri.cache.WrapValue;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Finals;
import top.fols.box.util.DataSequences;
import top.fols.box.util.EntitySpace;

/**
 * can read generic declarations written by normal people
 //	1.Class（原始类型）
 //	2.GenericArrayType（数组类型）
 //	3.TypeVariable（类型变量）
 //	4.WildcardType（通配符类型）
 //	5.ParameterizedType（参数化类型）
 //
 // ******
 // unsupported： static abstract class EntityTableTest<K, E extends top.fols.box.util.EntityTableTest<K, E>.Entity>{}
 */
@SuppressWarnings({"rawtypes", "UnnecessaryLocalVariable"})
public class GenericTypes {
	static class test {
		static class CC<E extends CharSequence> extends ArrayList<ArrayList<? extends E[]>> {
			static abstract class Wdddd implements List<List<StringBuffer>[]>  {}

			E hhh;
			<E extends List<CharSequence>> E get(List<E[]> value)  { return null; } // Type parameter 'E' hides type parameter 'E'
			<E extends List<CharSequence>> E get2(List<E[]> value) { return null; }

			void test(List<E>[] data) {};
		}
		static class CC2<E extends CharSequence> extends CC<E> {}
		static abstract class EntityTableTest<K, E extends EntitySpace.Entity>{}


		static public void main(String[] args) {
			try {
				GenericTypes gr = new GenericTypes();
				{
					GenericElement genericElement = gr.fromVariable(List.class, new GenericElement[]{gr.fromVariable(List.class, new GenericElement[]{gr.fromType(Object.class)})});
					System.out.println(genericElement);
				}
				{
					GenericElement ee = gr.fromVariable(CC2.class, new Type[]{String.class});
					TypeVariableValues ge = ee
						.toFinalClassGenericPlanar(CC.class)
						.parseDeclareMemberTypeParameterValuesFromParameters(
						CC.class.getDeclaredMethod("get2", List.class),
						new GenericElement[]{
							gr.fromVariable(List.class, new Type[]{})
						}
					);
					System.out.println(ge);
				}
//				{E=java.util.List<java.lang.CharSequence>}

				System.out.println("-----");


				System.out.println("1:" + gr.fromType(CC.class.getDeclaredMethod("test", List[].class).getGenericParameterTypes()[0]).toString());
//				java.util.List<CharSequence>[]

				System.out.println("2:" + gr.fromVariable(List.class, new GenericElement[]{gr.fromVariable(List.class, new Type[]{String.class})}));
//				java.util.List<java.util.List<java.lang.String>>

				GenericElement ee = gr.fromVariable(CC2.class, new Type[]{String.class});
				System.out.println("3:" + ee.toFinalClassGenericPlanar(Collection.class).toGenericElement());
//				java.util.Collection<java.util.ArrayList<java.lang.String[]>>

				System.out.println("4:" + ee.findClassGenericElement(Collection.class));
//				java.util.Collection<java.lang.Object>


				System.out.println("5:" + ee
								   .toFinalClassGenericPlanar(CC.class)
								   .parseDeclareFieldAsGenericElement(CC.class.getDeclaredField("hhh")));
//				java.lang.String hhh

				{
					TypeVariableValues ge = ee
						.toFinalClassGenericPlanar(CC.class)
						.parseDeclareMemberTypeParameterValuesFromParameters(
						CC.class.getDeclaredMethod("get", List.class),
						new GenericElement[]{
							gr.fromVariable(CC.Wdddd.class, new GenericElement[]{})
						}
					);
					System.out.println("6:" + ge);
//					{E=java.util.List<java.lang.StringBuffer>}
				}


				System.out.println("7:" + ee
								   .toFinalClassGenericPlanar(CC.class)
								   .parseDeclareMemberTypeParameterValuesFromUse(
									   CC.class.getDeclaredMethod("get", List.class),
									   new GenericElement[]{gr.fromType(String.class)}
								   ));
				//CC.<String>get();
//				{E=java.lang.String}




				GenericElement root = gr.fromVariable(List.class, new GenericElement[]{gr.fromVariable(List.class, new Type[]{CharSequence.class})});
				GenericElement two 	= gr.fromVariable(List.class, new GenericElement[]{gr.fromVariable(List.class, new Type[]{String.class})});
				System.out.println("8:" + root.isAssignableFromDeep(two));

				System.out.println("-----");

				GenericElement ee2 = gr.fromVariable(CC2[].class, new Type[]{String.class});
				System.out.println(ee2);
				System.out.println(ee2.getArrayGenericElementComponent());
				System.out.println("9:" +  ee2.findClassGenericElement(Collection[].class));
				System.out.println("10:" + ee2.toFinalClassGenericPlanar(Collection[].class).toGenericElement());


				System.out.println("error:");
				System.out.println(EntityTableTest.class.getTypeParameters()[1]);
				GenericElement genericElement = gr.fromType(EntityTableTest.class).toFinalClassGenericPlanar(EntityTableTest.class).toGenericElement();
				System.out.println("11:" + gr.newTypeParameterValuesFromDefine(genericElement.getStartClassTypeParameterInternal(), genericElement.getGenericElementsInternal()));

			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				System.out.println("===========");
			}
		}
	}



	protected DataSequences  dataSequences = new DataSequences();
	protected GenericElement object = fromType(Finals.OBJECT_CLASS);

	static final GenericElement[] EMPTY_ELEMENTS       = {};
	static final TypeVariable[]   EMPTY_TYPE_VARIABLES = {};
	static final String[]         EMPTY_STRINGS = {};

	protected static String nonNullName(String v) { return (null == v || "null".equals(v)) ? "" : v; }

	protected static Type[] objectTypes(int length) {
		Type[] values = new Type[length];
		Arrays.fill(values, Finals.OBJECT_CLASS);
		return values;
	}
	protected static Type checkVariableBounds(Type[] bounds) {
		// <E extends CharSequence>
		if (bounds.length == 1) { 
			return bounds[0];
		} 
		// <E extends CharSequence & Serializable>
		throw new UnsupportedOperationException("unable to handle too many bounds: " + Arrays.toString(bounds));
	}

	public static boolean isGenericType(Type type) {
		if (type == null) return false;
		if (type instanceof Class) return false;
		return true;
	}


	//A[]
	//A<B>[]

	public GenericElement fromType(Type targetSource) {
		if (targetSource == null) throw new NullPointerException("type");
		if (targetSource instanceof Class) {
			Class targetClass = getTypeToClass(targetSource);
			Class start       = targetClass.isArray() ? Arrayz.getRootComponentType(targetClass) : targetClass;
			int endDimension  = targetClass.isArray() ? Arrayz.dimension(targetClass) : 0;

			GenericElement raw = new GenericElement();
			raw.targetSource        = targetSource;
			raw.target 				= targetClass;
			raw.start  		 		= start;
			raw.endDimension 		= endDimension;

			raw.elementTypeVariable = start.getTypeParameters();
			raw.elements            = EMPTY_ELEMENTS;
			return raw;
		} 
		if (targetSource instanceof TypeVariable) {
			TypeVariable pType = (TypeVariable) targetSource;
			Type[] bounds = pType.getBounds();
			if (bounds.length > 0) {
				return fromType(checkVariableBounds(bounds));
			} else {
				return object;
			}
		} 
		if (targetSource instanceof GenericArrayType) {
			GenericArrayType pType = (GenericArrayType) targetSource;
			Type  startType    = getGenericArrayTypeRootComponent(pType);
			GenericElement startGe = fromType(startType).clone();

			Class targetClass  = getTypeToClass(targetSource);
			Class start        = getTypeToClass(startType);
			int endDimension   = getGenericArrayTypeDimension(pType);

			startGe.targetSource  = targetSource;
			startGe.target 	      = targetClass;
			startGe.start  		  = start;
			startGe.endDimension  = endDimension;

			startGe.elementTypeVariable = startGe.elementTypeVariable;
			startGe.elements            = startGe.elements;
			return startGe;
		} 
		if (targetSource instanceof WildcardType) {
			WildcardType tv = (WildcardType) targetSource; 
			Type[] bounds = tv.getUpperBounds();
			if (bounds.length > 0) { 
				// <E extends String> -> E
                return fromType(checkVariableBounds(bounds));
            } else {
				//<? super E> 没有任何意义 也没有名称, 只是限制上限 下限是Object
				return object;
			}
		} 
		if (targetSource instanceof ParameterizedType)	{
			ParameterizedType pType = (ParameterizedType) targetSource;

			Class targetClass  = getTypeToClass(targetSource);
			Class start        = targetClass.isArray() ? Arrayz.getRootComponentType(targetClass) : targetClass;
			int endDimension   = targetClass.isArray() ? Arrayz.dimension(targetClass) : 0;

			GenericElement raw = new GenericElement();
			raw.targetSource        = targetSource;
			raw.target 				= targetClass;
			raw.start  				= start;
			raw.endDimension 		= endDimension;

			raw.elementTypeVariable = start.getTypeParameters();
			raw.elements            = fromTypes(pType.getActualTypeArguments());
			return raw;
		} 
		throw new UnsupportedOperationException("input: " + "'" + targetSource + "'");
	}
	public GenericElement[] fromTypes(Type... types) {
		GenericElement[] elements = new GenericElement[types.length];
		for (int i = 0; i < types.length; i++)
			elements[i] = fromType(types[i]);
		return elements;
	}

	public GenericElement fromVariable(Class type, Type[] redefineTypeVariables) {
		return fromVariable(type, fromTypes(Objects.requireNonNull(redefineTypeVariables, "redefineTypeVariable")));
	}
	public GenericElement fromVariable(Class type, GenericElement[] redefineTypeVariables) {
		GenericElement[] redefineTypeVariable = Objects.requireNonNull(redefineTypeVariables, "redefineTypeVariable");

		GenericElement raw = new GenericElement();
		if (null == type) {
			raw.targetSource        = type;
			raw.target              = null;
			raw.start  	            = null;
			raw.elementTypeVariable = objectTypes(redefineTypeVariables.length);
			raw.elements 		    = redefineTypeVariable;
			raw.endDimension        = 0;
			return raw;
		} else {
			Class  targetClass = type;
			int    dimension   = Arrayz.dimension(type);
			Class  startClass  = type.isArray() ? Arrayz.getRootComponentType(type) : type;

			Type[] typeVariable  = null == startClass ? objectTypes(redefineTypeVariables.length) : startClass.getTypeParameters();

			if (redefineTypeVariable.length != 0) {
				if (redefineTypeVariable.length != typeVariable.length) {
					String result =
						"need: " + fromVariable(type, typeVariable) +
						"[" + typeVariable.length + "]" +
						" " +
						"input: " + fromVariable(null, redefineTypeVariable) +
						"[" + redefineTypeVariable.length + "]";
					throw new UnsupportedOperationException(result);
				}
			} 
			raw.targetSource        = type;
			raw.target              = targetClass;
			raw.start  	            = startClass;
			raw.elementTypeVariable = typeVariable;
			raw.elements 		    = redefineTypeVariable;
			raw.endDimension        = dimension;
			return raw;
		}
	}

	public GenericElement fromField(Field field) {
		if (field == null) return null;

		return fromType(field.getGenericType());
	}
	public ConstructorGenericElements fromConstructor(Constructor method) {
		if (method == null) return null;

		TypeVariable[]   elementTypeVariable = method.getTypeParameters();
		GenericElement[] elements            = fromTypes(elementTypeVariable);

		ConstructorGenericElements raw = new ConstructorGenericElements(method);

		raw.targetSource     = null;
		raw.target 			 = null;
		raw.start            = null;
		raw.endDimension     = 0;

		raw.elementTypeVariable = elementTypeVariable;
		raw.elements  			= elements;
		return raw;
	}
	public MethodGenericElements fromMethod(Method method) {
		if (method == null) return null;

		TypeVariable[]   elementTypeVariable = method.getTypeParameters();
		GenericElement[] elements            = fromTypes(elementTypeVariable);

		MethodGenericElements raw = new MethodGenericElements(method);

		raw.targetSource   = null;
		raw.target 	       = null;
		raw.start          = null;
		raw.endDimension   = 0;

		raw.elementTypeVariable = elementTypeVariable;
		raw.elements   		    = elements;
		return raw;
	}






	//	@Deprecated
//	public static String getTypeVariableNameOrClassName(Type type) {
//		if (type == null) return null;
//		if (type instanceof TypeVariable) {
//            // <A extends B & C> -> A
//		    TypeVariable tv = (TypeVariable) type;
//            return tv.getName();
//        } 
//		Class  toClass = getTypeToClass(type);
//		return toClass == null ? null : toClass.getName();
//	}

	public static int getGenericArrayTypeDimension(GenericArrayType type0) {
		int dimension = 0;
		Type   compont = type0;
		while (compont instanceof GenericArrayType) {
			dimension++;
			compont = ((GenericArrayType) compont).getGenericComponentType();
		}
		return dimension;
	}
    public static Type getGenericArrayTypeRootComponent(GenericArrayType type0) {
		Type   compont = type0;
		while (compont instanceof GenericArrayType) {
			compont = ((GenericArrayType) compont).getGenericComponentType();
		}
		return compont;
	}




	public static Class getTypeToClass(Type type) {
		if (type == null) return null;
		if (type instanceof Class) {
			// <String> -> String
			return (Class) type;
		} 
		if (type instanceof TypeVariable) {
            // <A extends B & C> -> A
		    TypeVariable tv = (TypeVariable) type;
            Type[] bounds = tv.getBounds();
            return getTypeToClass(checkVariableBounds(bounds)); 
        }
		if (type instanceof GenericArrayType) {
			// <A[]> -> A[]
            GenericArrayType tv = (GenericArrayType) type;

			Type  rootType = getGenericArrayTypeRootComponent(tv);
			Class root     = getTypeToClass(rootType);

            int  dimension = getGenericArrayTypeDimension(tv);
            return Arrayz.dimensionClass(root, dimension);//
        } 
		if (type instanceof WildcardType) {
//			System.out.println(Arrayz.toString(tv.getUpperBounds())); // <? extends Value>
//			System.out.println(Arrayz.toString(tv.getLowerBounds())); // <? super   Value>
			WildcardType tv = (WildcardType) type; 
			Type[] bounds = tv.getUpperBounds();
			if (bounds.length > 0) { 
				// <E extends String> -> E
                return getTypeToClass(checkVariableBounds(bounds));
            } else {
				//<? super E> 没有任何意义 也没有名称, 只是限制上限 下限是Object
				return Object.class;
			}
		} 
		if (type instanceof ParameterizedType) {
			// List<A> -> List
			ParameterizedType tv = (ParameterizedType) type;
			Type rawType = tv.getRawType();
			return getTypeToClass(rawType);
		}
        throw new UnsupportedOperationException("input: " + "'" + type + "'");
    }

	public static String getTypeVariableName(Type type) {
		if (type == null) return null;

		if (type instanceof TypeVariable) {
            // <A extends B & C> -> A
		    TypeVariable tv = (TypeVariable) type;
            return tv.getName();
        } 
		if (type instanceof GenericArrayType) {
			// <A[]> -> A[]
			GenericArrayType gat = (GenericArrayType) type;
			Type rcomponent = getGenericArrayTypeRootComponent(gat);
			int  rdimension = getGenericArrayTypeDimension(gat);

			if (rcomponent instanceof TypeVariable) {
				StringBuilder result = new StringBuilder();
				TypeVariable  tv = (TypeVariable) rcomponent;
				result.append(tv.getName());
				for (int i = 0; i < rdimension; i++) {
					result.append(Classx.java_array_suffix);
				}
				return result.toString();
			} else {
				throw new UnsupportedOperationException("input \'" + type + "\'");
			}
		}
		if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;
			Type[] upperBounds = wildcardType.getUpperBounds();
			if (upperBounds.length > 0) { 
                // <E extends String> -> E
				return getTypeVariableName(checkVariableBounds(upperBounds));
            } else {
				// <? super E> 没有任何意义 也没有名称, 只是限制上限 下限是Object
				return null;
			}
		}
		if (type instanceof ParameterizedType) {
			// List<A> -> List
			return null;
		}
		return null;
	}



	public static int dimension(Type type) {
		if (type instanceof Class) 				return Arrayz.dimension((Class)type);
		if (type instanceof GenericArrayType) 	return getGenericArrayTypeDimension((GenericArrayType)type);
		return 0;
	}
	public static Type dimensionType(Type type, int appendDimension) {
		if (type == null) 			return null;
		if (type instanceof Class) 	return Arrayz.dimensionClass((Class)type, appendDimension);

		if (appendDimension == 0)  return type;
		if (appendDimension <  0) throw new UnsupportedOperationException("type: " + type + ", dimension: " + appendDimension); 
		Type result = type;
		for (int i = 0; i < appendDimension; i++) {
			result = new GenericArrayTypeImpl(result);
		}
		return result;
	}

	public static Type reductionDimensionType(Type type) {
		if (type == null) return null;
		if (type instanceof Class)	{
			return ((Class) type).getComponentType();
		}
		if (type instanceof GenericArrayType) {
			return ((GenericArrayType)type).getGenericComponentType();
		} 
		throw new UnsupportedOperationException(String.valueOf(type));
	}
	public static Type elevationDimensionType(final Type component) {
		if (component == null) return null;
		if (component instanceof Class) {
			return Arrayz.dimensionClass((Class)component, 1);
		} else {
			return new GenericArrayTypeImpl(component);
		}
	}
	static class GenericArrayTypeImpl implements GenericArrayType {
		Type component;
		public GenericArrayTypeImpl(Type component) {
			this.component = component;
		}

		@Override
		public Type getGenericComponentType() {
			// TODO: Implement this method
			return component;
		}
		@Override
		public boolean equals(Object v) {
			if (v == this) return true;
			if (!(v instanceof GenericArrayType)) return false;
			GenericArrayType gat = (GenericArrayType) v;
			return Objects.equals(component, gat.getGenericComponentType());
		}
		@Override
		public int hashCode() {
			return Objects.hashCode(component);
		}
		@Override
		public String toString() {
			return component.toString() + Classx.java_array_suffix;
		}
	}






	protected GenericElement parameterizedTypeAsGenericElement(TypeVariableValues tvs, Type parameterizedType) {
		//System.out.println(">>" + superclassUseTypeGeneric);
		GenericElement ge = parameterizedTypeAsGenericElement0(tvs, parameterizedType);
		//System.out.println("<<" + ge);
		return ge;
	}
	private GenericElement parameterizedTypeAsGenericElement0(TypeVariableValues values, Type use) {
		//rawClass == useTypeParameter.getRawClass()
		GenericElement result;
		if (use instanceof Class) {
			Class checkClass = (Class) use;// 不使用泛型

			result = fromType(checkClass);
//				System.out.println(clazz + " -> " + root);
		} else if (use instanceof GenericArrayType) {
			GenericArrayType gar = (GenericArrayType) use;
			Type   rcommpont = getGenericArrayTypeRootComponent(gar);
			int    rcd       = getGenericArrayTypeDimension(gar);
			//String cName    = getTypeVariableName(commpont);
//				GenericElement cge = this.getValue(cName);
//				//数组内部是没有泛型的 只需要获取根class名称
//				if (null == cge)	{
//					throw new UnsupportedOperationException("cannout found type variable: " + cName + ", from: \'" + useTypeParameter + "\'" + ", type-list: " + this.getTypeParameterKeySet() + ", type-value-list: " + getValueMapInternal().keySet());
//				} else if (cge.getGenericElementCount() != 0) {
//					throw new UnsupportedOperationException("from: \'" + useTypeParameter + "\'" + ", type-list: " + this.getTypeParameterKeySet() + ", type-value-list: " + getValueMapInternal().keySet());
//				}
			GenericElement rcommpontGe    = parameterizedTypeAsGenericElement0(values, rcommpont);
			Class          rcommpontClass = rcommpontGe.getTargetClass();

			GenericElement ge   = rcommpontGe.clone();
			Class targetClass   = Arrayz.dimensionClass(rcommpontClass, rcd);
			Class startClass    = rcommpontClass;

			ge.targetSource    = targetClass;
			ge.target 		   = targetClass;

			ge.start           = startClass;
			ge.endDimension    = rcd;

			//System.out.println(targetClass);
			//System.out.println("ge:"+ge);

//			System.out.println(ge);
			return ge;
		} else if (use instanceof TypeVariable) {
			Type t = use;
			String tname = getTypeVariableName(use);
			if (null == tname) {
				throw new UnsupportedOperationException("input \'" + t + "\'" + " type: " + (null == t ? null : t.getClass().getName()));
			} 
			//type instanceof TypeVariable
//			System.out.println("name: " + tname);
//			System.out.println(rawClassTypeParameterUseValueMap);
			GenericElement ge = values.getValue(tname);
			if (null == ge)	{
				throw new UnsupportedOperationException("cannout found type variable: " + tname + ", from: \'" + use + "\'" + ", typeParameterList: " + values.getTypeParameterKeySet() + ", typeParameterValueList: " + values.getValueMapInternal().keySet());
			}
//			System.out.println(ge);
//			System.out.println(ge);
			return ge;
		} else if (use instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) use;

			Type first;
			Type[] upperBounds = wildcardType.getUpperBounds();
			if (upperBounds.length > 0) { 
				// <E extends String> -> String
				first = checkVariableBounds(upperBounds);
			} else {
				//<? super E> 没有任何意义
				first = Object.class;
			}
			result = parameterizedTypeAsGenericElement0(values, first);
//			System.out.println("WildcardType-first: " + first);
//			System.out.println("WildcardType-vars: " + rawClassTypeParameterUseValueMap);
//			System.out.println("WildcardType-result: " + result);
			return result;
		} else if (use instanceof ParameterizedType) {
			//获取 extends XXX<V> 对应的typeVariableParameter
			ParameterizedType   pt = (ParameterizedType) use;
			Type[] parameters = pt.getActualTypeArguments();

			Class checkClass = getTypeToClass(pt.getRawType());

			GenericElement[] elements = new GenericElement[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
//					int hash = new Object().hashCode();
//					System.out.println("ParameterizedType_start=" + hash);
//					System.out.println("ParameterizedType_vars=" + rawClassTypeParameterUseValueMap);
//					System.out.println("ParameterizedType_element=" + parameters[i]);
//					System.out.println("ParameterizedType_result=" + parameterizedAsGenericElement(rawClassTypeParameterUseValueMap, parameters[i]));
//					System.out.println("ParameterizedType_end=" + hash);
//					System.out.println();
				GenericElement e = parameterizedTypeAsGenericElement0(values, parameters[i]);
				elements[i] =  e;

				if (null == e) {
					elements = EMPTY_ELEMENTS;
					break;
				}
			}
			result = fromVariable(checkClass, elements);
//			System.out.println(clazz + " -> " + root);
		} else {
			throw new UnsupportedOperationException("input \'" + use + "\'" + " type: " + (null == use ? null : use.getClass().getName()));
		}
//			System.out.println("inputType:"    + type);
//			System.out.println("outputClass:" + toClass);
//			System.out.println("result:"  + Arrays.toString(result));
		return result;
	}


	protected void newTypeParameterValuesFromMemberParametersDump(
		GenericElement require,
		GenericElement parameter,
		TypeVariableValues putTable) {

		//System.out.println("*" + putTable);
		//System.out.println("*" + require.getType() + " paramter:" + parameter);

		//method(List<E> parameter) -> call(List input)
		if (parameter == null) {
			parameter = require;
		}
		if (require.isTypeUseGeneric()) {
			Type requireType = require.getType();
			String name = getTypeVariableName(requireType);
			if (null != name) {
				if (requireType instanceof  GenericArrayType) {
					GenericArrayType gar = (GenericArrayType) requireType;
					Type   rcommpont = getGenericArrayTypeRootComponent(gar);
					int    rcd       = getGenericArrayTypeDimension(gar);
					String rcName    = getTypeVariableName(rcommpont);

					GenericElement value = parameter;
					if (value != null) {
						while (rcd > 0) {
							GenericElement component = value.getArrayGenericElementComponent();
							if (component == null) {
								value = null; //error
								break;
							}
							value = component;
							rcd--;
						}
					}
					if (value == null)
						value = object;

					//System.out.println(requireType);
					//System.out.println(cName + "=" + value);

					putTable.putValue(rcName, value);
				} else {
					putTable.putValue(name, parameter);
				}
			} else {
				Class 			 checkClass  		  = require.getTargetClass();
				GenericElement[] checkParameterReqire = require.getGenericElementsInternal();

				Class 			 parameterClass    	  = parameter.getTargetClass();
				GenericElement[] parameterElements 	  = parameter.elements;

				for (int i = 0; i < checkParameterReqire.length; i++) {
					GenericElement requireGe   = (checkParameterReqire[i]);
					GenericElement parameterGe = (i < parameterElements.length) ? parameterElements[i] : null;
					newTypeParameterValuesFromMemberParametersDump(requireGe, parameterGe, putTable);
				}
			}
		} 
	}













	public <T> GenericElement getGenericSuperclasses(Class src) {
		if (src != null) {
			return fromType(src.getGenericSuperclass());
		}
		return null;
	}
	public GenericElement getGenericsInterfaces(Class src, Class interfax) {
		// Implements Serializable, List<E>
		if (src != null && interfax != null) {
			Type[] interfaceGenerics = src.getGenericInterfaces();
			for (Type element: interfaceGenerics) {
				Class rawClass;
				if (element instanceof Class) {
					rawClass = (Class) element;
				} else if (element instanceof ParameterizedType) {
					Type rawType = ((ParameterizedType) element).getRawType();
					rawClass = rawType instanceof Class ? ((Class) rawType) : null;
				} else {
					rawClass = null;
				}
				if (rawClass == interfax) {
					return fromType(element);
				}
			}
		}
		return null;
	}





	@SuppressWarnings({"rawtypes"})
	protected abstract class ExecutableGenericElements<E extends Member> extends GenericElement {
		private static final long serialVersionUID = 1L;

		E executable;
		protected ExecutableGenericElements(E method) {
			this.executable = method;
		}

		public E getMember() { return executable; }
		public Class getDeclareClass() { return executable.getDeclaringClass(); }

		protected Type getGenericReturnType() {
			if (executable instanceof Constructor)
				return ((Constructor) executable).getDeclaringClass();
			if (executable instanceof Method)
				return ((Method) executable).getGenericReturnType();
			throw new UnsupportedOperationException(String.valueOf(executable));
		}
		protected Type[] getGenericParameterTypes() {
			if (executable instanceof Constructor)
				return ((Constructor) executable).getGenericParameterTypes();
			if (executable instanceof Method)
				return ((Method) executable).getGenericParameterTypes();
			throw new UnsupportedOperationException(String.valueOf(executable));
		}
		protected Type[] getGenericExceptionTypes() {
			if (executable instanceof Constructor)
				return ((Constructor) executable).getGenericExceptionTypes();
			if (executable instanceof Method)
				return ((Method) executable).getGenericExceptionTypes();
			throw new UnsupportedOperationException(String.valueOf(executable));
		}

		transient GenericElement returnGeneric;
		protected GenericElement getReturnGenericElement() {
			GenericElement cache = returnGeneric;
			if (cache == null) {
				this.returnGeneric = cache = fromType(getGenericReturnType());
			}
			return cache;
		}


		transient GenericElement[] parameterGenericElements;
		protected GenericElement[] getParameterGenericElementsInternal() {
			GenericElement[] cache = parameterGenericElements;
			if (cache == null) {
				this.parameterGenericElements = cache = fromTypes(getGenericParameterTypes());
			}
			return cache;
		}
		protected GenericElement[] getParameterGenericElements() { return getParameterGenericElementsInternal().clone(); }

		transient GenericElement[] throwsGenericElements;
		protected GenericElement[] getThrowsGenericElementsInternal() {
			GenericElement[] cache = throwsGenericElements;
			if (cache == null) {
				this.throwsGenericElements = cache = fromTypes(getGenericExceptionTypes());
			}
			return cache;
		}
		protected GenericElement[] getThrowsGenericElements() { return getThrowsGenericElementsInternal().clone(); }
	}

	@SuppressWarnings("rawtypes")
	public class ConstructorGenericElements extends ExecutableGenericElements {
		private static final long serialVersionUID = 1L;

		protected ConstructorGenericElements(Constructor method) { super(method); }

		@Override public GenericElement   getReturnGenericElement()     { return super.getReturnGenericElement(); }
		@Override public GenericElement[] getParameterGenericElements() { return super.getParameterGenericElements(); }
		@Override public GenericElement[] getThrowsGenericElements() 	{ return super.getThrowsGenericElements(); }
	}

	public class MethodGenericElements extends ExecutableGenericElements<Method> {
		private static final long serialVersionUID = 1L;

		protected MethodGenericElements(Method method) { super(method); }

		@Override public GenericElement   getReturnGenericElement()     { return super.getReturnGenericElement(); }
		@Override public GenericElement[] getParameterGenericElements() { return super.getParameterGenericElements(); }
		@Override public GenericElement[] getThrowsGenericElements() 	{ return super.getThrowsGenericElements(); }
	}









	@SuppressWarnings({"unchecked", "ConstantConditions"})
	public class GenericElement implements Cloneable {
		GenericElement() {}

		public GenericElement getJavaLangObject(){
			return object;
		}
		@Override
		public GenericElement clone() {
			try {
				GenericElement cloned = (GenericElement) super.clone();

				cloned.cacheSequenceBuilder = null;
				cloned.cacheSequenceComponent 	  = null;
				cloned.cacheArrayComponent 		  = null;

				cloned.cacheTargetClassGeneric    = null;
				cloned.cacheSuperclassGeneric 	  = null;
				cloned.cacheInterfaceGeneric  	  = null;

				cloned.cacheFindClassFinalGeneric = null;

				return cloned;
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}



		public boolean isTypeSequence() {
			return
					isTypeSequenceArray() ||
					isTypeSequenceCollection();
		}
		public boolean isTypeSequenceArray() {
			return null != target && target.isArray();
		}
		public boolean isTypeSequenceCollection() {
			return null != target && DataSequences.CLASS_COLLECTION.isAssignableFrom(target);
		}
		transient GenericElement cacheSequenceComponent;
		public GenericElement getSequenceGenericElementComponent() {
			GenericElement cache = this.cacheSequenceComponent;
			if (null == (((cache)))) {
				this.cacheSequenceComponent = DataSequences.getSequenceGenericElementComponent(this);
			}
			return cache;
		}

		/**
		 * 只支持数组形式
		 * List<E>[]   -> List<E>
		 */
		transient GenericElement cacheArrayComponent;
		public GenericElement getArrayGenericElementComponent() {
			if (this.endDimension <= 0) 	 return null;
			if (this.cacheArrayComponent != null) return this.cacheArrayComponent;

			return this.cacheArrayComponent = getArrayGenericElementDimension(this.endDimension - 1);
		}
		protected GenericElement getArrayGenericElementDimension(int newDimension) {
			if (newDimension < 0) 
				throw new UnsupportedOperationException("new dimension: " + newDimension);

			int teDimension = this.endDimension;
			if (teDimension == newDimension) return this;

			GenericElement clone = clone();

			Type targetSourceTemp = clone.targetSource;
			if (teDimension >  newDimension) 
				for (int i = 0, s = teDimension - newDimension; i < s; i++) // lower
					targetSourceTemp = reductionDimensionType(targetSourceTemp);
			else 
				for (int i = 0, s = newDimension - teDimension; i < s; i++) // upper
					targetSourceTemp = elevationDimensionType(targetSourceTemp);

			clone.targetSource 		  = targetSourceTemp;
			clone.target       		  = getTypeToClass(targetSourceTemp);
			clone.start        		  = start;
			clone.elementTypeVariable = elementTypeVariable;
			clone.elements 			  = elements;
			clone.endDimension        = newDimension;
			return clone;
		}


		/**
		 * List<E>[]
		 *   List[].class is target
		 *   List<E>[]    is targetSource
		 *
		 *     List.class   is start
		 *     <E>          is elements
		 *     []		    is endDimension 1
		 *     

		 */
		Type   targetSource;
		Class  target;

		@Nullable Class 		   start;
		@NotNull  Type[]  		   elementTypeVariable; /* 这两个长度不一定是相等的 可能某个 是 0 */
		@NotNull  GenericElement[] elements = {};       /* 这两个长度不一定是相等的 可能某个 是 0 */
		@NotNull  int   		   endDimension;



		public boolean isTypeClass() { 
			return targetSource instanceof Class;
		}
		public boolean isTypeUseGeneric() {
			return
				isGenericType(targetSource) ||
				(elementTypeVariable.length != 0 || elements.length != 0);
		}
		public boolean isTypeNull() {
			return null == targetSource;
		}





		public Type   getType()               { return targetSource; }
		public String getTypeString() {
			return null == targetSource ? "" : targetSource.toString();
		}
//		public String getTypeVariableName()   { return null != rawVariableName ? rawVariableName : null; }

		public Class  getTargetClass()     { return target; }
		public String getTargetClassName() { return null != target ? target.getName() : null; }

		protected Class  getStartClass()     { return start; }
		protected String getStartClassName() { return null != start ? start.getName() : null; }

		protected TypeVariable<Class>[] getStartClassTypeParameterInternal() { 
			return null == start ? null : start.getTypeParameters();
		}
		public    TypeVariable<Class>[] getStartClassTypeParameter() { 
			TypeVariable[] result = getStartClassTypeParameterInternal(); 
			return null != result ? result.clone() : result; 
		}


		public boolean 		  hasGenericElements() 		{ return elements.length > 0; }
		public int   		  getGenericElementCount()  { return elements.length; }
		public GenericElement getGenericElement(int i)  { return this.elements[i]; }


		protected GenericElement[] getGenericElementsInternal() {
			return this.elements; 
		}
		public Type[] getGenericElementsAsType() {
			GenericElement[] elements = this.elements;
			Type[] classes = new TypeVariable[elements.length];
			for (int i = 0; i < elements.length; i++) {
				classes[i] = elements[i].getType();
			}
			return classes; 
		}
		public Class[] getGenericElementsAsTargetClass() {
			GenericElement[] elements = this.elements;
			Class[] classes = new Class[elements.length];
			for (int i = 0; i < elements.length; i++) {
				classes[i] = elements[i].getTargetClass();
			}
			return classes;
		}




		@Override
		public String toString() {
			// TODO: Implement this method
			return toClassNameString();
		}
		public String toClassNameString() {
			StringBuilder result = new StringBuilder();
			result.append(nonNullName(getStartClassName()));
			boolean nonStart = result.length() == 0;
			if (elements.length > 0) {
				StringJoiner sj = new StringJoiner(", ", "<", ">");
				for (GenericElement ge: elements) {
					sj.add(ge.toClassNameString());
				}
				result.append(sj);
			} else {
				if (nonStart) {
					result.append("<>");
				}
			}
			if (endDimension > 0) {
				for (int i = 0; i < endDimension;i++) {
					result.append(Classx.java_array_suffix);
				}
			}
			return result.toString();
		}


		public GenericElement findClassGenericElement(Class eqType) {
			if (eqType.equals(this.getTargetClass())) {
				return this;
			}
			GenericElement superClassGeneric = toGenericElementFromSuperclass();
			if (superClassGeneric != null) {
				GenericElement found = superClassGeneric.findClassGenericElement(eqType);
				if (found != null) {
					return found;
				}
			}
			for (GenericElement interfaceGeneric : toGenericElementFromInterfacesInternal()) {
				GenericElement found = interfaceGeneric.findClassGenericElement(eqType);
				if (found != null) {
					return found;
				}
			}
			return null;
		}



		transient GenericElement cacheTargetClassGeneric;
		public GenericElement toGenericElementFromTargetClass() {
			Class targetClass = this.getTargetClass();
			if (((targetClass == null))) {
				return null;
			}
			GenericElement cache = cacheTargetClassGeneric;
			if (cache == null) {
				this.cacheTargetClassGeneric = cache = fromType(targetClass);
			}
			return cache;
		}
		transient GenericElement cacheSuperclassGeneric;
		public GenericElement toGenericElementFromSuperclass() {
			Class targetClass = this.getTargetClass();
			if (((targetClass == null))) {
				return null;
			}
			Type  superGeneric = Classz.getArrayGenericSuperclass(targetClass);
			if (((superGeneric == null))) {
				return null;
			}
			GenericElement cache = cacheSuperclassGeneric;
			if (cache == null) {
				this.cacheSuperclassGeneric = cache = fromType(superGeneric);
			}
			return cache;
		}

		transient GenericElement[] cacheInterfaceGeneric;
		protected GenericElement[] toGenericElementFromInterfacesInternal() {
			Class tragetClass = this.getTargetClass();
			if (((tragetClass == null))) {
				return null;
			}
			Type[] interfaceGenerics = Classz.getArrayGenericInterfaces(tragetClass);
			if ((((interfaceGenerics == null)))) {
				return null;
			}
			GenericElement[] cache = cacheInterfaceGeneric;
			if (cache == null) {
				this.cacheInterfaceGeneric = cache = fromTypes(interfaceGenerics);
			}
			return cache;
		}
		public GenericElement[] toGenericElementFromInterfaces() { GenericElement[] value = toGenericElementFromInterfacesInternal(); return null == value ? null: value.clone(); }






		WeakMapCache<Class, GenericDeliveryPlanar, RuntimeException> newFindClassFinalGenericCache() {
			return new WeakMapCache<Class, GenericDeliveryPlanar, RuntimeException>(){
				@Override
				protected Map<Class, WrapValue<GenericDeliveryPlanar>> buildMap() {
					// TODO: Implement this method
					return new ConcurrentHashMap<>();
				}

				@Override
				public GenericDeliveryPlanar newCache(Class clazz) throws RuntimeException {
					// TODO: Implement this method
					return newTargetClassPlanarGenerics()
						.getTargetClassGenericPlanar(clazz);
				}
			};
		};

		transient WeakMapCache<Class, GenericDeliveryPlanar, RuntimeException> cacheFindClassFinalGeneric;
		public GenericDeliveryPlanar toFinalClassGenericPlanar(Class<?> targetClass) {
			if (null == target) 					return null;
			if (null == cacheFindClassFinalGeneric) cacheFindClassFinalGeneric = newFindClassFinalGenericCache();

			return cacheFindClassFinalGeneric.getOrCreateCache(targetClass);
		}

		public GenericDeliveryPlanar newTargetClassPlanarGenerics() {
			if (null == target) return null;

			return newPlanarGenerics(target, 
									 getStartClassTypeParameterInternal(), 
									 getGenericElementsInternal());
		}
		protected GenericDeliveryPlanar newPlanarGenerics(Class  targetClass ,
														  TypeVariable[] typeVariable,
														  GenericElement[] redefineTypeVariable) {
			GenericDeliveryPlanar result = new GenericDeliveryPlanar(null, 
																	 targetClass,
																	 typeVariable, 
																	 redefineTypeVariable);
			//System.out.println(result);
			return result;
		}

		/**
		 * Obtaining Generics at the Same Level (element)
		 * 
		 * this;
		 *     List<CharSequence>
		 * element: 
		 *     class AClass<E> extends List<E>
		 *     class BClass extends A<String>
		 *     element = BClass
		 *   -> 
		 *   -> BClass cast to List<String>
		 * result:
		 *    this:    List<CharSequence>
		 *    element: List<String>
		 */
		GenericElement toAssignableFromPlanar(GenericElement thatElement) {
			if (thatElement == null) {
				if (this.target == null && this.elements.length == 0) {
					return thatElement;
				} else {
					return null;
				}
			}
			Class thisClass = this.target;
			Class thatClass = thatElement.target;
			if (thisClass == null ||  thatClass == null) {
				if (thisClass != thatClass) 
					return null;
			} else if (!thisClass.isAssignableFrom(thatClass))	{
				return null;
			}
			if (thisClass == Finals.OBJECT_CLASS) //do you still need to think about it?
				return thatElement;
//			if (thisClass.isArray()) // is (array) and (array)isAssignableFrom(thatClass) is true
//				return thatElement;

			thatElement = Objects.requireNonNull(thatElement.toFinalClassGenericPlanar(thisClass), "internal logic error").toGenericElement();

			return thatElement;
		}
		/**
		 * deep
		 * Number.class.isisAssignableFrom(Integer.class) true
		 */
		public boolean isAssignableFromDeep(GenericElement thatElement) {
//			System.out.println();
//			System.out.println(this);
//			System.out.println(element);
//			System.out.println();

			if (thatElement == null) {
				if (this.target == null && this.elements.length == 0) {
					return true;
				} else {
					return false;
				}
			}
			Class thisClass = this.target;
			Class thatClass = thatElement.target;
			if (thisClass == null ||  thatClass == null) {
				if (thisClass != thatClass) 
					return false;
			} else if (!thisClass.isAssignableFrom(thatClass))	{
				return false;
			} 
			if (thisClass == Finals.OBJECT_CLASS) //do you still need to think about it?
				return true;
//			if (thisClass.isArray()) // is (array) and (array)isAssignableFrom(thatClass) is true
//				return true; //无法判断泛型

			boolean loose = true;
			if (loose) {
				// 比如要求 List 输入了 List<String> 是可行的, 不管输入多少个泛型
				if (this.elements.length            == 0 &&
					this.elementTypeVariable.length == 0) // no check input
					return true; 
			}
//			System.out.println(">>" + thisClass);
//			System.out.println(">>" + thatClass);
//			System.out.println(">>" + this);
//			System.out.println(">>" + thatElement);
			thatElement = Objects.requireNonNull(thatElement.toFinalClassGenericPlanar(thisClass), "internal logic error").toGenericElement();
//			System.out.println(">>" + thatElement);
			GenericElement[] thisElements = this.elements;
			GenericElement[] thatElements = thatElement.elements;
			if (loose) {
				//比如要求 List<String> 输入了 List 是可行的
				if (thatElements.length == 0) 
					return true;
			}
			if (thisElements.length == thatElements.length) {
				for (int i = 0; i < thisElements.length;i++) 
					if (!thisElements[i].isAssignableFromDeep(thatElements[i]))
						return false;
				return true;
			}  
			return false;
		}


		public class SequenceBuilder {
			transient GenericElement genericElement;
			public SequenceBuilder(GenericElement genericElement) {
				this.genericElement = genericElement;
			}

			transient int cacheDimension = -1;
			public int getDimension() {
				int dimension = this.cacheDimension;
				if (dimension == -1) {
					dimension = 1;
					SequenceBuilder component = this;
					while ((component = component.getComponent()) != null) {
						dimension++;
					}
					this.cacheDimension = dimension;
				}
				return dimension;
			}

			transient SequenceBuilder cacheComponent;
			public SequenceBuilder getComponent() {
				SequenceBuilder cache = this.cacheComponent;
				if (null == ((((cache))))) {
					GenericElement sequenceGenericElementComponent = this.genericElement.getSequenceGenericElementComponent();
					if (null == (((sequenceGenericElementComponent)))) return null;
					this.cacheComponent = cache = new SequenceBuilder(sequenceGenericElementComponent);
				}
				return cache;
			}
			public DataSequences.ISequenceBufferBuilder getInstanceBuilder() {
				Class rawClass = this.genericElement.getTargetClass();
				if   (rawClass  == null)
					return null;
				return dataSequences.newSequenceBufferBuilder(rawClass);
			}
		}
		SequenceBuilder cacheSequenceBuilder;
		public SequenceBuilder getSequenceBuilder() {
			SequenceBuilder cache = this.cacheSequenceBuilder;
			if (null ==  cache) {
				this.cacheSequenceBuilder = cache = new SequenceBuilder(this);
			}
			return cache;
		}

	}















	public class TypeVariableValues {
		//Its quantity may not be consistent
		Map<String, GenericElement> typeParameterVariableValuesMap;

		TypeVariable[] 				typeVariable;
		GenericElement[]      		typeParameterVariableValue;
		String[] 			  		typeParameterVariableName;
		Map<String, Integer>  		typeParameterVariableNameIndexMap;


		private TypeVariableValues(TypeVariable[] typeVariable) {
			this.initialize(typeVariable);
		}
		private void initialize(TypeVariable[] typeVariable) {
			int count = typeVariable.length;
			this.typeParameterVariableValuesMap    = new LinkedHashMap<>();

			this.typeParameterVariableValue        = new GenericElement[count];
			this.typeParameterVariableName         = new String[count];
			this.typeParameterVariableNameIndexMap = new LinkedHashMap<>();
		}
		protected Set<String>      keySetInternal() 		   {
			return this.typeParameterVariableNameIndexMap.keySet();
		}
		protected GenericElement[] getGenericElementInternal() {
			return this.typeParameterVariableValue;
		}


		public int    getTypeParameterVariableCount()        {
			return typeVariable.length;
		}
		public String getTypeParameterName(int i)            { 
			return this.typeParameterVariableName[i]; 
		}
		public GenericElement  getTypeParameterValue(int i)    {
			return this.typeParameterVariableValue[i];
		}
		/**
		 * 在 class Test<T> extends Super<V> 或者 implements 使用泛型可以通过 这个方法查找泛型名V在Test存不存在 
		 */
		public boolean hasTypeParameterGeneric(String name) {
			return this.typeParameterVariableNameIndexMap.containsKey(name);
		}
		public int    getTypeParameterNameIndex(String name) {
			Integer index = this.typeParameterVariableNameIndexMap.get(name);
			return null == index ? -1 : index;
		}
		public Set<String> getTypeParameterKeySet() { 
			return new LinkedHashSet<String>(keySetInternal());
		}


		/**
		 * will not change the TypeVariable name created during initialization
		 */
		protected Map<String, GenericElement> getValueMapInternal() {
			return this.typeParameterVariableValuesMap;
		}
		public boolean hasKey(String name) { 
			return this.typeParameterVariableValuesMap.containsKey(name);
		}
		public GenericElement getValue(String name) {
			return this.typeParameterVariableValuesMap.get(name);
		}
		protected void        putValue(String name, GenericElement element) {
			this.typeParameterVariableValuesMap.put(name, Objects.requireNonNull(element, "genericElement"));
		}
		protected void        putValues(TypeVariableValues values) {
			Map<String, GenericElement> thisValuesMap = this.getValueMapInternal();
			Map<String, GenericElement> thatValuesMap = values.getValueMapInternal();

			thisValuesMap.putAll(thatValuesMap);
		}

		public boolean hasValue() { return this.typeParameterVariableValuesMap.size() > 0; }

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.getValueMapInternal().toString();
		}
	}
	public TypeVariableValues newTypeParameterValuesFromDefine(TypeVariable[] clsTypeVariable, GenericElement[] redefineTypeVariable) {
		if (null == clsTypeVariable)      throw new NullPointerException("typeVariable");
		if (null == redefineTypeVariable) throw new NullPointerException("redefineTypeVariable");

		if (redefineTypeVariable.length != 0) {
			if (clsTypeVariable.length != redefineTypeVariable.length) {
				String result =
					"need: " + fromVariable(null, clsTypeVariable) +
					"[" + clsTypeVariable.length + "]" +
					" " +
					"input: " + fromVariable(null, redefineTypeVariable) +
					"[" + redefineTypeVariable.length + "]";
				throw new UnsupportedOperationException(result);
			}
			for (int i = 0; i < redefineTypeVariable.length; i++) {
				GenericElement ge = redefineTypeVariable[i];
				if (null == ge) {
					String result =
						"need: " + fromVariable(null, clsTypeVariable) +
						"[" + clsTypeVariable.length + "]" +
						" " +
						"input: " + Arrays.asList(redefineTypeVariable) +
						"[" + redefineTypeVariable.length + "]";
					throw new UnsupportedOperationException(result);
				}
			}
		}
		TypeVariableValues values = new TypeVariableValues(clsTypeVariable);
		values.typeVariable = clsTypeVariable;

		if (redefineTypeVariable.length == 0) {
			values.typeParameterVariableValue = EMPTY_ELEMENTS;

			for (int i = 0; i < clsTypeVariable.length; i++) {
				Type 		   elementTv = clsTypeVariable[i];
				GenericElement elementRd = fromType(elementTv);

				String elementName = getTypeVariableName(elementTv);
				if ((((elementName != null)))) {
					values.typeParameterVariableName[i] = elementName;
					values.typeParameterVariableNameIndexMap.put(elementName, i);

					values.typeParameterVariableValuesMap.put(elementName, elementRd);
				}
			}
		} else {
			values.typeParameterVariableValue = redefineTypeVariable;

			for (int i = 0; i < clsTypeVariable.length; i++) {
				Type 		   elementTv = clsTypeVariable[i];
				GenericElement elementRd = redefineTypeVariable[i];

				String elementName = getTypeVariableName(elementTv);
				if ((((elementName != null)))) {
					values.typeParameterVariableName[i] = elementName;
					values.typeParameterVariableNameIndexMap.put(elementName, i);

					values.typeParameterVariableValuesMap.put(elementName, elementRd);
				}
			}
		}


		//System.out.println(values.typeParameterVariableTragetMap);
		return values;
	}





	/** 
	 * Push the current generic value to the superclass and interface class,
	 *   so that the final generic value can be obtained.
	 */
	@SuppressWarnings("ForLoopReplaceableByForEach")
	public class GenericDeliveryPlanar {
		GenericDeliveryPlanar parent;
		GenericElement        generic;
		Class 			      clazz;

		TypeVariableValues    classTvs;

		Class   		      superclass;
		GenericElement        superclassTypeGenericElement;
		Class[] 		      interfacesClass;
		GenericElement[]      interfacesTypeGenericElement;

		//Collection<Collection<E>> 



		protected TypeVariableValues getTypeParameterValueMapInternal() {
			return classTvs;
		}






		/**
		 * class 的 TypeParameter
		 * class Map<K,V> 的 <K,V> 中 的泛型参数列表
		 */
		public int    		   getTypeParameterVariableCount()        	   { return classTvs.getTypeParameterVariableCount();}
		public int    		   getTypeParameterNameIndex(String name) 	   { return classTvs.getTypeParameterNameIndex(name); }
		public String 		   getTypeParameterName(int i)            	   { return classTvs.getTypeParameterName(i); }
		public GenericElement  getTypeParameterTargetValue(String name)    { return classTvs.getValue(name);}
		public GenericElement  getTypeParameterTargetValue(int i)    	   { return classTvs.getTypeParameterValue(i);}
		/**
		 * 在 class Test<T> extends Super<V> 或者 implements 使用泛型可以通过 这个方法查找泛型名V在Test<>存不存在 
		 */
		public boolean hasTypeParameterGeneric(String name) { return classTvs.hasTypeParameterGeneric(name); }





		public GenericDeliveryPlanar getParentPlanar() {
			return parent;
		}

		public Class getRawClass() {
			return clazz;
		}
		public Class getSuperclass() {
			return superclass;
		}
		public Class[] getInterfaces() {
			Class[] interfaces = this.interfacesClass;
			if (interfaces == null) 
				return        null;
			return interfaces.clone();
		}



		@Override
		public String toString() {
			// TODO: Implement this method
			StringBuilder sb;
			sb = new StringBuilder();
			sb.append("(");
			sb.append(fromVariable(clazz, classTvs.getGenericElementInternal()).toClassNameString());
			if (null != getSuperclass()) {
				sb.append(" extends ");
				sb.append(getSuperclassPlanar());
			}
			GenericDeliveryPlanar[] interfaces = getInterfacePlanarInternal();
			if (interfaces.length > 0) {
				sb.append(" implements ");
				StringJoiner sj = new StringJoiner(", ");
				for (GenericDeliveryPlanar element: interfaces) {
					sj.add(element.toString());
				}
				sb.append(sj);
			}
			sb.append(")");
			return sb.toString();
		}



		private GenericDeliveryPlanar(GenericDeliveryPlanar parent, Class cls, 
									  TypeVariable[] clsTypeVariable, GenericElement[] redefineTypeVariable) {
			this.initialize(parent, cls, clsTypeVariable, redefineTypeVariable);
		}
		private void initialize(GenericDeliveryPlanar parent, Class cls, 
								TypeVariable[] clsTypeVariable, GenericElement[] redefineTypeVariable) {
//			System.out.println(cls);
//			System.out.println(null == parent ? null :Classz.getArrayGenericSuperclass(parent.clazz));
//			System.out.println(Arrays.asList(clsTypeVariable) + "=" + Arrays.asList(redefineTypeVariable));
//			System.out.println();

			this.parent  = parent;
			this.clazz = cls;
			this.classTvs = newTypeParameterValuesFromDefine(clsTypeVariable, redefineTypeVariable);

			if (null != cls) {
				TypeVariableValues tvs = getTypeParameterValueMapInternal();

				// 泛型重写
				Class superclass               = Classz.getArraySuperclass(cls);
				Type  superclassUseTypeGeneric = Classz.getArrayGenericSuperclass(cls);
				this.superclass = superclass;
				if (null != superclassUseTypeGeneric) {
					this.superclassTypeGenericElement = parameterizedTypeAsGenericElement(tvs, superclassUseTypeGeneric);
//					System.out.println("getGenericSuperclass:" + type);
//					System.out.println("superclassTypeGenericElement:" + superclassTypeGenericElement);
				}

				Class[] interfaces                 = Classz.getArrayInterfaces(cls);
				Type[]  interfacesUseTypeGenerics  = Classz.getArrayGenericInterfaces(cls);
				GenericElement[] interfaceGes = new GenericElement[interfacesUseTypeGenerics.length];
				for (int i = 0; i < interfaces.length;i++) {
					Type interfaceUseTypeGeneric  = interfacesUseTypeGenerics[i];
					interfaceGes[i] = parameterizedTypeAsGenericElement(tvs, interfaceUseTypeGeneric);
				}
				this.interfacesClass = interfaces;
				this.interfacesTypeGenericElement = interfaceGes;
			}
		}


		transient GenericDeliveryPlanar superPlanar;
		public GenericDeliveryPlanar getSuperclassPlanar() { 
			Class sc = this.superclass;
			if (((sc == null))) {
				return  null;
			}
			GenericDeliveryPlanar cache = this.superPlanar;
			if (cache == null) {
				Class cls = sc; 
				TypeVariable[] clsTypeVariable = superclassTypeGenericElement.getStartClassTypeParameterInternal();
				GenericElement genericElement  = superclassTypeGenericElement;
				cache = new GenericDeliveryPlanar(this, cls, clsTypeVariable, genericElement.getGenericElementsInternal());
				this.superPlanar = cache;
			}
			return cache;
		}

		transient GenericDeliveryPlanar[] interfacePlanar;
		protected GenericDeliveryPlanar[] getInterfacePlanarInternal() { 
		    GenericElement[] interfacesTypeGeneric = this.interfacesTypeGenericElement;
			if (null == (((((interfacesTypeGeneric)))))) {
				return null;
			}
			GenericDeliveryPlanar[] cache = this.interfacePlanar;
			if (cache == null) {
				cache = new GenericDeliveryPlanar[interfacesClass.length];
				for (int i = 0; i < interfacesClass.length; i++) {
					Class cls = interfacesClass[i];
					TypeVariable[] clsTypeVariable = interfacesTypeGeneric[i].getStartClassTypeParameterInternal();
					GenericElement genericElement  = interfacesTypeGeneric[i];
					cache[i] = new GenericDeliveryPlanar(this, cls, clsTypeVariable, genericElement.getGenericElementsInternal());
				}
				this.interfacePlanar = cache;
			}
			return cache;
		}
		public GenericDeliveryPlanar[] getInterfacePlanar() {
			GenericDeliveryPlanar[] interfacePlanar = this.getInterfacePlanarInternal();
			if (interfacePlanar == null) {
				return              null;
			}
			return interfacePlanar.clone();
		}




		WeakMapCache<Field, GenericElement, RuntimeException> findFinalClassInnerGenericElementAsFieldCache = new WeakMapCache<Field, GenericElement, RuntimeException>(){
			@Override
			protected Map<Field, WrapValue<GenericElement>> buildMap() {
				// TODO: Implement this method
				return new ConcurrentHashMap<>();
			}

			@Override
			public GenericElement newCache(Field type) throws RuntimeException {
				// TODO: Implement this method
				if (type.getDeclaringClass() != clazz) {
					throw new UnsupportedOperationException(type + " no declare in " + clazz);
				}
				GenericDeliveryPlanar    planar = GenericDeliveryPlanar.this;
				TypeVariableValues tvs = planar.getTypeParameterValueMapInternal();
				return parameterizedTypeAsGenericElement(tvs, type.getGenericType());
			}
		};
		public GenericElement parseDeclareFieldAsGenericElement(Field type) {
			Class rawClass = getRawClass();
			if (rawClass == null)
				return      null;
			return findFinalClassInnerGenericElementAsFieldCache
				.getOrCreateCache(type);
		}






//		class DeclareExecutableInfo {
//			Executable     executable;
//			TypeVariable[] typeVariable;
//
//			Class[] parameterTypes;
//
//			Type   returnGe;
//			Type[] parametersGt;
//			Type[] exceptionGt;
//
//			boolean isUseCustomTypeVariable;
//
//			public DeclareExecutableInfo(Executable executable) {
//				if (executable.getDeclaringClass() != clazz) 
//					throw new UnsupportedOperationException(executable + " no declare in " + clazz);
//
//				this.executable     = executable;
//				this.typeVariable   = executable.getTypeParameters();
//				this.parameterTypes = executable.getParameterTypes();
//
//				this.returnGe     = executable instanceof Method ? ((Method)executable).getGenericReturnType() : null;
//				this.parametersGt = executable.getGenericParameterTypes();
//				this.exceptionGt  = executable.getGenericExceptionTypes();
//
//				//这里应该再判断参数是否使用了泛型 ? 好像也没啥必要。。。
//				this.isUseCustomTypeVariable = typeVariable.length != 0; 
//			}
//		}


		@Deprecated
		public TypeVariableValues parseDeclareMemberTypeParameterValuesFromUse(
			Member member, GenericElement[] parameters) {
			if (member.getDeclaringClass() != clazz)
				throw new UnsupportedOperationException(member + " no declare in " + clazz);
			return newTypeParameterValuesFromDefine(
				((GenericDeclaration)member).getTypeParameters(),
				parameters
			);
		}
		//指定方法的传餐 从而获取 TypeVariableValues
		@Deprecated
		public TypeVariableValues parseDeclareMemberTypeParameterValuesFromParameters(
			Member member, GenericElement[] parameters) {
			if (member.getDeclaringClass() != clazz)
				throw new UnsupportedOperationException(member + " no declare in " + clazz);

			int 		   modifier 	  = member.getModifiers();
			TypeVariable[] typeVariables  = ((GenericDeclaration)member).getTypeParameters();
			Type[] 		   parameterTypes;

			if (member instanceof Constructor)
				parameterTypes = ((Constructor)member).getGenericParameterTypes();
			else if (member instanceof Method)
				parameterTypes = ((Method)member).getGenericParameterTypes();
			else
				throw new UnsupportedOperationException("member: " + member);


			boolean isStatic = Modifier.isStatic(modifier);
			if (typeVariables.length != 0) {
				if (isStatic) {
					//获取参数对应的TypeVariable 的 Values 直接返回
					return newTypeParameterValuesFromMemberParameters(typeVariables, member,
																	  parameterTypes,
																	  parameters);
				} else {
					//获取参数对应的TypeVariable 的 Values 和 类的TypeVariable合并
					TypeVariableValues tvs          = newTypeParameterValuesFromDefine(EMPTY_TYPE_VARIABLES, EMPTY_ELEMENTS);
					TypeVariableValues paramTvs     = newTypeParameterValuesFromMemberParameters(typeVariables, member,
																								 parameterTypes,
																								 parameters);
					TypeVariableValues thisFinalTvs = getTypeParameterValueMapInternal();
					tvs.putValues(thisFinalTvs);
					tvs.putValues(paramTvs);
					return tvs;
				}
			} else {
				if (isStatic) {
					return newTypeParameterValuesFromDefine(EMPTY_TYPE_VARIABLES, EMPTY_ELEMENTS);
				} else {
					TypeVariableValues tvs = newTypeParameterValuesFromDefine(EMPTY_TYPE_VARIABLES, EMPTY_ELEMENTS);
					tvs.putValues(getTypeParameterValueMapInternal()); //only put name value, no change nameList
					return tvs;
				}
			}
		}
		/**
		 * <E extends CharSequence> E get(E value) 
		 * input:  (String)
		 * return: {E: String} values
		 */
		@Deprecated
		protected TypeVariableValues newTypeParameterValuesFromMemberParameters(TypeVariable[]    methodDefineTypeVariable,
																				Member 		  	  member,

																				Type[]    		  parametersRequireGenericType,
																				GenericElement[]  parametersInput) {
			TypeVariableValues memberValues = newTypeParameterValuesFromDefine(methodDefineTypeVariable, fromTypes(methodDefineTypeVariable));		

			if (!Modifier.isStatic(member.getModifiers())) {
				Map<String, GenericElement> memberMap = memberValues.getValueMapInternal();
				Map<String, GenericElement> valueMap  = getTypeParameterValueMapInternal().getValueMapInternal();
				for (String name: valueMap.keySet()) {
					if (memberMap.containsKey(name) == false) {
						//System.out.println("put: " + name);
						memberMap.put(name, valueMap.get(name));
					}
				}
			}
//			System.out.println(memberValues);
//			System.out.println(Arrays.toString(parametersRequireGenericType));
//			System.out.println(Arrays.toString(fromTypes(parametersRequireGenericType)));
//			System.out.println(Arrays.toString(parametersInput));

			//noinspection ConstantConditions
			if ((parametersRequireGenericType.length > 0 &&
				(parametersInput.length > 0))) {
				GenericElement[] define = fromTypes(parametersRequireGenericType);
				GenericElement[] input  = parametersInput;

				//noinspection UnusedLabel
				PARSE: {
//					System.out.println();
//					System.out.println("before-method: "	  + member);
//					System.out.println("before-cls-values: "  + getTypeParameterValueMapInternal());
//					System.out.println("before-key: " 		  + memberValues.getTypeParameterKeySet());
//					System.out.println("before-mrage-values-" + memberValues);
//					System.out.println();
					for (int i = 0;i < parametersRequireGenericType.length;i++) {
						GenericElement defineGe = define[i];
						GenericElement inputGe  = input[i];

						if (!defineGe.isAssignableFromDeep(inputGe)) {
							throw new UnsupportedOperationException(String.format("(%s).isAssignableFrom(%s) == false", defineGe, inputGe));
						}

						//*** 可能存在问题 ****
						@Deprecated
							GenericElement inputGePlanar = Objects.requireNonNull(defineGe.toAssignableFromPlanar(inputGe), "defineGe.toThisClassPlanar(inputGe): result");

						newTypeParameterValuesFromMemberParametersDump(
							defineGe, inputGePlanar,
							memberValues);
					}		
					//System.out.println("after-mrage-values-" + memberValues);
					//System.out.println();
				}
			}
			return memberValues;
		}


		public GenericElement toGenericElement() {
			GenericElement cache = this.generic;
			if (null ==    cache) {
				this.generic = cache = fromVariable(clazz, classTvs.getGenericElementInternal());
			}
			return cache;	
		}

		/**
		 * fast
		 */ 
		public GenericDeliveryPlanar getTargetClassGenericPlanar(Class target) {
			if (target == this.clazz) {
				return this;
			}
			if (this.superclass != null) {
				GenericDeliveryPlanar superClass = getSuperclassPlanar();
				GenericDeliveryPlanar found = superClass.getTargetClassGenericPlanar(target);
				if (null != found)	{
					return  found;
				}
			}
			GenericDeliveryPlanar[] interfaces = getInterfacePlanarInternal();
			if (null != interfaces) {
				if (interfaces.length > 0) {
					for (int i = 0; i < interfaces.length; i++) {
						GenericDeliveryPlanar interfaceClass = interfaces[i];
						GenericDeliveryPlanar found = interfaceClass.getTargetClassGenericPlanar(target);
						if (null != found)	{
							return  found;
						}
					}
				}
			}
			return null;
		}
	}




}

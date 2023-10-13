package top.fols.box.reflect;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import top.fols.atri.cache.WeakMapCacheConcurrentHash;
import top.fols.atri.lang.Objects;


public class ClassMember {
	final Builder builder;
	final Class<?> clazz;
	protected ClassMember(Builder builder, Class<?> clazz) {
		if (null == builder)
			throw new UnsupportedOperationException("builder is null");
		if (null == clazz)
			throw new UnsupportedOperationException("class is null");
		this.builder = builder;
		this.clazz   = clazz;
	}
	public Class<?> getClassType() {
		return clazz;
	}

	String simpleName;
	public String getSimpleName() {
		if (simpleName == null)
			simpleName = builder.getSimpleName(clazz);
		return simpleName;
	}

	public Class<?> getClassSupperclass() {
		return clazz.getSuperclass();
	}
	public Class<?>[] getClassInterfaces() {
		return clazz.getInterfaces();
	}
	public Class<?>[] getClassSuperLinked() {
		return getBuilder().getInheritClassList(clazz).toArray(new Class[]{});
	}

	protected Builder getBuilder() { return builder; }


	@Override
	public int hashCode() {
		// TODO: Implement this method
		return getSimpleName().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (obj == this) return true;
		if (obj == null) return false;
		if (obj.getClass() == getClass())
			return
					builder == ((ClassMember)obj).builder &&
							clazz == ((ClassMember)obj).clazz;
		return false;
	}


	private Reference<ClassMember> superClassMember = new SoftReference<>(null);
	public ClassMember getSuperMember() {
		Class<?> supperclass = getClassSupperclass();
		if (null == supperclass)
			return null;
		ClassMember cache = superClassMember.get();
		if (cache == null)
			superClassMember =  new SoftReference<>(cache = getBuilder().parse(supperclass));
		return cache;
	}
	private Reference<ClassMember[]> interfaceClassMember=new SoftReference<>(null);
	protected ClassMember[] getInterfaceMemberIntern() {
		ClassMember[] cache = interfaceClassMember.get();
		if (cache == null)
			interfaceClassMember = new SoftReference<>(cache = getBuilder().parse(getClassInterfaces()));
		return cache;
	}
	public ClassMember[] getInterfacesMember() {
		return getInterfaceMemberIntern().clone();
	}

	private Reference<ClassMember[]> superLinked = new SoftReference<>(null);
	protected ClassMember[] getClassSuperLinkedMemberIntern() {
		ClassMember[] cache = superLinked.get();
		if (cache == null)
			superLinked = new SoftReference<>(cache = getBuilder().parse(getClassSuperLinked()));
		return cache;
	}


	public static class AnnotationMemberTable {
		public static class AnnotationMember {
			ClassMember declaring;
			Annotation value;

			Class<? extends Annotation> type;

			protected AnnotationMember(ClassMember declaring, Annotation value) {
				this.init(declaring, value);
			}
			protected void init(ClassMember declaring, Annotation method) {
				if (null == declaring)
					throw new UnsupportedOperationException("declaring is null");
				if (null == method)
					throw new UnsupportedOperationException("Annotation is null");

				this.declaring = declaring;
				this.value = method;

				this.type = method.annotationType();
			}

			public Annotation  getValueIntern() { return value; }

			public Class<?> getType() { return type;}

			public ClassMember getDeclaringClassMember() { return declaring; }

			@Override
			public int hashCode() {
				// TODO: Implement this method
				return value.annotationType().getName().hashCode();
			}
			@Override
			public boolean equals(Object obj) {
				// TODO: Implement this method
				if (obj == this) return true;
				if (obj == null) return false;
				if (obj.getClass() == getClass()) {
					AnnotationMember that = (AnnotationMember)obj;
					return
							getDeclaringClassMember().equals(that.getDeclaringClassMember()) &&
									getValueIntern().equals(that.getValueIntern());
				}
				return false;
			}

			@Override
			public String toString() {
				// TODO: Implement this method
				return getValueIntern().toString();
			}
		}
		public static class AnnotationMemberList {
			AnnotationMember[] members;
			Annotation[] value;
			protected AnnotationMemberList(AnnotationMember[] members) {
				this.members = members;

				Annotation[] v = new Annotation[members.length];
				for (int i = 0; i < v.length; i++) {
					AnnotationMember member = members[i];
					v[i] = member.getValueIntern();
				}
				this.value = v;
			}

			protected Annotation[]       values()  { return value;  }
			protected AnnotationMember[] members() { return members; }

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (!(o instanceof AnnotationMemberList)) return false;

				AnnotationMemberList that = (AnnotationMemberList) o;
				if (!Arrays.equals(members(), that.members())) return false;
				return true;
			}

			@Override
			public int hashCode() {
				return Arrays.hashCode(members);
			}
		}

		Map<Class<? extends Annotation>, AnnotationMemberList> table = new HashMap<>();

		ClassMember declaring;
		Annotation[]       value;
		AnnotationMember[] valueMember;

		/**
		 * @param annotations sorted
		 */
		protected AnnotationMemberTable(ClassMember declaring, Annotation[] annotations) {
			init(declaring, annotations);
		}
		protected void init(ClassMember declaring, Annotation[] ans0) {
			this.declaring  = declaring;

			Builder builder = declaring.getBuilder();

			Annotation[] annotations = ans0 == null ?new Annotation[]{}: ans0.clone();
			Map<Object, List<AnnotationMember>> tableCache = new LinkedHashMap<>();
			for (Annotation a: annotations) {
				Object key = a.annotationType();
				List<AnnotationMember> list = tableCache.get(key);
				if (null == list) {
					tableCache.put(key, list = new ArrayList<>());
				}
				list.add(builder.newAnnotationMember(declaring,a));
			}
			Map<Class<? extends Annotation>, AnnotationMemberList> table = new LinkedHashMap<>();
			for (Object o : tableCache.keySet()) {
				Class<? extends Annotation> aClass = (Class<? extends Annotation>) o;
				table.put(aClass, builder.newAnnotationMemberList(tableCache.get(o).toArray(new AnnotationMember[]{})));
			}
			List<AnnotationMember> buffer2 = new ArrayList<>();
			for (Class<? extends Annotation> aClass : table.keySet()) {
				AnnotationMemberList annotationMembers = table.get(aClass);
				Collections.addAll(buffer2, annotationMembers.members());
			}
			this.table  	 = table;
			this.value  	 = annotations;
			this.valueMember = buffer2.toArray(new AnnotationMember[]{});
		}

		protected Map<Class<? extends Annotation>, AnnotationMemberList> getTableIntern() { return table;}

		protected AnnotationMember[] getAnnotationMemberIntern(Class<? extends Annotation> type) {
			AnnotationMemberList annotationMemberList = getTableIntern().get(type);
			return null != annotationMemberList ? annotationMemberList.members() : null;
		}
		protected AnnotationMember[] getAnnotationMemberIntern() {return valueMember;}

		public AnnotationMember[] getAnnotationMember(Class<? extends Annotation> type) {
			AnnotationMember[] annotationMemberIntern = getAnnotationMemberIntern(type);
			return null != annotationMemberIntern ? annotationMemberIntern.clone() : null;
		}
		public AnnotationMember   getAnnotationMemberFirst(Class<? extends Annotation> type) {return Objects.first(getAnnotationMemberIntern(type));}
		public AnnotationMember[] getAnnotationMember() {return getAnnotationMemberIntern().clone();}


		protected Annotation[]  getValueIntern() { return value; }
		public Annotation[]  getValue() {
			return getValueIntern().clone();
		}
		public ClassMember getDeclaringClassMember() { return declaring; }


		protected Annotation[] getAnnotationsIntern(Class<? extends Annotation> type) {
			AnnotationMemberList annotationMemberList = getTableIntern().get(type);
			if (null != annotationMemberList) {
				return  annotationMemberList.values();
			}
			return null;
		}

		public Annotation[] getAnnotations(Class<? extends Annotation> type) {
			Annotation[]   annotations = getAnnotationsIntern(type);
			return null != annotations ? annotations.clone() : null;
		}
		public Annotation   getAnnotationFirst(Class<? extends Annotation> type) {
			return Objects.first(getAnnotationsIntern(type));
		}


		public Annotation findFirstAnnotation(Set<Class<? extends Annotation>> orTypes) {
			for (Annotation annotation : getValueIntern()) {
				if (orTypes.contains(annotation.annotationType())) {
					return annotation;
				}
			}
			return null;
		}


		@Override
		public int hashCode() {
			// TODO: Implement this method
			return 1 + getDeclaringClassMember().hashCode() + getValueIntern().length;
		}
		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj == null) return false;
			if (obj.getClass() == getClass()) {
				AnnotationMemberTable that = (AnnotationMemberTable)obj;
				if (getDeclaringClassMember().equals(that.getDeclaringClassMember())) {
					Map<Class<? extends Annotation>, AnnotationMemberList>
							thisMap = this.getTableIntern(),
							thatMap = that.getTableIntern();
					if (thisMap.size() == thatMap.size()) {
						for (Class<? extends Annotation> key: thisMap.keySet()) {
							if (!thisMap.get(key).equals(thatMap.get(key))) {
								return false;
							}
						}
						return true;
					}
				}
			}
			return false;
		}
		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrays.toString(getValueIntern());
		}
	}
	transient AnnotationMemberTable annotationTable;
	public AnnotationMemberTable getAnnotationTable() {
		AnnotationMemberTable cache = this.annotationTable;
		if (null == (((cache))))
			this.annotationTable = cache = getBuilder().newAnnotationMemberTable(this, getBuilder().getDeclareAnnotation(getClassType()));
		return cache;
	}


	transient AnnotationMemberTable inheritAnnotationTable;
	public AnnotationMemberTable getInheritAnnotationTable() {
		AnnotationMemberTable cache = this.inheritAnnotationTable;
		if (null == (((cache))))
			this.inheritAnnotationTable = cache = getBuilder().findInheritAnnotationTable(this);
		return cache;
	}




	public static class ClassesMemberTable {
		ClassMember declaring;
		Class<?>[] classes;
		protected ClassesMemberTable(ClassMember declaring, Class<?>[] classes) {
			this.init(declaring, classes);
		}
		protected void init(ClassMember declaring, Class<?>[] classes) {
			if (null == declaring)
				throw new UnsupportedOperationException("declaring is null");
			if (null == classes)
				throw new UnsupportedOperationException("classes is null");

			this.declaring      = declaring;
			this.classes = classes.clone();
		}

		private Reference<Map<String, ClassMember>> cacheMap = new SoftReference<>(null);
		protected Map<String, ClassMember> getTableIntern() {
			Builder builder = getDeclaringClassMember().getBuilder();
			Map<String, ClassMember> cache = cacheMap.get();
			if (cache == null) {
				Map<String, ClassMember> table = new HashMap<>();
				for (Class<?> cls: classes) {
					table.put(builder.getSimpleName(cls), builder.parse(cls));
				}
				cacheMap = new SoftReference<>(cache = table);
			}
			return cache;
		}

		public ClassMember getClassMember(String name) {
			return getTableIntern().get(name);
		}
		public Collection<ClassMember> values() {
			return getTableIntern().values();
		}

		protected Class<?>[] getValueIntern() { return classes; }
		public ClassMember getDeclaringClassMember() { return declaring; }


		@Override
		public int hashCode() {
			// TODO: Implement this method
			return 2 + getDeclaringClassMember().hashCode() + getValueIntern().length;
		}
		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj == null) return false;
			if (obj.getClass() == getClass()) {
				ClassesMemberTable that = (ClassesMemberTable)obj;
				if (getDeclaringClassMember().equals(that.getDeclaringClassMember())) {
					Map<String, ClassMember>
							thisMap = this.getTableIntern(),
							thatMap = that.getTableIntern();
					if (thisMap.size() == thatMap.size()) {
						for (String key: thisMap.keySet()) {
							if (!thisMap.get(key).equals(thatMap.get(key))) {
								return false;
							}
						}
						return true;
					}
				}
			}
			return false;
		}
		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrays.toString(getValueIntern());
		}
	}
	transient ClassesMemberTable classesMemberTable;
	public ClassesMemberTable getClassesMemberTable() {
		ClassesMemberTable cache = this.classesMemberTable;
		if (null == (((cache))))
			this.classesMemberTable = cache = getBuilder().newClassesMemberTable(this, getBuilder().getDeclareClasses(getClassType()));
		return cache;
	}





	transient FieldMemberTable fieldMemberTableTable;
	public FieldMemberTable getFieldMemberTable() {
		FieldMemberTable cache = this.fieldMemberTableTable;
		if (null == (((cache))))
			this.fieldMemberTableTable = cache = getBuilder().newFieldMemberTable(this, getBuilder().getDeclareFields(getClassType()));
		return cache;
	}
	public static class FieldMemberTable {
		public static class FieldMember{
			ClassMember declaring;
			Field value;
			protected FieldMember(ClassMember declaring, Field value) {
				this.declaring = declaring;
				this.value = value;
			}
			protected void init(ClassMember declaring, Field value) {
				if (null == declaring)
					throw new UnsupportedOperationException("declaring is null");
				if (null == value)
					throw new UnsupportedOperationException("fieldd is null");

				this.declaring      = declaring;
				this.value = value;
			}

			public Class<?> getDeclaringClass() { return value.getDeclaringClass(); }
			public int    getModifiers() {return value.getModifiers();}
			public String getName() { return value.getName(); }

			protected Field  getValueIntern() { return value; }

			public Class<?>   getReturnType() { return value.getType();}

			protected ClassMember getDeclaringClassMember() { return declaring; }



			transient AnnotationMemberTable annotationMemberTable;
			public AnnotationMemberTable getAnnotationTable() {
				AnnotationMemberTable cache = this.annotationMemberTable;
				if (null == (((cache))))
					this.annotationMemberTable = cache = getDeclaringClassMember().getBuilder().newAnnotationMemberTable(getDeclaringClassMember(), getDeclaringClassMember().getBuilder().getDeclareAnnotation(getValueIntern()));
				return cache;
			}
		}
		ClassMember declaring;
		Field[] value;

		protected FieldMemberTable(ClassMember declaring, Field[] methods) {
			this.init(declaring, methods);
		}
		protected void init(ClassMember declaring, Field[] methods) {
			if (null == declaring)
				throw new UnsupportedOperationException("declaring is null");
			if (null == methods)
				throw new UnsupportedOperationException("field is null");

			this.declaring = declaring;
			this.value = methods.clone();

			Builder builder = declaring.getBuilder();

			Map<String, List<FieldMember>> membersCache = new LinkedHashMap<>();
			for (Field method : methods) {
				String name = method.getName();
				List<FieldMember> buffer = membersCache.get(name);
				if (null==buffer)
					membersCache.put(name,buffer= new ArrayList<>());
				buffer.add(builder.newFieldMember(declaring,method));
			}
			Map<String, FieldMember[]> members = new LinkedHashMap<>();
			for (String s : membersCache.keySet()) {
				members.put(s, membersCache.get(s).toArray(new FieldMember[]{}));
			}
			this.members = members;
		}
		protected Field[]  getValueIntern() { return value; }
		public ClassMember getDeclaringClassMember() { return declaring ; }

		Map<String, FieldMember[]> members;
		protected Map<String, FieldMember[]> getTableIntern() { return members; }


		public FieldMember getFieldMember(String name) {
			return getFieldMember(null, name);
		}
		public FieldMember getFieldMember(Class<?> returnType, String name) {
			FieldMember[] table = getTableIntern().get(name);
			if (null != (((table)))) {
				if (null == returnType)
					return Objects.first(table);
				for (FieldMember m : table)
					if (returnType == m.getReturnType())
						return m;
			}
			return null;
		}



		@Override
		public int hashCode() {
			// TODO: Implement this method
			return 3 + getDeclaringClassMember().hashCode() + getValueIntern().length;
		}
		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj == null) return false;
			if (obj.getClass() == getClass()) {
				FieldMemberTable that = (FieldMemberTable)obj;
				if (getDeclaringClassMember().equals(that.getDeclaringClassMember())) {
					if (getValueIntern().length == that.getValueIntern().length) {
						Map<String, FieldMember[]>
								thisMap = this.getTableIntern(),
								thatMap = that.getTableIntern();
						if (thisMap.size() == thatMap.size()) {
							for (String key: thisMap.keySet()) {
								FieldMember[] thisArr = thisMap.get(key);
								FieldMember[] thatArr = thatMap.get(key);
								if (thatArr == null)
									return false;
								if (thisArr.length == thatArr.length) {
									for (FieldMember thisMm : thisArr) {
										FieldMember thatMm = null;
										for (FieldMember fieldMember : thatArr) {
											if (fieldMember.equals(thisMm)) {
												thatMm = fieldMember;
												break;
											}
										}
										if (thatMm == null)
											return false;
									}
								}
							}
							return true;
						}
					}
				}
			}
			return false;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrays.toString(getValueIntern());
		}
	}


	transient ConstructorMemberTable constructorMemberTableTable;
	public ConstructorMemberTable getConstructorMemberTable() {
		ConstructorMemberTable cache = this.constructorMemberTableTable;
		if (null == (((cache))))
			this.constructorMemberTableTable = cache = getBuilder().newConstructorMemberTable(this, getBuilder().getDeclareConstructors(getClassType()));
		return cache;
	}

	public static class ConstructorMemberTable {
		public static class ConstructorMember{
			ClassMember declaring;
			Constructor<?> value;
			Class<?>[] parameterTypes;

			protected ConstructorMember(ClassMember declaring, Constructor<?> value) {
				this.declaring = declaring;
				this.value = value;
			}
			protected void init(ClassMember declaring, Constructor<?> value) {
				if (null == declaring)
					throw new UnsupportedOperationException("declaring is null");
				if (null == value)
					throw new UnsupportedOperationException("constructor is null");

				this.declaring      = declaring;
				this.value = value;
				this.parameterTypes = value.getParameterTypes();
			}

			public Class<?> getDeclaringClass() { return value.getDeclaringClass(); }
			public int    getModifiers() {return value.getModifiers();}
			public String getName() { return value.getName(); }

			protected Constructor<?>  getValueIntern() { return value; }

			public Class<?>   getReturnType() { return value.getDeclaringClass();}

			protected ClassMember getDeclaringClassMember() { return declaring; }



			transient AnnotationMemberTable annotationMemberTable;
			public AnnotationMemberTable getAnnotationTable() {
				AnnotationMemberTable cache = this.annotationMemberTable;
				if (null == (((cache))))
					this.annotationMemberTable = cache = getDeclaringClassMember().getBuilder().newAnnotationMemberTable(getDeclaringClassMember(), getDeclaringClassMember().getBuilder().getDeclareAnnotation(getValueIntern()));
				return cache;
			}

			public Object[] getParameterTypes() {
				return parameterTypes;
			}
		}
		ClassMember declaring;
		Constructor<?>[] value;

		protected ConstructorMemberTable(ClassMember declaring, Constructor<?>[] methods) {
			this.init(declaring, methods);
		}
		protected void init(ClassMember declaring, Constructor<?>[] methods) {
			if (null == declaring)
				throw new UnsupportedOperationException("declaring is null");
			if (null == methods)
				throw new UnsupportedOperationException("constructor is null");

			this.declaring      = declaring;
			this.value = methods.clone();

			Builder builder = declaring.getBuilder();

			List<ConstructorMember> buffer = new ArrayList<>();
			for (Constructor<?> member : methods) {
				buffer.add(builder.newConstructorMember(declaring, member));
			}
			this.members = buffer.toArray(new ConstructorMember[]{});
		}
		protected Constructor<?>[]  getValueIntern() { return value; }
		public ClassMember getDeclaringClassMember() { return declaring ; }

		ConstructorMember[] members;
		protected ConstructorMember[] getTableIntern() { return members; }

		public ConstructorMember getConstructorMember(Class<?>... parameterTypes) {
			ConstructorMember[] table = getTableIntern();
			if (null != (((table)))) {
				for (ConstructorMember m : table)
					if (Objects.identityEquals(m.getParameterTypes(), parameterTypes))
						return m;
			}
			return null;
		}


		@Override
		public int hashCode() {
			// TODO: Implement this method
			return 4 + getDeclaringClassMember().hashCode() + getValueIntern().length;
		}
		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj == null) return false;
			if (obj.getClass() == getClass()) {
				ConstructorMemberTable that = (ConstructorMemberTable)obj;
				if (getDeclaringClassMember().equals(that.getDeclaringClassMember())) {
					if (getValueIntern().length == that.getValueIntern().length) {
						ConstructorMember[] thisArr = this.getTableIntern();
						ConstructorMember[] thatArr = that.getTableIntern();
						if (thisArr.length == thatArr.length) {
							for (ConstructorMember thisMm : thisArr) {
								ConstructorMember thatMm = null;
								for (ConstructorMember constructorMember : thatArr) {
									if (constructorMember.equals(thisMm)) {
										thatMm = constructorMember;
										break;
									}
								}
								if (thatMm == null)
									return false;
							}
							return true;
						}
					}
				}
			}
			return false;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrays.toString(getValueIntern());
		}
	}



	transient MethodMemberTable methodMemberTable;
	public MethodMemberTable getMethodMemberTable() {
		MethodMemberTable cache = this.methodMemberTable;
		if (null == (((cache))))
			this.methodMemberTable = cache = getBuilder().newMethodMemberTable(this, getBuilder().getDeclareMethods(getClassType()));
		return cache;
	}

	public static class MethodMemberTable {
		public static class MethodMember {
			ClassMember declaring;
			Method value;
			Class<?>[] parameterTypes;
			Class<?> returnType;

			protected MethodMember(ClassMember declaring, Method method) {
				this.init(declaring, method);
			}
			protected void init(ClassMember declaring, Method method) {
				if (null == declaring)
					throw new UnsupportedOperationException("declaring is null");
				if (null == method)
					throw new UnsupportedOperationException("method is null");

				this.declaring = declaring;
				this.value = method;

				this.parameterTypes = method.getParameterTypes();
				this.returnType     = method.getReturnType();
			}

			public Class<?> getDeclaringClass() { return value.getDeclaringClass(); }
			public int    getModifiers() {return value.getModifiers();}
			public String getName() { return value.getName(); }

			protected Method  getValueIntern() { return value; }
			public Class<?>[]    getParameterTypesIntern() { return parameterTypes;}
			public Class<?>[]    getParameterTypes() { return getParameterTypesIntern().clone();}




			public Class<?>   getReturnType() { return returnType;}

			protected ClassMember getDeclaringClassMember() { return declaring; }

			protected MethodMember[] findSuperMethodMember() {
				Builder builder = getDeclaringClassMember().getBuilder();
				List<MethodMember> buffer = new ArrayList<>();
				ClassMember[] superLinked = getDeclaringClassMember().getClassSuperLinkedMemberIntern();
				for (ClassMember cm: superLinked) {
					MethodMember mm = builder.findOverrideMethod(cm.getMethodMemberTable(), this);
					if (null != (mm)) {
						buffer.add(mm);
					}
				}
				return buffer.toArray(new MethodMember[]{});
			}
			private Reference<MethodMember[]> superLinked = new SoftReference<>(null);
			protected MethodMember[] getSuperMethodMemberIntern() {
				MethodMember[] cache = superLinked.get();
				if (cache == null)
					superLinked = new SoftReference<>(cache = findSuperMethodMember());
				return cache;
			}
			public MethodMember[] getSuperMethodMember() {
				MethodMember[] cache = getSuperMethodMemberIntern();
				return null != cache ? cache.clone() : null;
			}


			transient AnnotationMemberTable annotationMemberTable;
			public AnnotationMemberTable getAnnotationTable() {
				AnnotationMemberTable cache = this.annotationMemberTable;
				if (null == (((cache))))
					this.annotationMemberTable = cache = getDeclaringClassMember().getBuilder().newAnnotationMemberTable(getDeclaringClassMember(), getDeclaringClassMember().getBuilder().getDeclareAnnotation(getValueIntern()));
				return cache;
			}


			protected AnnotationMemberTable findInheritAnnotationTable() {
				List<Annotation> list = new ArrayList<>();
				Collections.addAll(list, this.getAnnotationTable().getValueIntern());
				Collections.addAll(list, this.getDeclaringClassMember().getAnnotationTable().getValueIntern());
				ClassMember[] slink = getDeclaringClassMember().getClassSuperLinkedMemberIntern();
				for (ClassMember scm: slink) {
					MethodMemberTable methodMemberTable = scm.getMethodMemberTable();
					MethodMember methodMemberReturnAssignableFrom = methodMemberTable.getMethodMemberReturnAssignableFrom(this);
					if (null !=  methodMemberReturnAssignableFrom)
						Collections.addAll(list, methodMemberReturnAssignableFrom.getAnnotationTable().getValueIntern());
					Collections.addAll(list, scm.getAnnotationTable().getValueIntern());
				}

				Map<Object, Annotation> table = new LinkedHashMap<>();
				for (Annotation annotation : list) {
					Object key = annotation.annotationType();
					if (!table.containsKey(key)) {
						table.put(key, annotation);
					}
				}
				return getDeclaringClassMember().getBuilder().newAnnotationMemberTable(getDeclaringClassMember(), table.values().toArray(new Annotation[]{}));
			}
			transient AnnotationMemberTable inheritAnnotationTable;
			public AnnotationMemberTable getInheritAnnotationTable() {
				AnnotationMemberTable cache = this.inheritAnnotationTable;
				if (null == (((cache))))
					this.inheritAnnotationTable = cache = findInheritAnnotationTable();
				return cache;
			}

			@Override
			public int hashCode() {
				// TODO: Implement this method
				return 5 + getName().hashCode() + getParameterTypesIntern().length;
			}
			@Override
			public boolean equals(Object obj) {
				// TODO: Implement this method
				if (obj == this) return true;
				if (obj == null) return false;
				if (obj.getClass() == getClass()) {
					MethodMember that = (MethodMember)obj;
					return
							getDeclaringClassMember().equals(that.getDeclaringClassMember()) &&
									getName().equals(that.getName()) &&
									getReturnType().equals(that.getReturnType()) &&
									Arrays.equals(getParameterTypesIntern(), that.getParameterTypesIntern());
				}
				return false;
			}



			@Override
			public String toString() {
				// TODO: Implement this method
				return getValueIntern().toString();
			}
		}

		Map<String, MethodMember[]> methodMembers;
		ClassMember declaring;
		Method[] methods;

		protected Map<String, MethodMember[]> getTableIntern() { return methodMembers; }

		public MethodMember getMethodMember(MethodMember methodMember) {
			return getMethodMember(methodMember.getReturnType(), methodMember.getName(),
					methodMember.getParameterTypesIntern());
		}
		public MethodMember getMethodMember(String name, Class<?>... parameterTypes) {
			return getMethodMember(null, name, parameterTypes);
		}
		public MethodMember getMethodMember(Class<?> returnType, String name, Class<?>... parameterTypes) {
			return getMethodMember(true, returnType, name, parameterTypes);
		}
		public MethodMember getMethodMemberReturnAssignableFrom(MethodMember methodMember) {
			return getMethodMemberReturnAssignableFrom(methodMember.getReturnType(), methodMember.getName(),
					methodMember.getParameterTypesIntern());
		}
		public MethodMember getMethodMemberReturnAssignableFrom(Class<?> returnType, String name, Class<?>... parameterTypes) {
			return getMethodMember(false, returnType, name, parameterTypes);
		}

		protected MethodMember getMethodMember(boolean eqReturnType, Class<?> returnType, String name, Class<?>... parameterTypes) {
			MethodMember[] table = getTableIntern().get(name);
			if (null != (((table)))) {
				if (eqReturnType) {
					for (MethodMember m : table) {
						if (Objects.identityEquals(parameterTypes, m.parameterTypes)) {
							if (null == returnType || returnType == m.returnType) {
								return m;
							}
						}
					}
				} else {
					for (MethodMember m : table) {
						if (Objects.identityEquals(parameterTypes, m.parameterTypes)) {
							if (null == returnType || m.returnType.isAssignableFrom(returnType)) {
								return m;
							}
						}
					}
				}
			}
			return null;
		}




		protected MethodMemberTable(ClassMember declaring, Method[] methods) {
			this.init(declaring, methods);
		}
		protected void init(ClassMember declaring, Method[] methods) {
			if (null == declaring)
				throw new UnsupportedOperationException("declaring is null");
			if (null == methods)
				throw new UnsupportedOperationException("methods is null");

			this.declaring = declaring;
			this.methods   = methods.clone();

			Builder builder = declaring.getBuilder();

			Map<String, MethodMember[]> table = new LinkedHashMap<>();
			Map<String, List<MethodMember>> tableCache = new LinkedHashMap<>();
			for (Method m: this.methods) {
				String key = m.getName();
				List<MethodMember> list = tableCache.get(key);
				if (null == list) {
					tableCache.put(key, list = new ArrayList<>());
				}
				list.add(builder.newMethodMember(declaring, m));
			}
			for (String n: tableCache.keySet()) {
				List<MethodMember> list = tableCache.get(n);
				table.put(n, list.toArray(new MethodMember[]{}));
			}
			this.methodMembers = table;
		}
		protected Method[]  getValueIntern() { return methods; }
		public ClassMember getDeclaringClassMember() { return declaring ; }


		@Override
		public int hashCode() {
			// TODO: Implement this method
			return 6 + getDeclaringClassMember().hashCode() + getValueIntern().length;
		}
		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) return true;
			if (obj == null) return false;
			if (obj.getClass() == getClass()) {
				MethodMemberTable that = (MethodMemberTable)obj;
				if (getDeclaringClassMember().equals(that.getDeclaringClassMember())) {
					if (getValueIntern().length == that.getValueIntern().length) {
						Map<String, MethodMember[]>
								thisMap = this.getTableIntern(),
								thatMap = that.getTableIntern();
						if (thisMap.size() == thatMap.size()) {
							for (String key: thisMap.keySet()) {
								MethodMember[] thisArr = thisMap.get(key);
								MethodMember[] thatArr = thatMap.get(key);
								if (thatArr == null)
									return false;
								if (thisArr.length == thatArr.length) {
									for (MethodMember thisMm : thisArr) {
										MethodMember thatMm = null;
										for (MethodMember methodMember : thatArr) {
											if (methodMember.equals(thisMm)) {
												thatMm = methodMember;
												break;
											}
										}
										if (thatMm == null)
											return false;
									}
								}
							}
							return true;
						}
					}
				}
			}
			return false;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrays.toString(getValueIntern());
		}
	}

	public static class Builder implements Serializable {
		private static final long serialVersionUID = 42L;

		public ClassMember parse(Class<?> clazz) {
			if (null == clazz)
				throw new UnsupportedOperationException("class is null");
			return new ClassMember(this, clazz);
		}


		protected AnnotationMemberTable 						newAnnotationMemberTable(ClassMember declaring, Annotation[] ans){return new AnnotationMemberTable(declaring, ans);}
		protected AnnotationMemberTable.AnnotationMember 		newAnnotationMember(ClassMember declaring, Annotation ans){return new AnnotationMemberTable.AnnotationMember(declaring, ans);}
		protected AnnotationMemberTable.AnnotationMemberList 	newAnnotationMemberList(AnnotationMemberTable.AnnotationMember[] ans){return new AnnotationMemberTable.AnnotationMemberList(ans);}


		protected ClassesMemberTable newClassesMemberTable(ClassMember declaring, Class<?>[] classes) {return new ClassesMemberTable(declaring, classes);}

		protected FieldMemberTable newFieldMemberTable(ClassMember declaring, Field[] classes) {return new FieldMemberTable(declaring, classes);}
		protected FieldMemberTable.FieldMember newFieldMember(ClassMember declaring, Field classes) {return new FieldMemberTable.FieldMember(declaring, classes);}

		protected ConstructorMemberTable newConstructorMemberTable(ClassMember declaring, Constructor<?>[] classes) {return new ConstructorMemberTable(declaring, classes);}
		protected ConstructorMemberTable.ConstructorMember newConstructorMember(ClassMember declaring, Constructor<?> classes) {return new ConstructorMemberTable.ConstructorMember(declaring, classes);}

		protected MethodMemberTable newMethodMemberTable(ClassMember declaring, Method[] classes) {return new MethodMemberTable(declaring, classes);}
		protected MethodMemberTable.MethodMember newMethodMember(ClassMember declaring, Method classes) {return new MethodMemberTable.MethodMember(declaring, classes);}



		protected Annotation[] getDeclareAnnotation(Class<?> clazz) {
			return clazz.getDeclaredAnnotations();
		}
		protected Annotation[] getDeclareAnnotation(Field clazz) {
			return clazz.getDeclaredAnnotations();
		}
		protected Annotation[] getDeclareAnnotation(Constructor<?> clazz) {
			return clazz.getDeclaredAnnotations();
		}
		protected Annotation[] getDeclareAnnotation(Method clazz) {
			return clazz.getDeclaredAnnotations();
		}


		protected Class<?>[] getDeclareClasses(Class<?> clazz) {
			return clazz.getDeclaredClasses();
		}
		protected Field[] getDeclareFields(Class<?> clazz) {
			return clazz.getDeclaredFields();
		}
		protected Constructor<?>[] getDeclareConstructors(Class<?> clazz) {
			return clazz.getDeclaredConstructors();
		}
		protected Method[] getDeclareMethods(Class<?> clazz) {
			return clazz.getDeclaredMethods();
		}


//		protected Object 					  annotationTypeWrap(Class<?> c) {return c;}
//		protected Class<? extends Annotation> annotationTypeUnwrap(Object c) {return (Class<? extends Annotation>) c;}






		public Set<Class<?>> getInheritClassList(Class<?> clazz) {
			Set<Class<?>> annotations = new LinkedHashSet<>();
			annotations.add(clazz);

			Class<?>[] interfaces = clazz.getInterfaces();
			for (Class<?> interfaceClass : interfaces) {
				Collection<Class<?>> interfaceAnnotations = getInheritClassList(interfaceClass);
				annotations.addAll(interfaceAnnotations);
			}
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null) {
				Collection<Class<?>> superClassAnnotations = getInheritClassList(superClass);
				annotations.addAll(superClassAnnotations);
			}
			return annotations;
		}

		public AnnotationMemberTable findInheritAnnotationTable(ClassMember classMember) {
			ClassMember[] slink = classMember.getClassSuperLinkedMemberIntern();
			List<Annotation> list = new ArrayList<>();
			for (ClassMember cm: slink) {
				Collections.addAll(list, cm.getAnnotationTable().getValueIntern());
			}
			return classMember.getBuilder().newAnnotationMemberTable(classMember, list.toArray(new Annotation[]{}));
		}




		protected int calculateModifierMagic(int mod) {
			if (isPublic(mod))
				return 3;
			if (isProtected(mod))
				return 2;
			if (isNonAccessModifier(mod))
				return 1;
			if (isPrivate(mod))
				return 0;
			throw new UnsupportedOperationException("modifiers: " + mod);
		}
		protected MethodMemberTable.MethodMember findOverrideMethod(MethodMemberTable parent, MethodMemberTable.MethodMember override) {
			if (null == parent)
				return null;
			if (parent.getDeclaringClassMember() == override.getDeclaringClassMember())
				return null;
			return parent.getMethodMemberReturnAssignableFrom(override.getReturnType(), override.getName(), override.getParameterTypesIntern());
		}
		protected boolean isOverrideMethod(MethodMemberTable.MethodMember parent, MethodMemberTable.MethodMember override) {
			if (parent.getDeclaringClass() == override.getDeclaringClass())
				return false;
			if (!parent.getDeclaringClass().isAssignableFrom(override.getDeclaringClass()))
				return false;
			int parentClassModifiers = parent.getModifiers();
			int thisClassModifiers   = override.getModifiers();
			if (Modifier.isStatic(parentClassModifiers)  || Modifier.isStatic(thisClassModifiers))
				return false;
			if (Modifier.isPrivate(parentClassModifiers) || Modifier.isPrivate(thisClassModifiers))
				return false;
			if (parentClassModifiers != thisClassModifiers) {
				int pi = calculateModifierMagic(parentClassModifiers);
				int si = calculateModifierMagic(thisClassModifiers);
				if (pi > si)
					return false;
			}
			return parent.getName().equals(override.getName()) &&
					parent.getReturnType().isAssignableFrom(override.getReturnType()) &&
					Objects.identityEquals(parent.getParameterTypesIntern(), override.getParameterTypesIntern()); //low speed
		}

		public String getSimpleName(Class<?> type) {
			return type.getSimpleName();
		}




		public ClassMember[] parse(Class<?>... clazz) {
			ClassMember[] result = new ClassMember[null == clazz ? 0 : clazz.length];
			for (int i = 0;i < result.length; i++)
				result[i] = parse(clazz[i]);
			return  result;
		}

		protected <T> Constructor<T> copyConstructor(Constructor<T> m) {
			try {
				Class<T> declaringClass = m.getDeclaringClass();
				Class<?>[] parameterTypes = m.getParameterTypes();
				return declaringClass.getDeclaredConstructor(parameterTypes);
			} catch (UnsupportedOperationException e) {
				throw e;
			} catch (Throwable e) {
				throw new UnsupportedOperationException(e);
			}
		}
		protected Field copyField(Field m) {
			try {
				Class<?> declaringClass = m.getDeclaringClass();
				Class<?> returnType = m.getType();
				String name = m.getName();

				Field result = null;

				try {
					Field find = declaringClass.getDeclaredField(name);
					if (find.getType() == returnType) {
						result = find;
					}
				} catch (NoSuchFieldException ignored) {}
				if (null == result) {
					//confound
					Field[] finds = declaringClass.getDeclaredFields();
					for (Field find: finds) {
						if (name.equals(find.getName())) {
							if (find.getType() == returnType) {
								result = find;
							}
						}
					}
				}
				if (null == result)
					throw new UnsupportedOperationException("cannot found: " + m);
				return result;
			} catch (UnsupportedOperationException e) {
				throw e;
			} catch (Throwable e) {
				throw new UnsupportedOperationException(e);
			}
		}
		protected Method copyMethod(Method m) {
			try {
				Method result = null;
				Class<?> declaringClass = m.getDeclaringClass();
				Class<?> returnType = m.getReturnType();
				String name = m.getName();
				Class<?>[] parameterTypes = m.getParameterTypes();

				try {
					Method find = declaringClass.getDeclaredMethod(name, parameterTypes);
					if (find.getReturnType() == returnType) {
						result = find;
					}
				} catch (NoSuchMethodException ignored) {}
				if (null == result) {
					//confound
					Method[] finds = declaringClass.getDeclaredMethods();
					for (Method find: finds) {
						if (name.equals(find.getName())) {
							if (Objects.identityEquals(find.getParameterTypes(), parameterTypes)) {
								if (find.getReturnType() == returnType) {
									result = find;
									break;
								}
							}
						}
					}
				}
				if (null == result)
					throw new UnsupportedOperationException("cannot found: " + m);
				return result;
			} catch (UnsupportedOperationException e) {
				throw e;
			} catch (Throwable e) {
				throw new UnsupportedOperationException(e);
			}
		}




		public static boolean isPublic(int modifiers) {
			return Modifier.isPublic(modifiers);
		}
		public static boolean isProtected(int modifiers) {
			return Modifier.isProtected(modifiers);
		}
		public static boolean isPrivate(int modifiers) {
			return Modifier.isProtected(modifiers);
		}
		public static boolean isNonAccessModifier(int modifiers) {
			return !(
					Modifier.isPublic(modifiers) ||
							Modifier.isProtected(modifiers) ||
							Modifier.isPrivate(modifiers));
		}

		static Builder DEFAULT = new Builder();
		static public Builder getInstance() { return DEFAULT; }
	}




	public static class CacheBuilder extends Builder implements Serializable {
		private static final long serialVersionUID = 42L;

		protected final ClassMember superParse(Class<?> clazz) {
			if (clazz == null)
				return   null;
			return super.parse(clazz);
		}
		final WeakMapCacheConcurrentHash<Class<?>, ClassMember, RuntimeException> cache = new WeakMapCacheConcurrentHash<Class<?>, ClassMember, RuntimeException>() {
			@Override
			public ClassMember newCache(Class<?> aClass) throws RuntimeException {
				return superParse(aClass);
			}
		};
		@Override
		public ClassMember parse(Class<?> clazz) {
			if (clazz == null)
				return   null;
			return cache.getOrCreateCache(clazz);
		}

		static CacheBuilder DEFAULT = new CacheBuilder();
		static public CacheBuilder getInstance() { return DEFAULT; }
	}



//	static abstract class a {
//		abstract Number get(Number a) throws Throwable;
//	}
//	static class b extends a {
//		@Override
//		protected Long get(Number a) throws Exception, RuntimeException {
//			// TODO: Implement this method
//			return null;
//		}
//	}
}

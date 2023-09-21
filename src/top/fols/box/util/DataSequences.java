package top.fols.box.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import top.fols.atri.reflect.Reflects;
import top.fols.box.lang.Arrayx;
import top.fols.box.lang.Arrayy;
import top.fols.box.lang.GenericTypes;

/**
 * is array or Collection
 */
@SuppressWarnings("ALL")
public class DataSequences {
	public static final Class<Collection> 	CLASS_COLLECTION     = Collection.class;
	public static final Class<Set> 	   		CLASS_COLLECTION_SET = Set.class;



	public static GenericTypes.GenericElement getSequenceGenericElementComponent(GenericTypes.GenericElement element) {
		if (element.isTypeSequenceArray()) {
			return element.getArrayGenericElementComponent();
		} else if (element.isTypeSequenceCollection()) {
			GenericTypes.GenericElement genericElement = element.toFinalClassGenericPlanar(DataSequences.CLASS_COLLECTION).toGenericElement();
			int genericElementCount = genericElement.getGenericElementCount();
			if (genericElementCount == 0) {
				return element.getJavaLangObject();
			} else if (genericElementCount == 1) {
				return element.getGenericElement(0);
			} else {
				throw new UnsupportedOperationException(String.valueOf(genericElement));
			}
		}
		return null;
	}

	public static boolean isSequence(Object object) {
		if (null == object) return false;

		if (object.getClass().isArray())  return true;
		if (object instanceof Collection) return true;

		return false;
	}
	public static boolean isSequence(Class type) {
		if (null == type) return false;

		if (type.isArray())  return true;
		if (DataSequences.CLASS_COLLECTION.isAssignableFrom(type)) return true;

		return false;
	}

	public static int getSequenceLength(Object object) {
		if (null == object) return 0;

		if (object.getClass().isArray())  return Array.getLength(object);
		if (object instanceof Collection) return ((Collection) object).size();

		throw new UnsupportedOperationException("cannot from " + object.getClass().getName() + " get length");
	}



	protected Set  newDefaultSet(Class type)   { return new LinkedHashSet(); }
	protected Set  newSet(Class type) {
		Constructor constructor;
		try {
			constructor = Reflects.getEmptyArgsConstructor(type);
		} catch (Exception ignored) {
			constructor = null;
		}

		if (null != constructor) {
			try {
				return (Set) constructor.newInstance();
			} catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		} else {
			return newDefaultSet(type);
		}
	}

	protected Collection newDefaultCollection(Class type)  { return new ArrayList(); }
	protected Collection newCollection(Class type) {
		Constructor constructor;
		try {
			constructor = Reflects.getEmptyArgsConstructor(type);
		} catch (Exception ignored) {
			constructor = null;
		}

		if (null != constructor) {
			try {
				return (Collection) constructor.newInstance();
			} catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		} else {
			return newDefaultCollection(type);
		}
	}

	public ISequenceBufferBuilder newSequenceBufferBuilder(final Class arrayType) {
		if (arrayType.isArray()) {
			return new ISequenceBufferBuilder() {
				@Override
				public DataSequences.ISequenceBuffer newBuilder() {
					// TODO: Implement this method
					return new ArraySequenceBuffer(arrayType);
				}
			};
		} else {
			if (DataSequences.CLASS_COLLECTION.isAssignableFrom(arrayType)) {
				boolean isAbstract = arrayType.isInterface() || Modifier.isAbstract(arrayType.getModifiers());

				if (DataSequences.CLASS_COLLECTION_SET.isAssignableFrom(arrayType)) {
					FOR_SET: {
						final Set buffer;
						if (isAbstract) {
							buffer = newDefaultSet(arrayType);
						} else if ((buffer = newSet(arrayType)) == null) {
							break FOR_SET;
						}
						return new ISequenceBufferBuilder() {
							@Override
							public DataSequences.ISequenceBuffer newBuilder() {
								// TODO: Implement this method
								return new CollectionSequenceBuffer(buffer);
							}
						};
					}
				} else {
					FOR_COLLECTION: {
						final Collection buffer;
						if (isAbstract) {
							buffer = newDefaultCollection(arrayType);
						} else if ((buffer = newCollection(arrayType)) == null) {
							break FOR_COLLECTION;
						}
						return new ISequenceBufferBuilder() {
							@Override
							public DataSequences.ISequenceBuffer newBuilder() {
								// TODO: Implement this method
								return new CollectionSequenceBuffer(buffer);
							}
						};
					}
				}
			}
		}
		throw new UnsupportedOperationException(arrayType.getName());
	}
	public ISequenceBuffer newSequenceBuffer(Class arrayType) {
		return newSequenceBufferBuilder(arrayType).newBuilder();
	}



	public static interface ISequenceBufferBuilder<E> {
		public ISequenceBuffer<E> newBuilder();
	}
	public static interface ISequenceBuffer<E> {
		public void   add(E value);
		public int    size();
		public Object toSequence();
	}

	public static class CollectionSequenceBuffer<E> implements ISequenceBuffer<E> {
		protected Collection<E> buffer;
		CollectionSequenceBuffer() {}
		CollectionSequenceBuffer(Collection<E> buffer) {
			this.buffer = Objects.requireNonNull(buffer);
		}

		@Override public void add(E value) { buffer.add(value);}
		@Override public int  size() { return buffer.size(); }
		@Override public Object toSequence() { return buffer; }

		@Override
		public String toString() {
			// TODO: Implement this method
			return buffer.toString();
		}
	}
	public static class ArraySequenceBuffer extends CollectionSequenceBuffer implements ISequenceBuffer {
		protected List buffer;
		Class type;
		ArraySequenceBuffer(Class type) {
			this.buffer = new ArrayList();
			this.type = type;
		}

		@Override public void add(Object value) { buffer.add(value); }
		@Override public int  size() { return buffer.size(); }
		@Override public Object toSequence() {
			Object[] buffer = this.buffer.toArray();
			Object target = Array.newInstance(type.getComponentType(), buffer.length);
			Arrayy.arraycopyTraverse(buffer, 0, target, 0, buffer.length);
			return target;
		}
	}








}

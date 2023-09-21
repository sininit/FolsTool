package top.fols.atri.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import top.fols.atri.assist.ArrayLists;
import top.fols.atri.interfaces.abstracts.Filter;
import top.fols.atri.interfaces.interfaces.IFilter;
import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Arrayz;
import top.fols.atri.util.Lists;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ReflectLookup {
	ReflectCache cache;
	public ReflectLookup(ReflectCache cache) {
		this.cache = cache;
	}

	public static class JudgementsSelf<T> implements Cloneable, IFilter<T> {
		IFilter<T> judgment;

		@Override
		public JudgementsSelf<T> clone() {
			// TODO:  this method
			try {
				return ((JudgementsSelf<T>) super.clone());
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		public boolean isJudgment() { return null != judgment; }
		public IFilter<T> judgment() { return judgment; }
		public IFilter<T> judgment(IFilter<T> judgment) {
			this.judgment = judgment;
			return this;
		}
		public IFilter<T> unjudgment() {
			this.judgment = null;
			return this;
		}

		@Override
		public boolean next(T p1) {
			// TODO: Implement this method
			if (null == judgment) throw new NullPointerException("null == judgment");
			return judgment.next(p1);
		}
	}

	public static class JudgementsClass implements Cloneable, IFilter<Class> {
		IFilter<Class> judgment;

		@Override
		public JudgementsClass clone() {
			// TODO:  this method
			try {
				return ((JudgementsClass) super.clone());
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		public boolean isJudgment() { return null != judgment; }
		public IFilter<Class> judgment() { return judgment; }
		public IFilter<Class> judgment(IFilter<Class> judgment) {
			this.judgment = judgment;
			return this;
		}
		public IFilter<Class> judgment(final String simpleName) {
			if (null == simpleName) 
				throw new NullPointerException();
			return judgment(ReflectLookup.classCanonicalNameOrSimple(simpleName));
		}
		public IFilter<Class> judgment(final Class clasz) {
			if (null == clasz) 
				throw new NullPointerException();
			return judgment(ReflectLookup.classEq(clasz));
		}
		public IFilter<Class> judgmentInstanceof(Class judgments0) {
			if (null == judgments0)	
				throw new NullPointerException("null == judgments");
			return judgment(ReflectLookup.classInstanceof(judgments0));
		}
		public IFilter<Class> judgment(final Pattern judgment) {
			if (null == judgment) 
				throw new NullPointerException();
			return judgment(ReflectLookup.classCanonicalNamePattern(judgment));
		}
		public IFilter<Class> unjudgment() {
			this.judgment = null;
			return this;
		}
		
		@Override
		public boolean next(Class p1) {
			// TODO: Implement this method
			if (null == judgment) throw new NullPointerException("null == judgment");
			return judgment.next(p1);
		}
	}
	public static class JudgementsModifier implements Cloneable, IFilter<Integer> {
		IFilter<Integer> judgment;

		@Override
		public JudgementsModifier clone() {
			// TODO:  this method
			try {
				return ((JudgementsModifier) super.clone());
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		public boolean isJudgmentModifier() { return null != judgment; }
		public IFilter<Integer> modifier() { return judgment; }
		public IFilter<Integer> modifier(IFilter<Integer> judgment) {
			this.judgment = judgment;
			return this;
		}
		public IFilter<Integer> modifierHas(final int... hasModifier) {
			if (null == hasModifier) 
				throw new NullPointerException();
			Filter<Integer>[] array = new Filter[hasModifier.length];
			int count = 0;
			for (int i = 0; i < hasModifier.length; i++) {
				final int mod = hasModifier[i];
				if (0 != mod) {
					array[i] = new Filter<Integer>() {
						@Override
						public boolean next(Integer value) {
							// TODO: Implement this method
							return (value.intValue() & mod) != 0;
						}
					};
					count ++;
				}
			}
			return modifier(Filter.<Integer>or(count == array.length ? array : Arrays.copyOf(array, count)));
		}
		public IFilter<Integer> modifierHas(final String... hasModifier) {
			if (null == hasModifier) 
				throw new NullPointerException();
			Filter<Integer>[] array = new Filter[hasModifier.length];
			int count = 0;
			for (int i = 0; i < hasModifier.length; i++) {
				String mod = hasModifier[i];
				if (null != mod && !(mod.length() == 0)) {
					array[i] = ReflectLookup.modifierHas(mod);
					count ++;
				}
			}
			return modifier(Filter.<Integer>or(count == array.length ? array : Arrays.copyOf(array, count)));
		}
		public IFilter<Integer> unjudgment() {
			this.judgment = null;
			return this;
		}
		
		@Override
		public boolean next(Integer p1) {
			// TODO: Implement this method
			if (null == judgment) throw new NullPointerException("null == judgment");
			return judgment.next(p1);
		}
	}

	public static class JudgementsName implements Cloneable, IFilter<String> {
		IFilter<String> judgment;

		@Override
		public JudgementsName clone() {
			// TODO:  this method
			try {
				return ((JudgementsName) super.clone());
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		public boolean isJudgmentName() { return null != judgment; }
		public IFilter<String> name() { return judgment; }
		public IFilter<String> name(IFilter<String> judgment) {
			this.judgment = judgment;
			return this;
		}
		public IFilter<String> name(String judgment) {
			if (null == judgment) 
				throw new NullPointerException();
			return name(Filter.eq(judgment));
		}
		public IFilter<String> name(final Pattern judgment) {
			if (null == judgment) 
				throw new NullPointerException();
			return name(ReflectLookup.namePattern(judgment));
		}
		public IFilter<String> unjudgment() {
			this.judgment = null;
			return this;
		}
		
		@Override
		public boolean next(String p1) {
			// TODO: Implement this method
			if (null == judgment) throw new NullPointerException("null == judgment");
			return judgment.next(p1);
		}
	}
	public static class JudgementsParam implements Cloneable, IFilter<Class[]> {
		ArrayLists<IFilter<Class>[]> judgment;
		ArrayLists<IFilter<Class>[]> clone(ArrayLists<IFilter<Class>[]> paramTypes) {
			ArrayLists<IFilter<Class>[]> newParamTypes = new ArrayLists<IFilter<Class>[]>(IFilter.EMPTY_ARRAY_2D);
			if (null != paramTypes) {
				for (int i = 0; i < paramTypes.size(); i++) {
					IFilter<Class>[] array = paramTypes.get(i);
					newParamTypes.add(null == array ?null: array.clone());
				}
			}
			return newParamTypes;
		}


		@Override
		public JudgementsParam clone() {
			// TODO:  this method
			try {
				JudgementsParam p = (JudgementsParam) super.clone();
				p.judgment = clone(this.judgment);
				return p;
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		public boolean isJudgmentParam() { return null != judgment && judgment.size() != 0; }
		public IFilter<Class>[][] param() { return clone(judgment).toArray(); }
		public IFilter<Class[]> addParamMatch(String... judgments0) {
			if (null == judgments0)	
				throw new NullPointerException("null == judgments");
			IFilter<Class>[] array = new IFilter[judgments0.length];
			for (int i = 0; i < judgments0.length; i++) {
				String className = judgments0[i];
				array[i] = (null == className ? null : ReflectLookup.classCanonicalNameOrSimple(className));
			}
			return addParamMatch(array);
		}
		public IFilter<Class[]> addParamMatch(Class... judgments0) {
			if (null == judgments0)	
				throw new NullPointerException("null == judgments");
			IFilter<Class>[] array = new IFilter[judgments0.length];
			for (int i = 0; i < judgments0.length; i++) {
				Class clasz = judgments0[i];
				array[i] = (null == clasz ? null : ReflectLookup.classEq(clasz));
			}
			return addParamMatch(array);
		}
		public IFilter<Class[]> addParamMatchInstanceof(Class... judgments0) {
			if (null == judgments0)	
				throw new NullPointerException("null == judgments");
			IFilter<Class>[] array = new IFilter[judgments0.length];
			for (int i = 0; i < judgments0.length; i++) {
				Class clasz = judgments0[i];
				array[i] = (null == clasz ? null : ReflectLookup.classInstanceof(clasz));
			}
		    return addParamMatch(array);
		}
		public IFilter<Class[]> addParamMatch(IFilter<Class>... judgments0) {
			if (null == judgments0)	
				throw new NullPointerException("null == judgments");
			int paramCount = judgments0.length;

			ArrayLists<IFilter<Class>[]> list = this.judgment;
			if (list == null) 
				list = new ArrayLists<IFilter<Class>[]>(IFilter.EMPTY_ARRAY_2D);

			int nextIndex = paramCount + 1;
			if (list.size() < nextIndex) {
				for (int i = list.size(); i < nextIndex;i++) {
					list.add(null);
				}
				list.set(paramCount, judgments0.clone());
			}
			this.judgment = list;
			return this;
		}
	  	public IFilter<Class[]> unjudgment(int paramCount) {
			this.judgment.remove(paramCount);
			return this;
		}
		public IFilter<Class[]> unjudgmentAll() {
			this.judgment = null;
			return this;
		}


		public boolean match(Class[] paramTypes) {
			boolean isRequiredParamTypes = isJudgmentParam();
			boolean check = true;
			if (isRequiredParamTypes) {
				ArrayLists<IFilter<Class>[]> p = isRequiredParamTypes
					? this.judgment
					: null;
				IFilter<Class>[] opt = p.opt(paramTypes.length);
				if (null != opt) { 
					for (int pi = 0; pi < opt.length; pi++) {
						IFilter<Class> j = opt[pi];
						Class pClass = paramTypes[pi];
						if (j == null) {
							j = Filter.yes();
						}
						if (!j.next(pClass)) {
							check = false;
							break;
						}
					}
				} else {
					check = false;
				}
			} 
			return check;
		}
		
		@Override
		public boolean next(Class[] p1) {
			// TODO: Implement this method
			if (null == judgment) throw new NullPointerException("null == judgment");
			return match(p1);
		}
	}








	public ClassesLookup classesLookup() {
		return new ClassesLookup(cache);
	}
	public ClassesLookup classesLookup(Class clasz) {
		return new ClassesLookup(cache, clasz);
	}
	public static class ClassesQueryCollection {
		final Set<ClassesQuery> query;

		public ClassesQueryCollection() {
			this.query = new LinkedHashSet<>();
		}
		public ClassesQueryCollection(Set<ClassesQuery> query) {
			this.query = new LinkedHashSet<>(query);
		}

		public ClassesQueryCollection addQuery(ClassesQuery query) {
			if (null == query)
				throw new NullPointerException("null == query");
			ClassesQueryCollection newQuerys;
			newQuerys = new ClassesQueryCollection(this.query);
			newQuerys.query.add(query.clone());
			return newQuerys;
		}

		public static ClassesQueryCollection create(ClassesQuery... querys) {
			ClassesQueryCollection qs = new ClassesQueryCollection();
			if (null != querys) {
				for (ClassesQuery q: querys) {
					if (null ==  q) continue;
					qs.query.add(q.clone());
				}
			}
			return qs;
		}
	}

	public static class ClassesQuery implements Cloneable {
		@Override
		public ClassesQuery clone() {
			// TODO: Implement this method
			try {
				ClassesQuery q = (ClassesQuery) super.clone();
				q.self         = this.self.clone();
				q.declared     = this.declared.clone();
				q.modifier     = this.modifier.clone();
				q.simpleName   = this.simpleName.clone();
				return q;
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}
		JudgementsClass self       = new JudgementsClass();
		JudgementsClass declared   = new JudgementsClass();
		JudgementsModifier modifier = new JudgementsModifier();
		JudgementsName simpleName   = new JudgementsName();

		public boolean isJudgmentSelf() { return self.isJudgment(); }
		public IFilter<Class> self() { return self.judgment(); }
		public ClassesQuery self(IFilter<Class> judgment) {
			this.self.judgment(judgment);
			return this;
		}
		public ClassesQuery self(final Class clasz) {
			this.self.judgment(clasz);
			return this;
		}
		public ClassesQuery self(final String simpleName) {
			this.self.judgment(simpleName);
			return this;
		}
		public ClassesQuery self(final Pattern judgment) {
			this.self.judgment(judgment);
			return this;
		}
		public ClassesQuery selfInstanceof(final Class cls) {
			this.self.judgmentInstanceof(cls);
			return this;
		}
		public ClassesQuery unself() {
			this.self.unjudgment();
			return this;
		}




		public boolean isJudgmentDeclared() { return declared.isJudgment(); }
		public IFilter<Class> declaredClass() { return declared.judgment(); }
		public ClassesQuery declaredClass(IFilter<Class> judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public ClassesQuery declaredClass(final Class clasz) {
			this.declared.judgment(clasz);
			return this;
		}
		public ClassesQuery declaredClass(final String simpleName) {
			this.declared.judgment(simpleName);
			return this;
		}
		public ClassesQuery declaredClass(final Pattern judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public ClassesQuery declaredClassInstanceof(final Class cls) {
			this.declared.judgmentInstanceof(cls);
			return this;
		}
		public ClassesQuery undeclaredClass() {
			this.declared.unjudgment();
			return this;
		}


		public boolean isJudgmentModifier()   { return modifier.isJudgmentModifier(); }
		public IFilter<Integer> modifier() { return modifier.modifier(); }
		public ClassesQuery modifier(IFilter<Integer> judgment) {
			this.modifier.modifier(judgment);
			return this;
		}
		public ClassesQuery modifierHas(final int hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public ClassesQuery modifierHas(final String... hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public ClassesQuery unmodifier() {
			this.modifier.unjudgment();
			return this;
		}



		public boolean isJudgmentSimpleName()  { return simpleName.isJudgmentName(); }
		public IFilter<String> simpleName() { return simpleName.name(); }
		public ClassesQuery simpleName(IFilter<String> judgment) {
			this.simpleName.name(judgment);
			return this;
		}
		public ClassesQuery simpleName(String judgment) {
			this.simpleName.name(judgment);
			return this;
		}
		public ClassesQuery simpleName(final Pattern judgment) {
			this.simpleName.name(judgment);
			return this;
		}
		public ClassesQuery unsimplename() {
			this.simpleName.unjudgment();
			return this;
		}
	}
	public static class ClassesLookup implements Cloneable {
		ReflectCache cache;
		Class defaultClass;
		public ClassesLookup(ReflectCache cache) {
			this.cache  = cache;
			this.defaultClass = null;
		}
		public ClassesLookup(ReflectCache cache, Class defaultClass) {
			this.cache  = cache;
			this.defaultClass = defaultClass;
		}

		public ClassesQuery newQuery() { 
			return new ClassesQuery();
		}

		ReflectCache.ClassesList cacheList;
		ReflectCache.ClassesList getClassesList() {
			if (null != cacheList)
				return  cacheList;
			if (null == defaultClass) 
				throw new UnsupportedOperationException("no default lookup class");
			return cacheList = cache.getClassesList(defaultClass);
		}
		public ClassesLookup setClassesList(Class[] methods) {
			this.cacheList = ReflectCache.ClassesList.wrap(methods);
			return this;
		}

		public Result<Class> findResult(ClassesQuery... querys) {
			return new Result<Class>(find(querys));
		}
		public Result<Class> findResult(ClassesQueryCollection querys) {
			return new Result<Class>(find(querys));
		}
		public static Result<Class> findResult(ReflectCache.ClassesList methodList, ClassesQueryCollection querys) {
			return new Result<Class>(find(methodList, querys));
		}

		public Class[] find(ClassesQuery... querys) {
			return find(getClassesList(), ClassesQueryCollection.create(querys));
		}
		public Class[] find(ClassesQueryCollection querys) {
			return find(getClassesList(), querys);
		}
		public static Class[] find(ReflectCache.ClassesList cacheList, ClassesQueryCollection querys) {
			List<Class> result = new ArrayList<>();
			if (null != querys) {
				Set<ClassesQuery> querySet = querys.query;
				if (querySet.size() > 0) {
					if (null != cacheList) {
						Class[] elements = cacheList.list();
						EACH: for (int i = 0; i < elements.length; i++) {
							Class element = elements[i];
							Class   mSelf          = element;

							Class   mDeclaredClass = element.getDeclaringClass();
							int     mModifier      = element.getModifiers();
							String  mName          = Classz.getSimpleName(element);

							for (ClassesQuery query: querySet) {
								IFilter<Class> self = query.isJudgmentSelf()
									? query.self()
									: Filter.<Class> yes();
								if (self.next(mSelf)) {

									IFilter<Class> d = query.isJudgmentDeclared()
										? query.declaredClass()
										: Filter.<Class> yes();
									if (d.next(mDeclaredClass))	{

										IFilter<Integer> mod = query.isJudgmentModifier()
											? query.modifier()
											: Filter.<Integer> yes();
										if (mod.next(mModifier)) {


											IFilter<String> n = query.isJudgmentSimpleName()
												? query.simpleName()
												: Filter.<String> yes();
											if (n.next(mName)) {

												result.add(element);
												continue EACH;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return result.toArray(Finals.EMPTY_CLASS_ARRAY);
		}
	}






	public ConstructorLookup constructorLookup() {
		return new ConstructorLookup(cache);
	}
	public ConstructorLookup constructorLookup(Class clasz) {
		return new ConstructorLookup(cache, clasz);
	}
	public static class ConstructorQueryCollection {
		final Set<ConstructorQuery> query;

		public ConstructorQueryCollection() {
			this.query = new LinkedHashSet<>();
		}
		public ConstructorQueryCollection(Set<ConstructorQuery> query) {
			this.query = new LinkedHashSet<>(query);
		}

		public ConstructorQueryCollection addQuery(ConstructorQuery query) {
			if (null == query)
				throw new NullPointerException("null == query");
			ConstructorQueryCollection newQuerys;
			newQuerys = new ConstructorQueryCollection(this.query);
			newQuerys.query.add(query.clone());
			return newQuerys;
		}

		public static ConstructorQueryCollection create(ConstructorQuery... querys) {
			ConstructorQueryCollection qs = new ConstructorQueryCollection();
			if (null != querys) {
				for (ConstructorQuery q: querys) {
					if (null ==  q) continue;
					qs.query.add(q.clone());
				}
			}
			return qs;
		}
	}

	public static class ConstructorQuery implements Cloneable {
		@Override
		public ConstructorQuery clone() {
			// TODO: Implement this method
			try {
				ConstructorQuery q = (ConstructorQuery) super.clone();
				q.self       = this.self.clone();
				q.declared   = this.declared.clone();
				q.modifier   = this.modifier.clone();
				q.paramTypes = this.paramTypes.clone();
				return q;
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}

		JudgementsSelf<Constructor> self = new JudgementsSelf<Constructor>();
		JudgementsClass declared         = new JudgementsClass();
		JudgementsModifier modifier       = new JudgementsModifier();
		JudgementsParam paramTypes        = new JudgementsParam();



		public boolean isJudgmentSelf() { return self.isJudgment(); }
		public IFilter<Constructor> self() { return self.judgment(); }
		public ConstructorQuery self(IFilter<Constructor> judgment) {
			this.self.judgment(judgment);
			return this;
		}
		public ConstructorQuery unself() {
			this.self.unjudgment();
			return this;
		}


		public boolean isJudgmentDeclared() { return declared.isJudgment(); }
		public IFilter<Class> declaredClass() { return declared.judgment(); }
		public ConstructorQuery declaredClass(IFilter<Class> judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public ConstructorQuery declaredClass(final Class clasz) {
			this.declared.judgment(clasz);
			return this;
		}
		public ConstructorQuery declaredClass(final String simpleName) {
			this.declared.judgment(simpleName);
			return this;
		}
		public ConstructorQuery declaredClass(final Pattern judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public ConstructorQuery declaredInstanceof(final Class cls) {
			this.declared.judgmentInstanceof(cls);
			return this;
		}
		public ConstructorQuery undeclaredClass() {
			this.declared.unjudgment();
			return this;
		}


		public boolean isJudgmentModifier()   { return modifier.isJudgmentModifier(); }
		public IFilter<Integer> modifier() { return modifier.modifier(); }
		public ConstructorQuery modifier(IFilter<Integer> judgment) {
			this.modifier.modifier(judgment);
			return this;
		}
		public ConstructorQuery modifierHas(final int hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public ConstructorQuery modifierHas(final String... hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public ConstructorQuery unmodifier() {
			this.modifier.unjudgment();
			return this;
		}


		public boolean isJudgmentParam()     { return paramTypes.isJudgmentParam(); }
		public IFilter<Class>[][] param() { return paramTypes.param(); }
		public ConstructorQuery addParamMatch(String... judgments0) {
			this.paramTypes.addParamMatch(judgments0);
			return this;
		}
		public ConstructorQuery addParamMatch(Class... judgments0) {
			this.paramTypes.addParamMatch(judgments0);
			return this;
		}
		public ConstructorQuery addParamMatchInstanceof(Class... judgments0) {
			this.paramTypes.addParamMatchInstanceof(judgments0);
			return this;
		}
		public ConstructorQuery addParamMatch(IFilter<Class>... judgments0) {
			this.paramTypes.addParamMatch(judgments0);
			return this;
		}
	  	public ConstructorQuery unparamMatch(int paramCount) {
			this.paramTypes.unjudgment(paramCount);
			return this;
		}
		public ConstructorQuery unparam() {
			this.paramTypes.unjudgmentAll();
			return this;
		}
	}
	public static class ConstructorLookup implements Cloneable {
		ReflectCache cache;
		Class defaultClass;
		public ConstructorLookup(ReflectCache cache) {
			this.cache  = cache;
			this.defaultClass = null;
		}
		public ConstructorLookup(ReflectCache cache, Class defaultClass) {
			this.cache  = cache;
			this.defaultClass = defaultClass;
		}

		public ConstructorQuery newQuery() { 
			return new ConstructorQuery();
		}

		ReflectCache.ConstructorList cacheList;
		ReflectCache.ConstructorList getConstructorList() {
			if (null != cacheList)
				return  cacheList;
			if (null == defaultClass) 
				throw new UnsupportedOperationException("no default lookup class");
			return cacheList = cache.getConstructorList(defaultClass);
		}
		public ConstructorLookup setConstructorList(Constructor[] methods) {
			this.cacheList = ReflectCache.ConstructorList.wrap(methods);
			return this;
		}

		public Result<Constructor> findResult(ConstructorQuery... querys) {
			return new Result<Constructor>(find(querys));
		}
		public Result<Constructor> findResult(ConstructorQueryCollection querys) {
			return new Result<Constructor>(find(querys));
		}
		public static Result<Constructor> findResult(ReflectCache.ConstructorList methodList, ConstructorQueryCollection querys) {
			return new Result<Constructor>(find(methodList, querys));
		}

		public Constructor[] find(ConstructorQuery... querys) {
			return find(getConstructorList(), ConstructorQueryCollection.create(querys));
		}
		public Constructor[] find(ConstructorQueryCollection querys) {
			return find(getConstructorList(), querys);
		}
		public static Constructor[] find(ReflectCache.ConstructorList cacheList, ConstructorQueryCollection querys) {
			List<Constructor> result = new ArrayList<>();
			if (null != querys) {
				Set<ConstructorQuery> querySet = querys.query;
				if (querySet.size() > 0) {
					if (null != cacheList) {
						Constructor[] elements = cacheList.list();
						EACH: for (int i = 0; i < elements.length; i++) {
							Constructor  element = elements[i];
							Constructor mSelf      = element;

							Class   mDeclaredClass = element.getDeclaringClass();
							int     mModifier      = element.getModifiers();
							Class[] mParamTypes    = cacheList.parameterTypes(i);

							for (ConstructorQuery query: querySet) {
								IFilter<Constructor> self = query.isJudgmentSelf()
									? query.self()
									: Filter.<Constructor> yes();
								if (self.next(mSelf)) {

									IFilter<Class> d = query.isJudgmentDeclared()
										? query.declaredClass()
										: Filter.<Class> yes();
									if (d.next(mDeclaredClass))	{

										IFilter<Integer> mod = query.isJudgmentModifier()
											? query.modifier()
											: Filter.<Integer> yes();
										if (mod.next(mModifier)) {

											if (query.paramTypes.match(mParamTypes)) {
												result.add(element);
												continue EACH;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return result.toArray(Finals.EMPTY_CONSTRUCTOR_ARRAY);
		}
	}





	public FieldLookup fieldLookup() {
		return new FieldLookup(cache);
	}
	public FieldLookup fieldLookup(Class clasz) {
		return new FieldLookup(cache, clasz);
	}
	public static class FieldQueryCollection {
		final Set<FieldQuery> query;

		public FieldQueryCollection() {
			this.query = new LinkedHashSet<>();
		}
		public FieldQueryCollection(Set<FieldQuery> query) {
			this.query = new LinkedHashSet<>(query);
		}

		public FieldQueryCollection addQuery(FieldQuery query) {
			if (null == query)
				throw new NullPointerException("null == query");
			FieldQueryCollection newQuerys;
			newQuerys = new FieldQueryCollection(this.query);
			newQuerys.query.add(query.clone());
			return newQuerys;
		}

		public static FieldQueryCollection create(FieldQuery... querys) {
			FieldQueryCollection qs = new FieldQueryCollection();
			if (null != querys) {
				for (FieldQuery q: querys) {
					if (null ==  q) continue;
					qs.query.add(q.clone());
				}
			}
			return qs;
		}
	}

	public static class FieldQuery implements Cloneable {
		@Override
		public FieldQuery clone() {
			// TODO: Implement this method
			try {
				FieldQuery q = (FieldQuery) super.clone();
				q.self       = this.self.clone();
				q.declared   = this.declared.clone();
				q.modifier   = this.modifier.clone();
				q.retype     = this.retype.clone();
				q.name       = this.name.clone();
				return q;
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}
		JudgementsSelf<Field> self = new JudgementsSelf<>();
		JudgementsClass declared   = new JudgementsClass();
		JudgementsModifier modifier = new JudgementsModifier();
		JudgementsClass retype     = new JudgementsClass();
		JudgementsName name         = new JudgementsName();


		public boolean isJudgmentSelf() { return self.isJudgment(); }
		public IFilter<Field> self() { return self.judgment(); }
		public FieldQuery self(IFilter<Field> judgment) {
			this.self.judgment(judgment);
			return this;
		}
		public FieldQuery unself() {
			this.self.unjudgment();
			return this;
		}


		public boolean isJudgmentDeclared() { return declared.isJudgment(); }
		public IFilter<Class> declaredClass() { return declared.judgment(); }
		public FieldQuery declaredClass(IFilter<Class> judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public FieldQuery declaredClass(final Class clasz) {
			this.declared.judgment(clasz);
			return this;
		}
		public FieldQuery declaredClass(final String simpleName) {
			this.declared.judgment(simpleName);
			return this;
		}
		public FieldQuery declaredClass(final Pattern judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public FieldQuery declaredInstanceof(final Class cls) {
			this.declared.judgmentInstanceof(cls);
			return this;
		}
		public FieldQuery undeclaredClass() {
			this.declared.unjudgment();
			return this;
		}


		public boolean isJudgmentModifier()   { return modifier.isJudgmentModifier(); }
		public IFilter<Integer> modifier() { return modifier.modifier(); }
		public FieldQuery modifier(IFilter<Integer> judgment) {
			this.modifier.modifier(judgment);
			return this;
		}
		public FieldQuery modifierHas(final int hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public FieldQuery modifierHas(final String... hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public FieldQuery unmodifier() {
			this.modifier.unjudgment();
			return this;
		}


		public boolean isJudgmentReturnType() { return retype.isJudgment(); }
		public IFilter<Class> returnType() { return retype.judgment(); }
		public FieldQuery returnType(IFilter<Class> judgment) {
			this.retype.judgment(judgment);
			return this;
		}
		public FieldQuery returnType(final Class clasz) {
			this.retype.judgment(clasz);
			return this;
		}
		public FieldQuery returnType(final String simpleName) {
			this.retype.judgment(simpleName);
			return this;
		}
		public FieldQuery returnType(final Pattern judgment) {
			this.retype.judgment(judgment);
			return this;
		}
		public FieldQuery returnTypeInstanceof(final Class cls) {
			this.retype.judgmentInstanceof(cls);
			return this;
		}
		public FieldQuery unreturnType() {
			this.retype.unjudgment();
			return this;
		}

		public boolean isJudgmentName()  { return name.isJudgmentName(); }
		public IFilter<String> name() { return name.name(); }
		public FieldQuery name(IFilter<String> judgment) {
			this.name.name(judgment);
			return this;
		}
		public FieldQuery name(String judgment) {
			this.name.name(judgment);
			return this;
		}
		public FieldQuery name(final Pattern judgment) {
			this.name.name(judgment);
			return this;
		}
		public FieldQuery unname() {
			this.name.unjudgment();
			return this;
		}
	}
	public static class FieldLookup implements Cloneable {
		ReflectCache cache;
		Class defaultClass;
		public FieldLookup(ReflectCache cache) {
			this.cache  = cache;
			this.defaultClass = null;
		}
		public FieldLookup(ReflectCache cache, Class defaultClass) {
			this.cache  = cache;
			this.defaultClass = defaultClass;
		}

		public FieldQuery newQuery() { 
			return new FieldQuery();
		}

		ReflectCache.FieldList cacheList;
		ReflectCache.FieldList getFieldList() {
			if (null != cacheList)
				return  cacheList;
			if (null == defaultClass) 
				throw new UnsupportedOperationException("no default lookup class");
			return cacheList = cache.getFieldList(defaultClass);
		}
		public FieldLookup setFieldList(Field[] methods) {
			this.cacheList = ReflectCache.FieldList.wrap(methods);
			return this;
		}

		public Result<Field> findResult(FieldQuery... querys) {
			return new Result<Field>(find(querys));
		}
		public Result<Field> findResult(FieldQueryCollection querys) {
			return new Result<Field>(find(querys));
		}
		public static Result<Field> findResult(ReflectCache.FieldList methodList, FieldQueryCollection querys) {
			return new Result<Field>(find(methodList, querys));
		}

		public Field[] find(FieldQuery... querys) {
			return find(getFieldList(), FieldQueryCollection.create(querys));
		}
		public Field[] find(FieldQueryCollection querys) {
			return find(getFieldList(), querys);
		}
		public static Field[] find(ReflectCache.FieldList cacheList, FieldQueryCollection querys) {
			List<Field> result = new ArrayList<>();
			if (null != querys) {
				Set<FieldQuery> querySet = querys.query;
				if (querySet.size() > 0) {
					if (null != cacheList) {
						Field[] elements = cacheList.list();
						EACH: for (int i = 0; i < elements.length; i++) {
							Field  element = elements[i];
							Field   mSelf          = element;

							Class   mDeclaredClass = element.getDeclaringClass();
							int     mModifier      = element.getModifiers();
							Class   mReturnType    = element.getType();
							String  mName          = element.getName();

							for (FieldQuery query: querySet) {
								IFilter<Field> self = query.isJudgmentSelf()
									? query.self()
									: Filter.<Field> yes();
								if (self.next(mSelf)) {

									IFilter<Class> d = query.isJudgmentDeclared()
										? query.declaredClass()
										: Filter.<Class> yes();
									if (d.next(mDeclaredClass))	{

										IFilter<Integer> mod = query.isJudgmentModifier()
											? query.modifier()
											: Filter.<Integer> yes();
										if (mod.next(mModifier)) {

											IFilter<Class> rt = query.isJudgmentReturnType()
												? query.returnType()
												: Filter.<Class> yes();
											if (rt.next(mReturnType)) {

												IFilter<String> n = query.isJudgmentName()
													? query.name()
													: Filter.<String> yes();
												if (n.next(mName)) {

													result.add(element);
													continue EACH;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return result.toArray(Finals.EMPTY_FIELD_ARRAY);
		}
	}







	public MethodLookup methodLookup() {
		return new MethodLookup(cache);
	}
	public MethodLookup methodLookup(Class clasz) {
		return new MethodLookup(cache, clasz);
	}
	public static class MethodQueryCollection {
		final Set<MethodQuery> query;

		public MethodQueryCollection() {
			this.query = new LinkedHashSet<>();
		}
		public MethodQueryCollection(Set<MethodQuery> query) {
			this.query = new LinkedHashSet<>(query);
		}

		public MethodQueryCollection addQuery(MethodQuery query) {
			if (null == query)
				throw new NullPointerException("null == query");
			MethodQueryCollection newQuerys;
			newQuerys = new MethodQueryCollection(this.query);
			newQuerys.query.add(query.clone());
			return newQuerys;
		}

		public static MethodQueryCollection create(MethodQuery... querys) {
			MethodQueryCollection qs = new MethodQueryCollection();
			if (null != querys) {
				for (MethodQuery q: querys) {
					if (null ==  q) continue;
					qs.query.add(q.clone());
				}
			}
			return qs;
		}
	}

	public static class MethodQuery implements Cloneable {
		@Override
		public MethodQuery clone() {
			// TODO: Implement this method
			try {
				MethodQuery q = (MethodQuery) super.clone();
				q.self       = this.self.clone();
				q.declared   = this.declared.clone();
				q.modifier   = this.modifier.clone();
				q.retype     = this.retype.clone();
				q.name       = this.name.clone();
				q.paramTypes = this.paramTypes.clone();
				return q;
			} catch (CloneNotSupportedException e) {
				throw new UnsupportedOperationException(e);
			}
		}



		JudgementsSelf<Method> self= new JudgementsSelf<>();
		JudgementsClass declared   = new JudgementsClass();
		JudgementsModifier modifier = new JudgementsModifier();
		JudgementsClass retype     = new JudgementsClass();
		JudgementsName name         = new JudgementsName();
		JudgementsParam paramTypes  = new JudgementsParam();



		public boolean isJudgmentSelf() { return self.isJudgment(); }
		public IFilter<Method> self() { return self.judgment(); }
		public MethodQuery self(IFilter<Method> judgment) {
			this.self.judgment(judgment);
			return this;
		}
		public MethodQuery unself() {
			this.self.unjudgment();
			return this;
		}


		public boolean isJudgmentDeclared() { return declared.isJudgment(); }
		public IFilter<Class> declaredClass() { return declared.judgment(); }
		public MethodQuery declaredClass(IFilter<Class> judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public MethodQuery declaredClass(final Class clasz) {
			this.declared.judgment(clasz);
			return this;
		}
		public MethodQuery declaredClass(final String simpleName) {
			this.declared.judgment(simpleName);
			return this;
		}
		public MethodQuery declaredClass(final Pattern judgment) {
			this.declared.judgment(judgment);
			return this;
		}
		public MethodQuery declaredInstanceof(final Class cls) {
			this.declared.judgmentInstanceof(cls);
			return this;
		}
		public MethodQuery undeclaredClass() {
			this.declared.unjudgment();
			return this;
		}


		public boolean isJudgmentModifier()   { return modifier.isJudgmentModifier(); }
		public IFilter<Integer> modifier() { return modifier.modifier(); }
		public MethodQuery modifier(IFilter<Integer> judgment) {
			this.modifier.modifier(judgment);
			return this;
		}
		public MethodQuery modifierHas(final int hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public MethodQuery modifierHas(final String... hasModifier) {
			this.modifier.modifierHas(hasModifier);
			return this;
		}
		public MethodQuery unmodifier() {
			this.modifier.unjudgment();
			return this;
		}


		public boolean isJudgmentReturnType() { return retype.isJudgment(); }
		public IFilter<Class> returnType() { return retype.judgment(); }
		public MethodQuery returnType(IFilter<Class> judgment) {
			this.retype.judgment(judgment);
			return this;
		}
		public MethodQuery returnType(final Class clasz) {
			this.retype.judgment(clasz);
			return this;
		}
		public MethodQuery returnType(final String simpleName) {
			this.retype.judgment(simpleName);
			return this;
		}
		public MethodQuery returnType(final Pattern judgment) {
			this.retype.judgment(judgment);
			return this;
		}
		public MethodQuery returnTypeInstanceof(final Class cls) {
			this.retype.judgmentInstanceof(cls);
			return this;
		}
		public MethodQuery unreturnType() {
			this.retype.unjudgment();
			return this;
		}

		public boolean isJudgmentName()  { return name.isJudgmentName(); }
		public IFilter<String> name() { return name.name(); }
		public MethodQuery name(IFilter<String> judgment) {
			this.name.name(judgment);
			return this;
		}
		public MethodQuery name(String judgment) {
			this.name.name(judgment);
			return this;
		}
		public MethodQuery name(final Pattern judgment) {
			this.name.name(judgment);
			return this;
		}
		public MethodQuery unname() {
			this.name.unjudgment();
			return this;
		}



		public boolean isJudgmentParam()     { return paramTypes.isJudgmentParam(); }
		public IFilter<Class>[][] param() { return paramTypes.param(); }
		public MethodQuery addParamMatch(String... judgments0) {
			this.paramTypes.addParamMatch(judgments0);
			return this;
		}
		public MethodQuery addParamMatch(Class... judgments0) {
			this.paramTypes.addParamMatch(judgments0);
			return this;
		}
		public MethodQuery addParamMatchInstanceof(Class... judgments0) {
			this.paramTypes.addParamMatchInstanceof(judgments0);
			return this;
		}
		public MethodQuery addParamMatch(IFilter<Class>... judgments0) {
			this.paramTypes.addParamMatch(judgments0);
			return this;
		}
	  	public MethodQuery unparamMatch(int paramCount) {
			this.paramTypes.unjudgment(paramCount);
			return this;
		}
		public MethodQuery unparam() {
			this.paramTypes.unjudgmentAll();
			return this;
		}
	}
	public static class MethodLookup implements Cloneable {
		ReflectCache cache;
		Class defaultClass;
		public MethodLookup(ReflectCache cache) {
			this.cache  = cache;
			this.defaultClass = null;
		}
		public MethodLookup(ReflectCache cache, Class defaultClass) {
			this.cache  = cache;
			this.defaultClass = defaultClass;
		}

		public MethodQuery newQuery() { 
			return new MethodQuery();
		}

		ReflectCache.MethodList cacheList;
		ReflectCache.MethodList getMethodList() {
			if (null != cacheList)
				return  cacheList;
			if (null == defaultClass) 
				throw new UnsupportedOperationException("no default lookup class");
			return cacheList = cache.getMethodList(defaultClass);
		}
		public MethodLookup setMethodList(Method[] methods) {
			this.cacheList = ReflectCache.MethodList.wrap(methods);
			return this;
		}

		public Result<Method> findResult(MethodQuery... querys) {
			return new Result<Method>(find(querys));
		}
		public Result<Method> findResult(MethodQueryCollection querys) {
			return new Result<Method>(find(querys));
		}
		public static Result<Method> findResult(ReflectCache.MethodList methodList, MethodQueryCollection querys) {
			return new Result<Method>(find(methodList, querys));
		}

		public Method[] find(MethodQuery... querys) {
			return find(getMethodList(), MethodQueryCollection.create(querys));
		}
		public Method[] find(MethodQueryCollection querys) {
			return find(getMethodList(), querys);
		}
		public static Method[] find(ReflectCache.MethodList cacheList, MethodQueryCollection querys) {
			List<Method> result = new ArrayList<>();
			if (null != querys) {
				Set<MethodQuery> querySet = querys.query;
				if (querySet.size() > 0) {
					if (null != cacheList) {
						Method[] elements = cacheList.list();
						EACH: for (int i = 0; i < elements.length; i++) {
							Method  element = elements[i];
							Method  mSelf = element;

							Class   mDeclaredClass = element.getDeclaringClass();
							int     mModifier      = element.getModifiers();
							Class   mReturnType    = element.getReturnType();
							String  mName          = element.getName();
							Class[] mParamTypes    = cacheList.parameterTypes(i);

							for (MethodQuery query: querySet) {
								IFilter<Method> self = query.isJudgmentSelf()
									? query.self()
									: Filter.<Method> yes();
								if (self.next(mSelf)) {

									IFilter<Class> d = query.isJudgmentDeclared()
										? query.declaredClass()
										: Filter.<Class> yes();
									if (d.next(mDeclaredClass))	{

										IFilter<Integer> mod = query.isJudgmentModifier()
											? query.modifier()
											: Filter.<Integer> yes();
										if (mod.next(mModifier)) {

											IFilter<Class> rt = query.isJudgmentReturnType()
												? query.returnType()
												: Filter.<Class> yes();
											if (rt.next(mReturnType)) {

												IFilter<String> n = query.isJudgmentName()
													? query.name()
													: Filter.<String> yes();
												if (n.next(mName)) {

													if (query.paramTypes.match(mParamTypes)) {
														result.add(element);
														continue EACH;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return result.toArray(Finals.EMPTY_METHOD_ARRAY);
		}
	}














	public static class Result<T> implements Iterable<T> {
		T[] array;
		Iterable<T> iterable;

		protected Result(T[] array) {
			if (null == array)
				throw new NullPointerException("null == array");

			this.array = array;
			this.iterable = Lists.wrapArray(array, 0, array.length);
		}

		public T[] array() {
			return array;
		}

		public T[] toArray() {
			return Arrays.copyOf(array, array.length);
		}

		public T uniqueOne() {
			if (array.length == 1)
				return array[0];
			throw new UnsupportedOperationException("result.length != 1");
		}

		public int size()   { return array.length; }
		public T get(int i) { return array[i]; }

		@Override
		public String toString() {
			// TODO: Implement this method
			return Arrayz.toString(array);
		}

		@Override
		public Iterator<T> iterator() {
			// TODO: Implement this method
			return iterable.iterator();
		}
	}




	public boolean isDefault() { return this == DEFAULT; }
	public static final ReflectLookup DEFAULT = new ReflectLookup(ReflectCache.DEFAULT);




	public static Filter<Integer> modifierHas(final String mod) {
		if (null == mod || mod.length() == 0) 
			throw new NullPointerException();
		return new Filter<Integer>() {
			@Override
			public boolean next(Integer value) {
				// TODO: Implement this method
				return Modifier.toString(value).contains(mod);
			}
		};
	}

	public static Filter<Class> classEq(final Class clasz) {
		if (null == clasz) 
			throw new NullPointerException();
		return new Filter<Class>() {
			@Override
			public boolean next(Class value) {
				// TODO: Implement this method
				return clasz == value;
			}
		};
	}
	public static Filter<Class> classInstanceof(final Class clasz) {
		if (null == clasz) 
			throw new NullPointerException();
		return new Filter<Class>() {
			@Override
			public boolean next(Class value) {
				// TODO: Implement this method
				return Classz.isInstance(value, clasz, true);
			}
		};
	}

	public static Filter<Class> classCanonicalName(final String name) {
		if (null == name) 
			throw new NullPointerException();
		return new Filter<Class>() {
			@Override
			public boolean next(Class value) {
				// TODO: Implement this method
				if (name.equals(value.getName()))          return true;
				if (name.equals(value.getCanonicalName())) return true;
				return false;
			}
		};
	}
	public static Filter<Class> classCanonicalNameOrSimple(final String name) {
		if (null == name) 
			throw new NullPointerException();
		return new Filter<Class>() {
			@Override
			public boolean next(Class value) {
				// TODO: Implement this method
				if (name.equals(value.getName()))          return true;
				if (name.equals(value.getSimpleName()))    return true;
				if (name.equals(value.getCanonicalName())) return true;
				return false;
			}
		};
	}
	public static Filter<Class> classCanonicalNamePattern(final Pattern judgment) {
		if (null == judgment) 
			throw new NullPointerException();
		return new Filter<Class>() {
			@Override
			public boolean next(Class value) {
				// TODO: Implement this method
				Matcher matcher = judgment.matcher(value.getCanonicalName());
				if (matcher.find()) {
					return matcher.matches();
				}
				return false;
			}
		};
	}

	public static Filter<String> namePattern(final Pattern judgment) {
		if (null == judgment) 
			throw new NullPointerException();
		return new Filter<String>() {
			@Override
			public boolean next(String value) {
				// TODO: Implement this method
				Matcher matcher = judgment.matcher(value);
				if (matcher.find()) {
					return matcher.matches();
				}
				return false;
			}
		};
	}


}


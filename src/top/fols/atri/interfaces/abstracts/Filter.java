package top.fols.atri.interfaces.abstracts;

import top.fols.atri.interfaces.interfaces.IFilter;

import java.util.Objects;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Filter<T> implements IFilter<T> {
	@Override
	public abstract boolean next(T value);

	public Filter<T> or(final IFilter<T> judgment) {
		return new Filter<T>() {
			@Override
			public boolean next(T value) {
				// TODO: Implement this method
				boolean result = false;
				if (Filter.this.next(value)) {
					result = true;
				}
				if (judgment.next(value)) {
					result = true;
				} 
				return result;
			}
		};
	}
	
	public Filter<T> and(final IFilter<T> judgment) {
		return new Filter<T>() {
			@Override
			public boolean next(T value) {
				// TODO: Implement this method
				boolean result = true;
				if (!Filter.this.next(value)) {
					result = false;
				}
				if (!judgment.next(value)) {
					result = false;
				} 
				return result;
			}
		};
	}
	
	
	
	
	public static <T> Filter<T> or(final IFilter... judgments)  {
	    return new Filter<T>() {
			@Override
			public boolean next(T value) {
				// TODO: Implement this method
				if (judgments.length != 0) {
					for (IFilter<T> judgment: judgments) {
						if (judgment.next(value)) {
							return true;
						}
					}
				}
				return false;
			}
		};
	}
	
	public static <T> Filter and(final IFilter... judgments)  {
		return new Filter<T>() {
			@Override
			public boolean next(T value) {
				// TODO: Implement this method
				if (judgments.length != 0) {
					for (IFilter<T> judgment: judgments) {
						if (!judgment.next(value)) {
							return false;
						}
					}
					return true;
				}
				return false;
			}
		};
	}












	static final Filter YES = new Filter() {
		@Override
		public boolean next(Object value) {
			// TODO: Implement this method
			return true;
		}
	};
	static final Filter NO = new Filter() {
		@Override
		public boolean next(Object value) {
			// TODO: Implement this method
			return true;
		}
	};
	public static <T> Filter<T> yes() {
		return YES; 
	}
	public static <T> Filter<T> no() {
		return NO; 
	}

	public static <T> Filter<T> not(final IFilter<T> v) {
		return new Filter<T>() {
			@Override
			public boolean next(T value) {
				// TODO: Implement this method
				return ! v.next(value);
			}
		};
	}

	public static <T> Filter<T> identity(final T v) {
		return new Filter<T>() {
			@Override
			public boolean next(Object value) {
				// TODO: Implement this method
				return v == value;
			}
		}; 
	}
	public static <T> Filter<T> eq(final T v) {
		return new Filter<T>() {
			@Override
			public boolean next(Object value) {
				// TODO: Implement this method
				return Objects.equals(v, value);
			}
		}; 
	}

	
	
	

	



}

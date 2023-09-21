package top.fols.box.lang;

import top.fols.atri.lang.Value;

import java.io.Serializable;

@Deprecated
@SuppressWarnings("UnusedReturnValue")
public class Result <T, EX extends Throwable> extends Value<T> implements Serializable {
	private static final long serialVersionUID = 1L;





	public boolean 			isReturn() 					{ return null == error; }



	EX error;
	/**
	 * will clear tip
	 */
	public Result<T, EX> setError(EX exception) {
		super.set(null);
		this.error = exception;
		return this;
	}
	public boolean       isError()    { return !(null == error);}
	public EX            getError()   { return error; }
	public EX    		 clearError() { return error = null; }

	public void throwError() throws EX {
		if (isError()) {
			throw error;
		}
	}


	/**
	 * will clear error
	 */
	@Override
	public Result<T, EX> set(T value) {
		this.clearError();
		super.set(value);
		return this;
	}




	/**
	 * contains exception will  throw
	 */
	public T value() throws EX {
		if (isError()) {
			throw this.error;
		} else {
			return super.get();
		}
	}

	@Override
	public boolean release() {
		// TODO: Implement this method
		this.set(null);
		return true;
	}




	public Result<T, EX> from(Result<T, EX> from) {
		if (null == from) {
			super.set(null);
			this.setError(null);
		} else {
			super.set(from.get());
			this.setError(from.getError());
		}
		return this;
	}

	public interface Executor<T, EX extends Throwable> {
		T execute() throws EX;
	}



	@SuppressWarnings("unchecked")
	public void execute(Executor<T, EX> executor) {
		if (!(null == executor)) {
			try {
				set(executor.execute());
			} catch (Throwable e) {
				setError((EX) e);
			}
		}
	}








	public static class Silent<T> extends Result<T, Throwable> implements Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public Silent<T> from(Value<T> from) {
			super.from(from);
			return this;
		}

		@Override
		public Silent<T> from(Result<T, Throwable> from) {
			// TODO: Implement this method
			super.from(from);
			return this;
		}

		@Override
		public void execute(Result.Executor<T, Throwable> executor) {
			// TODO: Implement this method
			if (!(null == executor)) {
				try {
					set(executor.execute());
				} catch (Throwable e) {
					setError(e);
				}
			}
		}
	}









	public static boolean isReturn(Result<?, ? extends Throwable> result) {
		if (null == result) {
			return false;
		} else {
			return result.isReturn();
		}
	}

	public static <T> T get(Result<T, ?> result) {
		if (null == result) {
			return null;
		} else {
			return result.get();
		}
	}





}

package top.fols.atri.lang;

import java.io.Serializable;

public class Result <T, EX extends Throwable> extends Value<T> implements Serializable {
	private static final long serialVersionUID = 1L;





	public boolean 			isReturn() 					{ return null == error; }



	EX error;
	/**
	 * will clear value
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
	public T set(T value) {
		this.clearError();
		return super.set(value);
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









	public static boolean isReturn(Result result) {
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



	public static  Result<Object, Throwable> NULL() {
		return new Result<>();
	}
	public static  Result<Boolean, Throwable> TRUE() {
		Result<Boolean, Throwable> result = new Result<>();
		result.set(true);
		return result;
	}
	public static  Result<Boolean, Throwable> FALSE() {
		Result<Boolean, Throwable> result = new Result<>();
		result.set(false);
		return result;
	}


}

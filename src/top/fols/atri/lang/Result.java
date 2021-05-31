
import top.fols.atri.lang.Value;
import top.fols.atri.lang.Objects;

public class Result <T, EX extends Throwable> extends Value<T> {



	EX error;
	public Result<T, EX> setError(EX exception) {
		this.error = exception;
		return this;
	}
	public boolean       isError()    { return !(null == error);}
	public EX            getError()   { return error; }
	public EX    		 clearError() { return error = null; }



	public void thr() throws EX {
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
	 *
	 */
	public T value() throws EX {
		if (isError()) {
			throw this.error;
		} else {
			return super.get();
		}
	}









	public static <T> boolean isBoolean(Value<T> value) {
		return null != value && value.get() instanceof Boolean;
	}
	public static <T> boolean isTrue(Value<T> value) {
		return Objects.parseBoolean(value);
	}



	public static final Value<Boolean> TRUE  = new Value<Boolean>(true) {
		@Override
		public boolean release() {
			// TODO: Implement this method
			throw new UnsupportedOperationException();
		}
		@Override
		public Boolean set(Boolean p1) {
			// TODO: Implement this method
			throw new UnsupportedOperationException();
		}
	};
	public static final Value<Boolean> FALSE = new Value<Boolean>(false) {
		@Override
		public boolean release() {
			// TODO: Implement this method
			throw new UnsupportedOperationException();
		}
		@Override
		public Boolean set(Boolean p1) {
			// TODO: Implement this method
			throw new UnsupportedOperationException();
		}
	};


}

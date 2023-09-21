package top.fols.box.reflect.re;

import top.fols.atri.interfaces.annotations.NotNull;

/**
 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
 */
@SuppressWarnings("ALL")
public interface Re_IRe_Object {

	/**
	 * 底层实现的对象
	 */
	public boolean isPrimitive();


	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 */
	public Object getObjectValue(Re_Executor executor, Object key) throws Throwable;


	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 */
	public void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable;






	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 */
	public boolean hasObjectKey(Re_Executor executor, Object key) throws Throwable;

	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 */
	public boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable;

	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 */
	public int getObjectKeyCount(Re_Executor executor) throws Throwable;

	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 */
	public Iterable getObjectKeys(Re_Executor executor) throws Throwable;

	/**
	 * 如果返回 ture 则 {@link #getObjectKeys} 不可以返回null
	 */
	public boolean hasObjectKeys();

















	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 *
	 *
	 * 如果你不想处理，建议使用 {@link Re_IRe_Object#executePoint(Re_Executor, Object, Re_CodeLoader.Call)}
	 *
	 * 假定本对象名称x
	 * 那么执行的是 x.x()
	 * @param point_key          指子变量名称，假设这是个map里面有个a
	 *                            执行的就是map.a();
	 * @param call 如果是true 则callParam 为空 ，如果false则 callParam会经过计算后传入
	 */
	public Object executePoint(Re_Executor executor, Object point_key,
							   Re_CodeLoader.Call call) throws Throwable;




	/**
	 * 执行之前不可能已经return， 如果中间有执行了表达式应该判断表达式是否已经return 如果已经return 则返回return数据 而不是继续操作, 如果已经return返回的任何数据都是无效的
	 * 只要参数含有 {@link Re_Executor} 的方法 后都要手动检测是否 {@link Re_Executor#isReturnOrThrow()} 如果是直接返回null
	 *
	 * <p>
	 * 假定本对象名称x
	 * 那么执行的是 x()
	 *
	 * @param call     如果是true 则callParam 为空 ，如果false则 callParam会经过计算后传入
	 */
	public Object executeThis(Re_Executor executor,
							  Re_CodeLoader.Call call) throws Throwable;



	public String getName();
















	public static abstract class IPrimitiveObject implements Re_IRe_Object {
		@Override
		public boolean isPrimitive() {
			return true;
		}

		@Override
		public String toString() {
			return getName();
		}
	}


	/**
	 * 只执行 call()
	 * 其他任何操作无效
	 *  只要用了{@link Re_Executor#getExpressionValue(Re_CodeLoader.Call, int)} 后都要手动检测是否return
	 *
	 *  除了表达式和计算方法其他方法都应该检测每一个参数，保证每个参数被执行
	 */
	public static abstract class IPrimitiveCall implements Re_IRe_Object {
		final String callName;
		public IPrimitiveCall(String callName) {
			this.callName = callName;
		}


		@Override
		public String getName() {
			return callName;
		}


		@Override public boolean isPrimitive() { return true; }
		@Override public boolean hasObjectKeys() { return false; }

		@Override
		public String toString() {
			return getName();
		}




		@Override
		public final boolean hasObjectKey(Re_Executor executor, Object key) throws Throwable {
			return false;
		}
		@Override
		public final boolean removeObjectKey(Re_Executor executor, Object key) throws Throwable {
			return false;
		}
		@Override
		public final Object getObjectValue(Re_Executor executor, Object key) throws Throwable {
			return null;
		}
		@Override
		public final void putObjectValue(Re_Executor executor, Object key, Object value) throws Throwable {
			return;
		}
		@Override
		public final int getObjectKeyCount(Re_Executor executor) throws Throwable {
			return 0;
		}
		@Override
		public @NotNull final Iterable getObjectKeys(Re_Executor executor) throws Throwable {
			return null;
		}

		@Override
		public final Object executePoint(Re_Executor executor, Object point_key, Re_CodeLoader.Call call) throws Throwable {
			String s = Re_Utilities.toJString(point_key);
			executor.setThrow(Re_Accidents.undefined(this, s));
			return null;
		}

	}
}

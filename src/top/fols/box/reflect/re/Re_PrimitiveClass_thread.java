package top.fols.box.reflect.re;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.lock.LockAwait;
import java.util.concurrent.TimeUnit;
import top.fols.atri.lang.Objects;
import top.fols.box.reflect.Reflectx;

public class Re_PrimitiveClass_thread extends Re_PrimitiveClass {

	protected Re_PrimitiveClass_thread(Re re) {
		this(re, Re_Keywords.INNER_CLASS__THREAD);
	}
    protected Re_PrimitiveClass_thread(Re re, String className) {
        super(re, className, new Re_PrimitiveClass.InitializedBefore() {
				final String FIELD_RUNNER = "run";

				final String FUNCN_START0          = "start0";
				final String FUNCN_START           = "start";
				final String FUNCN_SLEEP           = "sleep";
				final String FUNCN_IS_THREAD       = "is_thread";
				final String FUNCN_IS_Start        = "is_start";

				final String FUNCN_interrupt       = "interrupt";
				final String FUNCN_IS_interrupted  = "is_interrupt";

				final String FUNCN_AWAIT           = "await";
				final String FUNCN_AWAIT_TIME      = "await_time";
				@Override
				public void doExecute(final Re_PrimitiveClass pClass) {
					// TODO: Implement this method
					pClass.addInit(Reflectx.getCallLine(),
                        "final", new Re_PrimitiveClassMyCVF.IInit() {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								if (arguments.length > 0) {
									Re_Variable.accessSetValue(executor, FIELD_RUNNER, arguments[0], runInInstance);
									if (executor.isReturnOrThrow()) return null;
								} else {
									executor.setThrow("null runner");
									return null;
								}
                                return null;
                            }
                        });



					pClass.addVariable(Reflectx.getCallLine(),
									   "final this",   FIELD_RUNNER);




					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_START0) {
                            @Override
                            public Async unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
                                if (null == runInInstance) {
                                    executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
                                    return null;
                                }
								synchronized (runInInstance) {
									Instance instance = (Instance) runInInstance;
									Object runner0 = Re_Variable.accessGetInstanceOrClassValue(executor, FIELD_RUNNER, runInInstance);
									if (executor.isReturnOrThrow()) return null;

									if (!Re_Utilities.isReFunction(runner0)) {
										executor.setThrow(Re_Accidents.unsupported_type("runner", Re_Utilities.objectAsName(runner0), Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION));
										return null;
									}

									if (null != instance.bind) {
										executor.setThrow("started");
										return null;
									}

									MyReThread bind;
									bind = new MyReThread(executor.getRe(), executor.getStackElement(), (Re_ClassFunction) runner0);
									instance.bind = bind;
									instance.bind.start();
									return instance;
								}
                            }
                        });
					pClass.addFunction(Reflectx.getCallLine(),
                        "struct", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_START) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								if (null == runInInstance) {
                                    executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
                                    return null;
                                }
								return Re_Class.SafesRe.executeInstanceOrClassFunction(executor, runInInstance, FUNCN_START0, Finals.EMPTY_OBJECT_ARRAY, null);
                            }
                        });
					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_SLEEP) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
                                Object time0 = arguments.length > 0 ? arguments[0] : null;
                                if (time0 instanceof Number) {
                                    long t = ((Number)time0).longValue();
                                    Thread.sleep(t > 0 ?t: 0);
                                } else {
                                    executor.setThrow(Re_Accidents.unsupported_type("time", Re_Utilities.objectAsName(time0), "number"));
                                    return null;
                                }
                                return null;
                            }
                        });



					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_IS_THREAD) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								Object obj;
								if (arguments.length == 0) {
									obj = runInInstance;
								} else {
									obj = Objects.first(arguments);
								}
								return obj instanceof Instance;
                            }
                        });
					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_IS_Start) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								Object   instance0 = Objects.nvl(runInInstance, Objects.opt(arguments, 0));
								Instance instance  = Objects.cast(instance0, JAVA_CLASS_INSTANCE);
								if (null == instance) {
									executor.setThrow(Re_Accidents.unsupported_type("instance", Re_Utilities.objectAsName(instance0), runInClass.getName()));
									return null;
								}
								return instance.isStarted();
                            }
                        });



					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_interrupt) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								Object   instance0 = Objects.nvl(runInInstance, Objects.opt(arguments, 0));
								Instance instance  = Objects.cast(instance0, JAVA_CLASS_INSTANCE);
								if (null == instance) {
									executor.setThrow(Re_Accidents.unsupported_type("instance", Re_Utilities.objectAsName(instance0), runInClass.getName()));
									return null;
								}
								return instance.interrupt();
                            }
                        });
					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_IS_interrupted) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								Object   instance0 = Objects.nvl(runInInstance, Objects.opt(arguments, 0));
								Instance instance  = Objects.cast(instance0, JAVA_CLASS_INSTANCE);
								if (null == instance) {
									executor.setThrow(Re_Accidents.unsupported_type("instance", Re_Utilities.objectAsName(instance0), runInClass.getName()));
									return null;
								}
								return instance.isInterrupted();
                            }
                        });



					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_AWAIT) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								Object   instance0 = Objects.nvl(runInInstance, Objects.opt(arguments, 0));
								Instance instance  = Objects.cast(instance0, JAVA_CLASS_INSTANCE);
								if (null == instance) {
									executor.setThrow(Re_Accidents.unsupported_type("instance", Re_Utilities.objectAsName(instance0), runInClass.getName()));
									return null;
								}
								return instance._await(executor);
                            }
                        });
					pClass.addFunction(Reflectx.getCallLine(),
                        "struct final", new Re_PrimitiveClassMyCVF.IFunction(FUNCN_AWAIT_TIME) {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                // TODO: Implement this method
								if (arguments.length == 1) {
									Instance instance = (Instance) runInInstance;
									if (null == instance) {
										executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
										return null;
									} 
									
									Number time0 = Objects.cast(arguments[0], JAVA_CLASS_NUMBER);
									if (null == time0) {
										executor.setThrow(Re_Accidents.unsupported_type("time", Re_Utilities.objectAsName(time0), JAVA_CLASS_NUMBER.getSimpleName()));
										return null;
									}
									long time = time0.longValue() < 0 ? 0L: time0.longValue();

									return instance._await(executor, time);
								} else if (arguments.length == 2) {
									Object instance0 = Objects.nvl(runInInstance, arguments[0]);
									Instance instance = Objects.cast(instance0, JAVA_CLASS_INSTANCE);
									if (null == instance) {
										executor.setThrow(Re_Accidents.unsupported_type("instance", Re_Utilities.objectAsName(instance0), runInClass.getName()));
										return null;
									}

									Number time0 = Objects.cast(arguments[1], JAVA_CLASS_NUMBER);
									if (null == time0) {
										executor.setThrow(Re_Accidents.unsupported_type("time", Re_Utilities.objectAsName(time0), JAVA_CLASS_NUMBER.getSimpleName()));
										return null;
									}
									long time = time0.longValue() < 0 ? 0L: time0.longValue();

									return instance._await(executor, time);
								} else {
									executor.setThrow("(thread: threadInstance, time: Number)");
									return null;
								}
                            }
                        });
				}
			});
    }


    @Override
    protected Instance newUndefinedInstance(Re_Class reClass) {
        return new Instance(reClass);
    }
	public static final Class<Number>   JAVA_CLASS_NUMBER   = Number.class;
	public static final Class<Instance> JAVA_CLASS_INSTANCE = Instance.class;
    public static final class Instance extends Re_PrimitiveClassInstance implements Async {
		@Override
		public boolean _await(Re_Executor call) throws InterruptedException {
			if (null == bind) return false;
			return bind._await(call);
		}
		@Override
		public boolean _await(Re_Executor call, long time) throws InterruptedException {
			if (null == bind) return false;
			return bind._await(call, time);
		}
		@Override
		public boolean _is_lock() {
			if (null == bind) return false;
			return bind._is_lock();
		}
		@Override
		public boolean _is_unlock() {
			if (null == bind) return false;
			return bind._is_unlock();
		}

		public boolean interrupt() {
			if (null == bind) return false;
			bind.interrupt();
			return true;
		}
		public boolean isInterrupted() {
			if (null == bind) return false;
			return bind._is_interrupt();
		}


		MyReThread bind;

        protected Instance(Re_Class reClass) {
            super(reClass);
        }

		protected boolean    isStarted() { return null != bind; }
		protected MyReThread getThread() { return bind; }
    }


	@SuppressWarnings("UnnecessaryInterfaceModifier")
	static interface Async {
		public boolean _await(Re_Executor call) throws InterruptedException;
		public boolean _await(Re_Executor call, long time) throws InterruptedException;
		public boolean _is_lock() ;
		public boolean _is_unlock();
	}
	static class MyReThread extends Thread implements Async {
		@Override
		public boolean _await(Re_Executor call) throws InterruptedException {
			this.locks.await();
			return true;
		}
		@Override
		public boolean _await(Re_Executor call, long time) throws InterruptedException {
			return this.locks.await(time, TimeUnit.MILLISECONDS);
		}
		@Override
		public boolean _is_lock() {
			return locks.isLock();
		}
		@Override
		public boolean _is_unlock() {
			return locks.isUnlock();
		}
		public boolean _interrupt() {
			super.interrupt();
			return true;
		}
		public boolean _is_interrupt() {
			return super.isInterrupted();
		}



        final Re 		       re;
        final Re_NativeStack   reStack;
        final Re_ClassFunction reFun;
		final LockAwait locks;


		MyReThread(@NotNull Re re, @NotNull Re_NativeStack.ReNativeTraceElement nte,
				   @NotNull Re_ClassFunction runner) {
            this.re = re;
            this.reStack = Re_NativeStack.newThreadStack(re, nte);
            this.reFun = runner;
			this.locks = new LockAwait();
        }

        @SuppressWarnings("UnnecessaryReturnStatement")
        @Override
        public void run() {
            // TODO: Implement this method
			try {
				Re_Executor executor = Re_Class.Safes.createExecutorOrThrowEx(re, reStack);
				if  (executor.isThrow()) {
					Re_PrimitiveClass_exception.Instance e = executor.getThrow();
					re.printlnErr(e.asString());
					return;
				}

				Exec: try {
					Re_Class.SafesRe.executeFunction(executor, reFun, Finals.EMPTY_OBJECT_ARRAY, null);
				} catch (Throwable e) {
					if (executor.isReturnOrThrow()) {
						break Exec;
					} else {
						executor.setThrowFromJavaExceptionToStringAsReason(e);
					}
				}

				if (executor.isThrow()) {
					Re_PrimitiveClass_exception.Instance e = executor.getThrow();
					re.printlnErr(e.asString());
					return;
				}
			} finally {
				locks.unlock();
			}
        }
    }


}


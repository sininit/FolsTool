package top.fols.box.reflect.re;

import top.fols.box.reflect.Reflectx;
import top.fols.box.reflect.re.Re_PrimitiveClassMyCVF.*;

import static top.fols.box.reflect.re.Re_NativeStack.ReTraceElement;

public class Re_PrimitiveClass_exception extends Re_PrimitiveClass {
    public static Object[] buildNewInstanceParameters(String throwReason, boolean isFillExecutorTrace) {
        return new Object[] { throwReason, isFillExecutorTrace };
    }

    protected Re_PrimitiveClass_exception(Re re) {
        this(re, Re_Keywords.INNER_CLASS__EXCEPTION);
    }
    protected Re_PrimitiveClass_exception(Re re, String className) {
        super(re, className, new InitializedBefore() {
            @Override
            public void doExecute(Re_PrimitiveClass thatClass) {
                thatClass.addInit(Reflectx.getCallLine(), Re_Modifiers.asString(),
                        new IInit() {
                    @Override
                    public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                        int length = arguments.length;
                        Instance eInstance = (Instance) runInInstance;

                        String throwReason = null;
                        if (length > 0) {
                            throwReason = Re_Utilities.toJString(arguments[0]);
                        }
                        eInstance.setThrowReason(throwReason);

                        boolean fillExecutorTrace = true;
                        if (length > 1) {
                            fillExecutorTrace = Re_Utilities.toJBoolean(arguments[1]);
                        }

                        if (fillExecutorTrace) {
                            Re_NativeStack.fillExceptionInstanceTraceElements(eInstance, executor.getStack());
                        }
                        return null;
                    }
                });

                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.STRUCT),
                        new IFunction("message") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 0) {
                                    Instance e = (Instance) runInInstance;
                                    return e.getReason();
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.STRUCT),
                        new IFunction("stack") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 0) {
                                    Instance e = (Instance) runInInstance;
                                    return e.getStackElements();
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
            }
        });
    }






    @Override
    protected Instance newUndefinedInstance(Re_Class reClass) {
        return new Instance(reClass);
    }




    public static final class Instance extends Re_PrimitiveClassInstance {
        protected Instance(Re_Class reClass) {
            super(reClass);
        }

        @Override
        Instance superClone() {
            Instance instance    = (Instance) super.superClone();
            instance.throwReason = this.throwReason;
            instance.traces      = new ReTraceElement[this.traces.length];
            for (int i = 0; i < this.traces.length; i++) {
                instance.traces[i] = this.traces[i].clone();
            }
            return instance;
        }

        //reason
        String throwReason;
        //safe
        ReTraceElement[] traces;

        public void setThrowReason(String throwReason) {
            this.throwReason = throwReason;
        }
        public void setStackElements(ReTraceElement[] traces) {
            this.traces = traces;
        }

        public String getReason() {
            return throwReason;
        }
        public ReTraceElement[] getStackElements() {
            return traces;
        }

        public String asString() {
            return Re_NativeStack.buildExceptionString(this);
        }



        @Override
        public String toString() {
            return asString();
        }
    }
}

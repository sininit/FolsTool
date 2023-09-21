package top.fols.box.reflect.re;

import top.fols.atri.lang.Finals;
import top.fols.box.reflect.Reflectx;

import static top.fols.box.reflect.re.Re_Variable.TRUE;

public class Re_PrimitiveClass_reflect extends Re_PrimitiveClass {


    protected Re_PrimitiveClass_reflect(Re re) {
        this(re, Re_Keywords.INNER_CLASS__REFLECT);
    }
    protected Re_PrimitiveClass_reflect(Re re, String className) {
        super(re, className, new InitializedBefore() {
            @Override
            public void doExecute(Re_PrimitiveClass thatClass) {
                thatClass.addInit(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IInit() {
                    @Override
                    public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                        executor.setThrow("cannot new instance");
                        return null;
                    }
                });


                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("getName") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 1) {
                                    Object ireobject = arguments[0];
                                    if (ireobject instanceof Re_IRe_Object) {
                                        return ((Re_IRe_Object) ireobject).getName();
                                    }

                                    executor.setThrow(Re_Accidents.unsupported_type(Re_Utilities.objectAsName(ireobject)));
                                    return null;
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("getPackageName") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 1) {
                                    Object reClass = arguments[0];
                                    if (Re_Utilities.isReClass(reClass)) {
                                        return ((Re_Class) reClass).getReClassPackageName();
                                    }

                                    executor.setThrow(Re_Accidents.unsupported_type(Re_Keywords.INNER_EXPRESSION_CALL__CLASS, Re_Utilities.objectAsName(reClass), Re_Keywords.INNER_EXPRESSION_CALL__CLASS));
                                    return null;
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("getFunctionParams") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 1) {
                                    Object reFunction = arguments[0];
                                    if (Re_Utilities.isReFunction(reFunction)) {
                                        Re_ClassFunction function = ((Re_ClassFunction) reFunction);
                                        String[] strings = new String[function.getParamCount()];
                                        for (int i = 0; i< strings.length; i++){
                                            strings[i] = function.getParamName(i);
                                        }
                                        return strings;
                                    }
                                    executor.setThrow(Re_Accidents.unsupported_type(Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION, Re_Utilities.objectAsName(reFunction), Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION));
                                    return null;
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("getFunctionParent") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 1) {
                                    Object ClsOrFun = arguments[0];
                                    if (Re_Utilities.isReFunction(ClsOrFun)) {
                                        Re_ClassFunction function = ((Re_ClassFunction) ClsOrFun);
                                        return function.getDeclareExecutor();
                                    }

                                    executor.setThrow(Re_Accidents.unsupported_type(Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION, Re_Utilities.objectAsName(ClsOrFun), Re_Keywords.INNER_EXPRESSION_CALL__FUNCTION));
                                    return null;
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("getConstructor") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 1) {
                                    Object reClassObject = arguments[0];
                                    if (Re_Utilities.isReClass(reClassObject)) {
                                        Re_Class reClass = (Re_Class) reClassObject;
                                        return reClass.getInitFunction();
                                    }

                                    executor.setThrow(Re_Accidents.unsupported_type(Re_Keywords.INNER_EXPRESSION_CALL__CLASS, Re_Utilities.objectAsName(reClassObject), Re_Keywords.INNER_EXPRESSION_CALL__CLASS));
                                    return null;
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("copyAttr") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                Re_Class runClass = executor.getReClass();
                                if (null == runClass) {
                                    executor.setThrow(Re_Accidents.executor_no_bind_class());
                                    return null;
                                }
                                for (Object argument : arguments) {
                                    Re_Class reClass = Re_Utilities.getReClassFromIReGetClass(argument);
                                    if (null == reClass)
                                        continue;

                                    Iterable<?> variableKeys = reClass.getObjectKeys(executor);
                                    if (executor.isReturnOrThrow()) return null;

                                    for (Object k : variableKeys) {
                                        Re_Variable<?> variable = Re_Variable.Unsafes.cloneVariable(k, reClass);

                                        Re_Variable.accessPutNewVariableRequire(executor, k, variable, runClass);
                                        if (executor.isReturnOrThrow()) return null;
                                    }
                                }
                                return TRUE.get();
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("invokeInit") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 2) {
                                    Re_Class runClass = executor.getReClass();
                                    if (null == runClass) {
                                        executor.setThrow(Re_Accidents.executor_no_bind_class());
                                        return null;
                                    }
                                    Re_ClassInstance runInstance = executor.getReClassInstance();
                                    if (null == runInstance) {
                                        executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
                                        return null;
                                    }

                                    //需要执行的特定的类构造器
                                    Re_Class findClass = Re_Utilities.getReClassFromIReGetClass(arguments[0]);
                                    if (null != arguments[0] && !Re_Utilities.isReClass(findClass)) {
                                        executor.setThrow("not a reClass: " + Re_Utilities.objectAsName(arguments[0]));
                                        return null;
                                    }
                                    Re_ClassFunction findFunction = findClass.getInitFunction();
                                    if (null == findFunction) {
                                        return null;
                                    }

                                    Object[] argumentArr;
                                    Object   argumentArr0 = arguments[1];
                                    if (null == argumentArr0) {
                                        argumentArr = Finals.EMPTY_OBJECT_ARRAY;
                                    } else {
                                        argumentArr = Re_Utilities.toarray(executor, argumentArr0);
                                        if (executor.isReturnOrThrow()) return null;
                                    }

                                    //为null 执行时自动生成
                                    return Re_Class.UnsafesRe.executeFunction(executor.getRe(), executor.getStack(),
                                            findFunction, runClass, runInstance,
                                            argumentArr, null);
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("invokeFunction") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 4) {

                                    Re_Class reClass = Re_Utilities.getReClassFromIReGetClass(arguments[0]);
                                    if (null != arguments[0] && !Re_Utilities.isReClass(reClass)) {
                                        executor.setThrow("not a reClass: " + Re_Utilities.objectAsName(arguments[0]));
                                        return null;
                                    }

                                    Re_ClassInstance reInstance;
                                    if (null != arguments[1] && !Re_Utilities.isReClassInstance(arguments[1])) {
                                        executor.setThrow("not a reClassInstance: " + Re_Utilities.objectAsName(arguments[1]));
                                        return null;
                                    } else {
                                        reInstance = (Re_ClassInstance)arguments[1];
                                    }

                                    Re_ClassFunction reClassFunction;
                                    if (null == arguments[2])
                                        return null;
                                    if (!Re_Utilities.isReFunction(arguments[2])) {
                                        executor.setThrow("not a reClassFunction: " + Re_Utilities.objectAsName(arguments[2]));
                                        return null;
                                    } else {
                                        reClassFunction = (Re_ClassFunction) arguments[2];
                                    }

                                    Object[] argumentArr;
                                    Object   argumentArr0 = arguments[3];
                                    if (null == argumentArr0) {
                                        argumentArr = Finals.EMPTY_OBJECT_ARRAY;
                                    } else {
                                        argumentArr = Re_Utilities.toarray(executor, argumentArr0);
                                        if (executor.isReturnOrThrow()) return null;
                                    }

                                    //为null 执行时自动生成
                                    return Re_Class.UnsafesRe.executeFunction(executor.getRe(), executor.getStack(),
                                            reClassFunction, reClass, reInstance,
                                            argumentArr, null);
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.FINAL),
                        new Re_PrimitiveClassMyCVF.IFunction("invoke") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                int length = arguments.length;
                                if (length == 3) {
                                    //(instance, name, arguments);

                                    Object pFunction = arguments[1];
                                    Object point_key;
                                    if (Re_Utilities.isReFunction(pFunction)) {
                                        point_key = ((Re_ClassFunction) pFunction).getName();
                                    } else {
                                        point_key = pFunction;
                                    }

                                    Re_Class         reClass;
                                    Re_ClassInstance reClassInstance;
                                    Re_ClassFunction reClassFunction;

                                    Object pInstance = arguments[0];
                                    if (Re_Utilities.isReClass(pInstance)) {
                                        reClassInstance = null;
                                        reClass = (Re_Class) pInstance;
                                        Object o = Re_Variable.accessGetClassValue(executor, point_key, reClass);
                                        if (executor.isReturnOrThrow()) return null;

                                        if (Re_Utilities.isReFunction(o)) {
                                            reClassFunction = (Re_ClassFunction) o;
                                        } else {
                                            String s = Re_Utilities.toJString(point_key);
                                            executor.setThrow(Re_Accidents.undefined(reClass, s));
                                            return null;
                                        }
                                    } else if (Re_Utilities.isReClassInstance(pInstance)) {
                                        reClassInstance = (Re_ClassInstance) pInstance;
                                        reClass = reClassInstance.getReClass();
                                        Object o = Re_Variable.accessGetInstanceOrClassValue(executor, point_key, reClassInstance);
                                        if (executor.isReturnOrThrow()) return null;

                                        if (Re_Utilities.isReFunction(o)) {
                                            reClassFunction = (Re_ClassFunction) o;
                                        } else {
                                            String s = Re_Utilities.toJString(point_key);
                                            executor.setThrow(Re_Accidents.undefined(reClassInstance, s));
                                            return null;
                                        }
                                    } else {
                                        executor.setThrow("not a reClass or reClassInstance: "+ Re_Utilities.objectAsName(pInstance));
                                        return null;
                                    }

                                    Object   pArgs_  = arguments[2];
                                    Object[] args;
                                    if (null == pArgs_) {
                                        args = Finals.EMPTY_OBJECT_ARRAY;
                                    } else {
                                        args = Re_Utilities.toarray(executor, pArgs_);
                                        if (executor.isReturnOrThrow()) return null;
                                    }

                                    //为null 执行时自动生成
                                    return Re_Class.UnsafesRe.executeFunction(executor.getRe(), executor.getStack(),
                                            reClassFunction, reClass, reClassInstance,
                                            args, null);
                                }
                                executor.setThrow(Re_Accidents.unable_to_process_parameters(name, length));
                                return null;
                            }
                        });
            }
        });
    }


    @Override
    protected Re_PrimitiveClassInstance newUndefinedInstance(Re_Class reClass) {
        return new Re_PrimitiveClassInstance(reClass);
    }
}

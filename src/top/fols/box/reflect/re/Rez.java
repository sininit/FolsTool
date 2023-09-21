package top.fols.box.reflect.re;

import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Finals;

import static top.fols.box.reflect.re.Re_Class.SafesRe.createInstance;
import static top.fols.box.reflect.re.Re_Keywords.*;

/**
 * 快速的匿名访问
 * 应该直接
 */
public class Rez {
    public static class SafesRe {
        public static Re_ClassInstance createInstance_object(Re_Executor executor) throws Throwable {
            return Re_Class.SafesRe.createInstance(executor,
                    executor.re.class_object(),
                    null, null);
        }
        public static Re_PrimitiveClass_list.Instance createInstance_list(Re_Executor executor) throws Throwable {
            Re_ClassInstance instance = createInstance(executor,
                    executor.re.class_list(),
                    null, null);
            if (Re_Utilities.isReClassInstance_list(instance)) {
                return (Re_PrimitiveClass_list.Instance) instance;
            } else {
                executor.setThrow("new instance type is not supported: " + Classz.getName(instance));
                return null;
            }
        }
        public static Re_ClassInstance createInstance_json(Re_Executor executor) throws Throwable {
            return createInstance(executor,
                    executor.re.class_json(),
                    null, null);
        }

    }

    /**
     * 每次访问都会创建一个匿名执行器
     */
    public static class Safes {
        public Re_ClassInstance createInstanceOrThrowEx_object(Re re) {
            return newInstanceOrThrowEx(re.class_object(),
                    Finals.EMPTY_OBJECT_ARRAY);
        }
        public static Re_PrimitiveClass_exception.Instance createInstanceOrThrowEx_exception(Re re,
                                                                                             String throwReason, Re_NativeStack stack) {
            return Re_Class.Safes.createInstance_exception(re, re.class_exception(),
                    throwReason, stack);
        }





        final Re re;

        public Safes(Re temp) {
            this.re = temp;
        }





        public Re_ClassInstance newInstanceOrThrowEx(Re_Class reClass, Object[] arguments) {
            Re_NativeStack stack = re.newStack();
            try {
                return Re_Class.Safes.createInstanceOrThrowEx(
                        re, stack,
                        reClass, arguments,
                        null
                );
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public void setClassValueOrThrowEx(Re_Class reClass,
                                           Object key, Object value) {
            Re_NativeStack stack = re.newStack();
            Re_Class.Safes.setClassValueOrThrowEx(
                    re, stack,
                    reClass,
                    key, value);
        }
        public Object getClassValueOrThrowEx(Re_Class reClass,
                                             Object key) {
            Re_NativeStack stack = re.newStack();
            return Re_Class.Safes.getClassValueOrThrowEx(
                    re, stack,
                    reClass,
                    key);
        }

        public void setInstanceValueOrThrowEx(Re_ClassInstance instance,
                                              Object key, Object value) {
            Re_NativeStack stack = re.newStack();
            Re_Class.Safes.setInstanceValueOrThrowEx(re, stack,
                    instance,
                    key, value);
        }
        public Object getInstanceOrClassValueOrThrowEx(Re_ClassInstance instance,
                                                       Object key) {
            Re_NativeStack stack = re.newStack();
            return Re_Class.Safes.getInstanceOrClassValueOrThrowEx(
                    re, stack,
                    instance,
                    key);
        }

        public Re_ClassFunction getFunctionFromClassOrThrowEx(Re_Class reClass,
                                                              Object key) {
            Re_NativeStack stack = re.newStack();
            return Re_Class.Safes.getFunctionFromClassOrThrowEx(re, stack,
                    reClass,
                    key);
        }
        public Re_ClassFunction getFunctionFromInstanceOrClassOrThrowEx(Re_ClassInstance instance,
                                                                        Object key) {
            Re_NativeStack stack = re.newStack();
            return Re_Class.Safes.getFunctionFromInstanceOrClassOrThrowEx(re, stack,
                    instance,
                    key);
        }


        public Object executeClassFunctionOrThrowEx(Re_Class fromReClassGet,
                                                    Object key,
                                                    Object[] args) {
            Re_NativeStack stack = re.newStack();
            try {
                return Re_Class.Safes.executeClassFunctionOrThrowEx(re, stack,
                        fromReClassGet,
                        key, args,
                        null);
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        public Object executeInstanceOrClassFunctionOrThrowEx(Re_ClassInstance fromReClassInstanceGet,
                                                              Object key,
                                                              Object[] args) {
            Re_NativeStack stack = re.newStack();
            try {
                return Re_Class.Safes.executeInstanceOrClassFunctionOrThrowEx(re, stack,
                        fromReClassInstanceGet,
                        key, args,
                        null);
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        public Object executeFunctionOrThrowEx(Re_ClassFunction function, Object[] args) {
            Re_NativeStack stack = re.newStack();
            try {
                return Re_Class.Safes.executeFunctionOrThrowEx(re, stack,
                        function,
                        args, null);
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

    }





}

package top.fols.box.reflect.re;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.box.reflect.Reflectx;
import top.fols.atri.util.Lists;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

import static top.fols.box.reflect.re.Re_Variable.FALSE;
import static top.fols.box.reflect.re.Re_Variable.TRUE;

/**
 * {对象} 和 它的抽象 {类}
 */
@SuppressWarnings("rawtypes")
public class Re_PrimitiveClass_list extends Re_PrimitiveClass {

    protected Re_PrimitiveClass_list(Re re) {
        this(re, Re_Keywords.INNER_CLASS__LIST);
    }
    protected Re_PrimitiveClass_list(Re re, String className) {
        super(re, className, new InitializedBefore() {
            @Override
            public void doExecute(Re_PrimitiveClass thatClass) {
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.STRUCT),
                        new Re_PrimitiveClassMyCVF.IFunction("add") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                if (runInInstance instanceof Instance) {
                                    Instance instance_ = (Instance) runInInstance;
                                    int length = arguments.length;
                                    if (length != 0) {
                                        for (int i = 0; i < arguments.length; i++) {
                                            instance_.addElement(arguments[i]);
                                        }
                                        return arguments[arguments.length-1];
                                    }
                                } else {
                                    executor.setThrow("not a reListInstance");
                                }
                                return null;
                            }
                        });
                thatClass.addFunction(Reflectx.getCallLine(), Re_Modifiers.asString(Re_Modifiers.STRUCT),
                        new Re_PrimitiveClassMyCVF.IFunction("setSize") {
                            @Override
                            public Object unauthenticationExecuteOnNative(Re_Executor executor, Re_Class runInClass, Re_ClassInstance runInInstance, Object[] arguments) throws Throwable {
                                if (!(runInInstance instanceof Instance)) {
                                    executor.setThrow("not a reListInstance");
                                    return null;
                                }

                                Instance instance_ = (Instance) runInInstance;
                                int length = arguments.length;
                                if (length == 1) {
                                    int i = Re_Utilities.toJInt(arguments[0]);
                                    instance_.setSize(i);
                                    return TRUE.get();
                                }
                                return FALSE.get();
                            }
                        });
            }
        });
    }





    @Override
    protected Re_PrimitiveClass_list.Instance newUndefinedInstance(Re_Class reClass) {
        return new Re_PrimitiveClass_list.Instance(reClass, Re.newPrimitiveListVariableMap());
    }


    @SuppressWarnings({"rawtypes", "UnnecessaryUnboxing"})
    public static final class Instance extends Re_PrimitiveClassInstance {
        protected AtomicInteger len = new AtomicInteger();

        @Override
        Instance superClone() {
            Instance clone = (Instance) super.superClone();
            clone.len = new AtomicInteger(len.intValue());
            return clone;
        }

        protected Instance(Re_Class reClass, Re_IRe_VariableMap map) {
            super(reClass, map);
        }








        public Object getElement(Re_Executor executor, int index) {
            return Re_Variable.accessFindTableValue(executor, index, this);
        }

        public Object[] toArray(Re_Executor executor) {
            return toArray(executor, Finals.EMPTY_OBJECT_ARRAY);
        }
        public <T> T[] toArray(Re_Executor executor, T[] a) {
            if (null == a) {
                return null;
            }
            int size = Re_Variable.size(this);

            T[] objects = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            for (int key = 0; key < size; key++) {
                objects[key] = (T) Re_Variable.accessFindTableValue(executor, key, this);
                if (executor.isReturnOrThrow()) return null;
            }
            return objects;
        }







        public void setElements(@NotNull final Re_Executor current_executor, @NotNull Re_CodeLoader.Call callParamController) {
            if (len.intValue() > 0) {
                current_executor.setThrow("try to set all elements, but this is not a new list");
            } else {
                int paramExpressionCount = callParamController.getParamExpressionCount();
                for (int i = 0; i < paramExpressionCount; i++) {
                    Object value = current_executor.getExpressionValue(callParamController, i);
                    if (current_executor.isReturnOrThrow()) return;

                    Integer key = i;
                    /*safe*/Re_Variable.Unsafes.putListVariable(key, value, this);
                }
                len.set(paramExpressionCount);
            }
        }
        public void setElements(@NotNull final Re_Executor current_executor, Object[] array) {
            if (len.intValue() > 0) {
                current_executor.setThrow("try to set all elements, but this is not a new list");
            } else {
                for (int i = 0; i < array.length; i++) {
                    Object value = array[i];

                    Integer key = i;
                    /*safe*/Re_Variable.Unsafes.putListVariable(key, value, this);
                }
                len.set(array.length);
            }
        }




        public void addElement(Object value) {
            int indexAndAddSize = this.getIndexAndAddSize();
            if (indexAndAddSize >= 0) {
                this._variable_put(indexAndAddSize, Re_Variable.createVariable(value));
            }
        }



        public final int size() {
            return len.intValue();
        }
        public final void setSize(int size) {
            len.set(size);
        }


        /**
         * 更新一个更大的值
         * 永远不会达到负数
         */
        @SuppressWarnings("ManualMinMaxCalculation")
        protected final void updateLargerSize(int observed) {
            int oldValue, newValue;
            do {
                oldValue = len.get();
                newValue = (oldValue >= observed) ? oldValue : observed;
            } while (!len.compareAndSet(oldValue, newValue));
        }

        /**
         * 这里最后指针可能会 = Integer.MAX_VALUE， 但是这个没什么意义，不需要理
         */
        protected final int getIndexAndAddSize() {
            for (;;) {
                int current = len.get();
                boolean valid = current + 1 >= 0;
                if (len.compareAndSet(current, valid ? current + 1 : Integer.MAX_VALUE)) //防止大小超过Integer.MAX_VALUE
                    return valid ? current : -1;
            }
        }






        //-------------------------------------------------------------------------------
        @Override
        public Re_Variable _variable_remove(Object key) {
            // TODO: Implement this method
            if (key instanceof Number) {
                key = key instanceof Integer ? (Integer) key : Integer.valueOf(((Number) key).intValue());
            }
            return reClassInstanceVariableMap._variable_remove(key);
        }
        @Override
        public Re_Variable _variable_find_table_or_parent(Object key) {
            // TODO: Implement this method
            if (key instanceof Number) {
                key = key instanceof Integer ? (Integer) key : Integer.valueOf(((Number) key).intValue());
            }
            return reClassInstanceVariableMap._variable_find_table_or_parent(key);
        }
        @Override
        public Re_Variable _variable_get(Object key) {
            // TODO: Implement this method
            if (key instanceof Number) {
                key = key instanceof Integer ? (Integer) key : Integer.valueOf(((Number) key).intValue());
            }
            return reClassInstanceVariableMap._variable_get(key);
        }
        @Override
        public Re_Variable _variable_put(Object key, Re_Variable p2) {
            // TODO: Implement this method
            if (key instanceof Number) {
                Integer index = key instanceof Integer ? (Integer) key : Integer.valueOf(((Number) key).intValue());
                this.updateLargerSize(index.intValue() + 1);
                return reClassInstanceVariableMap._variable_put(index, p2);
            }
            return reClassInstanceVariableMap._variable_put(key, p2);
        }
        @Override
        public boolean _variable_has(Object key) {
            // TODO: Implement this method
            if (key instanceof Number) {
                int index = key instanceof Integer ? ((Integer) key).intValue() : ((Number) key).intValue();
                return index >= 0 && index < len.get();
            }
            return reClassInstanceVariableMap._variable_has(key);
        }
        //-------------------------------------------------------------------------------
        @Override
        public int _variable_key_count() {
            // TODO: Implement this method
            return len.get();
        }
        @Override
        public Iterable<?> _variable_keys() {
            // TODO: Implement this method
            return Lists.wrapRange(0, len.get(),1);
        }
        //-------------------------------------------------------------------------------

        public Iterable<?> innerGetRealKeySet() {
            return reClassInstanceVariableMap._variable_keys();
        }
    }
}

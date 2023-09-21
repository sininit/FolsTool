package top.fols.box.reflect.re;


import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"UnnecessaryModifier", "UnnecessaryInterfaceModifier"})
public interface Re_IRe_Iterable {
    /**
     * 每次执行都可以获取一个元素遍历器
     */
    public abstract ReIterator iterator();

    @SuppressWarnings("rawtypes")
    abstract class ReIterator {
        public abstract Re_Variable keyVariable();
        public abstract Re_Variable valueVariable();

        public abstract boolean hasNext();


        //每次 next都会 设置key和value
        public abstract void next(Re_Executor executor) throws Throwable;
    }


    /**
     * 对象有 {@link Re_IRe_Iterable} 和 {@link Iterable}
     */
    @SuppressWarnings({"FieldMayBeFinal", "rawtypes"})
    public class Utilities {
        Utilities() { }


        static Re_IRe_Iterable wrapIReObject(final Re_Executor executor, final Re_IRe_Object map) throws Throwable {
            if (map.hasObjectKeys()) {
                final Iterable keysProcess = map.getObjectKeys(executor);
                return new Re_IRe_Iterable() {
                    @Override
                    public ReIterator iterator() {
                        // TODO: Implement this method
                        return new ReIterator() {

                            final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable key   = Re_Variable.createReusingTempObjectVariable(null);
                            final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable value = Re_Variable.createReusingTempObjectVariable(null);

                            Iterable keySet = keysProcess;
                            final Iterator iterator = keySet.iterator();


                            @Override
                            public Re_Variable keyVariable() {
                                // TODO: Implement this method
                                return key;
                            }

                            @Override
                            public Re_Variable valueVariable() {
                                // TODO: Implement this method
                                return value;
                            }

                            @Override
                            public boolean hasNext() {
                                // TODO: Implement this method
                                return iterator.hasNext();
                            }

                            @Override
                            public void next(Re_Executor executor) throws Throwable {
                                // TODO: Implement this method
                                Object k        = iterator.next();
                                key.set(k);
                                value.set(map.getObjectValue(executor, k));
                            }
                        };
                    }
                };
            } else {
                executor.setThrow(Re_Accidents.unsupported_type(Re_Utilities.objectAsName(map)));
                return null;
            }
        }

        public static Re_IRe_Iterable wrapJavaIterable(final Re_Executor executor, final Iterable keysProcess) {
            return new Re_IRe_Iterable() {
                @Override
                public ReIterator iterator() {
                    // TODO: Implement this method
                    return new ReIterator() {

                        final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable key   = Re_Variable.createReusingTempObjectVariable(null);
                        final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable value = Re_Variable.createReusingTempObjectVariable(null);

                        final Iterator iterator = keysProcess.iterator();

                        int i=0;

                        @Override
                        public Re_Variable keyVariable() {
                            // TODO: Implement this method
                            return key;
                        }

                        @Override
                        public Re_Variable valueVariable() {
                            // TODO: Implement this method
                            return value;
                        }

                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            return iterator.hasNext();
                        }

                        @Override
                        public void next(Re_Executor executor) throws Throwable {
                            // TODO: Implement this method
                            Integer index = i++;
                            key  .set(index);

                            Object next = iterator.next();
                            value.set(next);
                        }
                    };
                }
            };
        }

        public static Re_IRe_Iterable wrapJavaMap(final Re_Executor executor, final Map map) throws Throwable {
            final Set keysProcess = map.keySet();
            return new Re_IRe_Iterable() {
                @Override
                public ReIterator iterator() {
                    // TODO: Implement this method
                    return new ReIterator() {

                        final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable key   = Re_Variable.createReusingTempObjectVariable(null);
                        final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable value = Re_Variable.createReusingTempObjectVariable(null);

                        Set keySet = keysProcess;
                        final Iterator iterator = keySet.iterator();


                        @Override
                        public Re_Variable keyVariable() {
                            // TODO: Implement this method
                            return key;
                        }

                        @Override
                        public Re_Variable valueVariable() {
                            // TODO: Implement this method
                            return value;
                        }

                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            return iterator.hasNext();
                        }

                        @Override
                        public void next(Re_Executor executor) throws Throwable {
                            // TODO: Implement this method
                            Object k        = iterator.next();
                            key.set(k);
                            value.set(map.get(k));
                        }
                    };
                }
            };
        }

        public static Re_IRe_Iterable wrapJavaArray(final Re_Executor executor, final Object map) throws Throwable {
            return new Re_IRe_Iterable() {
                @Override
                public ReIterator iterator() {
                    // TODO: Implement this method
                    return new ReIterator() {
                        final Re_Variable.Re_Variable_Builtin_ReusingTempIntegerVariable key   = Re_Variable.createReusingTempIntegerVariable(-1);
                        final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable value = Re_Variable.createReusingTempObjectVariable(null);

                        int len = Array.getLength(map);

                        @Override
                        public Re_Variable keyVariable() {
                            // TODO: Implement this method
                            return key;
                        }

                        @Override
                        public Re_Variable valueVariable() {
                            // TODO: Implement this method
                            return value;
                        }

                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            return key.cache + 1 < len;
                        }

                        @Override
                        public void next(Re_Executor executor) throws Throwable {
                            // TODO: Implement this method
                            int i = key.cache + 1;
                            key.cache = i;
                            value.set(Array.get(map, i));
                        }
                    };
                }
            };
        }

        /**
         * 类似于python的 range
         * 假设 start是0 stop是10 只会遍历到 0和 10之前的数字
         */
        public static Re_IRe_Iterable wrapRange(final Re_Executor executor, final int start, final int stop) throws Throwable {
            if (stop > start) {
                return wrapRange(executor, start, stop, 1);
            } else if (stop == start) {
                return wrapRange(executor, start, stop, 0);
            } else {
                return wrapRange(executor, start, stop, -1);
            }
        }

        public static Re_IRe_Iterable wrapRange(final Re_Executor executor, final int start, final int stop, final int setp) throws Throwable {
            if (stop > start) {
                return new Re_IRe_Iterable() {
                    @Override
                    public ReIterator iterator() {
                        // TODO: Implement this method
                        return new ReIterator() {

                            boolean ed = false;
                            final Re_Variable.Re_Variable_Builtin_ReusingTempIntegerVariable key  = Re_Variable.createReusingTempIntegerVariable(start);
                            final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable value = Re_Variable.createReusingTempObjectVariable(null);

                            @Override
                            public Re_Variable keyVariable() {
                                // TODO: Implement this method
                                return key;
                            }

                            @Override
                            public Re_Variable valueVariable() {
                                // TODO: Implement this method
                                return value;
                            }

                            @Override
                            public boolean hasNext() {
                                // TODO: Implement this method
                                if (ed) {
                                    return key.cache + setp < stop;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public void next(Re_Executor executor) throws Throwable {
                                // TODO: Implement this method
                                if (ed) {
                                    key.cache += setp;
                                    value.set(null);
                                } else {
                                    key.cache = start;
                                    value.set(null);
                                    ed = true;
                                }
                            }
                        };
                    }
                };
            } else {
                return new Re_IRe_Iterable() {
                    @Override
                    public ReIterator iterator() {
                        // TODO: Implement this method
                        return new ReIterator() {

                            boolean ed = false;
                            final Re_Variable.Re_Variable_Builtin_ReusingTempIntegerVariable key  = Re_Variable.createReusingTempIntegerVariable(start);
                            final Re_Variable.Re_Variable_Builtin_ReusingTempObjectVariable value = Re_Variable.createReusingTempObjectVariable(null);

                            @Override
                            public Re_Variable keyVariable() {
                                // TODO: Implement this method
                                return key;
                            }

                            @Override
                            public Re_Variable valueVariable() {
                                // TODO: Implement this method
                                return value;
                            }

                            @Override
                            public boolean hasNext() {
                                // TODO: Implement this method
                                if (ed) {
                                    return key.cache + setp > stop;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public void next(Re_Executor executor) throws Throwable {
                                // TODO: Implement this method
                                if (ed) {
                                    key.cache += setp;
                                    value.set(null);
                                } else {
                                    key.cache = start;
                                    value.set(null);
                                    ed = true;
                                }
                            }
                        };
                    }
                };
            }
        }
    }
}

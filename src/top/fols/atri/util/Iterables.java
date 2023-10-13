package top.fols.atri.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import top.fols.atri.interfaces.interfaces.IFilter;

public class Iterables {

	
	
	
	
	public static <T> T[] toArray(Iterable<T> collection, T[] buffer) {
        if (collection instanceof List) {
            return ((List<T>) collection).toArray(buffer);
        } else {
            return toArray(collection.iterator(), buffer);
        }
    }
    public static <T> T[] toArray(Iterable<T> collection, int offset, int len,
                                  T[] buffer) {
        if (collection instanceof List) {
            return toArray(((List<T>) collection), offset, len, buffer);
        } else {
            return toArray(collection.iterator(),
						   offset, len, buffer);
        }
    }


    public static <T> T[] toArray(Iterator<T> collection, T[] buffer) {
        List<T> result = new ArrayList<>();
        while (collection.hasNext()) {
            result.add(collection.next());
        }
        return result.toArray(buffer);
    }
    public static <T> T[] toArray(Iterator<T> iterator , int offset, int len,
                                  T[] buffer) {
        if (buffer.length < len)
            buffer = (T[]) Array.newInstance(buffer.getClass().getComponentType(), len);
        for (int i = 0; i < offset; i++)
            iterator.next();
        for (int i = 0; i < len; i++)
            buffer[i] = iterator.next();
        return buffer;
    }



    public static <T> Iterable<T> filterIterable(final Iterable<T> iterable, final IFilter<T> filter) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                // TODO: Implement this method
                final Iterator<T> iterator = iterable.iterator();
                return new Iterator<T>() {
                    private T v;
                    private boolean geted = true;

                    @Override
                    public boolean hasNext() {
                        // TODO: Implement this method
                        if (geted) {
                            while (iterator.hasNext())	{
                                T next = iterator.next();
                                if (filter.next(next)) {
                                    this.v = next;
                                    return true;
                                }
                            }
                            return false;
                        } else {
                            return true;
                        }
                    }
                    @Override
                    public T next() {
                        // TODO: Implement this method
                        geted = true;
                        return v;
                    }

                    @Override
                    public void remove() {
                        // TODO: Implement this method
                        iterator.remove();
                    }
                };
            }
        };
    }







    public static <T> Iterable<T> wrapArray(final T[] array, final int astart, final int alen) {
        if (null == array || astart < 0 || alen < 0 || astart > array.length) {
            return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return Collections.emptyIterator();
                }
            };
        }
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    int cursor = astart;
                    final int range = astart + alen;

                    @Override
                    public boolean hasNext() {
                        return cursor != range;
                    }

                    @Override
                    public T next() {
                        return array[cursor++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * 类似于python的 range
     * 假设 start是0 stop是10 只会遍历到 0和 10之前的数字
     */
    public static Iterable<Integer> wrapRange(final int start, final int stop) {
        if (stop > start) {
            return wrapRange(start, stop, 1);
        } else if (stop == start) {
            return wrapRange(start, stop, 0);
        } else {
            return wrapRange(start, stop, -1);
        }
    }

    public static Iterable<Integer> wrapRange(final int start, final int stop, final int setp) {
        if (stop > start) {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    // TODO: Implement this method
                    return new Iterator<Integer>() {

                        boolean ed = false;
                        int key = start;

                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            if (ed) {
                                return key + setp < stop;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public Integer next() {
                            // TODO: Implement this method
                            if (ed) {
                                key += setp;
                            } else {
                                key = start;
                                ed = true;
                            }
                            return key;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }
                    };
                }

                @Override
                public String toString() {
                    Iterator<Integer> iterator = this.iterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    while (iterator.hasNext()) {
                        sb.append(iterator.next());
                        if (iterator.hasNext()) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    return sb.toString();
                }
            };
        } else if (start == stop) {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    // TODO: Implement this method
                    return new Iterator<Integer>() {
                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            return false;
                        }

                        @Override
                        public Integer next() {
                            // TODO: Implement this method
                            return null;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }

                    };
                }

                @Override
                public String toString() {
                    Iterator<Integer> iterator = this.iterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    while (iterator.hasNext()) {
                        sb.append(iterator.next());
                        if (iterator.hasNext()) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    return sb.toString();
                }
            };
        } else {
            return new Iterable<Integer>() {
                @Override
                public Iterator<Integer> iterator() {
                    // TODO: Implement this method
                    return new Iterator<Integer>() {

                        boolean ed = false;
                        int key = start;

                        @Override
                        public boolean hasNext() {
                            // TODO: Implement this method
                            if (ed) {
                                return key + setp > stop;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public Integer next() {
                            // TODO: Implement this method
                            if (ed) {
                                key += setp;
                            } else {
                                key = start;
                                ed = true;
                            }
                            return key;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }

                    };
                }

                @Override
                public String toString() {
                    Iterator<Integer> iterator = this.iterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    while (iterator.hasNext()) {
                        sb.append(iterator.next());
                        if (iterator.hasNext()) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    return sb.toString();
                }
            };
        }
    }


	
	

}

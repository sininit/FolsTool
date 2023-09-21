package top.fols.box.util;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;

@SuppressWarnings({"rawtypes", "unchecked", "EqualsWhichDoesntCheckParameterClass"})
public abstract class BlurryKey<T> implements Cloneable {





    public abstract T       getOriginKey();
    public abstract Object  getFormatKey();

    public abstract Object  formatOriginKey(T okay);


    public abstract BlurryKey<T> clone();
    public abstract BlurryKey<T> newKey(T okey);







    @Override
    public int hashCode() {
        // TODO: Implement this method
        return BlurryKey.hashCode(this,		this);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO: Implement this method
        return BlurryKey.equals(this,		this, obj);
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        Object oval = this.getOriginKey();
        return null == oval ?null: oval.toString();
    }


    public static int hashCode(BlurryKey factory, Object value) {
        return null == (value = BlurryKey.formatKey(factory, value)) ? 0: value.hashCode();
    }

    public static boolean equals(BlurryKey factory, Object value1 , Object value2) {
        return Objects.equals(BlurryKey.formatKey(factory, value1), BlurryKey.formatKey(factory, value2));
    }

    public static Object formatKey(BlurryKey factory, Object value) {
        if (value instanceof BlurryKey) {
            if (value.getClass() == factory.getClass()) {
                return ((BlurryKey)value).getFormatKey();
            } else {
                return factory.formatOriginKey(((BlurryKey)value).getOriginKey()); //套娃？
            }
        }
        return factory.formatOriginKey(value);
    }












    @SuppressWarnings("UnnecessaryLocalVariable")
    public static class IgnoreCaseKey<T> extends BlurryKey<T> implements Serializable {
        private static final long serialVersionUID = 1L;



        private static final IgnoreCaseKey DEFAULT = new IgnoreCaseKey(null);
        public  static IgnoreCaseKey getDefaultFactory() {
            return DEFAULT;
        }

        T originKey;
        transient Object getFormatKeyCache0;
        IgnoreCaseKey(IgnoreCaseKey<T> key) throws UnsupportedOperationException {
            if (null != key) {
                this.originKey = key.originKey;
                this.getFormatKeyCache0 = key.getFormatKeyCache0;
            }
        }



        /**
         * @exception UnsupportedOperationException cannot wrap Key
         */
        IgnoreCaseKey(T origin) throws UnsupportedOperationException {
            //if T is Object...
            if (origin instanceof IgnoreCaseKey) {
                throw new UnsupportedOperationException("wrap key: " + origin.getClass().getName());
            } else {
                this.originKey = origin;
            }
        }



        /** Custom action start **/
        @Override
        public T getOriginKey() {
            // TODO: Implement this method
            return this.originKey;
        }

        @Override
        public Object formatOriginKey(T object) {
            if (object instanceof CharSequence) {
                return object.toString().toLowerCase();
            }
            return object;
        }

        @Override
        public 	Object getFormatKey() {
            T ok = this.getOriginKey();
            if (null != ok) {
                if (null == this.getFormatKeyCache0) {
                    return  this.getFormatKeyCache0 = this.formatOriginKey(ok);
                } else {
                    return this.getFormatKeyCache0;
                }
            }
            return ok;
        }

        @Override
        public IgnoreCaseKey<T> newKey(T okey) {
            // TODO: Implement this method
            return new IgnoreCaseKey<T>(okey);
        }

        @Override
        public IgnoreCaseKey<T> clone() {
            // TODO: Implement this method
            IgnoreCaseKey<T> newKey = new IgnoreCaseKey<T>(this);
            return newKey;
        }


        /** Custom action end **/

    }


}


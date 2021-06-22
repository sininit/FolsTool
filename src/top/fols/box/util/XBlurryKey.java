package top.fols.box.util;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import top.fols.atri.lang.Objects;
import top.fols.box.statics.XStaticFixedValue;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class XBlurryKey<T extends Object> implements Cloneable {





    public abstract T getOriginKey();
    public abstract Object formatOriginKey(T okey);
    public abstract Object getFormatKey();



    public abstract XBlurryKey<T> clone();
    public XBlurryKey<T> newKey(T okey) {
        try {
            Constructor con = this.getClass().getDeclaredConstructor(XStaticFixedValue.Object_class);
            return (XBlurryKey<T>) con.newInstance(okey);
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        }
    }







    @Override
    public int hashCode() {
        // TODO: Implement this method
        return XBlurryKey.hashCode(this,		this);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO: Implement this method
        return XBlurryKey.equals(this,		this, obj);
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        Object oval = this.getOriginKey();
        return null == oval ?null: oval.toString();
    }


//		public final KeyFormatFactory getFactory() { return this; }


    public static int hashCode(XBlurryKey factory, Object value) {
        return null == (value = XBlurryKey.formatKey(factory, value)) ? 0: value.hashCode();
    }

    public static boolean equals(XBlurryKey factory, Object value1 , Object value2) {
        return Objects.equals(XBlurryKey.formatKey(factory, value1), XBlurryKey.formatKey(factory, value2));
    }

    public static Object formatKey(XBlurryKey factory, Object value) {
        if (value instanceof XBlurryKey) {
            if (value.getClass() == factory.getClass()) {
                return ((XBlurryKey)value).getFormatKey();
            } else {
                return factory.formatOriginKey(((XBlurryKey)value).getOriginKey()); //套娃？
            }
        }
        return factory.formatOriginKey(value);
    }












//    public static final IgnoreCaseKey IGNORE_CASE_KEY_FACTORY = IgnoreCaseKey.getDefaultFactory();
    public static class IgnoreCaseKey<T> extends XBlurryKey<T> implements Serializable {
        private static final long serialVersionUID = 1L;



        private static final IgnoreCaseKey DEFAULT = new IgnoreCaseKey(null);
        public  static final IgnoreCaseKey getDefaultFactory() {
            return DEFAULT;
        }










        T originkey;
        transient Object getFormatKeyCache0;
        IgnoreCaseKey(IgnoreCaseKey<T> key) throws UnsupportedOperationException {
            if (null != key) {
                this.originkey = key.originkey;
                this.getFormatKeyCache0 = key.getFormatKeyCache0;
            }
        }









        /**
         * @exception UnsupportedOperationException cannot wrap Key
         */
        IgnoreCaseKey(T originkey) throws UnsupportedOperationException {
            //if T is Object...
            if (originkey instanceof IgnoreCaseKey) {
                throw new UnsupportedOperationException("wrap key: " + originkey.getClass().getName());
            } else {
                this.originkey = originkey;
            }
        }



        /** Custom action start **/
        @Override
        public T getOriginKey() {
            // TODO: Implement this method
            return this.originkey;
        }

        @Override
        public 	Object getFormatKey() {
            T ok = this.getOriginKey();
            if (null != ok) {
                if (null == this.getFormatKeyCache0) {
                    return this.getFormatKeyCache0 = this.formatOriginKey(ok);
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

        @Override
        public Object formatOriginKey(T object) {
            if (object instanceof CharSequence) {
                return object.toString().toLowerCase();
            }
            return object;
        }


        /** Custom action end **/











        @Override
        public int hashCode() {
            // TODO: Implement this method
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            // TODO: Implement this method
            return super.equals(obj);
        }

        @Override
        public String toString() {
            // TODO: Implement this method
            return super.toString();
        }

    }


}


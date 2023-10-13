package top.fols.box.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.fols.atri.assist.util.IgnoreCaseLinkedHashMap;
import top.fols.atri.assist.util.StringJoiner;
import top.fols.box.reflect.ClassProperties;

public class ObjectTable {
    protected ClassProperties classProperties = new ClassProperties();


    private final boolean isIgnorePropertyNameCase;
    public ObjectTable() {
        this.isIgnorePropertyNameCase = true;
    }
    public ObjectTable(boolean isIgnorePropertyNameCase) {
        this.isIgnorePropertyNameCase = isIgnorePropertyNameCase;
    }
    public boolean isIgnorePropertyNameCase() {
        return isIgnorePropertyNameCase;
    }



    protected Map<String, Object> newObjectProertyMap(Map<String, Object> old) {
        if (isIgnorePropertyNameCase()) {
            if (null == old || old.isEmpty()) {
                return new IgnoreCaseLinkedHashMap<String, Object>();
            }
            return new IgnoreCaseLinkedHashMap<String, Object>(old);
        } else {
            if (null == old || old.isEmpty()) {
                return new LinkedHashMap<String, Object>();
            }
            return new LinkedHashMap<String, Object>(old);
        }
    }








    public void setKey(String... names) {
        if (null != this.key)
            throw new UnsupportedOperationException("inited");
        this.key = new Key(this, names);
    }
    public Key getKey() {
        return this.key;
    }

    public interface IObjectTable {
        public ObjectElement select(Key key);
        public void 		 insert(ObjectElement v);
        public void 		 delete(Key key);
        public Iterator<Key> keyIterator();

    }
    public static class JavaHashObjectTable extends HashMap<Key, ObjectElement> implements IObjectTable {
        @Override
        public ObjectElement select(Key key) {
            // TODO: Implement this method
            return super.get(key);
        }
        @Override
        public void insert(ObjectElement v) {
            // TODO: Implement this method
            super.put(v.keyImpl, v);
        }
        @Override
        public void delete(Key key) {
            // TODO: Implement this method
            super.remove(key);
        }
        @Override
        public Iterator<Key> keyIterator() {
            return super.keySet().iterator();
        }


        @Override
        public String toString() {
            // TODO: Implement this method
            return super.toString();
        }
    }



    public ObjectElement parseObjectElement(Key newKey, Map<String, Object> varMap) {
        return newObjectElement(newKey, varMap);
    }
    public ObjectElement parseObjectElement(Object entity) {
        Map<String, Object> varMap = newObjectProertyMap(null);
        ClassProperties.ClassProperty cp = classProperties.getClassProperty(entity.getClass());
        for (String n: cp.keySet()) {
            varMap.put(n, cp.propertyGet(n).get(entity));
        }
        Key newKey = newObjectKey(varMap);
        return newObjectElement(newKey, varMap);
    }



    public ObjectElement select(Key key) {
        return table.select(key);
    }
    public ObjectElement selectFromEntity(Object entity) {
        Key key = newObjectKeyFromEntity(entity);
        return select(key);
    }

    protected ObjectElement insert(ObjectElement obj) {
        if (null == obj)
            return  obj;
        Key key = obj.keyImpl;
        if (select(key) != null)
            throw new UnsupportedOperationException("already add object key: " + key);
        table.insert(obj);
        return obj;
    }
    public ObjectElement insert(Map<String, Object> value) {
        Map<String, Object> varMap = newObjectProertyMap(value);
        Key newKey = newObjectKey(varMap);
        return insert(parseObjectElement(newKey, varMap));
    }
    public ObjectElement insertEntity(Object entity) {
        return insert(parseObjectElement(entity));
    }

    public ObjectElement delete(ObjectElement obj) {
        if (null == obj)
            return  obj;
        Key key = obj.keyImpl;
        table.delete(key);
        return obj;
    }

    public Iterator<Key> keyIterator() { return table.keyIterator(); }



    public static class Key {
        protected final ObjectTable this$0;
        protected final String[]    keyNames;
        protected final Set<String> keyNameSet;
        protected final Set<String> keyNameSetFinal;

        protected final Object[] values;
        protected int   valueHashCode;

        public Key(ObjectTable this$0, String[] names) {
            if (names == null || names.length == 0) {
                throw new UnsupportedOperationException("names is null");
            }
            this.this$0 = this$0;
            Set<String> set = new LinkedHashSet<>(names.length);
            for (String n: names) {
                if (null != n)
                    set.add(n.intern()); // ***** intern
            }
            this.keyNames   = set.toArray(new String[]{});
            this.keyNameSet = set;
            this.keyNameSetFinal = Collections.unmodifiableSet(set);

            this.values = new Object[names.length];
        }
        @SuppressWarnings("CopyConstructorMissesField")
        public Key(Key  k) {
            if (null == k) {
                throw new UnsupportedOperationException("key is null");
            }
            this.this$0          = k.this$0;
            this.keyNames        = k.keyNames;
            this.keyNameSet      = k.keyNameSet;
            this.keyNameSetFinal = k.keyNameSetFinal;

            this.values = new Object[k.keyNames.length];
        }



        public String[] cloneNames() {
            // TODO: Implement this method
            return this.keyNames.clone();
        }

        public ValueComparer getKeyComparer() {
            return this$0.keyComparer;
        }


        protected void setKeyValue(Object[] map) {
            int hash = 0;
            ValueComparer keyCompare = this$0.keyComparer;
            for (int i = 0; i < Math.min(keyNames.length, map.length);i++) {
                Object v = map[i];
                this.values[i] = v;
                hash += keyCompare.hashCode(v);
            }
            this.valueHashCode = hash;
        }
        protected void setKeyValue(Map<String, Object> varMap) {
            int hash = 0;
            ValueComparer keyCompare = this$0.keyComparer;
            for (int i = 0; i < keyNames.length;i++) {
                Object v = varMap.get(keyNames[i]);
                this.values[i] = v;
                hash += keyCompare.hashCode(v);
            }
            this.valueHashCode = hash;
        }
        protected void setKeyValueFromEntity(Object entity) {
            ValueComparer keyCompare = this$0.keyComparer;
            ClassProperties.ClassProperty cp = this$0.classProperties.getClassProperty(entity.getClass());
            int hash = 0;
            for (int i = 0; i < keyNames.length;i++) {
                String n = keyNames[i];
                ClassProperties.Property p = cp.propertyGet(n);
                if (null == p) {
                    throw new UnsupportedOperationException("cannot found property: " + entity.getClass().getName() + "." + n);
                }
                Object v = p.get(entity);
                this.values[i] = v;
                hash += keyCompare.hashCode(v);
            }
            this.valueHashCode = hash;
        }


        @Override
        public int hashCode() {
            // TODO: Implement this method
            return valueHashCode;
        }

        @Override
        public boolean equals(Object obj) {
            // TODO: Implement this method
            if (obj == this) return true;
            if (obj instanceof Key) {
                ValueComparer keyCompare = this$0.keyComparer;
                Key tk = (Key) obj;
                if (this$0 == tk.this$0) {
                    for (int i = 0; i < keyNames.length;i++)
                        if (!keyCompare.isEquals(this.values[i], tk.values[i]))
                            return false;
                    return true;
                } else {
                    if (keyNames.length == tk.keyNames.length) {
                        for (int i = 0; i < keyNames.length;i++) {
                            if (!(this.keyNames[i] == tk.keyNames[i])) // ***** intern
                                return false;
                            if (!keyCompare.isEquals(this.values[i], tk.values[i]))
                                return false;
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            // TODO: Implement this method
            final String SEPARATOR = ", ";
            final ValueComparer keyCompare = this$0.keyComparer;

            StringBuilder buffer = new StringBuilder();
            buffer.append("[");
            int slen = buffer.length();
            for (Object v: values)
                buffer.append(keyCompare.toString(v)).append(SEPARATOR);
            if (buffer.length() > slen)
                buffer.setLength(buffer.length() - SEPARATOR.length());
            buffer.append("]");
            return buffer.toString();
        }

        public boolean hasName(String n) { return keyNameSetFinal.contains(n);}
        public int    getNameCount()  { return keyNames.length; }
        public String getName(int i)  { return keyNames[i];}

        public Object getValue(int i) { return values[i];}
    }

    public IObjectTable getTable(){ return table; }

    /******************/
    protected IObjectTable table = new JavaHashObjectTable();

    protected ValueComparer keyComparer = new ValueComparer();
    protected ValueComparer objComparer = new ValueComparer(); //nokey

    protected Key key;

    protected ObjectElement newObjectElement(Key newKey, Map<String, Object> varMap) {
        return new ObjectElement(this, newKey, varMap);
    }
    protected Key newObjectKeyAsEmpty() {
        return new Key(getKey());
    }
    public Key newObjectKey(Map<String, Object> varMap) {
        Key nk = newObjectKeyAsEmpty();
        nk.setKeyValue(varMap);
        return nk;
    }
    public Key newObjectKey(Object[] map) {
        Key nk = newObjectKeyAsEmpty();
        nk.setKeyValue(map);
        return nk;
    }
    public Key newObjectKeyFromEntity(Object entity) {
        Key nk = newObjectKeyAsEmpty();
        nk.setKeyValueFromEntity(entity);
        return nk;
    }
    /**
     * Do not customize key comparison
     */
    protected boolean isObjectElementContentEquals(ObjectElement a, ObjectElement b) {
        Key aKeyImpl = a.keyImpl;
        if (aKeyImpl.equals(b.keyImpl)) {//keys eq
            Map<String, Object> apropertyMap = a.property;
            Map<String, Object> bpropertyMap = b.property;
            Set<String> aSet = apropertyMap.keySet();
            Set<String> bSet = bpropertyMap.keySet();
            Set<String> cSet = aSet.size() >= bSet.size() ? aSet: bSet;
            for (String n: cSet) {
                if (aKeyImpl.hasName(n)) //Do not compare keys
                    continue;
                if (!objComparer.isEquals(apropertyMap.get(n),
                        bpropertyMap.get(n))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    /******************/



    public static class ObjectElement {
        final ObjectTable this$0;
        protected final Map<String, Object> property;
        protected final Key keyImpl;
        private transient Iterable<String> nameSet;

        public final Key getKey() { return keyImpl; }

        protected ObjectElement(ObjectTable this$0, Key key, Map<String, Object> property) {
            this.this$0 = this$0;
            this.property = property;
            this.keyImpl = key;
        }

        public int getPropertyCount() {
            return property.size();
        }

        public void set(String name, Object value) {
            if (keyImpl.keyNameSetFinal.contains(name)) {
                throw new UnsupportedOperationException("cannot set key property: " + name);
            }
            property.put(name, value);
        }
        public Object get(String name) {
            return property.get(name);
        }


        public Iterable<String> names() {
            if (nameSet == null)
                nameSet = new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new Iterator<String>(){
                            final Iterator<String> s = property.keySet().iterator();

                            @Override public boolean hasNext() { return s.hasNext(); }
                            @Override public String  next()    { return s.next(); }
                            @Override public void remove() { throw new UnsupportedOperationException();}
                        };
                    }
                };
            return nameSet;
        }

        @Override
        public int hashCode() {
            // TODO: Implement this method
            return keyImpl.hashCode();
        }
        @Override
        public boolean equals(Object obj) {
            // TODO: Implement this method
            if (obj == this) return true;
            if (obj instanceof ObjectElement) {
                return this$0.isObjectElementContentEquals(this, (ObjectElement) obj);
            }
            return false;
        }


        public Map<String, Object> toLinkedHashMap() {
            return new LinkedHashMap<String, Object>(property);
        }

        @Override
        public String toString() {
            // TODO: Implement this method
            return toLinkedHashMap().toString();
        }
    }





    public static interface Append<K, V> {
        public void append(K v, V oldValue, V newValue);
        public Iterator<K> keyIterator();
    }

    public static class ListKeyAppend implements Append<Key, ObjectElement> {
        final List<Key> buffer = new ArrayList<>();

        @Override
        public void append(Key v,
                           ObjectElement oldObj, ObjectElement newObj) {
            // TODO: Implement this method
            buffer.add(v);
        }
        @Override
        public Iterator<Key> keyIterator() {
            // TODO: Implement this method
            return buffer.iterator();
        }
    }

    public static class DiffAnalysis {
        protected Append<Key, ObjectElement> reserve;
        protected Append<Key, ObjectElement> added;
        protected Append<Key, ObjectElement> removed;
        protected Append<Key, ObjectElement> changed;

        protected boolean loaded;

        public static final int TYPE_RESERVE  = 1;
        public static final int TYPE_ADDED    = 2;
        public static final int TYPE_REMOVE   = 3;
        public static final int TYPE_CHANGED  = 4;

        protected Append<Key, ObjectElement> newAppend(int type) {
            return new ListKeyAppend();
        }
        public void reset() {
            this.reserve = newAppend(TYPE_RESERVE);
            this.added   = newAppend(TYPE_ADDED);
            this.removed = newAppend(TYPE_REMOVE);
            this.changed = newAppend(TYPE_CHANGED);
        }

        public DiffAnalysis() {}


        public DiffAnalysis from(IObjectTable oldMap, IObjectTable newMap) {
            //added removed changed
            this.reset();

            Iterator<Key> newKeyIterator = newMap.keyIterator();
            while (newKeyIterator.hasNext()) {
                Key nk = newKeyIterator.next();
                ObjectElement oldObject = oldMap.select(nk);
                ObjectElement newObject = newMap.select(nk);
                if (null == oldObject) {
                    added.append(nk, null, newObject);
                } else {
                    if (oldObject.equals(newObject)) {
                        reserve.append(nk, oldObject, newObject);
                    } else {
                        changed.append(nk, oldObject, newObject);
                    }
                }
            }
            Iterator<Key> oldKeyIterator = oldMap.keyIterator();
            while (oldKeyIterator.hasNext()) {
                Key nk = oldKeyIterator.next();
                ObjectElement oldObject = oldMap.select(nk);
                if (null == newMap.select(nk)) {
                    removed.append(nk, oldObject, null);
                }
            }
            return this;
        }

        public String toString(Append<?,?> a) {
            if (null == a) {
                return "[]";
            } else {
                Iterator<?> keyIterator = a.keyIterator();
                StringJoiner sj = new StringJoiner(", ");
                while (keyIterator.hasNext()) {
                    sj.add(String.valueOf(keyIterator.next()));
                }
                return "[" + sj + "]";
            }
        }

        @Override
        public String toString() {
            // TODO: Implement this method
            return "{"
                    + ("reserve: " + toString(reserve)) + ", "
                    + ("added: "   + toString(added))   + ", "
                    + ("remores: " + toString(removed)) + ", "
                    + ("changed: " + toString(changed))
                    + "}";
        }
    }



    public static class ValueComparer {
        final static String EMPTY_STRING = "";
        final static int    EMPTY_STRING_HASH = EMPTY_STRING.hashCode();
        final static char   ZERO_CHAR   = '0';
        final static String ZERO_STRING = String.valueOf(ZERO_CHAR);
        final static String FALSE_STRING = String.valueOf(false);

        final static String TRUE_AS_NUMBER      = String.valueOf(1);
        final static int    TRUE_AS_NUMBER_HASH = TRUE_AS_NUMBER.hashCode();

        protected boolean isFalseString(String v) {
            return v.equals(EMPTY_STRING) || v.equals(ZERO_STRING) || v.equals(FALSE_STRING);
        }
        protected boolean isFalseChar(Character v) {
            return v == ZERO_CHAR || v == 0;
        }


        public boolean isBasic(Object v) {
            if (v == null ||
                    v instanceof Number ||
                    v instanceof Character ||
                    v instanceof Boolean ||
                    v instanceof String)
                return true;
            return isBasicAsNotBasic(v);
        }
        public boolean isBasicAsNotBasic(Object v) {
            return false;
        }

        public String toString(Object v) {
            if (null == v)
                return EMPTY_STRING;
            if (v instanceof Number) {
                if (v instanceof Float || v instanceof Double)
                    return ((Number)v).doubleValue() == 0D ? EMPTY_STRING : v.toString();
                return ((Number)v).longValue()       == 0L ? EMPTY_STRING : v.toString();
            }
            if (v instanceof Character) {
                if (isFalseChar(((Character) v)))
                    return EMPTY_STRING;
                return v.toString();
            }
            if (v instanceof Boolean) {
                return ((Boolean)v)
                        ? TRUE_AS_NUMBER
                        : EMPTY_STRING;
            }
            if (v instanceof String) {
                return isFalseString((String) v) ? EMPTY_STRING : v.toString();
            }
            return toStringNotBasic(v);
        }
        protected String toStringNotBasic(Object v) {
            return v.toString();
        }


        public int hashCode(Object v) {
            if (v == null)
                return EMPTY_STRING_HASH;
            if (v instanceof Number) {
                if (v instanceof Float || v instanceof Double)
                    return ((Number)v).doubleValue() == 0D ? EMPTY_STRING_HASH : v.toString().hashCode();
                return ((Number)v).longValue()       == 0L ? EMPTY_STRING_HASH : v.toString().hashCode();
            }
            if (v instanceof Character) {
                if (isFalseChar(((Character) v)))
                    return EMPTY_STRING_HASH;
                return v.toString().hashCode();
            }
            if (v instanceof Boolean) {
                return ((Boolean)v)
                        ? TRUE_AS_NUMBER_HASH
                        : EMPTY_STRING_HASH;
            }
            if (v instanceof String) {
                return isFalseString((String) v) ? EMPTY_STRING_HASH : v.hashCode();
            }
            return hashCodeNotBasic(v);
        }
        protected int hashCodeNotBasic(Object v) {
            return v.hashCode();
        }



        @SuppressWarnings({"UnnecessaryUnboxing", "PointlessBooleanExpression"})
        public boolean isEquals(Object v, Object v2) {
            if (null == v) {
                if (null == v2)
                    return true;
                if (v2 instanceof Number) {
                    if (v2 instanceof Float || v2 instanceof Double)
                        return ((Number)v2).doubleValue() == 0D;
                    return ((Number)v2).longValue() == 0L;
                }
                if (v2 instanceof Character) {
                    return isFalseChar(((Character) v2));
                }
                if (v2 instanceof Boolean) {
                    return ((Boolean)v2) == false;
                }
                if (v2 instanceof String) {
                    return isFalseString((String)v2);
                }
                return isEqualsAsVIsNullAndV2NotBasic(v, v2);
            }
            if (v instanceof Number) {
                Number vNumber = (Number) v;
                if (v instanceof Float || v instanceof Double) {
                    //Float
                    if (null == v2)
                        return vNumber.doubleValue() == 0D;
                    if (v2 instanceof Number) {
                        if (v2 instanceof Float || v2 instanceof Double)
                            return ((Number)v2).doubleValue() == vNumber.doubleValue();
                        return ((Number)v2).longValue() == vNumber.longValue();
                    }
                    if (v2 instanceof Character) {
                        String vs  = vNumber.doubleValue() == 0D
                                ? EMPTY_STRING: v.toString();
                        String v2s = isFalseChar((Character) v2)
                                ? EMPTY_STRING: v2.toString();
                        return v2s.equals(vs);
                    }
                    if (v2 instanceof Boolean) {
                        return ((Boolean)v2) == (vNumber.doubleValue() != 0D);
                    }
                    if (v2 instanceof String) {
                        boolean vIsZero = vNumber.doubleValue() == 0;
                        if (isFalseString((String)v2))
                            return vIsZero;
                        if (vIsZero)
                            return false;
                        return v.toString().equals(v2);
                    }
                } else {
                    //Number
                    if (null == v2)
                        return vNumber.longValue() == 0;
                    if (v2 instanceof Number) {
                        if (v2 instanceof Float || v2 instanceof Double)
                            return ((Number)v2).doubleValue() == vNumber.longValue();
                        return ((Number)v2).longValue() == vNumber.longValue();
                    }
                    if (v2 instanceof Character) {
                        String vs  = vNumber.longValue() == 0L
                                ? EMPTY_STRING: v.toString();
                        String v2s = isFalseChar((Character) v2)
                                ? EMPTY_STRING: String.valueOf(((Character)v2).charValue());
                        return v2s.equals(vs);
                    }
                    if (v2 instanceof Boolean) {
                        return ((Boolean)v2) == (vNumber.longValue() != 0);
                    }
                    if (v2 instanceof String) {
                        boolean vIsZero = vNumber.longValue() == 0;
                        if (isFalseString((String) v2))
                            return vIsZero;
                        if (vIsZero)
                            return false;
                        return v.toString().equals(v2);
                    }
                }
                return isEqualsAsVIsNumberAndV2NotBasic(vNumber, v2);
            }
            if (v instanceof Character) {
                Character vChar = (Character) v;
                boolean vIsZero = isFalseChar(vChar);
                if (null == v2)
                    return vIsZero;
                if (v2 instanceof Number) {
                    if (v2 instanceof Float || v2 instanceof Double) {
                        if (vIsZero) {
                            return ((Number) v2).doubleValue() == 0;
                        } else {
                            return String.valueOf(vChar.charValue()).equals(v2.toString());
                        }
                    }
                    if (vIsZero) {
                        return ((Number)v2).longValue() == 0L;
                    } else {
                        return String.valueOf(vChar.charValue()).equals(v2.toString());
                    }
                }
                if (v2 instanceof Character) {
                    if (vIsZero) {
                        return isFalseChar((Character) v2);
                    } else {
                        return String.valueOf(vChar.charValue()).equals(v2.toString());
                    }
                }
                if (v2 instanceof Boolean) {
                    if (vIsZero) {
                        return ((Boolean)v2) == false;
                    } else {
                        return ((Boolean)v2) == true;
                    }
                }
                if (v2 instanceof String) {
                    if (isFalseString((String) v2))
                        return vIsZero;
                    if (vIsZero)
                        return false;
                    return v.toString().equals(v2);
                }
                return isEqualsAsVIsCharAndV2NotBasic(vChar, vIsZero,  v2);
            }
            if (v instanceof Boolean) {
                Boolean vB = (Boolean) v;
                if (null == v2)
                    return vB == false;
                if (v2 instanceof Number) {
                    if (v2 instanceof Float || v2 instanceof Double) {
                        if (((Number) v2).doubleValue() == 0) {
                            return vB == false;
                        } else {
                            return vB == true;
                        }
                    }
                    if (((Number) v2).longValue() == 0) {
                        return vB == false;
                    } else {
                        return vB == true;
                    }
                }
                if (v2 instanceof Character) {
                    if (isFalseChar((Character) v2)) {
                        return vB == false;
                    } else {
                        return vB == true;
                    }
                }
                if (v2 instanceof Boolean) {
                    return vB == (Boolean) v2;
                }
                if (v2 instanceof String) {
                    if (vB) {
                        if (isFalseString((String) v2))
                            return false;
                    } else {
                        if (isFalseString((String) v2))
                            return true;
                    }
                    return vB == !isFalseString((String) v2);
                }
                return isEqualsAsVIsBooleanAndV2NotBasic(vB, v2);
            }
            if (v instanceof String) {
                String vStr = (String) v;
                boolean vIsZero = isFalseString(vStr);
                if (null == v2)
                    return vIsZero;
                if (v2 instanceof Number) {
                    if (v2 instanceof Float || v2 instanceof Double) {
                        if (((Number) v2).doubleValue() == 0) {
                            return vIsZero;
                        } else {
                            return v2.toString().equals(v.toString());
                        }
                    }
                    if (((Number) v2).longValue() == 0) {
                        return vIsZero;
                    } else {
                        return v2.toString().equals(v.toString());
                    }
                }
                if (v2 instanceof Character) {
                    if (isFalseChar((Character) v2)) {
                        return vIsZero;
                    } else {
                        return v2.toString().equals(v.toString());
                    }
                }
                if (v2 instanceof Boolean) {
                    if ((((Boolean) v2).booleanValue())) {
                        return !vIsZero;
                    } else {
                        return vIsZero;
                    }
                }
                if (v2 instanceof String) {
                    boolean v2IsZero = isFalseString((String) v2);
                    if (vIsZero) {
                        return v2IsZero;
                    } else {
                        if (v2IsZero) {
                            return false;
                        }
                        return vStr.equals(v2.toString());
                    }
                }
                return isEqualsAsVIsStringAndV2NotBasic(vStr, vIsZero, v2);
            }
            return isEqualsAsVNotBasicAndV2NotBasic(v, v2);
        }
        protected boolean isEqualsAsVIsNullAndV2NotBasic(Object v, Object v2)     				  { return false;}
        protected boolean isEqualsAsVIsNumberAndV2NotBasic(Number v, Object v2) 				  { return false;}
        protected boolean isEqualsAsVIsCharAndV2NotBasic(Character v, boolean vIsZero, Object v2) { return false; }
        protected boolean isEqualsAsVIsBooleanAndV2NotBasic(Boolean v, Object v2) 				  { return false; }
        protected boolean isEqualsAsVIsStringAndV2NotBasic(String v, boolean vIsZero,  Object v2) { return v.toString().equals(v2.toString()); }
        protected boolean isEqualsAsVNotBasicAndV2NotBasic(Object v, Object v2) 				  { return v.equals(v2); }
    }
    static void assertTrue(boolean b) {
        System.out.println(b);
    }
    static void assertFalse(boolean b) {
        System.out.println(b == false);
    }
    static boolean isEqual(Object v, Object _2) {
        return new ValueComparer().isEquals(v, _2);
    }
    public static void main(String[] args) {
        assertTrue(isEqual(null, null));
        assertTrue(isEqual(null, 0));
        assertTrue(isEqual(null, 0.0));
        assertTrue(isEqual(null, '0'));
        assertTrue(isEqual(null, false));
        assertTrue(isEqual(null, "0"));
        assertTrue(isEqual(0, 0));
        assertTrue(isEqual(0, 0.0));
        assertTrue(isEqual(5, 5.0));
        assertTrue(isEqual(5, "5"));
        assertTrue(isEqual(5, '5'));
        assertFalse(isEqual(0, 1));
        assertFalse(isEqual(0, 0.1));
        assertTrue(isEqual(0.0, 0));
        assertTrue(isEqual(0.0, 0.0));
        assertTrue(isEqual(0.5, 0.5));
        assertFalse(isEqual(0.0, 0.1));
        assertTrue(isEqual(0.0, '0'));
        assertFalse(isEqual(0.0, '1'));
        assertFalse(isEqual(0.0, true));
        assertTrue(isEqual(0.0, false));
        assertTrue(isEqual(0.0, "0"));
        assertFalse(isEqual(0.0, "1"));
    }





}


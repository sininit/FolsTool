package top.fols.box.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.AbstractCollection;  
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
public class HashMapUtils<K,V>  extends AbstractMap<K,V>  implements Map<K,V>, Cloneable, Serializable  
{  
    // 系统默认初始容量，必须是2的n次幂，这是出于优化考虑的  
    static final int DEFAULT_INITIAL_CAPACITY = 16;  
    // 系统默认最大容量  
    static final int MAXIMUM_CAPACITY = 1 << 30;  
    // 系统默认负载因子，可在构造函数中指定  
    static final float DEFAULT_LOAD_FACTOR = 0.75f;  
    // 用于存储的表，长度可以调整，且必须是2的n次幂  
    transient Entry[] table;  
    // 当前map的key-value映射数，也就是当前size  
    transient int size;  
    // 阈值  
    int threshold;  
    // 哈希表的负载因子  
    final float loadFactor;  
    // 用于确保使用迭代器的时候，HashMap并未进行更改  
    transient volatile int modCount;  
    // 构造一个带指定初始容量和加载因子的空 HashMap。  
    public HashMapUtils(int initialCapacity, float loadFactor)
	{  
        // 如果指定初始容量小于0，抛错  
        if (initialCapacity < 0)  
            throw new IllegalArgumentException("Illegal initial capacity: " +  
                                               initialCapacity);  
        // 如果初始容量大于系统默认最大容量，则初始容量为最大容量  
        if (initialCapacity > MAXIMUM_CAPACITY)  
            initialCapacity = MAXIMUM_CAPACITY;  
        // 如果loadFactor小于0，或loadFactor是NaN，则抛错  
        if (loadFactor <= 0 || Float.isNaN(loadFactor))  
            throw new IllegalArgumentException("Illegal load factor: " +  
                                               loadFactor);  

        // 寻找一个2的k次幂capacity恰好大于initialCapacity  
        int capacity = 1;  
        while (capacity < initialCapacity)  
            capacity <<= 1;  

        // 设置加载因子  
        this.loadFactor = loadFactor;  
        // 设置阈值为capacity * loadFactor，实际上当HashMap当前size到达这个阈值时，HashMap就需要扩大一倍了。  
        threshold = (int)(capacity * loadFactor);  
        // 创建一个capacity长度的数组用于保存数据  
        table = new Entry[capacity];  
        // 开始初始化  
        init();  
    }  

    // 构造一个带指定初始容量和默认加载因子 (0.75) 的空 HashMap。  
    public HashMapUtils(int initialCapacity)
	{  
        this(initialCapacity, DEFAULT_LOAD_FACTOR);  
    }  

    // 构造一个具有默认初始容量 (16) 和默认加载因子 (0.75) 的空 HashMap。  
    public HashMapUtils()
	{  
        this.loadFactor = DEFAULT_LOAD_FACTOR;  
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);  
        table = new Entry[DEFAULT_INITIAL_CAPACITY];  
        init();  
    }  

    // 构造一个映射关系与指定 Map 相同的新 HashMap。  
    public HashMapUtils(Map<? extends K, ? extends V> m)
	{  
        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,  
                      DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);  
        putAllForCreate(m);  
    }  

    // 内部公用工具  

    // 定义一个空方法用于未来的子对象扩展，该方法用于初始化之后，插入元素之前  
    void init()
	{  
    }  

    // 预处理hash值，避免较差的离散hash序列，导致桶没有充分利用  
    static int hash(int h)
	{  
        h ^= (h >>> 20) ^ (h >>> 12);  
        return h ^ (h >>> 7) ^ (h >>> 4);  
    }  

    // 返回对应hash值得索引  
    static int indexFor(int h, int length)
	{  
        /***************** 
         * 由于length是2的n次幂，所以h & (length-1)相当于h % length。 
         * 对于length，其2进制表示为1000...0，那么length-1为0111...1。 
         * 那么对于任何小于length的数h，该式结果都是其本身h。 
         * 对于h = length，该式结果等于0。 
         * 对于大于length的数h，则和0111...1位与运算后， 
         * 比0111...1高或者长度相同的位都变成0， 
         * 相当于减去j个length，该式结果是h-j*length， 
         * 所以相当于h % length。 
         * 其中一个很常用的特例就是h & 1相当于h % 2。 
         * 这也是为什么length只能是2的n次幂的原因，为了优化。 
         */  
        return h & (length - 1);  
    }  
    // 返回当前map的key-value映射数，也就是当前size  
    public int size()
	{  
        return size;  
    }  
    // 该HashMap是否是空的，如果size为0，则为空  
    public boolean isEmpty()
	{  
        return size == 0;  
    }  
    // 返回指定键所映射的值；如果对于该键来说，此映射不包含任何映射关系，则返回 null  
    public V get(Object key)
	{  
        // 如果key为null  
        if (key == null)  
            return getForNullKey();  
        // 对hashCode值预处理  
        int hash = hash(key.hashCode());  
        // 得到对应的hash值的桶，如果这个桶不是，就通过next获取下一个桶  
        for (Entry<K,V> e = table[indexFor(hash, table.length)];  
             e != null;  
		e = e.next)
		{  
            Object k;  
            // 如果hash值相等，并且key相等则证明这个桶里的东西是我们想要的  
            if (e.hash == hash && ((k = e.key) == key || key.equals(k)))  
                return e.value;  
        }  
        // 所有桶都找遍了，没找到想要的，所以返回null  
        return null;  
    }  

    // 如果要得到key为null的值，则通过这个获取  
    private V getForNullKey()
	{  
        // 遍历table[0]里的所有桶  
        for (Entry<K,V> e = table[0]; e != null; e = e.next)
		{  
            // 看看桶的key是不是null，是则返回相应值  
            if (e.key == null)  
                return e.value;  
        }  
        // 没找到返回null  
        return null;  
    }  

    // 如果此映射包含对于指定键的映射关系，则返回true  
    public boolean containsKey(Object key)
	{  
        return getEntry(key) != null;  
    }  

    // 通过key获取一个value  
    final Entry<K,V> getEntry(Object key)
	{  
        // 如果key为null，则hash为0，否则用hash函数预处理  
        int hash = (key == null) ? 0 : hash(key.hashCode());  
        // 得到对应的hash值的桶，如果这个桶不是，就通过next获取下一个桶  
        for (Entry<K,V> e = table[indexFor(hash, table.length)];  
             e != null;  
		e = e.next)
		{  
            Object k;  
            // 如果hash值相等，并且key相等则证明这个桶里的东西是我们想要的  
            if (e.hash == hash &&  
                ((k = e.key) == key || (key != null && key.equals(k))))  
                return e;  
        }  
        // 所有桶都找遍了，没找到想要的，所以返回null  
        return null;  
    }  


    // 在此映射中关联指定值与指定键。如果该映射以前包含了一个该键的映射关系，则旧值被替换  
    public V put(K key, V value)
	{  
        // 如果key为null使用putForNullKey来获取  
        if (key == null)  
            return putForNullKey(value);  
        // 使用hash函数预处理hashCode  
        int hash = hash(key.hashCode());  
        // 获取对应的索引  
        int i = indexFor(hash, table.length);  
        // 得到对应的hash值的桶，如果这个桶不是，就通过next获取下一个桶  
        for (Entry<K,V> e = table[i]; e != null; e = e.next)
		{  
            Object k;  
            // 如果hash相同并且key相同  
            if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
			{  
                // 获取当前的value  
                V oldValue = e.value;  
                // 将要存储的value存进去  
                e.value = value;  
                e.recordAccess(this);  
                // 返回旧的value  
                return oldValue;  
            }  
        }  

        modCount++;  
        addEntry(hash, key, value, i);  
        return null;  
    }  

    // key为null怎么放value  
    private V putForNullKey(V value)
	{  
        // 遍历table[0]的所有桶  
        for (Entry<K,V> e = table[0]; e != null; e = e.next)
		{  
            // 如果key是null  
            if (e.key == null)
			{  
                // 取出oldValue，并存入value  
                V oldValue = e.value;  
                e.value = value;  
                e.recordAccess(this);  
                // 返回oldValue  
                return oldValue;  
            }  
        }  
        modCount++;  
        addEntry(0, null, value, 0);  
        return null;  
    }  

    // 看看需不需要创建新的桶  
    private void putForCreate(K key, V value)
	{  
        // 如果key为null，则定义hash为0，否则用hash函数预处理  
        int hash = (key == null) ? 0 : hash(key.hashCode());  
        // 获取对应的索引  
        int i = indexFor(hash, table.length);  

        // 遍历所有桶  
        for (Entry<K,V> e = table[i]; e != null; e = e.next)
		{  
            Object k;  
            // 如果有hash相同，且key相同，那么则不需要创建新的桶，退出  
            if (e.hash == hash &&  
                ((k = e.key) == key || (key != null && key.equals(k))))
			{  
                e.value = value;  
                return;  
            }  
        }  

        // 否则需要创建新的桶  
        createEntry(hash, key, value, i);  
    }  

    // 根据Map创建所有对应的桶  
    private void putAllForCreate(Map<? extends K, ? extends V> m)
	{  
        for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m.entrySet().iterator(); i.hasNext();)
		{  
            Map.Entry<? extends K, ? extends V> e = i.next();  
            putForCreate(e.getKey(), e.getValue());  
        }  
    }  

    // 更具新的容量来resize这个HashMap  
    void resize(int newCapacity)
	{  
        // 保存oldTable  
        Entry[] oldTable = table;  
        // 保存旧的容量  
        int oldCapacity = oldTable.length;  
        // 如果旧的容量已经是系统默认最大容量了，那么将阈值设置成整形的最大值，退出  
        if (oldCapacity == MAXIMUM_CAPACITY)
		{  
            threshold = Integer.MAX_VALUE;  
            return;  
        }  

        // 根据新的容量新建一个table  
        Entry[] newTable = new Entry[newCapacity];  
        // 将table转换成newTable  
        transfer(newTable);  
        // 将table设置newTable  
        table = newTable;  
        // 设置阈值  
        threshold = (int)(newCapacity * loadFactor);  
    }  

    // 将所有格子里的桶都放到新的table中  
    void transfer(Entry[] newTable)
	{  
        // 得到旧的table  
        Entry[] src = table;  
        // 得到新的容量  
        int newCapacity = newTable.length;  
        // 遍历src里面的所有格子  
        for (int j = 0; j < src.length; j++)
		{  
            // 取到格子里的桶（也就是链表）  
            Entry<K,V> e = src[j];  
            // 如果e不为空  
            if (e != null)
			{  
                // 将当前格子设成null  
                src[j] = null;  
                // 遍历格子的所有桶  
                do {  
                    // 取出下个桶  
                    Entry<K,V> next = e.next;  
                    // 寻找新的索引  
                    int i = indexFor(e.hash, newCapacity);  
                    // 设置e.next为newTable[i]保存的桶（也就是链表连接上）  
                    e.next = newTable[i];  
                    // 将e设成newTable[i]  
                    newTable[i] = e;  
                    // 设置e为下一个桶  
                    e = next;  
                } while (e != null);  
            }  
        }  
    }  

    // 将指定映射的所有映射关系复制到此映射中，这些映射关系将替换此映射目前针对指定映射中所有键的所有映射关系  
    public void putAll(Map<? extends K, ? extends V> m)
	{  
        // 看看需要复制多少个映射关系  
        int numKeysToBeAdded = m.size();  
        if (numKeysToBeAdded == 0)  
            return;  

        // 如果要复制的映射关系比阈值还要多  
        if (numKeysToBeAdded > threshold)
		{  
            // 重新计算新的容量先resize  
            int targetCapacity = (int)(numKeysToBeAdded / loadFactor + 1);  
            if (targetCapacity > MAXIMUM_CAPACITY)  
                targetCapacity = MAXIMUM_CAPACITY;  
            int newCapacity = table.length;  
            while (newCapacity < targetCapacity)  
                newCapacity <<= 1;  
            if (newCapacity > table.length)  
                resize(newCapacity);  
        }  

        // 迭代将key-value映射放进该HashMap  
        for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m.entrySet().iterator(); i.hasNext();)
		{  
            Map.Entry<? extends K, ? extends V> e = i.next();  
            put(e.getKey(), e.getValue());  
        }  
    }  

    // 从此映射中移除指定键的映射关系（如果存在）  
    public V remove(Object key)
	{  
        Entry<K,V> e = removeEntryForKey(key);  
        return (e == null ? null : e.value);  
    }  

    // 根据key删除桶，并返回对应value  
    final Entry<K,V> removeEntryForKey(Object key)
	{  
        int hash = (key == null) ? 0 : hash(key.hashCode());  
        int i = indexFor(hash, table.length);  
        // 找到对应的格子  
        Entry<K,V> prev = table[i];  
        Entry<K,V> e = prev;  

        // 遍历所有桶  
        while (e != null)
		{  
            Entry<K,V> next = e.next;  
            Object k;  
            // 如果找到对应的桶  
            if (e.hash == hash &&  
                ((k = e.key) == key || (key != null && key.equals(k))))
			{  
                modCount++;  
                // size减1  
                size--;  
                // 如果第一个就是要删的桶  
                if (prev == e)  
				// 则table[i]等于下一个桶  
                    table[i] = next;  
                else  
				// 否则上一个桶的下一个等于下一个桶  
                    prev.next = next;  
                e.recordRemoval(this);  
                return e;  
            }  
            prev = e;  
            e = next;  
        }  

        return e;  
    }  

    // 根据桶来删除map里的值  
    final Entry<K,V> removeMapping(Object o)
	{  
        // 如果o不是Map.Entry的实例，则退出返回null  
        if (!(o instanceof Map.Entry))  
            return null;  

        // 将o转成Map.Entry  
        Map.Entry<K,V> entry = (Map.Entry<K,V>) o;  
        // 得到他的key  
        Object key = entry.getKey();  
        // 得到对应的hash  
        int hash = (key == null) ? 0 : hash(key.hashCode());  
        // 得到对应的索引  
        int i = indexFor(hash, table.length);  
        Entry<K,V> prev = table[i];  
        Entry<K,V> e = prev;  

        // 遍历所有桶  
        while (e != null)
		{  
            Entry<K,V> next = e.next;  
            // 如果找到对应的桶，则删掉它  
            if (e.hash == hash && e.equals(entry))
			{  
                modCount++;  
                size--;  
                if (prev == e)  
                    table[i] = next;  
                else  
                    prev.next = next;  
                e.recordRemoval(this);  
                return e;  
            }  
            prev = e;  
            e = next;  
        }  

        // 并返回该桶  
        return e;  
    }  

    // 从此映射中移除所有映射关系。此调用返回后，映射将为空  
    public void clear()
	{  
        modCount++;  
        Entry[] tab = table;  
        // 遍历table中的所有格子，然偶后设为null  
        for (int i = 0; i < tab.length; i++)  
            tab[i] = null;  
        // 设置size为0  
        size = 0;  
    }  

    // 如果此映射将一个或多个键映射到指定值，则返回 true  
    public boolean containsValue(Object value)
	{  
		// 如果value为空，则返回containsNullValue函数的返回值  
		if (value == null)  
            return containsNullValue();  

		Entry[] tab = table;  
        // 遍历table所有格子（链表）  
        for (int i = 0; i < tab.length ; i++)  
		// 遍历链表中的每个桶  
            for (Entry e = tab[i] ; e != null ; e = e.next)  
		// 如果值相同，则返回true  
                if (value.equals(e.value))  
                    return true;  
		// 否则返回false  
		return false;  
    }  

    // 对value为null的处理，这里没什么特别的  
    private boolean containsNullValue()
	{  
		Entry[] tab = table;  
        for (int i = 0; i < tab.length ; i++)  
            for (Entry e = tab[i] ; e != null ; e = e.next)  
                if (e.value == null)  
                    return true;  
		return false;  
    }  

    // 返回此 HashMap 实例的浅表副本：并不复制键和值本身  
    public Object clone()
	{  
        HashMapUtils<K,V> result = null;  
		try
		{  
			result = (HashMapUtils<K,V>)super.clone();  
		}
		catch (CloneNotSupportedException e)
		{  
			// assert false;  
		}  
        result.table = new Entry[table.length];  
        result.entrySet = null;  
        result.modCount = 0;  
        result.size = 0;  
        result.init();  
        result.putAllForCreate(this);  

        return result;  
    }  

    // 内置class输入对象，也就是我们说的桶  
    static class Entry<K,V> implements Map.Entry<K,V>
	{  
        final K key;  
        V value;  
        Entry<K,V> next;  
        final int hash;  

        // 构造函数  
        Entry(int h, K k, V v, Entry<K,V> n)
		{  
            value = v;  
            next = n;  
            key = k;  
            hash = h;  
        }  

        // 返回key  
        public final K getKey()
		{  
            return key;  
        }  

        // 返回value  
        public final V getValue()
		{  
            return value;  
        }  

        // 设置value  
        public final V setValue(V newValue)
		{  
			V oldValue = value;  
            value = newValue;  
            return oldValue;  
        }  

        // 是否相同  
        public final boolean equals(Object o)
		{  
            // 如果o不是Map.Entry的实例，那么肯定不相同了  
            if (!(o instanceof Map.Entry))  
                return false;  
            // 将o转成Map.Entry  
            Map.Entry e = (Map.Entry)o;  
            // 得到key和value对比是否相同，相同则为true  
            Object k1 = getKey();  
            Object k2 = e.getKey();  
            if (k1 == k2 || (k1 != null && k1.equals(k2)))
			{  
                Object v1 = getValue();  
                Object v2 = e.getValue();  
                if (v1 == v2 || (v1 != null && v1.equals(v2)))  
                    return true;  
            }  
            // 否则为false  
            return false;  
        }  

        // hashCode  
        public final int hashCode()
		{  
            return (key == null   ? 0 : key.hashCode()) ^  
				(value == null ? 0 : value.hashCode());  
        }  

        // 返回String  
        public final String toString()
		{  
            return getKey() + "=" + getValue();  
        }  

        // 使用该方法证明该key已经在该map中  
        void recordAccess(HashMapUtils<K,V> m)
		{  
        }  

        // 该方法记录该key已经被移除了  
        void recordRemoval(HashMapUtils<K,V> m)
		{  
        }  
    }  

    // 添加一个新的桶来保存该key和value  
    void addEntry(int hash, K key, V value, int bucketIndex)
	{  
		// 保存对应table的值  
		Entry<K,V> e = table[bucketIndex];  
        // 然后用新的桶套住旧的桶，链表  
        table[bucketIndex] = new Entry<K,V>(hash, key, value, e);  
        // 如果当前size大于等于阈值  
        if (size++ >= threshold)  
		// 调整容量  
            resize(2 * table.length);  
    }  

    // 新建一个桶，该方法不需要判断是否超过阈值  
    void createEntry(int hash, K key, V value, int bucketIndex)
	{  
		Entry<K,V> e = table[bucketIndex];  
        table[bucketIndex] = new Entry<K,V>(hash, key, value, e);  
        size++;  
    }  

    // 内部class HashIterator迭代器  
    private abstract class HashIterator<E> implements Iterator<E>
	{  
        Entry<K,V> next;    // 下一个桶  
        int expectedModCount;    // 保护HashMap没有变更  
        int index;        // 当前的索引  
        Entry<K,V> current;    // 当前的桶  

        // 构造方法  
        HashIterator()
		{  
            // 保存modCount，因为如果HashMap进行了任何操作modCount都会增加，所以如果发现modCount变化了，就可以抛出失败  
            expectedModCount = modCount;  
            // 进入第一个桶  
            if (size > 0)
			{  
                Entry[] t = table;  
                while (index < t.length && (next = t[index++]) == null)  
                    ;  
            }  
        }  

        // 看看有没有下一个桶  
        public final boolean hasNext()
		{  
            return next != null;  
        }  

        // 获取下一个桶  
        final Entry<K,V> nextEntry()
		{  
            // modCount变化了，抛出失败  
            if (modCount != expectedModCount)  
                throw new ConcurrentModificationException();  
            // 得到next  
            Entry<K,V> e = next;  
            // 如果next为空，抛出失败  
            if (e == null)  
                throw new NoSuchElementException();  

            // 如果next.next为空，将next定义为下一个格子中的桶，否则为该格子的下一个桶  
            if ((next = e.next) == null)
			{  
                Entry[] t = table;  
                while (index < t.length && (next = t[index++]) == null)  
                    ;  
            }  
			// 给current赋值  
			current = e;  
            // 返回e  
            return e;  
        }  

        // 删除  
        public void remove()
		{  
            // 如果当前为空，抛出  
            if (current == null)  
                throw new IllegalStateException();  
            // modCount变化了，抛出失败  
            if (modCount != expectedModCount)  
                throw new ConcurrentModificationException();  
            // 获得当前的key  
            Object k = current.key;  
            // 设置current为null  
            current = null;  
            // 删除掉对应key的元素  
            HashMapUtils.this.removeEntryForKey(k);  
            // 重置expectedModCount  
            expectedModCount = modCount;  
        }  

    }  

    // 内部class ValueIterator迭代器，我们可以看到修改了next方法  
    private final class ValueIterator extends HashIterator<V>
	{  
        public V next()
		{  
            return nextEntry().value;  
        }  
    }  

    // 内部class KeyIterator迭代器，我们可以看到修改了next方法  
    private final class KeyIterator extends HashIterator<K>
	{  
        public K next()
		{  
            return nextEntry().getKey();  
        }  
    }  

    // 内部class EntryIterator迭代器，我们可以看到修改了next方法  
    private final class EntryIterator extends HashIterator<Map.Entry<K,V>>
	{  
        public Map.Entry<K,V> next()
		{  
            return nextEntry();  
        }  
    }  

    // 定义对应的 iterator() 方法  
    Iterator<K> newKeyIterator()
	{  
        return new KeyIterator();  
    }  
    Iterator<V> newValueIterator()
	{  
        return new ValueIterator();  
    }  
    Iterator<Map.Entry<K,V>> newEntryIterator()
	{  
        return new EntryIterator();  
    }  

    private transient Set<Map.Entry<K,V>> entrySet = null;  

    /** 
     * 返回此映射中所包含的键的 Set 视图。 
     * 该 set 受映射的支持，所以对映射的更改将反映在该 set 中， 
     * 反之亦然。如果在对 set 进行迭代的同时修改了映射（通过迭代器自己的 remove 操作除外）， 
     * 则迭代结果是不确定的。该 set 支持元素的移除，通过  
     * Iterator.remove、Set.remove、removeAll、retainAll 和 clear 操作 
     * 可从该映射中移除相应的映射关系。它不支持 add 或 addAll 操作。 
     */ 
	private transient Set<K> keySet = null;
    public Set<K> keySet()
	{  
        Set<K> ks = keySet;  
        // 如果keySet为空，则通过新建一个KeySet  
        return (ks != null ? ks : (keySet = new KeySet()));  
    }  

    // 内部类KeySet  
    private final class KeySet extends AbstractSet<K>
	{  
        // 定义iterator方法  
        public Iterator<K> iterator()
		{  
            return newKeyIterator();  
        }  
        // 定义size  
        public int size()
		{  
            return size;  
        }  
        // 定义contains  
        public boolean contains(Object o)
		{  
            return containsKey(o);  
        }  
        // 定义remove  
        public boolean remove(Object o)
		{  
            return HashMapUtils.this.removeEntryForKey(o) != null;  
        }  
        // 定义clear  
        public void clear()
		{  
            HashMapUtils.this.clear();  
        }  
    }  

    /** 
     * 返回此映射所包含的值的 Collection 视图。 
     * 该 collection 受映射的支持，所以对映射的更改将反映在该 collection 中， 
     * 反之亦然。如果在对 collection 进行迭代的同时修改了映射（通过迭代器自己的 remove 操作除外）， 
     * 则迭代结果是不确定的。该 collection 支持元素的移除， 
     * 通过 Iterator.remove、Collection.remove、removeAll、retainAll 和 clear 操作 
     * 可从该映射中移除相应的映射关系。它不支持 add 或 addAll 操作。 
     */ 
	private Collection<V> values;
    public Collection<V> values()
	{  
        Collection<V> vs = values;  
        return (vs != null ? vs : (values = new Values()));  
    }  

    // 内部类Values  
    private final class Values extends AbstractCollection<V>
	{  
        public Iterator<V> iterator()
		{  
            return newValueIterator();  
        }  
        public int size()
		{  
            return size;  
        }  
        public boolean contains(Object o)
		{  
            return containsValue(o);  
        }  
        public void clear()
		{  
            HashMapUtils.this.clear();  
        }  
    }  

    /** 
     * 返回此映射所包含的映射关系的 Set 视图。  
     * 该 set 受映射支持，所以对映射的更改将反映在此 set 中， 
     * 反之亦然。如果在对 set 进行迭代的同时修改了映射 
     * （通过迭代器自己的 remove 操作，或者通过在该迭代器返回的映射项上执行 setValue 操作除外）， 
     * 则迭代结果是不确定的。该 set 支持元素的移除， 
     * 通过 Iterator.remove、Set.remove、removeAll、retainAll 和 clear 操作 
     * 可从该映射中移除相应的映射关系。它不支持 add 或 addAll 操作。 
     */  
    public Set<Map.Entry<K,V>> entrySet()
	{  
		return entrySet0();  
    }  

    private Set<Map.Entry<K,V>> entrySet0()
	{  
        Set<Map.Entry<K,V>> es = entrySet;  
        return es != null ? es : (entrySet = new EntrySet());  
    }  

    // 内部类EntrySet  
    private final class EntrySet extends AbstractSet<Map.Entry<K,V>>
	{  
        public Iterator<Map.Entry<K,V>> iterator()
		{  
            return newEntryIterator();  
        }  
        public boolean contains(Object o)
		{  
            if (!(o instanceof Map.Entry))  
                return false;  
            Map.Entry<K,V> e = (Map.Entry<K,V>) o;  
            Entry<K,V> candidate = getEntry(e.getKey());  
            return candidate != null && candidate.equals(e);  
        }  
        public boolean remove(Object o)
		{  
            return removeMapping(o) != null;  
        }  
        public int size()
		{  
            return size;  
        }  
        public void clear()
		{  
            HashMapUtils.this.clear();  
        }  
    }  

    // 序列化方法  
    private void writeObject(java.io.ObjectOutputStream s)  throws IOException  
    {  
		Iterator<Map.Entry<K,V>> i =  (size > 0) ? entrySet0().iterator() : null;  
		s.defaultWriteObject();  
		s.writeInt(table.length);  
		s.writeInt(size);  
		if (i != null)
		{  
			while (i.hasNext())
			{  
				Map.Entry<K,V> e = i.next();  
				s.writeObject(e.getKey());  
				s.writeObject(e.getValue());  
			}  
        }  
    }  

    private static final long serialVersionUID = 362498820763181265L;  

    // 通过序列读取对象  
    private void readObject(java.io.ObjectInputStream s)  throws IOException, ClassNotFoundException  
    {  
		s.defaultReadObject();  
		int numBuckets = s.readInt();  
		table = new Entry[numBuckets];  
        init();  
		int size = s.readInt();  
		for (int i=0; i < size; i++)
		{  
			K key = (K) s.readObject();  
			V value = (V) s.readObject();  
			putForCreate(key, value);  
		}  
    }  

    int   capacity()
	{ return table.length; }  
    float loadFactor()
	{ return loadFactor;   }  
}  


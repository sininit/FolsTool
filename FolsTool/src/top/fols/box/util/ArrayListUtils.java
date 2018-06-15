package top.fols.box.util;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.RandomAccess;
public class ArrayListUtils<E> extends AbstractList<E>  implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{  
    // 序列版本号  
    private static final long serialVersionUID = 8683452581122892189L;  
    // ArrayList基于该数组实现，用该数组保存数据 
    private transient Object[] elementData;  
    // ArrayList中实际数据的数量  
    private int size;  
    // ArrayList带容量大小的构造函数。  
	public ArrayListUtils(Object obj)
	{
		if (obj == null)
			obj = new Object[0];
		Class ObjectClass = obj.getClass();
		if (ObjectClass.isArray() == false)
			throw new ClassCastException("cannot convert " + ObjectClass.getCanonicalName() + " to Array");
		elementData = XObjects.toObjectArray(obj);  
        size = elementData.length;  
	}
    public ArrayListUtils(int initialCapacity)
	{  
        super();  
        if (initialCapacity < 0)  
            throw new IllegalArgumentException("Illegal Capacity: " +  
                                               initialCapacity);  
        // 新建一个数组  
        this.elementData = new Object[initialCapacity];  
    }  
    // ArrayList无参构造函数。默认容量是10。  
    public ArrayListUtils()
	{  
        this(10);  
    }  
    // 创建一个包含collection的ArrayList  
    public ArrayListUtils(Collection<? extends E> c)
	{  
        elementData = c.toArray();  
        size = elementData.length;  
        if (elementData.getClass() != Object[].class)  
            elementData = Arrays.copyOf(elementData, size, Object[].class);  
    }  
    // 将当前容量值设为实际元素个数  
    public void trimToSize()
	{  
        modCount++;  
        int oldCapacity = elementData.length;  
        if (size < oldCapacity)
		{  
            elementData = Arrays.copyOf(elementData, size);  
        }  
    }  
    // 确定ArrarList的容量。  
    // 若ArrayList的容量不足以容纳当前的全部元素，设置 新的容量=“(原始容量x3)/2 + 1”  
    public void ensureCapacity(int minCapacity)
	{  
        // 将“修改统计数”+1，该变量主要是用来实现fail-fast机制的  
        modCount++;  
        int oldCapacity = elementData.length;  
        // 若当前容量不足以容纳当前的元素个数，设置 新的容量=“(原始容量x3)/2 + 1”  
        if (minCapacity > oldCapacity)
		{  
            Object oldData[] = elementData;  
            int newCapacity = (oldCapacity * 3) / 2 + 1;  
			//如果还不够，则直接将minCapacity设置为当前容量
            if (newCapacity < minCapacity)  
                newCapacity = minCapacity;  
            elementData = Arrays.copyOf(elementData, newCapacity);  
        }  
    }  
    // 返回ArrayList的实际大小  
    public int size()
	{  
        return size;  
    }  
    // ArrayList是否包含Object(o)  
    public boolean contains(Object o)
	{  
        return indexOf(o) >= 0;  
    }  
    //返回ArrayList是否为空  
    public boolean isEmpty()
	{  
        return size == 0;  
    }  
    // 正向查找，返回元素的索引值  
    public int indexOf(Object o)
	{  
        if (o == null)
		{  
            for (int i = 0; i < size; i++)  
				if (elementData[i] == null)  
					return i;  
		}
		else
		{  
			for (int i = 0; i < size; i++)  
                if (o.equals(elementData[i]))  
                    return i;  
		}  
		return -1;  
	}
    // 反向查找(从数组末尾向开始查找)，返回元素(o)的索引值  
    public int lastIndexOf(Object o)
	{  
        if (o == null)
		{  
            for (int i = size - 1; i >= 0; i--)  
				if (elementData[i] == null)  
					return i;  
        }
		else
		{  
            for (int i = size - 1; i >= 0; i--)  
				if (o.equals(elementData[i]))  
					return i;  
        }  
        return -1;  
    }  
    // 返回ArrayList的Object数组  
    public Object[] toArray()
	{  
        return Arrays.copyOf(elementData, size);  
    }
	public ArrayListUtils arraycopy(int p2, java.lang.Object p3, int p4, int p5)
	{
		XArrays.arraycopy(elementData, p2, p3, p4, p5);
		return this;
	}
	public ArrayListUtils arraycopyTraverse(int p2, java.lang.Object p3, int p4, int p5)
	{
		XArrays.arraycopyTraverse(elementData, p2, p3, p4, p5);
		return this;
	}
	public Object subArrayTraverse(int startIndex, int EndIndex, Class newElementType)
	{
		if (EndIndex > size)  
			throw new IndexOutOfBoundsException(  
				"start: " + startIndex + ", end: " + EndIndex + ", Size: " + size);
		Object array = Array.newInstance(newElementType, EndIndex - startIndex);
		XArrays.arraycopyTraverse(elementData, startIndex, array, 0, EndIndex - startIndex);
		return array;
	}
	public Object[] subArray(int startIndex, int EndIndex)
	{ 
		if (EndIndex > size)  
			throw new IndexOutOfBoundsException(  
				"start: " + startIndex + ", end: " + EndIndex + ", Size: " + size);
        return Arrays.copyOfRange(elementData, startIndex, EndIndex);  
    }
    // 返回ArrayList元素组成的数组
    public <T> T[] toArray(T[] a)
	{  
        // 若数组a的大小 < ArrayList的元素个数；  
        // 则新建一个T[]数组，数组大小是“ArrayList的元素个数”，并将“ArrayList”全部拷贝到新数组中  
        if (a.length < size)  
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());  

        // 若数组a的大小 >= ArrayList的元素个数；  
        // 则将ArrayList的全部元素都拷贝到数组a中。  
        System.arraycopy(elementData, 0, a, 0, size);  
        if (a.length > size)  
            a[size] = null;  
        return a;  
    }  
    // 获取index位置的元素值  
    public E get(int index)
	{  
        RangeCheck(index);  
        return (E) elementData[index];  
    }  
    // 设置index位置的值为element  
    public E set(int index, E element)
	{  
        RangeCheck(index);  
        E oldValue = (E) elementData[index];  
        elementData[index] = element;  
        return oldValue;  
    }  
    // 将e添加到ArrayList中  
    public boolean add(E e)
	{  
        ensureCapacity(size + 1);  // Increments modCount!!  
        elementData[size++] = e;  
        return true;  
    }  
    // 将e添加到ArrayList的指定位置  
    public void add(int index, E element)
	{  
        if (index > size || index < 0)  
            throw new IndexOutOfBoundsException(  
				"Index: " + index + ", Size: " + size);  

        ensureCapacity(size + 1);  // Increments modCount!!  
        System.arraycopy(elementData, index, elementData, index + 1,  
						 size - index);  
        elementData[index] = element;  
        size++;  
    }  
    // 删除ArrayList指定位置的元素  
    public E remove(int index)
	{  
        RangeCheck(index);  
        modCount++;  
        E oldValue = (E) elementData[index];  
        int numMoved = size - index - 1;  
        if (numMoved > 0)  
            System.arraycopy(elementData, index + 1, elementData, index,  
							 numMoved);  
        elementData[--size] = null; // Let gc do its work  
        return oldValue;  
    }  


    // 快速删除第index个元素  
    private void fastRemove(int index)
	{  
        modCount++;  
        int numMoved = size - index - 1;  
        // 从"index+1"开始，用后面的元素替换前面的元素。  
        if (numMoved > 0)  
            System.arraycopy(elementData, index + 1, elementData, index,  
                             numMoved);  
        // 将最后一个元素设为null  
        elementData[--size] = null; // Let gc do its work  
    }  

    // 删除元素  
    public boolean remove(Object o)
	{  
        if (o == null)
		{  
            for (int index = 0; index < size; index++)  
				if (elementData[index] == null)
				{  
					fastRemove(index);  
					return true;  
				}  
        }
		else
		{  
            // 便利ArrayList，找到“元素o”，则删除，并返回true。  
            for (int index = 0; index < size; index++)  
				if (o.equals(elementData[index]))
				{  
					fastRemove(index);  
					return true;  
				}  
        }  
        return false;  
    }  

    // 清空ArrayList，将全部的元素设为null  
    public void clear()
	{  
        modCount++;  

        for (int i = 0; i < size; i++)  
            elementData[i] = null;  

        size = 0;  
    }  

    // 将集合c追加到ArrayList中  
    public boolean addAll(Collection<? extends E> c)
	{  
        Object[] a = c.toArray();  
        int numNew = a.length;  
        ensureCapacity(size + numNew);  // Increments modCount  
        System.arraycopy(a, 0, elementData, size, numNew);  
        size += numNew;  
        return numNew != 0;  
    }  

    // 从index位置开始，将集合c添加到ArrayList  
    public boolean addAll(int index, Collection<? extends E> c)
	{  
        if (index > size || index < 0)  
            throw new IndexOutOfBoundsException(  
				"Index: " + index + ", Size: " + size);  

        Object[] a = c.toArray();  
        int numNew = a.length;  
        ensureCapacity(size + numNew);  // Increments modCount  

        int numMoved = size - index;  
        if (numMoved > 0)  
            System.arraycopy(elementData, index, elementData, index + numNew,  
							 numMoved);  

        System.arraycopy(a, 0, elementData, index, numNew);  
        size += numNew;  
        return numNew != 0;  
    }  



    // 将集合c追加到ArrayList中  
    public boolean addAllTraverse(Object c)
	{  
        int numNew = Array.getLength(c);
        ensureCapacity(size + numNew);  // Increments modCount  
        XArrays.arraycopyTraverse(c, 0, elementData, size, numNew);  
        size += numNew;  
        return numNew != 0;  
    }  

    // 从index位置开始，将集合c添加到ArrayList  
    public boolean addAllTraverse(int index, Object c)
	{  
        if (index > size || index < 0)  
            throw new IndexOutOfBoundsException(  
				"Index: " + index + ", Size: " + size);  

        int numNew = Array.getLength(c);  
        ensureCapacity(size + numNew);  // Increments modCount  

        int numMoved = size - index;  
        if (numMoved > 0)  
            XArrays.arraycopyTraverse(elementData, index, elementData, index + numNew,  
									  numMoved);  

        XArrays.arraycopyTraverse(c, 0, elementData, index, numNew);  
        size += numNew;  
        return numNew != 0;  
    }






    // 删除fromIndex到toIndex之间的全部元素。
    protected void removeRange(int fromIndex, int toIndex)
	{  
		modCount++;  
		int numMoved = size - toIndex;  
        System.arraycopy(elementData, toIndex, elementData, fromIndex,  
                         numMoved);  

		// Let gc do its work  
		int newSize = size - (toIndex - fromIndex);  
		while (size != newSize)  
			elementData[--size] = null;  
    }  

    private void RangeCheck(int index)
	{  
		if (index >= size)  
			throw new IndexOutOfBoundsException(  
				"Index: " + index + ", Size: " + size);  
    }  


    // 克隆函数  
    public Object clone()
	{  
        try
		{  
            ArrayListUtils<E> v = (ArrayListUtils<E>) super.clone();  
            // 将当前ArrayList的全部元素拷贝到v中  
            v.elementData = Arrays.copyOf(elementData, size);  
            v.modCount = 0;  
            return v;  
        }
		catch (CloneNotSupportedException e)
		{  
            // this shouldn't happen, since we are Cloneable  
            throw new InternalError();  
        }  
    }  


    // java.io.Serializable的写入函数  
    // 将ArrayList的“容量，所有的元素值”都写入到输出流中  
    private void writeObject(java.io.ObjectOutputStream s)  
	throws java.io.IOException
	{  
		// Write out element count, and any hidden stuff  
		int expectedModCount = modCount;  
		s.defaultWriteObject();  

        // 写入“数组的容量”  
        s.writeInt(elementData.length);  

		// 写入“数组的每一个元素”  
		for (int i=0; i < size; i++)  
            s.writeObject(elementData[i]);  

		if (modCount != expectedModCount)
		{  
            throw new ConcurrentModificationException();  
        }  

    }  

    // java.io.Serializable的读取函数：根据写入方式读出  
    // 先将ArrayList的“容量”读出，然后将“所有的元素值”读出  
    private void readObject(java.io.ObjectInputStream s)  
	throws java.io.IOException, ClassNotFoundException
	{  
        // Read in size, and any hidden stuff  
        s.defaultReadObject();  

        // 从输入流中读取ArrayList的“容量”  
        int arrayLength = s.readInt();  
        Object[] a = elementData = new Object[arrayLength];  

        // 从输入流中将“所有的元素值”读出  
        for (int i=0; i < size; i++)  
            a[i] = s.readObject();  
    }



	public void removeRanges(int fromIndex, int toIndex)
	{
		RangeCheck(toIndex);
		removeRange(fromIndex, toIndex);
	}
	public Object[] getBuf()
	{
		return elementData;
	}
}

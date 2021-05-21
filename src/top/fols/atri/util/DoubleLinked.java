package top.fols.atri.util;

import top.fols.atri.lang.Objects;
import top.fols.box.util.XStringJoiner;

import java.io.Serializable;

public class DoubleLinked<T extends Object> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected T content;
	public T content() {
		return this.content;
	}
	public DoubleLinked<T> setContent(T content) {
		this.content = content;
		return this;
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return null == this.content ? 0 : this.content.hashCode();
	}
	public int superHashCode(){ return super.hashCode(); }

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if (obj instanceof DoubleLinked) {
			return null == this.content ? null == ((DoubleLinked<?>) obj).content
					: this.content.equals(((DoubleLinked<?>) obj).content);
		} else {
			return false;
		}
	}



	@Override
	public String toString() {
		// TODO: Implement this method
		return null == this.content ? null : this.content.toString();
	}

	/**
	 *
	 * @param obj1
	 * @param obj2
	 * @return Is the value equals
	 */
	public static boolean equalsContent(Object obj1, Object obj2) {
		// TODO: Implement this method
		Object val1 = obj1 instanceof DoubleLinked ? (((DoubleLinked<?>) obj1).content) : obj1;
		Object val2 = obj2 instanceof DoubleLinked ? (((DoubleLinked<?>) obj2).content) : obj2;
		return Objects.equals(val1, val2);
	}





	private DoubleLinked<T> prev = null, next = null;


	public DoubleLinked() {
		super();
	}
	public DoubleLinked(T object) {
		this.content = object;
	}




	// 获取上一个item
	public DoubleLinked<T> getPrev() {
		return this.prev;
	}
	// 获取下一个item
	public DoubleLinked<T> getNext() {
		return this.next;
	}


	public boolean hasPrev() {
		return null != this.prev;
	}
	public boolean hasNext() {
		return null != this.next;
	}





	/**
	 * first >> elements >> last
	 */
	public void addNext(DoubleLinked<T> element) throws NullPointerException {
		DoubleLinked._addNext(this, element);
	}
	// 添加下一个item
	static <T extends Object> void _addNext(DoubleLinked<T> index, DoubleLinked<T> element) throws NullPointerException {
		if (null == index) {
			throw new NullPointerException("index");
		}
		if (null == element) {
			throw new NullPointerException("need add item for null");
		}

		/*
		 * [index, ...] 原index下个元素引索
		 */
		DoubleLinked<T> indexnext = index.next;
		if (indexnext == element) {
			return;
		}

		/**
		 * unlink element
		 * 将原来 element 两端连接 并且删除 element
		 */
		DoubleLinked._remove(element);

		/**
		 * link element
		 */
		index.next = element;
		if (null != indexnext) {
			indexnext.prev = element;
		}

		element.prev = index;
		element.next = indexnext;
	}
	public void remove() {
		DoubleLinked._remove(this);
	}
	static <T extends Object> void _remove(DoubleLinked<T> element) {
		/**
		 * 取消链接 next
		 * index 存在下个元素，将(index.next) 的 prev 连接到 index.prev
		 * 将 index.prev 删除
		 */
		if (null != element.next) {
			element.next.prev = element.prev;
		}
		/**
		 * 取消链接 prev
		 * index 存在上个元素，将(index.prev) 的 next 连接到 index.next
		 * 将 index.next 删除
		 */
		if (null != element.prev) {
			element.prev.next = element.next;
		}
		element.prev = element.next = null;
	}
	/**
	 *
	 * @param newFirst new First
	 * @return new First
	 * @throws NullPointerException
	 * @throws RuntimeException
	 */
	public void addFirst(DoubleLinked<T> newFirst) throws NullPointerException, RuntimeException {
		DoubleLinked._addFirst(this, newFirst);
	}
	static <T extends Object> void _addFirst(DoubleLinked<T> originalFirst, DoubleLinked<T> newFirst) throws NullPointerException, RuntimeException {
		if (null == originalFirst) {
			throw new NullPointerException("original-first for null");
		}
		if (null == newFirst) {
			throw new NullPointerException("new-first for null");
		}
		if (originalFirst == newFirst) {
			return;
		}
		if (!originalFirst.isFirst()) {
			throw new RuntimeException("only may be added from the first");
		}
		DoubleLinked._remove(newFirst);

//		newFirst.prev = null;
		newFirst.next = originalFirst;

		originalFirst.prev = newFirst;
//		originalFirst.next = originalFirst.next;

	}







	/*
	 * 是否为第一个
	 */
	public boolean isFirst() {
		return null == this.prev;
	}

	/*
	 * 是否为最后一个
	 */
	public boolean isLast() {
		return null == this.next;
	}




	/*
	 * 没有任何链接 独立item 孤儿
	 */
	public boolean isOrphan() {
		return null == this.prev && null == this.next;
	}








	// 获取第一个元素
	public DoubleLinked<T> findFirst() {
		DoubleLinked<T> now;
		for (now = this; null != now.prev;) {
			now = now.prev;
		}
		return now;
	}

	// 获取最后一个元素
	public DoubleLinked<T> findLast() {
		DoubleLinked<T> now;
		for (now = this; null != now.next;) {
			now = now.next;
		}
		return now;
	}



	public <T extends Object> String toStringFromFirstStart() {
		return         DoubleLinked.toStringFromFirstStart(this);
	}
	public <T extends Object> String toStringFromLastStart() {
		return         DoubleLinked.toStringFromLastStart(this);
	}
	public static <T extends Object> String toStringFromFirstStart(DoubleLinked<T> item) {
		if (null == item) {
			return String.valueOf(item);
		} else {
			XStringJoiner strbuf = new XStringJoiner(",", "{", "}");
			DoubleLinked element = item.findFirst();
			do {
				strbuf.add(element.toString());
			} while (null != (element = element.getNext()));
			return strbuf.toString();
		}
	}
	public static <T extends Object> String toStringFromLastStart(DoubleLinked<T> item) {
		if (null == item) {
			return String.valueOf(item);
		} else {
			XStringJoiner strbuf = new XStringJoiner(",", "{", "}");
			DoubleLinked element = item.findLast();
			do {
				strbuf.add(element.toString());
			} while (null != (element = element.getPrev()));
			return strbuf.toString();
		}
	}

}


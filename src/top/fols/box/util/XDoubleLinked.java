package top.fols.box.util;

import java.io.Serializable;

public class XDoubleLinked<T extends Object> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected T content;
	public T content() {
		return this.content;
	}
	public XDoubleLinked<T> setContent(T content) {
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
		if (obj instanceof XDoubleLinked) {
			return null == this.content ? null == ((XDoubleLinked<?>) obj).content
					: this.content.equals(((XDoubleLinked<?>) obj).content);
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
		Object val1 = obj1 instanceof XDoubleLinked ? (((XDoubleLinked<?>) obj1).content) : obj1;
		Object val2 = obj2 instanceof XDoubleLinked ? (((XDoubleLinked<?>) obj2).content) : obj2;
		return XObjects.isEquals(val1, val2);
	}





	private XDoubleLinked<T> prev = null, next = null;


	public XDoubleLinked() {
		super();
	}
	public XDoubleLinked(T object) {
		this.content = object;
	}




	// 获取上一个item
	public XDoubleLinked<T> getPrev() {
		return this.prev;
	}
	// 获取下一个item
	public XDoubleLinked<T> getNext() {
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
	public void addNext(XDoubleLinked<T> element) throws NullPointerException {
		XDoubleLinked._addNext(this, element);
	}
	// 添加下一个item
	static <T extends Object> void _addNext(XDoubleLinked<T> index, XDoubleLinked<T> element) throws NullPointerException {
		if (null == index) {
			throw new NullPointerException("index");
		}
		if (null == element) {
			throw new NullPointerException("need add item for null");
		}

		/*
		 * [index, ...] 原index下个元素引索
		 */
		XDoubleLinked<T> indexnext = index.next;
		if (indexnext == element) {
			return;
		}

		/**
		 * unlink element
		 * 将原来 element 两端连接 并且删除 element
		 */
		XDoubleLinked._remove(element);

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
		XDoubleLinked._remove(this);
	}
	static <T extends Object> void _remove(XDoubleLinked<T> element) {
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
	public void addFirst(XDoubleLinked<T> newFirst) throws NullPointerException, RuntimeException {
		XDoubleLinked._addFirst(this, newFirst);
	}
	static <T extends Object> void _addFirst(XDoubleLinked<T> originalFirst, XDoubleLinked<T> newFirst) throws NullPointerException, RuntimeException {
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
		XDoubleLinked._remove(newFirst);

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
	public XDoubleLinked<T> findFirst() {
		XDoubleLinked<T> now;
		for (now = this; null != now.prev;) {
			now = now.prev;
		}
		return now;
	}

	// 获取最后一个元素
	public XDoubleLinked<T> findLast() {
		XDoubleLinked<T> now;
		for (now = this; null != now.next;) {
			now = now.next;
		}
		return now;
	}



	public <T extends Object> String toStringFromFirstStart() {
		return         XDoubleLinked.toStringFromFirstStart(this);
	}
	public <T extends Object> String toStringFromLastStart() {
		return         XDoubleLinked.toStringFromLastStart(this);
	}
	public static <T extends Object> String toStringFromFirstStart(XDoubleLinked<T> item) {
		if (null == item) {
			return String.valueOf(item);
		} else {
			XStringJoiner strbuf = new XStringJoiner(",", "{", "}");
			XDoubleLinked element = item.findFirst();
			do {
				strbuf.add(element.toString());
			} while (null != (element = element.getNext()));
			return strbuf.toString();
		}
	}
	public static <T extends Object> String toStringFromLastStart(XDoubleLinked<T> item) {
		if (null == item) {
			return String.valueOf(item);
		} else {
			XStringJoiner strbuf = new XStringJoiner(",", "{", "}");
			XDoubleLinked element = item.findLast();
			do {
				strbuf.add(element.toString());
			} while (null != (element = element.getPrev()));
			return strbuf.toString();
		}
	}

}


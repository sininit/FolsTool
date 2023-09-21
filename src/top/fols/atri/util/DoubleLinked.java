package top.fols.atri.util;

import top.fols.atri.assist.util.StringJoiner;
import top.fols.atri.lang.Objects;

import java.io.Serializable;

@SuppressWarnings({"EqualsReplaceableByObjectsCall", "DanglingJavadoc", "UnusedReturnValue"})
public class DoubleLinked<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected DoubleLinked<T> prev = null, next = null;
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
	 * @return Is the tip equals
	 */
	public static boolean equalsContent(Object obj1, Object obj2) {
		// TODO: Implement this method
		Object val1 = obj1 instanceof DoubleLinked ? (((DoubleLinked<?>) obj1).content) : obj1;
		Object val2 = obj2 instanceof DoubleLinked ? (((DoubleLinked<?>) obj2).content) : obj2;
		return Objects.equals(val1, val2);
	}







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


	public DoubleLinked<T> disconAfter() {
		if (null != this.next) {
			DoubleLinked<T> next = this.next;
			this.next = null;
			return next;
		}
		return null;
	}
	public DoubleLinked<T> disconBefore() {
		if (null != this.prev) {
			DoubleLinked<T> prev = this.prev;
			this.prev = null;
			return prev;
		}
		return null;
	}



	/**
	 * first >> elements >> last
	 */
	public void addNext(DoubleLinked<T> element) throws NullPointerException {
		DoubleLinked._addNext(this, element);
	}
	// 添加下一个item
	static <T> void _addNext(DoubleLinked<T> index, DoubleLinked<T> element) throws NullPointerException {
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
	static <T> void _remove(DoubleLinked<T> element) {
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


	public void addFirst(DoubleLinked<T> newFirst) throws RuntimeException {
		DoubleLinked._addFirst(this, newFirst);
	}
	static <T> void _addFirst(DoubleLinked<T> originalFirst, DoubleLinked<T> newFirst) throws NullPointerException, RuntimeException {
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



	public void addPrev(DoubleLinked<T> prev_element) throws RuntimeException {
		DoubleLinked._addPrev(this, prev_element);
	}
	static <T> void _addPrev(DoubleLinked<T> current, DoubleLinked<T> prevElement) throws NullPointerException, RuntimeException {
		if (null == current) {
			throw new NullPointerException("null current element");
		}
		if (null == prevElement) {
			throw new NullPointerException("null prev element");
		}
		if (current == prevElement) {
			return;
		}
		DoubleLinked._remove(prevElement);

		prevElement.prev = current.prev;
		prevElement.next = current;

		current.prev = prevElement;
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



	public String toStringFromFirstStart() {
		return         DoubleLinked.toStringFromFirstStart(this);
	}
	public String toStringFromLastStart() {
		return         DoubleLinked.toStringFromLastStart(this);
	}
	public static <T> String toStringFromFirstStart(DoubleLinked<T> item) {
		if (null == item) {
			return String.valueOf((Object) null);
		} else {
			StringJoiner sj = new StringJoiner(",", "{", "}");
			DoubleLinked<T> element = item.findFirst();
			do {
				sj.add(element.toString());
			} while (null != (element = element.getNext()));
			return sj.toString();
		}
	}
	public static <T> String toStringFromLastStart(DoubleLinked<T> item) {
		if (null == item) {
			return String.valueOf((Object) null);
		} else {
			StringJoiner sj = new StringJoiner(",", "{", "}");
			DoubleLinked<T> element = item.findLast();
			do {
				sj.add(element.toString());
			} while (null != (element = element.getPrev()));
			return sj.toString();
		}
	}

}


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





	protected XDoubleLinked<T> prev = null, next = null;
	public XDoubleLinked() {
	}
	public XDoubleLinked(T object) {
		this.content = object;
	}

	// 获取上一个item
	public XDoubleLinked<T> getPrev() {
		return this.prev;
	}
	public static <T extends Object> XDoubleLinked<T> getPrev(XDoubleLinked<T> element) {
		return null == element ? null : element.getPrev();
	}


	// 获取下一个item
	public XDoubleLinked<T> getNext() {
		return this.next;
	}
	public static <T extends Object> XDoubleLinked<T> getNext(XDoubleLinked<T> element) {
		return null == element ? null : element.getNext();
	}


	public boolean hasPrev() {
		return null != this.prev;
	}
	public static <T extends Object> boolean hasPrev(XDoubleLinked<T> element) {
		return null == element ? false : element.hasPrev();
	}


	public boolean hasNext() {
		return null != this.next;
	}
	public static <T extends Object> boolean hasNext(XDoubleLinked<T> element) {
		return null == element ? false : element.hasNext();
	}

	/*
	 * bottom >> elements >> top
	 */

	// 添加下一个item
	public void addNext(XDoubleLinked<T> item) {
		this.addNext(this, item);
	}

	public static <T extends Object> void addNext(XDoubleLinked<T> index, XDoubleLinked<T> addelement) throws NullPointerException {
		if (null == index) {
			throw new NullPointerException("item for null");
		}
		if (null == addelement) {
			throw new NullPointerException("need add item for null");
		}

		/*
		 * [index, ...] 原index下个元素引索
		 */
		XDoubleLinked<T> itemnext = index.next;
		/*
		 * 如果index 的下一个元素就是addelement那么直接退出 (在位置重复添加元素了)
		 */
		if (itemnext == addelement) {
			return;
		}

		/*
		 * 将index的下一个元素设置成 addelement
		 */
		index.next = addelement;
		/*
		 * 将index 的 下一个元素 的上一个元素引索设置为 addelement
		 */
		if (null != itemnext) {
			itemnext.prev = addelement;
		}

		if (null != addelement.prev) {
			/*
			 * [a, addelement, index, ...] ==> [a, index, addelement, ...]
			 * 
			 * 将 原来addelement所在的位置的上一个元素 的下一个元素引索 设置为 addelement的下一个元素引索
			 */
			addelement.prev.next = addelement.next;
		}
		if (null != addelement.next) {
			addelement.next.prev = addelement.prev;
		}

		/*
		 * 将addelement 的上一个元素引索设置为index
		 */
		addelement.prev = index;

		/*
		 * 将addelement的下个元素引索改为原index的下个元素引索
		 */
		addelement.next = itemnext;
	}

	public boolean remove(XDoubleLinked<T> item) {
		if (null == item) {
			return false;// null
		}
		boolean result = false;
		XDoubleLinked<T> itemprev = item.prev;
		if (null != item.next) {
			item.next.prev = itemprev;
			item.prev = null;
			result = true;
		}
		if (null != itemprev) {
			itemprev.next = item.next;
			item.next = null;
			result = true;
		} else {
			/*
			 * 不存在前一个元素 所以 这是栈底
			 */
			item.next = null;
		}
		return result;
	}


	/*
	 * 是否为栈底
	 */
	public boolean isFirst() {
		return null == this.prev;
	}
	public static <T extends Object> boolean isFirst(XDoubleLinked<T> element) {
		return null == element ? false : element.isFirst();
	}

	/*
	 * 是否为栈顶
	 */
	public boolean isLast() {
		return null == this.next;
	}
	public static <T extends Object> boolean isLast(XDoubleLinked<T> element) {
		return null == element ? false : element.isLast();
	}

	/**
	 *
	 * @param first new First
	 * @return new First
	 * @throws NullPointerException
	 * @throws RuntimeException
	 */
	public XDoubleLinked<T> addFirst(XDoubleLinked<T> first) throws NullPointerException, RuntimeException{
		if (null == first) {
			throw new NullPointerException("item for null");
		}
		if (!isFirst()) {
			throw new RuntimeException("only may be added from the bottom");
		}

		this.addNext(first);
		first.addNext(this);
		return first;
	}

	/*
	 * 不在栈里 独立item 孤儿
	 */
	public boolean isOrphan() {
		return null == this.prev && null == this.next;
	}
	public static <T extends Object> boolean isOrphan(XDoubleLinked<T> element) {
		return null == element ? false : element.isOrphan();
	}















	// 获取第一个元素
	public XDoubleLinked<T> findFirst() {
		return XDoubleLinked.findFirst(this);
	}
	// 获取第一个元素
	public static <T extends Object> XDoubleLinked<T> findFirst(XDoubleLinked<T> item) throws NullPointerException {
		if (null == item) {
			throw new NullPointerException("item for null");
		}
		XDoubleLinked<T> tmp = item;
		XDoubleLinked<T> bottom = null;
		while (null != (tmp = tmp.prev)) {
			bottom = tmp;
		}
		if (null == bottom) {
			return item;
		}
		return bottom;
	}


	// 获取最后一个元素
	public XDoubleLinked<T> findLast() {
		return XDoubleLinked.findLast(this);
	}

	// 获取最后一个元素
	public static <T extends Object> XDoubleLinked<T> findLast(XDoubleLinked<T> item) throws NullPointerException{
		if (null == item) {
			throw new NullPointerException("item for null");
		}
		XDoubleLinked<T> tmp = item;
		XDoubleLinked<T> next = null;
		while (null != (tmp = tmp.next)) {
			next = tmp;
		}
		if (null == next) {
			return item;
		}
		return next;
	}





	public static <T extends Object> String toStringFromLastStart(XDoubleLinked<T> item) {
		if (null == item) {
			return "{}";
		}
		XStringJoiner sj = new XStringJoiner(",", "{", "}");
		XDoubleLinked top = findLast(item);
		if (null != top) {
			do {
				sj.add(top.toString());
			} while (top.hasPrev() && null != (top = top.getPrev()));
		}
		return sj.toString();
	}

	public static <T extends Object> String toStringFromFirstStart(XDoubleLinked<T> item) {
		if (null == item) {
			return "{}";
		}
		XStringJoiner sj = new XStringJoiner(",", "{", "}");
		XDoubleLinked top = findFirst(item);
		if (null != top) {
			do {
				sj.add(top.toString());
			} while (top.hasNext() && null != (top = top.getNext()));
		}
		return sj.toString();
	}
}

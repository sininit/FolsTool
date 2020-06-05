package top.fols.box.util;

import java.io.Serializable;

public class XDoubleLinked<T extends XDoubleLinked> implements Serializable {
	private static final long serialVersionUID = 1L;

	
	public static class VarLinked<T extends Object> extends XDoubleLinked<VarLinked<T>> implements Serializable {
		private static final long serialVersionUID = 1L;

		private T content;

		public VarLinked(T content) {
			this.content = content;
		}

		public T content() {
			return this.content;
		}

		public VarLinked<T> setContent(T content) {
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
			if (obj instanceof VarLinked) {
				return null == this.content ? null == ((VarLinked<?>) obj).content
						: this.content.equals(((VarLinked<?>) obj).content);
			} else {
				return null == this.content ? null == obj : this.content.equals(obj);
			}
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return null == this.content ? null : this.content.toString();
		}

		public static boolean equals(Object obj1, Object obj2) {
			// TODO: Implement this method
			Object val1 = obj1 instanceof VarLinked ? (((VarLinked<?>) obj1).content) : obj1;
			Object val2 = obj2 instanceof VarLinked ? (((VarLinked<?>) obj2).content) : obj2;
			return XObjects.isEquals(val1, val2);
		}
	}






	protected T last = null, next = null;

	public XDoubleLinked() {
	}

	// 获取上一个item
	public T getLast() {
		return this.last;
	}

	// 获取下一个item
	public T getNext() {
		return this.next;
	}

	public boolean hasLast() {
		return null != this.last;
	}

	public boolean hasNext() {
		return null != this.next;
	}

	/*
	 * bottom >> elements >> top
	 */

	// 添加下一个item
	public void addNext(T item) {
		this.addNext(this, item);
	}

	public static <T extends XDoubleLinked> void addNext(XDoubleLinked<T> index, XDoubleLinked<T> addelement) {
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
		index.next = (T) addelement;
		/*
		 * 将index 的 下一个元素 的上一个元素引索设置为 addelement
		 */
		if (null != itemnext) {
			itemnext.last = (T) addelement;
		}

		if (null != addelement.last) {
			/*
			 * [a, addelement, index, ...] ==> [a, index, addelement, ...]
			 * 
			 * 将 原来addelement所在的位置的上一个元素 的下一个元素引索 设置为 addelement的下一个元素引索
			 */
			addelement.last.next = addelement.next;
		}
		if (null != addelement.next) {
			addelement.next.last = addelement.last;
		}

		/*
		 * 将addelement 的上一个元素引索设置为index
		 */
		addelement.last = (T) index;

		/*
		 * 将addelement的下个元素引索改为原index的下个元素引索
		 */
		addelement.next = (T) itemnext;
	}

	public boolean remove(T item) {
		if (null == item) {
			return false;// null
		}
		boolean result = false;
		T itemlast = (T) item.last;
		if (null != item.next) {
			item.next.last = itemlast;
			item.last = null;
			result = true;
		}
		if (null != itemlast) {
			itemlast.next = item.next;
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

	// 获取第一个元素
	public T getBottom() {
		return (T) XDoubleLinked.getBottom(this);
	}

	// 获取最后一个元素
	public T getTop() {
		return (T) XDoubleLinked.getTop(this);
	}

	// 获取第一个元素
	public static <T extends XDoubleLinked> T getBottom(T item) {
		if (null == item) {
			throw new NullPointerException("item for null");
		}
		T tmp = item;
		T bottom = null;
		while (null != (tmp = (T) tmp.last)) {
			bottom = tmp;
		}
		if (null == bottom) {
			return item;
		}
		return bottom;
	}

	// 获取最后一个元素
	public static <T extends XDoubleLinked> T getTop(T item) {
		if (null == item) {
			throw new NullPointerException("item for null");
		}
		T tmp = item;
		T next = null;
		while (null != (tmp = (T) tmp.next)) {
			next = tmp;
		}
		if (null == next) {
			return item;
		}
		return next;
	}

	/*
	 * 是否为栈底
	 */
	public boolean isBottom() {
		return null == this.last;
	}

	/*
	 * 是否为栈顶
	 */
	public boolean isTop() {
		return null == this.next;
	}

	/*
	 * 将元素设置成栈底, 如果现在是栈底的话
	 */
	public T changeBottom(T bottom) {
		if (null == bottom) {
			throw new NullPointerException("item for null");
		}
		if (!isBottom()) {
			throw new RuntimeException("only may be added from the bottom");
		}

		this.addNext(bottom);
		bottom.addNext(this);
		return bottom;
	}

	/*
	 * 不在栈里 独立item 孤儿
	 */
	public boolean isOrphan() {
		return null == this.last && null == this.next;
	}
















	public static String toStringFromTopStart(XDoubleLinked item) {
		if (null == item) {
			return "{}";
		}
		XStringJoiner sj = new XStringJoiner(",", "{", "}");
		XDoubleLinked top = getTop(item);
		if (null != top) {
			do {
				sj.add(top.toString());
			} while (top.hasLast() && null != (top = top.getLast()));
		}
		return sj.toString();
	}

	public static String toStringFromBottomStart(XDoubleLinked item) {
		if (null == item) {
			return "{}";
		}
		XStringJoiner sj = new XStringJoiner(",", "{", "}");
		XDoubleLinked top = getBottom(item);
		if (null != top) {
			do {
				sj.add(top.toString());
			} while (top.hasNext() && null != (top = top.getNext()));
		}
		return sj.toString();
	}
}

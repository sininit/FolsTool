package top.fols.atri.io.buffer;

import top.fols.atri.util.DoubleLinkedList;

public abstract class BufferFilter<A extends Object> {
	protected DoubleLinkedList<A> separators = new DoubleLinkedList<>();

	public abstract A array(int count);
	public abstract A[] array2(int count);
	public abstract int sizeof(A array);

	public void addSeparator(A separator) {
		DoubleLinkedList<A> separators = this.separators;
		DoubleLinkedList.Element<A> element = new DoubleLinkedList.Element<>(separator);
		if (separators.size() == 0) {
			separators.addLast(element);
		} else {
			DoubleLinkedList.Element<A> first = separators.getFirst();
			if (sizeof(separator) > sizeof(first.content())) {
				separators.addFirst(element);
			} else {
				for (DoubleLinkedList.Element<A> now = separators.getFirst(); null != now; now = (
						DoubleLinkedList.Element<A>) now.getNext()) {
					if (sizeof(now.content()) >= sizeof(separator) && (null == now.getNext() || sizeof(separator) >= sizeof(now.getNext().content()))) {
						now.addNext(element);
						break;
					}
				}
			}
		}
		this.separatorCache = null;
	}
	protected A[] separatorCache = null;
	public A[] getSeparators() {
		if (null == this.separatorCache) {
			DoubleLinkedList<A> list = this.separators;
			return list.toArray(array2(list.size()));
		}
		return this.separatorCache;
	}
	public void refreshSeparator() {
		this.separatorCache = null;
		this.separatorCache = this.getSeparators();
	}





	protected boolean accept(int last, int search, A split, boolean readEnd) {
		return true;
	}

	BufferOperate<A> buffer;
	int  last, search;
	A    seachSeparator;
	boolean readEnd;
	void chk() {
		if (null == buffer) { throw new RuntimeException("not found"); }
	}
	void finded(BufferOperate<A> buffer, int last, int search, A separator, boolean readEnd) {
		this.buffer = buffer;
		this.last = last;
		this.search = search;
		this.seachSeparator = separator;
		this.readEnd = readEnd;
	}
	public BufferOperate<A> buffer() { return this.buffer; }
	public int 		contentOffset() { return last; }
	public int 		contentLength() { return search - last; }
	public A 		contentSeparator() { return this.seachSeparator; }
	public boolean  contentReadEnd() { return this.readEnd; }
	public boolean  contentReadToSeparator() { return null != this.seachSeparator; }
	
	
	public A        result(boolean addSeparator) {
		BufferFilter.this.chk();
		
		boolean readToSeparator = this.contentReadToSeparator();
		int offset = this.contentOffset();
		int count = this.contentLength() + (readToSeparator && addSeparator ?sizeof(seachSeparator): 0);
		A array = array(count);
		buffer.arraycopy(offset, array, 0, count);
		return array;
	}
	
	
	
	
	
	
	
	public int getSeparatorMaxSize() { return 0 == this.separators.size() ?0: sizeof(this.separators.getFirst().content()); }
	public int getSeparatorMinSize() { return 0 == this.separators.size() ?0: sizeof(this.separators.getLast().content()); }
	public int getSeparatorCount() { return this.separators.size(); }

	public A seekSeparator(int index) {
		DoubleLinkedList<A> list = this.separators;
		int size = list.size();
		if (index >= size) { return null; }

		DoubleLinkedList.Element<A> element = list.getFirst();
		for (int i = 0; i < Math.min(size, index); i++) {
			element = (DoubleLinkedList.Element<A>) element.getNext();
		}
		return element.content();
	}


	protected DoubleLinkedList<A>.ListIterator iterator() {
		DoubleLinkedList<A> list = this.separators;
		DoubleLinkedList<A>.ListIterator listIterator = list.iterator();
		return listIterator;
	}

}

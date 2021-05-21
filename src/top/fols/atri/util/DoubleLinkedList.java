package top.fols.atri.util;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T extends Object> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    public static class Element<T extends Object> extends DoubleLinked<T> implements Serializable {
        private static final long serialVersionUID = 1L;


        private DoubleLinkedList<T> superList;


        public Element() {
            super(null);
        }
        public Element(T object) {
            super(object);
        }

        @Override
        public void addNext(DoubleLinked<T> addelement) throws NullPointerException {
            // TODO: Implement this method
            if (null == this.superList) {
                throw new NullPointerException("this object does not exist in any list");
            }
            this.superList.addNext(this, (Element<T>)addelement);
        }

        @Override
        public void addFirst(DoubleLinked<T> newFirst) throws NullPointerException, RuntimeException {
            // TODO: Implement this method
            if (null == this.superList) {
                throw new NullPointerException("this object does not exist in any list");
            }
            this.superList.addFirst((Element<T>)newFirst);
        }

        @Override
        public void remove() {
            // TODO: Implement this method
            if (null == this.superList) {
                throw new NullPointerException("this object does not exist in any list");
            }
            this.superList.remove(this);
        }



        public DoubleLinkedList<T> superList() {
            return this.superList;
        }
        public boolean superListNull() {
            return null == this.superList;
        }


    }


    /**
     * The number of times this list has been <i>structurally modified</i>.
     * Structural modifications are those that change the size of the
     * list, or otherwise perturb it in such a fashion that iterations in
     * progress may yield incorrect results.
     *
     * <p>This field is used by the iterator and list iterator implementation
     * returned by the {@code iterator} and {@code listIterator} methods.
     * If the value of this field changes unexpectedly, the iterator (or list
     * iterator) will throw a {@code ConcurrentModificationException} in
     * response to the {@code next}, {@code remove}, {@code previous},
     * {@code set} or {@code add} operations.  This provides
     * <i>fail-fast</i> behavior, rather than non-deterministic behavior in
     * the face of concurrent modification during iteration.
     *
     * <p><b>Use of this field by subclasses is optional.</b> If a subclass
     * wishes to provide fail-fast iterators (and list iterators), then it
     * merely has to increment this field in its {@code add(int, E)} and
     * {@code remove(int)} methods (and any other methods that it overrides
     * that result in structural modifications to the list).  A single call to
     * {@code add(int, E)} or {@code remove(int)} must add no more than
     * one to this field, or the iterators (and list iterators) will throw
     * bogus {@code ConcurrentModificationExceptions}.  If an implementation
     * does not wish to provide fail-fast iterators, this field may be
     * ignored.
     */
    protected transient int modCount = 0;



    private int size;
    private Element<T> first;
    private Element<T> last;
    public DoubleLinkedList() {
        super();
    }

    public Element<T> getFirst() {
        return this.first;
    }
    public Element<T> getLast() {
        return this.last;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return null == this.first;
    }

    public boolean contains(Element<T> element) {
        return null == element ?false: element.superList == this;
    }



    public boolean removeFirst() {
        return null != this.first && this.remove(this.first);
    }
    public boolean removeLast() {
        return null != this.last && this.remove(this.last);
    }


    public Element<T> indexOf(Object o) {
        if (null == o) {
            for (DoubleLinked<T> x = first; null != x; x = x.getNext()) {
                if (null == x.content()) {
                    return (Element<T>)x;
                }
            }
        } else {
            for (DoubleLinked<T> x = first; null != x; x = x.getNext()) {
                if (o.equals(x.content())) {
                    return (Element<T>)x;
                }
            }
        }
        return null;
    }
    public Element<T> lastIndexOf(Object o) {
        if (null == o) {
            for (DoubleLinked<T> x = last; null != x; x = x.getPrev()) {
                if (null == x.content()) {
                    return (Element<T>)x;
                }
            }
        } else {
            for (DoubleLinked<T> x = last; null != x; x = x.getPrev()) {
                if (o.equals(x.content())) {
                    return (Element<T>)x;
                }
            }
        }
        return null;
    }


    /** basic method */


    public boolean remove(Element<T> element) {
        if (null != element.superList) {
            if (element.superList != this) {
                return element.superList.remove(element);
            } else {
                if (element.superList.size > 0) element.superList.size--;
                element.superList = null;
            }
        }
        if (null == this.first) { // empty list
            return false;
        } else {
            if (this.first == element) {
                Element<T> next = (Element<T>) this.first.getNext();
                DoubleLinked._remove(element);
                if (this.last == element) { //只有一个元素 且元素和(第一个 和 最后一个)相同
                    this.first = this.last = null;
                } else {//有多个元素
                    this.first = next;
                }
            } else {
                Element<T> prev = (Element<T>) this.last.getPrev();
                DoubleLinked._remove(element);
                if (this.last == element) {
                    this.last = prev;
                }
            }
            this.modCount++;
            return true;
        }
    }


    public void addFirst(Element<T> element) {
        if (null != element.superList) {
            element.superList.remove(element);
        }
        if (null == this.first) {
            this.first = this.last = element;
        } else {
            DoubleLinked._addFirst(this.first, element);
            this.first = element;
        }
        this.size++;
        this.modCount++;
        element.superList = this;
    }
    public void addLast(Element<T> element) {
        if (null != element.superList) {
            element.superList.remove(element);
        }
        if (null == this.first) { // && null == this.last
            this.first = this.last = element;
        } else {
            DoubleLinked._addNext(this.last, element);
            this.last = element;
        }
        this.size++;
        this.modCount++;
        element.superList = this;
    }
    public void addNext(Element<T> index, Element<T> element) throws NoSuchElementException {
        if (index == element) {
            return;
        } else if (null == index) {
            this.addFirst(element);
        } else {
            if (!this.contains(index)) {
                throw new NoSuchElementException("not contains index: " + index);
            }
            if (this.last == index) {
                this.addLast(element);
            } else {
                if (null != element.superList) {
                    element.superList.remove(element);
                }
                DoubleLinked._addNext(index, element);
                this.size++;
                this.modCount++;
                element.superList = this;
            }

        }
    }
    public void clear() {
        Element<T> now = this.last;
        for (; null != now; now = (Element<T>) now.getPrev()) {
            now.remove();
        }
        this.first = this.last = null;
        this.size = 0;
        this.modCount++;
    }


    /** basic method */



    public void addAll(Element<T> index, DoubleLinkedList<T> element) {
        while (!element.isEmpty()) {
            Element<T> next = element.getFirst();
            this.addNext(index, next);
            index = next;
        }
    }







    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (DoubleLinked<T> x = first; null != x; x = x.getNext()) {
            result[i++] = x.content();
        }
        return result;
    }
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            a = (E[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (DoubleLinked<T> x = first; null != x; x = x.getNext()) {
            result[i++] = x.content();
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }











    public ListIterator iterator() {
        return this.iterator(this.first);
    }
    public ListIterator iterator(Element<T> from) {
        return new ListIterator(from);
    }
    public class ListIterator implements Iterator<T> {
        private int expectedModCount = modCount;
        final void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }


        private Element<T> lastReturned;
        private Element<T> linked;
        private ListIterator(Element<T> from) {
            this.linked = from;
        }

        @Override
        public boolean hasNext() {
            // TODO: Implement this method
            return null != this.linked;
        }

        @Override
        public T next() {
            // TODO: Implement this method
            this.checkForComodification();
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T result = this.linked.content();
            this.lastReturned = this.linked;
            Element<T> next = (Element<T>) this.linked.getNext();
            this.linked = next;
            return result;
        }

        public Element<T> now() {
            if (null == this.lastReturned) {
                throw new IllegalStateException();
            }
            return this.lastReturned;
        }

        @Override
        public void remove() {
            // TODO: Implement this method
            this.checkForComodification();
            if (null == this.lastReturned) {
                throw new IllegalStateException();
            }
            this.lastReturned.remove();
            this.lastReturned = null;
            this.expectedModCount++;
        }
    }





    @Override
    public String toString() {
        // TODO: Implement this method
        return DoubleLinked.toStringFromFirstStart(this.first);
    }

    @Override
    public int hashCode() {
        // TODO: Implement this method
        return null == this.first ?0: this.first.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // TODO: Implement this method
//		return obj instanceof DoubleLinkedList && ((DoubleLinkedList)obj).first == this.first;
        return obj == this;
    }

    @Override
    public DoubleLinkedList<T> clone() {
        // TODO: Implement this method
        DoubleLinkedList<T> newList = new DoubleLinkedList<>();
        ListIterator li = this.iterator(this.first);
        while (li.hasNext()) {
            newList.addLast(new Element<T>(li.next()));
        }
        return newList;
    }



}

package top.fols.atri.assist.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import top.fols.atri.lang.Objects;



public class HasherMap<K, V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable  {
	private static final long serialVersionUID = 362498820763181265L;
	

	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
	static final int MAXIMUM_CAPACITY = 1 << 30;
	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	static final int TREEIFY_THRESHOLD = 8;
	static final int UNTREEIFY_THRESHOLD = 6;
	static final int MIN_TREEIFY_CAPACITY = 64;

	
	
	transient Node<K,V>[] table;
	transient Set<Map.Entry<K,V>> entrySet;
	transient Set<K> keySet;
	transient Collection<V> values;
	transient int modCount;
	int threshold;
	transient int size;
	
	final Hasher hasher;
	final float loadFactor;
	
	
	public HasherMap(Hasher hasher) {
		this.hasher     = hasher;
		this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
	}
	
	public HasherMap(Hasher hasher, int initialCapacity) {
        this(hasher, initialCapacity, DEFAULT_LOAD_FACTOR);
    }
	
	public HasherMap(Hasher hasher, int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
		this.hasher     = hasher;
        this.loadFactor = loadFactor;
        this.threshold  = tableSizeFor(initialCapacity);
    }

	public HasherMap(Hasher hasher, Map<? extends K, ? extends V> m) {
        this.hasher     = hasher;
		this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }
	
	
	

	/**
     * Reset to initial default state.  Called by clone and readObject.
     */
    void reinitialize() {
        table = null;
        entrySet = null;
        keySet = null;
        values = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }

	@Override
	public Object clone() {
        HasherMap<K,V> result;
        try {
            result = (HasherMap<K,V>)super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }


	
	public Hasher getHasher() { return hasher; }

	public static class Node<K,V> implements Map.Entry<K, V> {
		final Hasher hasher;

		final int hash;
		final K key;
		V value;
		Node<K,V> next;

		Node(Hasher hasher, int hash, K key, V value, Node<K,V> next) {
			this.hasher = hasher;
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}

		public final K getKey()        { return key; }
		public final V getValue()      { return value; }
		public final String toString() { return key + "=" + value; }

		public final int hashCode() {
			return hasher.hash(key) ^ Objects.hashCode(value);
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		public final boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof Node) {
				Node e = (Node)o;
				Object k = e.getKey();
				if ((k == key || hasher.equals(key, k)) && value == e.getValue())
					return true;
			}
			return false;
		}
	}

	static class Entry<K, V> extends Node<K, V> {
		Entry<K,V> before, after;
		Entry(Hasher hasher, int hash, K key, V value, Node<K,V> next) {
			super(hasher, hash, key, value, next);
		}
	}
	static class TreeNode<K,V> extends Entry<K,V> {
		TreeNode<K,V> parent;  // red-black tree links
		TreeNode<K,V> left;
		TreeNode<K,V> right;
		TreeNode<K,V> prev;    // needed to unlink next upon deletion
		boolean red;
		TreeNode(Hasher hasher, int hash, K key, V val, Node<K,V> next) {
			super(hasher, hash, key, val, next);
		}

		final TreeNode<K, V> root() {
			for (TreeNode<K, V> r = this, p;;) {
				if ((p = r.parent) == null)
					return r;
				r = p;
			}
		}

		static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K, V> root) {
			int n;
			if (root != null && tab != null && (n = tab.length) > 0) {
				int index = (n - 1) & root.hash;
				TreeNode<K, V> first = (TreeNode<K, V>)tab[index];
				if (root != first) {
					Node<K,V> rn;
					tab[index] = root;
					TreeNode<K, V> rp = root.prev;
					if ((rn = root.next) != null)
						((TreeNode<K, V>)rn).prev = rp;
					if (rp != null)
						rp.next = rn;
					if (first != null)
						first.prev = root;
					root.next = first;
					root.prev = null;
				}
				assert checkInvariants(root);
			}
		}

		final TreeNode<K, V> find(int h, Object k, Class<?> kc) {
			TreeNode<K, V> p = this;
			do {
				int ph, dir; K pk;
				TreeNode<K, V> pl = p.left, pr = p.right, q;
				if ((ph = p.hash) > h)
					p = pl;
				else if (ph < h)
					p = pr;
				else if ((pk = p.key) == k || hasher.equals(k, pk))
					return p;
				else if (pl == null)
					p = pr;
				else if (pr == null)
					p = pl;
				else if ((kc != null ||
						 (kc = comparableClassFor(k)) != null) &&
						 (dir = compareComparables(kc, k, pk)) != 0)
					p = (dir < 0) ? pl : pr;
				else if ((q = pr.find(h, k, kc)) != null)
					return q;
				else
					p = pl;
			} while (p != null);
			return null;
		}

		final TreeNode<K, V> getTreeNode(int h, Object k) {
			return ((parent != null) ? root() : this).find(h, k, null);
		}

		static int tieBreakOrder(Object a, Object b) {
			int d;
			if (a == null || b == null ||
				(d = a.getClass().getName().
				compareTo(b.getClass().getName())) == 0)
				d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
					-1 : 1);
			return d;
		}

		final void treeify(Node<K,V>[] tab) {
			TreeNode<K, V> root = null;
			for (TreeNode<K, V> x = this, next; x != null; x = next) {
				next = (TreeNode<K, V>)x.next;
				x.left = x.right = null;
				if (root == null) {
					x.parent = null;
					x.red = false;
					root = x;
				} else {
					K k = x.key;
					int h = x.hash;
					Class<?> kc = null;
					for (TreeNode<K, V> p = root;;) {
						int dir, ph;
						K pk = p.key;
						if ((ph = p.hash) > h)
							dir = -1;
						else if (ph < h)
							dir = 1;
						else if ((kc == null &&
								 (kc = comparableClassFor(k)) == null) ||
								 (dir = compareComparables(kc, k, pk)) == 0)
							dir = tieBreakOrder(k, pk);

						TreeNode<K, V> xp = p;
						if ((p = (dir <= 0) ? p.left : p.right) == null) {
							x.parent = xp;
							if (dir <= 0)
								xp.left = x;
							else
								xp.right = x;
							root = balanceInsertion(root, x);
							break;
						}
					}
				}
			}
			moveRootToFront(tab, root);
		}

		final Node<K,V> untreeify(HasherMap<K,V> map) {
			Node<K,V> hd = null, tl = null;
			for (Node<K,V> q = this; q != null; q = q.next) {
				Node<K,V> p = map.replacementNode(q, null);
				if (tl == null)
					hd = p;
				else
					tl.next = p;
				tl = p;
			}
			return hd;
		}

		static Class<?> comparableClassFor(Object x) {
			if (x instanceof Comparable) {
				Class<?> c; Type[] ts, as; Type t; ParameterizedType p;
				if ((c = x.getClass()) == String.class) // bypass checks
					return c;
				if ((ts = c.getGenericInterfaces()) != null) {
					for (int i = 0; i < ts.length; ++i) {
						if (((t = ts[i]) instanceof ParameterizedType) &&
							((p = (ParameterizedType)t).getRawType() ==
							Comparable.class) &&
							(as = p.getActualTypeArguments()) != null &&
							as.length == 1 && as[0] == c) // type arg is c
							return c;
					}
				}
			}
			return null;
		}
		static int compareComparables(Class<?> kc, Object k, Object x) {
			return (x == null || x.getClass() != kc ? 0 :
				((Comparable)k).compareTo(x));
		}

		final TreeNode<K, V> putTreeVal(HasherMap<K,V> map, Node<K,V>[] tab,
										int h, K k, V v) {
			Class<?> kc = null;
			boolean searched = false;
			TreeNode<K, V> root = (parent != null) ? root() : this;
			for (TreeNode<K, V> p = root;;) {
				int dir, ph; K pk;
				if ((ph = p.hash) > h)
					dir = -1;
				else if (ph < h)
					dir = 1;
				else if ((pk = p.key) == k || hasher.equals(k, pk))
					return p;
				else if ((kc == null &&
						 (kc = comparableClassFor(k)) == null) ||
						 (dir = compareComparables(kc, k, pk)) == 0) {
					if (!searched) {
						TreeNode<K, V> q, ch;
						searched = true;
						if (((ch = p.left) != null &&
							(q = ch.find(h, k, kc)) != null) ||
							((ch = p.right) != null &&
							(q = ch.find(h, k, kc)) != null))
							return q;
					}
					dir = tieBreakOrder(k, pk);
				}

				TreeNode<K, V> xp = p;
				if ((p = (dir <= 0) ? p.left : p.right) == null) {
					Node<K,V> xpn = xp.next;
					TreeNode<K, V> x = map.newTreeNode(h, k, v, xpn);
					if (dir <= 0)
						xp.left = x;
					else
						xp.right = x;
					xp.next = x;
					x.parent = x.prev = xp;
					if (xpn != null)
						((TreeNode<K, V>)xpn).prev = x;
					moveRootToFront(tab, balanceInsertion(root, x));
					return null;
				}
			}
		}

		final void removeTreeNode(HasherMap<K,V> map, Node<K,V>[] tab,
								  boolean movable) {
			int n;
			if (tab == null || (n = tab.length) == 0)
				return;
			int index = (n - 1) & hash;
			TreeNode<K, V> first = (TreeNode<K, V>)tab[index], root = first, rl;
			TreeNode<K, V> succ = (TreeNode<K, V>)next, pred = prev;
			if (pred == null)
				tab[index] = first = succ;
			else
				pred.next = succ;
			if (succ != null)
				succ.prev = pred;
			if (first == null)
				return;
			if (root.parent != null)
				root = root.root();
			if (root == null || root.right == null ||
				(rl = root.left) == null || rl.left == null) {
				tab[index] = first.untreeify(map);  // too small
				return;
			}
			TreeNode<K, V> p = this, pl = left, pr = right, replacement;
			if (pl != null && pr != null) {
				TreeNode<K, V> s = pr, sl;
				while ((sl = s.left) != null) // find successor
					s = sl;
				boolean c = s.red; s.red = p.red; p.red = c; // swap colors
				TreeNode<K, V> sr = s.right;
				TreeNode<K, V> pp = p.parent;
				if (s == pr) { // p was s's direct parent
					p.parent = s;
					s.right = p;
				} else {
					TreeNode<K, V> sp = s.parent;
					if ((p.parent = sp) != null) {
						if (s == sp.left)
							sp.left = p;
						else
							sp.right = p;
					}
					if ((s.right = pr) != null)
						pr.parent = s;
				}
				p.left = null;
				if ((p.right = sr) != null)
					sr.parent = p;
				if ((s.left = pl) != null)
					pl.parent = s;
				if ((s.parent = pp) == null)
					root = s;
				else if (p == pp.left)
					pp.left = s;
				else
					pp.right = s;
				if (sr != null)
					replacement = sr;
				else
					replacement = p;
			} else if (pl != null)
				replacement = pl;
			else if (pr != null)
				replacement = pr;
			else
				replacement = p;
			if (replacement != p) {
				TreeNode<K, V> pp = replacement.parent = p.parent;
				if (pp == null)
					root = replacement;
				else if (p == pp.left)
					pp.left = replacement;
				else
					pp.right = replacement;
				p.left = p.right = p.parent = null;
			}

			TreeNode<K, V> r = p.red ? root : balanceDeletion(root, replacement);

			if (replacement == p) {  // detach
				TreeNode<K, V> pp = p.parent;
				p.parent = null;
				if (pp != null) {
					if (p == pp.left)
						pp.left = null;
					else if (p == pp.right)
						pp.right = null;
				}
			}
			if (movable)
				moveRootToFront(tab, r);
		}

		final void split(HasherMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
			TreeNode<K, V> b = this;
			// Relink into lo and hi lists, preserving order
			TreeNode<K, V> loHead = null, loTail = null;
			TreeNode<K, V> hiHead = null, hiTail = null;
			int lc = 0, hc = 0;
			for (TreeNode<K, V> e = b, next; e != null; e = next) {
				next = (TreeNode<K, V>)e.next;
				e.next = null;
				if ((e.hash & bit) == 0) {
					if ((e.prev = loTail) == null)
						loHead = e;
					else
						loTail.next = e;
					loTail = e;
					++lc;
				} else {
					if ((e.prev = hiTail) == null)
						hiHead = e;
					else
						hiTail.next = e;
					hiTail = e;
					++hc;
				}
			}

			if (loHead != null) {
				if (lc <= UNTREEIFY_THRESHOLD)
					tab[index] = loHead.untreeify(map);
				else {
					tab[index] = loHead;
					if (hiHead != null) // (else is already treeified)
						loHead.treeify(tab);
				}
			}
			if (hiHead != null) {
				if (hc <= UNTREEIFY_THRESHOLD)
					tab[index + bit] = hiHead.untreeify(map);
				else {
					tab[index + bit] = hiHead;
					if (loHead != null)
						hiHead.treeify(tab);
				}
			}
		}

		/* ------------------------------------------------------------ */
		// Red-black tree methods, all adapted from CLR

		static <K,V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root,
											   TreeNode<K, V> p) {
			TreeNode<K, V> r, pp, rl;
			if (p != null && (r = p.right) != null) {
				if ((rl = p.right = r.left) != null)
					rl.parent = p;
				if ((pp = r.parent = p.parent) == null)
					(root = r).red = false;
				else if (pp.left == p)
					pp.left = r;
				else
					pp.right = r;
				r.left = p;
				p.parent = r;
			}
			return root;
		}

		static <K,V> TreeNode<K, V> rotateRight(TreeNode<K, V> root,
												TreeNode<K, V> p) {
			TreeNode<K, V> l, pp, lr;
			if (p != null && (l = p.left) != null) {
				if ((lr = p.left = l.right) != null)
					lr.parent = p;
				if ((pp = l.parent = p.parent) == null)
					(root = l).red = false;
				else if (pp.right == p)
					pp.right = l;
				else
					pp.left = l;
				l.right = p;
				p.parent = l;
			}
			return root;
		}

		static <K,V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root,
													 TreeNode<K, V> x) {
			x.red = true;
			for (TreeNode<K, V> xp, xpp, xppl, xppr;;) {
				if ((xp = x.parent) == null) {
					x.red = false;
					return x;
				} else if (!xp.red || (xpp = xp.parent) == null)
					return root;
				if (xp == (xppl = xpp.left)) {
					if ((xppr = xpp.right) != null && xppr.red) {
						xppr.red = false;
						xp.red = false;
						xpp.red = true;
						x = xpp;
					} else {
						if (x == xp.right) {
							root = rotateLeft(root, x = xp);
							xpp = (xp = x.parent) == null ? null : xp.parent;
						}
						if (xp != null) {
							xp.red = false;
							if (xpp != null) {
								xpp.red = true;
								root = rotateRight(root, xpp);
							}
						}
					}
				} else {
					if (xppl != null && xppl.red) {
						xppl.red = false;
						xp.red = false;
						xpp.red = true;
						x = xpp;
					} else {
						if (x == xp.left) {
							root = rotateRight(root, x = xp);
							xpp = (xp = x.parent) == null ? null : xp.parent;
						}
						if (xp != null) {
							xp.red = false;
							if (xpp != null) {
								xpp.red = true;
								root = rotateLeft(root, xpp);
							}
						}
					}
				}
			}
		}

		static <K,V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root,
													TreeNode<K, V> x) {
			for (TreeNode<K, V> xp, xpl, xpr;;)  {
				if (x == null || x == root)
					return root;
				else if ((xp = x.parent) == null) {
					x.red = false;
					return x;
				} else if (x.red) {
					x.red = false;
					return root;
				} else if ((xpl = xp.left) == x) {
					if ((xpr = xp.right) != null && xpr.red) {
						xpr.red = false;
						xp.red = true;
						root = rotateLeft(root, xp);
						xpr = (xp = x.parent) == null ? null : xp.right;
					}
					if (xpr == null)
						x = xp;
					else {
						TreeNode<K, V> sl = xpr.left, sr = xpr.right;
						if ((sr == null || !sr.red) &&
							(sl == null || !sl.red)) {
							xpr.red = true;
							x = xp;
						} else {
							if (sr == null || !sr.red) {
								if (sl != null)
									sl.red = false;
								xpr.red = true;
								root = rotateRight(root, xpr);
								xpr = (xp = x.parent) == null ?
									null : xp.right;
							}
							if (xpr != null) {
								xpr.red = (xp == null) ? false : xp.red;
								if ((sr = xpr.right) != null)
									sr.red = false;
							}
							if (xp != null) {
								xp.red = false;
								root = rotateLeft(root, xp);
							}
							x = root;
						}
					}
				} else { // symmetric
					if (xpl != null && xpl.red) {
						xpl.red = false;
						xp.red = true;
						root = rotateRight(root, xp);
						xpl = (xp = x.parent) == null ? null : xp.left;
					}
					if (xpl == null)
						x = xp;
					else {
						TreeNode<K, V> sl = xpl.left, sr = xpl.right;
						if ((sl == null || !sl.red) &&
							(sr == null || !sr.red)) {
							xpl.red = true;
							x = xp;
						} else {
							if (sl == null || !sl.red) {
								if (sr != null)
									sr.red = false;
								xpl.red = true;
								root = rotateLeft(root, xpl);
								xpl = (xp = x.parent) == null ?
									null : xp.left;
							}
							if (xpl != null) {
								xpl.red = (xp == null) ? false : xp.red;
								if ((sl = xpl.left) != null)
									sl.red = false;
							}
							if (xp != null) {
								xp.red = false;
								root = rotateRight(root, xp);
							}
							x = root;
						}
					}
				}
			}
		}

		static <K,V> boolean checkInvariants(TreeNode<K, V> t) {
			TreeNode<K, V> tp = t.parent, tl = t.left, tr = t.right,
				tb = t.prev, tn = (TreeNode<K, V>)t.next;
			if (tb != null && tb.next != t)
				return false;
			if (tn != null && tn.prev != t)
				return false;
			if (tp != null && t != tp.left && t != tp.right)
				return false;
			if (tl != null && (tl.parent != t || tl.hash > t.hash))
				return false;
			if (tr != null && (tr.parent != t || tr.hash < t.hash))
				return false;
			if (t.red && tl != null && tl.red && tr != null && tr.red)
				return false;
			if (tl != null && !checkInvariants(tl))
				return false;
			if (tr != null && !checkInvariants(tr))
				return false;
			return true;
		}
	}

	static int tableSizeFor(int cap) {
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}


	public Node<K,V> getNode(Object key) {
		return getNode(hash(key), key);
	}
	final Node<K,V> getNode(int hash, Object key) {
		Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
		if ((tab = table) != null && (n = tab.length) > 0 &&
			(first = tab[(n - 1) & hash]) != null) {
			if (first.hash == hash && // always check first node
				((k = first.key) == key || hasher.equals(key, k)))
				return first;
			if ((e = first.next) != null) {
				if (first instanceof TreeNode)
					return ((TreeNode<K, V>)first).getTreeNode(hash, key);
				do {
					if (e.hash == hash &&
						((k = e.key) == key || hasher.equals(key, k)))
						return e;
				} while ((e = e.next) != null);
			}
		}
		return null;
	}

	
	int hash(Object key) {
		int h;
		return (h = hasher.hash(key)) ^ (h >>> 16);
	}

	
	@Override
	public Set<K> keySet() {
		Set<K> ks = keySet;
		if (ks == null) {
			ks = new HasherMap<K,V>.KeySet();
			keySet = ks;
		}
		return ks;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean containsKey(Object key) {
		return getNode(hash(key), key) != null;
	}
	@Override
	public boolean containsValue(Object value) {
		Node<K,V>[] tab; V v;
		if ((tab = table) != null && size > 0) {
			for (int i = 0; i < tab.length; ++i) {
				for (Node<K,V> e = tab[i]; e != null; e = e.next) {
					if ((v = e.value) == value)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public V get(Object key) {
		Node<K,V> e;
		return (e = getNode(hash(key), key)) == null ? null : e.value;
	}

	@Override
	public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
	final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || hasher.equals(key, k)))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || hasher.equals(key, k)))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
	
	
	

	final Node<K,V>[] resize() {
		Node<K,V>[] oldTab = table;
		int oldCap = (oldTab == null) ? 0 : oldTab.length;
		int oldThr = threshold;
		int newCap, newThr = 0;
		if (oldCap > 0) {
			if (oldCap >= MAXIMUM_CAPACITY) {
				threshold = Integer.MAX_VALUE;
				return oldTab;
			} else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
					   oldCap >= DEFAULT_INITIAL_CAPACITY)
				newThr = oldThr << 1; // double threshold
		} else if (oldThr > 0) // initial capacity was placed in threshold
			newCap = oldThr;
		else {               // zero initial threshold signifies using defaults
			newCap = DEFAULT_INITIAL_CAPACITY;
			newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
		}
		if (newThr == 0) {
			float ft = (float)newCap * loadFactor;
			newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
				(int)ft : Integer.MAX_VALUE);
		}
		threshold = newThr;
		@SuppressWarnings({"unchecked"})
			Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
		table = newTab;
		if (oldTab != null) {
			for (int j = 0; j < oldCap; ++j) {
				Node<K,V> e;
				if ((e = oldTab[j]) != null) {
					oldTab[j] = null;
					if (e.next == null)
						newTab[e.hash & (newCap - 1)] = e;
					else if (e instanceof TreeNode)
						((TreeNode<K, V>)e).split(this, newTab, j, oldCap);
					else { // preserve order
						Node<K,V> loHead = null, loTail = null;
						Node<K,V> hiHead = null, hiTail = null;
						Node<K,V> next;
						do {
							next = e.next;
							if ((e.hash & oldCap) == 0) {
								if (loTail == null)
									loHead = e;
								else
									loTail.next = e;
								loTail = e;
							} else {
								if (hiTail == null)
									hiHead = e;
								else
									hiTail.next = e;
								hiTail = e;
							}
						} while ((e = next) != null);
						if (loTail != null) {
							loTail.next = null;
							newTab[j] = loHead;
						}
						if (hiTail != null) {
							hiTail.next = null;
							newTab[j + oldCap] = hiHead;
						}
					}
				}
			}
		}
		return newTab;
	}
	final void treeifyBin(Node<K,V>[] tab, int hash) {
		int n, index; Node<K,V> e;
		if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
			resize();
		else if ((e = tab[index = (n - 1) & hash]) != null) {
			TreeNode<K, V> hd = null, tl = null;
			do {
				TreeNode<K, V> p = replacementTreeNode(e, null);
				if (tl == null)
					hd = p;
				else {
					p.prev = tl;
					tl.next = p;
				}
				tl = p;
			} while ((e = e.next) != null);
			if ((tab[index] = hd) != null)
				hd.treeify(tab);
		}
	}

	@Override
	public V remove(Object key) {
		Node<K,V> e;
		return (e = removeNode(hash(key), key, null, false, true)) == null ? null : e.value;
	}
	final Node<K,V> removeNode(int hash, Object key, Object value,
							   boolean matchValue, boolean movable) {
		Node<K,V>[] tab; Node<K,V> p; int n, index;
		if ((tab = table) != null && (n = tab.length) > 0 &&
			(p = tab[index = (n - 1) & hash]) != null) {
			Node<K,V> node = null, e; K k; V v;
			if (p.hash == hash &&
				((k = p.key) == key || hasher.equals(key, k)))
				node = p;
			else if ((e = p.next) != null) {
				if (p instanceof TreeNode)
					node = ((TreeNode<K, V>)p).getTreeNode(hash, key);
				else {
					do {
						if (e.hash == hash &&
							((k = e.key) == key ||
							hasher.equals(key, k))) {
							node = e;
							break;
						}
						p = e;
					} while ((e = e.next) != null);
				}
			}
			if (node != null && (!matchValue || (v = node.value) == value)) {
				if (node instanceof TreeNode)
					((TreeNode<K, V>)node).removeTreeNode(this, tab, movable);
				else if (node == p)
					tab[index] = node.next;
				else
					p.next = node.next;
				++modCount;
				--size;
				afterNodeRemoval(node);
				return node;
			}
		}
		return null;
	}

	@Override
	public void clear() {
		Node<K,V>[] tab;
		modCount++;
		if ((tab = table) != null && size > 0) {
			size = 0;
			for (int i = 0; i < tab.length; ++i)
				tab[i] = null;
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO: Implement this method
		return size() == 0;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO: Implement this method
		putMapEntries(m, true);
	}
	
	@Override
	public Collection<V> values() {
		// TODO: Implement this method
		Collection<V> vs = values;
		if (vs == null) {
			vs = new Values();
			values = vs;
		}
		return vs;
	}




	void afterNodeAccess(Node<K,V> p) { }
	void afterNodeInsertion(boolean evict) { }
	void afterNodeRemoval(Node<K,V> p) { }

	Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
		return new Node<K,V>(hasher, hash, key, value, next);
	}

	TreeNode<K, V> newTreeNode(int hash, K key, V value, Node<K,V> next) {
		return new TreeNode<K,V>(hasher, hash, key, value, next);
	}
	Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
		return new Node<K,V>(hasher, p.hash, p.key, p.value, next);
	}
	TreeNode<K, V> replacementTreeNode(Node<K,V> p, Node<K,V> next) {
		return new TreeNode<K,V>(hasher, p.hash, p.key, p.value, next);
	}


	final class KeySet extends AbstractSet<K> {
		public final int size()                 { return size; }
		public final void clear()               { HasherMap.this.clear(); }
		public final Iterator<K> iterator()     { return new KeyIterator(); }
		public final boolean contains(Object o) { return containsKey(o); }
		public final boolean remove(Object key) {
			return removeNode(hash(key), key, null, false, true) != null;
		}
	}

	abstract class HashIterator {
		Node<K,V> next;        // next entry to return
		Node<K,V> current;     // current entry
		int expectedModCount;  // for fast-fail
		int index;             // current slot

		HashIterator() {
			expectedModCount = modCount;
			Node<K,V>[] t = table;
			current = next = null;
			index = 0;
			if (t != null && size > 0) { // advance to first entry
				do {} while (index < t.length && (next = t[index++]) == null);
			}
		}

		public final boolean hasNext() {
			return next != null;
		}

		final Node<K,V> nextNode() {
			Node<K,V>[] t;
			Node<K,V> e = next;
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			if (e == null)
				throw new NoSuchElementException();
			if ((next = (current = e).next) == null && (t = table) != null) {
				do {} while (index < t.length && (next = t[index++]) == null);
			}
			return e;
		}

		public final void remove() {
			Node<K,V> p = current;
			if (p == null)
				throw new IllegalStateException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			current = null;
			K key = p.key;
			removeNode(hash(key), key, null, false, false);
			expectedModCount = modCount;
		}
	}


	final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
		int s = m.size();
		if (s > 0) {
			if (table == null) { // pre-size
				float ft = ((float)s / loadFactor) + 1.0F;
				int t = ((ft < (float)MAXIMUM_CAPACITY) ?
					(int)ft : MAXIMUM_CAPACITY);
				if (t > threshold)
					threshold = tableSizeFor(t);
			} else if (s > threshold)
				resize();
			for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
				K key = e.getKey();
				V value = e.getValue();
				putVal(hash(key), key, value, false, evict);
			}
		}
	}

	@Override
	public Set<Map.Entry<K,V>> entrySet() {
		Set<Map.Entry<K,V>> es;
		return (es = entrySet) == null ? (entrySet = new HasherMap<K,V>.EntrySet()) : es;
	}

	final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
		public final int size()                 { return size; }
		public final void clear()               { HasherMap.this.clear(); }
		public final Iterator<Map.Entry<K,V>> iterator() {
			return new HasherMap<K,V>.EntryIterator();
		}
		public final boolean contains(Object o) {
			if (!(o instanceof Entry))
				return false;
			Entry e = (Entry) o;
			Object key = e.getKey();
			Node<K,V> candidate = getNode(hash(key), key);
			return candidate != null && candidate.equals(e);
		}
		public final boolean remove(Object o) {
			if (o instanceof Entry) {
				Entry e = (Entry) o;
				Object key = e.getKey();
				Object value = e.getValue();
				return removeNode(hash(key), key, value, true, true) != null;
			}
			return false;
		}
	}

	final class KeyIterator extends HasherMap<K,V>.HashIterator	implements Iterator<K> {
		public final K next() { return nextNode().key; }
	}

	final class ValueIterator extends HashIterator implements Iterator<V> {
        public final V next() { return nextNode().value; }
    }
	final class EntryIterator extends HasherMap<K,V>.HashIterator implements Iterator<Map.Entry<K,V>> {
		public final Map.Entry<K,V> next() { return nextNode(); }
	}

	@SuppressWarnings("unchecked")
    final <T> T[] prepareArray(T[] a) {
        int size = this.size;
        if (a.length < size) {
            return (T[]) java.lang.reflect.Array
				.newInstance(a.getClass().getComponentType(), size);
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
	<T> T[] keysToArray(T[] a) {
        Object[] r = a;
        Node<K,V>[] tab;
        int idx = 0;
        if (size > 0 && (tab = table) != null) {
            for (Node<K,V> e : tab) {
                for (; e != null; e = e.next) {
                    r[idx++] = e.key;
                }
            }
        }
        return a;
    }
	<T> T[] valuesToArray(T[] a) {
        Object[] r = a;
        Node<K,V>[] tab;
        int idx = 0;
        if (size > 0 && (tab = table) != null) {
            for (Node<K,V> e : tab) {
                for (; e != null; e = e.next) {
                    r[idx++] = e.value;
                }
            }
        }
        return a;
    }


	final class Values extends AbstractCollection<V> {
        public final int size()                 { return size; }
        public final void clear()               { HasherMap.this.clear(); }
        public final Iterator<V> iterator()     { return new ValueIterator(); }
        public final boolean contains(Object o) { return containsValue(o); }

        public Object[] toArray() {
            return valuesToArray(new Object[size]);
        }

        public <T> T[] toArray(T[] a) {
            return valuesToArray(prepareArray(a));
        }
    }

	
	
	void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        Node<K,V>[] tab;
        if (size > 0 && (tab = table) != null) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                    s.writeObject(e.key);
                    s.writeObject(e.value);
                }
            }
        }
    }
	final int capacity() {
        return (table != null) ? table.length :
            (threshold > 0) ? threshold :
            DEFAULT_INITIAL_CAPACITY;
    }
	private void writeObject(java.io.ObjectOutputStream s)
	throws IOException {
        int buckets = capacity();
        // Write out the threshold, loadfactor, and any hidden stuff
        s.defaultWriteObject();
        s.writeInt(buckets);
        s.writeInt(size);
        internalWriteEntries(s);
    }
	private void readObject(java.io.ObjectInputStream s)
	throws IOException, ClassNotFoundException {
        // Read in the threshold (ignored), loadfactor, and any hidden stuff
        s.defaultReadObject();
        reinitialize();
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new InvalidObjectException("Illegal load factor: " +
                                             loadFactor);
        s.readInt();                // Read and ignore number of buckets
        int mappings = s.readInt(); // Read number of mappings (size)
        if (mappings < 0)
            throw new InvalidObjectException("Illegal mappings count: " +
                                             mappings);
        else if (mappings > 0) { // (if zero, use defaults)
            // Size the table using given load factor only if within
            // range of 0.25...4.0
            float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
            float fc = (float)mappings / lf + 1.0f;
            int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
				DEFAULT_INITIAL_CAPACITY :
				(fc >= MAXIMUM_CAPACITY) ?
				MAXIMUM_CAPACITY :
				tableSizeFor((int)fc));
            float ft = (float)cap * lf;
            threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
				(int)ft : Integer.MAX_VALUE);

            // Check Map.Entry[].class since it's the nearest public type to
            // what we're actually creating.
            // SharedSecrets.getJavaOISAccess().checkArray(s, Map.Entry[].class, cap);
            @SuppressWarnings({"rawtypes","unchecked"})
				Node<K,V>[] tab = (Node<K,V>[])new Node[cap];
            table = tab;

            // Read the keys and values, and put the mappings in the HashMap
            for (int i = 0; i < mappings; i++) {
                @SuppressWarnings("unchecked")
                    K key = (K) s.readObject();
                @SuppressWarnings("unchecked")
                    V value = (V) s.readObject();
                putVal(hash(key), key, value, false, false);
            }
        }
    }
}

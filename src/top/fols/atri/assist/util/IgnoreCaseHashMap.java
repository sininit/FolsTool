package top.fols.atri.assist.util;

import java.util.Map;

public class IgnoreCaseHashMap<K, V> extends HasherMap<K, V> {
 
  public IgnoreCaseHashMap() { 
    super(Hasher.IGNORE_CASE_HASHER);
  }
  public IgnoreCaseHashMap(int initialCapacity) {
	super(Hasher.IGNORE_CASE_HASHER, initialCapacity);
  }
  public IgnoreCaseHashMap(int initialCapacity, float loadFactor) {
	super(Hasher.IGNORE_CASE_HASHER, initialCapacity, loadFactor);
  }
  public IgnoreCaseHashMap(Map<? extends K, ? extends V> m) {
	super(Hasher.IGNORE_CASE_HASHER, m);
  }

}

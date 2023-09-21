package top.fols.atri.assist.util;

import java.util.Map;

public class IgnoreCaseLinkedHashMap <K, V> extends LinkedHasherMap<K, V> {

	public IgnoreCaseLinkedHashMap() { 
		super(Hasher.IGNORE_CASE_HASHER);
	}
	public IgnoreCaseLinkedHashMap(int initialCapacity) {
		super(Hasher.IGNORE_CASE_HASHER, initialCapacity);
	}
	public IgnoreCaseLinkedHashMap(int initialCapacity, float loadFactor) {
		super(Hasher.IGNORE_CASE_HASHER, initialCapacity, loadFactor);
	}
	public IgnoreCaseLinkedHashMap(Map<? extends K, ? extends V> m) {
		super(Hasher.IGNORE_CASE_HASHER, m);
	}
	
}

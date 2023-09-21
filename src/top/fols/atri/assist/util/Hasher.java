package top.fols.atri.assist.util;

import java.io.Serializable;

public abstract class Hasher implements Serializable {
	private static final long serialVersionUID = 362498820763181265L;

	public abstract int     hash(Object key);
	public abstract boolean equals(Object key, Object nodeKey);


	/**
	 * java.lang.String
	 */
	public static final Hasher IGNORE_CASE_HASHER = new Hasher() {
		int upperHash(String v) {
			int h = 0;
			int length = v.length();
			for (int i = 0; i < length; i++) {
				h = 31 * h + Character.toUpperCase(v.charAt(i));
			}
			return h;
		}

		@Override
		public int hash(Object key) {
			// TODO: Implement this method
			return (null == key) ? 0 : upperHash(key.toString());
		}

		@Override
		public boolean equals(Object key, Object nodeKey) {
			// TODO: Implement this method
			return (null == key || null == nodeKey) ? nodeKey == key : key.toString().equalsIgnoreCase(nodeKey.toString());
		}
	};
}
	

package top.fols.box.lang;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import top.fols.box.io.base.XCharArrayWriter;
import top.fols.box.lang.reflect.optdeclared.XReflectAccessible;

public class XStringFormat {

	public static interface VarManager {
		public abstract Object getVarValue(String name) throws RuntimeException;
	}

	private static Object getVarValue(Object[] arrs, String name) throws RuntimeException {
		try {
			return arrs[Integer.parseInt(name)];
		} catch (Throwable e) {
			throw new RuntimeException("not found variable: " + name + ", index=" + name + ", length=" + arrs.length);
		}
	}

	private static Object getVarValue(Map map, String name) throws RuntimeException {
		if (!map.containsKey(name)) {
			throw new RuntimeException("not found variable: " + name + ", map: " + map);
		} else {
			return map.get(name);
		}
	}

	public static VarManager wrapArray(final Object... arrs) {
		VarManager vm = new VarManager() {
			@Override
			public Object getVarValue(String name) throws RuntimeException {
				return XStringFormat.getVarValue(arrs, name);
			}
		};
		return vm;
	}

	public static VarManager wrapMap(final Map map) {
		VarManager vm = new VarManager() {
			@Override
			public Object getVarValue(String name) throws RuntimeException {
				return XStringFormat.getVarValue(map, name);
			}
		};
		return vm;
	}

	public static VarManager wrapObjectField(final Object object) {
		VarManager vm = new VarManager() {
			private final Class thisClass = null == object ? null : object.getClass();
			private Map<String, Field> fields;

			private Field getField(String name) {
				if (null == this.fields) {
					this.fields = new HashMap<>();
					for (Field field : thisClass.getDeclaredFields()) {
						this.fields.put(field.getName(), XReflectAccessible.setAccessibleTrueOne(field));
					}
				}
				return this.fields.get(name);
			}

			@Override
			public Object getVarValue(String name) throws RuntimeException {
				try {
					Field field = this.getField(name);
					return field.get(object);
				} catch (Throwable e) {
					throw new RuntimeException(
							"not found variable: " + name + ", field: " + (thisClass.getName() + "." + name));
				}
			}
		};
		return vm;
	}

	public static VarManager wrapObjectStaticField(final Class thisClass) {
		VarManager vm = new VarManager() {
			private Map<String, Field> fields;

			private Field getField(String name) {
				if (null == this.fields) {
					this.fields = new HashMap<>();
					for (Field field : thisClass.getDeclaredFields()) {
						this.fields.put(field.getName(), XReflectAccessible.setAccessibleTrueOne(field));
					}
				}
				return this.fields.get(name);
			}

			@Override
			public Object getVarValue(String name) throws RuntimeException {
				try {
					Field field = this.getField(name);
					return field.get(null);
				} catch (Throwable e) {
					throw new RuntimeException(
							"not found variable: " + name + ", field: " + (thisClass.getName() + "." + name));
				}
			}
		};
		return vm;
	}

	/**
	 * {local_var_name} example: ("Hello, {0}!", "admin") --> Hello, admin! any char
	 * of the variable name must conform to XFEKeyWords.isStandardVariableNameChar
	 */
	public static String strf(String text, VarManager varm) {
		if (null == text) {
			return null;
		}
		XCharArrayWriter writer = new XCharArrayWriter();
		char ANNOTATION_START = CharCheck.CharAnnotation.STRF.ANNOTATION_START;
		char ANNOTATION_END = CharCheck.CharAnnotation.STRF.ANNOTATION_END;
		int strlen = text.length();
		int st_index = -1;
		boolean st = false;
		for (int i = 0; i < strlen; i++) {
			char ch = text.charAt(i);
			if (st) {
				if (ch == ANNOTATION_START) {
					writer.write(text, st_index, i - st_index);
					st_index = i;
					st = true;
				} else if (ch == ANNOTATION_END) {
					int len = i - (st_index + 1);
					if (len > 0) {
						String name = text.substring(st_index + 1, i);
						// writer.append("|" + name + "|");
						writer.append(Util.ftoString(varm.getVarValue(name)));
						st_index = i + 1;
					}
				} else {
					if (!CharCheck.isStandardVariableNameChar(ch)) {
						writer.write(text, st_index, i - st_index + 1);
						st_index = -1;
						st = false;
					}
				}
			} else {
				if (ch == ANNOTATION_START) {
					st_index = i;
					st = true;
				} else {
					writer.append(ch);
				}
			}
		}
		if (st) {
			writer.write(text, st_index, text.length() - st_index);
		}
		return writer.toString();
	}


	

	/* ---- fast ---- */
	public static String strf(String text, Map varm) {
		if (null == text) {
			return null;
		}
		XCharArrayWriter writer = new XCharArrayWriter();
		char ANNOTATION_START = CharCheck.CharAnnotation.STRF.ANNOTATION_START;
		char ANNOTATION_END = CharCheck.CharAnnotation.STRF.ANNOTATION_END;
		int strlen = text.length();
		int st_index = -1;
		boolean st = false;
		for (int i = 0; i < strlen; i++) {
			char ch = text.charAt(i);
			if (st) {
				if (ch == ANNOTATION_START) {
					writer.write(text, st_index, i - st_index);
					st_index = i;
					st = true;
				} else if (ch == ANNOTATION_END) {
					int len = i - (st_index + 1);
					if (len > 0) {
						String name = text.substring(st_index + 1, i);
						// writer.append("|" + name + "|");
						writer.append(Util.ftoString(XStringFormat.getVarValue(varm, name)));
						st_index = i + 1;
					}
				} else {
					if (!CharCheck.isStandardVariableNameChar(ch)) {
						writer.write(text, st_index, i - st_index + 1);
						st_index = -1;
						st = false;
					}
				}
			} else {
				if (ch == ANNOTATION_START) {
					st_index = i;
					st = true;
				} else {
					writer.append(ch);
				}
			}
		}
		if (st) {
			writer.write(text, st_index, text.length() - st_index);
		}
		return writer.toString();
	}
	public static String strf(String text, Object... varm) {
		if (null == text) {
			return null;
		}
		XCharArrayWriter writer = new XCharArrayWriter();
		char ANNOTATION_START = CharCheck.CharAnnotation.STRF.ANNOTATION_START;
		char ANNOTATION_END = CharCheck.CharAnnotation.STRF.ANNOTATION_END;
		int strlen = text.length();
		int st_index = -1;
		boolean st = false;
		for (int i = 0; i < strlen; i++) {
			char ch = text.charAt(i);
			if (st) {
				if (ch == ANNOTATION_START) {
					writer.write(text, st_index, i - st_index);
					st_index = i;
					st = true;
				} else if (ch == ANNOTATION_END) {
					int len = i - (st_index + 1);
					if (len > 0) {
						String name = text.substring(st_index + 1, i);
						// writer.append("|" + name + "|");
						writer.append(Util.ftoString(XStringFormat.getVarValue(varm, name)));
						st_index = i + 1;
					}
				} else {
					if (!CharCheck.isStandardVariableNameChar(ch)) {
						writer.write(text, st_index, i - st_index + 1);
						st_index = -1;
						st = false;
					}
				}
			} else {
				if (ch == ANNOTATION_START) {
					st_index = i;
					st = true;
				} else {
					writer.append(ch);
				}
			}
		}
		if (st) {
			writer.write(text, st_index, text.length() - st_index);
		}
		return writer.toString();
	}
	/* ---- ---- */

	private static class CharCheck {
		public static boolean isStandardVariableNameChar(char ch) {
			return Character.isDigit(ch) || Character.isLowerCase(ch) || Character.isUpperCase(ch) || ch == '_';
		}

		public static class CharAnnotation {
			public static class STRF {
				public static final char ANNOTATION_START = '{';
				public static final char ANNOTATION_END = '}';
			}
		}
	}

	private static class Util {
		public static String ftoString(Object value) {
			if (null == value) {
				return "null";
			}
			return value.toString();
		}
	}

}

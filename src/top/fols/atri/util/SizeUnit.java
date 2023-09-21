package top.fols.atri.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import top.fols.atri.io.CharsReaders;
import top.fols.atri.io.Delimiter;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.interfaces.annotations.Nullable;

@SuppressWarnings("SameParameterValue")
public class SizeUnit {
	public static class NotFoundUnitException extends RuntimeException {
		public NotFoundUnitException(String message) {
			super(message);
		}
	}
	public static class EmptyUnitException extends RuntimeException {
		public EmptyUnitException(String message) {
			super(message);
		}
	}
	public static class ParseException extends RuntimeException {
		public ParseException(String message) {
			super(message);
		}
	}
	public static final class Unit {
		final String     name;
		final String     standardUnit;
		final BigDecimal value;

		public Unit(@NotNull String name, 
					@Nullable String standardUnit, BigDecimal standardValue) {
			this.name = name;
			this.standardUnit  = standardUnit;
			this.value = standardValue;
		}


		public boolean isAlias() {return null != standardUnit;}
		public boolean isStandardUnit() { return null == standardUnit; }


		@Override
		public String toString() {
			// TODO: Implement this method
			return "{" + (name + "=" + value) + "}";
		}
	}


	String defaultUnit;

	Map<String, Unit> 						unitValue = new HashMap<>();
	Unit[]   								unitValueSortLeftMinRightMaxCache;
	char[][] 								unitNamesCharsArraysSortCache;
	Delimiter.ICharsDelimiter unitNamesCharsArraysDelimiter;

	char[][] unitNamesSort() {
		List<char[]> list = new ArrayList<>();
		for (String k:    unitValue.keySet()) {
			char[] nameChars = k.toCharArray();
			list.add(nameChars);
		}
		char[][] data  = list.toArray(new char[][]{});
		Delimiter.sortSeparatorsLeftMaxRightMin(data);
		return   data;
	}
	Unit[] unitValueSort() {
		List<Unit> list = new ArrayList<>();
		for (String k:  unitValue.keySet()) {
			Unit unit = unitValue.get(k);
			list.add(unit);
		}
		Collections.sort(list, new Comparator<Unit>() {
				@Override
				public int compare(SizeUnit.Unit p1, SizeUnit.Unit p2) {
					// TODO: Implement this method
					return p1.value.compareTo(p2.value);
				}
			});
		List<Unit> unitSort = new ArrayList<>();
		for (Unit u: list) {
			if (u.isStandardUnit()) {
				unitSort.add(u);
			}
		}
		return unitSort.toArray(new Unit[]{});
	}
	void updateCache() {
		unitNamesCharsArraysSortCache     = unitNamesSort();
		try {
			unitNamesCharsArraysDelimiter = Delimiter.build(unitNamesCharsArraysSortCache);
		} catch (Throwable e){
			unitNamesCharsArraysDelimiter = null;
		}

		unitValueSortLeftMinRightMaxCache = unitValueSort();
	}



	CharsReaders createUnitReader() {
		CharsReaders reader = new CharsReaders();
		reader.setDelimiter(unitNamesCharsArraysDelimiter);
		return reader;
	}

	public String  getDefaultUnit()  { return defaultUnit; }
	protected void setDefaultUnit(String defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	public String[] getUnitNames() {
		return unitValue.keySet().toArray(Finals.EMPTY_STRING_ARRAY);
	}
	public String[] getStandardUnitNames() {
		List<String> list = new ArrayList<>();
		for (String k: unitValue.keySet()) {
			Unit unit = unitValue.get(k);
			if (unit.isStandardUnit()) {
				list.add(unit.name);
			}
		}
		return list.toArray(Finals.EMPTY_STRING_ARRAY);
	}



	protected void removeUnit(String unitNameOrAlias) {
		try {
			Unit unit = unitValue.get(unitNameOrAlias);
			if (null != unit) {
				unitValue.remove(unitNameOrAlias);

				if (unit.isStandardUnit()) {
					for (String key: new HashSet<String>(unitValue.keySet())) {
						Unit f = unitValue.get(key);
						if (unitNameOrAlias.equals(f.standardUnit)) {
							unitValue.remove(key);
						}
					}
				}
			}
		} finally {
			updateCache();
		}
	}
	protected void addUnit(Number value, String unitStandardName, String... alias) {
		addUnit(unitStandardName, alias, value);
	}
	protected void addUnit(String unitStandardName, Number value) {
		addUnit(unitStandardName, null, value);
	}
	protected void addUnit(String unitStandardName, String[] alias, Number value0) {
		try {
			removeUnit(unitStandardName.toString());
			BigDecimal value = toBigDecimal(value0);
			Unit unit = new Unit(unitStandardName, null, value);
			unitValue.put(unitStandardName, unit);

			if (!(null == alias || alias.length == 0)) {
				for (String aliasElement: alias) {
					unitValue.remove(aliasElement);
				}
				for (String aliasElement: alias) {
					if (null == aliasElement)
						continue;
					if (Objects.equals(aliasElement, unitStandardName))
						continue;
					unit = new Unit(aliasElement, unitStandardName, value);
					unitValue.put(aliasElement, unit);
				}
			}
		} finally {
			updateCache();
		}
	}




	public Unit getStandardUnit(String unitNameOrAlias) {
		Unit  value;
		if (null == (value = findStandardUnit(unitNameOrAlias))) {
			throw new NotFoundUnitException("not found unit: " + "[" + unitNameOrAlias + "]");
		}
		return value;
	}
	public Unit findStandardUnit(String unitNameOrAlias) {
		Unit  value = unitValue.get(unitNameOrAlias);
		if (null != value && value.isAlias()) {
			value = unitValue.get(value.standardUnit);
		}
		return value;
	}


	public Unit getUnit(String unitNameOrAlias) {
		Unit  value;
		if (null == (value = findUnit(unitNameOrAlias))) {
			throw new NotFoundUnitException("not found unit: " + "[" + unitNameOrAlias + "]");
		}
		return value;
	}
	public Unit findUnit(String unitNameOrAlias) {
		return unitValue.get(unitNameOrAlias);
	}



	public int size() {
		return unitValue.size();
	}


	static BigDecimal toBigDecimal(Number value) {
		return value instanceof BigDecimal ? (BigDecimal) value: new BigDecimal(value.toString());
	}

	public String to(Number value, String unit, int scale) {
		return to(value, getUnit(unit), scale);
	}
	public String to(Number value, Unit unit, int scale) {
		return to(value, unit, RoundingMode.FLOOR, scale);
	}
	public String to(Number value, String unit, RoundingMode rm, int scale) {
		return to(value, getUnit(unit), rm, scale);
	}
	public String to(Number value, Unit unit, RoundingMode rm, int scale) {
		return toBigDecimal(value).divide(unit.value, scale, rm) + unit.name;
	}


	public String toRecommend(Number value, int scale) {
		return toRecommend(value, RoundingMode.FLOOR, scale);
	}
	public String toRecommend(Number value, RoundingMode rm, int scale) {
		if (size() == 0) {
			throw new EmptyUnitException("");
		} else {
			BigDecimal v = toBigDecimal(value);
			BigDecimal last = null;
			Unit unit = null;
			for (Unit tempu : this.unitValueSortLeftMinRightMaxCache) {
				BigDecimal temp = v.divide(tempu.value, scale, rm);
				if (temp.compareTo(BigDecimal.ONE) >= 0) {
					if (null == last || last.compareTo(temp) > 0) {
						last = temp;
						unit = tempu;
					}
				} else if (null != last) {
					break;
				}
			}
			if (null == last) {
				unit = this.unitValueSortLeftMinRightMaxCache[0];
				last = v.divide(unit.value, scale, rm);
			} 
			return last + unit.name;
		}
	}




	public BigDecimal parseValue(String v) {
		if (null == v) {
			return BigDecimal.ZERO;
		} else {
			CharsReaders reader = createUnitReader();
			char[] data = v.toCharArray();
			reader.buffer(data, data.length);

			BigDecimal sum = BigDecimal.ZERO;
			String value = null;
			String unit;
			while  (reader.findNext()) {
				char[] next = reader.readNext();
				if (reader.lastIsReadReadSeparator()) {
					char[] unitSeparator = reader.lastReadSeparator();
					unit = new String(unitSeparator);
					if (null == value) {
						throw new ParseException(v);
					} else {
						String n = value.trim();
						Unit unitV = getStandardUnit(unit);
						sum = sum.add(unitV.value.multiply(new BigDecimal(n)));
					}
					value = null;
				} else {
					value = new String(next);
				}
			}
			if (null == value) {
				return sum;
			} else {
				String defaultUnit = getDefaultUnit();
				if (null == defaultUnit) {
					throw new ParseException("no default unit: " + v);
				}
				String n = value.trim();
				Unit unitV = getStandardUnit(defaultUnit);
				sum = sum.add(unitV.value.multiply(new BigDecimal(n)));
				return sum;
			}
		}
	}




	public static final BigDecimal  FILE_SIZE_BASE_1024D     = BigDecimal.valueOf(1024);
	public static final SizeUnit 	FILE_SIZE_1024_SIZE_UNIT = new SizeUnit() {{
		BigDecimal decimal = FILE_SIZE_BASE_1024D;

		super.setDefaultUnit("B");

		super.addUnit(decimal.pow(0),   "B",  "b");
		super.addUnit(decimal.pow(1),   "KB", "K", "kb","k");
		super.addUnit(decimal.pow(2),   "MB", "M", "mb","m");
		super.addUnit(decimal.pow(3),   "GB", "G", "gb","g");
		super.addUnit(decimal.pow(4),   "TB", "T", "tb","t");
		super.addUnit(decimal.pow(5),   "PB", "P", "pb","p");
		super.addUnit(decimal.pow(6),   "EB", "eb");
		super.addUnit(decimal.pow(7),   "ZB", "Z", "zb","z");
		super.addUnit(decimal.pow(8),   "YB", "Y", "yb","y");
		super.addUnit(decimal.pow(9),   "BB", "bb");
		super.addUnit(decimal.pow(10),  "NB", "N", "nb","n");
		super.addUnit(decimal.pow(11),  "DB", "db");
		super.addUnit(decimal.pow(12),  "CB", "C", "cb","c");
	}
	};
}

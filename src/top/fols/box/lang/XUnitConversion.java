package top.fols.box.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import top.fols.atri.util.DoubleLinked;
import top.fols.box.util.XCollections;

public class XUnitConversion {

	public static class Num {
		private BigDecimal value;

		public Num(BigDecimal number) {
			this.value = null == number ? BigDecimal.ZERO : number;
		}

		public Num(Number number) {
			this(new BigDecimal(number.toString()));
		}

		public Num(double number) {
			this(new BigDecimal(number));
		}

		public Num(String number) {
			this(new BigDecimal(number));
		}

		public BigDecimal value() {
			return this.value;
		}

		@Override
		public int hashCode() {
			// TODO: Implement this method
			return this.value.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			return this.value.equals(obj);
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			return this.value.toString();
		}
	}

	/**
	 * time conversion example
	 *
	 * XUnitConversion uc = new XUnitConversion(new LinkedHashMap<String,
	 * BigDecimal>(){{ put("ms", BigDecimal.valueOf(1L)); put("s",
	 * BigDecimal.valueOf(1L * 1000L)); put("m", BigDecimal.valueOf(1L * 1000L *
	 * 60L)); put("h", BigDecimal.valueOf(1L * 1000L * 60L * 60L)); put("d",
	 * BigDecimal.valueOf(1L * 1000L * 60L * 60L * 24L)); }});
	 * System.out.println(uc.parseToSum("666666s").format(new
	 * String[]{"m","s"}));//11111m6s (666666s = 11111m6s)
	 * System.out.println(uc.parseToSum("666.8h7m444s").format(2));//27.79d
	 * (666.8h7m444s = 27.79d)
	 */
	private String[] unitNames;
	private BigDecimal[] unitValues;

	private BigDecimal searchUnitValue(String unit) {
		if (null == unit) {
			return null;
		}
		for (int i = 0; i < this.unitNames.length; i++) {
			if (unit.equals(this.unitNames[i])) {
				return this.unitValues[i];
			}
		}
		return null;
	}

	private static class Sort {
		private BigDecimal unit;

		private String name;
	}

	private void setSortUnitAndValues0(String[] ns, BigDecimal[] us) {
		List<Sort> list = new ArrayList<>();
		for (int i = 0; i < us.length; i++) {
			Sort sort = new Sort();
			sort.name = ns[i];
			sort.unit = us[i];

			list.add(sort);
		}

		XCollections.sort(list, new Comparator<Sort>() {
                @Override
                public int compare(Sort p1, Sort p2) {
                    // TODO: Implement this method
                    return p1.unit.compareTo(p2.unit);
                }
            });

		List<String> names = new ArrayList<>();
		List<BigDecimal> units = new ArrayList<>();

		for (Sort si : list) {
			names.add(si.name);
			units.add(si.unit);
		}

		this.unitNames = names.toArray(new String[names.size()]);
		this.unitValues = units.toArray(new BigDecimal[units.size()]);

		list = null;
		names = null;
		units = null;
	}

	/**
	 *
	 * @param unit2 format: {D, 24L*60L*60L*1000L, H, 60L*60L*1000L}
	 */
	public XUnitConversion(LinkedHashMap<String, BigDecimal> unit2) {
		this.setSortUnitAndValues0(unit2.keySet().toArray(new String[unit2.size()]),
            unit2.values().toArray(new BigDecimal[unit2.size()]));
	}

	public XUnitConversion(String[] units, BigDecimal[] unitvs) {
		this.setSortUnitAndValues0(units, unitvs);
	}

	@SuppressWarnings("unchecked")
	private DoubleLinked<String> nextItem0(DoubleLinked<String> now) {
		return null == now ? null : (DoubleLinked<String>) now.getNext();
	}

	/**
	 *
	 * @param value format: 1unit1 2unit2 3unit3, example: 1.0D24H
	 * @return base
	 */
	public Num parseToSum(String value) throws ArithmeticException {
		DoubleLinked<String> bottom = splitStringValue(value);
		DoubleLinked<String> num = nextItem0(bottom);
		DoubleLinked<String> unit = nextItem0(num);
		BigDecimal base = BigDecimal.ZERO;
		while (null != num && null != unit) {
			BigDecimal decimal = new BigDecimal(num.toString());
			Number unitValue = this.searchUnitValue(unit.toString());
			if (null == unitValue) {
				throw new ArithmeticException("found not unit: " + unit);
			}
			base = base.add(decimal.multiply(new BigDecimal(unitValue.toString())));
			// System.out.println(num + "-" + unit + "/" + unitValue);
			num = nextItem0(unit);
			unit = nextItem0(num);
		}
		return new Num(base);
	}

	/**
	 * 
	 * @param unit: sort from largest to smallest example: {"D", "H"} return: xDxH
	 */
	public String format(String[] unit, Num num) {
		return this.format(unit, num, true);
	}

	public String format(String[] unit, Num num, boolean omitTheEmptyData) throws ArithmeticException {
		BigDecimal bd = num.value();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < unit.length; i++) {
			Number n = this.searchUnitValue(unit[i]);
			if (null == n) {
				throw new ArithmeticException("found not unit: " + unit[i]);
			}
			BigDecimal bdn = new BigDecimal(n.toString());
			BigDecimal k = bd.divide(bdn, 0, RoundingMode.DOWN);
			if (k.compareTo(BigDecimal.ZERO) == 0) {
				if (!omitTheEmptyData) {
					sb.append(k).append(unit[i]);
				}
			} else {
				sb.append(k).append(unit[i]);
			}
			bd = bd.subtract(bdn.multiply(k));
		}
		return sb.toString();
	}

	/**
	 * returns the largest unit as far as possible
	 * 
	 * @param scale: the length of the decimal
	 */
	public String format(Num num, int scale) {
		return this.format(num, RoundingMode.HALF_UP, scale);
	}

	public String format(Num num, RoundingMode rm, int scale) {
		BigDecimal bd = num.value();
		BigDecimal[] ts = new BigDecimal[this.unitValues.length];
		int maxunit = 0;
		for (int i = 0; i < this.unitValues.length; i++) {
			ts[i] = bd.divide(this.unitValues[i], scale, rm);
			if (ts[i].compareTo(BigDecimal.ONE) >= 0) {
				maxunit = i;
			} else {
				break;
			}
		}
		return ts[maxunit] + this.unitNames[maxunit];
	}

	private boolean isNumber(char c) {
		return c == '+' || c == '-' || c >= '0' && c <= '9' || c == '.';
	}

	private DoubleLinked<String> splitStringValue(String value) {
		int TYPE_START = 0, TYPE_NUMBER = 1, TYPE_OTHER = 2;

		int length = value.length();
		int lastIndex = 0;
		int lastType = TYPE_START; // start

		DoubleLinked<String> bottom = new DoubleLinked<String>(null);
		DoubleLinked<String> top = bottom;

		for (int i = 0; i < length; i++) {
			char ch = value.charAt(i);
			if (isNumber(ch)) {
				if (lastType != TYPE_NUMBER) {
					lastType = TYPE_NUMBER;
					if (lastIndex != i) {
						DoubleLinked<String> next = new DoubleLinked<String>(
                            value.substring(lastIndex, i));
						top.addNext(next);
						top = next;
					}
					lastIndex = i;
				}
			} else {
				if (lastType != TYPE_OTHER) {
					lastType = TYPE_OTHER;
					if (lastIndex != i) {
						DoubleLinked<String> next = new DoubleLinked<String>(
                            value.substring(lastIndex, i));
						top.addNext(next);
						top = next;
					}
					lastIndex = i;
				}
			}
		}
		if (lastIndex != length) {
			DoubleLinked<String> next = new DoubleLinked<String>(
                value.substring(lastIndex, length));
			top.addNext(next);
			top = next;
		}
		return bottom;
	}

}

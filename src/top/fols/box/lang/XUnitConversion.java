package top.fols.box.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;
public class XUnitConversion {

	public static String unitCalc(String data,
								  String[] unitChars, int offUnit,
								  BigDecimal[] perUnitSize, 
								  boolean round, int roundScale) {
		if (null == unitChars || unitChars.length == 0)
			throw new RuntimeException("unitChars for null");
		if (unitChars.length != perUnitSize.length)
			throw new RuntimeException("unitChars and perUnitSize length not equals");
		int i = offUnit;
		if (null == data)
			return 0 + unitChars[i];
		if (new BigDecimal(data).compareTo(perUnitSize[i]) == -1)
			return data + unitChars[i];
		BigDecimal kiloByte = new BigDecimal(data).divide(perUnitSize[i], 4, RoundingMode.DOWN);
		BigDecimal tmp;
		while (kiloByte.compareTo(BigDecimal.ZERO) == 1) {
			if (i + 1 > unitChars.length - 1)
				break;
			i++;
			tmp = kiloByte.divide(perUnitSize[i], 4, RoundingMode.DOWN);
			if (tmp.compareTo(BigDecimal.ONE) == -1)
				break;//<1
			kiloByte = tmp;
		}
		if (round) 
			return kiloByte.setScale(roundScale, BigDecimal.ROUND_HALF_UP).toString() + unitChars[i];
		return kiloByte.toString() + unitChars[i];
	}
	public static String unitCalc(String data, 
								  String[] unitChars, int offUnit,
								  BigDecimal perUnitSize, 
								  boolean round, int roundScale) {
		if (null == unitChars || unitChars.length == 0)
			throw new RuntimeException("unitChars for null");
		int i = offUnit;
		if (null == data)
			return 0 + unitChars[i];
		if (new BigDecimal(data).compareTo(perUnitSize) == -1)
			return data + unitChars[i];
		BigDecimal kiloByte = new BigDecimal(data).divide(perUnitSize, 4, RoundingMode.DOWN);
		BigDecimal tmp;
		while (kiloByte.compareTo(BigDecimal.ZERO) == 1) {
			if (i + 1 > unitChars.length - 1)
				break;
			i++;
			tmp = kiloByte.divide(perUnitSize, 4, RoundingMode.DOWN);
			if (tmp.compareTo(BigDecimal.ONE) == -1)
				break;//<1
			kiloByte = tmp;
		}
		if (round) 
			return kiloByte.setScale(roundScale, BigDecimal.ROUND_HALF_UP).toString() + unitChars[i];
		return kiloByte.toString() + unitChars[i];
	}






	public static String unitCalc(double data, 
								  String[] unitChars, int offUnit,
								  double[] perUnitSize,
								  boolean round, int roundScale) {
		if (null == unitChars || unitChars.length == 0)
			throw new RuntimeException("unitChars for null");
		if (unitChars.length != perUnitSize.length)
			throw new RuntimeException("unitChars and perUnitSize length not equals");
		int i = offUnit;
		if (data < perUnitSize[i])
			return Double.toString(data) + unitChars[i];
		double kiloByte = data / perUnitSize[i];
		double tmp;
		while (kiloByte > 0) {
			if (i + 1 > unitChars.length - 1)
				break;
			i++;
			tmp = kiloByte / perUnitSize[i];
			if (tmp < 1)
				break;//<1
			kiloByte = tmp;
		}
		if (round)
			return new BigDecimal(kiloByte).setScale(roundScale, BigDecimal.ROUND_HALF_UP).toString() + unitChars[i];
		return Double.toString(kiloByte) + unitChars[i];
	}
	public static String unitCalc(double data,
								  String[] unitChars, int offUnit,
								  double perUnitSize,
								  boolean round, int roundScale) {
		if (null == unitChars || unitChars.length == 0)
			throw new RuntimeException("unitChars for null");
		int i = 0;
		if (data < perUnitSize)
			return Double.toString(data) + unitChars[i];
		double kiloByte = data / perUnitSize;
		double tmp;
		while (kiloByte > 0) {
			if (i + 1 > unitChars.length - 1)
				break;
			i++;
			tmp = kiloByte / perUnitSize;
			if (tmp < 1)
				break;//<1
			kiloByte = tmp;
		}
		if (round)
			return new BigDecimal(kiloByte).setScale(roundScale, BigDecimal.ROUND_HALF_UP).toString() + unitChars[i];
		return Double.toString(kiloByte) + unitChars[i];
	}


}

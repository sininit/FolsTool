package top.fols.box.util;

import java.io.IOException;

public class XPropertiesTest {
    public static void main(String[] args) throws IOException {
        XProperties xpw = new XProperties();
		xpw.put("1", "666");
		xpw.putBytes("2", "666".getBytes());
		xpw.put("3", null);
		xpw.put("null", null);
		System.out.println("(" + xpw.saveToString() + ")");

		XProperties xp = new XProperties().loadString(xpw.saveToString());
		System.out.println(xp.getInnerMap().size());
		System.out.println(xp.getInnerMap());
		System.out.println("(" + xp.saveToString() + ")");

		System.out.println("("+new XProperties().saveToString()+")");

		System.out.println();
    }
}
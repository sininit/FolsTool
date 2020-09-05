package top.fols.box.util;

import java.util.HashMap;
import java.util.Map;

public class XBlurryKeyTest {
    public static void main(String[] args) {
        final XBlurryKey.IgnoreCaseKey ick = XBlurryKey.IgnoreCaseKey.getDefaultFactory();

        Map<XBlurryKey, Object> bkm = new HashMap<>();
        bkm.putAll(new HashMap<XBlurryKey, Object>(){{
            put(ick.newKey("H"), "hh");
            put(ick.newKey(""), "hh2");
            put(ick.newKey(666), "hh3");
            put(ick.newKey(666), "hh2");

            put(ick.newKey(XBlurryKey.formatKey(ick, ick.newKey(777))), "hh1");
        }});
        bkm.putAll(new HashMap(){{
            put(777, 8);
        }});

        System.out.println(bkm);

    }
}
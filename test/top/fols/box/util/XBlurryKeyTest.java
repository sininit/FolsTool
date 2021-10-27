package top.fols.box.util;

import top.fols.atri.util.BlurryKey;

import java.util.HashMap;
import java.util.Map;

public class XBlurryKeyTest {
    public static void main(String[] args) {
        final BlurryKey.IgnoreCaseKey ick = BlurryKey.IgnoreCaseKey.getDefaultFactory();

        Map<BlurryKey, Object> bkm = new HashMap<>();
        bkm.putAll(new HashMap<BlurryKey, Object>(){{
            put(ick.newKey("H"), "hh");
            put(ick.newKey(""), "hh2");
            put(ick.newKey(666), "hh3");
            put(ick.newKey(666), "hh2");

            put(ick.newKey(BlurryKey.formatKey(ick, ick.newKey(777))), "hh1");
        }});
        bkm.putAll(new HashMap(){{
            put(777, 8);
        }});

        System.out.println(bkm);

    }
}
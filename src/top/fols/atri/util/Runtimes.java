package top.fols.atri.util;

import java.util.ArrayList;
import java.util.List;

public class Runtimes {


    public static long getJVMUseMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }


    /**
     * @return use memory / max apply memory
     */
    public static double getJVMUseMemoryPercentage() {
        Runtime runtime = Runtime.getRuntime();
        double use = (runtime.totalMemory() - runtime.freeMemory());
        return(use / (double) runtime.maxMemory()) * 100;
    }


    /**
     * @return use memory / already apply memory
     */
    public static double getJVMUseApplyMemoryPercentage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        double  use = (totalMemory - runtime.freeMemory());
        return (use / (double) totalMemory) * 100;
    }


    public static void outOfMemory() {
        List<Object> data;
        try {
            data  = new ArrayList<>();
            for (;;) {
                data.add(new int[Integer.MAX_VALUE]);
            }
        } catch (OutOfMemoryError ignored) {}
        data = null;
    }
}

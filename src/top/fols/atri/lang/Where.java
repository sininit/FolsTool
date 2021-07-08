package top.fols.atri.lang;

import top.fols.atri.lang.Objects.Executor;

@SuppressWarnings({"UnnecessaryUnboxing", "PointlessBooleanExpression", "unused", "SpellCheckingInspection"})
public class Where<T> {
    /**
     * non null
     * non false
     * non 0
     */
    @SuppressWarnings("rawtypes")
    public static boolean isTrue(Object object) {
        if (null == object) 			    { return false; }

        if (object instanceof Boolean)      { return ((Boolean)object).booleanValue() != false; }
        if (object instanceof Number)       { return ((Number) object).doubleValue()  != 0D;    }

//        * non ""
//        if (object instanceof String)       { return ((String) object).length()  != 0;          }

        if (object instanceof Where)        {
            Where  where = (Where) object;
            return null != where.process && isTrue(where.process.execute());
        }

        return true;
    }




    public static <T> Where<T> execute(Executor<T> w) {
        return new Where<>(w);
    }

    private final Executor<T> process;
    private Where(Executor<T> process) {
        this.process = process;
    }


    @Override
    public final String toString() {
        // TODO: Implement this method
        // TODO: Implement this method
        Object     result = null;
        try {
            result = process.execute();
        } catch (Throwable ignored) {
        }
        return null == result ? null: String.valueOf(result);
    }






    public static boolean and(Object... value) {
        if (null == value || value.length == 0) { return false; }
        boolean result = true;
        for (Object object: value) {
            if (!Where.isTrue(object))
                result = false;
        }
        return result;
    }
    public static boolean or(Object... value) {
        if (null == value || value.length == 0) { return false; }
        boolean result = false;
        for (Object object: value)
            result |= Where.isTrue(object);
        return result;
    }






    public static boolean aand(Object... value) {
        if (null == value || value.length == 0) { return false; }
        for (Object object: value) {
            if (!(Where.isTrue(object))) {
                return false;
            }
        }
        return true;
    }
    public static boolean oor(Object... value) {
        if (null == value || value.length == 0) { return false; }
        for (Object object: value) {
            if (Where.isTrue(object)) {
                return true;
            }
        }
        return false;
    }

}


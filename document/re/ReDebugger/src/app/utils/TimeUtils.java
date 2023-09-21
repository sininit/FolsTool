package app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeUtils {

    /**
     * Get yyyy-MM-dd
     */
    public static String getTime_yyyy_MM_dd_toString(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }
    /**
     * Get yyyy-MM-dd
     */
    public static Long getTime_yyyy_MM_dd(String time) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime();
        } catch (ParseException e) {
            return null;
        }
    }
    /**
     * Get yyyy-MM-dd
     */
    public static boolean isTime_yyyy_MM_dd(String time) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(time);
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }


    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getTime_yyyy_MM_dd_HH_mm_ss_toString(Long time) {
        if (null == time) return null;

        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(time);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static Long getTime_yyyy_MM_dd_HH_mm_ss(String format)  {
        try {
            if (null == format) return null;

            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            return sdf.parse(format).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static boolean isTime_yyyy_MM_dd_HH_mm_ss(String time) {
        try {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }






    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getTime_toString(String format, Long time) {
        if (null == time) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static Long getTime(String format, String time)  {
        try {
            if (null == format) return null;

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static boolean isTime(String format, String time) {
        try {
            new SimpleDateFormat(format).parse(time);
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }





    /**
     * HH:mm:ss
     */
    public static String getTime_HH_mm_ss_toString(Long time) {
        if (null == time) return null;

        String strDateFormat = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(time);
    }
    /**
     * HH:mm:ss
     */
    public static Long getTime_HH_mm_ss(String format)  {
        try {
            if (null == format) return null;

            String strDateFormat = "HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            return sdf.parse(format).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * HH:mm:ss
     */
    public static boolean isTime_HH_mm_ss(String time) {
        try {
            new SimpleDateFormat("HH:mm:ss").parse(time);
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }


    /**
     * yyyyMMdd
     */
    @SuppressWarnings({"unused"})
    public static String getTime_yyyyMMdd_toString(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(time);
    }
    /**
     * yyyyMMdd
     */
    public static Long getTime_yyyyMMdd(String format)  {
        try {
            if (null == format) return null;

            String strDateFormat = "yyyyMMdd";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            return sdf.parse(format).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * yyyyMMddHHmmss
     */
    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static String getGMT_yyyyMMddHHmmss(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(time);
    }

}

package top.fols.atri.lang;

import java.util.*;

import top.fols.atri.util.Lists;

@SuppressWarnings("UnnecessaryUnboxing")
public class Objects {
    private static final String INFINITY_REP = "Infinity";
    private static final String NAN_REP = "NaN";

    public static boolean is_Boolean(String s)           {return Finals.TRUE_STRING.equalsIgnoreCase(s) || Finals.FALSE_STRING.equals(s);}
    public static Boolean get_Boolean(String s)          {return ((s != null) && s.equalsIgnoreCase(Finals.TRUE_STRING));}
    public static boolean get_boolean(String s)          {return ((s != null) && s.equalsIgnoreCase(Finals.TRUE_STRING));}

    /**
     * @see Integer#parseInt(String)
     */
    public static char parseChar(String s) {
        if ((s != null) && s.length() == 1) {
            return s.charAt(0);
        } else {
            throw new NumberFormatException(to_String(s));
        }
    }
    public static boolean   is_Char(String s)            {
        return ((s != null) && s.length() == 1);
    }

    public static Character get_Char(String s)           {
        return ((s != null) && s.length() > 0) ? s.charAt(0) : null;
    }
    public static char      get_char(String s)           {
        return ((s != null) && s.length() > 0) ? s.charAt(0) : 0;
    }



    public static boolean is_Short(String s) {return is_Short(s, 10);}
    public static boolean is_Short(String s, int radix) {
        if (s == null) {
            return false;
        }

        if (radix < Character.MIN_RADIX) {
            return false;
        }

        if (radix > Character.MAX_RADIX) {
            return false;
        }

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Short.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Short.MIN_VALUE;
                } else if (firstChar != '+')
                    return false;

                if (len == 1) // Cannot have lone "+" or "-"
                    return false;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return false;
                }
                if (result < multmin) {
                    return false;
                }
                result *= radix;
                if (result < limit + digit) {
                    return false;
                }
                result -= digit;
            }
        } else {
            return false;
        }
        return true;
    }

    public static Short get_Short(String s) {
        return get_Short(s, 10);
    }
    public static Short get_Short(String s, int radix) {
        if (s == null) {
            return null;
        }

        if (radix < Character.MIN_RADIX) {
            return null;
        }

        if (radix > Character.MAX_RADIX) {
            return null;
        }

        short result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Short.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Short.MIN_VALUE;
                } else if (firstChar != '+')
                    return null;

                if (len == 1) // Cannot have lone "+" or "-"
                    return null;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return null;
                }
                if (result < multmin) {
                    return null;
                }
                result *= radix;
                if (result < limit + digit) {
                    return null;
                }
                result -= digit;
            }
        } else {
            return null;
        }
        return negative ? result : (short) -result;
    }
    public static short get_short(String s) {
        return get_short(s, 10);
    }
    public static short get_short(String s, int radix) {
        if (s == null) {
            return 0;
        }

        if (radix < Character.MIN_RADIX) {
            return 0;
        }

        if (radix > Character.MAX_RADIX) {
            return 0;
        }

        short result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Short.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Short.MIN_VALUE;
                } else if (firstChar != '+')
                    return 0;

                if (len == 1) // Cannot have lone "+" or "-"
                    return 0;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return 0;
                }
                if (result < multmin) {
                    return 0;
                }
                result *= radix;
                if (result < limit + digit) {
                    return 0;
                }
                result -= digit;
            }
        } else {
            return 0;
        }
        return negative ? result : (short) -result;
    }




    public static boolean is_Byte(String s) {
        return is_Byte(s, 10);
    }
    public static boolean is_Byte(String s, int radix) {
        if (s == null) {
            return false;
        }

        if (radix < Character.MIN_RADIX) {
            return false;
        }

        if (radix > Character.MAX_RADIX) {
            return false;
        }

        byte result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Byte.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Byte.MIN_VALUE;
                } else if (firstChar != '+')
                    return false;

                if (len == 1) // Cannot have lone "+" or "-"
                    return false;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return false;
                }
                if (result < multmin) {
                    return false;
                }
                result *= radix;
                if (result < limit + digit) {
                    return false;
                }
                result -= digit;
            }
        } else {
            return false;
        }
        return true;
    }

    public static Byte get_Byte(String s) {
        return get_Byte(s, 10);
    }
    public static Byte get_Byte(String s, int radix) {
        if (s == null) {
            return null;
        }

        if (radix < Character.MIN_RADIX) {
            return null;
        }

        if (radix > Character.MAX_RADIX) {
            return null;
        }

        byte result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Byte.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Byte.MIN_VALUE;
                } else if (firstChar != '+')
                    return null;

                if (len == 1) // Cannot have lone "+" or "-"
                    return null;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return null;
                }
                if (result < multmin) {
                    return null;
                }
                result *= radix;
                if (result < limit + digit) {
                    return null;
                }
                result -= digit;
            }
        } else {
            return null;
        }
        return negative ? result : (byte) -result;
    }

    public static byte get_byte(String s) {
        return get_byte(s, 10);
    }
    public static byte get_byte(String s, int radix) {
        if (s == null) {
            return 0;
        }

        if (radix < Character.MIN_RADIX) {
            return 0;
        }

        if (radix > Character.MAX_RADIX) {
            return 0;
        }

        byte result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Byte.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Byte.MIN_VALUE;
                } else if (firstChar != '+')
                    return 0;

                if (len == 1) // Cannot have lone "+" or "-"
                    return 0;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return 0;
                }
                if (result < multmin) {
                    return 0;
                }
                result *= radix;
                if (result < limit + digit) {
                    return 0;
                }
                result -= digit;
            }
        } else {
            return 0;
        }
        return negative ? result : (byte) -result;
    }



    public static boolean is_Int(String s) {
        return is_Int(s, 10);
    }
    public static boolean is_Int(String s, int radix) {
        if (s == null) {
            return false;
        }

        if (radix < Character.MIN_RADIX) {
            return false;
        }

        if (radix > Character.MAX_RADIX) {
            return false;
        }

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+')
                    return false;

                if (len == 1) // Cannot have lone "+" or "-"
                    return false;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return false;
                }
                if (result < multmin) {
                    return false;
                }
                result *= radix;
                if (result < limit + digit) {
                    return false;
                }
                result -= digit;
            }
        } else {
            return false;
        }
        return true;
    }

    public static Integer get_Int(String s) {
        return get_Int(s, 10);
    }
    public static Integer get_Int(String s, int radix) {
        if (s == null) {
            return null;
        }

        if (radix < Character.MIN_RADIX) {
            return null;
        }

        if (radix > Character.MAX_RADIX) {
            return null;
        }

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+')
                    return null;

                if (len == 1) // Cannot have lone "+" or "-"
                    return null;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return null;
                }
                if (result < multmin) {
                    return null;
                }
                result *= radix;
                if (result < limit + digit) {
                    return null;
                }
                result -= digit;
            }
        } else {
            return null;
        }
        return negative ? result : -result;
    }

    public static int get_int(String s) {
        return get_int(s, 10);
    }
    public static int get_int(String s, int radix) {
        if (s == null) {
            return 0;
        }

        if (radix < Character.MIN_RADIX) {
            return 0;
        }

        if (radix > Character.MAX_RADIX) {
            return 0;
        }

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+')
                    return 0;

                if (len == 1) // Cannot have lone "+" or "-"
                    return 0;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return 0;
                }
                if (result < multmin) {
                    return 0;
                }
                result *= radix;
                if (result < limit + digit) {
                    return 0;
                }
                result -= digit;
            }
        } else {
            return 0;
        }
        return negative ? result : -result;
    }


    public static boolean is_Long(String s) {
        return is_Long(s, 10);
    }
    public static boolean is_Long(String s, int radix) {
        if (s == null) {
            return false;
        }

        if (radix < Character.MIN_RADIX) {
            return false;
        }
        if (radix > Character.MAX_RADIX) {
            return false;
        }

        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+')
                    return false;

                if (len == 1) // Cannot have lone "+" or "-"
                    return false;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return false;
                }
                if (result < multmin) {
                    return false;
                }
                result *= radix;
                if (result < limit + digit) {
                    return false;
                }
                result -= digit;
            }
        } else {
            return false;
        }
        return true;
    }

    public static Long get_Long(String s) {
        return get_Long(s, 10);
    }
    public static Long get_Long(String s, int radix) {
        if (s == null) {
            return null;
        }

        if (radix < Character.MIN_RADIX) {
            return null;
        }
        if (radix > Character.MAX_RADIX) {
            return null;
        }

        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+')
                    return null;

                if (len == 1) // Cannot have lone "+" or "-"
                    return null;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return null;
                }
                if (result < multmin) {
                    return null;
                }
                result *= radix;
                if (result < limit + digit) {
                    return null;
                }
                result -= digit;
            }
        } else {
            return null;
        }
        return negative ? result : -result;
    }

    public static long get_long(String s) {
        return get_long(s, 10);
    }
    public static long get_long(String s, int radix) {
        if (s == null) {
            return 0;
        }

        if (radix < Character.MIN_RADIX) {
            return 0;
        }
        if (radix > Character.MAX_RADIX) {
            return 0;
        }

        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+')
                    return 0;

                if (len == 1) // Cannot have lone "+" or "-"
                    return 0;
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
// Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    return 0;
                }
                if (result < multmin) {
                    return 0;
                }
                result *= radix;
                if (result < limit + digit) {
                    return 0;
                }
                result -= digit;
            }
        } else {
            return 0;
        }
        return negative ? result : -result;
    }





    public static boolean is_Double(String s) {
        if (null == s)
            return false;
        int len = s.length();
        if (len == 0)
            return false;
        if (INFINITY_REP.equals(s) || NAN_REP.equals(s))
            return true;

        int zs = 0, xs = 0, kxs = 0;
        boolean point = false, kx = false;

        int i = 0;
        char firstChar = s.charAt(0);
        if (firstChar < '0') {
            if (firstChar == '.') {
                point = true;
            } else if (firstChar == '-') {
            } else if (firstChar != '+') {
                return false;
            }

            if (len == 1) // Cannot have lone "+" or "-"
                return false;
            i++;
        }
        while (i < len) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                if (kx)
                    kxs++;
                else if (point)
                    xs++;
                else
                    zs++;
            } else if (ch == '.') {
                if (kx)
                    return false;
                if (point)
                    return false;
                point = true;
            } else if (ch == 'e' || ch == 'E') {
                if (kx)
                    return false;
                kx = true;

                if (i + 1 < len) {
                    firstChar = s.charAt(i + 1);
                    if (firstChar == '-' || firstChar == '+') { //1E+1   1E-1
                        i++;
                    }
                }
            } else if (i == len - 1) {
                if (ch == 'f' || ch == 'F' ||
                        ch == 'd' || ch == 'D') {
                    break;
                }
                return false;
            } else
                return false;
            i++;
        }
        if (zs <= 0)
            if (xs <= 0)
                return false;
        if (kx)
            return kxs != 0;
        return true;
    }
    public static Double get_Double(String in) {return is_Double(in) ? Double.parseDouble(in) : null;}
    public static double get_double(String in) {return is_Double(in) ? Double.parseDouble(in) : 0D;}


    public static boolean is_Float(String in) {return is_Double(in);}
    public static Float get_Float(String in)  {return is_Double(in) ? Float.parseFloat(in) : null;}
    public static float get_float(String in)  {return is_Double(in) ? Float.parseFloat(in) : 0F;}



    static void ____________(){}





    /*
     * parse tip Object[] byte[] long[] double[] char[] int[] boolean[] float[] short[]
     */
    public static String get_String(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    public static char get_char(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Character)   return (Character) obj;
        if (obj instanceof Number)      return (char) ((Number) obj).intValue();

        return get_char(obj.toString());
    }

    public static boolean get_boolean(Object obj) {
        if (obj == null) return false;

        if (obj instanceof Boolean)   return (Boolean) obj;
        if (obj instanceof Number)    return ((Number) obj).intValue() != 0;
        if (obj instanceof Character) return (Character) obj != 0;

        return get_boolean(obj.toString());
    }

    public static byte get_byte(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).byteValue();
        if (obj instanceof Character)   return (byte) ((Character) obj).charValue();

        return get_byte(obj.toString());
    }


    public static int get_int(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).intValue();
        if (obj instanceof Character)   return (int) ((Character) obj).charValue();

        return get_int(obj.toString());
    }

    public static long get_long(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).longValue();
        if (obj instanceof Character)   return (long) ((Character) obj).charValue();

        return get_long(obj.toString());
    }

    public static short get_short(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).shortValue();
        if (obj instanceof Character)   return (short) ((Character) obj).charValue();

        return get_short(obj.toString());
    }


    public static double get_double(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).doubleValue();
        if (obj instanceof Character)   return (double) ((Character) obj).charValue();

        return get_double(obj.toString());
    }

    public static float get_float(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).floatValue();
        if (obj instanceof Character)   return (float) ((Character) obj).charValue();

        return get_float(obj.toString());
    }










    static void ___________(){}


    public static String to_String(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    public static char to_char(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Character)    return (Character) obj;
        if (obj instanceof Number)       return (char) ((Number) obj).intValue();

        return Objects.parseChar(obj.toString());
    }

    public static boolean to_boolean(Object obj) {
        if (obj == null) return false;

        if (obj instanceof Boolean)   return (Boolean) obj;
        if (obj instanceof Number)    return ((Number)obj).intValue() != 0;
        if (obj instanceof Character) return (Character) obj != 0;

        return Boolean.parseBoolean(obj.toString());
    }

    public static byte to_byte(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).byteValue();
        if (obj instanceof Character)   return (byte) ((Character) obj).charValue();

        return Byte.parseByte(obj.toString());
    }

    public static int to_int(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).intValue();
        if (obj instanceof Character)   return (int) ((Character) obj).charValue();

        return Integer.parseInt(obj.toString());
    }

    public static long to_long(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).longValue();
        if (obj instanceof Character)   return (long) ((Character) obj).charValue();

        return Long.parseLong(obj.toString());
    }

    public static short to_short(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).shortValue();
        if (obj instanceof Character)   return (short) ((Character) obj).charValue();

        return Short.parseShort(obj.toString());
    }

    /**
     * xx.xxx
     **/
    public static double to_double(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).doubleValue();
        if (obj instanceof Character)   return (double) ((Character) obj).charValue();

        return Double.parseDouble(obj.toString());
    }

    /**
     * xx.xxx
     **/
    public static float to_float(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Number)      return ((Number) obj).floatValue();
        if (obj instanceof Character)   return (float) ((Character) obj).charValue();

        return Float.parseFloat(obj.toString());
    }


    static void _________(){}




    public static boolean equals(Object obj, Object obj2) {
        if (null == obj) {
            return null == obj2;
        } else {
            return obj.equals(obj2);
        }
    }
    public static boolean identityEquals(Object[] parameterTypes, Object[] parameterTypes2) {
        if (parameterTypes.length != parameterTypes2.length) { return false;}
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] != parameterTypes2[i]) {
                return false;
            }
        }
        return true;
    }



    public static boolean and(boolean... value) {
        if (null == value || value.length == 0) { return false; }
        for (boolean object: value)
            if (!object)
                return false;
        return true;
    }
    public static boolean or(boolean... value) {
        if (null == value || value.length == 0) { return false; }
        for (boolean object: value)
            if (object)
                return true;
        return false;
    }










    public static <T> T first(T[] obj) {
        return (null != obj && obj.length > 0) ? obj[0] : null;
    }
    public static <T> T last(T[] obj) {
        return (null != obj && obj.length > 0) ? obj[obj.length - 1] : null;
    }
    public static <T> T first(List<T> obj) {
        return (null != obj && obj.size() > 0) ? obj.get(0) : null;
    }
    public static <T> T last(List<T> obj) {
        if (null == obj)
            return null;
        int    size = obj.size();
        return size > 0 ? obj.get(size - 1) : null;
    }
    public static Object opt(Object[] arguments, int index) {
        return index >= 0 && index < arguments.length ? arguments[index]: null;
    }

    public static <T> T nvl(T p1, T p2) {
        return null == p1 ? p2 : p1;
    }
    public static <T> T cast(Object value, Class<T> type) {
        return Classz.isInstance(value, type, false) ? (T) value : null;
    }


    public static <T> Set<T> asSet(T... vs) {
        return Lists.asLinkedHashSet(vs);
    }
    public static <T> List<T> asList(T... vs) {
        List<T> newList = new ArrayList<>();
        Collections.addAll(newList, vs);
        return newList;
    }


    public static <T> T requireNonNull(T obj) {
        if (null == obj) { throw new NullPointerException(); }
        return obj;
    }
    public static <T> T requireNonNull(T obj, String elseThrow) {
        if (null == obj) { throw new NullPointerException(elseThrow); }
        return obj;
    }


    public static void requireTrue(boolean obj) throws RuntimeException {
        if (!obj) { throw new RuntimeException("tip require = true"); }
    }
    public static void requireTrue(boolean obj, String elseThrow) throws RuntimeException {
        if (!obj) { throw new RuntimeException(elseThrow); }
    }



    public static <T extends Number> T requireLess(T value, long require, String name) {
        if (null != value && value.longValue() < require)
            return (value);
        throw new IllegalArgumentException((null == name ? "" : "input: " + name + "=") + value + " >= " + require);
    }
    public static <T extends Number> T requireLessOrEq(T value, long require, String name) {
        if (null != value && value.longValue() <= require)
            return (value);
        throw new IllegalArgumentException((null == name ? "" : "input: " + name + "=") + value + " > " + require);
    }
    public static <T extends Number> T requireGreater(T value, long require, String name) {
        if (null != value && value.longValue() > require)
            return (value);
        throw new IllegalArgumentException((null == name ? "" : "input: " + name + "=") + value + " <= " + require);
    }
    public static <T extends Number> T requireGreaterOrEq(T value, long require, String name) {
        if (null != value && value.longValue() >= require)
            return (value);
        throw new IllegalArgumentException((null == name ? "" : "input: " + name + "=") + value + " < " + require);
    }













    public static boolean isSmallNumber(Number number) {
        return null != number && number.doubleValue() % 1 != 0;
    }
    public static boolean isSmallNumber(String s) {
        return Objects.is_Double(s);
    }

    public static boolean isWholeNumber(Number number) {
        return null != number && number.doubleValue() % 1 == 0;
    }
    public static boolean isWholeNumber(String s) {
        if (null == s)
            return false;
        int length = s.length();
        if (length == 0)
            return false;
        int  i = 0;
        char c = s.charAt(0);
        if (c == '+' || c == '-')
            i = 1;
        for (; i < length; i++)
            if (!Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }









    public static int identityHashCode(Object object) {
        return System.identityHashCode(object);
    }
    public static String identityToString(Object o) {
        if (null == o) return "null";
        return o.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(o));
    }





    public static int hashCode(Object object) {
        return null != object ? object.hashCode() : 0;
    }

    public static int hashCode(String clasz) {
        if (clasz == null)
            return 0;
        return clasz.hashCode();
    }

    public static int hashCode(Class<?> clasz) {
        if (clasz == null)
            return 0;
        return clasz.getName().hashCode();
    }
    public static int hashCode(Class<?>[] classes) {
        if (classes == null)
            return 0;

        int result = 1;

        for (Class<?> element : classes)
            result = 31 * result + (element == null ? 0 : element.getName().hashCode());

        return result;
    }








    public static boolean isEmpty(Map<?,?> obj) {return null == obj || obj.size() == 0;}
    public static boolean isEmpty(Collection<?> obj) {return null == obj || obj.size() == 0;}
    public static boolean isEmpty(Object[] obj) {return null == obj || obj.length == 0;}
    public static boolean isEmpty(Object obj) { return null == obj; }
    public static boolean isEmpty(CharSequence obj) {return null == obj || obj.length() == 0;}
    public static boolean isEmpty(long[] obj) { return null == obj || obj.length == 0; }
    public static boolean isEmpty(int[] obj) { return null == obj || obj.length == 0; }
    public static boolean isEmpty(short[] obj) { return null == obj || obj.length == 0; }
    public static boolean isEmpty(byte[] obj) { return null == obj || obj.length == 0; }
    public static boolean isEmpty(boolean[] obj) { return null == obj || obj.length == 0; }
    public static boolean isEmpty(double[] obj) { return null == obj || obj.length == 0; }
    public static boolean isEmpty(float[] obj) { return null == obj || obj.length == 0; }
    public static boolean isEmpty(char[] obj) { return null == obj || obj.length == 0;}




    public static boolean isArray(Object object) { return null != object && object.getClass().isArray(); }
    public static boolean isEnum(Object object) { return null != object && object.getClass().isEnum(); }
    public static boolean isInstance(Object object, Class<?> type)   { return Classz.isInstanceNullable(object, type); }
    public static boolean isInstance(Class<?> object, Class<?> type) { return Classz.isInstanceNullable(object, type); }









}


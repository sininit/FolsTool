/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * origin module @org.json
 */

package top.fols.atri.assist.json;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked", "ForLoopReplaceableByForEach", "UnusedReturnValue", "unused"})
public class JSON {
    /**
     * Returns the input if it is a JSON-permissible tip; throws otherwise.
     */
    static double checkDouble(double d) throws JSONException {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new JSONException("Forbidden numeric tip: " + d);
        }
        return d;
    }

    static Boolean toBoolean(Object value) throws JSONException {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return null;
    }

    static Double toDouble(Object value) throws JSONException {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    static Integer toInteger(Object value) throws JSONException {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    static Long toLong(Object value) throws JSONException {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return (long) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    static String toString(Object value) throws JSONException {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }




	












    public static JSONException typeMismatch(Object indexOrName, Object actual,
											 String requiredType) throws JSONException {
        if (actual == null) {
            throw new JSONException("Value at " + indexOrName + " is null.");
        } else {
            throw new JSONException("Value " + actual + " at " + indexOrName
									+ " of type " + actual.getClass().getName()
									+ " cannot be converted to " + requiredType);
        }
    }

    public static JSONException typeMismatch(Object actual, String requiredType)
	throws JSONException {
        if (actual == null) {
            throw new JSONException("Value is null.");
        } else {
            throw new JSONException("Value " + actual
									+ " of type " + actual.getClass().getName()
									+ " cannot be converted to " + requiredType);
        }
    }








    public static JSONObject parseJSONObject(String str) throws JSONException {
        Object object = parseJSON(str);
        if (object instanceof JSONObject) {
            return ((JSONObject) object);
        } else {
            throw JSON.typeMismatch(object, "JSONObject");
        }
    }
    public static JSONArray parseJSONArray(String str) throws JSONException {
        Object object = parseJSON(str);
        if (object instanceof JSONArray) {
            return ((JSONArray) object);
        } else {
            throw JSON.typeMismatch(object, "JSONArray");
        }
    }
    public static Object parseJSON(String str) throws JSONException {
        /*
         * Getting the parser to populate this could get tricky. Instead, just
         * parse to temporary JSONObject and then steal the data from that.
         */
        JSONTokener readFrom = new JSONTokener(str);
        Object object = readFrom.readFirstJSONObject();
        if (object instanceof JSONObject || object instanceof JSONArray) {
            return object;
        } else {
            throw JSON.typeMismatch(object, "JSON");
        }
    }




    public static boolean isJSON(String str) {
        return isJSONObject(str) || isJSONArray(str);
    }
    public static boolean isJSONObject(String str) {
        if (!(null == str || str.length() == 0) && (str = str.trim()).length() > 0 && (str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}')) {
            try {
                parseJSONObject(str);
                return true;
            } catch (Throwable ignored) { }
        }
        return false;
    }
    public static boolean isJSONArray(String str) {
        if (!(null == str || str.length() == 0) && (str = str.trim()).length() > 0 && (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']')) {
            try {
                parseJSONArray(str);
                return true;
            } catch (Throwable ignored) { }
        }
        return false;
    }



    public static JSONObject toJSONObject(Map<String, Object> map) {
        if (null == map)
            return null;
        return new JSONObject((Map<?, ?>) map);
    }
    public static JSONArray toJSONArrayFromArray(Object array) {
        if (null == array)
            return null;
        return new JSONArray(array);
    }
    public static JSONArray toJSONArray(List map) {
        if (null == map)
            return null;

        return new JSONArray((Collection<?>) map);
    }
    public static JSONArray toJSONArray(Iterable map) {
        if (null == map)
            return null;

        return new JSONArray((Iterable<?>) map);
    }





    static Object jsonAsJavaObject(Object object) {
        if (object instanceof JSONObject) {
            return toMapOrList((JSONObject)object);
        } else if (object instanceof JSONArray) {
            return toMapOrList((JSONArray)object);
        }
        return object;
    }
    public static Map<String, Object> toMapOrList(JSONObject json) {
        if (null == json)
            return null;
        Map<String, Object> cloneMap = new LinkedHashMap<>();
        Map<String, Object> inner = json.getInnerMap();
        for (String k:  inner.keySet()) {
            cloneMap.put(k, jsonAsJavaObject(inner.get(k)));
        }
        return cloneMap;
    }
    public static List<Object> toMapOrList(JSONArray json) {
        if (null == json)
            return null;
        List<Object> cloneMap = new ArrayList<>();
        List<Object> inner = json.getInnerList();
        for (int i = 0; i < inner.size();i++) {
            cloneMap.add(jsonAsJavaObject(inner.get(i)));
        }
        return cloneMap;
    }

}

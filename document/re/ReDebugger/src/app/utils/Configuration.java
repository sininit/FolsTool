package app.utils;

import app.utils.lock.JsonProperties;
import app.utils.swing.input.InputConfigBox;
import com.google.gson.Gson;
import top.fols.atri.assist.json.JSON;
import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.io.file.Filex;
import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Strings;

import java.io.File;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"SynchronizeOnNonFinalField"})
public abstract class Configuration {
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    Object lock;
    JsonProperties properties;

    public Configuration(File configFile) {
        this.lock = new Object();
        this.configFile = configFile;
        this.properties = new JsonProperties(this.configFile, Charset.forName(DEFAULT_CHARSET_NAME));
    }

    private final File configFile;
    public  File getConfigFile() {
        return this.configFile;
    }


    public abstract MyConfigurationKeys myConfigurationKeys();


    public String get(String key) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            return properties.getString(key);
        }
    }
    public String get(String key, String defaultValue) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            if (!properties.containsKey(key)){
                return defaultValue;
            }
            return properties.getString(key);
        }
    }

    public boolean contains(String key) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            return properties.containsKey(key);
        }
    }
    public void set(String key, String value) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            properties.put(key, value);
            properties.save();
        }
        this.forCallback();
    }
    public void remove(String key) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            properties.remove(key);
            properties.save();
        }
        this.forCallback();
    }


    public void setGson(String key, Object value) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            properties.put(key, new Gson().toJson(value));
            properties.save();
        }
        this.forCallback();
    }
    public <T> T getGson(String key, Class<T> type) {
        return getGson(key, type, null);
    }
    public <T> T getGson(String key, Class<T> type, T defaultValue) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            if (!properties.containsKey(key)) {
                return defaultValue;
            }
            Object json = properties.get(key);
            String jsonString = Strings.cast(json);
            if (JSON.isJSON(jsonString)) {
                return new Gson().fromJson(jsonString, type);
            } else {
                return (T) json;
            }
        }
    }






    public Map<String, String> toMap() {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            LinkedHashMap<String, String> values = new LinkedHashMap<>();
            for (String key : properties.keySet()) {
                Object pv = properties.get(key);
                values.put(key, null == pv ? null : String.valueOf(pv));
            }
            return values;
        }
    }
    public void fromMap(Map<String, String> map) {
        synchronized (this.lock) {
            boolean success = false;
            JsonProperties properties = this.properties;
            for (String key : map.keySet()) {
                String pv = map.get(key);
                Object ov = properties.get(key);
                if (!Objects.equals(pv, ov)){
                    success = true;
                }
                properties.put(key, pv);
            }
            if (success) {
                properties.save();
            }
        }
        this.forCallback();
    }







    Set<Objects.CallbackOneParam<Configuration>> callbackSet = new LinkedHashSet<>();
    public Configuration addCallback(Objects.CallbackOneParam<Configuration> callback) {
        if (null != callback) {
            callbackSet.add(callback);
        }
        return this;
    }
    public void forCallback() {
        for (Objects.CallbackOneParam<Configuration> callback : callbackSet) {
            try {
                callback.callback(this);
            } catch (Throwable ignored) {}
        }
    }







    @Override
    public String toString() {
        return this.properties.toString();
    }
    public void fromString(String string) {
        synchronized (this.lock) {
            JsonProperties properties = this.properties;
            JsonProperties parse = new JsonProperties(new JSONObject(string));
            for (String key : parse.keySet()) {
                Object pv = parse.get(key);
                properties.put(key, null == pv ? null : String.valueOf(pv));
            }
            properties.save();
        }
    }




    static String getClassSName(Class<?> cls) {
        try {
            String canonical = cls.getName();
            int lastIndex = canonical.lastIndexOf(".");
            if (lastIndex == -1) {
                return canonical;
            } else {
                return canonical.substring(lastIndex + 1, canonical.length());
            }
        } catch (Throwable e) {
            return Classz.getSimpleName(cls);
        }
    }



    public static class ConfigurationConstructor {
        File parent;
        Map<Class<?>, Configuration> CLASS_CONFIGURATION_MAP = new ConcurrentHashMap<>();

        public ConfigurationConstructor(File parent) {
            this.parent = parent;
        }
        public Configuration getConfiguration(Class<?> path, MyConfigurationKeys keys) {
            if (null == path) { throw new NullPointerException("class"); }

            Configuration cache = CLASS_CONFIGURATION_MAP.get(path);
            if (null == cache) {
                String sub_dir  = path.getPackage().getName().replace(".", Filex.system_separators); // java package to sub dir
                String sub_name = getClassSName(path) + Filex.FILE_EXTENSION_NAME_SEPARATOR + "json";

                File dir;
                dir = new File(parent, sub_dir);
                dir.mkdirs();

                File file = new File(dir, sub_name);

                cache = new Configuration(file) {
                    @Override
                    public MyConfigurationKeys myConfigurationKeys() {
                        return keys;
                    }
                };
                CLASS_CONFIGURATION_MAP.put(path, cache);
            }
            return cache;
        }
    }
    public static ConfigurationConstructor newConfigurationConstructor(File parent) { return new ConfigurationConstructor(parent); }




    public void edit(String title) {
        edit(title, toMap());
    }
    public void edit(String title, Map<String, String> defaultData) {
        InputConfigBox.newInstance(title, this.myConfigurationKeys(), defaultData)
                .setConfirmAbstractAction(param -> {
                    for (String key: param.keySet()) {
                        set(key, param.get(key));
                    }
                })
                .create();
    }




    public <T> Element<T> createElement(String bind, Class<T> type) {
        return new Element<>(this, bind, type);
    }
    public static class Element<T> {
        public Element(Configuration configuration, String bind, Class<T> type) {
            this.configuration = configuration;
            this.bind = bind;
            this.type = type;
        }

        private final Configuration configuration;
        private final Class<T>        type;
        private final String          bind;



        public String key() { return bind; }


        public String   get() { return configuration.get(bind); }
        public void     set(String value) { configuration.set(bind, value); }

        public boolean  getBoolean() { return Objects.get_boolean(configuration.get(bind)); }
        public boolean  getBoolean(boolean defaultValue) {
            String value = configuration.get(bind);
            return Objects.empty(value)?defaultValue: Objects.get_boolean(value);
        }
        public void     setBoolean(boolean value) { configuration.set(bind, String.valueOf(value)); }

        public long  getLong() { return Objects.get_long(configuration.get(bind)); }
        public long  getLong(long defaultValue) {
            String value = configuration.get(bind);
            return Objects.empty(value)?defaultValue: Objects.get_long(value);
        }
        public void  setLong(long value) { configuration.set(bind, String.valueOf(value)); }

        public double  getDouble() { return Objects.get_double(configuration.get(bind)); }
        public double  getDouble(double defaultValue) {
            String value = configuration.get(bind);
            return Objects.empty(value)?defaultValue: Objects.get_double(value);
        }
        public void  setLong(double value) { configuration.set(bind, String.valueOf(value)); }



        public T getGson() {
            return configuration.getGson(bind, type);
        }
        public T getGson(T defaultValue) { return configuration.getGson(bind, type, defaultValue); }

        public void setGson(T value) { configuration.setGson(bind, value); }
    }


}

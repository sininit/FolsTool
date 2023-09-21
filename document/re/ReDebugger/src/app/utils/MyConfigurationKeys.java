package app.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class MyConfigurationKeys implements Cloneable {
    /**
     * Map<name, remarks>
     */
    Map<String, Element> map = new LinkedHashMap<>();

    public static class Element {
        private String remarks;

        public String getRemarks() { return remarks; }
        protected void setRemarks(String remarks) { this.remarks = remarks; }

        public String getDefaultValue() { return defaultValue; }
        protected void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

        private String defaultValue;

        public Element(String remarks) {
            this.remarks = remarks;
        }
        public Element(String remarks, String defaultValue) {
            this.remarks = remarks;
            this.defaultValue = defaultValue;
        }
    }




    public boolean contains(String key) {
        return this.map.containsKey(key);
    }

    public Set<String> keySet() {
        return Collections.unmodifiableSet(this.map.keySet());
    }


    public Element element(String key) {
        return this.map.get(key);
    }
    public String getRemarks(String key) {
        Element element = this.map.get(key);
        return null == element?null:element.getRemarks();
    }
    public String getDefaultValue(String key) {
        Element element = this.map.get(key);
        return null == element?null:(null == element.getDefaultValue()?"":element.getDefaultValue());
    }



    public Map<String, Element> getMap() {
        return Collections.unmodifiableMap(map);
    }


    protected void add(String key) { this.add(key, (Element) null);}
    protected void add(String key, String remark) { this.add(key, new Element(remark)); }
    protected void add(String key,
                       String remark,
                       String defaultValue) { this.add(key, new Element(remark, defaultValue)); }
    protected void add(String key, Element remark) {
//        if (!this.map.containsKey(key)) {
            this.map.put(key, remark);
//        }
    }
    protected void addAll(MyConfigurationKeys myConfigurationKeys) {
        Map<String, Element> data = myConfigurationKeys.map;
        for (String k: data.keySet()) {
            Element value = data.get(k);
            this.add(k, value);
        }
    }
    protected void remove(String key) {
        this.map.remove(key);
    }

    public int size(){ return this.map.size(); }

    @Override
    public String toString() {
        return "MyConfigurationKeys{" +
                "map=" + map +
                '}';
    }
}

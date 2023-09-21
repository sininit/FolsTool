package top.fols.box.util;

import top.fols.box.array.ArrayObject;
import top.fols.atri.lang.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated
public class TabPrint {
	public String tag = "";
	public String start="{", end="}";
	public String assignment=": ";
	public String separator = ",";

	List<Object> list = new ArrayList<>();



	public static TabPrint wrap(Object object) {
		if (ArrayObject.wrapable(object)) {
			TabPrint tp = new TabPrint();
			tp.start = "["; tp.end = "]";

			@SuppressWarnings("rawtypes")
			ArrayObject arr = ArrayObject.wrap(object);
			for (int i = 0; i < arr.length(); i++) {
				Object eo = arr.objectValue(i);
				if (ArrayObject.wrapable(eo)) {
					tp.add(wrap(eo));
				} else {
					tp.add(eo);
				}
			}
			return tp;
		} else {
			return new TabPrint().add(object);
		}
	}




	public TabPrint() {
	}
	public TabPrint(String tag) {
		this.tag = tag;
	}


	public TabPrint add(Object line) {
		this.list.add(line);
		return this;
	}
	public TabPrint add(Object key, Object value) {
		return this.add(new StringBuilder().append(key).append(assignment).append(value));
	}
	public TabPrint addAll(Object... line) {
		if (null == line){
			return this;
		}
		for (Object value: line) {
			this.add(value);
		}
		return this;
	}
	public TabPrint addAll(Iterable<?> line) {
		if (null == line){
			return this;
		}
		for (Object value: line) {
			this.add(value);
		}
		return this;
	}
	public TabPrint addAll(Map<?,?> map) {
		if (null == map){
			return this;
		}
		for (Object key: map.keySet()) {
			Object value = map.get(key);
			this.add(key, value);
		}
		return this;
	}




	@Override
	public String toString() {
		// TODO: Implement this method
		StringBuilder str = new StringBuilder();
		str.append(tag);
		str.append(start);
		str.append(Strings.line(""));
		for (int i = 0, last = list.size() - 1;i < list.size(); i++) {
			Object value = list.get(i);
			str.append(
				Strings.line(
					Strings.tabs(value + (i <  last ?separator: "")), 
					""
				)
			);
		}
		str.append(end);
		return str.toString();
	} 


}

package top.fols.atri.util;

import java.util.ArrayList;
import java.util.List;
import top.fols.atri.array.ArrayObject;
import top.fols.atri.lang.Strings;

public class TabPrint {
	public String tag = "";
	public String start="{", end="}";
	public String assignment=": ";
	public String separator = ",";

	List<Object> list = new ArrayList<>();


	public static TabPrint wrap(Object array) {
		if (ArrayObject.wrapable(array)) {
			TabPrint tp = new TabPrint();
			tp.start = "["; tp.end = "]";
			ArrayObject arr = ArrayObject.wrap(array);
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
			return new TabPrint().add(array);
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

package top.fols.box.net.header;

import top.fols.atri.lang.Objects;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*


 在RFC 822的扩展BNF表示法中，Content-Type头字段值定义如下：
 Content-Type：= type“/”子类型* [“;” 参数]
 键入：=“application”/“audio”/“image”/“message”/“multipart”/“text”/“video”/ x-token
 x-token：= <随后的两个字符“X-”，没有任何中间空格，由任何标记>
 子类型=令牌
 参数：=属性“=”值
 属性：=标记
 值：=标记/引用字符串
 token：= 1 *
 tspecials：=“（”/“）”/“<”/“>”/“@”; 必须在/“，”/“;” /“：”/“\”/ <“>; quoted-string，/”/“/”[“/”]“/”？“/”。“;用于/”=“;参数值




 text/html  ：HTML格式
 text/plain ：纯文本格式      
 text/xml   ：XML格式

 image/gif  ：gif图片格式    
 image/jpeg ：jpg图片格式 
 image/png  ：png图片格式

 application/xml     ： XML数据格式
 application/json    ： JSON数据格式
 application/pdf     ： pdf格式  
 application/msword  ： Word文档格式
 application/octet-stream ： 二进制流数据（如文件下载）

 application/x-www-form-urlencoded ： 

 <form encType="">中默认的encType，
 form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）。
 服务器收到的raw body会是，name=aaa&key=bbb。

 multipart/form-data ： 表单上传文件

 */
@Deprecated
@SuppressWarnings({"ConstantConditions", "StatementWithEmptyBody", "SimplifiableConditionalExpression"})
public class ContentType {

	public static final String TYPE_TEXT_PLAIN = "text/plain";
	public static final String TYPE_TEXT_HTML  = "text/html";

	public static final String TYPE_APPLICATION_JSON         = "application/json";
	public static final String TYPE_APPLICATION_OCTET_STREAM = "application/octet-stream";



	public static final String SEPARATOR = ";";

	public static class Value {
		String property;
		String value;

		public Value(String property, String value) {
			this.property = property;
			this.value = value;
		}

		@Override
		public int hashCode() {
			// TODO: Implement this method
			return 
				getClass().hashCode() + 
				Objects.hashCode(this.property);
		}

		@Override
		public boolean equals(Object obj) {
			// TODO: Implement this method
			if (obj == this) { return true; }
			if (obj instanceof Value) {
				Value no = (Value) obj;
				return
					Objects.equals(null == this.property ?null: this.property.toLowerCase(), null == no.property ?null: no.property.toLowerCase())
//					&& Objects.equals(this.tip, no.tip)
					;
			}
			return false;
		}

		@Override
		public String toString() {
			// TODO: Implement this method
			if (null == value) {
				return String.valueOf(property);
			} else {
				return property + "=" + value;
			}
		}

	}

	private String type = null;
	private String type_sub = null;

	private final List<Value> values = new ArrayList<>();



	public String type() { return this.type; }
	public ContentType type(String newType) { this.type = newType; return this; }

	public String type_sub() { return this.type_sub; }
	public ContentType type_sub(String newType) { this.type_sub = newType; return this; }



	public boolean hasValue(String p) {
		return values.contains(new Value(p, null));
	}
	public void putValue(String property, String value) {
		Value search = new Value(property, null);
		int index = values.indexOf(search);
		if (index == -1) {
			values.add(new Value(property, value));
		} else {
			Value  v = values.get(index);
			v.value = value;
		}
	}
	public  boolean removeValue(String property) {
		return values.remove(new Value(property, null));
	}
	public String getValue(String property) {
		int index = values.indexOf(new Value(property, null));
		if (index == -1) {
			return null;
		} else {
			Value  v = values.get(index);
			return v.value;
		}
	}
	public Map<String, String> getValues() {
		Map<String, String> values = new LinkedHashMap<>();
		for (Value obj: this.values) {
			values.put(obj.property, obj.value);
		}
		return values;
	}

	public boolean isOctetStreamType() { return isType("application", "octet-stream"); }


	public boolean isUrlencodedType() { return isType("application", "x-www-form-urlencoded"); }
	public boolean isJsonType() { return isType("application", "json"); }

	public boolean isHtmlType() { return isType("text", "html"); }
	public boolean isTextType() { return isType("text", "plain"); }


	public boolean isType(String type, String subType) {
		return
		    (null == type ?   true: type.equalsIgnoreCase(type()) &&
			(null == subType ?true: subType.equalsIgnoreCase(type_sub())));
	}




	public boolean hasCharset() 				{ return hasValue("charset"); }
	public String getCharset() 				{ return getValue("charset"); }
	public ContentType setCharset(String charset) 	{
		if (null == charset)	{
			removeValue("charset");
		} else {
			putValue("charset", charset); 
		}
		return this;
	}


	static void addElementToInstance(ContentType instance, String oneValue) {
		if (null == (oneValue = oneValue.trim()) || oneValue.length() == 0) {
		} else {
			String type_join = "/";
			int type_join_index = oneValue.indexOf(type_join);
			if (type_join_index != -1) {
				String name = oneValue.substring(0, type_join_index);
				String value = oneValue.substring(type_join_index + type_join.length(), oneValue.length());
				instance.type = name;
				instance.type_sub = value;
			} else {
				String assignment = "=";
				int assignmentIndex = oneValue.indexOf(assignment);
				if (assignmentIndex == -1) {
					String name = oneValue;
					String value = null;
					instance.putValue(name, value);
				} else {
					String name = oneValue.substring(0, assignmentIndex);
					String valu = oneValue.substring(assignmentIndex + assignment.length(), oneValue.length());
					instance.putValue(name, valu);
				}
			}
		}
	}
	public static ContentType parse(String content_type) {
		ContentType instance = new ContentType();
		if (null == content_type || content_type.trim().length() == 0) {
			return instance;
		}
		String[] splits = content_type.split(SEPARATOR);
		if (splits.length == 0) {
			addElementToInstance(instance, content_type);
		} else {
			for (String item: splits) {
				addElementToInstance(instance, item);
			}
		}
		return instance;
	}





	@Override
	public String toString() {
		// TODO: Implement this method
		if (null == type && null == type_sub && values.size() == 0) { return null; }

		String type_join = "/";
		String separator = "; ";

		StringBuilder builder = new StringBuilder();
		if (null != type && null != type_sub) {
			builder.append(type).append(type_join).append(type_sub).append(separator);
		}

		for (Object obj: values) {
			builder.append(obj.toString()).append(separator);
		}
		if (builder.length() > separator.length()) {
			builder.setLength(builder.length() - separator.length());
		}
		return builder.toString();
	}


	public static String getContentTypeForFileName(String name) {
		if (null == name) { return TYPE_APPLICATION_OCTET_STREAM; }
		try {
			return URLConnection.getFileNameMap().getContentTypeFor(name);
		} catch (Throwable e) {
			return TYPE_APPLICATION_OCTET_STREAM;
		}
	}


}

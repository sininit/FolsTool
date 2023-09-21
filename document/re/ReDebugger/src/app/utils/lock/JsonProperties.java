package app.utils.lock;

import top.fols.atri.assist.json.JSONArray;
import top.fols.atri.assist.json.JSONObject;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;

public class JsonProperties implements Cloneable {
	private static JSONObject load(File file, Charset charset) throws IOError {
		if (null == file) { return null; }
		File parent = file.getParentFile();
		if (null != parent) {
			if (!parent.canRead()) {
				throw new IOError(new IOException("permission denied: " + parent+", file: " + file));
			}
		}
		if (file.exists() && file.canRead()) {
			String string = new String(ExecutorLock.fileToChars(file, charset));
			if (string.length() == 0) { return new JSONObject(); }
			try {
				JSONObject jsonObject = new JSONObject(string);
				return jsonObject;
			} catch (Throwable throwable) {
				throw new IOError(new IOException("file: " + file+", message: "+throwable.getMessage()));
			}
		} else {
			return null;
		}
	}
	private static boolean write(File file, JSONObject jsonObject, Charset charset) throws IOError, RuntimeException {
		if (null == file) { throw new NullPointerException("file"); }
		File parent = file.getParentFile();
		if (null != parent) {
			if (!parent.canWrite()) {
				throw new IOError(new IOException("permission denied: " + parent+", file: " + file));
			}
		}
		char[] chars = jsonObject.toFormatString().toCharArray();
		return ExecutorLock.charsToFile(file, chars, charset);
	}







	private File file;
	private Charset charset;
	private JSONObject values;

	public JsonProperties(File file) throws NullPointerException {
		this(file, StandardCharsets.UTF_8);
	}
	public JsonProperties(File file, Charset charset) throws NullPointerException {
		this.file = file;
		this.charset = charset;
		this.init(null);
	}
	public JsonProperties(JSONObject jsonObject) throws NullPointerException {
		this.file = null;
		this.charset = null;
		this.init(null == jsonObject ? new JSONObject() : jsonObject);
	}
	public JsonProperties(String jsonObject) throws NullPointerException {
		this.file = null;
		this.charset = null;
		this.init(null == jsonObject ? new JSONObject() : new JSONObject(jsonObject));
	}

	private boolean init(JSONObject jsonObject) {
		if (null == jsonObject) {
			if (null == this.file) {
				this.values = new JSONObject();
				return true;
			} else {
				JSONObject fileJson = this.load(this.file(), this.charset());
				this.values = null == fileJson ? new JSONObject() : fileJson;
				return true;
			}
		} else {
			this.values = jsonObject;
			return true;
		}
	}



	public Charset charset(){ return null == this.charset?Charset.defaultCharset():this.charset; }
	public File file() { return this.file; }
	public int length() { return this.values.length(); }


	public Object get(String key) {
		JSONObject json = this.values;
		return json.opt(key);
	}
	public String getString(String key) {
		JSONObject json = this.values;
		return json.optString(key, null);
	}
	public JSONObject getJSONObject(String key) {
		JSONObject json = this.values;
		return json.optJSONObject(key);
	}
	public JSONArray getJSONArray(String key) {
		JSONObject json = this.values;
		return json.optJSONArray(key);
	}
	
	public boolean containsKey(String key) {
		JSONObject json = this.values;
		return json.has(key);
	}

	public boolean remove(String key) {
		JSONObject json = this.values;
		json.remove(key);
		return !json.has(key);
	}

	public Object put(String key, Object value) {
		JSONObject json = this.values;
		return json.put(key, value);
	}
	public Object putString(String key, String value) {
		JSONObject json = this.values;
		return json.put(key, value);
	}
	public Object putJSONObject(String key, JSONObject value) {
		JSONObject json = this.values;
		return json.put(key, value);
	}
	public Object putJSONArray(String key, JSONObject value) {
		JSONObject json = this.values;
		return json.put(key, value);
	}


	public Set<String> keySet(){
		return this.values.keySet();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof JsonProperties)) return false;
		JsonProperties that = (JsonProperties) o;
		return Objects.equals(file, that.file) &&
				Objects.equals(charset, that.charset);
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		JSONObject json = this.values;
		return json.toFormatString();
	}
	@Override
	public int hashCode() {
		// TODO: Implement this method
		JSONObject json = this.values;
		return json.hashCode();
	}


	public boolean reload() {
		return this.init(null);
	}
	public boolean save() throws IOError {
		return save(this.file);
	}
	public boolean save(File file) throws IOError {
		return null != file && write(file, this.values, this.charset);
	}
}

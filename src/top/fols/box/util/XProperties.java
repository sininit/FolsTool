package top.fols.box.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import top.fols.box.io.XStream;
import top.fols.box.io.base.XCharArrayWriter;
import top.fols.box.io.base.XStringWriter;
import top.fols.box.util.encode.XHexEncoder;
import top.fols.box.util.interfaces.XInterfaceGetInnerMap;
import top.fols.box.util.interfaces.XInterfaceSetInnerMap;


/**
 * properties read and write key and value read and write will escape
 * \ 	>>	('\', '\')
 * = 	>>	('\', '=')
 * \n 	>>	('\', 'n')
 *
 * @author xiaoxinwangluo / https://github.com/xiaoxinwangluo
 */
public class XProperties implements Serializable, XInterfaceGetInnerMap<String, String>, XInterfaceSetInnerMap<String, String> {
    private static final long serialVersionUID = 1L;


    private Map<String, String> properties = new LinkedHashMap<>();

    public XProperties() {
        super();
    }

    public XProperties(Map<String, String> map) {
        this.putAll(map);
    }

    public XProperties(XProperties map) {
        this.putAll(map);
    }


    public XProperties loadString(CharSequence text) {
        this.readCharSequence0(text, 0, text.length());
        return this;
    }

    public XProperties loadChars(char[] text) {
        this.readChars0(text, 0, text.length);
        return this;
    }

    public boolean loadFileTry(File file, Charset charsetName) {
        return loadFileTry(file, charsetName);
    }

    public XProperties loadFile(File file, Charset charsetName) throws IOException {
        XProperties newProperties;
        top:
        try {
            newProperties = new XProperties();
            FileInputStream fis = new FileInputStream(file);
            newProperties.load(fis, charsetName);
            XStream.tryClose(fis);
        } catch (IOException e) {
            throw e;
        }
        this.putAll(newProperties.getInnerMap());
        newProperties = null;
        return this;
    }

    public boolean loadTry(InputStream is, Charset charsetName) {
        try {
            this.load(is, charsetName);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public XProperties load(InputStream is, Charset charsetName) throws IOException {
        this.load(new InputStreamReader(is, charsetName));
        return this;
    }

    public boolean loadTry(Reader isr) {
        try {
            this.load(isr);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public XProperties load(Reader isr) throws IOException {
        XCharArrayWriter charstream = new XCharArrayWriter();
        XStream.copy(isr, charstream);
        if (charstream.size() == 0) {
            return this;
        }
        char[] chars = charstream.getBuff();
        this.readChars0(chars, 0, charstream.size());
        charstream.close();
        return this;
    }


    private static final char escapeChar = '\\';
    private static final char valueSeparatorChar = '=';
    private static final char lineSeparatorChar = '\n';

    static char escapeCodeToContent(char code) {
        switch (code) {
//			case '=':
//			case '\\':
//				return code;
            case 'n':
                return '\n';
        }
//		throw new IOException(String.format("data read error, escape=%s index=%s, datalength=%s",
//										    (escapeChar + (index + 1 < length ? String.valueOf(chars[index + 1]) : "")),
//										    index, olength));
        return code;
    }

    static char contentToEscapeCode(char content) {
        switch (content) {
//			case '=':
//			case '\\':
//				return content;
            case '\n':
                return 'n';
        }
//		throw new IOException(String.format("data read error, escape=%s index=%s, datalength=%s",
//										    (escapeChar + (index + 1 < length ? String.valueOf(chars[index + 1]) : "")),
//										    index, olength));
        return content;
    }


    private void readChars0(char[] chars, int off, int len) {
        int previousLineIndex = off;
        int length = off + len;
        int fori;

        XCharArrayWriter keybuffered = new XCharArrayWriter();
        XCharArrayWriter valbuffered = new XCharArrayWriter();
        try {
            XCharArrayWriter nowbuffered = keybuffered;////
            for (fori = previousLineIndex; fori < length; fori++) {
                char ch = chars[fori];
                if (ch == escapeChar) {
                    if (fori + 1 < length) {
                        char next = chars[fori + 1];
                        nowbuffered.write(escapeCodeToContent(next));
                        fori++;
                        continue;
                    }
//					else {
//						nowbuffered.write(ch);
//					}
                } else if (ch == valueSeparatorChar) {
                    nowbuffered = valbuffered;
                } else if (ch == lineSeparatorChar) {
                    previousLineIndex = fori + 1;//next-line

                    String k = new String(keybuffered.getBuff(), 0, keybuffered.size());
                    String v = nowbuffered != valbuffered ? null : new String(valbuffered.getBuff(), 0, valbuffered.size());
                    this.properties.put(k, v);
                    keybuffered.releaseBuffer();
                    valbuffered.releaseBuffer();

                    nowbuffered = keybuffered;//set now buffered
                } else {
                    nowbuffered.write(ch);
                }
                // System.out.println(index);
            }
            if (previousLineIndex < length) {
                //last-line
                String k = new String(keybuffered.getBuff(), 0, keybuffered.size());
                String v = nowbuffered != valbuffered ? null : new String(valbuffered.getBuff(), 0, valbuffered.size());
                this.properties.put(k, v);
                keybuffered.releaseBuffer();
                valbuffered.releaseBuffer();
            }
        } finally {
            keybuffered.close();
            valbuffered.close();
        }
    }

    /**
     * @see XProperties#readChars0(char[], int, int);
     */
    private void readCharSequence0(CharSequence chars, int off, int len) {
        int previousLineIndex = off;
        int length = off + len;
        int fori;

        XCharArrayWriter keybuffered = new XCharArrayWriter();
        XCharArrayWriter valbuffered = new XCharArrayWriter();
        try {
            XCharArrayWriter nowbuffered = keybuffered;////
            for (fori = previousLineIndex; fori < length; fori++) {
                char ch = chars.charAt(fori);
                if (ch == escapeChar) {
                    if (fori + 1 < length) {
                        char next = chars.charAt(fori + 1);
                        nowbuffered.write(escapeCodeToContent(next));
                        fori++;
                        continue;
                    }
//					else {
//						nowbuffered.write(ch);
//					}
                } else if (ch == valueSeparatorChar) {
                    nowbuffered = valbuffered;
                } else if (ch == lineSeparatorChar) {
                    previousLineIndex = fori + 1;//next-line

                    String k = new String(keybuffered.getBuff(), 0, keybuffered.size());
                    String v = nowbuffered != valbuffered ? null : new String(valbuffered.getBuff(), 0, valbuffered.size());
                    this.properties.put(k, v);
                    keybuffered.releaseBuffer();
                    valbuffered.releaseBuffer();

                    nowbuffered = keybuffered;//set now buffered
                } else {
                    nowbuffered.write(ch);
                }
                // System.out.println(index);
            }
            if (previousLineIndex < length) {
                //last-line
                String k = new String(keybuffered.getBuff(), 0, keybuffered.size());
                String v = nowbuffered != valbuffered ? null : new String(valbuffered.getBuff(), 0, valbuffered.size());
                this.properties.put(k, v);
                keybuffered.releaseBuffer();
                valbuffered.releaseBuffer();
            }
        } finally {
            keybuffered.close();
            valbuffered.close();
        }
    }


    @Override
    public Map<String, String> getInnerMap() {
        return this.properties;
    }

    @Override
    public void setInnerMap(Map<String, String> innerMap) {
        this.properties = XObjects.requireNonNull(innerMap);
    }


    public Set<String> keySet() {
        return this.properties.keySet();
    }


    public Collection<String> values() {
        return this.properties.values();
    }


    public boolean containsKey(String key) {
        return this.properties.containsKey(key);
    }


    public String get(String key) {
        return this.properties.get(key);
    }

    public XProperties put(String key, String datas) {
        this.properties.put(key, datas);
        return this;
    }

    public XProperties putAll(Map<String, String> datas) {
        this.properties.putAll(datas);
        return this;
    }

    public XProperties putAll(XProperties datas) {
        this.properties.putAll(datas.properties);
        return this;
    }


    public int size() {
        return this.properties.size();
    }


    @SuppressWarnings("unchecked")
    public XProperties removeMatches(String regexKey) {
        XDoubleLinked<String> list = new XDoubleLinked<String>(null);
        for (String key : this.keySet()) {
            if (null == key) {
                continue;
            }
            if (Pattern.matches(regexKey, key)) {
                list.addNext(new XDoubleLinked<String>(key));
            }
        }
        while (list.hasNext()) {
            String key = ((XDoubleLinked<String>) list.getNext()).content();
            list.getNext().remove();
            this.remove(key);
        }
        return this;
    }

    public XProperties remove(String key) {
        this.properties.remove(key);
        return this;
    }

    public XProperties clear() {
        this.properties.clear();
        return this;
    }

    @Override
    public String toString() {
        // TODO: Implement this method
        return this.properties.toString();
    }


    public static String getBackupFilePath(String file) {
        String backupPath = String.format("%s.bak", file);
        return backupPath;
    }

    public static File getBackupFile(File file) {
        File backupPath = new File(String.format("%s.bak", file.getPath()));
        return backupPath;
    }


    public String saveToString() throws NullPointerException, RuntimeException {
        XStringWriter writer = new XStringWriter();
        try {
            this.save(writer);
            writer.flush();
        } catch (NullPointerException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }


    public boolean saveToFileTry(File file, Charset charset) {
        return this.saveToFileTry(file, charset, false);
    }

    public boolean saveToFileTry(File file, Charset charset, boolean backup) {
        try {
            saveToFile(file, charset, backup);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public XProperties saveToFile(File file, Charset charset) throws IOException {
        return this.saveToFile(file, charset, false);
    }

    public XProperties saveToFile(File file, Charset charset, boolean backup) throws IOException {
        String path = file.getAbsolutePath();
        if (backup) {
            String backupPath = getBackupFilePath(path);
            new File(backupPath).delete();// 删除备份文件
            new File(path).renameTo(new File(backupPath));// 将原文件重命名为备份文件
        }
        Writer fos = new OutputStreamWriter(new FileOutputStream(file), charset);
        this.save(fos);
        fos.flush();
        fos.close();
        return this;
    }

    public XProperties save(OutputStream out, String charsetName) throws IOException {
        return this.save(new OutputStreamWriter(out, charsetName));
    }

    public XProperties save(Writer out) throws IOException, NullPointerException {
        if (this.properties.size() == 0) {
            return this;
        }

        BufferedWriter bufferedWriter = new BufferedWriter(out);

        XCharArrayWriter keybuffered = new XCharArrayWriter();
        XCharArrayWriter valbuffered = new XCharArrayWriter();
        try {
            Iterator<String> keyIterator = this.properties.keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                if (null == key) {
                    throw new NullPointerException("contains null key");
                }
                String value = this.properties.get(key);

                {
                    int length = key.length();
                    for (int i = 0; i < length; i++) {
                        char ch = key.charAt(i);
                        if (ch == escapeChar ||
                                ch == valueSeparatorChar ||
                                ch == lineSeparatorChar) {
                            keybuffered.append(escapeChar);
                            keybuffered.append(contentToEscapeCode(ch));
                        } else {
                            keybuffered.append(ch);
                        }
                    }
                    bufferedWriter.write(keybuffered.getBuff(), 0, keybuffered.size());
                    keybuffered.releaseBuffer();
                }

                if (null != value) {
                    bufferedWriter.write(valueSeparatorChar);

                    {
                        int length = value.length();
                        for (int i = 0; i < length; i++) {
                            char ch = value.charAt(i);
                            if (ch == escapeChar ||
                                    ch == valueSeparatorChar ||
                                    ch == lineSeparatorChar) {
                                valbuffered.append(escapeChar);
                                valbuffered.append(contentToEscapeCode(ch));
                            } else {
                                valbuffered.append(ch);
                            }
                        }
                        bufferedWriter.write(valbuffered.getBuff(), 0, valbuffered.size());
                        valbuffered.releaseBuffer();
                    }

                }

                if (keyIterator.hasNext()) {
                    bufferedWriter.write(lineSeparatorChar);
                }
            }
            bufferedWriter.flush();
        } finally {
            keybuffered.close();
            valbuffered.close();
        }
        return this;
    }


    // Object[] byte[] long[] double[] char[] int[] boolean[] float[] short[]
    public byte getByte(String key) {
        String value = this.get(key);
        return null == value ? (byte) 0 : XObjects.tobyte(value);
    }

    public void putByte(String key, byte value) {
        this.put(key, String.valueOf(value));
    }


    public long getLong(String key) {
        String value = this.get(key);
        return null == value ? (long) 0 : XObjects.tolong(value);
    }

    public void putLong(String key, long value) {
        this.put(key, String.valueOf(value));
    }

    public double getDouble(String key) {
        String value = this.get(key);
        return null == value ? (double) 0 : XObjects.todouble(value);
    }

    public void putDouble(String key, double value) {
        this.put(key, String.valueOf(value));
    }


    public char getChar(String key) {
        String value = this.get(key);
        return null == value ? (char) 0 : XObjects.tochar(value);
    }

    public void putChar(String key, char value) {
        this.put(key, String.valueOf(value));
    }

    public int getInt(String key) {
        String value = this.get(key);
        return null == value ? (int) 0 : XObjects.toint(value);
    }

    public void putInt(String key, int value) {
        this.put(key, String.valueOf(value));
    }

    public boolean getBoolean(String key) {
        String value = this.get(key);
        return null == value ? false : XObjects.toboolean(value);
    }

    public void putBoolean(String key, boolean value) {
        this.put(key, String.valueOf(value));
    }


    public float getFloat(String key) {
        String value = this.get(key);
        return null == value ? (float) 0 : XObjects.tofloat(value);
    }

    public void putFloat(String key, float value) {
        this.put(key, String.valueOf(value));
    }

    public short getShort(String key) {
        String value = this.get(key);
        return null == value ? (short) 0 : XObjects.toshort(value);
    }

    public void putShort(String key, short value) {
        this.put(key, String.valueOf(value));
    }


    public byte[] getBytes(String key) {
        String value = this.get(key);
        return null == value ? null : XHexEncoder.decode(value);
    }

    public XProperties putBytes(String key, byte[] bytes) {
        String hex = null == bytes ? null : XHexEncoder.encodeToString(bytes);
        this.put(key, hex);
        return this;
    }


    public String[] keyArray() {
        return this.keySet().toArray(new String[this.size()]);
    }

    public String[] valueArray() {
        return this.values().toArray(new String[this.size()]);
    }


}





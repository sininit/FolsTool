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
import top.fols.box.io.base.XStringReader;
import top.fols.box.io.base.XStringWriter;
import top.fols.box.util.encode.XBase64Encoder;
import top.fols.box.util.interfaces.XInterfaceGetInnerMap;

/**
 * properties read and write key and value read and write will escape (\ >> '\',
 * '\'), (= >> '\', '='), (\n >> '\', 'n')
 *
 * @author xiaoxinwangluo / https://github.com/xiaoxinwangluo
 */
public class XProperties implements Serializable, XInterfaceGetInnerMap {
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


    public boolean loadStringTry(String text) {
        try {
            this.loadString(text);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    public XProperties loadString(String text) throws IOException {
        XStringReader reader = new XStringReader(text);
        this.load(reader);
        reader.close();
        return this;
    }


    public boolean loadFileTry(File file, Charset charsetName) {
        try {
            this.loadFile(file, charsetName);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    public XProperties loadFile(File file, Charset charsetName) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        this.load(fis, charsetName);
        fis.close();
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
        final char escapeChar = '\\';
        final char valueSeparatorChar = '=';

        final char lineSeparatorChar = '\n';
        final char lineSeparatorEscapeCodeChar = 'n';

        XCharArrayWriter charstream = new XCharArrayWriter();
        XStream.copy(isr, charstream);
        if (charstream.size()  == 0) { return this; } //
        charstream.write(lineSeparatorChar);

        char[] chars = charstream.getBuff();
        int previousLineIndex = 0;
        int length = charstream.size();
        int index = 0;

        XCharArrayWriter keybuffered = new XCharArrayWriter();
        XCharArrayWriter valbuffered = new XCharArrayWriter();

        try {
            while (!(index + 1 > length)) {
                XCharArrayWriter nowbuffered = keybuffered;
                for (index = previousLineIndex; index < length; index++) {
                    char ch = chars[index];
                    if (ch == escapeChar) {
                        if (index + 1 < length) {
                            if (chars[index + 1] == escapeChar) {
                                nowbuffered.write(escapeChar);
                                index++;
                                continue;
                            } else if (chars[index + 1] == valueSeparatorChar) {
                                nowbuffered.write(escapeChar);
                                index++;
                                continue;
                            } else if (chars[index + 1] == lineSeparatorEscapeCodeChar) {
                                nowbuffered.write(escapeChar);
                                index++;
                                continue;
                            } else {
                                throw new IOException(String.format(
                                        "data read error, escape=%s index=%s, datalength=%s",
                                        (escapeChar + (index + 1 < length ? String.valueOf(chars[index + 1]) : "")),
                                        index, length));
                            }
                        } else {
                            throw new IOException(String.format("data read error, escape=%s index=%s, datalength=%s",
                                    (escapeChar + (index + 1 < length ? String.valueOf(chars[index + 1]) : "")),
                                    index, length));
                        }
                    } else if (ch == valueSeparatorChar) {
                        nowbuffered = valbuffered;
                        continue;
                    } else if (ch == lineSeparatorChar) {
                        previousLineIndex = index + 1;

                        int keylen = keybuffered.size();
                        int vallen = valbuffered.size();

                        String k = new String(keybuffered.getBuff(), 0, keylen);
                        String v = nowbuffered != valbuffered ? null: new String(valbuffered.getBuff(), 0, vallen);

                        keybuffered.releaseBuffer();
                        valbuffered.releaseBuffer();

                        this.properties.put(k, v);

                        break;
                    } else {
                        nowbuffered.write(ch);
                    }
                    // System.out.println(index);
                }

                keybuffered.releaseBuffer();
                valbuffered.releaseBuffer();
            }
        } finally {
            keybuffered.close();
            valbuffered.close();
            charstream.close();
        }
        return this;
    }

    @Override
    public Map<String, String> getInnerMap() {
        return this.properties;
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
        for (String key : this.properties.keySet()) {
            if (null == key) { continue; }
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
        File backupPath = new File(String.format("%s.bak", file));
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

    public XProperties save(Writer out) throws IOException,NullPointerException {
        final char escapeChar = '\\';
        final char valueSeparatorChar = '=';

        final char lineSeparatorChar = '\n';
        final char lineSeparatorEscapeCodeChar = 'n';

        if (this.properties.size() == 0) { return this; }

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
                        if (ch == escapeChar || ch == valueSeparatorChar) {
                            keybuffered.append(escapeChar);
                            keybuffered.append(ch);
                        } else if (ch == lineSeparatorChar) {
                            keybuffered.append(escapeChar);
                            keybuffered.append(lineSeparatorEscapeCodeChar);
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
                            if (ch == escapeChar || ch == valueSeparatorChar) {
                                valbuffered.append(escapeChar);
                                valbuffered.append(ch);
                            } else if (ch == lineSeparatorChar) {
                                valbuffered.append(escapeChar);
                                valbuffered.append(lineSeparatorEscapeCodeChar);
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
        return null == value ?(byte)0: XObjects.tobyte(value);
    }

    public long getLong(String key) {
        String value = this.get(key);
        return null == value ?(long)0: XObjects.tolong(value);
    }

    public double getDouble(String key) {
        String value = this.get(key);
        return null == value ?(double)0: XObjects.todouble(value);
    }

    public char getChar(String key) {
        String value = this.get(key);
        return null == value ?(char)0: XObjects.tochar(value);
    }

    public int getInt(String key) {
        String value = this.get(key);
        return null == value ?(int)0: XObjects.toint(value);
    }

    public boolean getBoolean(String key) {
        String value = this.get(key);
        return null == value ?false: XObjects.toboolean(value);
    }

    public float getFloat(String key) {
        String value = this.get(key);
        return null == value ?(float)0: XObjects.tofloat(value);
    }

    public short getShort(String key) {
        String value = this.get(key);
        return null == value ?(short)0: XObjects.toshort(value);
    }

    public byte[] getBytes(String key) {
        String value = this.get(key);
        return null == value ?null: XBase64Encoder.getDecoder().decode(value);
    }


    public XProperties putBytes(String key, byte[] bytes) {
        String base64 = null == bytes ?null: XBase64Encoder.getEncoder().encodeToString(bytes);
        this.put(key, base64);
        return this;
    }









    public String[] keyArray() {
        return this.keySet().toArray(new String[this.size()]);
    }

    public String[] valueArray() {
        return this.values().toArray(new String[this.size()]);
    }


}




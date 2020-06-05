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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import top.fols.box.io.XStream;
import top.fols.box.io.base.XCharArrayWriter;
import top.fols.box.io.base.XInputStreamFixedLength;
import top.fols.box.io.base.XReaderFixedLength;
import top.fols.box.util.encode.XBase64Encoder;
import top.fols.box.util.interfaces.XInterfaceGetOriginMap;

/**
 * properties read and write key and value read and write will escape (\ >> '\',
 * '\'), (= >> '\', '='), (\n >> '\', 'n')
 *
 * @author xiaoxinwangluo / https://github.com/xiaoxinwangluo
 */
public class XProperties implements Serializable, XInterfaceGetOriginMap{
    private static final long serialVersionUID = 1L;
    
    
    private Map<String, String> properties = new LinkedHashMap<>();

    public XProperties() {
        super();
    }

    public XProperties(Map<String, String> map) {
        this.putAll(map);
    }

    public boolean loadFileTry(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            this.load(fis);
            fis.close();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public XProperties loadFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        this.load(fis);
        fis.close();
        return this;
    }

    public XProperties load(InputStream is, long readlimit) throws IOException {
        return this.load(new InputStreamReader(new XInputStreamFixedLength<InputStream>(is, readlimit)));
    }

    public XProperties load(InputStream is, long readlimit, String charsetName) throws IOException {
        return this.load(new InputStreamReader(new XInputStreamFixedLength<InputStream>(is, readlimit), charsetName));
    }

    public XProperties load(InputStream is) throws IOException {
        this.load(new InputStreamReader(is));
        return this;
    }

    public XProperties load(InputStream is, String charsetName) throws IOException {
        this.load(new InputStreamReader(is, charsetName));
        return this;
    }

    public XProperties load(Reader is, long readlimit) throws IOException {
        return this.load(new XReaderFixedLength<Reader>(is, readlimit));
    }

    public XProperties load(Reader isr) throws IOException {
        final char escapeCharByte = '\\';
        final char valueSeparatorCharByte = '=';
        final char lineSeparatorCharByte = '\n', //
            lineSeparatorCodeCharByte = 'n';

        XCharArrayWriter allw = new XCharArrayWriter();
        XStream.copy(isr, allw);
        allw.write(lineSeparatorCharByte);
        char[] all = allw.getBuff();

        int previousLineIndex = 0;
        int length = allw.size();
        int index = 0;

        XCharArrayWriter keybuffered = new XCharArrayWriter();
        XCharArrayWriter valbuffered = new XCharArrayWriter();
        try {
            while (true) {
                if (index + 1 >= length) {
                    break;
                }
                boolean isWriteKey = true;
                for (index = previousLineIndex; index < length; index++) {
                    char ch = all[index];
                    if (ch == escapeCharByte) {
                        if (index + 1 < length) {
                            if (all[index + 1] == escapeCharByte) {
                                if (isWriteKey) {
                                    keybuffered.write(escapeCharByte);
                                } else {
                                    valbuffered.write(escapeCharByte);
                                }
                                index++;
                                continue;
                            } else if (all[index + 1] == valueSeparatorCharByte) {
                                if (isWriteKey) {
                                    keybuffered.write(valueSeparatorCharByte);
                                } else {
                                    valbuffered.write(valueSeparatorCharByte);
                                }
                                index++;
                                continue;
                            } else if (all[index + 1] == lineSeparatorCodeCharByte) {
                                if (isWriteKey) {
                                    keybuffered.write(lineSeparatorCharByte);
                                } else {
                                    valbuffered.write(lineSeparatorCharByte);
                                }
                                index++;
                                continue;
                            } else {
                                throw new IOException(String.format(
                                        "data read error, escape=%s index=%s, datalength=%s",
                                        (escapeCharByte + (index + 1 < length ? String.valueOf(all[index + 1]) : "")),
                                        index, length));
                            }
                        } else {
                            throw new IOException(String.format("data read error, escape=%s index=%s, datalength=%s",
                                    (escapeCharByte + (index + 1 < length ? String.valueOf(all[index + 1]) : "")),
                                    index, length));
                        }
                    } else if (ch == valueSeparatorCharByte) {
                        isWriteKey = false;
                        continue;
                    } else if (ch == lineSeparatorCharByte) {
                        previousLineIndex = index + 1;

                        int keylen = keybuffered.size();
                        int vallen = valbuffered.size();

                        if (keylen == 0 && vallen == 0) {
                            break;
                        }

                        String k = new String(keybuffered.getBuff(), 0, keylen);
                        String v = new String(valbuffered.getBuff(), 0, vallen);

                        keybuffered.releaseBuffer();
                        valbuffered.releaseBuffer();

                        this.properties.put(k, v);
                        break;
                    } else {
                        if (isWriteKey) {
                            keybuffered.append(ch);
                        } else {
                            valbuffered.append(ch);
                        }
                    }
                    // System.out.println(index);
                }
                keybuffered.releaseBuffer();
                valbuffered.releaseBuffer();
            }
        } finally {
            keybuffered.close();
            valbuffered.close();
            allw.close();
        }
        return this;
    }

    @Override
    public Map<String, String> getMap() {
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

    // Object[] byte[] long[] double[] char[] int[] boolean[] float[] short[]
    public byte getByte(String key) {
        return this.get(key).getBytes()[0];
    }

    public long getLong(String key) {
        return XObjects.tolong(this.get(key));
    }

    public double getDouble(String key) {
        return XObjects.todouble(this.get(key));
    }

    public char getChar(String key) {
        return XObjects.tochar(this.get(key));
    }

    public int getInt(String key) {
        return XObjects.toint(this.get(key));
    }

    public boolean getBoolean(String key) {
        return XObjects.toboolean(this.get(key));
    }

    public float getFloat(String key) {
        return XObjects.tofloat(this.get(key));
    }

    public short getShort(String key) {
        return XObjects.toshort(this.get(key));
    }

    public byte[] getBytes(String key) {
        byte[] bytes = XBase64Encoder.getDecoder().decode(this.get(key));
        return bytes;
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

    public XProperties putString(String key, Object datas) {
        return this.put(key, null == datas ? "null" : datas.toString());
    }

    public XProperties putBytes(String key, byte[] bytes) {
        String base64 = XBase64Encoder.getEncoder().encodeToString(bytes);
        return this.put(key, base64);
    }

    public int size() {
        return this.properties.size();
    }

    public XProperties saveToFile(File file) throws IOException {
        return this.saveToFile(file, false);
    }

    public boolean saveToFileTry(File file, boolean backup) {
        try {
            saveToFile(file, backup);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public XProperties saveToFile(File file, boolean backup) throws IOException {
        String path = file.getAbsolutePath();
        if (backup) {
            String backupPath = String.format("%s.bak", path);
            new File(backupPath).delete();// 删除备份文件
            new File(path).renameTo(new File(backupPath));// 将原文件重命名为备份文件
        }
        FileOutputStream fos = new FileOutputStream(path);
        this.save(fos);
        fos.flush();
        fos.close();
        return this;
    }

    public XProperties save(OutputStream out) throws IOException {
        return this.save(new OutputStreamWriter(out));
    }

    public XProperties save(OutputStream out, String charsetName) throws IOException {
        return this.save(new OutputStreamWriter(out, charsetName));
    }

    public XProperties save(Writer out) throws IOException {
        final char escapeCharByte = '\\';
        final char valueSeparatorCharByte = '=';
        final char lineSeparatorCharByte = '\n', //
            lineSeparatorCodeCharByte = 'n';

        BufferedWriter bw = new BufferedWriter(out);

        XCharArrayWriter keybuffered = new XCharArrayWriter();
        XCharArrayWriter valbuffered = new XCharArrayWriter();
        try {
            Set<String> keys = this.properties.keySet();
            for (String key : keys) {
                String value = this.properties.get(key);

                {
                    int length = key.length();
                    for (int i = 0; i < length; i++) {
                        char ch = key.charAt(i);
                        if (ch == escapeCharByte || ch == valueSeparatorCharByte) {
                            keybuffered.append(escapeCharByte);
                            keybuffered.append(ch);
                        } else if (ch == lineSeparatorCharByte) {
                            keybuffered.append(escapeCharByte);
                            keybuffered.append(lineSeparatorCodeCharByte);
                        } else {
                            keybuffered.append(ch);
                        }
                    }
                    bw.write(keybuffered.getBuff(), 0, keybuffered.size());
                    keybuffered.releaseBuffer();
                }

                bw.write(valueSeparatorCharByte);

                {
                    int length = value.length();
                    for (int i = 0; i < length; i++) {
                        char ch = value.charAt(i);
                        if (ch == escapeCharByte || ch == valueSeparatorCharByte) {
                            valbuffered.append(escapeCharByte);
                            valbuffered.append(ch);
                        } else if (ch == lineSeparatorCharByte) {
                            valbuffered.append(escapeCharByte);
                            valbuffered.append(lineSeparatorCodeCharByte);
                        } else {
                            valbuffered.append(ch);
                        }
                    }
                    bw.write(valbuffered.getBuff(), 0, valbuffered.size());
                    valbuffered.releaseBuffer();
                }

                bw.write(lineSeparatorCharByte);
            }
            bw.flush();
        } finally {
            keybuffered.close();
            valbuffered.close();
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public XProperties removeMatches(String regexKey) {
        XDoubleLinked.VarLinked<String> rk = new XDoubleLinked.VarLinked<String>(null);
        for (String key : this.properties.keySet()) {
            if (Pattern.matches(regexKey, key)) {
                rk.addNext(new XDoubleLinked.VarLinked<String>(key));
            }
        }
        while (rk.hasNext()) {
            String key = ((XDoubleLinked.VarLinked<String>) rk.getNext()).content();
            rk.remove(rk.getNext());
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

}



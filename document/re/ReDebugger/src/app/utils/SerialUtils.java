package app.utils;

import top.fols.atri.lang.Classz;
import top.fols.atri.lang.Objects;
import top.fols.box.util.encode.HexEncoders;

import java.io.*;

public class SerialUtils {




    public static String toBase64(Object value) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            return HexEncoders.encodeToString(bytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }



    @SuppressWarnings("all")
    public static <T> T toValue(String str, Class<T> tClass) throws RuntimeException {
        try {
            if (Objects.empty(str)) { return null; }
            byte[] bytes = HexEncoders.decode(str);

            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
            Object value = objectInputStream.readObject();
            if (null == value) {
                return (T) value;
            } else {
                if (Classz.isInstanceNullable(value, tClass)) {
                    return (T) value;
                } else {
                    throw new ClassCastException(String.format("type %s cannot cast to %s", value.getClass(), tClass));
                }
            }
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Throwable e) {
            if (e instanceof InvalidObjectException) {
                if (e.getMessage().trim().startsWith("enum constant")) {
                    return null;
                }
            }
            throw new RuntimeException(e);
        }
    }


}

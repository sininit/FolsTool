package app.utils.reflects.interfaces;

import app.utils.reflects.ReflectUtils;
import top.fols.atri.reflect.Reflects;

import java.lang.reflect.Field;

public interface FromSet {

    default boolean fromSet(Object object) {
        if (null == object) {
            throw new NullPointerException("object");
        }
        try {
            Field[] fields = Reflects.accessible(ReflectUtils.cache.fields(getClass()));
            for (Field field: fields) {
                if (ReflectUtils.isIgnoreField(field)) {
                    continue;
                }

                Field field1 = ReflectUtils.cache.field(object.getClass(), field.getName());
                if (null == field1) {
                    continue;
                }

                Field  objectField      = Reflects.accessible(field1);
                Object objectFieldValue = objectField.get(object);

                field.set(this, objectFieldValue);
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

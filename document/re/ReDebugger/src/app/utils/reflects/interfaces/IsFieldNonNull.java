package app.utils.reflects.interfaces;

import app.utils.reflects.ReflectUtils;
import top.fols.atri.reflect.Reflects;

import java.lang.reflect.Field;


/**
 * 判断本实例所有字段是否都不为null  如果都不为null 则返回true.
 */
public interface IsFieldNonNull {

    default boolean isComplete() {
        Field[] fields = Reflects.accessible(ReflectUtils.cache.fields(getClass()));
        for (Field field: fields) {
            if (ReflectUtils.isIgnoreField(field)){
                continue;
            }
            try {
                Object o = field.get(this);
                if (null == o) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new Error(e); //ignored
            }
        }
        return true;
    }
}

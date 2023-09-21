package app.utils;

import top.fols.atri.lang.Objects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArrayUtils {
    public static <T extends Object> T[][] splitArray(
            T[] objects,
            int threadReceivingSize, //每个线程接接受数量
            int threadMaximunCount) { //最大线程数
        //实际上不需要创建太多的线程   只需要 按照规定线程数填充即可

        Class cClass = objects.getClass().getComponentType();

        int taskI = 0;
        List<List<T>> tasks = new ArrayList<>();
        for (int i = 0; i < threadMaximunCount; i++) {
            tasks.add(new ArrayList<T>());
        }
        int taskIndex = 0;

        ArrayPieceIndex apim = new ArrayPieceIndex(objects.length, threadReceivingSize);
        for (int i = 0; i < apim.getPieceCount(); i++) {
            List<T> list = tasks.get(taskIndex);

            int each = Math.toIntExact(apim.getPieceSize(i));
            for (int eachi = 0;eachi < each; eachi++) {
                list.add(objects[taskI++]);
            }

            if (taskIndex + 1 == threadMaximunCount) {
                taskIndex = 0;
            } else {
                taskIndex++;
            }
        }
        T[][] result = (T[][]) Array.newInstance(cClass, threadMaximunCount, 0);
        for (int i = 0; i < threadMaximunCount; i++) {
            List<T> list = tasks.get(i);
            if (!Objects.empty(list)) {
                result[i] = list.toArray((T[]) Array.newInstance(cClass, 0));
            } else {
                result = Arrays.copyOf(result, i);
                break;
            }
        }
        return result;
    }


}

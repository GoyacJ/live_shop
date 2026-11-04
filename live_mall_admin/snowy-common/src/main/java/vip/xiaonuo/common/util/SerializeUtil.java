package vip.xiaonuo.common.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SerializeUtil {

    /**
     * 序列化 list 集合
     *
     * @param list
     * @return
     */
    public static byte[] serializeList(List<?> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            for (Object obj : list) {
                oos.writeObject(obj);
            }
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(oos);
            close(baos);
        }
        return bytes;
    }

        /**
         * 反序列化 list 集合
         *
         * @param
         * @return
         */
        public static List<?> unserializeList(byte[] bytes){
            if (bytes == null) {
                return null;
            }
            List<Object> list = new ArrayList<Object>();
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                // 反序列化
                bais = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bais);
                while (bais.available() > 0) {
                    Object obj = (Object) ois.readObject();
                    if (obj == null) {
                        break;
                    }
                    list.add(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(bais);
                close(ois);
            }
            return list;
        }

        /**
         * 关闭io流对象
         *
         * @param closeable
         */
        public static void close(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    /**
     * 将一个对象转换为指定类型的列表。
     * 该方法用于将一个对象（假设其是一个列表）中的所有元素转换为指定类型。
     *
     * @param obj 要转换的对象
     * @param clazz 目标元素类型
     * @return 返回转换后的列表，如果对象不是列表则返回null
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 在list集合中随机取出指定数量的元素
     * @param list 取元素的集合
     * @param count 个数
     * @return
     */
    public static List<String> getRandomThreeInfoList(List<String> list, int count) {
        if (list == null || list.isEmpty() || count <= 0) {
            return new ArrayList<>();
        }
        if (count >= list.size()) {
            return new ArrayList<>(list);
        }
        List<String> copyList = new ArrayList<>(list);
        Collections.shuffle(copyList, new Random());
        return copyList.subList(0, count);
    }
}

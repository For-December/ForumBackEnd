package com.fordece.forum.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public interface BaseData {
    default <V> V asViewObject(Class<V> clazz, Consumer<V> consumer) {
        V v = this.asViewObject(clazz);
        consumer.accept(v);
        return v;
    }

    default <V> V asViewObject(Class<V> clazz) {
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            Constructor<V> constructor = clazz.getConstructor();
            V v = constructor.newInstance();
            for (Field declaredField : declaredFields) {
                convert(declaredField, v);
            }
            return v;

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private void convert(Field field, Object vo) {
        try {
            Field source;
            try {
                source = this.getClass().getDeclaredField(field.getName());
            } catch (NoSuchFieldException e) {
                source = this.getClass()
                        .getSuperclass()
                        .getDeclaredField(field.getName());
            } // 获取父类字段

            field.setAccessible(true);
            source.setAccessible(true);
            field.set(vo, source.get(this)); // 将当前对象字段的值设置给新对象
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // 新对象中有可能存在this里没有的字段
            if (!(e instanceof NoSuchFieldException)) {
                throw new RuntimeException(e);
            }
        }
    }
}

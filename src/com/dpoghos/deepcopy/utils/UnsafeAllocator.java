package com.dpoghos.deepcopy.utils;

import java.lang.reflect.Field;

class UnsafeAllocator {

    private static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to access Unsafe", e);
        }
    }

    public static <T> T createInstance(Class<T> clazz) throws InstantiationException {
        return (T) UNSAFE.allocateInstance(clazz);
    }
}
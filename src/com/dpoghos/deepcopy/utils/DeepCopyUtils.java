package com.dpoghos.deepcopy.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.dpoghos.deepcopy.exception.DeepCopyException;

public class DeepCopyUtils {

    public static <T> T deepCopy(T obj) {
        return Optional.ofNullable(obj)
            .map(o -> doDeepCopy(o, new IdentityHashMap<>()))
            .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static <T> T doDeepCopy(T obj, Map<Object, Object> visited) {
        if (isSimpleType(obj)) {
            return obj;
        }

        if (visited.containsKey(obj)) {
            return (T) visited.get(obj);
        }

        if (obj.getClass().isArray()) {
            return (T) copyArray(obj, visited);
        }

        if (obj instanceof Collection<?>) {
            return (T) copyCollection((Collection<?>) obj, visited);
        }

        if (obj instanceof Map<?, ?>) {
            return (T) copyMap((Map<?, ?>) obj, visited);
        }

        return copyObjectOrHandleNoDefaultConstructor(obj, visited);
    }

    private static boolean isSimpleType(Object obj) {
        return obj == null || obj.getClass().isPrimitive() || obj instanceof String || obj instanceof Enum;
    }

    private static Object copyArray(Object obj, Map<Object, Object> visited) {
        Class<?> componentType = obj.getClass().getComponentType();
        int length = Array.getLength(obj);
        Object copy = Array.newInstance(componentType, length);
        visited.put(obj, copy);
        for (int i = 0; i < length; i++) {
            Array.set(copy, i, doDeepCopy(Array.get(obj, i), visited));
        }
        return copy;
    }

    private static Collection<?> copyCollection(Collection<?> collection, Map<Object, Object> visited) {
        Collection<Object> copy = createCollectionInstance(collection);
        visited.put(collection, copy);
        for (Object item : collection) {
            copy.add(doDeepCopy(item, visited));
        }
        return copy;
    }

    private static Collection<Object> createCollectionInstance(Collection<?> collection) {
        if (collection instanceof List) {
            return new ArrayList<>();
        } else if (collection instanceof Set) {
            return new HashSet<>();
        } else if (collection instanceof Queue) {
            return new LinkedList<>();
        } else {
            throw new UnsupportedOperationException("Unsupported collection type: " + collection.getClass().getName());
        }
    }

    private static Map<?, ?> copyMap(Map<?, ?> map, Map<Object, Object> visited) {
        Map<Object, Object> copy = new HashMap<>();
        visited.put(map, copy);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object keyCopy = doDeepCopy(entry.getKey(), visited);
            Object valueCopy = doDeepCopy(entry.getValue(), visited);
            copy.put(keyCopy, valueCopy);
        }
        return copy;
    }

    @SuppressWarnings("unchecked")
    private static <T> T copyObjectOrHandleNoDefaultConstructor(T obj, Map<Object, Object> visited) {
        try {
            Constructor<?> targetConstructor = findMatchingConstructor(obj);
            if (targetConstructor != null) {
                Object[] fieldValues = getFieldValues(obj, visited);
                T copy = (T) targetConstructor.newInstance(fieldValues);
                visited.put(obj, copy);
                setDeepCopiedFields(obj, copy, visited);
                return copy;
            } else {
                return createCopyWithoutConstructor(obj, visited);
            }
        } catch (Exception e) {
            throw new DeepCopyException("Failed to create a deep copy of the object", e);
        }
    }

    private static Constructor<?> findMatchingConstructor(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == fields.length) {
                constructor.setAccessible(true);
                return constructor;
            }
        }
        return null;
    }

    private static <T> T createCopyWithoutConstructor(T obj, Map<Object, Object> visited) throws Exception {
        T copy = (T) UnsafeAllocator.createInstance(obj.getClass());
        visited.put(obj, copy);
        setDeepCopiedFields(obj, copy, visited);
        return copy;
    }

    private static Object[] getFieldValues(Object obj, Map<Object, Object> visited) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object[] fieldValues = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object fieldValue = fields[i].get(obj);
            fieldValues[i] = fields[i].getType().isPrimitive() ? fieldValue : doDeepCopy(fieldValue, visited);
        }
        return fieldValues;
    }

    private static void setDeepCopiedFields(Object obj, Object copy, Map<Object, Object> visited) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(obj);
            field.set(copy, field.getType().isPrimitive() ? fieldValue : doDeepCopy(fieldValue, visited));
        }
    }
}
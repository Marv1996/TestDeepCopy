import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class CopyUtils {

    public static <T> T deepCopy(T object) {
        if (object == null)
            return null;

        if (isImmutable(object.getClass()))
            return object;

        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            T newArray = (T) Array.newInstance(object.getClass().getComponentType(), length);
            for (int i = 0; i < length; i++) {
                Array.set(newArray, i, deepCopy(Array.get(object, i)));
            }
            return newArray;
        }

        if (object instanceof Collection<?>) {
            Collection<Object> myCollection = (Collection<Object>) object;
            Collection<Object> newCollection = (Collection<Object>) createCollectionInstance(myCollection);
            for (Object element : myCollection) {
                newCollection.add(deepCopy(element));
            }
            return (T) newCollection;
        }

        if (object instanceof Map<?, ?>) {
            Map<Object, Object> myMap = (Map<Object, Object>) object;
            Map<Object, Object> newMap = new HashMap<>();
            for (Map.Entry<Object, Object> entry : myMap.entrySet()) {
                newMap.put(deepCopy(entry.getKey()), deepCopy(entry.getValue()));
            }
            return (T) newMap;
        }

        try {
            T newObject = (T) object.getClass().getDeclaredConstructor().newInstance();
            for (Field field : getAllFields(object.getClass())) {
                field.setAccessible(true);
                field.set(newObject, deepCopy(field.get(object)));
            }
            return newObject;
        } catch (Exception e) {
            throw new RuntimeException("Unable to perform deep copy", e);
        }
    }

    private static <T> Collection<?> createCollectionInstance(Collection<T> collection) {
        if (collection instanceof List<?>) {
            return new ArrayList<>();
        } else if (collection instanceof Set<?>) {
            return new HashSet<>();
        } else if (collection instanceof Queue<?>) {
            return new LinkedList<>();
        } else {
            throw new IllegalArgumentException("Unsupported collection type: " + collection.getClass());
        }
    }

    private static boolean isImmutable(Class<?> myClass) {
        return myClass.isPrimitive() ||
                myClass == Boolean.class || myClass == Character.class ||
                Number.class.isAssignableFrom(myClass) || myClass == String.class ||
                myClass.isEnum();
    }

    private static List<Field> getAllFields(Class<?> myClass) {
        List<Field> fields = new ArrayList<>();
        while (myClass != null) {
            fields.addAll(Arrays.asList(myClass.getDeclaredFields()));
            myClass = myClass.getSuperclass();
        }
        return fields;
    }
}
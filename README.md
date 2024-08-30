# Deep Copy Utility

This utility provides a deep copy mechanism for objects of arbitrary structure and size in Java, without relying on `java.io.Serializable` or `java.lang.Cloneable`. The deep copy method allows for creating a new instance of an object with the same properties, fully independent of the original object, including handling nested structures like arrays, collections, and maps.

## Package

`com.dpoghos.deepcopy.utils`

## Main Class: `DeepCopyUtils`

The `DeepCopyUtils` class provides a static method `deepCopy` to perform deep copies of objects. It uses Java Reflection to traverse and copy complex object graphs, ensuring that any modifications to the copied object do not affect the original object.

### Main Functionalities

1. **`deepCopy(T obj)`**:
    - This is the public API method that performs a deep copy of the given object.
    - If the object is `null`, it returns `null`.
    - If the object is a primitive, `String`, or `Enum`, it returns the object itself since these types are immutable.
    - For other types, it delegates the copying process to `doDeepCopy`.

2. **`doDeepCopy(T obj, Map<Object, Object> visited)`**:
    - A recursive helper method that handles the actual deep copying logic.
    - Tracks already visited objects to handle cyclic references and prevent infinite loops.
    - It performs deep copying based on the type of object (simple types, arrays, collections, maps, or complex objects).

3. **`isSimpleType(Object obj)`**:
    - Checks if an object is a simple type that does not require deep copying, such as primitives, Strings, or Enums.

4. **`copyArray(Object obj, Map<Object, Object> visited)`**:
    - Deep copies arrays of any type and dimension.

5. **`copyCollection(Collection<?> collection, Map<Object, Object> visited)`**:
    - Deep copies standard Java collections like `List`, `Set`, and `Queue`.

6. **`copyMap(Map<?, ?> map, Map<Object, Object> visited)`**:
    - Deep copies standard Java maps by recursively copying keys and values.

7. **`copyObjectOrHandleNoDefaultConstructor(T obj, Map<Object, Object> visited)`**:
    - Deep copies complex objects that are not arrays, collections, or maps.
    - Handles objects without default constructors by leveraging Unsafe operations to instantiate the object.

8. **`findMatchingConstructor(Object obj)`**:
    - Finds a constructor that matches the number of fields in the class for deep copying.

9. **`createCopyWithoutConstructor(T obj, Map<Object, Object> visited)`**:
    - Creates a deep copy of an object without using its constructor, handling cases where no appropriate constructor is available.

10. **`getFieldValues(Object obj, Map<Object, Object> visited)`**:
    - Retrieves field values for objects to pass them to constructors or set them manually.

11. **`setDeepCopiedFields(Object obj, Object copy, Map<Object, Object> visited)`**:
    - Sets the fields of the deep-copied object to match those of the original object after recursively copying them.

### Exception Handling

- **`DeepCopyException`**:
    - Custom exception thrown when a deep copy operation fails due to reflection issues, missing constructors, or unsupported collection types.

## Usage Example

To use the `DeepCopyUtils` class:

```java
import com.dpoghos.deepcopy.utils.DeepCopyUtils;

public class Example {
    public static void main(String[] args) {
        MyClass originalObject = new MyClass();
        MyClass deepCopiedObject = DeepCopyUtils.deepCopy(originalObject);

        // Modifications to deepCopiedObject will not affect originalObject
    }
}
```

## Notes

- This utility does not support some types of collections and may need to be extended for custom collection types.
- Ensure that the types being deep-copied do not have circular references that are not properly handled by the IdentityHashMap.
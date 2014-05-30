package br.com.bernardorufino.esfinge.genexample.util;

public class TypeConverter {

    // Prevents instantiation
    private TypeConverter() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }

    public static <T> T convertFromString(String string, Class<T> type) {
        if (type.isAssignableFrom(String.class)) {
            return type.cast(string);
        } else if (type.isAssignableFrom(Integer.class)) {
            return type.cast(Integer.valueOf(string));
        } else if (type.isAssignableFrom(Float.class)) {
            return type.cast(Float.valueOf(string));
        } else if (type.isAssignableFrom(Double.class)) {
            return type.cast(Double.valueOf(string));
        } else if (type.isAssignableFrom(Boolean.class)) {
            return type.cast(Boolean.valueOf(string));
        } else {
            throw new TypeConversionNotSupportedException("Cannot perform conversion to " + type.getName());
        }
    }

    public static Class<?> getType(String typeName) {
        switch (typeName) {
            case "String":  return String.class;
            case "Integer": return Integer.class;
            case "Float":   return Float.class;
            case "Double":  return Double.class;
            case "Boolean": return Boolean.class;
            case "Object":  return Object.class;
            default:        return null;
        }
    }

    public static Class<?> safelyGetType(String typeName) {
        Class<?> type = getType(typeName);
        if (type == null) type = Object.class;
        return type;
    }


}

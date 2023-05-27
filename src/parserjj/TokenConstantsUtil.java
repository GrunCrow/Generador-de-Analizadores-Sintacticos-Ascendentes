package parserjj;

import generated.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class TokenConstantsUtil {

    public static Map<String, Integer> getConstants(Class<?> tokenConstantsClass) {
        Map<String, Integer> constants = new HashMap<>();
        Field[] fields = tokenConstantsClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) &&
                    Modifier.isFinal(field.getModifiers())) {
                try {
                    String fieldName = field.getName();
                    int fieldValue = field.getInt(null);
                    constants.put(fieldName, fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return constants;
    }
}

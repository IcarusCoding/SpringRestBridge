package de.intelligence.restbridge.api.util;

import java.lang.annotation.Annotation;

public final class AnnotationUtils {

    private AnnotationUtils() {
    }

    public static Object getDefaultAnnotationValue(Annotation annotation) {
        try {
            return annotation.annotationType().getDeclaredMethod("value").getDefaultValue();
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static Object getValue(Annotation annotation, String attribute) {
        try {
            return annotation.annotationType().getDeclaredMethod(attribute).invoke(annotation);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

}

package de.intelligence.restbridge.api.util;

import java.lang.reflect.Type;

public final class TypeResolver {

    private TypeResolver() {}

    public static boolean isResolvable(Type type) {
        if (type == null) {
            return false;
        }
        if (type instanceof Class<?>) {
            return true;
        }
        // TODO support generic types
        return false;
    }

}

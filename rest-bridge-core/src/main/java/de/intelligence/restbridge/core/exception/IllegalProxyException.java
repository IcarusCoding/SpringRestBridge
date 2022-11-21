package de.intelligence.restbridge.core.exception;

import java.lang.reflect.Method;

public final class IllegalProxyException extends RuntimeException {

    public IllegalProxyException(Class<?> clazz, String message) {
        super(IllegalProxyException.createErrorMessage(clazz, message));
    }

    public IllegalProxyException(Method method, String message) {
        super(IllegalProxyException.createErrorMessage(method, message));
    }

    private static String createErrorMessage(Class<?> clazz, String message) {
        return "Can not create proxy of class \"" + clazz.getCanonicalName() + "\" [Reason: " + message + " ]";
    }

    private static String createErrorMessage(Method method, String message) {
        return "Method \"" + method.getName() + "\" in \"" + method.getDeclaringClass().getCanonicalName() + "\" is invalid [Reason: " + message + " ]";
    }

}

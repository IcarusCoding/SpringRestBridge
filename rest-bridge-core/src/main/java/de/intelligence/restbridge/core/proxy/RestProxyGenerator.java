package de.intelligence.restbridge.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

import de.intelligence.restbridge.api.proxy.IProxyGenerator;
import de.intelligence.restbridge.api.rest.registry.IRestMethodRegistry;
import de.intelligence.restbridge.api.rest.RestMethod;
import de.intelligence.restbridge.api.rest.factory.IRestMethodFactory;
import de.intelligence.restbridge.api.util.TypeResolver;
import de.intelligence.restbridge.core.exception.IllegalProxyException;

@RequiredArgsConstructor
public final class RestProxyGenerator implements IProxyGenerator {

    private final IRestMethodRegistry restMethodRegistry;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> interfaceClass, IRestMethodFactory restMethodFactory) {
        this.validateInterfaceClass(interfaceClass);
        final HashMap<Method, RestMethod<?>> restMethods = new HashMap<>();
        for (final Method method : interfaceClass.getDeclaredMethods()) {
            this.validateInterfaceMethod(method);
            restMethods.put(method, restMethodFactory.createRestMethod(method));
        }
        final T proxyInstance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                new RestProxyInvocationHandler());
        this.restMethodRegistry.add(restMethods);
        return proxyInstance;
    }

    private void validateInterfaceClass(Class<?> interfaceClass) {
        Objects.requireNonNull(interfaceClass, "Supplied interface class must not be null");
        if (!interfaceClass.isInterface()) {
            throw new IllegalProxyException(interfaceClass, "Class must be an interface");
        }
        if (interfaceClass.isHidden()) {
            throw new IllegalProxyException(interfaceClass, "Interface must not be hidden");
        }
        if (interfaceClass.isSealed()) {
            throw new IllegalProxyException(interfaceClass, "Sealed interfaces are not supported");
        }
        if (interfaceClass.isMemberClass() && !Modifier.isStatic(interfaceClass.getModifiers())) {
            throw new IllegalProxyException(interfaceClass, "Interface has to be independent (e.g. non-static inner classes are not allowed)");
        }
    }

    private void validateInterfaceMethod(Method method) {
        if (method.isDefault()) {
            throw new IllegalProxyException(method, "Default methods are not supported");
        }
        if (!TypeResolver.isResolvable(method.getGenericReturnType())) {
            throw new IllegalProxyException(method, "Return type is not resolvable at runtime");
        }
    }

    public final class RestProxyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            return RestProxyGenerator.this.restMethodRegistry.findMethod(method).orElseThrow(() ->
                    new IllegalStateException("Could not find rest method for proxied method \"" + method.toGenericString() + "\"")).invoke(args);
        }

    }

}

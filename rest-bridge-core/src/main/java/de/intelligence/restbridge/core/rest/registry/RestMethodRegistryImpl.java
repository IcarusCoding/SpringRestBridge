package de.intelligence.restbridge.core.rest.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.intelligence.restbridge.api.rest.RestMethod;
import de.intelligence.restbridge.api.rest.registry.IRestMethodRegistry;

public final class RestMethodRegistryImpl implements IRestMethodRegistry {

    private final Map<Method, RestMethod<?>> restMethodMap;

    public RestMethodRegistryImpl() {
        this.restMethodMap = new HashMap<>();
    }

    @Override
    public void add(Map<Method, RestMethod<?>> restMethods) {
        this.restMethodMap.putAll(restMethods);
    }

    @Override
    public Optional<RestMethod<?>> findMethod(Method method) {
        return Optional.ofNullable(this.restMethodMap.get(method));
    }

}

package de.intelligence.restbridge.api.rest.registry;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import de.intelligence.restbridge.api.rest.RestMethod;

public interface IRestMethodRegistry {

    void add(Map<Method, RestMethod<?>> restMethods);

    Optional<RestMethod<?>> findMethod(Method method);

}

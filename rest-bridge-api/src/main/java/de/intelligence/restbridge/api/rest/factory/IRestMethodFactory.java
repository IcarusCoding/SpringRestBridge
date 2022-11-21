package de.intelligence.restbridge.api.rest.factory;

import java.lang.reflect.Method;

import de.intelligence.restbridge.api.rest.RestMethod;

public interface IRestMethodFactory {

    <T> RestMethod<T> createRestMethod(Method method);

}

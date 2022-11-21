package de.intelligence.restbridge.api.proxy;

import de.intelligence.restbridge.api.rest.factory.IRestMethodFactory;

public interface IProxyGenerator {

    <T> T createProxy(Class<T> interfaceClass, IRestMethodFactory restMethodFactory);

}

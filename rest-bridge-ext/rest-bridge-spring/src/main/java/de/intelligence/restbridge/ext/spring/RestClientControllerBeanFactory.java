package de.intelligence.restbridge.ext.spring;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import de.intelligence.restbridge.api.proxy.IProxyGenerator;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public final class RestClientControllerBeanFactory<T> implements FactoryBean<T>, RestClientControllerClassAware<T> {

    private final IProxyGenerator proxyGenerator;

    private Class<T> restClientControllerClass;

    @Override
    public T getObject() throws Exception {
        // TODO get strategy of interface, get appropriate factory classes from strategy registrar and create instance of restClientControllerClass with proxyGenerator
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return this.restClientControllerClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setRestClientControllerClass(Class<T> restClientControllerClass) {
        this.restClientControllerClass = restClientControllerClass;
    }

}

package de.intelligence.restbridge.ext.spring;

import org.springframework.beans.factory.Aware;

public interface RestClientControllerClassAware<T> extends Aware {

    void setRestClientControllerClass(Class<T> restClientControllerClass);

}

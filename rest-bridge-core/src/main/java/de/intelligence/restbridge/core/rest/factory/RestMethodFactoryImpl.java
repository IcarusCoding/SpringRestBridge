package de.intelligence.restbridge.core.rest.factory;

import java.lang.reflect.Method;

import de.intelligence.restbridge.api.rest.RestMethod;
import de.intelligence.restbridge.api.rest.factory.IRestCallFactory;
import de.intelligence.restbridge.api.rest.factory.IRestMethodFactory;
import de.intelligence.restbridge.api.strategy.IRequestTemplateFactory;

public final class RestMethodFactoryImpl implements IRestMethodFactory {

    private final IRequestTemplateFactory templateFactory;
    private final IRestCallFactory restCallFactory;

    public RestMethodFactoryImpl(IRequestTemplateFactory templateFactory, IRestCallFactory restCallFactory) {
        this.templateFactory = templateFactory;
        this.restCallFactory = restCallFactory;
    }

    @Override
    public <T> RestMethod<T> createRestMethod(Method method) {
        return new RestMethod<>(this.templateFactory.create(method), this.restCallFactory);
    }

}

package de.intelligence.restbridge.api.strategy;

import java.lang.reflect.Method;

import de.intelligence.restbridge.api.rest.RequestTemplate;

public interface IRequestTemplateFactory {

    RequestTemplate create(Method method);

}

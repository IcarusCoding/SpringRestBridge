package de.intelligence.restbridge.api.rest;

import de.intelligence.restbridge.api.rest.factory.IRestCallFactory;

public final class RestMethod<T> {

    private final RequestTemplate template;
    private final IRestCallFactory restCallFactory;

    public RestMethod(RequestTemplate template, IRestCallFactory restCallFactory) {
        this.template = template;
        this.restCallFactory = restCallFactory;
    }

    public T invoke(Object... args) {
        final RestRequest request = RestRequest.fromTemplate(this.template);
        for (int i = 0; i < args.length; i++) {
            this.template.applyParameterResolver(i, request, args[i]);
        }
        System.out.println("URL: " + request.getUrl());
        System.out.println("HttpMethod: " + request.getHttpMethodType());
        System.out.println("Headers: " + request.getHeaders());
        System.out.println("FormParams: " + request.getFormParameters());
        System.out.println("QueryParams: " + request.getQueryParameters());
        System.out.println("PathParams: " + request.getPathParameters());
        final IRestCall<T> restCall = this.restCallFactory.create(request);
        restCall.call();
        throw new UnsupportedOperationException();
    }

}

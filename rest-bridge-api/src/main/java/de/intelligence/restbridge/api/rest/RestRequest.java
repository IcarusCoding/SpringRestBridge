package de.intelligence.restbridge.api.rest;


import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import de.intelligence.restbridge.api.http.HttpMethodType;
import de.intelligence.restbridge.api.util.MultiValueMap;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RestRequest {

    private final HttpMethodType httpMethodType;
    private final String url;
    private final MultiValueMap<String, String> headers;
    private final Map<String, String> pathParameters;
    private final Map<String, String> formParameters;
    private final Map<String, String> queryParameters;

    public static RestRequest fromTemplate(RequestTemplate template) {
        return new RestRequest(template.getHttpMethodType(), template.getUrl(),
                new MultiValueMap<>(template.getHeaders()), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addPath(String name, String value) {
        this.pathParameters.put(name, value);
    }

    public void addFormParameter(String name, String value) {
        this.formParameters.put(name, value);
    }

    public void addQueryParameter(String name, String value) {
        this.queryParameters.put(name, value);
    }

}

package de.intelligence.restbridge.api.rest;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

import de.intelligence.restbridge.api.http.HttpEncoding;
import de.intelligence.restbridge.api.http.HttpMethodType;
import de.intelligence.restbridge.api.rest.parameter.IParameterResolver;
import de.intelligence.restbridge.api.util.MultiValueMap;

@Getter
@Builder
public final class RequestTemplate {

    private HttpMethodType httpMethodType;
    private String url;
    private HttpEncoding encoding;
    private MultiValueMap<String, String> headers;
    private IParameterResolver<?>[] parameterResolvers;

    @SuppressWarnings("unchecked")
    public void applyParameterResolver(int index, RestRequest request, Object arg) {
        ((IParameterResolver<Object>) this.parameterResolvers[Objects.checkIndex(index, parameterResolvers.length)]).resolve(request, arg);
    }

}

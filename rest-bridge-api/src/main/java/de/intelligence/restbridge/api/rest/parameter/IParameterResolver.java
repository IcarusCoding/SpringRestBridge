package de.intelligence.restbridge.api.rest.parameter;

import de.intelligence.restbridge.api.rest.RestRequest;

public interface IParameterResolver<T> {

    void resolve(RestRequest request, T value);

}

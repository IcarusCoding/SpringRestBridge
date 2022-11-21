package de.intelligence.restbridge.strategy.rest;

import de.intelligence.restbridge.api.rest.IRestCall;
import de.intelligence.restbridge.api.rest.RestRequest;

public abstract class ARestCall<T> implements IRestCall<T> {

    protected final RestRequest restRequest;

    protected ARestCall(RestRequest restRequest) {
        this.restRequest = restRequest;
    }


}

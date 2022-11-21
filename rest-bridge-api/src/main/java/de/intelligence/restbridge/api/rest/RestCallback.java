package de.intelligence.restbridge.api.rest;

public interface RestCallback<T> {

    void handleRestResponse(RestResponse<T> response);

}

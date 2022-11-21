package de.intelligence.restbridge.api.rest;

public interface IRestCall<T> {

    RestResponse<T> call();

    void callAsync(RestCallback<T> restCallback);

}

package de.intelligence.restbridge.api.rest.factory;

import de.intelligence.restbridge.api.rest.IRestCall;
import de.intelligence.restbridge.api.rest.RestRequest;

public interface IRestCallFactory {

    <T> IRestCall<T> create(RestRequest request);

}

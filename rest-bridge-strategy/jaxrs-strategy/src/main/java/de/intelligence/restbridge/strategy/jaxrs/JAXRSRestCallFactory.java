package de.intelligence.restbridge.strategy.jaxrs;

import de.intelligence.restbridge.api.rest.IRestCall;
import de.intelligence.restbridge.api.rest.RestCallback;
import de.intelligence.restbridge.api.rest.RestRequest;
import de.intelligence.restbridge.api.rest.RestResponse;
import de.intelligence.restbridge.api.rest.factory.IRestCallFactory;
import de.intelligence.restbridge.strategy.rest.ARestCall;

public final class JAXRSRestCallFactory implements IRestCallFactory {

    @Override
    public <T> IRestCall<T> create(RestRequest request) {
        return new JAXRSRestCall<>(request);
    }

    public static final class JAXRSRestCall<T> extends ARestCall<T> {

        public JAXRSRestCall(RestRequest request) {
            super(request);
        }

        @Override
        public RestResponse<T> call() {
            return null;
        }

        @Override
        public void callAsync(RestCallback<T> restCallback) {

        }

    }

}

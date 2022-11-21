package de.intelligence.restbridge.strategy.icarus;

import de.intelligence.restbridge.api.rest.IRestCall;
import de.intelligence.restbridge.api.rest.RestCallback;
import de.intelligence.restbridge.api.rest.RestRequest;
import de.intelligence.restbridge.api.rest.RestResponse;
import de.intelligence.restbridge.api.rest.factory.IRestCallFactory;
import de.intelligence.restbridge.strategy.rest.ARestCall;

public final class IcarusRestCallFactory implements IRestCallFactory {

    @Override
    public <T> IRestCall<T> create(RestRequest request) {
        return new IcarusRestCall<>(request);
    }

    public static final class IcarusRestCall<T> extends ARestCall<T> {

        public IcarusRestCall(RestRequest request) {
            super(request);
        }

        @Override
        public RestResponse<T> call() {
            throw new UnsupportedOperationException("CALL NOT IMPLEMENTED IN ICARUS");
        }

        @Override
        public void callAsync(RestCallback<T> restCallback) {
            throw new UnsupportedOperationException("CALL ASYNC NOT IMPLEMENTED IN ICARUS");
        }

    }

}

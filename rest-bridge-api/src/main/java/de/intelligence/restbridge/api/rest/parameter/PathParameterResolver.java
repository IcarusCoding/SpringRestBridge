package de.intelligence.restbridge.api.rest.parameter;

import java.util.function.Function;

import de.intelligence.restbridge.api.rest.RestRequest;

public final class PathParameterResolver<T> extends AParameterResolver<T> {

    private final String name;
    private final Function<T, String> stringFunc;

    public PathParameterResolver(int index, String name, Function<T, String> stringFunc) {
        super(index);
        this.name = name;
        this.stringFunc = stringFunc;
    }

    @Override
    public void resolve(RestRequest request, T value) {
        request.addPath(this.name, this.stringFunc.apply(value));
    }

}

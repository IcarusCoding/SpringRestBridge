package de.intelligence.restbridge.api.rest.parameter;

public abstract class AParameterResolver<T> implements IParameterResolver<T> {

    protected int index;

    protected AParameterResolver(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

}

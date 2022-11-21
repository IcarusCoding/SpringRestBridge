package de.intelligence.restbridge.api.http;

import java.util.Arrays;

public enum HttpMethodType {

    DELETE(false),
    GET(false),
    HEAD(false),
    PATCH(true),
    POST(true),
    PUT(true),
    OPTIONS(false);

    private final boolean bodyNeeded;

    HttpMethodType(boolean bodyNeeded) {
        this.bodyNeeded = bodyNeeded;
    }

    public boolean isBodyNeeded() {
        return this.bodyNeeded;
    }

    public static HttpMethodType parseHttpMethod(String method) {
        return Arrays.stream(HttpMethodType.values()).filter(httpMethod ->
                httpMethod.name().equalsIgnoreCase(method)).findAny().orElse(null);
    }

}

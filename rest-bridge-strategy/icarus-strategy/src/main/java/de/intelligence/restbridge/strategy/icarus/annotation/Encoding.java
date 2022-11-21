package de.intelligence.restbridge.strategy.icarus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.intelligence.restbridge.api.http.HttpEncoding;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Encoding {

    HttpEncoding value();

}

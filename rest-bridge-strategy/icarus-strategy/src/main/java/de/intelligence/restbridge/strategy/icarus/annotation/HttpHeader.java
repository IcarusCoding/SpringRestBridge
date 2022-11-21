package de.intelligence.restbridge.strategy.icarus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(HttpHeaders.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpHeader {

    String value();

}

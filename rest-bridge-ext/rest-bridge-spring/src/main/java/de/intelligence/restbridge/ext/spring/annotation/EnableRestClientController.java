package de.intelligence.restbridge.ext.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import de.intelligence.restbridge.ext.spring.RestClientControllerDefinitionRegistrar;
import de.intelligence.restbridge.ext.spring.conf.SpringBridgeConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SpringBridgeConfig.class, RestClientControllerDefinitionRegistrar.class})
public @interface EnableRestClientController {

    String[] basePackages() default "*";

}

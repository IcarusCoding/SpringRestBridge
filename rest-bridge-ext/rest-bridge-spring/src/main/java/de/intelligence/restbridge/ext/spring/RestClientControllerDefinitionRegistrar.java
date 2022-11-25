package de.intelligence.restbridge.ext.spring;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import de.intelligence.restbridge.ext.spring.annotation.EnableRestClientController;

public final class RestClientControllerDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        final String[] basePackages = Objects.requireNonNull(AnnotationAttributes
                .fromMap(Optional.ofNullable(importingClassMetadata.getAnnotationAttributes(EnableRestClientController.class.getName()))
                        .orElse(Collections.emptyMap())).getStringArray("basePackages"));
        final GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(SpringRestBridgeProperties.class);
        final MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues.add("basePackages", basePackages);
        genericBeanDefinition.setPropertyValues(mutablePropertyValues);
        registry.registerBeanDefinition("restBridgeProperties", genericBeanDefinition);
        new RestClientControllerClasspathScanner(registry).doScan(basePackages);
    }

}

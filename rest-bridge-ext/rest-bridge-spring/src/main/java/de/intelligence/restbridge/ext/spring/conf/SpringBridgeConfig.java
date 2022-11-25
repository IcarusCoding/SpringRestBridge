package de.intelligence.restbridge.ext.spring.conf;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.intelligence.restbridge.api.proxy.IProxyGenerator;
import de.intelligence.restbridge.api.rest.registry.IRestMethodRegistry;
import de.intelligence.restbridge.core.proxy.RestProxyGenerator;
import de.intelligence.restbridge.core.rest.registry.RestMethodRegistryImpl;

@Configuration
public class SpringBridgeConfig {

    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    protected IRestMethodRegistry restMethodRegistry() {
        return new RestMethodRegistryImpl();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    protected IProxyGenerator proxyGenerator(IRestMethodRegistry restMethodRegistry) {
        return new RestProxyGenerator(restMethodRegistry);
    }

}

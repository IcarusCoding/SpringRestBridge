package de.intelligence.restbridge.ext.spring;

import java.util.Set;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.intelligence.restbridge.api.annotation.RestClientController;

@Log4j2
public final class RestClientControllerClasspathScanner extends ClassPathBeanDefinitionScanner {

    public RestClientControllerClasspathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected void registerDefaultFilters() {
        super.addIncludeFilter(new AnnotationTypeFilter(RestClientController.class));
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        final Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        beanDefinitionHolders.forEach(bdh -> {
            log.info("Found REST client interface definition \"{}\"", bdh.getBeanDefinition().getBeanClassName());
            bdh.getBeanDefinition().getPropertyValues().add("restClientControllerClass",
                    bdh.getBeanDefinition().getBeanClassName());
            ((GenericBeanDefinition) bdh.getBeanDefinition()).setBeanClass(RestClientControllerBeanFactory.class);
        });
        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent(); //TODO add more restrictions
    }

}

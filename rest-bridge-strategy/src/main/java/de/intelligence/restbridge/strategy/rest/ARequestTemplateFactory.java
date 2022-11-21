package de.intelligence.restbridge.strategy.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import lombok.NonNull;

import de.intelligence.restbridge.api.annotation.RestClientController;
import de.intelligence.restbridge.api.http.HttpEncoding;
import de.intelligence.restbridge.api.http.HttpMethodType;
import de.intelligence.restbridge.api.rest.RequestTemplate;
import de.intelligence.restbridge.api.rest.parameter.IParameterResolver;
import de.intelligence.restbridge.api.strategy.IRequestTemplateFactory;
import de.intelligence.restbridge.api.util.AnnotationUtils;
import de.intelligence.restbridge.api.util.MultiValueMap;
import de.intelligence.restbridge.api.util.TypeResolver;

public abstract class ARequestTemplateFactory implements IRequestTemplateFactory {

    @Override
    public RequestTemplate create(Method method) {
        return this.getTemplateBuilder(method).buildTemplate();
    }

    protected abstract ARequestTemplateBuilder getTemplateBuilder(Method method);

    protected abstract static class ARequestTemplateBuilder {

        protected final Method method;

        protected Annotation[][] parameterAnnotations;

        protected HttpMethodType httpMethodType;
        protected Class<? extends Annotation> httpMethodClass;
        protected String baseUrl;
        protected String relativeUrl;
        protected String fullUrl;
        protected HttpEncoding httpEncoding;
        protected MultiValueMap<String, String> headers;

        protected ARequestTemplateBuilder(@NonNull Method method) {
            this.method = method;
            this.parameterAnnotations = this.method.getParameterAnnotations();
        }

        protected RequestTemplate buildTemplate() {
            for (final HttpMethodType type : HttpMethodType.values()) {
                final Class<? extends Annotation> annotationClass = this.getHttpMethodAnnotation(type);
                if (this.method.isAnnotationPresent(annotationClass)) {
                    if (this.httpMethodType != null) {
                        throw new IllegalStateException("Only one Http method annotation is supported");
                    }
                    this.httpMethodType = type;
                    this.httpMethodClass = annotationClass;
                }
            }
            final HttpMethodType fallbackType = this.fallbackHttpMethod();
            if (fallbackType != null) {
                if (this.httpMethodType != null) {
                    throw new IllegalStateException("Only one Http method annotation is supported");
                }
                this.httpMethodType = fallbackType;
            }
            if (this.httpMethodType == null) {
                throw new IllegalStateException("Method \"" + this.method
                        + "\" is not correctly annotated [Reason: Missing or unsupported http method]");
            }
            this.baseUrl = this.extractBaseUrl();
            this.relativeUrl = this.extractRelativeUrl();
            this.fullUrl = this.baseUrl + this.relativeUrl;
            this.httpEncoding = this.extractHttpEncoding();
            if (!this.httpMethodType.isBodyNeeded() && (this.httpEncoding != null && this.httpEncoding != HttpEncoding.NO_ENCODING)) {
                throw new IllegalStateException("Method \"" + this.method + "\" is annotated with a http method annotation that requires no body but specifies an encoding");
            }
            this.headers = this.extractDefaultHttpHeaders();
            final Type[] parameterTypes = this.method.getParameterTypes();
            final IParameterResolver<?>[] parameterResolvers = new IParameterResolver[parameterTypes.length];
            for (int i = 0; i < this.parameterAnnotations.length; i++) {
                parameterResolvers[i] = this.extractParameterResolver(i, this.parameterAnnotations[i], parameterTypes[i]);
            }
            return RequestTemplate.builder()
                    .httpMethodType(this.httpMethodType)
                    .url(this.fullUrl)
                    .encoding(this.httpEncoding)
                    .headers(this.headers)
                    .parameterResolvers(parameterResolvers)
                    .build();
        }

        protected IParameterResolver<?> extractParameterResolver(int index, Annotation[] parameterAnnotations, Type parameterType) {
            if (!TypeResolver.isResolvable(parameterType)) {
                throw new IllegalStateException("Parameter at index " + index + " for method \"" + this.method + "\" has unresolvable type");
            }
            IParameterResolver<?> parameterResolver = null;
            for (final Annotation parameterAnnotation : parameterAnnotations) {
                final Optional<IParameterResolver<?>> tempResolverOpt = this.extractParameterResolver(index, parameterAnnotation, parameterType);
                if (tempResolverOpt.isEmpty()) {
                    continue;
                }
                if (parameterResolver != null) {
                    throw new IllegalStateException("Parameter at index " + index + " for method \"" + this.method + "\" has an invalid annotation combination");
                }
                parameterResolver = tempResolverOpt.get();
            }
            if (parameterResolver == null) {
                throw new IllegalStateException("Could not create a parameter resolver for parameter at index " + index + " for method \"" + this.method + "\" ");
            }
            return parameterResolver;
        }

        protected final <T> T getAnnotationValue(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass) {
            return this.getAnnotationValue(annotatedElement, annotationClass, "value");
        }

        @SuppressWarnings("unchecked")
        protected final <T> T getAnnotationValue(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass,
                                                 String attribute) {
            return (T) AnnotationUtils.getValue(annotatedElement.getAnnotation(annotationClass), attribute);
        }

        protected final <T> T getAnnotationValueOrDefault(AnnotatedElement annotatedElement,
                                                          Class<? extends Annotation> annotationClass, Supplier<T> defaultValue) {
            return this.getAnnotationValueOrDefault(annotatedElement, annotationClass, "value", defaultValue);
        }

        protected final <T> T getAnnotationValueOrDefault(AnnotatedElement annotatedElement,
                                                          Class<? extends Annotation> annotationClass, String attribute,
                                                          Supplier<T> defaultValue) {
            if (annotatedElement.isAnnotationPresent(annotationClass)) {
                return this.getAnnotationValue(annotatedElement, annotationClass, attribute);
            }
            return defaultValue.get();
        }

        protected final Optional<? extends Annotation> extractAnnotation(Annotation[] annotations,
                                                                         Class<? extends Annotation> annotationClass) {
            return Arrays.stream(annotations).filter(annotation -> annotation.annotationType()
                    .equals(annotationClass)).findAny();
        }

        protected final String validatePathReplacement(String pathReplacement) {
            final String replacementTemplate = "{" + pathReplacement + "}";
            if (!this.fullUrl.contains(replacementTemplate)) {
                throw new IllegalStateException("Path replacement key \"" + pathReplacement + "\" not found in url template \"" + this.fullUrl + "\"");
            }
            return pathReplacement;
        }

        protected final boolean isFormEncoded() {
            return this.httpEncoding == HttpEncoding.FORM_URL_ENCODING;
        }

        protected final boolean isMultipartEncoded() {
            return this.httpEncoding == HttpEncoding.MULTIPART_ENCODING;
        }

        protected String extractBaseUrl() {
            return this.getAnnotationValueOrDefault(this.method.getDeclaringClass(), RestClientController.class, () -> "");
        }

        protected abstract Class<? extends Annotation> getHttpMethodAnnotation(HttpMethodType httpMethodType);

        protected abstract HttpMethodType fallbackHttpMethod();

        protected abstract MultiValueMap<String, String> extractDefaultHttpHeaders();

        protected abstract String extractRelativeUrl();

        protected abstract Optional<IParameterResolver<?>> extractParameterResolver(int index, Annotation parameterAnnotation, Type parameterType);

        protected abstract HttpEncoding extractHttpEncoding();

    }

}

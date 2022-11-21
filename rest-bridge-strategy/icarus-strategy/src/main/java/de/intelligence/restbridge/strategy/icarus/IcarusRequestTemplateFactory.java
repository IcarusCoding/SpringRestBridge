package de.intelligence.restbridge.strategy.icarus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import de.intelligence.restbridge.api.http.HttpEncoding;
import de.intelligence.restbridge.api.http.HttpMethodType;
import de.intelligence.restbridge.api.rest.parameter.FormParameterResolver;
import de.intelligence.restbridge.api.rest.parameter.HeaderParameterResolver;
import de.intelligence.restbridge.api.rest.parameter.IParameterResolver;
import de.intelligence.restbridge.api.rest.parameter.PathParameterResolver;
import de.intelligence.restbridge.api.rest.parameter.QueryParameterResolver;
import de.intelligence.restbridge.api.util.MultiValueMap;
import de.intelligence.restbridge.strategy.icarus.annotation.BaseUrl;
import de.intelligence.restbridge.strategy.icarus.annotation.Delete;
import de.intelligence.restbridge.strategy.icarus.annotation.Encoding;
import de.intelligence.restbridge.strategy.icarus.annotation.FormEncoded;
import de.intelligence.restbridge.strategy.icarus.annotation.FormParam;
import de.intelligence.restbridge.strategy.icarus.annotation.Get;
import de.intelligence.restbridge.strategy.icarus.annotation.Head;
import de.intelligence.restbridge.strategy.icarus.annotation.HeaderParam;
import de.intelligence.restbridge.strategy.icarus.annotation.HttpHeader;
import de.intelligence.restbridge.strategy.icarus.annotation.HttpMethod;
import de.intelligence.restbridge.strategy.icarus.annotation.MultipartEncoded;
import de.intelligence.restbridge.strategy.icarus.annotation.Options;
import de.intelligence.restbridge.strategy.icarus.annotation.PartParam;
import de.intelligence.restbridge.strategy.icarus.annotation.Patch;
import de.intelligence.restbridge.strategy.icarus.annotation.PathParam;
import de.intelligence.restbridge.strategy.icarus.annotation.Post;
import de.intelligence.restbridge.strategy.icarus.annotation.Put;
import de.intelligence.restbridge.strategy.icarus.annotation.QueryParam;
import de.intelligence.restbridge.strategy.rest.ARequestTemplateFactory;

public final class IcarusRequestTemplateFactory extends ARequestTemplateFactory {

    @Override
    protected ARequestTemplateBuilder getTemplateBuilder(Method method) {
        return new IcarusRequestTemplateBuilder(method);
    }

    private static final class IcarusRequestTemplateBuilder extends ARequestTemplateBuilder {

        public IcarusRequestTemplateBuilder(Method method) {
            super(method);
        }

        @Override
        protected Class<? extends Annotation> getHttpMethodAnnotation(HttpMethodType httpMethodType) {
            return switch (httpMethodType) {
                case DELETE -> Delete.class;
                case GET -> Get.class;
                case HEAD -> Head.class;
                case PATCH -> Patch.class;
                case POST -> Post.class;
                case PUT -> Put.class;
                case OPTIONS -> Options.class;
            };
        }

        @Override
        protected HttpMethodType fallbackHttpMethod() {
            return super.getAnnotationValueOrDefault(super.method, HttpMethod.class, () -> null);
        }

        @Override
        protected String extractBaseUrl() {
            return super.getAnnotationValueOrDefault(super.method.getDeclaringClass(), BaseUrl.class, super::extractBaseUrl);
        }

        @Override
        protected MultiValueMap<String, String> extractDefaultHttpHeaders() {
            final MultiValueMap<String, String> headers = new MultiValueMap<>();
            Arrays.stream(super.method.getAnnotationsByType(HttpHeader.class)).forEach(header -> {
                final String value = header.value();
                final int colonIndex = value.indexOf(':');
                if (colonIndex <= 0 || colonIndex == value.length() - 1) {
                    throw new IllegalStateException("The key and value of a http header must be separated by a colon");
                }
                final String[] splitHeader = value.split(":");
                headers.put(splitHeader[0], splitHeader[1]);
            });
            return headers;
        }

        @Override
        protected String extractRelativeUrl() {
            if (super.httpMethodClass != null) {
                return super.getAnnotationValue(super.method, super.httpMethodClass);
            }
            return super.getAnnotationValueOrDefault(super.method, HttpMethod.class, "path", () -> "");
        }

        @Override
        protected Optional<IParameterResolver<?>> extractParameterResolver(int index, Annotation parameterAnnotation, Type parameterType) {
            final Class<? extends Annotation> annotationClass = parameterAnnotation.annotationType();
            //TODO customizable functions
            final Function<?, String> generalToStringFunc = Object::toString;
            if (annotationClass == FormParam.class) {
                if (!super.isFormEncoded()) {
                    throw new IllegalStateException("@FormParam can only be used with @FormEncoded");
                }
                return Optional.of(new FormParameterResolver<>(index, ((FormParam) parameterAnnotation).value(), generalToStringFunc));
            }
            if (annotationClass == HeaderParam.class) {
                final String headerName = ((HeaderParam) parameterAnnotation).value();
                if (super.headers.containsKey(headerName)) {
                    throw new IllegalStateException("Header with name \"" + headerName + "\" is already present");
                }
                return Optional.of(new HeaderParameterResolver<>(index, headerName, generalToStringFunc));
            }
            if (annotationClass == PathParam.class) {
                return Optional.of(new PathParameterResolver<>(index,
                        super.validatePathReplacement(((PathParam) parameterAnnotation).value()), generalToStringFunc));
            }
            if (annotationClass == PartParam.class) {
                throw new UnsupportedOperationException("Multipart encoding is not yet supported.");
            }
            if (annotationClass == QueryParam.class) {
                return Optional.of(new QueryParameterResolver<>(index, ((QueryParam) parameterAnnotation).value(), generalToStringFunc));
            }
            return Optional.empty();
        }

        @Override
        protected HttpEncoding extractHttpEncoding() {
            HttpEncoding encoding = null;
            if (super.method.isAnnotationPresent(MultipartEncoded.class)) {
                encoding = HttpEncoding.MULTIPART_ENCODING;
                throw new UnsupportedOperationException("Multipart encoding is not yet supported.");
            }
            if (super.method.isAnnotationPresent(FormEncoded.class)) {
                if (encoding != null) {
                    throw new IllegalStateException("Only one encoding annotation is supported at the same time");
                }
                encoding = HttpEncoding.FORM_URL_ENCODING;
            }
            if (super.method.isAnnotationPresent(Encoding.class)) {
                if (encoding != null) {
                    throw new IllegalStateException("Only one encoding annotation is supported at the same time");
                }
                encoding = super.getAnnotationValue(super.method, Encoding.class);
            }
            return Optional.ofNullable(encoding).orElse(HttpEncoding.NO_ENCODING);
        }
    }

}

package de.intelligence.restbridge.strategy.jaxrs;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Function;

import de.intelligence.restbridge.api.http.HttpEncoding;
import de.intelligence.restbridge.api.http.HttpMethodType;
import de.intelligence.restbridge.api.rest.parameter.FormParameterResolver;
import de.intelligence.restbridge.api.rest.parameter.IParameterResolver;
import de.intelligence.restbridge.api.rest.parameter.PathParameterResolver;
import de.intelligence.restbridge.api.rest.parameter.QueryParameterResolver;
import de.intelligence.restbridge.api.util.MultiValueMap;
import de.intelligence.restbridge.strategy.rest.ARequestTemplateFactory;

public final class JAXRSRequestTemplateFactory extends ARequestTemplateFactory {

    @Override
    protected ARequestTemplateBuilder getTemplateBuilder(Method method) {
        return new JAXRSRequestTemplateBuilder(method);
    }

    private static final class JAXRSRequestTemplateBuilder extends ARequestTemplateBuilder {

        public JAXRSRequestTemplateBuilder(Method method) {
            super(method);
        }

        @Override
        protected Class<? extends Annotation> getHttpMethodAnnotation(HttpMethodType httpMethodType) {
            return switch (httpMethodType) {
                case DELETE -> DELETE.class;
                case GET -> GET.class;
                case HEAD -> HEAD.class;
                case PATCH -> PATCH.class;
                case POST -> POST.class;
                case PUT -> PUT.class;
                case OPTIONS -> OPTIONS.class;
            };
        }

        @Override
        protected HttpMethodType fallbackHttpMethod() {
            return HttpMethodType.parseHttpMethod(super.getAnnotationValueOrDefault(super.method, HttpMethod.class, () -> null));
        }

        @Override
        protected MultiValueMap<String, String> extractDefaultHttpHeaders() {
            return new MultiValueMap<>();
        }

        @Override
        protected String extractRelativeUrl() {
            return super.getAnnotationValueOrDefault(super.method.getDeclaringClass(), Path.class, () -> "") +
                    super.getAnnotationValueOrDefault(super.method, Path.class, () -> "");
        }

        @Override
        protected Optional<IParameterResolver<?>> extractParameterResolver(int index, Annotation parameterAnnotation, Type parameterType) {
            final Class<? extends Annotation> annotationClass = parameterAnnotation.annotationType();
            //TODO customizable functions
            Function<?, String> generalToStringFunc = Object::toString;
            // TODO validate parameters and check if conversion to string etc is supported
            if (annotationClass == FormParam.class) {
                return Optional.of(new FormParameterResolver<>(index, ((FormParam) parameterAnnotation).value(), generalToStringFunc));
            }
            if (annotationClass == HeaderParam.class) {
                return Optional.of(new FormParameterResolver<>(index, ((HeaderParam) parameterAnnotation).value(), generalToStringFunc));
            }
            if (annotationClass == PathParam.class) {
                return Optional.of(new PathParameterResolver<>(index,
                        super.validatePathReplacement(((PathParam) parameterAnnotation).value()), generalToStringFunc));
            }
            if (annotationClass == QueryParam.class) {
                return Optional.of(new QueryParameterResolver<>(index, ((QueryParam) parameterAnnotation).value(), generalToStringFunc));
            }
            return Optional.empty();
        }

        @Override
        protected HttpEncoding extractHttpEncoding() {
            HttpEncoding encoding = null;
            for (final Annotation[] paramAnnotations : super.parameterAnnotations) {
                for (final Annotation paramAnnotation : paramAnnotations) {
                    if (paramAnnotation instanceof FormParam) {
                        encoding = HttpEncoding.FORM_URL_ENCODING;
                        break;
                    }
                }
                if (encoding != null) {
                    break;
                }
            }
            return Optional.ofNullable(encoding).orElse(HttpEncoding.NO_ENCODING);
        }

    }

}

package works.lightship.spring.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import works.lightship.jackson.filter.TreeFilterProvider;
import works.lightship.jackson.filter.TreeStringParser;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * ControllerAdvice used to wrap the ResponseBody in a {@code MappingJacksonValue} and
 * inject the {@code TreeFilterProvider}.  Also caches the annotation filter string to
 * Map conversions for each controller that has a {@code FilteredResponseBody}.
 *
 * May be extended and annotated with {@code ControllerAdvice} to enable the usage of {@code FilteredResponseBody}
 *
 * @author Tyler Eastman
 * @since 1.0
 * @see FilteredResponseBody
 */
public class FilteredResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private Map<FilteredResponseBody, Map<String, Object>> filterTreeMaps = new HashMap<>();

    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        Annotation[] methodAnnotations = methodParameter.getMethodAnnotations();
        for (Annotation annotation : methodAnnotations) {
            if (annotation.annotationType().equals(FilteredResponseBody.class)) {
                return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
            }
        }
        return false;
    }

    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Annotation[] methodAnnotations = methodParameter.getMethodAnnotations();
        for (Annotation annotation : methodAnnotations) {
            if (annotation.annotationType().equals(FilteredResponseBody.class)) {
                FilteredResponseBody filter = (FilteredResponseBody) annotation;
                Map<String, Object> filterTree;
                if(filterTreeMaps.containsKey(filter)){
                    filterTree = filterTreeMaps.get(filter);
                }
                else{
                    filterTree = TreeStringParser.parse(filter.value());
                    filterTreeMaps.put(filter, filterTree);
                }
                MappingJacksonValue result = new MappingJacksonValue(o);
                result.setFilters(new TreeFilterProvider(filterTree));
                return result;
            }
        }
        return o;
    }
}

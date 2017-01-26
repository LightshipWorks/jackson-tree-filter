package works.lightship.spring.annotation;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that indicates that a method should be annotated with {@code @ResponseBody} and
 * the properties of the response value should be filtered according to the passed in filter string.
 * This is intended for annotated handler methods in a Servlet.
 *
 * The filter string is a comma separated list of property names.  If a property is an Object, the objects
 * properties can also be serialized by including their names in a comma separated list contained within \{\}.
 *
 * ex: @FilteredResponseBody("id, name, relateObject{id, otherRelatedObject{id}, createDate}, createDate")
 *
 * @author Tyler Eastman
 * @since 1.0
 * @see org.springframework.stereotype.Controller
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ResponseBody
public @interface FilteredResponseBody {
    String value();
}

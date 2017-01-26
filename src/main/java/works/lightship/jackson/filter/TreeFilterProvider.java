package works.lightship.jackson.filter;

import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

import java.util.Map;

/**
 * {@code PropertyFilter} provider for {@code TreePropertyFilter}
 *
 * @author Tyler Eastman
 * @since 1.0
 * @see TreePropertyFilter
 */
public class TreeFilterProvider extends FilterProvider {

    private TreePropertyFilter filter;

    public TreeFilterProvider(Map<String, Object> filterMap) {
        filter = new TreePropertyFilter(filterMap);
    }

    @Override
    public BeanPropertyFilter findFilter(Object o) {
        return filter;
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
        return filter;
    }
}

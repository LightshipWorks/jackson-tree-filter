package works.lightship.jackson.filter;

/**
 * Container class used for tracking serialization position inside a stack.
 *
 * @author Tyler Eastman
 * @since 1.0
 * @see TreePropertyFilter
 */
public class BranchStackEntry {
    private Object context;
    private String fieldName;
    private Class fieldClass;

    public BranchStackEntry(Object context, String fieldName, Class fieldClass) {
        this.context = context;
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }
}

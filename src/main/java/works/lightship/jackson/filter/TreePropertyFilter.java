package works.lightship.jackson.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.Map;
import java.util.Stack;

/**
 * Filters out any properties from serialization that do not exist within the filterTree.
 *
 * @author Tyler Eastman
 * @since 1.0
 */
public class TreePropertyFilter extends SimpleBeanPropertyFilter {

    private Map<String, Object> filterTree;

    private Stack<BranchStackEntry> branchStack;

    public TreePropertyFilter(Map<String, Object> filterTree) {
        this.branchStack = new Stack<>();
        this.filterTree = filterTree;
    }

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {

        String name = writer.getName();
        Class fieldClass = writer.getType().getRawClass();

        if(writer.getType().getClass().equals(CollectionType.class) || writer.getType().getClass().equals(ArrayType.class)){
            fieldClass = writer.getType().getContentType().getRawClass();
        }

        while(!branchStack.empty()){
            BranchStackEntry entry = branchStack.pop();
            if(entry.getContext().equals(pojo)){
                break;
            }
            if(entry.getFieldClass().equals(pojo.getClass())){
                branchStack.push(entry);
                break;
            }
        }
        branchStack.push(new BranchStackEntry(pojo, name, fieldClass));

        boolean serialize = true;
        Map<String, Object> currentTree = filterTree;

        for(BranchStackEntry entry : branchStack){
            if(currentTree != null && currentTree.containsKey(entry.getFieldName())){
                currentTree = (Map<String, Object>)currentTree.get(entry.getFieldName());
            }else{
                serialize = false;
                break;
            }
        }

        if(serialize){
            writer.serializeAsField(pojo, jgen, provider);
        }
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception
    {
        this.serializeAsField(bean, jgen, provider, (PropertyWriter)writer);
    }
}

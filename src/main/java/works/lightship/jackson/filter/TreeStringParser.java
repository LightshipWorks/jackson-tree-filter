package works.lightship.jackson.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts a given filter string into a Tree, stored as a {@code Map}.
 *
 * The filter string is a comma separated list of property names.  If a property is an Object, the objects
 * properties can also be serialized by including their names in a comma separated list contained within \{\}.
 *
 * ex: "id, name, relateObject{id, otherRelatedObject{id}, createDate}, createDate"

 * @author Tyler Eastman
 * @since 1.0
 * @see TreePropertyFilter
 */
public class TreeStringParser {

    // TODO: Return a tree structure instead of a Map

    public static Map<String, Object> parse(String filter){
        Map<String, Object> result = new HashMap<>();
        TreeStringParser.parse(filter.replaceAll("\\s",""), result);
        return result;
    }

    private static int parse(String filter, Map<String, Object> map){
        int resultIndex = 0;
        SymbolLocation nextSymbol = getNextSymbol(filter);
        while (nextSymbol != null){
            String fieldName = filter.substring(0, nextSymbol.index);
            resultIndex += nextSymbol.index + 1;
            if(nextSymbol.character == ','){
                if(!fieldName.isEmpty())
                    map.put(fieldName, null);
                filter = filter.substring(nextSymbol.index + 1);
            }
            else if(nextSymbol.character == '{'){
                Map<String, Object> submap = new HashMap<>();
                int subIndex = parse(filter.substring(nextSymbol.index + 1), submap);
                if(!fieldName.isEmpty())
                    map.put(fieldName, submap);
                resultIndex += subIndex;
                filter = filter.substring(subIndex + nextSymbol.index + 1);
            }
            else {
                if(!fieldName.isEmpty())
                    map.put(fieldName, null);
                return resultIndex;
            }
            nextSymbol = getNextSymbol(filter);
        }
        map.put(filter, null);
        return -1;
    }

    private static class SymbolLocation{
        public int index;
        public char character;

        SymbolLocation(int index, char character) {
            this.index = index;
            this.character = character;
        }
    }

    private static SymbolLocation getNextSymbol(String filter){
        int comma = filter.indexOf(",");
        int open = filter.indexOf("{");
        int close = filter.indexOf("}");

        int min = Integer.MAX_VALUE;
        if(comma >= 0)
            min = Math.min(comma, min);
        if(open >= 0)
            min = Math.min(open, min);
        if(close >= 0)
            min = Math.min(close, min);

        if(comma == min)
            return new SymbolLocation(min, ',');
        if(open == min)
            return new SymbolLocation(min, '{');
        if(close == min)
            return new SymbolLocation(min, '}');

        return null;
    }
}

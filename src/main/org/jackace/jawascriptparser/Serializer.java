
/*
This file is part of JawaScript.

JawaScript is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JawaScript is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JawaScript.  If not, see <http://www.gnu.org/licenses/>.
*/
package main.org.jackace.jawascriptparser;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/27.
 */
public class Serializer {
    public Serializer() {}

    public static String toJSON(Token token) {
        String output = "{";
        output += "\"t\":" + token.type.ordinal() + ",";
        output += "\"v\":\"" + token.value + "\"}";
        return output;
    }

    public static String toJSON(AST ast) {
        String output = "{";

        output += "\"t\":" + ast.getType().ordinal() + "";
        TreeMap<PropType, Object> treeMap = new TreeMap<>();
        for (PropType key : ast.getProps().keySet()) {
            treeMap.put(key, ast.getProps().get(key));
        }
        for (PropType key : treeMap.keySet()) {
            output += ",\"" + key.ordinal() + "\":";
            if (treeMap.get(key) instanceof AST) {
                output += toJSON(((AST) treeMap.get(key)));
            } else if (treeMap.get(key) instanceof List) {
                output += "[";
                boolean isFirst = true;
                for(Object obj : (List<Object>)treeMap.get(key)) {
                    if (!isFirst)
                        output += ",";
                    isFirst = false;
                    if (obj instanceof AST)
                        output += toJSON((AST)(obj));
                    else if (obj instanceof Token)
                        output += toJSON((Token)(obj));
                    else if (obj == null)
                        output += "\"NULL\"";
                    else
                        output += "\"" + obj.toString() + "\"";
                }
                output += "]";
            }
            else
                output += "\"" + treeMap.get(key).toString() + "\"";
        }
        output += "}";
        return output;
    }

    public static String toString(AST ast, int level) {
        String output = "";
        String indent = "";
        for (int i = 0 ; i < level ; i++)
            indent += "  ";
        output += indent + ast.getType().name() + "\n";
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (PropType key : ast.getProps().keySet()) {
            treeMap.put(key.name(), ast.getProps().get(key));
        }
        for (String key : treeMap.keySet()) {
            output += indent + " " + key + " :";
            if (treeMap.get(key) instanceof AST) {
                output += "\n";
                output += toString(((AST) treeMap.get(key)), level + 1);
            } else if (treeMap.get(key) instanceof List) {
                output += "[\n";
                for(Object obj : (List<Object>)treeMap.get(key)) {
                    if (obj instanceof AST)
                        output += toString(((AST) (obj)), level + 1);
                    else if (obj instanceof Token)
                        output += indent + " " + ((Token)(obj)).value + "\n";
                    else if (obj == null)
                        output += indent + "  null\n";
                    else
                        output += indent + " " + obj.toString() + "\n";
                }
                output += indent + " " + "]\n";
            }
            else
                output += " " + treeMap.get(key).toString() + "\n";
        }
        return output;
    }
}

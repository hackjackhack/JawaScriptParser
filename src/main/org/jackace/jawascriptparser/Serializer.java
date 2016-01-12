/*
Copyright (c) 2016, Chi-Wei(Jack) Wang
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the intowow nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Chi-Wei(Jack) Wang BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
            if (key == PropType.valueType)
                continue;
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

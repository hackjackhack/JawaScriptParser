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

import java.util.HashMap;

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/6.
 */
public class AST {
    private ASTType astType;
    private HashMap<PropType, Object> props;

    protected AST(ASTType t) {
        astType = t;
        props = new HashMap<>();
    }

    public ASTType getType() {
        return astType;
    }

    public Object get(PropType key) {
        return props.get(key);
    }

    public void set(PropType key, Object val) {
        props.put(key, val);
    }

    public HashMap<PropType, Object> getProps() { return props; }
}

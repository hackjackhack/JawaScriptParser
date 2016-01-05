
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

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/4.
 */

public class Token {
    public TokenType type;
    public String value;
    public int start;
    public int end;

    public Token(TokenType type, String value, int start, int end) {
        this.type = type;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return type.name() + "," + value;
    }
}

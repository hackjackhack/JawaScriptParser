
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
package test.org.jackace.jawascriptparser;

import main.org.jackace.jawascriptparser.Token;
import main.org.jackace.jawascriptparser.Tokenizer;
import main.org.jackace.jawascriptparser.UnexpectedTokenException;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/6.
 */
public class TokenizerTest {
    @Test
    public void testTokenize1() throws Exception {
        String program = MultiLineStringLiteral.S(/*
            alert('aaa');
        */);
        Tokenizer tn = new Tokenizer();
        try {
            List<Token> tokens = tn.tokenize(program);
            String answer = MultiLineStringLiteral.S(/*
IDENTIFIER,alert
PUNCTUATOR,(
STRING_LITERAL,aaa
PUNCTUATOR,)
PUNCTUATOR,;
*/);
            String result = "\n";
            for (Token tk : tokens) {
                result += tk + "\n";
            }
            assertEquals(result, answer);
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            System.out.println(tn.cur_pos);
            throw e;
        }
    }

    @Test
    public void testTokenize2() throws Exception {
        String program = MultiLineStringLiteral.S(/*
function insertion_sort(arr) {
  for (var i = 0 ; i < arr.length ; i++) {
    var min = arr[i];
    var minp = i;
    for (var j = i + 1; j < arr.length ; j++) {
      if (arr[j] < min) {
        min = arr[j];
        minp = j;
      }
      arr[minp] = arr[i];
      arr[i] = min;
    }
  }
}
        */);
        Tokenizer tn = new Tokenizer();
        try {
            List<Token> tokens = tn.tokenize(program);
            String answer = MultiLineStringLiteral.S(/*
KEYWORD,function
IDENTIFIER,insertion_sort
PUNCTUATOR,(
IDENTIFIER,arr
PUNCTUATOR,)
PUNCTUATOR,{
KEYWORD,for
PUNCTUATOR,(
KEYWORD,var
IDENTIFIER,i
PUNCTUATOR,=
NUMERIC_LITERAL,0.0
PUNCTUATOR,;
IDENTIFIER,i
PUNCTUATOR,<
IDENTIFIER,arr
PUNCTUATOR,.
IDENTIFIER,length
PUNCTUATOR,;
IDENTIFIER,i
PUNCTUATOR,++
PUNCTUATOR,)
PUNCTUATOR,{
KEYWORD,var
IDENTIFIER,min
PUNCTUATOR,=
IDENTIFIER,arr
PUNCTUATOR,[
IDENTIFIER,i
PUNCTUATOR,]
PUNCTUATOR,;
KEYWORD,var
IDENTIFIER,minp
PUNCTUATOR,=
IDENTIFIER,i
PUNCTUATOR,;
KEYWORD,for
PUNCTUATOR,(
KEYWORD,var
IDENTIFIER,j
PUNCTUATOR,=
IDENTIFIER,i
PUNCTUATOR,+
NUMERIC_LITERAL,1.0
PUNCTUATOR,;
IDENTIFIER,j
PUNCTUATOR,<
IDENTIFIER,arr
PUNCTUATOR,.
IDENTIFIER,length
PUNCTUATOR,;
IDENTIFIER,j
PUNCTUATOR,++
PUNCTUATOR,)
PUNCTUATOR,{
KEYWORD,if
PUNCTUATOR,(
IDENTIFIER,arr
PUNCTUATOR,[
IDENTIFIER,j
PUNCTUATOR,]
PUNCTUATOR,<
IDENTIFIER,min
PUNCTUATOR,)
PUNCTUATOR,{
IDENTIFIER,min
PUNCTUATOR,=
IDENTIFIER,arr
PUNCTUATOR,[
IDENTIFIER,j
PUNCTUATOR,]
PUNCTUATOR,;
IDENTIFIER,minp
PUNCTUATOR,=
IDENTIFIER,j
PUNCTUATOR,;
PUNCTUATOR,}
IDENTIFIER,arr
PUNCTUATOR,[
IDENTIFIER,minp
PUNCTUATOR,]
PUNCTUATOR,=
IDENTIFIER,arr
PUNCTUATOR,[
IDENTIFIER,i
PUNCTUATOR,]
PUNCTUATOR,;
IDENTIFIER,arr
PUNCTUATOR,[
IDENTIFIER,i
PUNCTUATOR,]
PUNCTUATOR,=
IDENTIFIER,min
PUNCTUATOR,;
PUNCTUATOR,}
PUNCTUATOR,}
PUNCTUATOR,}
*/);
            String result = "\n";
            for (Token tk : tokens) {
                result += tk + "\n";
            }
            assertEquals(result, answer);
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            System.out.println(tn.cur_pos);
            throw e;
        }
    }

    @Test
    public void testTokenize3() throws Exception {
        String program = MultiLineStringLiteral.S(/*
            // this is a single line test
            alert('aaa');
            alert(1E36);
            alert(-.2345);
            alert(1.542e-10);
            alert(0x10ffff);
            alert(0b11111111);
            alert(0o123);
            alert(0123);
        */);
        Tokenizer tn = new Tokenizer();
        try {
            List<Token> tokens = tn.tokenize(program);
            String answer = MultiLineStringLiteral.S(/*
IDENTIFIER,alert
PUNCTUATOR,(
STRING_LITERAL,aaa
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
NUMERIC_LITERAL,1.0E36
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
PUNCTUATOR,-
NUMERIC_LITERAL,0.2345
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
NUMERIC_LITERAL,1.542E-10
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
NUMERIC_LITERAL,1114111
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
NUMERIC_LITERAL,255
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
NUMERIC_LITERAL,83
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
NUMERIC_LITERAL,83
PUNCTUATOR,)
PUNCTUATOR,;
*/);
            String result = "\n";
            for (Token tk : tokens) {
                result += tk + "\n";
            }
            assertEquals(result, answer);
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            System.out.println(tn.cur_pos);
            throw e;
        }
    }

    @Test
    public void testTokenize4() throws Exception {
        String program = MultiLineStringLiteral.S(/*
            alert("aaa");
            alert("\n\tabc");
            alert('\x32');
        */);
        Tokenizer tn = new Tokenizer();
        try {
            List<Token> tokens = tn.tokenize(program);
            String answer = MultiLineStringLiteral.S(/*
IDENTIFIER,alert
PUNCTUATOR,(
STRING_LITERAL,aaa
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
STRING_LITERAL,
	abc
PUNCTUATOR,)
PUNCTUATOR,;
IDENTIFIER,alert
PUNCTUATOR,(
STRING_LITERAL,2
PUNCTUATOR,)
PUNCTUATOR,;
*/);
            String result = "\n";
            for (Token tk : tokens) {
                result += tk + "\n";
            }
            assertEquals(result, answer);
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            System.out.println(tn.cur_pos);
            throw e;
        }
    }

    @Test
    public void testTokenize5() throws Exception {
        String program = MultiLineStringLiteral.S(/*
            var bt = true;
            var bf = false;
            var nul = null;
            do {
            } while (false);
            while (bf) {
            }
        */);
        Tokenizer tn = new Tokenizer();
        try {
            List<Token> tokens = tn.tokenize(program);
            String answer = MultiLineStringLiteral.S(/*
KEYWORD,var
IDENTIFIER,bt
PUNCTUATOR,=
BOOLEAN,true
PUNCTUATOR,;
KEYWORD,var
IDENTIFIER,bf
PUNCTUATOR,=
BOOLEAN,false
PUNCTUATOR,;
KEYWORD,var
IDENTIFIER,nul
PUNCTUATOR,=
NULL,null
PUNCTUATOR,;
KEYWORD,do
PUNCTUATOR,{
PUNCTUATOR,}
KEYWORD,while
PUNCTUATOR,(
BOOLEAN,false
PUNCTUATOR,)
PUNCTUATOR,;
KEYWORD,while
PUNCTUATOR,(
IDENTIFIER,bf
PUNCTUATOR,)
PUNCTUATOR,{
PUNCTUATOR,}
*/);
            String result = "\n";
            for (Token tk : tokens) {
                result += tk + "\n";
            }
            assertEquals(result, answer);
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            System.out.println(tn.cur_pos);
            throw e;
        }
    }
}
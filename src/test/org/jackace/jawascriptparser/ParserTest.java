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
package test.org.jackace.jawascriptparser;

import main.org.jackace.jawascriptparser.*;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/23.
 */
public class ParserTest {
    @Test
    public void testParser1() throws Exception {
        String program = MultiLineStringLiteral.S(/*
            alert('aaa');
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  CALL_EXPRESSION
   arguments :
    ARGUMENTS
     arguments :[
      LITERAL
       literal : STRING_LITERAL,aaa
       valueType : R
     ]
     valueType : R
   function :
    IDENTIFIER
     id : alert
     valueType : L
   valueType : R
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            System.out.println(Serializer.toJSON(tree));
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser2() throws Exception {
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
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      FOR_STATEMENT
       body :
        BLOCK_STATEMENT
         statements :[
          VAR_STATEMENT
           declarations :[
            VARIABLE_DECLARATION
             initialization :
              COMPUTED_MEMBER_EXPRESSION
               object :
                IDENTIFIER
                 id : arr
                 valueType : L
               property :
                IDENTIFIER
                 id : i
                 valueType : L
               valueType : L
             varName : min
           ]
          VAR_STATEMENT
           declarations :[
            VARIABLE_DECLARATION
             initialization :
              IDENTIFIER
               id : i
               valueType : L
             varName : minp
           ]
          FOR_STATEMENT
           body :
            BLOCK_STATEMENT
             statements :[
              IF_STATEMENT
               onTrue :
                BLOCK_STATEMENT
                 statements :[
                  ASSIGNMENT_EXPRESSION
                   left :
                    IDENTIFIER
                     id : min
                     valueType : L
                   op : PUNCTUATOR,=
                   right :
                    COMPUTED_MEMBER_EXPRESSION
                     object :
                      IDENTIFIER
                       id : arr
                       valueType : L
                     property :
                      IDENTIFIER
                       id : j
                       valueType : L
                     valueType : L
                   valueType : L
                  ASSIGNMENT_EXPRESSION
                   left :
                    IDENTIFIER
                     id : minp
                     valueType : L
                   op : PUNCTUATOR,=
                   right :
                    IDENTIFIER
                     id : j
                     valueType : L
                   valueType : L
                 ]
               test :
                RELATIONAL_EXPRESSION
                 ops :[
                 <
                 ]
                 subExpressions :[
                  COMPUTED_MEMBER_EXPRESSION
                   object :
                    IDENTIFIER
                     id : arr
                     valueType : L
                   property :
                    IDENTIFIER
                     id : j
                     valueType : L
                   valueType : L
                  IDENTIFIER
                   id : min
                   valueType : L
                 ]
                 valueType : R
              ASSIGNMENT_EXPRESSION
               left :
                COMPUTED_MEMBER_EXPRESSION
                 object :
                  IDENTIFIER
                   id : arr
                   valueType : L
                 property :
                  IDENTIFIER
                   id : minp
                   valueType : L
                 valueType : L
               op : PUNCTUATOR,=
               right :
                COMPUTED_MEMBER_EXPRESSION
                 object :
                  IDENTIFIER
                   id : arr
                   valueType : L
                 property :
                  IDENTIFIER
                   id : i
                   valueType : L
                 valueType : L
               valueType : L
              ASSIGNMENT_EXPRESSION
               left :
                COMPUTED_MEMBER_EXPRESSION
                 object :
                  IDENTIFIER
                   id : arr
                   valueType : L
                 property :
                  IDENTIFIER
                   id : i
                   valueType : L
                 valueType : L
               op : PUNCTUATOR,=
               right :
                IDENTIFIER
                 id : min
                 valueType : L
               valueType : L
             ]
           init :[
            VARIABLE_DECLARATION
             initialization :
              ADDITIVE_EXPRESSION
               ops :[
               +
               ]
               subExpressions :[
                IDENTIFIER
                 id : i
                 valueType : L
                LITERAL
                 literal : NUMERIC_LITERAL,1.0
                 valueType : R
               ]
               valueType : R
             varName : j
           ]
           test :
            RELATIONAL_EXPRESSION
             ops :[
             <
             ]
             subExpressions :[
              IDENTIFIER
               id : j
               valueType : L
              STATIC_MEMBER_EXPRESSION
               object :
                IDENTIFIER
                 id : arr
                 valueType : L
               property :
                IDENTIFIER
                 id : length
                 valueType : R
               valueType : L
             ]
             valueType : R
           update :
            POSTFIX_EXPRESSION
             op : PUNCTUATOR,++
             subExpression :
              IDENTIFIER
               id : j
               valueType : L
             valueType : R
         ]
       init :[
        VARIABLE_DECLARATION
         initialization :
          LITERAL
           literal : NUMERIC_LITERAL,0.0
           valueType : R
         varName : i
       ]
       test :
        RELATIONAL_EXPRESSION
         ops :[
         <
         ]
         subExpressions :[
          IDENTIFIER
           id : i
           valueType : L
          STATIC_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : arr
             valueType : L
           property :
            IDENTIFIER
             id : length
             valueType : R
           valueType : L
         ]
         valueType : R
       update :
        POSTFIX_EXPRESSION
         op : PUNCTUATOR,++
         subExpression :
          IDENTIFIER
           id : i
           valueType : L
         valueType : R
     ]
   id : insertion_sort
   params :[
   arr
   ]
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(Serializer.toString(tree, 0));
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser3() throws Exception {
        String program = MultiLineStringLiteral.S(/*
var a = {
    first : [
        'a', 'b', 1,
    ],
    second : {
        inner : ['a', 1, 2 ],
    }
};
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      OBJECT_EXPRESSION
       properties :[
        OBJECT_PROPERTY
         expr :
          ARRAY_EXPRESSION
           elements :[
            LITERAL
             literal : STRING_LITERAL,a
             valueType : R
            LITERAL
             literal : STRING_LITERAL,b
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.0
             valueType : R
           ]
         key : first
        OBJECT_PROPERTY
         expr :
          OBJECT_EXPRESSION
           properties :[
            OBJECT_PROPERTY
             expr :
              ARRAY_EXPRESSION
               elements :[
                LITERAL
                 literal : STRING_LITERAL,a
                 valueType : R
                LITERAL
                 literal : NUMERIC_LITERAL,1.0
                 valueType : R
                LITERAL
                 literal : NUMERIC_LITERAL,2.0
                 valueType : R
               ]
             key : inner
           ]
         key : second
       ]
     varName : a
   ]
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(Serializer.toString(tree, 0));
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser4() throws Exception {
        String program = MultiLineStringLiteral.S(/*
var arr = new Array(10);
function test(a, b, c) {
    return a * (b + c) >> 2;
}
function test2(x) {
    return test(x, 2*x, 3*x);
}
var xxx = test2(1);
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      NEW_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          LITERAL
           literal : NUMERIC_LITERAL,10.0
           valueType : R
         ]
         valueType : R
       constructor :
        IDENTIFIER
         id : Array
         valueType : L
       valueType : R
     varName : arr
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        SHIFT_EXPRESSION
         ops :[
         >>
         ]
         subExpressions :[
          MULTIPLICATIVE_EXPRESSION
           ops :[
           *
           ]
           subExpressions :[
            IDENTIFIER
             id : a
             valueType : L
            ADDITIVE_EXPRESSION
             ops :[
             +
             ]
             subExpressions :[
              IDENTIFIER
               id : b
               valueType : L
              IDENTIFIER
               id : c
               valueType : L
             ]
             valueType : R
           ]
           valueType : R
          LITERAL
           literal : NUMERIC_LITERAL,2.0
           valueType : R
         ]
         valueType : R
     ]
   id : test
   params :[
   a
   b
   c
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : x
             valueType : L
            MULTIPLICATIVE_EXPRESSION
             ops :[
             *
             ]
             subExpressions :[
              LITERAL
               literal : NUMERIC_LITERAL,2.0
               valueType : R
              IDENTIFIER
               id : x
               valueType : L
             ]
             valueType : R
            MULTIPLICATIVE_EXPRESSION
             ops :[
             *
             ]
             subExpressions :[
              LITERAL
               literal : NUMERIC_LITERAL,3.0
               valueType : R
              IDENTIFIER
               id : x
               valueType : L
             ]
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : test
           valueType : L
         valueType : R
     ]
   id : test2
   params :[
   x
   ]
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      CALL_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          LITERAL
           literal : NUMERIC_LITERAL,1.0
           valueType : R
         ]
         valueType : R
       function :
        IDENTIFIER
         id : test2
         valueType : L
       valueType : R
     varName : xxx
   ]
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(Serializer.toString(tree, 0));
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser5() throws Exception {
        String program = MultiLineStringLiteral.S(/*
var PouchUtils = {};
PouchUtils.Crypto = {};
function md5cycle(x, k) {
    var a = x[0], b = x[1], c = x[2], d = x[3];

    a = ff(a, b, c, d, k[0], 7, -680876936);
    d = ff(d, a, b, c, k[1], 12, -389564586);
    c = ff(c, d, a, b, k[2], 17, 606105819);
    b = ff(b, c, d, a, k[3], 22, -1044525330);
    a = ff(a, b, c, d, k[4], 7, -176418897);
    d = ff(d, a, b, c, k[5], 12, 1200080426);
    c = ff(c, d, a, b, k[6], 17, -1473231341);
    b = ff(b, c, d, a, k[7], 22, -45705983);
    a = ff(a, b, c, d, k[8], 7, 1770035416);
    d = ff(d, a, b, c, k[9], 12, -1958414417);
    c = ff(c, d, a, b, k[10], 17, -42063);
    b = ff(b, c, d, a, k[11], 22, -1990404162);
    a = ff(a, b, c, d, k[12], 7, 1804603682);
    d = ff(d, a, b, c, k[13], 12, -40341101);
    c = ff(c, d, a, b, k[14], 17, -1502002290);
    b = ff(b, c, d, a, k[15], 22, 1236535329);

    a = gg(a, b, c, d, k[1], 5, -165796510);
    d = gg(d, a, b, c, k[6], 9, -1069501632);
    c = gg(c, d, a, b, k[11], 14, 643717713);
    b = gg(b, c, d, a, k[0], 20, -373897302);
    a = gg(a, b, c, d, k[5], 5, -701558691);
    d = gg(d, a, b, c, k[10], 9, 38016083);
    c = gg(c, d, a, b, k[15], 14, -660478335);
    b = gg(b, c, d, a, k[4], 20, -405537848);
    a = gg(a, b, c, d, k[9], 5, 568446438);
    d = gg(d, a, b, c, k[14], 9, -1019803690);
    c = gg(c, d, a, b, k[3], 14, -187363961);
    b = gg(b, c, d, a, k[8], 20, 1163531501);
    a = gg(a, b, c, d, k[13], 5, -1444681467);
    d = gg(d, a, b, c, k[2], 9, -51403784);
    c = gg(c, d, a, b, k[7], 14, 1735328473);
    b = gg(b, c, d, a, k[12], 20, -1926607734);

    a = hh(a, b, c, d, k[5], 4, -378558);
    d = hh(d, a, b, c, k[8], 11, -2022574463);
    c = hh(c, d, a, b, k[11], 16, 1839030562);
    b = hh(b, c, d, a, k[14], 23, -35309556);
    a = hh(a, b, c, d, k[1], 4, -1530992060);
    d = hh(d, a, b, c, k[4], 11, 1272893353);
    c = hh(c, d, a, b, k[7], 16, -155497632);
    b = hh(b, c, d, a, k[10], 23, -1094730640);
    a = hh(a, b, c, d, k[13], 4, 681279174);
    d = hh(d, a, b, c, k[0], 11, -358537222);
    c = hh(c, d, a, b, k[3], 16, -722521979);
    b = hh(b, c, d, a, k[6], 23, 76029189);
    a = hh(a, b, c, d, k[9], 4, -640364487);
    d = hh(d, a, b, c, k[12], 11, -421815835);
    c = hh(c, d, a, b, k[15], 16, 530742520);
    b = hh(b, c, d, a, k[2], 23, -995338651);

    a = ii(a, b, c, d, k[0], 6, -198630844);
    d = ii(d, a, b, c, k[7], 10, 1126891415);
    c = ii(c, d, a, b, k[14], 15, -1416354905);
    b = ii(b, c, d, a, k[5], 21, -57434055);
    a = ii(a, b, c, d, k[12], 6, 1700485571);
    d = ii(d, a, b, c, k[3], 10, -1894986606);
    c = ii(c, d, a, b, k[10], 15, -1051523);
    b = ii(b, c, d, a, k[1], 21, -2054922799);
    a = ii(a, b, c, d, k[8], 6, 1873313359);
    d = ii(d, a, b, c, k[15], 10, -30611744);
    c = ii(c, d, a, b, k[6], 15, -1560198380);
    b = ii(b, c, d, a, k[13], 21, 1309151649);
    a = ii(a, b, c, d, k[4], 6, -145523070);
    d = ii(d, a, b, c, k[11], 10, -1120210379);
    c = ii(c, d, a, b, k[2], 15, 718787259);
    b = ii(b, c, d, a, k[9], 21, -343485551);

    x[0] = add32(a, x[0]);
    x[1] = add32(b, x[1]);
    x[2] = add32(c, x[2]);
    x[3] = add32(d, x[3]);

}

function cmn(q, a, b, x, s, t) {
    a = add32(add32(a, q), add32(x, t));
    return add32((a << s) | (a >>> (32 - s)), b);
}

function ff(a, b, c, d, x, s, t) {
    return cmn((b & c) | ((~b) & d), a, b, x, s, t);
}

function gg(a, b, c, d, x, s, t) {
    return cmn((b & d) | (c & (~d)), a, b, x, s, t);
}

function hh(a, b, c, d, x, s, t) {
    return cmn(b ^ c ^ d, a, b, x, s, t);
}

function ii(a, b, c, d, x, s, t) {
    return cmn(c ^ (b | (~d)), a, b, x, s, t);
}

function md51(s) {
    txt = '';
    var n = s.length,
    state = [1732584193, -271733879, -1732584194, 271733878], i;
    for (i = 64; i <= s.length; i += 64) {
        md5cycle(state, md5blk(s.subarray(i - 64, i)));
    }
    s = s.subarray(i - 64);
    var tail = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    for (i = 0; i < s.length; i++)
        tail[i >> 2] |= s[i] << ((i % 4) << 3);
    tail[i >> 2] |= 0x80 << ((i % 4) << 3);
    if (i > 55) {
        md5cycle(state, tail);
        for (i = 0; i < 16; i++) tail[i] = 0;
    }
    tail[14] = n * 8;
    md5cycle(state, tail);
    return state;
}


function md5blk(s) {
    var md5blks = [], i;
    for (i = 0; i < 64; i += 4) {
        md5blks[i >> 2] = s[i]
                        + (s[i + 1] << 8)
                        + (s[i + 2] << 16)
                        + (s[i + 3] << 24);
    }
    return md5blks;
}

var hex_chr = '0123456789abcdef'.split('');

function rhex(n) {
    var s = '', j = 0;
    for (; j < 4; j++)
        s += hex_chr[(n >> (j * 8 + 4)) & 0x0F]
             + hex_chr[(n >> (j * 8)) & 0x0F];
    return s;
}

function hex(x) {
    for (var i = 0; i < x.length; i++)
        x[i] = rhex(x[i]);
    return x.join('');
}

function md5(s) {
    return hex(md51(s));
}

function add32(a, b) {
    return (a + b) & 0xFFFFFFFF;
}
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      OBJECT_EXPRESSION
       properties :[
       ]
     varName : PouchUtils
   ]
  ASSIGNMENT_EXPRESSION
   left :
    STATIC_MEMBER_EXPRESSION
     object :
      IDENTIFIER
       id : PouchUtils
       valueType : L
     property :
      IDENTIFIER
       id : Crypto
       valueType : R
     valueType : L
   op : PUNCTUATOR,=
   right :
    OBJECT_EXPRESSION
     properties :[
     ]
   valueType : L
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      VAR_STATEMENT
       declarations :[
        VARIABLE_DECLARATION
         initialization :
          COMPUTED_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : x
             valueType : L
           property :
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
           valueType : L
         varName : a
        VARIABLE_DECLARATION
         initialization :
          COMPUTED_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : x
             valueType : L
           property :
            LITERAL
             literal : NUMERIC_LITERAL,1.0
             valueType : R
           valueType : L
         varName : b
        VARIABLE_DECLARATION
         initialization :
          COMPUTED_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : x
             valueType : L
           property :
            LITERAL
             literal : NUMERIC_LITERAL,2.0
             valueType : R
           valueType : L
         varName : c
        VARIABLE_DECLARATION
         initialization :
          COMPUTED_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : x
             valueType : L
           property :
            LITERAL
             literal : NUMERIC_LITERAL,3.0
             valueType : R
           valueType : L
         varName : d
       ]
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,0.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,7.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,6.80876936E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,1.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,12.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,3.89564586E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,2.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,17.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,6.06105819E8
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,3.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,22.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.04452533E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,4.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,7.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.76418897E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,5.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,12.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.200080426E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,6.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,17.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.473231341E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,7.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,22.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,4.5705983E7
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,8.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,7.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.770035416E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,9.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,12.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.958414417E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,10.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,17.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,42063.0
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,11.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,22.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.990404162E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,12.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,7.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.804603682E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,13.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,12.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,4.0341101E7
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,14.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,17.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.50200229E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,15.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,22.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.236535329E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ff
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,1.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,5.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.6579651E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,6.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,9.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.069501632E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,11.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,14.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,6.43717713E8
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,0.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,20.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,3.73897302E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,5.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,5.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,7.01558691E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,10.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,9.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,3.8016083E7
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,15.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,14.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,6.60478335E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,4.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,20.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,4.05537848E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,9.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,5.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,5.68446438E8
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,14.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,9.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.01980369E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,3.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,14.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.87363961E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,8.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,20.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.163531501E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,13.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,5.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.444681467E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,2.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,9.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,5.1403784E7
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,7.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,14.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.735328473E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,12.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,20.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.926607734E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : gg
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,5.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,4.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,378558.0
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,8.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,11.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,2.022574463E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,11.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,16.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.839030562E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,14.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,23.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,3.5309556E7
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,1.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,4.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.53099206E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,4.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,11.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.272893353E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,7.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,16.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.55497632E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,10.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,23.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.09473064E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,13.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,4.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,6.81279174E8
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,0.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,11.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,3.58537222E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,3.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,16.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,7.22521979E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,6.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,23.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,7.6029189E7
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,9.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,4.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,6.40364487E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,12.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,11.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,4.21815835E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,15.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,16.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,5.3074252E8
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,2.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,23.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,9.95338651E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hh
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,0.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,6.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.98630844E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,7.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,10.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.126891415E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,14.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,15.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.416354905E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,5.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,21.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,5.7434055E7
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,12.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,6.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.700485571E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,3.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,10.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.894986606E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,10.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,15.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1051523.0
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,1.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,21.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,2.054922799E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,8.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,6.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.873313359E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,15.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,10.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,3.0611744E7
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,6.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,15.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.56019838E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,13.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,21.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,1.309151649E9
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,4.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,6.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.4552307E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : d
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,11.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,10.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.120210379E9
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : c
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,2.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,15.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,7.18787259E8
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : b
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : c
             valueType : L
            IDENTIFIER
             id : d
             valueType : L
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : k
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,9.0
               valueType : R
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,21.0
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,3.43485551E8
               valueType : R
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : ii
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        COMPUTED_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : x
           valueType : L
         property :
          LITERAL
           literal : NUMERIC_LITERAL,0.0
           valueType : R
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : a
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : x
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,0.0
               valueType : R
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : add32
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        COMPUTED_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : x
           valueType : L
         property :
          LITERAL
           literal : NUMERIC_LITERAL,1.0
           valueType : R
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : b
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : x
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,1.0
               valueType : R
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : add32
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        COMPUTED_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : x
           valueType : L
         property :
          LITERAL
           literal : NUMERIC_LITERAL,2.0
           valueType : R
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : c
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : x
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,2.0
               valueType : R
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : add32
           valueType : L
         valueType : R
       valueType : L
      ASSIGNMENT_EXPRESSION
       left :
        COMPUTED_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : x
           valueType : L
         property :
          LITERAL
           literal : NUMERIC_LITERAL,3.0
           valueType : R
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            IDENTIFIER
             id : d
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : x
               valueType : L
             property :
              LITERAL
               literal : NUMERIC_LITERAL,3.0
               valueType : R
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : add32
           valueType : L
         valueType : R
       valueType : L
     ]
   id : md5cycle
   params :[
   x
   k
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : a
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            CALL_EXPRESSION
             arguments :
              ARGUMENTS
               arguments :[
                IDENTIFIER
                 id : a
                 valueType : L
                IDENTIFIER
                 id : q
                 valueType : L
               ]
               valueType : R
             function :
              IDENTIFIER
               id : add32
               valueType : L
             valueType : R
            CALL_EXPRESSION
             arguments :
              ARGUMENTS
               arguments :[
                IDENTIFIER
                 id : x
                 valueType : L
                IDENTIFIER
                 id : t
                 valueType : L
               ]
               valueType : R
             function :
              IDENTIFIER
               id : add32
               valueType : L
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : add32
           valueType : L
         valueType : R
       valueType : L
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            INCLUSIVE_OR_EXPRESSION
             subExpressions :[
              SHIFT_EXPRESSION
               ops :[
               <<
               ]
               subExpressions :[
                IDENTIFIER
                 id : a
                 valueType : L
                IDENTIFIER
                 id : s
                 valueType : L
               ]
               valueType : R
              SHIFT_EXPRESSION
               ops :[
               >>>
               ]
               subExpressions :[
                IDENTIFIER
                 id : a
                 valueType : L
                ADDITIVE_EXPRESSION
                 ops :[
                 -
                 ]
                 subExpressions :[
                  LITERAL
                   literal : NUMERIC_LITERAL,32.0
                   valueType : R
                  IDENTIFIER
                   id : s
                   valueType : L
                 ]
                 valueType : R
               ]
               valueType : R
             ]
             valueType : R
            IDENTIFIER
             id : b
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : add32
           valueType : L
         valueType : R
     ]
   id : cmn
   params :[
   q
   a
   b
   x
   s
   t
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            INCLUSIVE_OR_EXPRESSION
             subExpressions :[
              AND_EXPRESSION
               subExpressions :[
                IDENTIFIER
                 id : b
                 valueType : L
                IDENTIFIER
                 id : c
                 valueType : L
               ]
               valueType : R
              AND_EXPRESSION
               subExpressions :[
                UNARY_EXPRESSION
                 op : PUNCTUATOR,~
                 subExpression :
                  IDENTIFIER
                   id : b
                   valueType : L
                 valueType : R
                IDENTIFIER
                 id : d
                 valueType : L
               ]
               valueType : R
             ]
             valueType : R
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : x
             valueType : L
            IDENTIFIER
             id : s
             valueType : L
            IDENTIFIER
             id : t
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : cmn
           valueType : L
         valueType : R
     ]
   id : ff
   params :[
   a
   b
   c
   d
   x
   s
   t
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            INCLUSIVE_OR_EXPRESSION
             subExpressions :[
              AND_EXPRESSION
               subExpressions :[
                IDENTIFIER
                 id : b
                 valueType : L
                IDENTIFIER
                 id : d
                 valueType : L
               ]
               valueType : R
              AND_EXPRESSION
               subExpressions :[
                IDENTIFIER
                 id : c
                 valueType : L
                UNARY_EXPRESSION
                 op : PUNCTUATOR,~
                 subExpression :
                  IDENTIFIER
                   id : d
                   valueType : L
                 valueType : R
               ]
               valueType : R
             ]
             valueType : R
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : x
             valueType : L
            IDENTIFIER
             id : s
             valueType : L
            IDENTIFIER
             id : t
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : cmn
           valueType : L
         valueType : R
     ]
   id : gg
   params :[
   a
   b
   c
   d
   x
   s
   t
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            EXCLUSIVE_OR_EXPRESSION
             subExpressions :[
              IDENTIFIER
               id : b
               valueType : L
              IDENTIFIER
               id : c
               valueType : L
              IDENTIFIER
               id : d
               valueType : L
             ]
             valueType : R
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : x
             valueType : L
            IDENTIFIER
             id : s
             valueType : L
            IDENTIFIER
             id : t
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : cmn
           valueType : L
         valueType : R
     ]
   id : hh
   params :[
   a
   b
   c
   d
   x
   s
   t
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            EXCLUSIVE_OR_EXPRESSION
             subExpressions :[
              IDENTIFIER
               id : c
               valueType : L
              INCLUSIVE_OR_EXPRESSION
               subExpressions :[
                IDENTIFIER
                 id : b
                 valueType : L
                UNARY_EXPRESSION
                 op : PUNCTUATOR,~
                 subExpression :
                  IDENTIFIER
                   id : d
                   valueType : L
                 valueType : R
               ]
               valueType : R
             ]
             valueType : R
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
            IDENTIFIER
             id : x
             valueType : L
            IDENTIFIER
             id : s
             valueType : L
            IDENTIFIER
             id : t
             valueType : L
           ]
           valueType : R
         function :
          IDENTIFIER
           id : cmn
           valueType : L
         valueType : R
     ]
   id : ii
   params :[
   a
   b
   c
   d
   x
   s
   t
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : txt
         valueType : L
       op : PUNCTUATOR,=
       right :
        LITERAL
         literal : STRING_LITERAL,
         valueType : R
       valueType : L
      VAR_STATEMENT
       declarations :[
        VARIABLE_DECLARATION
         initialization :
          STATIC_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : s
             valueType : L
           property :
            IDENTIFIER
             id : length
             valueType : R
           valueType : L
         varName : n
        VARIABLE_DECLARATION
         initialization :
          ARRAY_EXPRESSION
           elements :[
            LITERAL
             literal : NUMERIC_LITERAL,1.732584193E9
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,2.71733879E8
               valueType : R
             valueType : R
            UNARY_EXPRESSION
             op : PUNCTUATOR,-
             subExpression :
              LITERAL
               literal : NUMERIC_LITERAL,1.732584194E9
               valueType : R
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,2.71733878E8
             valueType : R
           ]
         varName : state
        VARIABLE_DECLARATION
         varName : i
       ]
      FOR_STATEMENT
       body :
        BLOCK_STATEMENT
         statements :[
          CALL_EXPRESSION
           arguments :
            ARGUMENTS
             arguments :[
              IDENTIFIER
               id : state
               valueType : L
              CALL_EXPRESSION
               arguments :
                ARGUMENTS
                 arguments :[
                  CALL_EXPRESSION
                   arguments :
                    ARGUMENTS
                     arguments :[
                      ADDITIVE_EXPRESSION
                       ops :[
                       -
                       ]
                       subExpressions :[
                        IDENTIFIER
                         id : i
                         valueType : L
                        LITERAL
                         literal : NUMERIC_LITERAL,64.0
                         valueType : R
                       ]
                       valueType : R
                      IDENTIFIER
                       id : i
                       valueType : L
                     ]
                     valueType : R
                   function :
                    STATIC_MEMBER_EXPRESSION
                     object :
                      IDENTIFIER
                       id : s
                       valueType : L
                     property :
                      IDENTIFIER
                       id : subarray
                       valueType : R
                     valueType : L
                   valueType : R
                 ]
                 valueType : R
               function :
                IDENTIFIER
                 id : md5blk
                 valueType : L
               valueType : R
             ]
             valueType : R
           function :
            IDENTIFIER
             id : md5cycle
             valueType : L
           valueType : R
         ]
       init :
        ASSIGNMENT_EXPRESSION
         left :
          IDENTIFIER
           id : i
           valueType : L
         op : PUNCTUATOR,=
         right :
          LITERAL
           literal : NUMERIC_LITERAL,64.0
           valueType : R
         valueType : L
       test :
        RELATIONAL_EXPRESSION
         ops :[
         <=
         ]
         subExpressions :[
          IDENTIFIER
           id : i
           valueType : L
          STATIC_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : s
             valueType : L
           property :
            IDENTIFIER
             id : length
             valueType : R
           valueType : L
         ]
         valueType : R
       update :
        ASSIGNMENT_EXPRESSION
         left :
          IDENTIFIER
           id : i
           valueType : L
         op : PUNCTUATOR,+=
         right :
          LITERAL
           literal : NUMERIC_LITERAL,64.0
           valueType : R
         valueType : R
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : s
         valueType : L
       op : PUNCTUATOR,=
       right :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            ADDITIVE_EXPRESSION
             ops :[
             -
             ]
             subExpressions :[
              IDENTIFIER
               id : i
               valueType : L
              LITERAL
               literal : NUMERIC_LITERAL,64.0
               valueType : R
             ]
             valueType : R
           ]
           valueType : R
         function :
          STATIC_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : s
             valueType : L
           property :
            IDENTIFIER
             id : subarray
             valueType : R
           valueType : L
         valueType : R
       valueType : L
      VAR_STATEMENT
       declarations :[
        VARIABLE_DECLARATION
         initialization :
          ARRAY_EXPRESSION
           elements :[
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,0.0
             valueType : R
           ]
         varName : tail
       ]
      FOR_STATEMENT
       body :
        ASSIGNMENT_EXPRESSION
         left :
          COMPUTED_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : tail
             valueType : L
           property :
            SHIFT_EXPRESSION
             ops :[
             >>
             ]
             subExpressions :[
              IDENTIFIER
               id : i
               valueType : L
              LITERAL
               literal : NUMERIC_LITERAL,2.0
               valueType : R
             ]
             valueType : R
           valueType : L
         op : PUNCTUATOR,|=
         right :
          SHIFT_EXPRESSION
           ops :[
           <<
           ]
           subExpressions :[
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : s
               valueType : L
             property :
              IDENTIFIER
               id : i
               valueType : L
             valueType : L
            SHIFT_EXPRESSION
             ops :[
             <<
             ]
             subExpressions :[
              MULTIPLICATIVE_EXPRESSION
               ops :[
               %
               ]
               subExpressions :[
                IDENTIFIER
                 id : i
                 valueType : L
                LITERAL
                 literal : NUMERIC_LITERAL,4.0
                 valueType : R
               ]
               valueType : R
              LITERAL
               literal : NUMERIC_LITERAL,3.0
               valueType : R
             ]
             valueType : R
           ]
           valueType : R
         valueType : R
       init :
        ASSIGNMENT_EXPRESSION
         left :
          IDENTIFIER
           id : i
           valueType : L
         op : PUNCTUATOR,=
         right :
          LITERAL
           literal : NUMERIC_LITERAL,0.0
           valueType : R
         valueType : L
       test :
        RELATIONAL_EXPRESSION
         ops :[
         <
         ]
         subExpressions :[
          IDENTIFIER
           id : i
           valueType : L
          STATIC_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : s
             valueType : L
           property :
            IDENTIFIER
             id : length
             valueType : R
           valueType : L
         ]
         valueType : R
       update :
        POSTFIX_EXPRESSION
         op : PUNCTUATOR,++
         subExpression :
          IDENTIFIER
           id : i
           valueType : L
         valueType : R
      ASSIGNMENT_EXPRESSION
       left :
        COMPUTED_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : tail
           valueType : L
         property :
          SHIFT_EXPRESSION
           ops :[
           >>
           ]
           subExpressions :[
            IDENTIFIER
             id : i
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,2.0
             valueType : R
           ]
           valueType : R
         valueType : L
       op : PUNCTUATOR,|=
       right :
        SHIFT_EXPRESSION
         ops :[
         <<
         ]
         subExpressions :[
          LITERAL
           literal : NUMERIC_LITERAL,128
           valueType : R
          SHIFT_EXPRESSION
           ops :[
           <<
           ]
           subExpressions :[
            MULTIPLICATIVE_EXPRESSION
             ops :[
             %
             ]
             subExpressions :[
              IDENTIFIER
               id : i
               valueType : L
              LITERAL
               literal : NUMERIC_LITERAL,4.0
               valueType : R
             ]
             valueType : R
            LITERAL
             literal : NUMERIC_LITERAL,3.0
             valueType : R
           ]
           valueType : R
         ]
         valueType : R
       valueType : R
      IF_STATEMENT
       onTrue :
        BLOCK_STATEMENT
         statements :[
          CALL_EXPRESSION
           arguments :
            ARGUMENTS
             arguments :[
              IDENTIFIER
               id : state
               valueType : L
              IDENTIFIER
               id : tail
               valueType : L
             ]
             valueType : R
           function :
            IDENTIFIER
             id : md5cycle
             valueType : L
           valueType : R
          FOR_STATEMENT
           body :
            ASSIGNMENT_EXPRESSION
             left :
              COMPUTED_MEMBER_EXPRESSION
               object :
                IDENTIFIER
                 id : tail
                 valueType : L
               property :
                IDENTIFIER
                 id : i
                 valueType : L
               valueType : L
             op : PUNCTUATOR,=
             right :
              LITERAL
               literal : NUMERIC_LITERAL,0.0
               valueType : R
             valueType : L
           init :
            ASSIGNMENT_EXPRESSION
             left :
              IDENTIFIER
               id : i
               valueType : L
             op : PUNCTUATOR,=
             right :
              LITERAL
               literal : NUMERIC_LITERAL,0.0
               valueType : R
             valueType : L
           test :
            RELATIONAL_EXPRESSION
             ops :[
             <
             ]
             subExpressions :[
              IDENTIFIER
               id : i
               valueType : L
              LITERAL
               literal : NUMERIC_LITERAL,16.0
               valueType : R
             ]
             valueType : R
           update :
            POSTFIX_EXPRESSION
             op : PUNCTUATOR,++
             subExpression :
              IDENTIFIER
               id : i
               valueType : L
             valueType : R
         ]
       test :
        RELATIONAL_EXPRESSION
         ops :[
         >
         ]
         subExpressions :[
          IDENTIFIER
           id : i
           valueType : L
          LITERAL
           literal : NUMERIC_LITERAL,55.0
           valueType : R
         ]
         valueType : R
      ASSIGNMENT_EXPRESSION
       left :
        COMPUTED_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : tail
           valueType : L
         property :
          LITERAL
           literal : NUMERIC_LITERAL,14.0
           valueType : R
         valueType : L
       op : PUNCTUATOR,=
       right :
        MULTIPLICATIVE_EXPRESSION
         ops :[
         *
         ]
         subExpressions :[
          IDENTIFIER
           id : n
           valueType : L
          LITERAL
           literal : NUMERIC_LITERAL,8.0
           valueType : R
         ]
         valueType : R
       valueType : L
      CALL_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          IDENTIFIER
           id : state
           valueType : L
          IDENTIFIER
           id : tail
           valueType : L
         ]
         valueType : R
       function :
        IDENTIFIER
         id : md5cycle
         valueType : L
       valueType : R
      RETURN_STATEMENT
       argument :
        IDENTIFIER
         id : state
         valueType : L
     ]
   id : md51
   params :[
   s
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      VAR_STATEMENT
       declarations :[
        VARIABLE_DECLARATION
         initialization :
          ARRAY_EXPRESSION
           elements :[
           ]
         varName : md5blks
        VARIABLE_DECLARATION
         varName : i
       ]
      FOR_STATEMENT
       body :
        BLOCK_STATEMENT
         statements :[
          ASSIGNMENT_EXPRESSION
           left :
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : md5blks
               valueType : L
             property :
              SHIFT_EXPRESSION
               ops :[
               >>
               ]
               subExpressions :[
                IDENTIFIER
                 id : i
                 valueType : L
                LITERAL
                 literal : NUMERIC_LITERAL,2.0
                 valueType : R
               ]
               valueType : R
             valueType : L
           op : PUNCTUATOR,=
           right :
            ADDITIVE_EXPRESSION
             ops :[
             +
             +
             +
             ]
             subExpressions :[
              COMPUTED_MEMBER_EXPRESSION
               object :
                IDENTIFIER
                 id : s
                 valueType : L
               property :
                IDENTIFIER
                 id : i
                 valueType : L
               valueType : L
              SHIFT_EXPRESSION
               ops :[
               <<
               ]
               subExpressions :[
                COMPUTED_MEMBER_EXPRESSION
                 object :
                  IDENTIFIER
                   id : s
                   valueType : L
                 property :
                  ADDITIVE_EXPRESSION
                   ops :[
                   +
                   ]
                   subExpressions :[
                    IDENTIFIER
                     id : i
                     valueType : L
                    LITERAL
                     literal : NUMERIC_LITERAL,1.0
                     valueType : R
                   ]
                   valueType : R
                 valueType : L
                LITERAL
                 literal : NUMERIC_LITERAL,8.0
                 valueType : R
               ]
               valueType : R
              SHIFT_EXPRESSION
               ops :[
               <<
               ]
               subExpressions :[
                COMPUTED_MEMBER_EXPRESSION
                 object :
                  IDENTIFIER
                   id : s
                   valueType : L
                 property :
                  ADDITIVE_EXPRESSION
                   ops :[
                   +
                   ]
                   subExpressions :[
                    IDENTIFIER
                     id : i
                     valueType : L
                    LITERAL
                     literal : NUMERIC_LITERAL,2.0
                     valueType : R
                   ]
                   valueType : R
                 valueType : L
                LITERAL
                 literal : NUMERIC_LITERAL,16.0
                 valueType : R
               ]
               valueType : R
              SHIFT_EXPRESSION
               ops :[
               <<
               ]
               subExpressions :[
                COMPUTED_MEMBER_EXPRESSION
                 object :
                  IDENTIFIER
                   id : s
                   valueType : L
                 property :
                  ADDITIVE_EXPRESSION
                   ops :[
                   +
                   ]
                   subExpressions :[
                    IDENTIFIER
                     id : i
                     valueType : L
                    LITERAL
                     literal : NUMERIC_LITERAL,3.0
                     valueType : R
                   ]
                   valueType : R
                 valueType : L
                LITERAL
                 literal : NUMERIC_LITERAL,24.0
                 valueType : R
               ]
               valueType : R
             ]
             valueType : R
           valueType : L
         ]
       init :
        ASSIGNMENT_EXPRESSION
         left :
          IDENTIFIER
           id : i
           valueType : L
         op : PUNCTUATOR,=
         right :
          LITERAL
           literal : NUMERIC_LITERAL,0.0
           valueType : R
         valueType : L
       test :
        RELATIONAL_EXPRESSION
         ops :[
         <
         ]
         subExpressions :[
          IDENTIFIER
           id : i
           valueType : L
          LITERAL
           literal : NUMERIC_LITERAL,64.0
           valueType : R
         ]
         valueType : R
       update :
        ASSIGNMENT_EXPRESSION
         left :
          IDENTIFIER
           id : i
           valueType : L
         op : PUNCTUATOR,+=
         right :
          LITERAL
           literal : NUMERIC_LITERAL,4.0
           valueType : R
         valueType : R
      RETURN_STATEMENT
       argument :
        IDENTIFIER
         id : md5blks
         valueType : L
     ]
   id : md5blk
   params :[
   s
   ]
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      CALL_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          LITERAL
           literal : STRING_LITERAL,
           valueType : R
         ]
         valueType : R
       function :
        STATIC_MEMBER_EXPRESSION
         object :
          LITERAL
           literal : STRING_LITERAL,0123456789abcdef
           valueType : R
         property :
          IDENTIFIER
           id : split
           valueType : R
         valueType : L
       valueType : R
     varName : hex_chr
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      VAR_STATEMENT
       declarations :[
        VARIABLE_DECLARATION
         initialization :
          LITERAL
           literal : STRING_LITERAL,
           valueType : R
         varName : s
        VARIABLE_DECLARATION
         initialization :
          LITERAL
           literal : NUMERIC_LITERAL,0.0
           valueType : R
         varName : j
       ]
      FOR_STATEMENT
       body :
        ASSIGNMENT_EXPRESSION
         left :
          IDENTIFIER
           id : s
           valueType : L
         op : PUNCTUATOR,+=
         right :
          ADDITIVE_EXPRESSION
           ops :[
           +
           ]
           subExpressions :[
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : hex_chr
               valueType : L
             property :
              AND_EXPRESSION
               subExpressions :[
                SHIFT_EXPRESSION
                 ops :[
                 >>
                 ]
                 subExpressions :[
                  IDENTIFIER
                   id : n
                   valueType : L
                  ADDITIVE_EXPRESSION
                   ops :[
                   +
                   ]
                   subExpressions :[
                    MULTIPLICATIVE_EXPRESSION
                     ops :[
                     *
                     ]
                     subExpressions :[
                      IDENTIFIER
                       id : j
                       valueType : L
                      LITERAL
                       literal : NUMERIC_LITERAL,8.0
                       valueType : R
                     ]
                     valueType : R
                    LITERAL
                     literal : NUMERIC_LITERAL,4.0
                     valueType : R
                   ]
                   valueType : R
                 ]
                 valueType : R
                LITERAL
                 literal : NUMERIC_LITERAL,15
                 valueType : R
               ]
               valueType : R
             valueType : L
            COMPUTED_MEMBER_EXPRESSION
             object :
              IDENTIFIER
               id : hex_chr
               valueType : L
             property :
              AND_EXPRESSION
               subExpressions :[
                SHIFT_EXPRESSION
                 ops :[
                 >>
                 ]
                 subExpressions :[
                  IDENTIFIER
                   id : n
                   valueType : L
                  MULTIPLICATIVE_EXPRESSION
                   ops :[
                   *
                   ]
                   subExpressions :[
                    IDENTIFIER
                     id : j
                     valueType : L
                    LITERAL
                     literal : NUMERIC_LITERAL,8.0
                     valueType : R
                   ]
                   valueType : R
                 ]
                 valueType : R
                LITERAL
                 literal : NUMERIC_LITERAL,15
                 valueType : R
               ]
               valueType : R
             valueType : L
           ]
           valueType : R
         valueType : R
       test :
        RELATIONAL_EXPRESSION
         ops :[
         <
         ]
         subExpressions :[
          IDENTIFIER
           id : j
           valueType : L
          LITERAL
           literal : NUMERIC_LITERAL,4.0
           valueType : R
         ]
         valueType : R
       update :
        POSTFIX_EXPRESSION
         op : PUNCTUATOR,++
         subExpression :
          IDENTIFIER
           id : j
           valueType : L
         valueType : R
      RETURN_STATEMENT
       argument :
        IDENTIFIER
         id : s
         valueType : L
     ]
   id : rhex
   params :[
   n
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      FOR_STATEMENT
       body :
        ASSIGNMENT_EXPRESSION
         left :
          COMPUTED_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : x
             valueType : L
           property :
            IDENTIFIER
             id : i
             valueType : L
           valueType : L
         op : PUNCTUATOR,=
         right :
          CALL_EXPRESSION
           arguments :
            ARGUMENTS
             arguments :[
              COMPUTED_MEMBER_EXPRESSION
               object :
                IDENTIFIER
                 id : x
                 valueType : L
               property :
                IDENTIFIER
                 id : i
                 valueType : L
               valueType : L
             ]
             valueType : R
           function :
            IDENTIFIER
             id : rhex
             valueType : L
           valueType : R
         valueType : L
       init :[
        VARIABLE_DECLARATION
         initialization :
          LITERAL
           literal : NUMERIC_LITERAL,0.0
           valueType : R
         varName : i
       ]
       test :
        RELATIONAL_EXPRESSION
         ops :[
         <
         ]
         subExpressions :[
          IDENTIFIER
           id : i
           valueType : L
          STATIC_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : x
             valueType : L
           property :
            IDENTIFIER
             id : length
             valueType : R
           valueType : L
         ]
         valueType : R
       update :
        POSTFIX_EXPRESSION
         op : PUNCTUATOR,++
         subExpression :
          IDENTIFIER
           id : i
           valueType : L
         valueType : R
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            LITERAL
             literal : STRING_LITERAL,
             valueType : R
           ]
           valueType : R
         function :
          STATIC_MEMBER_EXPRESSION
           object :
            IDENTIFIER
             id : x
             valueType : L
           property :
            IDENTIFIER
             id : join
             valueType : R
           valueType : L
         valueType : R
     ]
   id : hex
   params :[
   x
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        CALL_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            CALL_EXPRESSION
             arguments :
              ARGUMENTS
               arguments :[
                IDENTIFIER
                 id : s
                 valueType : L
               ]
               valueType : R
             function :
              IDENTIFIER
               id : md51
               valueType : L
             valueType : R
           ]
           valueType : R
         function :
          IDENTIFIER
           id : hex
           valueType : L
         valueType : R
     ]
   id : md5
   params :[
   s
   ]
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      RETURN_STATEMENT
       argument :
        AND_EXPRESSION
         subExpressions :[
          ADDITIVE_EXPRESSION
           ops :[
           +
           ]
           subExpressions :[
            IDENTIFIER
             id : a
             valueType : L
            IDENTIFIER
             id : b
             valueType : L
           ]
           valueType : R
          LITERAL
           literal : NUMERIC_LITERAL,4294967295
           valueType : R
         ]
         valueType : R
     ]
   id : add32
   params :[
   a
   b
   ]
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(Serializer.toString(tree, 0));
            System.out.println(Serializer.toJSON(tree));
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser6() throws Exception {
        String program = MultiLineStringLiteral.S(/*
var i = 0;
do {
    i += 1;
    console.log(i);
} while (i < 5);

if (i % 2 == 0) {
    console.log('Even');
} else {
    console.log('Odd');
}

var str = 'ABCDE';
for (var c in str) {
    console.log(c);
}

while (i < 10000) {
    if (i == 327)
        continue;
    if (i % 5464 == 0)
        break;
    i += 1;
}
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      LITERAL
       literal : NUMERIC_LITERAL,0.0
       valueType : R
     varName : i
   ]
  DO_WHILE_STATEMENT
   body :
    BLOCK_STATEMENT
     statements :[
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : i
         valueType : L
       op : PUNCTUATOR,+=
       right :
        LITERAL
         literal : NUMERIC_LITERAL,1.0
         valueType : R
       valueType : R
      CALL_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          IDENTIFIER
           id : i
           valueType : L
         ]
         valueType : R
       function :
        STATIC_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : console
           valueType : L
         property :
          IDENTIFIER
           id : log
           valueType : R
         valueType : L
       valueType : R
     ]
   test :
    RELATIONAL_EXPRESSION
     ops :[
     <
     ]
     subExpressions :[
      IDENTIFIER
       id : i
       valueType : L
      LITERAL
       literal : NUMERIC_LITERAL,5.0
       valueType : R
     ]
     valueType : R
  IF_STATEMENT
   onFalse :
    BLOCK_STATEMENT
     statements :[
      CALL_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          LITERAL
           literal : STRING_LITERAL,Odd
           valueType : R
         ]
         valueType : R
       function :
        STATIC_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : console
           valueType : L
         property :
          IDENTIFIER
           id : log
           valueType : R
         valueType : L
       valueType : R
     ]
   onTrue :
    BLOCK_STATEMENT
     statements :[
      CALL_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          LITERAL
           literal : STRING_LITERAL,Even
           valueType : R
         ]
         valueType : R
       function :
        STATIC_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : console
           valueType : L
         property :
          IDENTIFIER
           id : log
           valueType : R
         valueType : L
       valueType : R
     ]
   test :
    EQUALITY_EXPRESSION
     ops :[
     ==
     ]
     subExpressions :[
      MULTIPLICATIVE_EXPRESSION
       ops :[
       %
       ]
       subExpressions :[
        IDENTIFIER
         id : i
         valueType : L
        LITERAL
         literal : NUMERIC_LITERAL,2.0
         valueType : R
       ]
       valueType : R
      LITERAL
       literal : NUMERIC_LITERAL,0.0
       valueType : R
     ]
     valueType : R
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      LITERAL
       literal : STRING_LITERAL,ABCDE
       valueType : R
     varName : str
   ]
  FOR_STATEMENT
   body :
    BLOCK_STATEMENT
     statements :[
      CALL_EXPRESSION
       arguments :
        ARGUMENTS
         arguments :[
          IDENTIFIER
           id : c
           valueType : L
         ]
         valueType : R
       function :
        STATIC_MEMBER_EXPRESSION
         object :
          IDENTIFIER
           id : console
           valueType : L
         property :
          IDENTIFIER
           id : log
           valueType : R
         valueType : L
       valueType : R
     ]
   iterator :
    ITERATOR_DECLARATION
     iterable :
      IDENTIFIER
       id : str
       valueType : L
     varName : c
  WHILE_STATEMENT
   body :
    BLOCK_STATEMENT
     statements :[
      IF_STATEMENT
       onTrue :
        CONTINUE_STATEMENT
       test :
        EQUALITY_EXPRESSION
         ops :[
         ==
         ]
         subExpressions :[
          IDENTIFIER
           id : i
           valueType : L
          LITERAL
           literal : NUMERIC_LITERAL,327.0
           valueType : R
         ]
         valueType : R
      IF_STATEMENT
       onTrue :
        BREAK_STATEMENT
       test :
        EQUALITY_EXPRESSION
         ops :[
         ==
         ]
         subExpressions :[
          MULTIPLICATIVE_EXPRESSION
           ops :[
           %
           ]
           subExpressions :[
            IDENTIFIER
             id : i
             valueType : L
            LITERAL
             literal : NUMERIC_LITERAL,5464.0
             valueType : R
           ]
           valueType : R
          LITERAL
           literal : NUMERIC_LITERAL,0.0
           valueType : R
         ]
         valueType : R
      ASSIGNMENT_EXPRESSION
       left :
        IDENTIFIER
         id : i
         valueType : L
       op : PUNCTUATOR,+=
       right :
        LITERAL
         literal : NUMERIC_LITERAL,1.0
         valueType : R
       valueType : R
     ]
   test :
    RELATIONAL_EXPRESSION
     ops :[
     <
     ]
     subExpressions :[
      IDENTIFIER
       id : i
       valueType : L
      LITERAL
       literal : NUMERIC_LITERAL,10000.0
       valueType : R
     ]
     valueType : R
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(tree.toString());
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser7() throws Exception {
        String program = MultiLineStringLiteral.S(/*
var b = 'abcde' in 'abcdefg';
if ('bb' in 'abbc')
    console.log('XX');
;
;
function xx () {
var i = 1;
++i;
--i;
;
}
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      IN_EXPRESSION
       subExpressions :[
        LITERAL
         literal : STRING_LITERAL,abcde
         valueType : R
        LITERAL
         literal : STRING_LITERAL,abcdefg
         valueType : R
       ]
       valueType : R
     varName : b
   ]
  IF_STATEMENT
   onTrue :
    CALL_EXPRESSION
     arguments :
      ARGUMENTS
       arguments :[
        LITERAL
         literal : STRING_LITERAL,XX
         valueType : R
       ]
       valueType : R
     function :
      STATIC_MEMBER_EXPRESSION
       object :
        IDENTIFIER
         id : console
         valueType : L
       property :
        IDENTIFIER
         id : log
         valueType : R
       valueType : L
     valueType : R
   test :
    IN_EXPRESSION
     subExpressions :[
      LITERAL
       literal : STRING_LITERAL,bb
       valueType : R
      LITERAL
       literal : STRING_LITERAL,abbc
       valueType : R
     ]
     valueType : R
  EMPTY_STATEMENT
  EMPTY_STATEMENT
  FUNCTION_DECLARATION
   body :
    BLOCK_STATEMENT
     statements :[
      VAR_STATEMENT
       declarations :[
        VARIABLE_DECLARATION
         initialization :
          LITERAL
           literal : NUMERIC_LITERAL,1.0
           valueType : R
         varName : i
       ]
      UNARY_EXPRESSION
       op : PUNCTUATOR,++
       subExpression :
        IDENTIFIER
         id : i
         valueType : L
       valueType : R
      UNARY_EXPRESSION
       op : PUNCTUATOR,--
       subExpression :
        IDENTIFIER
         id : i
         valueType : L
       valueType : R
      EMPTY_STATEMENT
     ]
   id : xx
   params :[
   ]
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(tree.toString());
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser8() throws Exception {
        String program = MultiLineStringLiteral.S(/*
var a = 1, b = 2;
(a == 2 || b == 1 && a == b);
a = b == 2 ? 'XX' : 4;
b = 'xx', a = 'yy';
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      LITERAL
       literal : NUMERIC_LITERAL,1.0
       valueType : R
     varName : a
    VARIABLE_DECLARATION
     initialization :
      LITERAL
       literal : NUMERIC_LITERAL,2.0
       valueType : R
     varName : b
   ]
  LOGICAL_OR_EXPRESSION
   subExpressions :[
    EQUALITY_EXPRESSION
     ops :[
     ==
     ]
     subExpressions :[
      IDENTIFIER
       id : a
       valueType : L
      LITERAL
       literal : NUMERIC_LITERAL,2.0
       valueType : R
     ]
     valueType : R
    LOGICAL_AND_EXPRESSION
     subExpressions :[
      EQUALITY_EXPRESSION
       ops :[
       ==
       ]
       subExpressions :[
        IDENTIFIER
         id : b
         valueType : L
        LITERAL
         literal : NUMERIC_LITERAL,1.0
         valueType : R
       ]
       valueType : R
      EQUALITY_EXPRESSION
       ops :[
       ==
       ]
       subExpressions :[
        IDENTIFIER
         id : a
         valueType : L
        IDENTIFIER
         id : b
         valueType : L
       ]
       valueType : R
     ]
     valueType : R
   ]
   valueType : R
  ASSIGNMENT_EXPRESSION
   left :
    IDENTIFIER
     id : a
     valueType : L
   op : PUNCTUATOR,=
   right :
    CONDITIONAL_EXPRESSION
     condition :
      EQUALITY_EXPRESSION
       ops :[
       ==
       ]
       subExpressions :[
        IDENTIFIER
         id : b
         valueType : L
        LITERAL
         literal : NUMERIC_LITERAL,2.0
         valueType : R
       ]
       valueType : R
     onFalse :
      LITERAL
       literal : NUMERIC_LITERAL,4.0
       valueType : R
     onTrue :
      LITERAL
       literal : STRING_LITERAL,XX
       valueType : R
     valueType : R
   valueType : L
  SEQUENCE_EXPRESSION
   expressions :[
    ASSIGNMENT_EXPRESSION
     left :
      IDENTIFIER
       id : b
       valueType : L
     op : PUNCTUATOR,=
     right :
      LITERAL
       literal : STRING_LITERAL,xx
       valueType : R
     valueType : L
    ASSIGNMENT_EXPRESSION
     left :
      IDENTIFIER
       id : a
       valueType : L
     op : PUNCTUATOR,=
     right :
      LITERAL
       literal : STRING_LITERAL,yy
       valueType : R
     valueType : L
   ]
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(tree.toString());
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser9() throws Exception {
        String program = MultiLineStringLiteral.S(/*
var a = [1,2,3,,4];
var b = [1,2,3,,];
var c = [1,2,3,];
var d = new Array(10).length;
var e = new Buffer(10)[5];
        */);
        String answer = MultiLineStringLiteral.S(/*
SCRIPT_BODY
 statements :[
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      ARRAY_EXPRESSION
       elements :[
        LITERAL
         literal : NUMERIC_LITERAL,1.0
         valueType : R
        LITERAL
         literal : NUMERIC_LITERAL,2.0
         valueType : R
        LITERAL
         literal : NUMERIC_LITERAL,3.0
         valueType : R
        null
        LITERAL
         literal : NUMERIC_LITERAL,4.0
         valueType : R
       ]
     varName : a
   ]
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      ARRAY_EXPRESSION
       elements :[
        LITERAL
         literal : NUMERIC_LITERAL,1.0
         valueType : R
        LITERAL
         literal : NUMERIC_LITERAL,2.0
         valueType : R
        LITERAL
         literal : NUMERIC_LITERAL,3.0
         valueType : R
        null
       ]
     varName : b
   ]
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      ARRAY_EXPRESSION
       elements :[
        LITERAL
         literal : NUMERIC_LITERAL,1.0
         valueType : R
        LITERAL
         literal : NUMERIC_LITERAL,2.0
         valueType : R
        LITERAL
         literal : NUMERIC_LITERAL,3.0
         valueType : R
       ]
     varName : c
   ]
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      STATIC_MEMBER_EXPRESSION
       object :
        NEW_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            LITERAL
             literal : NUMERIC_LITERAL,10.0
             valueType : R
           ]
           valueType : R
         constructor :
          IDENTIFIER
           id : Array
           valueType : L
         valueType : R
       property :
        IDENTIFIER
         id : length
         valueType : R
       valueType : L
     varName : d
   ]
  VAR_STATEMENT
   declarations :[
    VARIABLE_DECLARATION
     initialization :
      COMPUTED_MEMBER_EXPRESSION
       object :
        NEW_EXPRESSION
         arguments :
          ARGUMENTS
           arguments :[
            LITERAL
             literal : NUMERIC_LITERAL,10.0
             valueType : R
           ]
           valueType : R
         constructor :
          IDENTIFIER
           id : Buffer
           valueType : L
         valueType : R
       property :
        LITERAL
         literal : NUMERIC_LITERAL,5.0
         valueType : R
       valueType : L
     varName : e
   ]
 ]
*/);
        Tokenizer tn = new Tokenizer();
        try {
            ArrayList<Token> tokens = tn.tokenize(program);
            Parser ps = new Parser();
            AST tree = ps.parse(tokens);
            //System.out.println(tree.toString());
            assertEquals(Serializer.toString(tree, 0).trim(), answer.trim());
        } catch (UnexpectedTokenException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}

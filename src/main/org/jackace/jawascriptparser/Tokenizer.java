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

import java.util.ArrayList;

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/4.
 */
public class Tokenizer {
    private String code = "";
    public int cur_pos = 0;

    private boolean eof() {
        return cur_pos >= code.length();
    }

    private char c() {
        return code.charAt(cur_pos);
    }

    private boolean isIdentifierStart(char c) {
        return c == '$' || c == '_' ||
                (c >= 'A' && c <= 'Z') ||
                (c >= 'a' && c <= 'z');
    }

    private boolean isOctal(char c) {
        return ('0' <= c && c <= '7');
    }

    private int hexValue(char c) {
        return "0123456789abcdef".indexOf(Character.toLowerCase(c));
    }

    private int parseHex(char prefix) {
        int len = prefix == 'u' ? 4 : 2;
        int code = 0;

        for (int i = 0 ; i < len ; i++) {
            if (!eof()) {
                char c = c();
                if (hexValue(c) >= 0) {
                    code = (code << 4) + hexValue(c);
                    cur_pos++;
                }
            } else
                return -1;
        }
        return code;
    }

    private boolean isImplicitOctalLiteral() {
        for (int i = cur_pos + 1; i < code.length(); ++i) {
            char c = code.charAt(i);
            if (c == '8' || c == '9') {
                return false;
            }
            if (!isOctal(c)) {
                return true;
            }
        }

        return true;
    }

    private boolean isKeyword(String id) {
        switch (id.length()) {
            case 2:
                return id.equals("if") || id.equals("in") || id.equals("do");
            case 3:
                return id.equals("var") || id.equals("for") || id.equals("new") ||
                        id.equals("let");
            case 4:
                return id.equals("else") ||
                        id.equals("void") || id.equals("with") || id.equals("enum");
            case 5:
                return id.equals("while") || id.equals("break");
            case 6:
                return id.equals("return") || id.equals("typeof") || id.equals("delete");
            case 7:
                return id.equals("default");
            case 8:
                return id.equals("function") || id.equals("continue");
            case 10:
                return id.equals("instanceof");
            default:
                return false;
        }
    }

    private Token parseHexLiteral(int start) throws UnexpectedTokenException {
        String number = "";
        int begin = cur_pos;

        while (!eof()) {
            char c = c();
            if (hexValue(c) < 0)
                break;
            number += c;
            cur_pos++;
        }

        if (cur_pos == begin || (!eof() && isIdentifierStart(c()))) {
            throw new UnexpectedTokenException(code.substring(start, cur_pos));
        }
        return new Token(TokenType.NUMERIC_LITERAL,
                Long.toString(Long.parseLong(number, 16)),
                start, cur_pos);
    }

    private Token parseBinaryLiteral(int start) throws UnexpectedTokenException {
        String number = "";
        int begin = cur_pos;

        while (!eof()) {
            char c = c();
            if (c != '0' && c != '1')
                break;
            number += c;
            cur_pos++;
        }

        if (cur_pos == begin || (!eof() && (isIdentifierStart(c()) || Character.isDigit(c())))) {
            throw new UnexpectedTokenException(code.substring(start, cur_pos));
        }
        return new Token(TokenType.NUMERIC_LITERAL,
                Long.toString(Long.parseLong(number, 2)),
                start, cur_pos);
    }

    private Token parseOctalLiteral(char prefix, int start) throws UnexpectedTokenException {
        String number = "";
        boolean octal = false;

        if (isOctal(prefix)) {
            octal = true;
            number = "0" + c();
        }
        cur_pos++;
        int begin = cur_pos;

        while (!eof()) {
            char c = c();
            if (!isOctal(c))
                break;
            number += c;
            cur_pos++;
        }

        if ((!octal && begin == cur_pos) ||
                (!eof() && (isIdentifierStart(c()) ||
                        Character.isDigit(c()))))
            throw new UnexpectedTokenException(code.substring(start, cur_pos));

        return new Token(TokenType.NUMERIC_LITERAL,
                Long.toString(Long.parseLong(number, 8)),
                start, cur_pos);
    }

    private Token parseIdentifier() {
        int start = cur_pos;
        while (!eof()) {
            char c = c();
            if (isIdentifierStart(c) || Character.isDigit(c))
                ++cur_pos;
            else
                break;
        }
        String id = code.substring(start, cur_pos);
        TokenType type;

        if (id.length() == 1)
            type = TokenType.IDENTIFIER;
        else if (isKeyword(id))
            type = TokenType.KEYWORD;
        else if (id.equals("null"))
            type = TokenType.NULL;
        else if (id.equals("true") || id.equals("false"))
            type = TokenType.BOOLEAN;
        else
            type = TokenType.IDENTIFIER;

        return new Token(type, id, start, cur_pos);
    }

    private Token parsePunctuator() throws UnexpectedTokenException {
        int start = cur_pos;

        char c = c();
        switch (c) {
            case '{':
                ++cur_pos;
                break;
            case '}':
                ++cur_pos;
                break;
            case '.':
            case '(':
            case ')':
            case ';':
            case ',':
            case '[':
            case ']':
            case ':':
            case '?':
            case '~':
                ++cur_pos;
                break;
            default:

                String val = code.substring(cur_pos, Math.min(cur_pos + 4, code.length()));
                if (val.equals(">>>="))
                    cur_pos += 4;
                else {
                    val = val.substring(0, Math.min(val.length(), 3));
                    if (val.equals("==") || val.equals("!==") || val.equals(">>>") ||
                            val.equals("<<=") || val.equals(">>="))
                        cur_pos += 3;
                    else {
                        val = val.substring(0, Math.min(val.length(), 2));
                        if (val.equals("&&") || val.equals("||") || val.equals("==") || val.equals("!=") ||
                                val.equals("+=") || val.equals("-=") || val.equals("*=") || val.equals("/=") ||
                                val.equals("++") || val.equals("--") || val.equals("<<") || val.equals(">>") ||
                                val.equals("&=") || val.equals("|=") || val.equals("^=") || val.equals("%=") ||
                                val.equals("<=") || val.equals(">=") || val.equals("=>") )
                            cur_pos += 2;
                        else {
                            val = val.substring(0,1);
                            if ("<>=!+-*%&|^/".contains(val))
                                cur_pos++;
                        }
                    }
                }
        }

        if (start == cur_pos)
            throw new UnexpectedTokenException(code.substring(cur_pos, cur_pos + 1));
        return new Token(TokenType.PUNCTUATOR, code.substring(start, cur_pos), start, cur_pos);
    }

    private Token parseStringLiteral() throws UnexpectedTokenException {
        int start = cur_pos;
        char quote = c();

        cur_pos++;
        String str = "";

        while (!eof()) {
            char c = c();
            cur_pos++;
            if (c == quote) {
                quote = 255;
                break;
            } else if (c == '\\') {
                if (eof())
                    break;
                c = c();
                cur_pos++;
                if (c != '\r' && c != '\n') {
                    switch(c) {
                        case 'u':
                        case 'x':
                            int unescaped = parseHex(c);
                            if (unescaped < 0)
                                throw new UnexpectedTokenException(code.substring(start, cur_pos));
                            str += (char)unescaped;
                            break;
                        case 'n':
                            str += '\n';
                            break;
                        case 'r':
                            str += '\r';
                            break;
                        case 't':
                            str += '\t';
                            break;
                        case 'v':
                            str += (char)(0x0b);
                            break;
                        default:
                            str += c;
                            break;
                    }
                }
            } else if (c == '\r' || c == '\n') {
                break;
            } else
                str += c;
        }

        if (quote != 255)
            throw new UnexpectedTokenException(code.substring(start, cur_pos));
        return new Token(TokenType.STRING_LITERAL, str, start, cur_pos);
    }

    private Token parseNumericLiteral() throws UnexpectedTokenException, EarlyEOFException {
        int start = cur_pos;
        char c = c();
        String number = "";

        if (c != '.') {
            number += c;
            cur_pos++;
            if (!eof()) {
                char d = c();

                if (c == '0') {
                    if (d == 'x' || d == 'X') {
                        cur_pos++;
                        return parseHexLiteral(start);
                    }
                    if (d == 'b' || d == 'B') {
                        cur_pos++;
                        return parseBinaryLiteral(start);
                    }
                    if (d == 'o' || d == 'O')
                        return parseOctalLiteral(d, start);
                    if (isOctal(d) && isImplicitOctalLiteral())
                        return parseOctalLiteral(d, start);
                }

                while (!eof()) {
                    d = c();
                    if (Character.isDigit(d)) {
                        number += d;
                        cur_pos++;
                        continue;
                    }
                    break;
                }

                c = c();
            }
        }

        if (c == '.') {
            number += c;
            cur_pos++;
            while (!eof() && Character.isDigit(c())) {
                number += c();
                cur_pos++;
            }
        }

        if (!eof()) {
            c = c();
            if (c == 'e' || c == 'E') {
                number += c;
                cur_pos++;
                if (eof())
                    throw new EarlyEOFException();
                c = c();
                if (c == '+' || c == '-') {
                    number += c;
                    cur_pos++;
                    if (eof())
                        throw new EarlyEOFException();
                    c = c();
                }
                if (Character.isDigit(c)) {
                    do {
                        number += c;
                        cur_pos++;
                        if (eof())
                            break;
                        c = c();
                    } while (Character.isDigit(c));
                } else
                    throw new UnexpectedTokenException(code.substring(start, cur_pos));
            }
        }

        if (!eof() && isIdentifierStart(c()))
            throw new UnexpectedTokenException(code.substring(start, cur_pos + 1));
        return new Token(TokenType.NUMERIC_LITERAL,
                Double.toString(Double.parseDouble(number)),
                start, cur_pos);
    }

    private void skipSingleLineComment() {
        while (!eof()) {
            char c = c();
            cur_pos++;
            if (c == '\r' || c == '\n') {
                if (c == '\r' && !eof() && c() == '\n')
                    cur_pos++;
                return;
            }
        }
    }

    private void skipMultiLineComment() {
        while (!eof()) {
            char c = c();
            cur_pos++;
            if (c == '*') {
                if (!eof() && c() == '/') {
                    cur_pos++;
                    return;
                }
            }
        }
    }

    private void skipComments() {
        while (!eof()) {
            char c = c();

            if (Character.isWhitespace(c)) {
                cur_pos++;
            } else if (c == '/') {
                // Peek next character
                if (cur_pos + 1 < code.length()) {
                    char d = code.charAt(cur_pos + 1);
                    if (d == '/') {
                        cur_pos += 2;
                        skipSingleLineComment();
                    } else if (d == '*') {
                        cur_pos += 2;
                        skipMultiLineComment();
                    } else
                        break;
                } else
                    break;
            } else
                break;
        }
    }

    private Token nextToken() throws UnexpectedTokenException, EarlyEOFException {
        skipComments();
        if (eof())
            return new Token(TokenType.EOF, "", cur_pos, cur_pos);

        char c = c();
        if (isIdentifierStart(c))
            return parseIdentifier();
        if (c == '\'' || c == '"')
            return parseStringLiteral();

        // A dot can start a floating number, peek the next character
        if (c == '.') {
            if (Character.isDigit(code.charAt(cur_pos + 1)))
                return parseNumericLiteral();
            return parsePunctuator();
        }

        if (Character.isDigit(c))
            return parseNumericLiteral();

        return parsePunctuator();
    }

    public Tokenizer() {}

    public ArrayList<Token> tokenize(String program) throws UnexpectedTokenException, EarlyEOFException {
        cur_pos = 0;
        code = program;
        ArrayList<Token> tokens = new ArrayList<>();
        while (!eof()) {
            Token token = nextToken();
            if (token.type == TokenType.EOF)
                break;
            tokens.add(token);
        }
        return tokens;
    }

}

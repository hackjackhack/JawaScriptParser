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
import java.util.LinkedList;

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/6.
 */
public class Parser {
    private ArrayList<Token> tokens;
    private int index = 0;
    private boolean inIteration = false;
    private boolean inFunctionBody = false;

    private boolean eof() {
        return index >= tokens.size() || tokens.get(index).type == TokenType.EOF;
    }

    private Token peek() throws EarlyEOFException {
        if (eof())
            throw new EarlyEOFException();
        return tokens.get(index);
    }

    private Token next() throws EarlyEOFException {
        Token token = peek();
        index++;
        return token;
    }

    private boolean isKeyword(String expected) throws EarlyEOFException, UnexpectedTokenException {
        Token peek = peek();
        return peek.type == TokenType.KEYWORD && peek.value.equals(expected);
    }

    private void eatKeyword(String expected) throws EarlyEOFException, UnexpectedTokenException {
        Token token = next();
        if (token.type != TokenType.KEYWORD || !token.value.equals(expected))
            throw new UnexpectedTokenException(token.value);
    }

    private boolean isPunctuator(String expected) throws EarlyEOFException, UnexpectedTokenException {
        Token peek = peek();
        return peek.type == TokenType.PUNCTUATOR && peek.value.equals(expected);
    }

    private void eatPunctuator(String expected) throws EarlyEOFException, UnexpectedTokenException {
        Token token = next();
        if (token.type != TokenType.PUNCTUATOR || !token.value.equals(expected))
            throw new UnexpectedTokenException(token.value);
    }

    private String eatIdentifier() throws EarlyEOFException, UnexpectedTokenException {
        Token token = next();
        if (token.type != TokenType.IDENTIFIER)
            throw new UnexpectedTokenException(token.value);
        return token.value;
    }

    private LinkedList<String> parseParams() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseParams");
        eatPunctuator("(");
        LinkedList<String> params = new LinkedList<>();
        while (true) {
            if (isPunctuator(")")) {
                next();
                break;
            }
            String param = eatIdentifier();
            params.add(param);
            if (eof())
                throw new EarlyEOFException();
            Token peek = peek();
            if (peek.value.equals(","))
                eatPunctuator(",");
            else if (peek.value.equals(")")) {
                eatPunctuator(")");
                break;
            } else
                throw new UnexpectedTokenException(peek.value);
        }

        return params;
    }

    private AST parseBlock() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseBlock");
        AST blockStatement = new AST(ASTType.BLOCK_STATEMENT);
        LinkedList<AST> statements = new LinkedList<>();
        eatPunctuator("{");
        while (!eof()) {
            statements.add(parseStatement());
            if (peek().value.equals("}"))
                break;
        }
        eatPunctuator("}");
        blockStatement.set(PropType.statements, statements);
        return blockStatement;
    }

    private AST parseEmptyStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseEmptyStatement");
        AST emptyStatement = new AST(ASTType.EMPTY_STATEMENT);
        eatPunctuator(";");
        return emptyStatement;
    }

    private AST parseArguments() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseArguments");
        eatPunctuator("(");
        AST arguments = new AST(ASTType.ARGUMENTS);
        arguments.set(PropType.valueType, "R");
        LinkedList<AST> args = new LinkedList<>();
        if (!isPunctuator(")")) {
            while (!eof()) {
                args.add(parseAssignmentExpression());
                if (isPunctuator(")"))
                    break;
                eatPunctuator(",");
            }
        }
        eatPunctuator(")");
        arguments.set(PropType.arguments, args);
        return arguments;
    }

    private AST parseNonComputedProperty() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseNonComputedProperty");
        Token token = next();
        if (token.type != TokenType.IDENTIFIER)
            throw new UnexpectedTokenException(token.value);
        AST identifier = new AST(ASTType.IDENTIFIER);
        identifier.set(PropType.id, token.value);
        identifier.set(PropType.valueType, "R");
        return identifier;
    }

    private AST parseNonComputedMember() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseNonComputedMember");
        eatPunctuator(".");
        return parseNonComputedProperty();
    }

    private AST parseComputedMember() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseComputedMember");
        eatPunctuator("[");
        AST expression = parseExpression();
        eatPunctuator("]");
        return expression;
    }

    private AST parseObjectProperty() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseObjectProperty");
        String key = eatIdentifier();
        eatPunctuator(":");
        AST expr = parseAssignmentExpression();
        AST objectProperty = new AST(ASTType.OBJECT_PROPERTY);
        objectProperty.set(PropType.key, key);
        objectProperty.set(PropType.expr, expr);
        return objectProperty;
    }

    private AST parseObjectInitializer() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseObjectInitializer");
        LinkedList<AST> properties = new LinkedList<>();
        eatPunctuator("{");
        while (!isPunctuator("}")) {
            AST prop = parseObjectProperty();
            properties.addLast(prop);
            if (!isPunctuator("}")) {
                eatPunctuator(",");
            }
        }
        eatPunctuator("}");
        AST objectExpression = new AST(ASTType.OBJECT_EXPRESSION);
        objectExpression.set(PropType.properties, properties);
        return objectExpression;
    }

    private AST parseArrayInitializer() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseArrayInitializer");
        LinkedList<AST> elements = new LinkedList<>();
        eatPunctuator("[");
        while (!isPunctuator("]")) {
            if (isPunctuator(",")) {
                next();
                elements.addLast(null);
            } else {
                elements.addLast(parseAssignmentExpression());
                if (!isPunctuator("]"))
                    eatPunctuator(",");
            }
        }
        eatPunctuator("]");
        AST arrayExpression = new AST(ASTType.ARRAY_EXPRESSION);
        arrayExpression.set(PropType.elements, elements);
        return arrayExpression;
    }

    private AST parseGroupExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseGroupExpression");
        eatPunctuator("(");
        AST expression = parseExpression();
        eatPunctuator(")");
        return expression;
    }

    private AST parsePrimaryExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parsePrimaryExpression");
        if (isPunctuator("("))
            return parseGroupExpression();
        if (isPunctuator("["))
            return parseArrayInitializer();
        if (isPunctuator("{"))
            return parseObjectInitializer();

        Token peek = peek();
        if (peek.type == TokenType.IDENTIFIER) {
            AST identifier = new AST(ASTType.IDENTIFIER);
            identifier.set(PropType.id, peek.value);
            identifier.set(PropType.valueType, "L");
            next();
            return identifier;
        } else if (peek.type == TokenType.STRING_LITERAL ||
                peek.type == TokenType.NUMERIC_LITERAL ||
                peek.type == TokenType.BOOLEAN ||
                peek.type == TokenType.NULL) {
            AST literal = new AST(ASTType.LITERAL);
            literal.set(PropType.literal, peek);
            literal.set(PropType.valueType, "R");
            next();
            return literal;
        } else
            throw new UnexpectedTokenException(peek.value);
    }

    private AST parseNewExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseNewExpression");
        eatKeyword("new");
        AST constructor = parseLValueExpression();
        AST arguments = isPunctuator("(") ? parseArguments() : new AST(ASTType.EMPTY_STATEMENT);
        AST newExpression = new AST(ASTType.NEW_EXPRESSION);
        newExpression.set(PropType.constructor, constructor);
        newExpression.set(PropType.arguments, arguments);
        newExpression.set(PropType.valueType, "R");
        return newExpression;
    }

    private AST parseLValueExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseLValueExpression");
        AST expression;
        if (isKeyword("new"))
            expression = parseNewExpression();
        else
            expression = parsePrimaryExpression();

        while (true) {
            if (isPunctuator(".")) {
                AST staticMemberExpression = new AST(ASTType.STATIC_MEMBER_EXPRESSION);
                AST property = parseNonComputedMember();
                staticMemberExpression.set(PropType.object, expression);
                staticMemberExpression.set(PropType.property, property);
                staticMemberExpression.set(PropType.valueType, "L");
                expression = staticMemberExpression;
            } else if (isPunctuator("[")) {
                AST computedMemberExpression = new AST(ASTType.COMPUTED_MEMBER_EXPRESSION);
                AST property = parseComputedMember();
                computedMemberExpression.set(PropType.object, expression);
                computedMemberExpression.set(PropType.property, property);
                computedMemberExpression.set(PropType.valueType, "L");
                expression = computedMemberExpression;
            } else
                break;
        }
        return expression;
    }

    private AST parseLValueExpressionAllowCall() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseLValueExpressionAllowCall");
        AST expression;
        if (isKeyword("new"))
            expression = parseNewExpression();
        else
            expression = parsePrimaryExpression();

        while (true) {
            if (isPunctuator(".")) {
                AST staticMemberExpression = new AST(ASTType.STATIC_MEMBER_EXPRESSION);
                AST property = parseNonComputedMember();
                staticMemberExpression.set(PropType.object, expression);
                staticMemberExpression.set(PropType.property, property);
                staticMemberExpression.set(PropType.valueType, "L");
                expression = staticMemberExpression;
            } else if (isPunctuator("(")) {
                AST callExpression = new AST(ASTType.CALL_EXPRESSION);
                AST arguments = parseArguments();
                callExpression.set(PropType.function, expression);
                callExpression.set(PropType.arguments, arguments);
                callExpression.set(PropType.valueType, "R");
                expression = callExpression;
            } else if (isPunctuator("[")) {
                AST computedMemberExpression = new AST(ASTType.COMPUTED_MEMBER_EXPRESSION);
                AST property = parseComputedMember();
                computedMemberExpression.set(PropType.object, expression);
                computedMemberExpression.set(PropType.property, property);
                computedMemberExpression.set(PropType.valueType, "L");
                expression = computedMemberExpression;
            } else
                break;
        }
        return expression;
    }

    private AST parsePostfixExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parsePostfixExpression");
        AST lValueExpressionAllowCall = parseLValueExpressionAllowCall();
        if (!eof() && (isPunctuator("++") || isPunctuator("--"))) {
            if (!lValueExpressionAllowCall.get(PropType.valueType).equals("L"))
                throw new UnexpectedTokenException("++ and -- must be applied on L-values");
            AST postfixExpression = new AST(ASTType.POSTFIX_EXPRESSION);
            Token op = next();
            postfixExpression.set(PropType.subExpression, lValueExpressionAllowCall);
            postfixExpression.set(PropType.op, op);
            postfixExpression.set(PropType.valueType, "R");
            return postfixExpression;
        } else
            return lValueExpressionAllowCall;
    }

    private AST parseUnaryExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseUnaryExpression");
        if (isPunctuator("++") || isPunctuator("--")) {
            AST unaryExpression = new AST(ASTType.UNARY_EXPRESSION);
            unaryExpression.set(PropType.op, next());
            AST subExpression = parseUnaryExpression();
            if (!subExpression.get(PropType.valueType).equals("L"))
                throw new UnexpectedTokenException("++ and -- must be applied on L-value.");
            unaryExpression.set(PropType.subExpression, subExpression);
            unaryExpression.set(PropType.valueType, "R");
            return unaryExpression;
        } else if (isPunctuator("+") || isPunctuator("-") ||
                isPunctuator("~") || isPunctuator("!") ||
                isKeyword("delete") || isKeyword("void") || isKeyword("typeof")) {
            AST unaryExpression = new AST(ASTType.UNARY_EXPRESSION);
            unaryExpression.set(PropType.op, next());
            unaryExpression.set(PropType.subExpression, parseUnaryExpression());
            unaryExpression.set(PropType.valueType, "R");
            return unaryExpression;
        } else
            return parsePostfixExpression();
    }

    private boolean isMultiplicative() throws EarlyEOFException {
        Token peek = peek();
        if (peek.type != TokenType.PUNCTUATOR)
            return false;
        String op = "'" + peek.value + "'";
        return "'*'/'%'".indexOf(op) >= 0;
    }

    private AST parseMultiplicativeExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseMultiplicativeExpression");
        AST unaryExpression = parseUnaryExpression();
        if (!eof() && isMultiplicative()) {
            AST multiplicativeExpression = new AST(ASTType.MULTIPLICATIVE_EXPRESSION);
            LinkedList<Token> ops = new LinkedList<>();
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(unaryExpression);
            do {
                ops.add(next());
                subExpressions.add(parseUnaryExpression());
            } while (!eof() && isMultiplicative());
            multiplicativeExpression.set(PropType.ops, ops);
            multiplicativeExpression.set(PropType.subExpressions, subExpressions);
            multiplicativeExpression.set(PropType.valueType, "R");
            return multiplicativeExpression;
        } else
            return unaryExpression;
    }

    private boolean isAdditive() throws EarlyEOFException {
        Token peek = peek();
        if (peek.type != TokenType.PUNCTUATOR)
            return false;
        String op = "'" + peek.value + "'";
        return "'+'-'".indexOf(op) >= 0;
    }

    private AST parseAdditiveExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseAdditiveExpression");
        AST multiplicativeExpression = parseMultiplicativeExpression();
        if (!eof() && isAdditive()) {
            AST additiveExpression = new AST(ASTType.ADDITIVE_EXPRESSION);
            LinkedList<Token> ops = new LinkedList<>();
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(multiplicativeExpression);
            do {
                ops.add(next());
                subExpressions.add(parseMultiplicativeExpression());
            } while (!eof() && isAdditive());
            additiveExpression.set(PropType.ops, ops);
            additiveExpression.set(PropType.subExpressions, subExpressions);
            additiveExpression.set(PropType.valueType, "R");
            return additiveExpression;
        } else
            return multiplicativeExpression;
    }

    private boolean isShift() throws EarlyEOFException {
        Token peek = peek();
        if (peek.type != TokenType.PUNCTUATOR)
            return false;
        String op = "'" + peek.value + "'";
        return "'<<'>>'>>>'".indexOf(op) >= 0;
    }

    private AST parseShiftExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseShiftExpression");
        AST additiveExpression = parseAdditiveExpression();
        if (!eof() && isShift()) {
            AST shiftExpression = new AST(ASTType.SHIFT_EXPRESSION);
            LinkedList<Token> ops = new LinkedList<>();
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(additiveExpression);
            do {
                ops.add(next());
                subExpressions.add(parseAdditiveExpression());
            } while (!eof() && isShift());
            shiftExpression.set(PropType.ops, ops);
            shiftExpression.set(PropType.subExpressions, subExpressions);
            shiftExpression.set(PropType.valueType, "R");
            return shiftExpression;
        } else
            return additiveExpression;
    }

    private AST parseInExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseInExpression");
        AST shiftExpression = parseShiftExpression();
        if (!eof() && isKeyword("in")) {
            AST inExpression = new AST(ASTType.IN_EXPRESSION);
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(shiftExpression);
            do {
                next();
                subExpressions.add(parseShiftExpression());
            } while (!eof() && isKeyword("in"));
            inExpression.set(PropType.subExpressions, subExpressions);
            inExpression.set(PropType.valueType, "R");
            return inExpression;
        } else
            return shiftExpression;
    }

    private boolean isRelational() throws EarlyEOFException {
        Token peek = peek();
        if (peek.type != TokenType.PUNCTUATOR)
            return false;
        String op = "'" + peek.value + "'";
        return "'<'>'<='>='".indexOf(op) >= 0;
    }

    private AST parseRelationalExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseRelationalExpression");
        AST inExpression = parseInExpression();
        if (!eof() && (isRelational() || isKeyword("instanceof"))) {
            AST relationalExpression = new AST(ASTType.RELATIONAL_EXPRESSION);
            LinkedList<Token> ops = new LinkedList<>();
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(inExpression);
            do {
                ops.add(next());
                subExpressions.add(parseInExpression());
            } while (!eof() && (isRelational() || isKeyword("instanceof")));
            relationalExpression.set(PropType.ops, ops);
            relationalExpression.set(PropType.subExpressions, subExpressions);
            relationalExpression.set(PropType.valueType, "R");
            return relationalExpression;
        } else
            return inExpression;
    }

    private boolean isEquality() throws EarlyEOFException {
        Token peek = peek();
        if (peek.type != TokenType.PUNCTUATOR)
            return false;
        String op = "'" + peek.value + "'";
        return "'=='!='==='!=='".indexOf(op) >= 0;
    }

    private AST parseEqualityExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseEqualityExpression");
        AST relationalExpression = parseRelationalExpression();
        if (!eof() && isEquality()) {
            AST equalityExpression = new AST(ASTType.EQUALITY_EXPRESSION);
            LinkedList<Token> ops = new LinkedList<>();
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(relationalExpression);
            do {
                ops.add(next());
                subExpressions.add(parseRelationalExpression());
            } while (!eof() && isEquality());
            equalityExpression.set(PropType.ops, ops);
            equalityExpression.set(PropType.subExpressions, subExpressions);
            equalityExpression.set(PropType.valueType, "R");
            return equalityExpression;
        } else
            return relationalExpression;
    }

    private AST parseAndExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseAndExpression");
        AST equalityExpression = parseEqualityExpression();
        if (!eof() && isPunctuator("&")) {
            AST andExpression = new AST(ASTType.AND_EXPRESSION);
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(equalityExpression);
            do {
                next();
                subExpressions.add(parseEqualityExpression());
            } while (!eof() && isPunctuator("&"));
            andExpression.set(PropType.subExpressions, subExpressions);
            andExpression.set(PropType.valueType, "R");
            return andExpression;
        } else
            return equalityExpression;
    }

    private AST parseExclusiveOrExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseExclusiveOrExpression");
        AST andExpression = parseAndExpression();
        if (!eof() && isPunctuator("^")) {
            AST exclusiveOrExpression = new AST(ASTType.EXCLUSIVE_OR_EXPRESSION);
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(andExpression);
            do {
                next();
                subExpressions.add(parseAndExpression());
            } while (!eof() && isPunctuator("^"));
            exclusiveOrExpression.set(PropType.subExpressions, subExpressions);
            exclusiveOrExpression.set(PropType.valueType, "R");
            return exclusiveOrExpression;
        } else
            return andExpression;
    }

    private AST parseInclusiveOrExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseInclusiveOrExpression");
        AST exclusiveOrExpression = parseExclusiveOrExpression();
        if (!eof() && isPunctuator("|")) {
            AST inclusiveOrExpression = new AST(ASTType.INCLUSIVE_OR_EXPRESSION);
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(exclusiveOrExpression);
            do {
                next();
                subExpressions.add(parseExclusiveOrExpression());
            } while (!eof() && isPunctuator("|"));
            inclusiveOrExpression.set(PropType.subExpressions, subExpressions);
            inclusiveOrExpression.set(PropType.valueType, "R");
            return inclusiveOrExpression;
        } else
            return exclusiveOrExpression;
    }

    private AST parseLogicalAndExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseLogicalAndExpression");
        AST inclusiveOrExpression = parseInclusiveOrExpression();
        if (!eof() && isPunctuator("&&")) {
            AST logicalAndExpression = new AST(ASTType.LOGICAL_AND_EXPRESSION);
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(inclusiveOrExpression);
            do {
                next();
                subExpressions.add(parseInclusiveOrExpression());
            } while (!eof() && isPunctuator("&&"));
            logicalAndExpression.set(PropType.subExpressions, subExpressions);
            logicalAndExpression.set(PropType.valueType, "R");
            return logicalAndExpression;
        } else
            return inclusiveOrExpression;
    }

    private AST parseLogicalOrExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseLogicalOrExpression");
        AST logicalAndExpression = parseLogicalAndExpression();
        if (!eof() && isPunctuator("||")) {
            AST logicalOrExpression = new AST(ASTType.LOGICAL_OR_EXPRESSION);
            LinkedList<AST> subExpressions = new LinkedList<>();
            subExpressions.add(logicalAndExpression);
            do {
                next();
                subExpressions.add(parseLogicalAndExpression());
            } while (!eof() && isPunctuator("||"));
            logicalOrExpression.set(PropType.subExpressions, subExpressions);
            logicalOrExpression.set(PropType.valueType, "R");
            return logicalOrExpression;
        } else
            return logicalAndExpression;
    }

    private AST parseConditionalExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseConditionalExpression");
        AST binaryExpression = parseLogicalOrExpression();
        if (!eof() && isPunctuator("?")) {
            AST conditionalExpression = new AST(ASTType.CONDITIONAL_EXPRESSION);
            next();
            AST onTrue = parseAssignmentExpression();
            eatPunctuator(":");
            AST onFalse = parseAssignmentExpression();
            conditionalExpression.set(PropType.condition, binaryExpression);
            conditionalExpression.set(PropType.onTrue, onTrue);
            conditionalExpression.set(PropType.onFalse, onFalse);
            conditionalExpression.set(PropType.valueType, "R");
            return conditionalExpression;
        } else
            return binaryExpression;
    }

    private boolean isAssignment() throws EarlyEOFException {
        Token peek = peek();
        if (peek.type != TokenType.PUNCTUATOR)
            return false;
        String op = "'" + peek.value + "'";
        return "'='+='-='*='/='%='<<='>>='>>>='&='|='^='".indexOf(op) >= 0;
    }

    private AST parseAssignmentExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseAssignmentExpression");
        AST conditionalExpression = parseConditionalExpression();
        if (isAssignment()) {
            if (!conditionalExpression.get(PropType.valueType).equals("L"))
                throw new UnexpectedTokenException("Assignment target must be an LVALUE");
            Token op = next();
            AST right = parseAssignmentExpression();
            AST assignmentExpression = new AST(ASTType.ASSIGNMENT_EXPRESSION);
            assignmentExpression.set(PropType.left, conditionalExpression);
            assignmentExpression.set(PropType.op, op);
            assignmentExpression.set(PropType.right, right);
            assignmentExpression.set(PropType.valueType, op.value.equals("=") ? "L" : "R");
            return assignmentExpression;
        } else
            return conditionalExpression;
    }

    private AST parseExpression() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseExpression");
        AST assignmentExpression = parseAssignmentExpression();
        if (!isPunctuator(","))
            return assignmentExpression;
        AST sequenceExpression = new AST(ASTType.SEQUENCE_EXPRESSION);
        LinkedList<AST> expressions = new LinkedList<>();
        expressions.add(assignmentExpression);
        while (!eof() && isPunctuator(",")) {
            eatPunctuator(",");
            expressions.addLast(parseAssignmentExpression());
        }
        sequenceExpression.set(PropType.expressions, expressions);
        return sequenceExpression;
    }

    private AST parseExpressionStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseExpressionStatement");
        AST expression = parseExpression();
        eatPunctuator(";");
        return expression;
    }

    private AST parseFunctionDeclaration() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseFunctionDeclaration");
        eatKeyword("function");
        String id = eatIdentifier();
        LinkedList<String> params = parseParams();
        boolean oldInFunctionBody = inFunctionBody;
        inFunctionBody = true;
        AST functionBody = parseBlock();
        inFunctionBody = oldInFunctionBody;
        if (functionBody == null) return null;

        AST functionDeclaration = new AST(ASTType.FUNCTION_DECLARATION);
        functionDeclaration.set(PropType.id, id);
        functionDeclaration.set(PropType.params, params);
        functionDeclaration.set(PropType.body, functionBody);
        return functionDeclaration;
    }

    private AST parseBreakStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseBreakStatement");
        eatKeyword("break");
        eatPunctuator(";");
        if (!inIteration)
            throw new UnexpectedTokenException("break not in iteration.");
        return new AST(ASTType.BREAK_STATEMENT);
    }

    private AST parseContinueStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseContinueStatement");
        eatKeyword("continue");
        eatPunctuator(";");
        if (!inIteration)
            throw new UnexpectedTokenException("continue not in iteration.");
        return new AST(ASTType.CONTINUE_STATEMENT);
    }

    private AST parseDoWhileStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseDoWhileStatement");
        eatKeyword("do");
        boolean oldInIteration = inIteration;
        inIteration = true;
        AST body = parseStatement();
        inIteration = oldInIteration;
        eatKeyword("while");
        eatPunctuator("(");
        AST test = parseExpression();
        eatPunctuator(")");
        if (isPunctuator(";"))
            next();
        AST doWhileStatement = new AST(ASTType.DO_WHILE_STATEMENT);
        doWhileStatement.set(PropType.body, body);
        doWhileStatement.set(PropType.test, test);
        return doWhileStatement;
    }

    private AST parseVariableDeclaration(boolean inFor) throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseVariableDeclaration");
        AST variableDeclaration = new AST(ASTType.VARIABLE_DECLARATION);
        variableDeclaration.set(PropType.varName, eatIdentifier());
        if (isPunctuator("=")) {
            next();
            variableDeclaration.set(PropType.initialization, parseAssignmentExpression());
        }
        return variableDeclaration;
    }

    private LinkedList<AST> parseVariableDeclarationList(boolean inFor) throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseVariableDeclarationList");
        LinkedList<AST> variableDeclarations = new LinkedList<>();
        variableDeclarations.addLast(parseVariableDeclaration(inFor));
        while (isPunctuator(",")) {
            next();
            variableDeclarations.addLast(parseVariableDeclaration(inFor));
        }
        return variableDeclarations;
    }

    private AST parseForStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseForStatement");

        AST forStatement = new AST(ASTType.FOR_STATEMENT);

        eatKeyword("for");
        eatPunctuator("(");
        if (isPunctuator(";"))
            next();
        else {
            if (isKeyword("var")) {
                next();
                String varName = eatIdentifier();
                if (isKeyword("in")) {
                    next();
                    AST iteratorDeclaration = new AST(ASTType.ITERATOR_DECLARATION);
                    iteratorDeclaration.set(PropType.varName, varName);
                    iteratorDeclaration.set(PropType.iterable, parseExpression());
                    forStatement.set(PropType.iterator, iteratorDeclaration);
                } else {
                    index--;    // Back to identifier
                    forStatement.set(PropType.init, parseVariableDeclarationList(true));
                    eatPunctuator(";");
                }
            } else {
                forStatement.set(PropType.init, parseExpression());
                eatPunctuator(";");
            }
        }

        if (forStatement.get(PropType.iterator) == null) {
            if (!isPunctuator(";"))
                forStatement.set(PropType.test, parseExpression());
            eatPunctuator(";");
            if (!isPunctuator(")"))
                forStatement.set(PropType.update, parseExpression());
        }
        eatPunctuator(")");

        boolean oldInIteration = inIteration;
        inIteration = true;
        forStatement.set(PropType.body, parseStatement());
        inIteration = oldInIteration;

        return forStatement;
    }

    private AST parseIfStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseIfStatement");
        eatKeyword("if");
        eatPunctuator("(");
        AST ifStatement = new AST(ASTType.IF_STATEMENT);
        ifStatement.set(PropType.test, parseExpression());
        eatPunctuator(")");
        ifStatement.set(PropType.onTrue, parseStatement());
        if (isKeyword("else")) {
            next();
            ifStatement.set(PropType.onFalse, parseStatement());
        }
        return ifStatement;
    }

    private AST parseReturnStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseReturnStatement");
        eatKeyword("return");
        if (!inFunctionBody)
            throw new UnexpectedTokenException("return can only appear in function body.");
        AST returnStatement = new AST(ASTType.RETURN_STATEMENT);
        if (!isPunctuator(";"))
            returnStatement.set(PropType.argument, parseExpression());
        eatPunctuator(";");
        return returnStatement;
    }

    private AST parseVarStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseVarStatement");
        eatKeyword("var");
        LinkedList<AST> declarations = parseVariableDeclarationList(false);
        eatPunctuator(";");
        AST varStatement = new AST(ASTType.VAR_STATEMENT);
        varStatement.set(PropType.declarations, declarations);
        return varStatement;
    }

    private AST parseWhileStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseWhileStatement");
        eatKeyword("while");
        eatPunctuator("(");
        AST whileStatement = new AST(ASTType.WHILE_STATEMENT);
        whileStatement.set(PropType.test, parseExpression());
        eatPunctuator(")");
        boolean oldInIteration = inIteration;
        inIteration = true;
        whileStatement.set(PropType.body, parseStatement());
        inIteration = oldInIteration;
        return whileStatement;
    }

    private AST parseStatement() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseStatement");
        Token peek = peek();
        if (peek.type == TokenType.PUNCTUATOR && peek.value.equals("{"))
            return parseBlock();
        if (peek.type == TokenType.PUNCTUATOR) {
            if (peek.value.equals(";"))
                return parseEmptyStatement();
            else if(peek.value.equals("("))
                return parseExpressionStatement();
        } else if (peek.type == TokenType.KEYWORD) {
            if (peek.value.equals("break"))
                return parseBreakStatement();
            else if (peek.value.equals("continue"))
                return parseContinueStatement();
            else if (peek.value.equals("do"))
                return parseDoWhileStatement();
            else if (peek.value.equals("for"))
                return parseForStatement();
            else if (peek.value.equals("if"))
                return parseIfStatement();
            else if (peek.value.equals("return"))
                return parseReturnStatement();
            else if (peek.value.equals("var"))
                return parseVarStatement();
            else if (peek.value.equals("while"))
                return parseWhileStatement();
        }

        return parseExpressionStatement();
    }

    private AST parseStatementListItem() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseStatementListItem");
        Token peek = peek();
        if (peek.type == TokenType.KEYWORD && peek.value.equals("function"))
            return parseFunctionDeclaration();
        else
            return parseStatement();
    }

    private AST parseScriptBody() throws EarlyEOFException, UnexpectedTokenException {
        //System.out.println("parseScriptBody");
        AST body = new AST(ASTType.SCRIPT_BODY);
        LinkedList<AST> statements = new LinkedList<>();
        while (!eof()) {
            AST statement = parseStatementListItem();
            if (statement == null)
                break;
            statements.add(statement);

        }
        body.set(PropType.statements, statements);
        return body;
    }

    public Parser() {}

    public AST parse(ArrayList<Token> tks) throws EarlyEOFException, UnexpectedTokenException {
        tokens = tks;
        index = 0;

        return parseScriptBody();
    }
}


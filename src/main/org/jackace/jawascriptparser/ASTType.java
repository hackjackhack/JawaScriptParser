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

/**
 * Created by Chi-Wei(Jack) Wang on 2015/12/6.
 */
public enum ASTType {
    SCRIPT_BODY,
    FUNCTION_DECLARATION,
    BLOCK_STATEMENT,
    EMPTY_STATEMENT,
    SEQUENCE_EXPRESSION,

    ASSIGNMENT_EXPRESSION,
    CONDITIONAL_EXPRESSION,
    LOGICAL_OR_EXPRESSION,
    LOGICAL_AND_EXPRESSION,
    INCLUSIVE_OR_EXPRESSION,

    EXCLUSIVE_OR_EXPRESSION,
    AND_EXPRESSION,
    EQUALITY_EXPRESSION,
    RELATIONAL_EXPRESSION,
    IN_EXPRESSION,

    SHIFT_EXPRESSION,
    ADDITIVE_EXPRESSION,
    MULTIPLICATIVE_EXPRESSION,
    UNARY_EXPRESSION,
    POSTFIX_EXPRESSION,

    STATIC_MEMBER_EXPRESSION,
    CALL_EXPRESSION,
    COMPUTED_MEMBER_EXPRESSION,
    NEW_EXPRESSION,
    IDENTIFIER,

    LITERAL,
    ARGUMENTS,
    ARRAY_EXPRESSION,
    OBJECT_EXPRESSION,
    BREAK_STATEMENT,

    CONTINUE_STATEMENT,
    DO_WHILE_STATEMENT,
    ITERATOR_DECLARATION,
    FOR_STATEMENT,
    VARIABLE_DECLARATION,

    IF_STATEMENT,
    RETURN_STATEMENT,
    VAR_STATEMENT,
    WHILE_STATEMENT,
    OBJECT_PROPERTY,
}



package gio.blue.greenery.gdscript.syntax.parsers

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate

class FunctionSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    /**
     * Function type hint
     *
     * (here! rarrow) (type: identifier)
     */
    private fun parseFunctionTypeHint(): Boolean {
        assertType(TokenLibrary.RARROW)
        val marker = mark()
        next()

        // Expect identifier
        if (tokenType != TokenLibrary.IDENTIFIER) {
            marker.error(
                message("SYNTAX.generic.expected.0.got.1", TokenLibrary.IDENTIFIER.toString(), tokenType.toString())
            )
            return false
        }

        next()
        marker.done(SyntaxLibrary.FUNCTION_DECL_TYPE_HINT)
        return true
    }

    /**
     * Function declaration
     *
     * (here! func keyword) (name: identifier) (parameters: parameter list) [type hint] (colon) (block)
     */
    fun parse(): Boolean {
        assertType(TokenLibrary.FUNC_KEYWORD)
        val marker = mark()
        next()

        // Expect identifier
        if (tokenType != TokenLibrary.IDENTIFIER) {
            marker.error(
                message(
                    "SYNTAX.generic.expected.0.got.1", TokenLibrary.IDENTIFIER.toString(), tokenType.toString()
                )
            )
            return false
        }
        next()

        // Expect a left par to start the parameter list
        if (tokenType != TokenLibrary.LPAR) {
            marker.error(
                message(
                    "SYNTAX.func-decl.expected.parameter-list.got.0", tokenType.toString()
                )
            )
            return false
        }

        // Read the parameter list
        if (!parseParameterListInParentheses()) {
            marker.drop()
            return false
        }

        // Check for a type hint
        if (tokenType == TokenLibrary.RARROW && !parseFunctionTypeHint()) {
            marker.drop()
            return false
        }

        // Expect colon
        if (tokenType != TokenLibrary.COLON) {
            marker.error(
                message(
                    "SYNTAX.generic.expected.0.got.1", TokenLibrary.COLON.toString(), tokenType.toString()
                )
            )
            return false
        }
        next()

        // Parse block
        if (!context.blocks.parse()) {
            marker.error(
                message(
                    "SYNTAX.statement.expected.block"
                )
            )
            return false
        }

        marker.done(SyntaxLibrary.FUNCTION_DECL_STATEMENT)
        return true
    }

    //region Parameters

    /**
     * Parameter type hint
     *
     * (here! colon) (type name: identifier)
     */
    fun parseParameterTypeHint(): Boolean {
        assertType(TokenLibrary.COLON)
        val marker = mark()
        next()

        if (tokenType != TokenLibrary.IDENTIFIER) {
            marker.error(
                message("SYNTAX.generic.expected.0.got.1", TokenLibrary.IDENTIFIER.toString(), tokenType.toString())
            )
            return false
        }

        next()
        marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_TYPE_HINT)
        return true
    }

    /**
     * Parameter default value expression
     *
     * (here! eq) (value: expression)
     */
    fun parseParameterDefaultValueExpression(): Boolean {
        assertType(TokenLibrary.EQ)
        val marker = mark()
        next()

        val expressionStartTokenType = tokenType
        if (!context.expressions.parse()) {
            marker.error(
                message("SYNTAX.generic.expected.expression.got.0", expressionStartTokenType.toString())
            )
            return false
        }

        marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_DEFAULT_ASSIGNMENT)
        return true
    }

    /**
     * Single parameter
     *
     * (here! identifier) [type hint] [default value expression]
     */
    fun parseParameter(): Boolean {
        assertType(TokenLibrary.IDENTIFIER)

        val marker = mark()
        next()

        if (tokenType == TokenLibrary.COLON) {
            parseParameterTypeHint()
        }

        if (tokenType == TokenLibrary.EQ) {
            if (!parseParameterDefaultValueExpression()) {
                // Failed to parse the default value, skip past whatever it found
                next()
            }
        }

        marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER)
        return true
    }

    /**
     * Parameter list (in parentheses)
     *
     * (here! lpar) [(parameter)(comma...)] (rpar)
     */
    fun parseParameterListInParentheses(): Boolean {
        assertType(TokenLibrary.LPAR)
        val marker = mark()
        next()

        while (tokenType != TokenLibrary.RPAR) {
            val foundType = tokenType // Save the token type first

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // Try to parse a parameter
            if (tokenType != TokenLibrary.IDENTIFIER || !parseParameter()) {
                marker.error(
                    message(
                        "SYNTAX.generic.expected.parameter.got.0", foundType.toString()
                    )
                )
                return false
            }

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // We just read a pair, expect a comma or right brace
            if (tokenType == TokenLibrary.COMMA) {
                next()
                continue
            }

            if (tokenType == TokenLibrary.RPAR) {
                next()
                break
            }

            marker.error(
                message(
                    "SYNTAX.parameter-list.expected.end.or.continuation.got.0", foundType.toString()
                )
            )
            return false
        }

        marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_LIST)
        return true
    }

    //endregion
}
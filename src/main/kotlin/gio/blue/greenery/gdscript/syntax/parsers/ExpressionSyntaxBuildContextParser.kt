package gio.blue.greenery.gdscript.syntax.parsers

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate

class ExpressionSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context,
        builder
    ) {

    fun parse(): Boolean {
        val t0 = tokenType ?: return false
        when (t0) {
            TokenLibrary.FLOAT_LITERAL -> {
                markSingleHere(SyntaxLibrary.FLOAT_LITERAL)
                return true
            }

            TokenLibrary.INTEGER_LITERAL -> {
                markSingleHere(SyntaxLibrary.INTEGER_LITERAL)
                return true
            }

            TokenLibrary.HEX_LITERAL -> {
                markSingleHere(SyntaxLibrary.HEX_LITERAL)
                return true
            }

            TokenLibrary.BINARY_LITERAL -> {
                markSingleHere(SyntaxLibrary.BINARY_LITERAL)
                return true
            }

            TokenLibrary.TRUE_KEYWORD, TokenLibrary.FALSE_KEYWORD -> {
                markSingleHere(SyntaxLibrary.BOOLEAN_LITERAL)
                return true
            }

            TokenLibrary.LPAR -> {
                return parseExpressionInParentheses()
            }

            TokenLibrary.LBRACKET -> {
                parseListInBrackets()
                return true
            }

            TokenLibrary.LBRACE -> {
                return context.dictionaries.parse()
            }
        }

        return context.operations.parse()
    }

    /**
     * Expression in parentheses
     *
     * (here! lpar) (expression) (rpar)
     */
    fun parseExpressionInParentheses(): Boolean {
        assertType(TokenLibrary.LPAR)
        val marker = mark()
        next()

        // Parse the expression
        if (!parse()) {
            marker.drop()
            return false
        }

        // Expect right par
        if (tokenType != TokenLibrary.RPAR) {
            marker.error(
                message(
                    "SYNTAX.generic.expected.0.got.1", TokenLibrary.RPAR.toString(), tokenType.toString()
                )
            )
            return false
        }
        next()

        marker.drop()
        return true
    }

    fun parseListInBrackets() {
        TODO()
    }

    /**
     * Basic expression list (in parentheses)
     *
     * (here! lpar) [(expression)(...comma...)] (rpar)
     */
    fun parseBasicExpressionList(): Boolean {
        assertType(TokenLibrary.LPAR)
        val marker = mark()
        next()

        while (tokenType != TokenLibrary.RPAR) {
            val foundType = tokenType // Save the token type first

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // Try to parse an expression
            if (!parse()) {
                marker.error(
                    message(
                        "SYNTAX.generic.expected.expression.got.0", foundType.toString()
                    )
                )
                return false
            }

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // We just read an expression, expect a comma or right brace
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
                    "SYNTAX.expression-list.expected.end.or.continuation.got.0", foundType.toString()
                )
            )
            return false
        }

        marker.done(SyntaxLibrary.EXPRESSION_LIST)
        return true
    }
}


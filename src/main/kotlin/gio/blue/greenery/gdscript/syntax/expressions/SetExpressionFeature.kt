package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Basic group of expressions surrounded by parenthesis
 *
 * (here! lpar) [(expression)(...comma...)] (rpar)
 */
fun ExpressionSyntaxBuildContextParser.parseSetExpression(): Boolean {
    assertType(TokenLibrary.LPAR)
    val marker = mark()
    next()

    while (tokenType != TokenLibrary.RPAR) {
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        // Try to parse an expression
        want({ context.expressions.parse() }) {
            marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
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

        marker.error(message("SYNTAX.expr.set.expected.delimiter-or-end"))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_LIST)
    return true
}
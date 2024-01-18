package gio.blue.greenery.gdscript.syntax.expressions.arithmetic

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse an expression prefixed by a unary operator
 *
 * (here! unary operator) (expression)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseExpressionWithUnaryPrefix(): Boolean {
    assertSet(TokenLibrary.UNARY_OPERATORS)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.expr.op.unary.expected.expr"))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_PREFIX_UNARY)
    return true
}
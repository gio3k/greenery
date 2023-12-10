package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Unary start
 *
 * ... (here! unary operator) (expression)
 */
fun ExpressionSyntaxBuildContextParser.parseExpressionPrefixedByUnary(): Boolean {
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

/**
 * Negation start
 *
 * ... (here! negation operator) (expression)
 */
fun ExpressionSyntaxBuildContextParser.parseExpressionPrefixedByNegation(): Boolean {
    assertSet(TokenLibrary.NEGATION_OPERATORS)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.expr.op.negation.expected.expr"))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_PREFIX_NEGATION)
    return true
}
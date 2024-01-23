package gio.blue.greenery.gdscript.syntax.expressions.booleans

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a negated boolean expression starting from the current token
 *
 * (here! negation operator) (expression)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseExpressionWithBoolNegationPrefix(): Boolean {
    assertSet(TokenLibrary.BOOLEAN_NEGATION_OPERATORS)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.expr.op.bool-negation.expected.expr"))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_PREFIX_NEGATION)
    return true
}
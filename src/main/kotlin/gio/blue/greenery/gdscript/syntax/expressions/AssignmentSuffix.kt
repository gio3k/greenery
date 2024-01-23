package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Parse an assignment following up an expression
 *
 * (lhs: expression) (here! EQ) (rhs: expression)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the assignment operation was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseAssignmentAfterExpression(): Boolean {
    assertType(TokenLibrary.EQ)
    val marker = mark()
    next()

    // Read expression
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", tokenType.toString()))
        return false
    }

    next()
    marker.done(SyntaxLibrary.EXPRESSION_SUFFIX_ASSIGNMENT)
    return true
}
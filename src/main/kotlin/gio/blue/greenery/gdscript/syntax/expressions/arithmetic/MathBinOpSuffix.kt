package gio.blue.greenery.gdscript.syntax.expressions.arithmetic

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a 2-part math operation following up an expression
 *
 * (lhs: expression) (here! math operator) (rhs: expression)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the math operation was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseMathBinOpAfterExpression(): Boolean {
    val marker = mark()

    // Read operator
    if (!TokenLibrary.MATH_BINARY_OPERATORS.contains(tokenType)) {
        marker.drop()
        return false
    }

    // Move past the token
    next()

    // Read expression
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", tokenType.toString()))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_SUFFIX_MATH_BINOP)
    return true
}
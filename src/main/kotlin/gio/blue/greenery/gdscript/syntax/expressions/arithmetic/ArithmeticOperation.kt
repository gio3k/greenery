package gio.blue.greenery.gdscript.syntax.expressions.arithmetic

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse an arithmetic operation following up an expression
 *
 * (lhs: expression) (here! binary operator) (rhs: expression)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the arithmetic operation was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseArithmeticOperationAfterExpression(): Boolean {
    val operatorMarker = mark()

    // Read binary operator
    if (!TokenLibrary.ARITHMETIC_OPERATORS.contains(tokenType)) {
        operatorMarker.drop()
        return false
    }

    // Move past the token
    next()
    operatorMarker.done(SyntaxLibrary.EXPRESSION_MATH_OPERATION)

    // Read expression
    val rhsMarker = mark()
    want({ parse() }) {
        rhsMarker.error(message("SYNTAX.generic.expected.expr.got.0", tokenType.toString()))
        return false
    }

    rhsMarker.done(SyntaxLibrary.EXPRESSION_MATH_RHS)
    return true
}
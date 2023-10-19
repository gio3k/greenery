package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Expression with outer parentheses
 *
 * (here! lpar) (expression) (rpar)
 */
fun ExpressionSyntaxBuildContextParser.parseExpressionWithOuterParenthesesExpression(): Boolean {
    assertType(TokenLibrary.LPAR)
    val marker = mark()
    next()

    // Parse the expression
    want({ parse() }) {
        marker.drop()
        return false
    }

    // Expect the right parenthesis
    wantThenNext({ tokenType == TokenLibrary.RPAR }) {
        marker.error(message("SYNTAX.expr.outer-par.expected.end"))
        return false
    }

    marker.drop()
    return true
}
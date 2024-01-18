package gio.blue.greenery.gdscript.syntax.expressions.pars

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse an expression inside parenthesis
 *
 * (here! lpar) (expression) (rpar)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseEnclosedExpression(): Boolean {
    assertType(TokenLibrary.LPAR)
    val marker = mark()
    next()

    // Parse the expression
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        return false
    }

    // Make sure there's a corresponding end parenthesis
    wantThenNext({ tokenType == TokenLibrary.RPAR }) {
        marker.error(message("SYNTAX.expr.outer-par.expected.end.got.0", it.toString()))
        return false
    }

    // Drop the marker - the parsed expression should already be in the tree
    marker.drop()
    return true
}
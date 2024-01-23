package gio.blue.greenery.gdscript.syntax.expressions.logic

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse ternary if logic following up an expression
 *
 * (on_true: expression) (here! IF) (expr: expression) (ELSE) (on_false: expression)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the "if" expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseIfAfterExpression(): Boolean {
    assertType(TokenLibrary.IF_KEYWORD)
    val marker = mark()
    next()

    // Read condition expression
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", tokenType.toString()))
        return false
    }

    // Expect else
    wantThenNext({tokenType == TokenLibrary.ELSE_KEYWORD}) {
        marker.error(message("SYNTAX.expr.ternary.expected.else.got.0", tokenType.toString()))
        return false
    }

    // Read on false expression
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", tokenType.toString()))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_SUFFIX_TERNARY)
    return true
}
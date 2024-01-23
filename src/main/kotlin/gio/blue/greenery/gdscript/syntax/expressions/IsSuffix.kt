package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.identifiers.parseIdentifier

/**
 * Parse an *is* logic checker following up an expression
 *
 * (lhs: expression) (here! IS) (rhs: identifier)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the "is" expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseIsAfterExpression(): Boolean {
    assertType(TokenLibrary.IS_KEYWORD)
    val marker = mark()
    next()

    // Read identifier
    want({ parseIdentifier() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", tokenType.toString()))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_SUFFIX_BOOL_IS)
    return true
}
package gio.blue.greenery.gdscript.syntax.expressions.identifiers

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse an identifier expression starting from the current token
 *
 * (here! identifier) [function call]
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseIdentifier(): Boolean {
    assertType(TokenLibrary.IDENTIFIER)
    val marker = mark()
    next()

    // Check for function call
    if (tokenType == TokenLibrary.LPAR) {
        want({ parseIdentifierFunctionCall() }) {
            marker.drop()
            return false
        }
    }

    marker.done(SyntaxLibrary.IDENTIFIER)
    return true
}

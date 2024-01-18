package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Parse a single token expression starting at the current token
 *
 * (here! any)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the token was parsed
 */
fun ExpressionSyntaxBuildContextParser.parseSingleTokenExpression(): Boolean {
    val marker = mark()

    val elementType = when (tokenType) {
        TokenLibrary.FLOAT_LITERAL -> SyntaxLibrary.FLOAT_LITERAL

        TokenLibrary.INTEGER_LITERAL -> SyntaxLibrary.INTEGER_LITERAL

        TokenLibrary.HEX_LITERAL -> SyntaxLibrary.HEX_LITERAL

        TokenLibrary.BINARY_LITERAL -> SyntaxLibrary.BINARY_LITERAL

        TokenLibrary.TRUE_KEYWORD,
        TokenLibrary.FALSE_KEYWORD -> SyntaxLibrary.BOOLEAN_LITERAL

        else -> null
    }

    if (elementType == null) {
        marker.drop()
        return false
    } else {
        next()
        marker.done(elementType)
        return true
    }
}
package gio.blue.greenery.gdscript.syntax.expressions.strings

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a string literal at the current token
 *
 * (here! string marker)

 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseStringLiteral(): Boolean {
    assertSet(TokenLibrary.STRING_STARTERS)
    val marker = mark()

    // Check for prefix
    if (tokenType == TokenLibrary.RAW_STRING_PREFIX ||
        tokenType == TokenLibrary.NODE_PATH_STRING_PREFIX ||
        tokenType == TokenLibrary.STRING_NAME_STRING_PREFIX
    ) {
        // String is prefixed, skip the prefix!
        next()
    }

    // Check for string starter / marker
    want({ TokenLibrary.STRING_MARKERS.contains(tokenType) }) {
        marker.error(message("SYNTAX.expr.string.expected.string-marker.got.0", it.toString()))
        return false
    }

    // Save the type of string marker, then move on
    val stringType = tokenType
    next()

    // Proceed through string content until it's completed
    if (!proceedUntilStringEnds(stringType)) {
        marker.error(message("SYNTAX.expr.string.expected.string-token.got.0", tokenType.toString()))
        return false
    }

    marker.done(SyntaxLibrary.STRING_LITERAL)
    return true
}

private fun ExpressionSyntaxBuildContextParser.proceedUntilStringEnds(stringType: IElementType?): Boolean {
    // Iterate over the string tokens and content
    while (tokenType != null) {
        when (tokenType) {
            TokenLibrary.STRING_CONTENT,
            TokenLibrary.STRING_ESCAPE, TokenLibrary.STRING_ESCAPE_UNICODE_16, TokenLibrary.STRING_ESCAPE_UNICODE_32 -> {
                // String content, skip!
                next()
                continue
            }

            stringType -> {
                // String end marker, stop iterating
                next()
                break
            }

            else -> {
                // Unknown token, error!
                return false
            }
        }
    }

    return tokenType != null
}
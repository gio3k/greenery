package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.strings.parseStringLiteral

/**
 * Parse a node path expression starting from the current token
 *
 * (here! dollar)
 *
 * Keep in mind this isn't the same as a string-based node path - those start with $
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the node path was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseNodePath(): Boolean {
    assertType(TokenLibrary.DOLLAR)
    val marker = mark()
    next()

    while (tokenType != null) {
        if (tokenType == TokenLibrary.IDENTIFIER) {
            next()
        } else if (TokenLibrary.STRING_STARTERS.contains(tokenType)) {
            want({ parseStringLiteral() }) {
                marker.error("failed to parse string literal")
                return false
            }
        } else {
            marker.error("unknown token")
            return false
        }

        // Keep going if a slash (/) is found, stop if not
        if (tokenType == TokenLibrary.DIV) {
            next()
            continue
        } else {
            break
        }
    }

    marker.done(SyntaxLibrary.NODE_PATH)
    return true
}
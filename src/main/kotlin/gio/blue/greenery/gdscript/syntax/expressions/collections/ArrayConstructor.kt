package gio.blue.greenery.gdscript.syntax.expressions.collections

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse an array creator expression
 *
 * (here! LBRACKET) [(item)(...comma...)] (RBRACKET)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the array creator was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseArrayCtor(): Boolean {
    assertType(TokenLibrary.LBRACKET)
    val marker = mark()
    next()

    while (tokenType != TokenLibrary.RBRACKET) {
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        // Try to parse an expression
        want({ parse() }) {
            marker.drop()
            return false
        }

        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        // We just read a pair, expect a comma or right bracket
        if (tokenType == TokenLibrary.COMMA) {
            next()
            continue
        }

        if (tokenType != TokenLibrary.RBRACKET) {
            marker.error(message("SYNTAX.expr.set.expected.delimiter-or-end"))
            return false
        }
    }

    // We should be on the right bracket token now - skip it
    next()
    marker.done(SyntaxLibrary.ARRAY_CTOR)
    return true
}
package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Dictionary pair
 *
 * There are two types of dictionary pair syntax:
 * (here! key: expression) (colon) (value: expression)
 * Lua style: (here! key: identifier or string literal) (equals) (value: expression)
 *
 * Having mixed styles of pairs is not allowed.
 */
private fun ExpressionSyntaxBuildContextParser.parseDictionaryPair(): Boolean {
    val marker = mark()

    // Read key as expression
    // If the pair is Lua style, the expression *needs* to be a string literal or identifier
    // We'll deal with that later though - parse as a normal expression for now
    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.expr.dict.pair.expected.key"))
        return false
    }

    skip(TokenLibrary.LINE_BREAK)
    skip(TokenLibrary.INDENT)

    wantThenNext({ tokenType == TokenLibrary.COLON || tokenType == TokenLibrary.EQ }) {
        marker.error(message("SYNTAX.expr.dict.pair.expected.colon-or-eq"))
        return false
    }

    skip(TokenLibrary.LINE_BREAK)
    skip(TokenLibrary.INDENT)

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.expr.dict.pair.expected.value"))
        return false
    }

    marker.done(SyntaxLibrary.DICTIONARY_PAIR)
    return true
}

/**
 * Dictionary
 *
 * (here! lbrace) [(dictionary pair)(...comma...)] (rbrace)
 */
fun ExpressionSyntaxBuildContextParser.parseDictionaryExpression(): Boolean {
    assertType(TokenLibrary.LBRACE)
    val marker = mark()
    next()

    while (tokenType != TokenLibrary.RBRACE) {
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        // Try to parse a pair
        want({ parseDictionaryPair() }) {
            marker.drop()
            return false
        }

        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        // We just read a pair, expect a comma or right brace
        wantThenNext({ tokenType == TokenLibrary.COMMA || tokenType == TokenLibrary.RBRACE }) {
            marker.error(message("SYNTAX.expr.dict.expected.delimiter-or-end"))
            return false
        }
    }

    marker.done(SyntaxLibrary.DICTIONARY)
    return true
}
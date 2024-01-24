package gio.blue.greenery.gdscript.syntax.expressions.collections

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a dictionary pair item
 *
 * There are two types of dictionary pair syntax:
 * (here! key: expression) (colon) (value: expression)
 * Lua style: (here! key: identifier or string literal) (equals) (value: expression)
 *
 * Having mixed styles of pairs is not allowed.
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the dictionary pair was fully parsed
 */
internal fun ExpressionSyntaxBuildContextParser.parseDictionaryPair(): Boolean {
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


package gio.blue.greenery.gdscript.syntax.expressions.dictionaries

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a dictionary expression
 *
 * (here! lbrace) [(dictionary pair)(...comma...)] (rbrace)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the dictionary was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseDictionary(): Boolean {
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
package gio.blue.greenery.gdscript.syntax.expressions.collections

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a dictionary creator expression
 *
 * (here! lbrace) [(dictionary pair)(...comma...)] (rbrace)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the dictionary creator was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseDictionaryCtor(): Boolean {
    assertType(TokenLibrary.LBRACE)
    val marker = mark()
    next()

    if (tokenType == TokenLibrary.IDENTIFIER || TokenLibrary.STRING_STARTERS.contains(tokenType)) {
        
    }

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
        skip(TokenLibrary.DEDENT)

        // We just read a pair, expect a comma or right brace
        if (tokenType == TokenLibrary.COMMA) {
            next()
            continue
        }

        if (tokenType != TokenLibrary.RBRACE) {
            marker.error(message("SYNTAX.expr.dict.expected.delimiter-or-end"))
            return false
        }
    }

    // We should be on the right brace token now - skip it
    next()
    marker.done(SyntaxLibrary.DICTIONARY_CTOR)
    return true
}
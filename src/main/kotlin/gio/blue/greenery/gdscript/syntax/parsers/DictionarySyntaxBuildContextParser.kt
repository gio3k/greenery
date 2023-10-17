package gio.blue.greenery.gdscript.syntax.parsers

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate

class DictionarySyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context,
        builder
    ) {

    //region Dictionaries

    /**
     * Dictionary pair
     *
     * There are two types of dictionary pair syntax:
     * (here! key: expression) (colon) (value: expression)
     * Lua style: (here! key: identifier or string literal) (equals) (value: expression)
     *
     * Having mixed styles of pairs is not allowed.
     */
    fun parseDictionaryPair(): Boolean {
        val marker = mark()

        // Read key as expression
        // If the pair is Lua style, the expression *needs* to be a string literal or identifier
        // We'll deal with that later though - just parse as a normal expression for now
        if (!context.expressions.parse()) {
            marker.error(
                message("SYNTAX.dictionary.expected.pair.key.expression.got.0", tokenType.toString())
            )
            return false
        }

        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        if (tokenType != TokenLibrary.COLON && tokenType != TokenLibrary.EQ) {
            marker.error(
                message("SYNTAX.dictionary.expected.pair.separator.got.0", tokenType.toString())
            )
            return false
        }

        next()
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        if (!parse()) {
            marker.error(
                message("SYNTAX.dictionary.expected.pair.value.expression.got.0", tokenType.toString())
            )
            return false
        }

        marker.done(SyntaxLibrary.DICTIONARY_PAIR)
        return true
    }

    /**
     * Dictionary inside braces
     *
     * (here! lbrace) [(dictionary pair)(...comma...)] (rbrace)
     */
    fun parse(): Boolean {
        assertType(TokenLibrary.LBRACE)
        val marker = mark()
        next()

        while (tokenType != TokenLibrary.RBRACE) {
            val foundType = tokenType // Save the token type first

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // Try to parse a pair
            if (!parseDictionaryPair()) {
                marker.error(
                    message(
                        "SYNTAX.dictionary.expected.pair.got.0", foundType.toString()
                    )
                )
                return false
            }

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // We just read a pair, expect a comma or right brace
            if (tokenType != TokenLibrary.COMMA && tokenType != TokenLibrary.RBRACE) {
                marker.error(
                    message(
                        "SYNTAX.dictionary.expected.end.or.continuation.got.0", foundType.toString()
                    )
                )
                return false
            }
        }

        marker.done(SyntaxLibrary.DICTIONARY)
        return true
    }

    //endregion
}
package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary

class ExpressionSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context,
        builder
    ) {

    /**
     * Figure out what expression this token is and try to parse it
     */
    fun parse(): Boolean {
        val t0 = tokenType ?: return false
        when (t0) {
            TokenLibrary.FLOAT_LITERAL -> {
                markSingleHere(SyntaxLibrary.FLOAT_LITERAL_EXPRESSION)
                return true
            }

            TokenLibrary.INTEGER_LITERAL -> {
                markSingleHere(SyntaxLibrary.INTEGER_LITERAL_EXPRESSION)
                return true
            }

            TokenLibrary.HEX_LITERAL -> {
                markSingleHere(SyntaxLibrary.HEX_LITERAL_EXPRESSION)
                return true
            }

            TokenLibrary.BINARY_LITERAL -> {
                markSingleHere(SyntaxLibrary.BINARY_LITERAL_EXPRESSION)
                return true
            }

            TokenLibrary.TRUE_KEYWORD, TokenLibrary.FALSE_KEYWORD -> {
                markSingleHere(SyntaxLibrary.BOOLEAN_LITERAL_EXPRESSION)
                return true
            }

            TokenLibrary.LPAR -> {
                parseInParentheses()
                return true
            }

            TokenLibrary.LBRACKET -> {
                parseListInBrackets()
                return true
            }

            TokenLibrary.LBRACE -> {
                parseDictionaryInBraces()
                return true
            }
        }
        return false
    }

    fun parseInParentheses() {
        TODO()
    }

    fun parseListInBrackets() {
        TODO()
    }

    //region Dictionaries

    /**
     * Parse dictionary expression pair
     *
     * There are two types of dictionary pair syntax:
     * (key: expression) (colon) (value: expression)
     * Lua style: (key: identifier or string literal) (equals) (value: expression)
     *
     * Having mixed styles of pairs is not allowed.
     */
    fun parseDictionaryPair(): Boolean {
        val marker = mark()

        // Read key as expression
        // If the pair is Lua style, the expression *needs* to be a string literal or identifier
        // We'll deal with that later though - just parse as a normal expression for now
        if (!parse()) {
            marker.error(
                GDSyntaxBundle.message("SYNTAX.expected.dictionary.pair.key.expression.got.0", tokenType.toString())
            )
            return false
        }

        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        if (tokenType != TokenLibrary.COLON && tokenType != TokenLibrary.EQ) {
            marker.error(
                GDSyntaxBundle.message("SYNTAX.expected.dictionary.pair.separator.got.0", tokenType.toString())
            )
            return false
        }

        next()
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        if (!parse()) {
            marker.error(
                GDSyntaxBundle.message("SYNTAX.expected.dictionary.pair.value.expression.got.0", tokenType.toString())
            )
            return false
        }

        marker.done(SyntaxLibrary.DICTIONARY_PAIR_EXPRESSION)
        return true
    }

    fun parseDictionaryInBraces() {
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
                    GDSyntaxBundle.message(
                        "SYNTAX.expected.dictionary.pair.got.0", foundType.toString()
                    )
                )
                return
            }

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // We just read a pair, expect a comma or right brace
            if (tokenType != TokenLibrary.COMMA && tokenType != TokenLibrary.RBRACE) {
                marker.error(
                    GDSyntaxBundle.message(
                        "SYNTAX.expected.dictionary.end.or.continuation.got.0", foundType.toString()
                    )
                )
                return
            }
        }

        marker.done(SyntaxLibrary.DICTIONARY_EXPRESSION)
    }

    //endregion

    fun parseArgumentListInParentheses(): Boolean {

        if (!nextForExpectedElementAfterThis(TokenLibrary.IDENTIFIER)) {
            marker.drop()
            return false
        }
    }
}
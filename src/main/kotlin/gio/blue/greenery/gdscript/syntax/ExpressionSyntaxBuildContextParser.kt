package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary

class ExpressionSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context,
        builder
    ) {

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

        return context.opex.parse()
    }

    fun parseInParentheses() {
        TODO()
    }

    fun parseListInBrackets() {
        TODO()
    }

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
        if (!parse()) {
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

        marker.done(SyntaxLibrary.DICTIONARY_PAIR_EXPRESSION)
        return true
    }

    /**
     * Dictionary inside braces
     *
     * (here! lbrace) [(dictionary pair)(comma...)] (rbrace)
     */
    fun parseDictionaryInBraces(): Boolean {
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

        marker.done(SyntaxLibrary.DICTIONARY_EXPRESSION)
        return true
    }

    //endregion

    //region Arguments

    /**
     * Type hint
     *
     * (here! colon) (identifier)
     */
    fun parseArgumentTypeHint(): Boolean {
        assertType(TokenLibrary.COLON)
        val marker = mark()
        next()

        if (tokenType != TokenLibrary.IDENTIFIER) {
            marker.error(
                message("SYNTAX.argument.type-hint.expected.identifier.after.colon")
            )
            return false
        }

        next()
        marker.done(SyntaxLibrary.ARGUMENT_TYPE_HINT)
        return true
    }

    /**
     * Single argument
     *
     * (here! identifier) [(colon) (name: identifier)] [(equals) (default: expression)]
     */
    fun parseArgument(): Boolean {
        assertType(TokenLibrary.IDENTIFIER)
        val marker = mark()
        next()

        marker.drop()
        return true
    }

    /**
     * Argument list (in parentheses)
     *
     * (here! lpar) [(argument)(comma...)] (rpar)
     */
    fun parseArgumentListInParentheses(): Boolean {
        assertType(TokenLibrary.LPAR)
        val marker = mark()
        next()

        while (tokenType != TokenLibrary.RPAR) {
            val foundType = tokenType // Save the token type first

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // Try to parse a pair
            if (!parseArgument()) {
                marker.error(
                    message(
                        "SYNTAX.generic.expected.argument.got.0", foundType.toString()
                    )
                )
                return false
            }

            skip(TokenLibrary.LINE_BREAK)
            skip(TokenLibrary.INDENT)

            // We just read a pair, expect a comma or right brace
            if (tokenType != TokenLibrary.COMMA && tokenType != TokenLibrary.RPAR) {
                marker.error(
                    message(
                        "SYNTAX.argument-list.expected.end.or.continuation.got.0", foundType.toString()
                    )
                )
                return false
            }
        }

        marker.drop()
        return true
    }

    //endregion
}


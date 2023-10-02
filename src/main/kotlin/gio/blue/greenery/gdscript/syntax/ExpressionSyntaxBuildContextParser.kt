package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.elements.SyntaxLibrary
import gio.blue.greenery.gdscript.elements.TokenLibrary

class ExpressionSyntaxBuildContextParser(parser: SyntaxParser, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        parser,
        builder
    ) {

    /**
     * Parse an expression
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
                parseExpressionInParentheses()
                return true
            }

            TokenLibrary.LBRACKET -> {
                parseListExpressionInBrackets()
                return true
            }

            TokenLibrary.LBRACE -> {
                parseDictionaryExpressionInBraces()
                return true
            }
        }
        return false
    }

    fun parseExpressionInParentheses() {
        TODO()
    }

    fun parseListExpressionInBrackets() {
        TODO()
    }

    fun parseDictionaryExpressionInBraces() {
        TODO()
    }
}
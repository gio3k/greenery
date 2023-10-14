package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary

class OpExSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context,
        builder
    ) {

    fun parse(): Boolean {
        if (TokenLibrary.UNARY_OPERATORS.contains(tokenType)) return parseFromUnaryStart()


    }

    /**
     * Unary start
     *
     * ... (here! unary operator) (expression)
     */
    private fun parseFromUnaryStart(): Boolean {
        assertSet(TokenLibrary.UNARY_OPERATORS)

        val marker = mark()

        next()

        if (!context.expressions.parse()) {
            marker.error(message("SYNTAX.unary.invalid.target"))
            return false
        }

        marker.done(SyntaxLibrary.UNARY_PREFIX_EXPRESSION)
        return true
    }

    private fun parseFromNotStart(): Boolean {

    }
}
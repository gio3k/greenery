package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.PsiBuilder
import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.elements.SyntaxLibrary
import gio.blue.greenery.gdscript.elements.TokenLibrary

class ExpressionSyntaxBuildContextParser(parser: SyntaxParser, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        parser,
        builder
    ) {

    fun tryParsePrimary() {

    }


    fun tryParseStringLiteral(builder: PsiBuilder): Boolean {
        if (!TokenLibrary.STRING_ELEMENTS.contains(builder.tokenType))
            return false // Can't parse

        val marker = builder.mark()

        while (TokenLibrary.STRING_ELEMENTS.contains(builder.tokenType)) {
            builder.advanceLexer()
        }

        marker.done(SyntaxLibrary.STRING_LITERAL_EXPRESSION)
        return true
    }

    fun parseListLiteral(builder: PsiBuilder) {
        assert(builder.tokenType == TokenLibrary.LBRACKET)

        val marker = builder.mark()

        // Advance to the token after the [
        // If it's a ], just return an empty list
        if (builder.tokenType == TokenLibrary.RBRACKET) {
            builder.advanceLexer()
            marker.done(SyntaxLibrary.LIST_LITERAL_EXPRESSION)
            return
        }

        while (TokenLibrary.STRING_ELEMENTS.contains(builder.tokenType)) {
            builder.advanceLexer()
        }

        marker.done(SyntaxLibrary.STRING_LITERAL_EXPRESSION)
        return true
    }
}
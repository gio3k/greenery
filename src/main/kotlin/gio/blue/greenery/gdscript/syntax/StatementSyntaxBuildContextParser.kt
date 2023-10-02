package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.elements.TokenLibrary

class StatementSyntaxBuildContextParser(parser: SyntaxParser, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        parser,
        builder
    ) {

    /**
     * Parse a statement
     */
    fun parse() {
        println("parse: $tokenType")
        val tokenType0 = tokenType ?: return
        when (tokenType0) {
            TokenLibrary.EXTENDS_KEYWORD -> return parseExtendsStatement()
            TokenLibrary.CLASS_NAME_KEYWORD -> return parseClassNameStatement()
        }

        // Unknown token
        next()
        builder.error("Unknown statement start token $tokenType0")
    }


    private fun parseExtendsStatement() {
        assert(tokenType == TokenLibrary.EXTENDS_KEYWORD)

        if (!nextExpect(TokenLibrary.IDENTIFIER, GDSyntaxBundle.message("SYNTAX.expected.identifier")))
            return

        next()
    }

    private fun parseClassNameStatement() {
        assert(builder.tokenType == TokenLibrary.CLASS_NAME_KEYWORD)

        if (!nextExpect(TokenLibrary.IDENTIFIER, GDSyntaxBundle.message("SYNTAX.expected.identifier")))
            return

        next()
    }
}

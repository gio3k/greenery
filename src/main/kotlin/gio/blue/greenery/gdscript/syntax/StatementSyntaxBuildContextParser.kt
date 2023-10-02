package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.elements.TokenLibrary

class StatementSyntaxBuildContextParser(parser: SyntaxParser, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        parser, builder
    ) {

    /**
     * Parse a statement
     */
    fun parse() {
        println("parse: $tokenType")
        val t0 = tokenType ?: return
        when (t0) {
            TokenLibrary.EXTENDS_KEYWORD -> return parseExtendsStatement()
            TokenLibrary.CLASS_NAME_KEYWORD -> return parseClassNameStatement()
        }

        // Unknown token
        next()
        builder.error(
            GDSyntaxBundle.message(
                "SYNTAX.expected.statement.got.0", t0.toString()
            )
        )
    }
    
    private fun parseExtendsStatement() {
        assert(tokenType == TokenLibrary.EXTENDS_KEYWORD)

        if (!nextForExpectedElementAfterThis(TokenLibrary.IDENTIFIER)) return

        if (!nextForStatementBreakAfterThis()) return

        next()
    }

    private fun parseClassNameStatement() {
        assert(builder.tokenType == TokenLibrary.CLASS_NAME_KEYWORD)

        if (!nextForExpectedElementAfterThis(TokenLibrary.IDENTIFIER)) return

        if (!nextForStatementBreakAfterThis()) return

        next()
    }
}

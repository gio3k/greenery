package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.PsiBuilder
import gio.blue.greenery.gdscript.elements.TokenLibrary

class StatementSyntaxParser(parser: SyntaxParser) : SyntaxParserAssociate(parser) {
    /**
     * Parse a statement
     * @param builder PsiBuilder
     */
    fun parse(builder: PsiBuilder) {

    }


    private fun parseExtendsStatement(builder: PsiBuilder) {
        assert(builder.tokenType == TokenLibrary.EXTENDS_KEYWORD)

        if (!builder.nextExpect(TokenLibrary.IDENTIFIER, GDSyntaxBundle.message("SYNTAX.expected.identifier")))
            return



        builder.    next()


    }

    private fun parseClassNameStatement(builder: PsiBuilder) {
        assert(builder.tokenType == TokenLibrary.CLASS_NAME_KEYWORD)


    }


}

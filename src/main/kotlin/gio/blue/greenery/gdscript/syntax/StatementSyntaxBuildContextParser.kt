package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.PsiBuilder
import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.elements.TokenLibrary

class StatementSyntaxBuildContextParser(parser: SyntaxParser, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        parser,
        builder
    ) {

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



        builder.next()


    }

    private fun parseClassNameStatement(builder: PsiBuilder) {
        assert(builder.tokenType == TokenLibrary.CLASS_NAME_KEYWORD)


    }


}

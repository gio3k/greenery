package gio.blue.greenery.gdscript.syntax.expressions

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate

class ExpressionSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    private fun parseSingle(resultSyntaxElement: IElementType): Boolean {
        val mark = mark()
        next()
        mark.done(resultSyntaxElement)
        return true
    }

    /**
     * Returns whether the current token can be counted as the target / end of the expression
     *
     * @return Boolean
     */
    fun checkExpressionIsTarget() : Boolean {
        val tokenTypeAhead1 = builder.lookAhead(1) ?: return true

        if (TokenLibrary.STATEMENT_BREAKERS.contains(tokenTypeAhead1)) {
            return true
        }

        if (tokenTypeAhead1 == TokenLibrary.INDENT) {
            return true
        }

        return false
    }

    fun parse(): Boolean {
        if (tokenType == null) return false

        return when (tokenType) {
            TokenLibrary.FLOAT_LITERAL -> parseSingle(SyntaxLibrary.FLOAT_LITERAL)
            TokenLibrary.INTEGER_LITERAL -> parseSingle(SyntaxLibrary.INTEGER_LITERAL)
            TokenLibrary.HEX_LITERAL -> parseSingle(SyntaxLibrary.HEX_LITERAL)
            TokenLibrary.BINARY_LITERAL -> parseSingle(SyntaxLibrary.BINARY_LITERAL)

            TokenLibrary.IDENTIFIER -> parseSingle(SyntaxLibrary.IDENTIFIER)

            TokenLibrary.TRUE_KEYWORD, TokenLibrary.FALSE_KEYWORD -> parseSingle(SyntaxLibrary.BOOLEAN_LITERAL)

            TokenLibrary.LPAR -> parseExpressionWithOuterParenthesesExpression()
            TokenLibrary.LBRACKET -> parseListExpression()
            TokenLibrary.LBRACE -> parseDictionaryExpression()

            else -> context.operations.parse()
        }
    }
}


package gio.blue.greenery.gdscript.syntax.expressions

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate
import gio.blue.greenery.gdscript.syntax.expressions.arithmetic.parseArithmeticOperationAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.arithmetic.parseExpressionWithUnaryPrefix
import gio.blue.greenery.gdscript.syntax.expressions.booleans.parseExpressionWithBoolNegationPrefix
import gio.blue.greenery.gdscript.syntax.expressions.dictionaries.parseDictionary
import gio.blue.greenery.gdscript.syntax.expressions.identifiers.parseIdentifier
import gio.blue.greenery.gdscript.syntax.expressions.members.parseMemberAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.pars.parseEnclosedExpression

class ExpressionSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    /**
     * Returns whether the current token can be counted as the target / end of the expression part
     *
     * @return Boolean
     */
    private fun isExpressionPartComplete(): Boolean {
        val tokenTypeAhead1 = builder.lookAhead(1) ?: return true
        return TokenLibrary.EXPRESSION_BREAKERS.contains(tokenTypeAhead1)
    }

    private fun parseInnerExpression(): Boolean {
        // Check for grouped / bracket expressions
        when (tokenType) {
            TokenLibrary.LPAR -> return parseEnclosedExpression()
            TokenLibrary.LBRACE -> return parseDictionary()
        }

        // Check for a prefixed expression
        if (TokenLibrary.UNARY_OPERATORS.contains(tokenType))
            return parseExpressionWithUnaryPrefix()
        if (TokenLibrary.BOOLEAN_NEGATION_OPERATORS.contains(tokenType))
            return parseExpressionWithBoolNegationPrefix()

        // Try to parse single token expressions if possible
        if (isExpressionPartComplete()) {
            // Check for a single token expression
            if (parseSingleTokenExpression())
                return true

            // Check for an identifier
            if (tokenType == TokenLibrary.IDENTIFIER)
                return parseIdentifier()
        }

        return false
    }

    fun parse(): Boolean {
        if (tokenType == null)
            return false

        val marker = mark()
        if (!parseInnerExpression()) {
            marker.drop()
            return false
        }

        // Try to continue the expression
        // Check for a member chain (.)
        if (tokenType == TokenLibrary.PERIOD) {
            parseMemberAfterExpression()
        }

        // Check for an arithmetic operation
        if (TokenLibrary.ARITHMETIC_OPERATORS.contains(tokenType)) {
            parseArithmeticOperationAfterExpression()
        }

        marker.done(SyntaxLibrary.COMPLETE_EXPRESSION)
        return true
    }
}


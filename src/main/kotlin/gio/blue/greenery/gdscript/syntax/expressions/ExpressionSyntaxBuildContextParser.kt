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

    /**
     * Returns whether the current token can be counted as the target / end of the expression
     *
     * @return Boolean
     */
    private fun checkExpressionIsTarget(): Boolean {
        val tokenTypeAhead1 = builder.lookAhead(1) ?: return true

        return TokenLibrary.EXPRESSION_BREAKERS.contains(tokenTypeAhead1)
    }

    private fun parseTargetExpression(): Boolean {
        val marker = mark()

        val result: IElementType? = when (tokenType) {
            TokenLibrary.FLOAT_LITERAL -> SyntaxLibrary.FLOAT_LITERAL
            TokenLibrary.INTEGER_LITERAL -> SyntaxLibrary.INTEGER_LITERAL
            TokenLibrary.HEX_LITERAL -> SyntaxLibrary.HEX_LITERAL
            TokenLibrary.BINARY_LITERAL -> SyntaxLibrary.BINARY_LITERAL

            TokenLibrary.IDENTIFIER -> SyntaxLibrary.IDENTIFIER

            TokenLibrary.TRUE_KEYWORD,
            TokenLibrary.FALSE_KEYWORD -> SyntaxLibrary.BOOLEAN_LITERAL

            else -> null
        }

        if (result == null) {
            marker.drop()
            return false
        }

        next()

        marker.done(result)
        return true
    }

    /**
     * Finish assignment expression
     *
     * (expression) (here! eq) (value: expression)
     */
    private fun parseSecondTokenAsAssignmentExpression(marker: SyntaxTreeBuilder.Marker): Boolean {
        assertType(TokenLibrary.EQ)
        next()

        want({ parse() }) {
            marker.error("failed to parse assignment rhs expr")
            return false
        }

        marker.done(SyntaxLibrary.EXPRESSION_PART_ASSIGNMENT)
        return true
    }

    /**
     * Finish targeted expression
     *
     * (expression) (here! targeted operator) (argument: expression)
     */
    private fun parseSecondTokenAsTargetedExpression(marker: SyntaxTreeBuilder.Marker): Boolean {
        assertSet(TokenLibrary.TARGETED_OPERATORS)
        next()

        // Parse argument
        want({ parse() }) {
            marker.error("failed to parse targeted rhs expr")
            return false
        }

        marker.done(SyntaxLibrary.EXPRESSION_PART_TARGETED)
        return true
    }

    /**
     * Finish binary expression
     *
     * (expression) (here! binary operator) (expression)
     */
    private fun parseSecondTokenAsBinaryExpression(marker: SyntaxTreeBuilder.Marker): Boolean {
        assertSet(TokenLibrary.BINARY_OPERATORS)
        next()

        // Parse argument
        want({ parse() }) {
            marker.error("failed to parse binary rhs expr")
            return false
        }

        marker.done(SyntaxLibrary.EXPRESSION_PART_BINARY)
        return true
    }

    private fun tryParseMultiTokenExpression(): Boolean {
        if (tokenType == null) return false
        val marker = mark()
        val tokenType0 = tokenType
        next()

        // Start checking
        when (val tokenType1 = tokenType) {
            TokenLibrary.EQ -> {
                next()
            }

            TokenLibrary.BINARY_OPERATORS.contains(tokenType1) -> {
            
            }
        }
    }

    fun parse(): Boolean {
        if (tokenType == null) return false

        val marker = mark()

        fun parseInner(): Boolean {
            if (checkExpressionIsTarget()) {
                return parseTargetExpression()
            }

            // Check for prefixes
            // Check for unary
            if (TokenLibrary.UNARY_OPERAT RS.contains(tokenType)) {
                return parseExpressionPrefixedByUnary()
            }

            // Check for negation
            if (TokenLibrary.NEGATION_OPERATORS.contains(tokenType)) {
                return parseExpressionPrefixedByNegation()
            }

            // Check for grouped / bracket expressions
            when (tokenType) {
                TokenLibrary.LPAR -> parseExpressionWithOuterParenthesesExpression()
                TokenLibrary.LBRACKET -> parseListExpression()
                TokenLibrary.LBRACE -> parseDictionaryExpression()
            }

            return parseMultiPartExpression()
        }

        return if (parseInner()) {
            marker.done(SyntaxLibrary.EXPRESSION)
            true
        } else {
            marker.drop()
            false
        }
    }
}


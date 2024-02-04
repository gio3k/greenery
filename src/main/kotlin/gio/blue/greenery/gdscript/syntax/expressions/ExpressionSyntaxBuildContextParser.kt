package gio.blue.greenery.gdscript.syntax.expressions

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate
import gio.blue.greenery.gdscript.syntax.expressions.arithmetic.parseExpressionWithUnaryPrefix
import gio.blue.greenery.gdscript.syntax.expressions.arithmetic.parseMathBinOpAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.arithmetic.parseMathTargetedOpAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.booleans.parseBoolBinOpAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.booleans.parseExpressionWithBoolNegationPrefix
import gio.blue.greenery.gdscript.syntax.expressions.collections.parseArrayCtor
import gio.blue.greenery.gdscript.syntax.expressions.collections.parseDictionaryCtor
import gio.blue.greenery.gdscript.syntax.expressions.collections.parseIndexerAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.identifiers.parseIdentifier
import gio.blue.greenery.gdscript.syntax.expressions.lambda.parseFunctionLambda
import gio.blue.greenery.gdscript.syntax.expressions.logic.parseIfAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.members.parseMemberAfterExpression
import gio.blue.greenery.gdscript.syntax.expressions.pars.parseEnclosedExpression
import gio.blue.greenery.gdscript.syntax.expressions.strings.parseStringLiteral

class ExpressionSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    private fun parseInnerExpression(): Boolean {
        // Check for grouped / bracket expressions
        when (tokenType) {
            TokenLibrary.LPAR -> return parseEnclosedExpression()
            TokenLibrary.LBRACE -> return parseDictionaryCtor()
            TokenLibrary.LBRACKET -> return parseArrayCtor()
            TokenLibrary.FUNC_KEYWORD -> return parseFunctionLambda()
        }

        // Check for a prefixed expression
        if (TokenLibrary.UNARY_OPERATORS.contains(tokenType))
            return parseExpressionWithUnaryPrefix()
        if (TokenLibrary.BOOLEAN_NEGATION_OPERATORS.contains(tokenType))
            return parseExpressionWithBoolNegationPrefix()

        // Check for a node path
        if (tokenType == TokenLibrary.DOLLAR)
            return parseNodePath()

        // Check for a string literal
        if (TokenLibrary.STRING_STARTERS.contains(tokenType))
            return parseStringLiteral()

        // Check for a single token expression
        if (parseSingleLiteral())
            return true

        // Check for an identifier
        if (tokenType == TokenLibrary.IDENTIFIER)
            return parseIdentifier()

        return false
    }

    fun parse(continueExpression: Boolean = true, ignoreAssignmentContinuation: Boolean = false): Boolean {
        if (tokenType == null)
            return false

        val marker = mark()

        if (!parseInnerExpression()) {
            marker.drop()
            return false
        }

        if (continueExpression) {
            // Try to continue the expression
            while (tokenType != null) {
                val wasExpressionContinuationSuccessful = when {
                    // Check for a member chain (.)
                    tokenType == TokenLibrary.PERIOD -> parseMemberAfterExpression()

                    // Check for an indexer
                    tokenType == TokenLibrary.LBRACKET -> parseIndexerAfterExpression()

                    // Check for an assignment
                    !ignoreAssignmentContinuation && tokenType == TokenLibrary.EQ -> parseAssignmentAfterExpression()

                    // Check for "is"
                    tokenType == TokenLibrary.IS_KEYWORD -> parseIsAfterExpression()

                    // Check for "if"
                    tokenType == TokenLibrary.IF_KEYWORD -> parseIfAfterExpression()

                    // Check for a 2-part math operation
                    TokenLibrary.MATH_BINARY_OPERATORS.contains(tokenType) -> parseMathBinOpAfterExpression()

                    // Check for a 2-part bool operation
                    TokenLibrary.BOOLEAN_BINARY_OPERATORS.contains(tokenType) -> parseBoolBinOpAfterExpression()

                    // Check for a targeted math operation
                    TokenLibrary.MATH_TARGETED_OPERATORS.contains(tokenType) -> parseMathTargetedOpAfterExpression()

                    // Fallback
                    else -> false
                }

                // Keep trying to continue the expression if the last continuation succeeded
                if (wasExpressionContinuationSuccessful)
                    continue

                break
            }
        }

        marker.done(SyntaxLibrary.EXPRESSION)
        return true
    }
}


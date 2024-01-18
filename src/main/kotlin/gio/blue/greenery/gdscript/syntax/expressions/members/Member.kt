package gio.blue.greenery.gdscript.syntax.expressions.members

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a member following up an expression
 *
 * (lhs: expression) (here! period) (rhs: identifier)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the followup member was parsed
 */
fun ExpressionSyntaxBuildContextParser.parseMemberAfterExpression(): Boolean {
    val marker = mark()
    assertType(TokenLibrary.PERIOD)
    next()

    // Expect an identifier
    if (tokenType != TokenLibrary.IDENTIFIER) {
        marker.error(message("SYNTAX.expr.member.expected.identifier"))
        return false
    }

    // Parse identifier
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        return false
    }

    marker.done(SyntaxLibrary.EXPRESSION_MEMBER_CHAIN)
    return true
}
package gio.blue.greenery.gdscript.syntax.expressions.collections

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse an indexer following up an expression
 *
 * (lhs: expression) (here! LBRACKET) (index: expression) (RBRACKET)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the indexer was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseIndexerAfterExpression(): Boolean {
    assertType(TokenLibrary.LBRACKET)
    val marker = mark()
    next()

    // Read expression
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", tokenType.toString()))
        return false
    }

    // Expect closing bracket
    if (tokenType != TokenLibrary.RBRACKET) {
        marker.error(message("SYNTAX.expr.indexer.expected.end.got.0", tokenType.toString()))
        return false
    }

    next()
    marker.done(SyntaxLibrary.EXPRESSION_INDEXER)
    return true
}
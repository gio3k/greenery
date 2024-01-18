package gio.blue.greenery.gdscript.syntax.statements.loops

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a for-loop statement starting from the current token
 *
 * (here! for keyword) (x: identifier) (in keyword) (iterable: expression) (colon) (block)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseFor(): Boolean {
    assertType(TokenLibrary.FOR_KEYWORD)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.stmt.for.expected.identifier"))
        return false
    }

    wantThenNext({ tokenType == TokenLibrary.IN_KEYWORD }) {
        marker.error(message("SYNTAX.stmt.for.expected.in"))
        return false
    }

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.stmt.for.expected.expr"))
        return false
    }

    // Parse block
    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.FOR_STATEMENT)
    return true
}
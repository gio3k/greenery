package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * For statement
 *
 * (here! for keyword) (x: identifier) (in keyword) (iterable: expression) (colon) (block)
 */
fun StatementSyntaxBuildContextParser.parseForStatement(): Boolean {
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
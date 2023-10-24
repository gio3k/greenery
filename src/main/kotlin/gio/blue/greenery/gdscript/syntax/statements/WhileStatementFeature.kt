package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * While statement
 *
 * (here! while keyword) (condition: expression) (colon) (block)
 */
fun StatementSyntaxBuildContextParser.parseWhileStatement(): Boolean {
    assertType(TokenLibrary.WHILE_KEYWORD)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.stmt.while.expected.expr"))
        return false
    }

    // Parse block
    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.WHILE_STATEMENT)
    return true
}
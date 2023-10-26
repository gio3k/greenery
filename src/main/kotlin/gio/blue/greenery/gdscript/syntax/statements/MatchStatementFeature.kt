package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * While statement
 *
 * (here! match keyword) (condition: expression) (colon) (indent)
 */
fun StatementSyntaxBuildContextParser.parseMatchStatement(): Boolean {
    assertType(TokenLibrary.MATCH_KEYWORD)
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
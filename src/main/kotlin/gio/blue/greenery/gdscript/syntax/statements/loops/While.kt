package gio.blue.greenery.gdscript.syntax.statements.loops

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a while statement starting from the current token
 *
 * (here! while keyword) (condition: expression) (colon) (block)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseWhile(): Boolean {
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
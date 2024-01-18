package gio.blue.greenery.gdscript.syntax.statements.logic

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse the else part (starting at the current token) of an if statement
 *
 * (here! else keyword) (colon) (block)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the part was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseIfElsePart(): Boolean {
    assertType(TokenLibrary.ELSE_KEYWORD)
    val marker = mark()
    next()

    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.IF_PART_ELSE)
    return true
}
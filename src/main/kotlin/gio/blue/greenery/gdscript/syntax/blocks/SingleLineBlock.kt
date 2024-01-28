package gio.blue.greenery.gdscript.syntax.blocks

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Parse a single line block starting from the current token
 *
 * @receiver BlockSyntaxBuildContextParser
 * @return Boolean Whether the block was fully parsed
 */
fun BlockSyntaxBuildContextParser.parseSingleLineBlock(): Boolean {
    val marker = mark()

    // Parse first statement
    if (!context.statements.parse()) {
        parseUnknownToken()
        marker.drop()
        return false
    }

    while (tokenType == TokenLibrary.SEMICOLON) {
        next() // Skip the semicolon

        val statementMarker = mark()
        if (!context.statements.parse()) {
            parseUnknownToken()
            statementMarker.error(message("SYNTAX.core.block.expected.indent"))
            marker.drop()
            return false
        }
        statementMarker.drop()
    }

    marker.done(SyntaxLibrary.STATEMENT_GROUP)
    return true
}
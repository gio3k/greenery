package gio.blue.greenery.gdscript.syntax.blocks

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Parse an indented block starting from the current token
 *
 * @receiver BlockSyntaxBuildContextParser
 * @return Boolean Whether the block was fully parsed
 */
fun BlockSyntaxBuildContextParser.parseIndentedBlock(): Boolean {
    assertType(TokenLibrary.LINE_BREAK)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.INDENT }) {
        marker.error(message("SYNTAX.core.block.expected.indent"))
        return false
    }

    while (tokenType != null) {
        // Skip line breaks
        skip(TokenLibrary.LINE_BREAK)

        if (tokenType == TokenLibrary.DEDENT) {
            next()
            break
        }

        val statementMarker = mark()
        if (!context.statements.parse()) {
            parseUnknownToken()
            statementMarker.error(message("SYNTAX.generic.expected.stmt"))
            continue
        }
        statementMarker.drop()
    }

    marker.done(SyntaxLibrary.STATEMENT_GROUP)
    return true
}
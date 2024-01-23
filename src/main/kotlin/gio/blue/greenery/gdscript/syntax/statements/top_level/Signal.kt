package gio.blue.greenery.gdscript.syntax.statements.top_level

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a top-level signal statement starting from the current token
 *
 * (here! SIGNAL) (identifier)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseSignal(): Boolean {
    assertType(TokenLibrary.SIGNAL_KEYWORD)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.generic.expected.identifier.after.0", "signal"))
        return false
    }

    marker.done(SyntaxLibrary.SIGNAL_STATEMENT)
    return true
}
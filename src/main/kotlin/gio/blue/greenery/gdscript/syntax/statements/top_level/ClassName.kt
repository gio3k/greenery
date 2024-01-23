package gio.blue.greenery.gdscript.syntax.statements.top_level

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a top-level class_name statement starting from the current token
 *
 * (here! class name keyword) (identifier)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseClassName(): Boolean {
    assertType(TokenLibrary.CLASS_NAME_KEYWORD)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.generic.expected.identifier"))
        return false
    }

    marker.done(SyntaxLibrary.CLASS_NAME_STATEMENT)
    return true
}
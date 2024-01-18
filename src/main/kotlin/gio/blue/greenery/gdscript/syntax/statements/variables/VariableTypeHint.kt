package gio.blue.greenery.gdscript.syntax.statements.variables

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a variable's type hint starting from the current token
 *
 * (here! colon) (type identifier: identifier)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the part was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseVariableTypeHint(): Boolean {
    assertType(TokenLibrary.COLON)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.generic.expected.identifier"))
        return false
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_TYPE_HINT)
    return true
}
package gio.blue.greenery.gdscript.syntax.statements.func

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a function's type hint starting at the current token
 *
 * (here! rarrow) (type: identifier)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the type hint was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseFunctionTypeHint(): Boolean {
    assertType(TokenLibrary.RARROW)
    val marker = mark()
    next()

    // Expect identifier
    if (tokenType != TokenLibrary.IDENTIFIER) {
        marker.error(message("SYNTAX.stmt.func.type-hint.expected.identifier"))
        return false
    }

    next()
    marker.done(SyntaxLibrary.FUNCTION_DECL_TYPE_HINT)
    return true
}
package gio.blue.greenery.gdscript.syntax.statements.func

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a function parameter's type hint starting from the current token
 *
 * (here! colon) (type name: identifier)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the type hint was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseFunctionParameterTypeHint(): Boolean {
    assertType(TokenLibrary.COLON)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.stmt.func.param.type-hint.expected.identifier"))
        return false
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_TYPE_HINT)
    return true
}
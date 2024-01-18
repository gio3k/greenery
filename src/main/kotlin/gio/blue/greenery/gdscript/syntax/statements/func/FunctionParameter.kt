package gio.blue.greenery.gdscript.syntax.statements.func

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a function declaration's parameter definition starting from the current token
 *
 * (here! identifier) [(type hint)] [(default value: expression)]
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the parameter was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseFunctionParameter(): Boolean {
    assertType(TokenLibrary.IDENTIFIER)
    val marker = mark()
    next()

    if (tokenType == TokenLibrary.COLON) {
        parseFunctionParameterTypeHint()
    }

    if (tokenType == TokenLibrary.EQ) {
        parseFunctionParameterDefaultValue()
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER)
    return true
}
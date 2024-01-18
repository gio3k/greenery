package gio.blue.greenery.gdscript.syntax.statements.func

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a function declaration's parameter list starting at the current token
 *
 * (here! lpar) [(parameter)(comma...)] (rpar)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the list was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseFunctionParameterList(): Boolean {
    assertType(TokenLibrary.LPAR)
    val marker = mark()
    next()

    while (tokenType != null) {
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        if (tokenType == TokenLibrary.RPAR) {
            next()
            break
        }

        // Try to parse a parameter
        want({ tokenType == TokenLibrary.IDENTIFIER && parseFunctionParameter() }) {
            marker.error(message("SYNTAX.stmt.func.param-list.expected.param"))
            return false
        }

        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        // We just read a pair, expect a comma or right brace
        want({ tokenType == TokenLibrary.COMMA || tokenType == TokenLibrary.RPAR }) {
            marker.error(message("SYNTAX.stmt.func.param-list.expected.delimiter-or-end"))
            return false
        }

        if (tokenType != TokenLibrary.RPAR)
            next()
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_LIST)
    return true
}
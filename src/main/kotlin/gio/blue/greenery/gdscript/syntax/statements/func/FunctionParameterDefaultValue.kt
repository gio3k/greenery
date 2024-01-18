package gio.blue.greenery.gdscript.syntax.statements.func

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a function parameter's default value starting from the current token
 *
 * (here! eq) (value: expression)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the default value part was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseFunctionParameterDefaultValue(): Boolean {
    assertType(TokenLibrary.EQ)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.stmt.func.param.default.expected.expr"))
        return false
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_DEFAULT_ASSIGNMENT)
    return true
}
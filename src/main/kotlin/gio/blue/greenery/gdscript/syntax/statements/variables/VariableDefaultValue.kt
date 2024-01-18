package gio.blue.greenery.gdscript.syntax.statements.variables

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a variable's default value starting from the current token
 *
 * (here! eq) (value: expression)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the part was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseVariableDefaultValue(): Boolean {
    assertType(TokenLibrary.EQ)
    val marker = mark()
    next()

    val expressionMarker = mark()
    want({ context.expressions.parse() }) {
        expressionMarker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        marker.drop()
        return false
    }

    expressionMarker.drop()
    marker.done(SyntaxLibrary.VARIABLE_DECL_DEFAULT_ASSIGNMENT)
    return true
}
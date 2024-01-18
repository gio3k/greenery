package gio.blue.greenery.gdscript.syntax.statements.top_level

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a top-level extends statement starting from the current token
 *
 * (here! extends keyword) (identifier)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseExtends(): Boolean {
    assertType(TokenLibrary.EXTENDS_KEYWORD)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        return false
    }

    marker.done(SyntaxLibrary.EXTENDS_STATEMENT)
    return true
}
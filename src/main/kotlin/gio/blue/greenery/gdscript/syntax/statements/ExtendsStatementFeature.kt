package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Extends statement
 *
 * (here! extends keyword) (expression)
 */
fun StatementSyntaxBuildContextParser.parseExtendsStatement(): Boolean {
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
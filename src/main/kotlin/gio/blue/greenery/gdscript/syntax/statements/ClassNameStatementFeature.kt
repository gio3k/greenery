package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Class name statement
 *
 * (here! class name keyword) (expression)
 */
fun StatementSyntaxBuildContextParser.parseClassNameStatement(): Boolean {
    assertType(TokenLibrary.CLASS_NAME_KEYWORD)
    val marker = mark()
    this.next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        return false
    }

    marker.done(SyntaxLibrary.CLASS_NAME_STATEMENT)
    return true
}
package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Pass statement
 *
 * (here! pass keyword)
 */
fun StatementSyntaxBuildContextParser.parsePassStatement(): Boolean {
    assertType(TokenLibrary.PASS_KEYWORD)
    next()
    return true
}
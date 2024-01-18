package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Parse a pass statement
 *
 * (here! pass keyword)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parsePass(): Boolean {
    assertType(TokenLibrary.PASS_KEYWORD)
    next()
    return true
}
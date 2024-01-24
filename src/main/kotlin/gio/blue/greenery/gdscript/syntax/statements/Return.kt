package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Parse a return statement
 *
 * (here! RETURN) [ return value expression ]
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseReturn(): Boolean {
    assertType(TokenLibrary.RETURN_KEYWORD)
    val marker = mark()
    next()

    // See if we're returning something
    // We can find this out by checking for a statement breaker
    if (TokenLibrary.STATEMENT_BREAKERS.contains(tokenType)) {
        // No return expression
        marker.done(SyntaxLibrary.RETURN_STATEMENT)
        return true
    }

    // There's a return expression - try to parse it
    // Read expression
    want({ parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.after.0.got.1", "return", tokenType.toString()))
        return false
    }

    marker.done(SyntaxLibrary.RETURN_STATEMENT)
    return true
}
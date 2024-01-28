package gio.blue.greenery.gdscript.syntax.expressions.pars

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser

/**
 * Parse a set of multiple expressions
 *
 * (lpar) [(expression)(...comma...)] (rpar)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the set was parsed
 */
fun ExpressionSyntaxBuildContextParser.parseSet(): Boolean {
    assertType(TokenLibrary.LPAR)
    val marker = mark()
    next()

    while (tokenType != TokenLibrary.RPAR) {
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)
        skip(TokenLibrary.SPACE)

        // Parse the expression
        want({ parse() }) {
            marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
            return false
        }

        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)
        skip(TokenLibrary.DEDENT)

        // We just read a pair, expect a comma or right parenthesis
        if (tokenType == TokenLibrary.COMMA) {
            next()
            continue
        }

        if (tokenType != TokenLibrary.RPAR) {
            marker.error(message("SYNTAX.expr.set.expected.delimiter-or-end"))
            return false
        }
    }

    // We should be on the right parenthesis token now - skip it
    next()
    marker.done(SyntaxLibrary.EXPRESSION_GROUP)
    return true
}
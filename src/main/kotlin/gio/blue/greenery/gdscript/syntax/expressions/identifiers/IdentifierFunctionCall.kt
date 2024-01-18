package gio.blue.greenery.gdscript.syntax.expressions.identifiers

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser
import gio.blue.greenery.gdscript.syntax.expressions.pars.parseSet

/**
 * Parse function call / arguments following up an identifier
 *
 * (lhs: identifier) (here! lpar) [(expression)(...comma...)] (rpar)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the function call was fully parsed
 */
internal fun ExpressionSyntaxBuildContextParser.parseIdentifierFunctionCall(): Boolean {
    assertType(TokenLibrary.LPAR)

    want({ parseSet() }) { return false }

    return true
}
package gio.blue.greenery.gdscript.syntax.expressions

import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Parse a string literal at the current token
 *
 * (here! string marker)
 *
 * @receiver ExpressionSyntaxBuildContextParser
 * @return Boolean Whether the expression was fully parsed
 */
fun ExpressionSyntaxBuildContextParser.parseStringLiteral(): Boolean {
    assertSet(TokenLibrary.STRING_STARTERS)
    val marker = mark()
    next()



    // Drop the marker - the parsed expression should already be in the tree
    marker.drop()
    return true
}
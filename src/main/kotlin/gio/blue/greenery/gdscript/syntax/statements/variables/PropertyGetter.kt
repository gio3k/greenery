package gio.blue.greenery.gdscript.syntax.statements.variables

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse the property getter for a variable starting at the current token
 *
 * (here! get keyword) (colon) (block)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the part was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parsePropertyGetter(): Boolean {
    assertType(TokenLibrary.GET_KEYWORD)
    val marker = mark()
    next()

    // Parse block
    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_PROPERTY_GETTER)
    return true
}
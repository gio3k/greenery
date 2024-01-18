package gio.blue.greenery.gdscript.syntax.statements.variables

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse the property setter for a variable starting at the current token
 *
 * (here! set keyword) (lpar) (value variable identifier: expression) (rpar) (colon) (block)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the part was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parsePropertySetter(): Boolean {
    assertType(TokenLibrary.SET_KEYWORD)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.LPAR }) {
        marker.error(message("SYNTAX.stmt.var.prop.expected.single-parameter-list"))
        return false
    }

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.generic.expected.identifier"))
        return false
    }

    wantThenNext({ tokenType == TokenLibrary.RPAR }) {
        marker.error(message("SYNTAX.generic.expected.0", it.toString()))
        return false
    }

    // Parse block
    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_PROPERTY_SETTER)
    return true
}
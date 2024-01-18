package gio.blue.greenery.gdscript.syntax.statements.variables

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a generic variable declaration starting from the current token
 *
 * (here! var keyword) (name: identifier) [(type hint)] [(default value: expression)] [(property followup)]
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the declaration was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseVariableDeclaration(): Boolean {
    assertType(TokenLibrary.VAR_KEYWORD)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.generic.expected.identifier"))
        return false
    }

    if (tokenType == TokenLibrary.COLON) {
        want({ parseVariableTypeHint() }) {
            marker.drop()
            return false
        }
    }

    if (tokenType == TokenLibrary.EQ) {
        want({ parseVariableDefaultValue() }) {
            marker.drop()
            return false
        }
    }

    if (tokenType == TokenLibrary.COLON) {
        want({ parsePropertyFollowup() }) {
            marker.drop()
            return false
        }
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_STATEMENT)
    return true
}


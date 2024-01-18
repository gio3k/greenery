package gio.blue.greenery.gdscript.syntax.statements.variables

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a constant variable declaration starting from the current token
 *
 * (here! const keyword) (name: identifier) [(type hint)] (eq) (value: expression)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the declaration was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseConstantDeclaration(): Boolean {
    assertType(TokenLibrary.CONST_KEYWORD)
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

    want({ tokenType == TokenLibrary.EQ }) {
        marker.error(message("SYNTAX.stmt.var.const.expected.value"))
        return false
    }

    want({ parseVariableDefaultValue() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.CONSTANT_DECL_STATEMENT)
    return true
}
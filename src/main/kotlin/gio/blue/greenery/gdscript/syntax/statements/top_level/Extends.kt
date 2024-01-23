package gio.blue.greenery.gdscript.syntax.statements.top_level

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.strings.parseStringLiteral
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a top-level extends statement starting from the current token
 *
 * (here! EXTENDS) (identifier or string)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseExtends(): Boolean {
    assertType(TokenLibrary.EXTENDS_KEYWORD)
    val marker = mark()
    next()

    // Check for identifier
    if (tokenType == TokenLibrary.IDENTIFIER) {
        next()
    } else if (TokenLibrary.STRING_STARTERS.contains(tokenType)) {
        want({ context.expressions.parseStringLiteral() }) {
            marker.error(message("SYNTAX.stmt.extends.expected.identifier-or-string"))
            return false
        }
    } else {
        marker.error(message("SYNTAX.stmt.extends.expected.identifier-or-string"))
        return false
    }

    marker.done(SyntaxLibrary.EXTENDS_STATEMENT)
    return true
}
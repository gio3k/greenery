package gio.blue.greenery.gdscript.syntax.statements.func

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse a function declaration starting from the current token
 *
 * (here! func keyword) (name: identifier) (parameters: parameter list) [type hint] (colon) (block)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the declaration was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseFunctionDeclaration(): Boolean {
    assertType(TokenLibrary.FUNC_KEYWORD)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.stmt.func.expected.identifier"))
        return false
    }

    want({ tokenType == TokenLibrary.LPAR && parseFunctionParameterList() }) {
        marker.error(message("SYNTAX.stmt.func.expected.parameter-list"))
        return false
    }

    // Check for a type hint
    if (tokenType == TokenLibrary.RARROW) {
        want({ parseFunctionTypeHint() }) {
            marker.drop()
            return false
        }
    }

    // Parse block
    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_STATEMENT)
    return true
}

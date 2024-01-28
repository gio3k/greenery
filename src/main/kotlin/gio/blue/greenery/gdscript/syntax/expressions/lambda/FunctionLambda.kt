package gio.blue.greenery.gdscript.syntax.expressions.lambda

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser
import gio.blue.greenery.gdscript.syntax.statements.func.parseFunctionParameterList
import gio.blue.greenery.gdscript.syntax.statements.func.parseFunctionTypeHint

fun ExpressionSyntaxBuildContextParser.parseFunctionLambda(): Boolean {
    assertType(TokenLibrary.FUNC_KEYWORD)
    val marker = mark()
    next()

    want({ tokenType == TokenLibrary.LPAR && context.statements.parseFunctionParameterList() }) {
        marker.error(message("SYNTAX.stmt.func.expected.parameter-list"))
        return false
    }

    // Check for a type hint
    if (tokenType == TokenLibrary.RARROW) {
        want({ context.statements.parseFunctionTypeHint() }) {
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

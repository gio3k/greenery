package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Function type hint
 *
 * (here! rarrow) (type: identifier)
 */
private fun StatementSyntaxBuildContextParser.parseFunctionDeclTypeHint(): Boolean {
    assertType(TokenLibrary.RARROW)
    val marker = mark()
    next()

    // Expect identifier
    if (tokenType != TokenLibrary.IDENTIFIER) {
        marker.error(message("SYNTAX.stmt.func.type-hint.expected.identifier"))
        return false
    }

    next()
    marker.done(SyntaxLibrary.FUNCTION_DECL_TYPE_HINT)
    return true
}

/**
 * Function declaration statement
 *
 * (here! func keyword) (name: identifier) (parameters: parameter list) [type hint] (colon) (block)
 */
fun StatementSyntaxBuildContextParser.parseFunctionDeclarationStatement(): Boolean {
    assertType(TokenLibrary.FUNC_KEYWORD)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.stmt.func.expected.identifier"))
        return false
    }

    want({ tokenType == TokenLibrary.LPAR && parseFunctionDeclParameterList() }) {
        marker.error(message("SYNTAX.stmt.func.expected.parameter-list"))
        return false
    }

    // Check for a type hint
    if (tokenType == TokenLibrary.RARROW) {
        want({ parseFunctionDeclTypeHint() }) {
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

//region Parameters

/**
 * Parameter type hint
 *
 * (here! colon) (type name: identifier)
 */
private fun StatementSyntaxBuildContextParser.parseFunctionDeclParameterTypeHint(): Boolean {
    assertType(TokenLibrary.COLON)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.stmt.func.param.type-hint.expected.identifier"))
        return false
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_TYPE_HINT)
    return true
}

/**
 * Parameter default value expression
 *
 * (here! eq) (value: expression)
 */
private fun StatementSyntaxBuildContextParser.parseFunctionDeclParameterDefaultValueExpression(): Boolean {
    assertType(TokenLibrary.EQ)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.stmt.func.param.default.expected.expr"))
        return false
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_DEFAULT_ASSIGNMENT)
    return true
}

/**
 * Function declaration parameter
 *
 * (here! identifier) [(type hint)] [(default value: expression)]
 */
fun StatementSyntaxBuildContextParser.parseFunctionDeclParameter(): Boolean {
    assertType(TokenLibrary.IDENTIFIER)
    val marker = mark()
    next()

    if (tokenType == TokenLibrary.COLON) {
        parseFunctionDeclParameterTypeHint()
    }

    if (tokenType == TokenLibrary.EQ) {
        parseFunctionDeclParameterDefaultValueExpression()
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER)
    return true
}

/**
 * Function declaration parameter list
 *
 * (here! lpar) [(parameter)(comma...)] (rpar)
 */
private fun StatementSyntaxBuildContextParser.parseFunctionDeclParameterList(): Boolean {
    assertType(TokenLibrary.LPAR)
    val marker = mark()
    next()

    while (tokenType != null) {
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        if (tokenType == TokenLibrary.RPAR) {
            next()
            break
        }

        // Try to parse a parameter
        want({ tokenType == TokenLibrary.IDENTIFIER && parseFunctionDeclParameter() }) {
            marker.error(message("SYNTAX.stmt.func.param-list.expected.param"))
            return false
        }

        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.INDENT)

        // We just read a pair, expect a comma or right brace
        want({ tokenType == TokenLibrary.COMMA || tokenType == TokenLibrary.RPAR }) {
            marker.error(message("SYNTAX.stmt.func.param-list.expected.delimiter-or-end"))
            return false
        }

        if (tokenType != TokenLibrary.RPAR)
            next()
    }

    marker.done(SyntaxLibrary.FUNCTION_DECL_PARAMETER_LIST)
    return true
}

//endregion
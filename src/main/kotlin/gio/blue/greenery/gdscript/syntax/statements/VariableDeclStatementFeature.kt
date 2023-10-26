package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Property getter
 *
 * (here! get keyword) (colon) (block)
 */
private fun StatementSyntaxBuildContextParser.parsePropertyGetter(): Boolean {
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

/**
 * Property setter
 *
 * (here! set keyword) (lpar) (value variable identifier: expression) (rpar) (colon) (block)
 */
private fun StatementSyntaxBuildContextParser.parsePropertySetter(): Boolean {
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

/**
 * Variable type hint
 *
 * (here! colon) (type identifier: identifier)
 */
private fun StatementSyntaxBuildContextParser.parseVariableTypeHint(): Boolean {
    assertType(TokenLibrary.COLON)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.IDENTIFIER }) {
        marker.error(message("SYNTAX.generic.expected.identifier"))
        return false
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_TYPE_HINT)
    return true
}

/**
 * Variable default value expression
 *
 * (here! eq) (value: expression)
 */
private fun StatementSyntaxBuildContextParser.parseVariableDeclDefaultValueExpression(): Boolean {
    assertType(TokenLibrary.EQ)
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        return false
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_DEFAULT_ASSIGNMENT)
    return true
}

/**
 * Constant declaration
 *
 * (here! const keyword) (name: identifier) [(type hint)] (eq) (value: expression)
 */
fun StatementSyntaxBuildContextParser.parseConstantDeclStatement(): Boolean {
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

    want({ parseVariableDeclDefaultValueExpression() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.CONSTANT_DECL_STATEMENT)
    return true
}

/**
 * Property followup
 *
 * This doesn't handle errors relating to multiple setters, etc. That should be done in PSI code
 *
 * (here! colon) _NEWLINE_ [(setter | getter)] [_NEWLINE_ (setter | getter)]
 */
fun StatementSyntaxBuildContextParser.parsePropertyDeclFollowup(): Boolean {
    assertType(TokenLibrary.COLON)
    val marker = mark()
    next()

    skip(TokenLibrary.LINE_BREAK)

    wantThenNext({ tokenType == TokenLibrary.INDENT }) {
        marker.error(message("SYNTAX.stmt.var.prop.expected.indent"))
        return false
    }

    var getterWasFirst = false
    when (tokenType) {
        TokenLibrary.GET_KEYWORD -> {
            getterWasFirst = true
            want({ parsePropertyGetter() }) {
                marker.drop()
                return false
            }
        }

        TokenLibrary.SET_KEYWORD -> {
            want({ parsePropertySetter() }) {
                marker.drop()
                return false
            }
        }

        else -> {
            next()
            marker.error(message("SYNTAX.stmt.var.prop.expected.get-or-set"))
            return false
        }
    }

    skip(TokenLibrary.LINE_BREAK)

    when {
        tokenType == TokenLibrary.GET_KEYWORD && getterWasFirst -> {
            next()
            marker.error(message("SYNTAX.stmt.var.prop.double.get"))
            return false
        }

        tokenType == TokenLibrary.SET_KEYWORD && !getterWasFirst -> {
            next()
            marker.error(message("SYNTAX.stmt.var.prop.double.set"))
            return false
        }

        tokenType == TokenLibrary.GET_KEYWORD -> {
            want({ parsePropertyGetter() }) {
                marker.drop()
                return false
            }
        }

        tokenType == TokenLibrary.SET_KEYWORD -> {
            want({ parsePropertySetter() }) {
                marker.drop()
                return false
            }
        }
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_PROPERTY)
    return true
}

/**
 * Variable declaration
 *
 * (here! var keyword) (name: identifier) [(type hint)] [(default value: expression)] [(property followup)]
 */
fun StatementSyntaxBuildContextParser.parseVariableDeclStatement(): Boolean {
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
        want({ parseVariableDeclDefaultValueExpression() }) {
            marker.drop()
            return false
        }
    }

    if (tokenType == TokenLibrary.COLON) {
        want({ parsePropertyDeclFollowup() }) {
            marker.drop()
            return false
        }
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_STATEMENT)
    return true
}


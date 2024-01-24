package gio.blue.greenery.gdscript.syntax.statements.variables

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse follow-up property information after a variable declaration
 *
 * This doesn't handle errors relating to multiple setters, etc. - that should be done in PSI code.
 *
 * (here! colon) _NEWLINE_ [(setter | getter)] [_NEWLINE_ (setter | getter)]
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the property information was parsed
 */
fun StatementSyntaxBuildContextParser.parsePropertyFollowup(): Boolean {
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

    // Expect dedent
    skip(TokenLibrary.LINE_BREAK)
    wantThenNext({ tokenType == TokenLibrary.DEDENT }) {
        marker.error("SYNTAX.stmt.var.prop.expected.dedent")
        return false
    }

    marker.done(SyntaxLibrary.VARIABLE_DECL_PROPERTY)
    return true
}
package gio.blue.greenery.gdscript.syntax.statements

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate
import gio.blue.greenery.gdscript.syntax.statements.func.parseFunctionDeclaration
import gio.blue.greenery.gdscript.syntax.statements.logic.parseIf
import gio.blue.greenery.gdscript.syntax.statements.loops.parseFor
import gio.blue.greenery.gdscript.syntax.statements.loops.parseWhile
import gio.blue.greenery.gdscript.syntax.statements.top_level.parseClassName
import gio.blue.greenery.gdscript.syntax.statements.top_level.parseExtends
import gio.blue.greenery.gdscript.syntax.statements.variables.parseConstantDeclaration
import gio.blue.greenery.gdscript.syntax.statements.variables.parseVariableDeclaration

class StatementSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    /**
     * Parse a statement
     */
    fun parse(
        shouldMarkErrorIfUnsuccessful: Boolean = true,
        maintainState: Boolean = false,
        skipLeadingStatementBreakers: Boolean = true
    ): Boolean {
        if (skipLeadingStatementBreakers) {
            skipSet(TokenLibrary.STATEMENT_BREAKERS)
        }

        val marker = mark()

        // Check for dedents
        if (tokenType == TokenLibrary.DEDENT) {
            if (!maintainState)
                next()
            if (shouldMarkErrorIfUnsuccessful) {
                marker.error(
                    message("SYNTAX.core.depth.unexpected.dedent", tokenType.toString())
                )
            } else {
                marker.drop()
            }
            return false
        }

        // Check for indents
        if (tokenType == TokenLibrary.INDENT) {
            if (!maintainState)
                next()
            if (shouldMarkErrorIfUnsuccessful) {
                marker.error(
                    message("SYNTAX.core.depth.unexpected.indent", tokenType.toString())
                )
            } else {
                marker.drop()
            }
            return false
        }

        // Check for null first
        if (tokenType == null) {
            // Stop here
            marker.drop()
            return false
        }

        val statementParseResult = when (tokenType) {
            TokenLibrary.ANNOTATION -> parseAnnotation()

            TokenLibrary.FUNC_KEYWORD -> parseFunctionDeclaration()
            TokenLibrary.IF_KEYWORD -> parseIf()
            TokenLibrary.PASS_KEYWORD -> parsePass()
            TokenLibrary.FOR_KEYWORD -> parseFor()
            TokenLibrary.WHILE_KEYWORD -> parseWhile()

            TokenLibrary.VAR_KEYWORD -> parseVariableDeclaration()
            TokenLibrary.CONST_KEYWORD -> parseConstantDeclaration()

            TokenLibrary.EXTENDS_KEYWORD -> parseExtends()
            TokenLibrary.CLASS_NAME_KEYWORD -> parseClassName()

            else -> {
                // Unknown token - not a statement starter.
                // Attempt to parse it as an expression
                if (parseExpression()) {
                    marker.drop()
                    return true
                }

                // Couldn't parse as an expression, just fail
                val foundTokenType = tokenType
                if (!maintainState)
                    next()
                if (shouldMarkErrorIfUnsuccessful) {
                    marker.error(
                        message("SYNTAX.core.expected.stmt.got.0", foundTokenType.toString())
                    )
                } else {
                    marker.drop()
                }
                return false
            }
        }

        marker.drop()
        return statementParseResult
    }
}


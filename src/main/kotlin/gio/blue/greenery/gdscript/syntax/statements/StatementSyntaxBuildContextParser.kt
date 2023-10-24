package gio.blue.greenery.gdscript.syntax.statements

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate

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
            TokenLibrary.ANNOTATION -> parseAnnotationStatement()

            TokenLibrary.FUNC_KEYWORD -> parseFunctionDeclarationStatement()
            TokenLibrary.IF_KEYWORD -> parseIfStatement()
            TokenLibrary.PASS_KEYWORD -> parsePassStatement()
            TokenLibrary.FOR_KEYWORD -> parseForStatement()
            TokenLibrary.WHILE_KEYWORD -> parseWhileStatement()

            TokenLibrary.VAR_KEYWORD -> parseVariableDeclStatement()
            TokenLibrary.CONST_KEYWORD -> parseConstantDeclStatement()

            TokenLibrary.EXTENDS_KEYWORD -> parseExtendsStatement()
            TokenLibrary.CLASS_NAME_KEYWORD -> parseClassNameStatement()

            else -> {
                // Unknown token - not a statement starter.
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


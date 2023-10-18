package gio.blue.greenery.gdscript.syntax.parsers

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
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
                    message("SYNTAX.generic.unexpected.dedent", tokenType.toString())
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
                    message("SYNTAX.generic.unexpected.indent", tokenType.toString())
                )
            } else {
                marker.drop()
            }
            return false
        }

        val statementParseResult = when (tokenType) {
            TokenLibrary.FUNC_KEYWORD -> context.functions.parse()

            TokenLibrary.EXTENDS_KEYWORD -> parseExtends()
            TokenLibrary.CLASS_NAME_KEYWORD -> parseClassName()
            TokenLibrary.ANNOTATION -> parseAnnotation()
            TokenLibrary.FOR_KEYWORD -> parseFor()
            TokenLibrary.PASS_KEYWORD -> parsePass()

            else -> {
                // Unknown token - not a statement starter.
                val foundTokenType = tokenType
                if (!maintainState)
                    next()
                if (shouldMarkErrorIfUnsuccessful) {
                    marker.error(
                        message("SYNTAX.generic.expected.statement.got.0", foundTokenType.toString())
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

    private fun parseClassInformation(token: IElementType, syntax: IElementType): Boolean {
        assertType(token)
        val marker = mark()
        next()

        if (tokenType != TokenLibrary.IDENTIFIER) {
            marker.error(
                message("SYNTAX.generic.expected.0.got.1", tokenType.toString(), TokenLibrary.IDENTIFIER.toString())
            )
            return false
        }
        next()

        marker.done(syntax)
        return true
    }

    /**
     * Pass
     *
     * (here! pass keyword)
     */
    private fun parsePass(): Boolean {
        assertType(TokenLibrary.PASS_KEYWORD)
        next()
        return true
    }

    private fun parseExtends(): Boolean =
        parseClassInformation(TokenLibrary.EXTENDS_KEYWORD, SyntaxLibrary.EXTENDS_STATEMENT)

    private fun parseClassName(): Boolean =
        parseClassInformation(TokenLibrary.CLASS_NAME_KEYWORD, SyntaxLibrary.CLASS_NAME_STATEMENT)


    /**
     * For statement
     *
     * (here! for) (?: identifier) (in) (iterable: expression) (colon) (block)
     */
    private fun parseFor(): Boolean {
        assertType(TokenLibrary.FOR_KEYWORD)
        val marker = mark()
        next()

        // Expect identifier
        if (tokenType != TokenLibrary.IDENTIFIER) {
            marker.error(
                message(
                    "SYNTAX.generic.expected.0.got.1", TokenLibrary.IDENTIFIER.toString(), tokenType.toString()
                )
            )
            return false
        }
        next()

        // Expect in keyword
        if (tokenType != TokenLibrary.IN_KEYWORD) {
            marker.error(
                message(
                    "SYNTAX.for.expected.in.got.0", tokenType.toString()
                )
            )
            return false
        }
        next()

        // Expect expression
        if (!context.expressions.parse()) {
            marker.error(
                message("SYNTAX.for.expected.expression.got.0", tokenType.toString())
            )
            return false
        }

        // Expect colon
        if (tokenType != TokenLibrary.COLON) {
            marker.error(
                message(
                    "SYNTAX.generic.expected.0.got.1", TokenLibrary.COLON.toString(), tokenType.toString()
                )
            )
            return false
        }
        next()

        // Parse block
        if (!context.blocks.parse()) {
            marker.error(
                message(
                    "SYNTAX.statement.expected.block"
                )
            )
            return false
        }

        marker.done(SyntaxLibrary.FOR_STATEMENT)
        return true
    }

    /**
     * Variable declaration
     *
     * Constant or variable:
     * (here! const or var) (name: identifier) [(colon) (type: identifier)] [(equals) (value: expression)]
     *
     * Property: (here! var) (name: identifier) [(colon) (type: identifier)] (colon)
     *     [_INDENT_ (get) (colon) (block)]
     *     [_INDENT_ (set) (parameter list) (colon) (block)]
     */
    private fun parseVariableDeclaration(): Boolean {
        assert(tokenType == TokenLibrary.VAR_KEYWORD || tokenType == TokenLibrary.CONST_KEYWORD)
        val marker = mark()
        next()

        marker.drop()
        return true
    }

    /**
     * Annotation
     *
     * (at)_NO SPACE_(here! identifier) [(lpar) (argument list) (rpar)] _NEWLINE_
     */
    private fun parseAnnotation(): Boolean {
        assertType(TokenLibrary.ANNOTATION)
        val marker = mark()
        next()

        if (tokenType != TokenLibrary.LPAR) {
            marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
            return true
        }

        // Read a parameter list
        if (!context.expressions.parseBasicExpressionList()) {
            marker.drop()
            return false
        }

        marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
        return true
    }
}


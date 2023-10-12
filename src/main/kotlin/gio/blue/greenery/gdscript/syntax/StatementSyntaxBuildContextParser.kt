package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary

class StatementSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    /**
     * Parse a statement
     */
    fun parse() {
        println("parse: $tokenType")
        val t0 = tokenType ?: return
        when (t0) {
            TokenLibrary.EXTENDS_KEYWORD -> return parseExtendsStatement()
            TokenLibrary.CLASS_NAME_KEYWORD -> return parseClassNameStatement()
            TokenLibrary.ANNOTATION -> return parseAnnotationStatement()
        }

        // Unknown token
        next()
        builder.error(
            GDSyntaxBundle.message(
                "SYNTAX.expected.statement.got.0", t0.toString()
            )
        )
    }

    private fun parseExtendsStatement() {
        assertType(TokenLibrary.EXTENDS_KEYWORD)

        val marker = mark()

        if (!nextForExpectedElementAfterThis(TokenLibrary.IDENTIFIER)) {
            marker.drop()
            return
        }

        if (!nextForStatementBreakAfterThis()) {
            marker.drop()
            return
        }

        next()

        marker.done(SyntaxLibrary.EXTENDS_STATEMENT)
    }

    private fun parseClassNameStatement() {
        assertType(TokenLibrary.CLASS_NAME_KEYWORD)

        val marker = mark()

        if (!nextForExpectedElementAfterThis(TokenLibrary.IDENTIFIER)) {
            marker.drop()
            return
        }

        if (!nextForStatementBreakAfterThis()) {
            marker.drop()
            return
        }

        next()

        marker.done(SyntaxLibrary.CLASS_NAME_STATEMENT)
    }

    private fun parseForStatement() {
        assertType(TokenLibrary.FOR_KEYWORD)

        val marker = mark()

        if (!nextForExpectedElementAfterThis(TokenLibrary.IDENTIFIER)) {
            marker.drop()
            return
        }

        if (!nextForExpectedElementAfterThis(TokenLibrary.IN_KEYWORD)) {
            marker.drop()
            return
        }

        next()

        marker.done(SyntaxLibrary.EXTENDS_STATEMENT)
    }

    private fun parseVariableDeclarationStatement() {
        assertType(TokenLibrary.VAR_KEYWORD)

        val marker = mark()

        if (!nextForExpectedElementAfterThis(TokenLibrary.IDENTIFIER)) {
            marker.drop()
            return
        }

        next()

        // var hello = 3
        // var hello: String = ""
        // var hello:<br><tab>get:<br><tab><tab>return 3

        when (tokenType) {
            TokenLibrary.COLON -> {
                // Type hint or property

            }
        }
    }

    private fun parseAnnotationStatement(): Boolean {
        assertType(TokenLibrary.ANNOTATION)

        val marker = mark()

        // Move forward, see if there are arguments to this annotation
        next()

        // Check for annotation arguments
        if (tokenType == TokenLibrary.LPAR) {
            // Annotation has arguments, parse them
            if (!context.expressions.parseArgumentListExpressionInParentheses()) {
                marker.drop()
                return false // Failure to parse argument list, give up on annotation
            }
        }

        marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
        return true
    }
}

package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.TokenLibrary

class StatementSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    /**
     * Parse a statement
     */
    fun parse(): Boolean {
        val t0 = tokenType ?: return false
        when (t0) {
            TokenLibrary.EXTENDS_KEYWORD -> return parseExtendsStatement()
            TokenLibrary.CLASS_NAME_KEYWORD -> return parseClassNameStatement()
            TokenLibrary.ANNOTATION -> return parseAnnotationStatement()
        }

        // Unknown token
        next()
        builder.error(
            message("SYNTAX.generic.expected.statement.got.0", t0.toString())
        )
        return false
    }

    private fun parseClassInformationStatement(token: IElementType, syntax: IElementType): Boolean {
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
        if (!isCurrentlyOnEndOfStatement()) {
            marker.error(
                message("SYNTAX.statement.expected.statement-break.got.0", tokenType.toString())
            )
            return false
        }

        next()

        marker.done(syntax)
        return true
    }

    private fun parseExtendsStatement(): Boolean =
        parseClassInformationStatement(TokenLibrary.EXTENDS_KEYWORD, SyntaxLibrary.EXTENDS_STATEMENT)

    private fun parseClassNameStatement(): Boolean =
        parseClassInformationStatement(TokenLibrary.CLASS_NAME_KEYWORD, SyntaxLibrary.CLASS_NAME_STATEMENT)

    /**
     * For statement
     *
     * (here! for) (?: identifier) (in) (iterable: expression) (colon) (block)
     */
    private fun parseForStatement(): Boolean {
        assertType(TokenLibrary.FOR_KEYWORD)
        val marker = mark()
        next()

        marker.drop()
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
    private fun parseVariableDeclarationStatement(): Boolean {
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
    private fun parseAnnotationStatement(): Boolean {
        assertType(TokenLibrary.ANNOTATION)
        val marker = mark()
        next()

        if (tokenType != TokenLibrary.LPAR) {
            marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
            return true
        }

        // Read an argument list
        if (!context.expressions.parseArgumentListInParentheses()) {
            marker.drop()
            return false
        }

        marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
        return true
    }
}

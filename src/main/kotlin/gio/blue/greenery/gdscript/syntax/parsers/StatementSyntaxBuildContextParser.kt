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
    fun parse(): Boolean {
        skip(TokenLibrary.LINE_BREAK)

        val t0 = tokenType ?: return false
        when (t0) {
            TokenLibrary.EXTENDS_KEYWORD -> return parseExtends()
            TokenLibrary.CLASS_NAME_KEYWORD -> return parseClassName()
            TokenLibrary.ANNOTATION -> return parseAnnotation()
            TokenLibrary.FOR_KEYWORD -> return parseFor()
        }

        // Unknown token
        next()
        builder.error(
            message("SYNTAX.generic.expected.statement.got.0", t0.toString())
        )
        return false
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
        if (tokenType != null && !TokenLibrary.STATEMENT_BREAKERS.contains(tokenType)) {
            marker.error(
                message("SYNTAX.statement.expected.statement-break.got.0", tokenType.toString())
            )
            return false
        }

        next()

        marker.done(syntax)
        return true
    }

    private fun parseExtends(): Boolean =
        parseClassInformation(TokenLibrary.EXTENDS_KEYWORD, SyntaxLibrary.EXTENDS_STATEMENT)

    private fun parseClassName(): Boolean =
        parseClassInformation(TokenLibrary.CLASS_NAME_KEYWORD, SyntaxLibrary.CLASS_NAME_STATEMENT)

    /**
     * Function declaration
     *
     * (here! func) (name: identifier) (parameters: parameter list) (colon) (block)
     */
    private fun parseFunc(): Boolean {
        assertType(TokenLibrary.FUNC_KEYWORD)
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

        // Read the parameter list
        if (!context.expressions.parseParameterListInParentheses()) {
            marker.error(
                message(
                    "SYNTAX.func-decl.expected.parameter-list"
                )
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

        marker.done(SyntaxLibrary.FUNCTION_STATEMENT)
        return true
    }

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
        if (!context.expressions.parseParameterListInParentheses()) {
            marker.drop()
            return false
        }

        marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
        return true
    }
}

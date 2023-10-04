package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.TokenLibrary

abstract class SyntaxParserBuildContextAssociate(
    val context: SyntaxParserBuildContext,
    protected val builder: SyntaxTreeBuilder
) {
    val tokenType: IElementType?
        get() = builder.tokenType

    /**
     * Checks if the next element is the expected one, errors if not
     * @param et1 IElementType Expected element
     * @return Boolean Whether the element was expected
     */
    protected fun nextForExpectedElementAfterThis(et1: IElementType): Boolean {
        // What we started with
        val t0 = tokenType
        next()

        // What we got
        val t1 = tokenType
        if (t1 == et1)
            return true

        // Error
        builder.error(
            GDSyntaxBundle.message(
                "SYNTAX.expected.identifier.0.after.1.got.2",
                et1.toString(),
                t0.toString(),
                t1.toString()
            )
        )
        return false
    }

    protected fun nextForLineBreakAfterThis(): Boolean {
        // What we started with
        val t0 = tokenType
        next()

        // What we got
        val t1 = tokenType
        if (t1 == TokenLibrary.LINE_BREAK)
            return true

        // Error
        builder.error(
            GDSyntaxBundle.message(
                "SYNTAX.expected.linebreak.after.0.got.1",
                t0.toString(),
                t1.toString()
            )
        )
        return false
    }

    protected fun nextForStatementBreakAfterThis(): Boolean {
        // What we started with
        val t0 = tokenType
        next()

        // What we got
        val t1 = tokenType
        when (t1) {
            TokenLibrary.LINE_BREAK -> return true
            null -> return true
        }

        // Error
        builder.error(
            GDSyntaxBundle.message(
                "SYNTAX.expected.statement.break.after.0.got.1",
                t0.toString(),
                t1.toString()
            )
        )
        return false
    }

    protected fun assertType(et: IElementType) = assert(tokenType == et)

    protected fun next() {
        builder.advanceLexer()
    }

    protected fun markSingleHere(type: IElementType) {
        val marker = mark()
        next()
        marker.done(type)
    }

    protected fun mark(): SyntaxTreeBuilder.Marker {
        return builder.mark()
    }
}
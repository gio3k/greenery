package gio.blue.greenery.gdscript.syntax.statements.logic

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse the conditional (elif, if) part (starting at the current token) of an if statement
 *
 * (here! any) (expression) (colon) (block)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @param resultElementType IElementType Type of syntax element to make on success
 * @return Boolean Whether the part was fully parsed
 */
internal fun StatementSyntaxBuildContextParser.parseIfConditionalPart(resultElementType: IElementType): Boolean {
    val marker = mark()
    next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.stmt.if.conditional.expected.expr"))
        return false
    }

    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(resultElementType)
    return true
}
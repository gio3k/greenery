package gio.blue.greenery.gdscript.syntax.statements

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Conditional (elif, if) part of if statement
 * @param resultElementType Type of syntax element to make on success
 *
 * (here! any) (expression) (colon) (block)
 */
private fun StatementSyntaxBuildContextParser.parseConditionalPartOfIf(resultElementType: IElementType): Boolean {
    val marker = mark()
    next()

    want({ context.expressions.parse() }, {
        marker.error(message("SYNTAX.stmt.if.conditional.expected.expr"))
        return false
    })

    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(resultElementType)
    return true
}

/**
 * Else (else) part of if statement
 *
 * (here! else keyword) (colon) (block)
 */
private fun StatementSyntaxBuildContextParser.parseElsePart(): Boolean {
    assertType(TokenLibrary.ELSE_KEYWORD)
    val marker = mark()
    next()

    want({ context.blocks.parseBlockStartingFromColon() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.IF_PART_ELSE)
    return true
}

/**
 * If statement
 *
 * (here! if keyword) (condition: expression) (colon) (block) [(elif)] [(else)]
 */
fun StatementSyntaxBuildContextParser.parseIfStatement(): Boolean {
    assertType(TokenLibrary.IF_KEYWORD)
    val marker = mark()

    while (tokenType == TokenLibrary.IF_KEYWORD || tokenType == TokenLibrary.ELIF_KEYWORD) {
        want({
            parseConditionalPartOfIf(
                if (tokenType == TokenLibrary.IF_KEYWORD) SyntaxLibrary.IF_PART_IF else SyntaxLibrary.IF_PART_ELIF
            )
        }) {}

        skip(TokenLibrary.LINE_BREAK)
    }

    if (tokenType == TokenLibrary.ELSE_KEYWORD) {
        want({ parseElsePart() }) {
            marker.drop()
            return false
        }
    }

    marker.done(SyntaxLibrary.IF_STATEMENT)
    return true
}
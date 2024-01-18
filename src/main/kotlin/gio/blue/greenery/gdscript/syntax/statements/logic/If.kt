package gio.blue.greenery.gdscript.syntax.statements.logic

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

/**
 * Parse an if statement starting from the current token
 *
 * (here! if keyword) (condition: expression) (colon) (block) [(elif)] [(else)]
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the statement was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseIf(): Boolean {
    assertType(TokenLibrary.IF_KEYWORD)
    val marker = mark()

    while (tokenType == TokenLibrary.IF_KEYWORD || tokenType == TokenLibrary.ELIF_KEYWORD) {
        want({
            parseIfConditionalPart(
                if (tokenType == TokenLibrary.IF_KEYWORD) SyntaxLibrary.IF_PART_IF else SyntaxLibrary.IF_PART_ELIF
            )
        }) {}

        skip(TokenLibrary.LINE_BREAK)
    }

    if (tokenType == TokenLibrary.ELSE_KEYWORD) {
        want({ parseIfElsePart() }) {
            marker.drop()
            return false
        }
    }

    marker.done(SyntaxLibrary.IF_STATEMENT)
    return true
}
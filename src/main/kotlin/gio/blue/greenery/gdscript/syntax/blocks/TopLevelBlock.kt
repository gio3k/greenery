package gio.blue.greenery.gdscript.syntax.blocks

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildScope
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildScopePurpose

/**
 * Parse a top level block starting from the current token
 *
 * @receiver BlockSyntaxBuildContextParser
 * @return Boolean Whether the block was fully parsed
 */
fun BlockSyntaxBuildContextParser.parseTopLevelBlock(): Boolean {
    context.pushScope(SyntaxParserBuildScope(SyntaxParserBuildScopePurpose.TOP_LEVEL))

    while (tokenType != null) {
        // Skip line breaks
        skip(TokenLibrary.LINE_BREAK)
        skip(TokenLibrary.SEMICOLON)

        if (tokenType == null)
            break

        val statementMarker = mark()
        if (!context.statements.parse()) {
            parseUnknownToken()
            statementMarker.drop()
            continue
        }
        statementMarker.done(SyntaxLibrary.STATEMENT)
    }

    context.popScope()
    return true
}
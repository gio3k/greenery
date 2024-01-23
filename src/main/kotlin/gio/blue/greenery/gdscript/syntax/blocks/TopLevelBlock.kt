package gio.blue.greenery.gdscript.syntax.blocks

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildScope
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildScopePurpose

private fun BlockSyntaxBuildContextParser.parseUnknownToken() {
    val marker = mark()
    val unknownTokenType = tokenType
    next()

    val markerErrorMessage = when (unknownTokenType) {
        TokenLibrary.ISSUE_EOF -> "unexpected eof"

        TokenLibrary.ISSUE_BAD_CHARACTER -> "unexpected char"

        TokenLibrary.ISSUE_MIXED_INDENTS -> "unexpected mixed indents"

        TokenLibrary.ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER -> "unexpected string invis char"

        TokenLibrary.ISSUE_STRAY_CARRIAGE_RETURN -> "unexpected carriage return"

        TokenLibrary.ISSUE_DEDENT_DEPTH_UNEXPECTED -> "unexpected dedent depth"

        else -> "unknown token $unknownTokenType"
    }

    marker.error(markerErrorMessage)
}

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

/**
 * Parse an indented block starting from the current token
 *
 * @receiver BlockSyntaxBuildContextParser
 * @return Boolean Whether the block was fully parsed
 */
fun BlockSyntaxBuildContextParser.parseIndentedBlock(): Boolean {
    assertType(TokenLibrary.LINE_BREAK)
    val marker = mark()
    next()

    wantThenNext({ tokenType == TokenLibrary.INDENT }) {
        marker.error("no indent for indented block")
        return false
    }

    while (tokenType != null) {
        // Skip line breaks
        skip(TokenLibrary.LINE_BREAK)

        if (tokenType == TokenLibrary.DEDENT) {
            next()
            break
        }

        val statementMarker = mark()
        if (!context.statements.parse()) {
            parseUnknownToken()
            statementMarker.error("failed to parse statement")
            // skipUntil(TokenLibrary.LINE_BREAK)
            continue
        }
        statementMarker.drop()
    }

    marker.done(SyntaxLibrary.STATEMENT_GROUP)
    return true
}
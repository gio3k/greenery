package gio.blue.greenery.gdscript.syntax.blocks

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate


class BlockSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    /**
     * Block starting with colon
     *
     * (here! colon) (block)
     */
    fun parseBlockStartingFromColon(): Boolean {
        val marker = mark()

        wantThenNext({ tokenType == TokenLibrary.COLON }) {
            marker.error(message("SYNTAX.core.block.expected.colon"))
            return false
        }

        marker.drop()

        return context.blocks.parseIndentedBlock()
    }

    fun BlockSyntaxBuildContextParser.parseUnknownToken() {
        if (tokenType == null)
            return

        val marker = mark()
        val unknownTokenType = tokenType
        next()

        val markerErrorMessage = when (unknownTokenType) {
            TokenLibrary.ISSUE_EOF -> message("SYNTAX.issues.lexer.eof")
            TokenLibrary.ISSUE_BAD_CHARACTER -> message("SYNTAX.issues.lexer.bad-character")
            TokenLibrary.ISSUE_MIXED_INDENTS -> message("SYNTAX.issues.lexer.mixed-indents")
            TokenLibrary.ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER -> message("SYNTAX.issues.lexer.invisible-control-char")
            TokenLibrary.ISSUE_STRAY_CARRIAGE_RETURN -> message("SYNTAX.issues.lexer.carriage-return")
            TokenLibrary.ISSUE_DEDENT_DEPTH_UNEXPECTED -> message("SYNTAX.issues.lexer.unexpected.dedent-depth")

            TokenLibrary.DEDENT -> message("SYNTAX.core.depth.unexpected.dedent")
            TokenLibrary.INDENT -> message("SYNTAX.core.depth.unexpected.indent")

            else -> message("SYNTAX.issues.unexpected.token.0", unknownTokenType.toString())
        }

        marker.error(markerErrorMessage)
    }
}
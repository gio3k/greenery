package gio.blue.greenery.gdscript.syntax.basic

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.*


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

        want({ parseBlock() }) {
            marker.drop()
            return false
        }

        marker.drop()
        return true
    }

    fun parseBlock(): Boolean {
        val marker = mark()
        val isIndentedBlock = tokenType == TokenLibrary.LINE_BREAK

        if (isIndentedBlock) {
            next()

            want({ tokenType == TokenLibrary.INDENT }) {
                marker.error(message("SYNTAX.core.block.expected.indent"))
                return false
            }

            // Skip that indent
            next()
        }

        return context.withinScope(SyntaxParserBuildScope(SyntaxParserBuildScopePurpose.STATEMENT_GROUP)) {
            var encounteredParseError = false
            while (tokenType != null) {
                skipSet(TokenLibrary.STATEMENT_BREAKERS)

                if (!context.statements.parse(shouldMarkErrorIfUnsuccessful = true)) {
                    // Failed to parse the statement
                    // Clean up - make sure we're ready to parse the next one
                    encounteredParseError = true
                }

                if (isIndentedBlock) {
                    skip(TokenLibrary.LINE_BREAK)
                }

                // Check for an end condition
                when {
                    isIndentedBlock && tokenType == TokenLibrary.DEDENT || !isIndentedBlock && tokenType == TokenLibrary.LINE_BREAK || tokenType == null -> {
                        break
                    }
                }
            }

            next()

            if (!encounteredParseError) {
                marker.done(SyntaxLibrary.STATEMENT_GROUP)
                true
            } else {
                marker.drop()
                false
            }
        }
    }
}
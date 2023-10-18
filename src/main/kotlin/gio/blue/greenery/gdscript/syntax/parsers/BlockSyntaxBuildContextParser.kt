package gio.blue.greenery.gdscript.syntax.parsers

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.*

class BlockSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    fun parse(): Boolean {
        val marker = mark()
        val isIndentedBlock = tokenType == TokenLibrary.LINE_BREAK

        if (isIndentedBlock) {
            next()

            // Expect an indent
            val foundTokenType = tokenType
            if (tokenType != TokenLibrary.INDENT) {
                next()
                marker.error(
                    message("SYNTAX.statement.expected.indent.got.0", foundTokenType.toString())
                )
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
                    isIndentedBlock && tokenType == TokenLibrary.DEDENT
                            || !isIndentedBlock && tokenType == TokenLibrary.LINE_BREAK
                            || tokenType == null -> {
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
package gio.blue.greenery.gdscript.syntax.parsers

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.*

class BlockSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    fun parse(): Boolean {
        if (tokenType == TokenLibrary.LINE_BREAK) {
            return parseIndentedBlock()
        }

        return parseSingleLineBlock()
    }

    private fun skipUntilDedentOrEof() {
        while (tokenType != null && tokenType != TokenLibrary.DEDENT) {
            next()
        }
    }

    /**
     * Indented block
     *
     * (colon) (here! _NEWLINE_) _INDENT_ [(statement)(..._STATEMENT BREAK_...)] _DEDENT_
     */
    fun parseIndentedBlock(): Boolean {
        assertType(TokenLibrary.LINE_BREAK)
        val marker = mark()
        next()

        // This token should be an indent
        if (tokenType != TokenLibrary.INDENT) {
            marker.error(
                message("SYNTAX.statement.expected.indent.got.0", tokenType.toString())
            )
            return false
        }
        next()

        context.withinScope(SyntaxParserBuildScope(SyntaxParserBuildScopePurpose.STATEMENT_GROUP)) {
            // Parse statements until we hit the corresponding dedent
            while (tokenType != null && tokenType != TokenLibrary.DEDENT) {
                skip(TokenLibrary.LINE_BREAK)

                val foundTokenType = tokenType
                if (!context.statements.parse()) {
                    marker.error(
                        message("SYNTAX.generic.expected.statement.got.0", foundTokenType.toString())
                    )

                    // Stop actually parsing the block, just finish it and return
                    skipUntilDedentOrEof()
                    return false
                }

                // Check for an end of statement token
                if (TokenLibrary.STATEMENT_BREAKERS.contains(tokenType)) {
                    skipSet(TokenLibrary.STATEMENT_BREAKERS)
                    continue
                } else {
                    marker.error(
                        message("SYNTAX.statement.expected.statement-break.got.0", tokenType.toString())
                    )

                    // Stop actually parsing the block, just finish it and return
                    skipUntilDedentOrEof()
                    return false
                }
            }
        }

        next()
        marker.done(SyntaxLibrary.STATEMENT_GROUP)
        return true
    }

    /**
     * Single line block
     *
     * (colon) (here! (statement)(..._STATEMENT BREAK_...)) _NEWLINE_
     */
    private fun parseSingleLineBlock(): Boolean {
        val marker = mark()

        context.withinScope(SyntaxParserBuildScope(SyntaxParserBuildScopePurpose.STATEMENT_GROUP)) {
            // Parse statements until we hit EOF or a line break
            while (tokenType != null && tokenType != TokenLibrary.LINE_BREAK) {
                val foundTokenType = tokenType

                // Attempt to parse statement
                if (!context.statements.parse()) {
                    marker.error(
                        message("SYNTAX.generic.expected.statement.got.0", foundTokenType.toString())
                    )
                    return false
                }

                // Check for line break or EOF
                if (tokenType == null || tokenType == TokenLibrary.LINE_BREAK) {
                    next()
                    break
                }

                // Check for an end of statement token
                if (TokenLibrary.STATEMENT_BREAKERS.contains(tokenType)) {
                    next() // Move forward to the next statement
                    continue
                }

                marker.error(
                    message("SYNTAX.statement.expected.statement-break.got.0", tokenType.toString())
                )
                return false
            }
        }

        marker.done(SyntaxLibrary.STATEMENT_GROUP)
        return true
    }
}
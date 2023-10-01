package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.elements.TokenLibrary
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Depth")
class DepthTests {
    @Test
    @DisplayName("Scan indents")
    fun scanIndentsForDepth() {
        val lexer = createTestLexer("   \n    \na")

        lexer.expectTypeSkipSpaces(TokenLibrary.INDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)

        lexer.expectTypeSkipSpaces(TokenLibrary.INDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)
    }

    @Test
    @DisplayName("Scan indents and handle depth reversal correctly")
    fun scanIndentsForDepthAndHandleReversal() {
        val lexer = createTestLexer("   \n    \na")

        lexer.expectTypeSkipSpaces(TokenLibrary.INDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)

        lexer.expectTypeSkipSpaces(TokenLibrary.INDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)

        // Because a non-space character is the first character on the line, all indents should be reversed
        lexer.expectTypeSkipSpaces(TokenLibrary.DEDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.DEDENT)
    }

    @Test
    @DisplayName("Maintain state after depth reversal")
    fun scanIndentsForDepthAndHandleReversalSafely() {
        val lexer = createTestLexer("   \n    \na")

        lexer.expectTypeSkipSpaces(TokenLibrary.INDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)

        lexer.expectTypeSkipSpaces(TokenLibrary.INDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)

        // Because a non-space character is the first character on the line, all indents should be reversed
        lexer.expectTypeSkipSpaces(TokenLibrary.DEDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.DEDENT)

        // We should get the identifier (a) now
        lexer.expectTypeSkipSpaces(TokenLibrary.IDENTIFIER, "a?")
    }
}


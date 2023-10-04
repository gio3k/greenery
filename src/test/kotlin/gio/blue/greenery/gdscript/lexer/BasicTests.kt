package gio.blue.greenery.gdscript.lexer

import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Basic Structure")
class BasicTests {
    @Test
    @DisplayName("Basic variable, no param function into pass")
    fun basicStructureForKindScanning() {
        val lexer = createTestLexer("var a = 3\nfunc hello():\n    pass")

        lexer.expectTypeSkipSpaces(TokenLibrary.VAR_KEYWORD)
        lexer.expectTypeSkipSpaces(TokenLibrary.IDENTIFIER, "a?")
        lexer.expectTypeSkipSpaces(TokenLibrary.EQ)
        lexer.expectTypeSkipSpaces(TokenLibrary.INTEGER_LITERAL)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)

        lexer.expectTypeSkipSpaces(TokenLibrary.FUNC_KEYWORD)
        lexer.expectTypeSkipSpaces(TokenLibrary.IDENTIFIER, "hello?")
        lexer.expectTypeSkipSpaces(TokenLibrary.LPAR)
        lexer.expectTypeSkipSpaces(TokenLibrary.RPAR)
        lexer.expectTypeSkipSpaces(TokenLibrary.COLON)
        lexer.expectTypeSkipSpaces(TokenLibrary.LINE_BREAK)

        lexer.expectTypeSkipSpaces(TokenLibrary.INDENT)
        lexer.expectTypeSkipSpaces(TokenLibrary.PASS_KEYWORD)
    }
}
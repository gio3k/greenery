package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDTokens
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Basic Structure")
class BasicTests {
    @Test
    @DisplayName("Basic variable, no param function into pass")
    fun basicStructureForKindScanning() {
        val lexer = createTestLexer("var a = 3\nfunc hello():\n    pass")

        lexer.expectTypeSkipSpaces(GDTokens.VAR_KEYWORD)
        lexer.expectTypeSkipSpaces(GDTokens.IDENTIFIER, "a?")
        lexer.expectTypeSkipSpaces(GDTokens.EQ)
        lexer.expectTypeSkipSpaces(GDTokens.INTEGER_LITERAL)
        lexer.expectTypeSkipSpaces(GDTokens.LINE_BREAK)

        lexer.expectTypeSkipSpaces(GDTokens.FUNC_KEYWORD)
        lexer.expectTypeSkipSpaces(GDTokens.IDENTIFIER, "hello?")
        lexer.expectTypeSkipSpaces(GDTokens.LPAR)
        lexer.expectTypeSkipSpaces(GDTokens.RPAR)
        lexer.expectTypeSkipSpaces(GDTokens.COLON)
        lexer.expectTypeSkipSpaces(GDTokens.LINE_BREAK)

        lexer.expectTypeSkipSpaces(GDTokens.INDENT)
        lexer.expectTypeSkipSpaces(GDTokens.PASS_KEYWORD)
    }
}
package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDTokens
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Language Features")
class LanguageFeatureTests {
    @Test
    @DisplayName("Function with type hint into pass")
    fun scanFunctionWithTypeHint() {
        val lexer = createTestLexer("func a() -> String:\n\tpass")

        lexer.expectSkipSpaces(GDTokens.FUNC_KEYWORD, 0, 3)
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 5)
        lexer.expectSkipSpaces(GDTokens.LPAR, 6)
        lexer.expectSkipSpaces(GDTokens.RPAR, 7)
        lexer.expectSkipSpaces(GDTokens.RARROW, 9, 10)
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 12, 17, "String?")
        lexer.expectSkipSpaces(GDTokens.COLON, 18)
        lexer.expectSkipSpaces(GDTokens.LINE_BREAK, 19)

        lexer.expectSkipSpaces(GDTokens.INDENT, 20)
        lexer.expectSkipSpaces(GDTokens.PASS_KEYWORD, 21, 24)
    }

    @Test
    @DisplayName("Variable with type hint and default")
    fun scanVariableWithTypeHintAndDefault() {
        val lexer = createTestLexer("var a: String = \"abc\"")

        lexer.expectSkipSpaces(GDTokens.VAR_KEYWORD, 0, 2)
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 4, 4, "a?")
        lexer.expectSkipSpaces(GDTokens.COLON, 5)
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 7, 12, "String?")
        lexer.expectSkipSpaces(GDTokens.EQ, 14)
        lexer.expectSkipSpaces(GDTokens.SINGLE_STRING_MARKER, 16)
        lexer.expectSkipSpaces(GDTokens.STRING_CONTENT_PART, 17, 19)
        lexer.expectSkipSpaces(GDTokens.SINGLE_STRING_MARKER, 20)
    }

    @Test
    @DisplayName("Basic enum with 3 values")
    fun scanEnumWith3Values() {
        val lexer = createTestLexer("enum Test{A, B,C}")

        lexer.expectSkipSpaces(GDTokens.ENUM_KEYWORD, 0, 3)
        lexer.expectTypeSkipSpaces(GDTokens.IDENTIFIER, "Test?")
        lexer.expectTypeSkipSpaces(GDTokens.LBRACE)
        lexer.expectTypeSkipSpaces(GDTokens.IDENTIFIER, "A?")
        lexer.expectTypeSkipSpaces(GDTokens.COMMA)
        lexer.expectTypeSkipSpaces(GDTokens.IDENTIFIER, "B?")
        lexer.expectTypeSkipSpaces(GDTokens.COMMA)
        lexer.expectTypeSkipSpaces(GDTokens.IDENTIFIER, "C?")
        lexer.expectSkipSpaces(GDTokens.RBRACE, 16)
    }
}
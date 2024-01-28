package gio.blue.greenery.gdscript.lexer

import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Language Features")
class LanguageFeatureTests {
    @Test
    @DisplayName("Function with type hint into pass")
    fun scanFunctionWithTypeHint() {
        val lexer = createTestLexer("func a() -> String:\n\tpass")

        lexer.expectSkipSpaces(TokenLibrary.FUNC_KEYWORD, 0, 3)
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 5)
        lexer.expectSkipSpaces(TokenLibrary.LPAR, 6)
        lexer.expectSkipSpaces(TokenLibrary.RPAR, 7)
        lexer.expectSkipSpaces(TokenLibrary.RARROW, 9, 10)
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 12, 17, "String?")
        lexer.expectSkipSpaces(TokenLibrary.COLON, 18)
        lexer.expectSkipSpaces(TokenLibrary.LINE_BREAK, 19)

        lexer.expectSkipSpaces(TokenLibrary.INDENT, 20)
        lexer.expectSkipSpaces(TokenLibrary.PASS_KEYWORD, 21, 24)
    }

    @Test
    @DisplayName("Variable with type hint and default")
    fun scanVariableWithTypeHintAndDefault() {
        val lexer = createTestLexer("var a: String = \"abc\"")

        lexer.expectSkipSpaces(TokenLibrary.VAR_KEYWORD, 0, 2)
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 4, 4, "a?")
        lexer.expectSkipSpaces(TokenLibrary.COLON, 5)
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 7, 12, "String?")
        lexer.expectSkipSpaces(TokenLibrary.EQ, 14)
        lexer.expectSkipSpaces(TokenLibrary.SINGLE_STRING_MARKER, 16)
        lexer.expectSkipSpaces(TokenLibrary.STRING_CONTENT, 17, 19)
        lexer.expectSkipSpaces(TokenLibrary.SINGLE_STRING_MARKER, 20)
    }

    @Test
    @DisplayName("Basic enum with 3 values")
    fun scanEnumWith3Values() {
        val lexer = createTestLexer("enum Test{A, B,C}")

        lexer.expectSkipSpaces(TokenLibrary.ENUM_KEYWORD, 0, 3)
        lexer.expectTypeSkipSpaces(TokenLibrary.IDENTIFIER, "Test?")
        lexer.expectTypeSkipSpaces(TokenLibrary.LBRACE)
        lexer.expectTypeSkipSpaces(TokenLibrary.IDENTIFIER, "A?")
        lexer.expectTypeSkipSpaces(TokenLibrary.COMMA)
        lexer.expectTypeSkipSpaces(TokenLibrary.IDENTIFIER, "B?")
        lexer.expectTypeSkipSpaces(TokenLibrary.COMMA)
        lexer.expectTypeSkipSpaces(TokenLibrary.IDENTIFIER, "C?")
        lexer.expectSkipSpaces(TokenLibrary.RBRACE, 16)
    }
}
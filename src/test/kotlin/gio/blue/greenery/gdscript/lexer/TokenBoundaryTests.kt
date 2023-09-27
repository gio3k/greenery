package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDTokens
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Token Boundaries and Sizing")
class TokenBoundaryTests {
    @Test
    @DisplayName("Solo keyword")
    fun scanSoloKeyword() {
        val lexer = createTestLexer("func")
        lexer.expectSkipSpaces(GDTokens.FUNC_KEYWORD, 0, 3)
    }

    @Test
    @DisplayName("Keyword before spaced identifier")
    fun scanKeywordBeforeSpacedIdentifier() {
        val lexer = createTestLexer("func hello")
        lexer.expect(GDTokens.FUNC_KEYWORD, 0, 3)
        lexer.expect(GDTokens.SPACE, 4)
        lexer.expect(GDTokens.IDENTIFIER, 5, 9, "hello?")
    }

    @Test
    @DisplayName("Solo identifier")
    fun scanIdentifier() {
        val lexer = createTestLexer("hello")
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 0, 4, "hello?")
    }

    @Test
    @DisplayName("Triple identifier")
    fun scanThreeIdentifiers() {
        val lexer = createTestLexer("hello hello hello")
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 0, 4, "hello?")
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 6, 10, "hello?")
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 12, 16, "hello?")
    }

    @Test
    @DisplayName("Solo float")
    fun scanFloat() {
        val lexer = createTestLexer("23.6e2")
        lexer.expect(GDTokens.FLOAT_LITERAL, 0, 5, "23.6e2?")
    }

    @Test
    @DisplayName("Float before spaced keyword")
    fun scanFloatBeforeSpacedKeyword() {
        val lexer = createTestLexer("23.6e2 var")
        lexer.expectSkipSpaces(GDTokens.FLOAT_LITERAL, 0, 5, "23.6e2?")
        lexer.expectSkipSpaces(GDTokens.VAR_KEYWORD, 7, 9)
    }

    @Test
    @DisplayName("Integer before spaced keyword")
    fun scanIntegerBeforeSpacedKeyword() {
        val lexer = createTestLexer("3333 var")
        lexer.expectSkipSpaces(GDTokens.INTEGER_LITERAL, 0, 3, "3333?")
        lexer.expectSkipSpaces(GDTokens.VAR_KEYWORD, 5, 7)
    }

    @Test
    @DisplayName("Indent before keyword")
    fun scanIndentBeforeKeyword() {
        val lexer = createTestLexer("    var")
        lexer.expectSkipSpaces(GDTokens.INDENT, 0, 3)
        lexer.expectSkipSpaces(GDTokens.VAR_KEYWORD, 4, 6)
    }

    @Test
    @DisplayName("Bracket before keyword")
    fun scanBracketBeforeKeyword() {
        val lexer = createTestLexer("(var")
        lexer.expectSkipSpaces(GDTokens.LPAR, 0)
        lexer.expectSkipSpaces(GDTokens.VAR_KEYWORD, 1, 3)
    }

    @Test
    @DisplayName("Bracket before spaced keyword")
    fun scanBracketBeforeSpacedKeyword() {
        val lexer = createTestLexer("( var")

        lexer.expect(GDTokens.LPAR, 0)
        lexer.expect(GDTokens.SPACE, 1)
        lexer.expect(GDTokens.VAR_KEYWORD, 2, 4)
    }

    @Test
    @DisplayName("Targeted left shift before spaced keyword")
    fun scanLeftShiftBeforeSpacedKeyword() {
        val lexer = createTestLexer("<<= var")
        lexer.expect(GDTokens.LTLTEQ, 0, 2)
        lexer.expect(GDTokens.SPACE, 3)
        lexer.expect(GDTokens.VAR_KEYWORD, 4, 6)
    }

    @Test
    @DisplayName("Keyword before spaced keyword")
    fun scanKeywordBeforeSpacedKeyword() {
        val lexer = createTestLexer("var var")
        lexer.expect(GDTokens.VAR_KEYWORD, 0, 2)
        lexer.expect(GDTokens.SPACE, 3)
        lexer.expect(GDTokens.VAR_KEYWORD, 4, 6)
    }

    @Test
    @DisplayName("Annotation before spaced keyword")
    fun scanAnnotationBeforeSpacedKeyword() {
        val lexer = createTestLexer("@export var")
        lexer.expect(GDTokens.ANNOTATION, 0, 6, "@export?")
        lexer.expect(GDTokens.SPACE, 7)
        lexer.expect(GDTokens.VAR_KEYWORD, 8, 10)
    }

    @Test
    @DisplayName("Comment before line break")
    fun scanCommentBeforeSpacedKeyword() {
        val lexer = createTestLexer("# Hello!\nvar")
        lexer.expect(GDTokens.COMMENT, 0, 7, "(comment?) Hello!")
        lexer.expect(GDTokens.LINE_BREAK, 8)
        lexer.expect(GDTokens.VAR_KEYWORD, 9, 11)
    }

    @Test
    @DisplayName("Math (plus) before spaced keyword")
    fun scanPlusBeforeSpacedKeyword() {
        val lexer = createTestLexer("+ var")
        lexer.expect(GDTokens.PLUS, 0, 0)
        lexer.expect(GDTokens.SPACE, 1)
        lexer.expect(GDTokens.VAR_KEYWORD, 2, 4)
    }

    @Test
    @DisplayName("Function with single argument into pass")
    fun scanSuperBasicFunction() {
        val lexer = createTestLexer("func main(a):\n\tpass")

        lexer.expectSkipSpaces(GDTokens.FUNC_KEYWORD, 0, 3)
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 5, 8, "main?")
        lexer.expectSkipSpaces(GDTokens.LPAR, 9)
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 10, 10, "a?")
        lexer.expectSkipSpaces(GDTokens.RPAR, 11)
        lexer.expectSkipSpaces(GDTokens.COLON, 12)
        lexer.expectSkipSpaces(GDTokens.LINE_BREAK, 13)

        lexer.expectSkipSpaces(GDTokens.INDENT, 14)
        lexer.expectSkipSpaces(GDTokens.PASS_KEYWORD, 15, 18)
    }

    @Test
    @DisplayName("Keyword class_name")
    fun scanClassNameKeyword() {
        val lexer = createTestLexer("class_name KotlinNode")

        lexer.expectSkipSpaces(GDTokens.CLASS_NAME_KEYWORD, 0, 9)
        lexer.expectSkipSpaces(GDTokens.IDENTIFIER, 11, 20, "KotlinNode?")
    }

    @Test
    @DisplayName("Multiple character elements in succession")
    fun scanMultiChars() {
        val lexer = createTestLexer("> < = += -= **= +")

        lexer.expectSkipSpaces(GDTokens.GT, 0)
        lexer.expectSkipSpaces(GDTokens.LT, 2)
        lexer.expectSkipSpaces(GDTokens.EQ, 4)
        lexer.expectSkipSpaces(GDTokens.PLUSEQ, 6, 7)
        lexer.expectSkipSpaces(GDTokens.MINUSEQ, 9, 10)
        lexer.expectSkipSpaces(GDTokens.EXPEQ, 12, 14)
        lexer.expectSkipSpaces(GDTokens.PLUS, 16)
    }
}
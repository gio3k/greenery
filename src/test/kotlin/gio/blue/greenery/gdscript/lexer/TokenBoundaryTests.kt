package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.elements.TokenLibrary
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

@DisplayName("Token Boundaries and Sizing")
class TokenBoundaryTests {
    @Test
    @DisplayName("Solo keyword")
    fun scanSoloKeyword() {
        val lexer = createTestLexer("func")
        lexer.expectSkipSpaces(TokenLibrary.FUNC_KEYWORD, 0, 3)
    }

    @Test
    @DisplayName("Keyword before spaced identifier")
    fun scanKeywordBeforeSpacedIdentifier() {
        val lexer = createTestLexer("func hello")
        lexer.expect(TokenLibrary.FUNC_KEYWORD, 0, 3)
        lexer.expect(TokenLibrary.SPACE, 4)
        lexer.expect(TokenLibrary.IDENTIFIER, 5, 9, "hello?")
    }

    @Test
    @DisplayName("Solo identifier")
    fun scanIdentifier() {
        val lexer = createTestLexer("hello")
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 0, 4, "hello?")
    }

    @Test
    @DisplayName("Triple identifier")
    fun scanThreeIdentifiers() {
        val lexer = createTestLexer("hello hello hello")
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 0, 4, "hello?")
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 6, 10, "hello?")
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 12, 16, "hello?")
    }

    @Test
    @DisplayName("Solo float")
    fun scanFloat() {
        val lexer = createTestLexer("23.6e2")
        lexer.expect(TokenLibrary.FLOAT_LITERAL, 0, 5, "23.6e2?")
    }

    @Test
    @DisplayName("Float before spaced keyword")
    fun scanFloatBeforeSpacedKeyword() {
        val lexer = createTestLexer("23.6e2 var")
        lexer.expectSkipSpaces(TokenLibrary.FLOAT_LITERAL, 0, 5, "23.6e2?")
        lexer.expectSkipSpaces(TokenLibrary.VAR_KEYWORD, 7, 9)
    }

    @Test
    @DisplayName("Integer before spaced keyword")
    fun scanIntegerBeforeSpacedKeyword() {
        val lexer = createTestLexer("3333 var")
        lexer.expectSkipSpaces(TokenLibrary.INTEGER_LITERAL, 0, 3, "3333?")
        lexer.expectSkipSpaces(TokenLibrary.VAR_KEYWORD, 5, 7)
    }

    @Test
    @DisplayName("Indent before keyword")
    fun scanIndentBeforeKeyword() {
        val lexer = createTestLexer("    var")
        lexer.expectSkipSpaces(TokenLibrary.INDENT, 0, 3)
        lexer.expectSkipSpaces(TokenLibrary.VAR_KEYWORD, 4, 6)
    }

    @Test
    @DisplayName("Bracket before keyword")
    fun scanBracketBeforeKeyword() {
        val lexer = createTestLexer("(var")
        lexer.expectSkipSpaces(TokenLibrary.LPAR, 0)
        lexer.expectSkipSpaces(TokenLibrary.VAR_KEYWORD, 1, 3)
    }

    @Test
    @DisplayName("Bracket before spaced keyword")
    fun scanBracketBeforeSpacedKeyword() {
        val lexer = createTestLexer("( var")

        lexer.expect(TokenLibrary.LPAR, 0)
        lexer.expect(TokenLibrary.SPACE, 1)
        lexer.expect(TokenLibrary.VAR_KEYWORD, 2, 4)
    }

    @Test
    @DisplayName("Targeted left shift before spaced keyword")
    fun scanLeftShiftBeforeSpacedKeyword() {
        val lexer = createTestLexer("<<= var")
        lexer.expect(TokenLibrary.LTLTEQ, 0, 2)
        lexer.expect(TokenLibrary.SPACE, 3)
        lexer.expect(TokenLibrary.VAR_KEYWORD, 4, 6)
    }

    @Test
    @DisplayName("Keyword before spaced keyword")
    fun scanKeywordBeforeSpacedKeyword() {
        val lexer = createTestLexer("var var")
        lexer.expect(TokenLibrary.VAR_KEYWORD, 0, 2)
        lexer.expect(TokenLibrary.SPACE, 3)
        lexer.expect(TokenLibrary.VAR_KEYWORD, 4, 6)
    }

    @Test
    @DisplayName("Annotation before spaced keyword")
    fun scanAnnotationBeforeSpacedKeyword() {
        val lexer = createTestLexer("@export var")
        lexer.expect(TokenLibrary.ANNOTATION, 0, 6, "@export?")
        lexer.expect(TokenLibrary.SPACE, 7)
        lexer.expect(TokenLibrary.VAR_KEYWORD, 8, 10)
    }

    @Test
    @DisplayName("Comment before line break")
    fun scanCommentBeforeSpacedKeyword() {
        val lexer = createTestLexer("# Hello!\nvar")
        lexer.expect(TokenLibrary.COMMENT, 0, 7, "(comment?) Hello!")
        lexer.expect(TokenLibrary.LINE_BREAK, 8)
        lexer.expect(TokenLibrary.VAR_KEYWORD, 9, 11)
    }

    @Test
    @DisplayName("Math (plus) before spaced keyword")
    fun scanPlusBeforeSpacedKeyword() {
        val lexer = createTestLexer("+ var")
        lexer.expect(TokenLibrary.PLUS, 0, 0)
        lexer.expect(TokenLibrary.SPACE, 1)
        lexer.expect(TokenLibrary.VAR_KEYWORD, 2, 4)
    }

    @Test
    @DisplayName("Function with single argument into pass")
    fun scanSuperBasicFunction() {
        val lexer = createTestLexer("func main(a):\n\tpass")

        lexer.expectSkipSpaces(TokenLibrary.FUNC_KEYWORD, 0, 3)
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 5, 8, "main?")
        lexer.expectSkipSpaces(TokenLibrary.LPAR, 9)
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 10, 10, "a?")
        lexer.expectSkipSpaces(TokenLibrary.RPAR, 11)
        lexer.expectSkipSpaces(TokenLibrary.COLON, 12)
        lexer.expectSkipSpaces(TokenLibrary.LINE_BREAK, 13)

        lexer.expectSkipSpaces(TokenLibrary.INDENT, 14)
        lexer.expectSkipSpaces(TokenLibrary.PASS_KEYWORD, 15, 18)
    }

    @Test
    @DisplayName("Keyword class_name")
    fun scanClassNameKeyword() {
        val lexer = createTestLexer("class_name KotlinNode")

        lexer.expectSkipSpaces(TokenLibrary.CLASS_NAME_KEYWORD, 0, 9)
        lexer.expectSkipSpaces(TokenLibrary.IDENTIFIER, 11, 20, "KotlinNode?")
    }

    @Test
    @DisplayName("Multiple character elements in succession")
    fun scanMultiChars() {
        val lexer = createTestLexer("> < = += -= **= +")

        lexer.expectSkipSpaces(TokenLibrary.GT, 0)
        lexer.expectSkipSpaces(TokenLibrary.LT, 2)
        lexer.expectSkipSpaces(TokenLibrary.EQ, 4)
        lexer.expectSkipSpaces(TokenLibrary.PLUSEQ, 6, 7)
        lexer.expectSkipSpaces(TokenLibrary.MINUSEQ, 9, 10)
        lexer.expectSkipSpaces(TokenLibrary.EXPEQ, 12, 14)
        lexer.expectSkipSpaces(TokenLibrary.PLUS, 16)
    }
}
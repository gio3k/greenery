package gio.blue.greenery.gdscript.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import gio.blue.greenery.gdscript.ElementDescription

object TokenLibrary {
    val INVALID = ElementDescription("INVALID")

    val IDENTIFIER = ElementDescription("IDENTIFIER")
    val LINE_BREAK = ElementDescription("LINE_BREAK")

    // These are probably issues
    val ISSUE_STRAY_CARRIAGE_RETURN = ElementDescription("ISSUE_STRAY_CARRIAGE_RETURN")
    val ISSUE_MIXED_INDENTS = ElementDescription("ISSUE_MIXED_INDENTS")
    val ISSUE_DEDENT_DEPTH_UNEXPECTED = ElementDescription("ISSUE_DEDENT_DEPTH_UNEXPECTED")
    val ISSUE_BAD_CHARACTER = TokenType.BAD_CHARACTER

    val SPACE = ElementDescription("SPACE")
    val TAB = ElementDescription("TAB")
    var WHITESPACE = TokenSet.create(SPACE, TAB)

    // Keywords
    val IF_KEYWORD = ElementDescription("IF_KEYWORD")
    val ELIF_KEYWORD = ElementDescription("ELIF_KEYWORD")
    val ELSE_KEYWORD = ElementDescription("ELSE_KEYWORD")
    val FOR_KEYWORD = ElementDescription("FOR_KEYWORD")
    val WHILE_KEYWORD = ElementDescription("WHILE_KEYWORD")
    val MATCH_KEYWORD = ElementDescription("MATCH_KEYWORD")
    val BREAK_KEYWORD = ElementDescription("BREAK_KEYWORD")
    val CONTINUE_KEYWORD = ElementDescription("CONTINUE_KEYWORD")
    val PASS_KEYWORD = ElementDescription("PASS_KEYWORD")
    val RETURN_KEYWORD = ElementDescription("RETURN_KEYWORD")
    val AS_KEYWORD = ElementDescription("AS_KEYWORD")
    val ASSERT_KEYWORD = ElementDescription("ASSERT_KEYWORD")
    val AWAIT_KEYWORD = ElementDescription("AWAIT_KEYWORD")
    val BREAKPOINT_KEYWORD = ElementDescription("BREAKPOINT_KEYWORD")
    val CLASS_KEYWORD = ElementDescription("CLASS_KEYWORD")
    val CLASS_NAME_KEYWORD = ElementDescription("CLASS_NAME_KEYWORD")
    val CONST_KEYWORD = ElementDescription("CONST_KEYWORD")
    val ENUM_KEYWORD = ElementDescription("ENUM_KEYWORD")
    val EXTENDS_KEYWORD = ElementDescription("EXTENDS_KEYWORD")
    val FUNC_KEYWORD = ElementDescription("FUNC_KEYWORD")
    val SET_KEYWORD = ElementDescription("SET_KEYWORD")
    val GET_KEYWORD = ElementDescription("GET_KEYWORD")
    val IN_KEYWORD = ElementDescription("IN_KEYWORD")
    val IS_KEYWORD = ElementDescription("IS_KEYWORD")
    val NOT_KEYWORD = ElementDescription("NOT_KEYWORD")
    val NAMESPACE_KEYWORD = ElementDescription("NAMESPACE_KEYWORD")
    val PRELOAD_KEYWORD = ElementDescription("PRELOAD_KEYWORD")
    val SELF_KEYWORD = ElementDescription("SELF_KEYWORD")
    val SIGNAL_KEYWORD = ElementDescription("SIGNAL_KEYWORD")
    val STATIC_KEYWORD = ElementDescription("STATIC_KEYWORD")
    val SUPER_KEYWORD = ElementDescription("SUPER_KEYWORD")
    val TRAIT_KEYWORD = ElementDescription("TRAIT_KEYWORD")
    val VAR_KEYWORD = ElementDescription("VAR_KEYWORD")
    val VOID_KEYWORD = ElementDescription("VOID_KEYWORD")
    val YIELD_KEYWORD = ElementDescription("YIELD_KEYWORD")

    // Operators
    val PLUS = ElementDescription("PLUS") // +
    val MINUS = ElementDescription("MINUS") // -
    val MULT = ElementDescription("MULT") // *
    val EXP = ElementDescription("EXP") // **
    val DIV = ElementDescription("DIV") // /
    val PERC = ElementDescription("PERC") // %
    val LTLT = ElementDescription("LTLT") // <<
    val GTGT = ElementDescription("GTGT") // >>
    val AND = ElementDescription("AND") // &
    val OR = ElementDescription("OR") // |
    val XOR = ElementDescription("XOR") // ^
    val TILDE = ElementDescription("TILDE") // ~
    val EXCLAIM = ElementDescription("EXCLAIM") // !
    val LT = ElementDescription("LT") // <
    val GT = ElementDescription("GT") // >
    val LE = ElementDescription("LE") // <=
    val GE = ElementDescription("GE") // >=
    val EQEQ = ElementDescription("EQEQ") // ==
    val NE = ElementDescription("NE") // !=

    // Assignment operators
    val PLUSEQ = ElementDescription("PLUSEQ") // +=
    val MINUSEQ = ElementDescription("MINUSEQ") // -=
    val MULTEQ = ElementDescription("MULTEQ") // *=
    val DIVEQ = ElementDescription("DIVEQ")  // /=
    val PERCEQ = ElementDescription("PERCEQ") // %=
    val ANDEQ = ElementDescription("ANDEQ") // &=
    val OREQ = ElementDescription("OREQ") // |=
    val XOREQ = ElementDescription("XOREQ") // ^=
    val LTLTEQ = ElementDescription("LTLTEQ") // <<=
    val GTGTEQ = ElementDescription("GTGTEQ") // >>=
    val EXPEQ = ElementDescription("EXPEQ") // **=
    val RARROW = ElementDescription("RARROW") // ->
    val COLONEQ = ElementDescription("COLONEQ") // :=

    val UNARY_OPERATORS = TokenSet.create(
        PLUS, MINUS, TILDE
    )

    val BINARY_OPERATORS = TokenSet.create(
        PLUS, MINUS, MULT, EXP, DIV, PERC, LTLT, GTGT, AND, OR, XOR, LT, GT, LE, GE, EQEQ, NE
    )

    val NEGATION_OPERATORS = TokenSet.create(
        NOT_KEYWORD, EXCLAIM
    )

    val TARGETED_OPERATORS = TokenSet.create(
        PLUSEQ,
        MINUSEQ,
        MULTEQ,
        DIVEQ,
        PERCEQ,
        ANDEQ,
        PERCEQ,
        ANDEQ,
        OREQ,
        XOREQ,
        LTLTEQ,
        GTGTEQ,
        EXPEQ,
        RARROW,
        COLONEQ
    )

    // Boolean keywords
    val TRUE_KEYWORD = ElementDescription("TRUE_KEYWORD")
    val FALSE_KEYWORD = ElementDescription("FALSE_KEYWORD");

    // Depth
    val INDENT = ElementDescription("INDENT")
    val DEDENT = ElementDescription("DEDENT")

    // Delimiters
    val LPAR = ElementDescription("LPAR") // (
    val RPAR = ElementDescription("RPAR") // )
    val LBRACKET = ElementDescription("LBRACKET") // [
    val RBRACKET = ElementDescription("RBRACKET") // ]
    val LBRACE = ElementDescription("LBRACE") // {
    val RBRACE = ElementDescription("RBRACE") // }
    val COMMA = ElementDescription("COMMA") // ,
    val COLON = ElementDescription("COLON") // :
    val SEMICOLON = ElementDescription("SEMICOLON") // ;
    val DOLLAR = ElementDescription("DOLLAR") // $
    val QUESTION = ElementDescription("QUESTION") // ?
    val BACKTICK = ElementDescription("BACKTICK") // `
    val EQ = ElementDescription("EQ") // =

    /**
     * Comment:
     * A whole comment, from the starting hash to the end of the line
     */
    val COMMENT = ElementDescription("COMMENT")
    val COMMENTS = TokenSet.create(COMMENT)

    /**
     * Annotation:
     * A whole annotation, from the starting at to the end of the identifier
     */
    val ANNOTATION = ElementDescription("ANNOTATION")

    /* String related */
    // String markers
    val TRIPLE_STRING_MARKER = ElementDescription("TRIPLE_STRING_MARKER") // """
    val SINGLE_STRING_MARKER = ElementDescription("SINGLE_STRING_MARKER") // "
    val SMALL_STRING_MARKER = ElementDescription("SMALL_STRING_MARKER") // '

    // String prefixes (only one allowed at a time)
    val RAW_STRING_PREFIX = ElementDescription("RAW_STRING_PREFIX") // r
    val NODE_PATH_STRING_PREFIX = ElementDescription("NODE_PATH_STRING_PREFIX") // ^
    val STRING_NAME_STRING_PREFIX = ElementDescription("STRING_NAME_STRING_PREFIX") // &

    // String data
    val STRING_CONTENT_PART = ElementDescription("STRING_CONTENT") // *
    val STRING_ESCAPE_PART = ElementDescription("STRING_ESCAPE_PART") // \*
    val ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER =
        ElementDescription("ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER")

    // String token set
    val STRING_ELEMENTS = TokenSet.create(
        TRIPLE_STRING_MARKER,
        SINGLE_STRING_MARKER,
        SMALL_STRING_MARKER,
        RAW_STRING_PREFIX,
        NODE_PATH_STRING_PREFIX,
        STRING_NAME_STRING_PREFIX,
        STRING_CONTENT_PART,
        STRING_ESCAPE_PART,
    )

    // String starters
    val STRING_STARTERS = TokenSet.create(
        TRIPLE_STRING_MARKER,
        SINGLE_STRING_MARKER,
        SMALL_STRING_MARKER,
        RAW_STRING_PREFIX,
        NODE_PATH_STRING_PREFIX,
        STRING_NAME_STRING_PREFIX,
    )

    val STATEMENT_BREAKERS = TokenSet.create(
        LINE_BREAK,
        SEMICOLON,
    )

    val EXPRESSION_BREAKERS = TokenSet.create(
        LINE_BREAK, SEMICOLON, INDENT, RPAR, RBRACE, RBRACKET, COLON, COMMA
    )

    /* Literals */
    val INTEGER_LITERAL = ElementDescription("INTEGER_LITERAL")
    val FLOAT_LITERAL = ElementDescription("FLOAT_LITERAL")
    val BINARY_LITERAL = ElementDescription("BINARY_LITERAL")
    val HEX_LITERAL = ElementDescription("HEX_LITERAL")
    var BOOLEAN_LITERALS = TokenSet.create(TRUE_KEYWORD, FALSE_KEYWORD)
    var NUMERIC_LITERALS = TokenSet.create(INTEGER_LITERAL, FLOAT_LITERAL, BINARY_LITERAL, HEX_LITERAL)
}
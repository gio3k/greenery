package gio.blue.greenery.gdscript

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import gio.blue.greenery.gdscript.psi.GDElementType

object GDTokens {
    val INVALID = GDElementType("INVALID")

    val IDENTIFIER = GDElementType("IDENTIFIER")
    val LINE_BREAK = GDElementType("LINE_BREAK")

    // These are probably issues
    var ISSUE_STRAY_CARRIAGE_RETURN = GDElementType("ISSUE_STRAY_CARRIAGE_RETURN")
    var ISSUE_MIXED_INDENTS = GDElementType("ISSUE_MIXED_INDENTS")
    val ISSUE_BAD_CHARACTER = TokenType.BAD_CHARACTER

    val SPACE = GDElementType("SPACE")
    val TAB = GDElementType("TAB")

    // Keywords
    val IF_KEYWORD = GDElementType("IF_KEYWORD")
    val ELIF_KEYWORD = GDElementType("ELIF_KEYWORD")
    val ELSE_KEYWORD = GDElementType("ELSE_KEYWORD")
    val FOR_KEYWORD = GDElementType("FOR_KEYWORD")
    val WHILE_KEYWORD = GDElementType("WHILE_KEYWORD")
    val MATCH_KEYWORD = GDElementType("MATCH_KEYWORD")
    val BREAK_KEYWORD = GDElementType("BREAK_KEYWORD")
    val CONTINUE_KEYWORD = GDElementType("CONTINUE_KEYWORD")
    val PASS_KEYWORD = GDElementType("PASS_KEYWORD")
    val RETURN_KEYWORD = GDElementType("RETURN_KEYWORD")
    val AS_KEYWORD = GDElementType("AS_KEYWORD")
    val ASSERT_KEYWORD = GDElementType("ASSERT_KEYWORD")
    val AWAIT_KEYWORD = GDElementType("AWAIT_KEYWORD")
    val BREAKPOINT_KEYWORD = GDElementType("BREAKPOINT_KEYWORD")
    val CLASS_KEYWORD = GDElementType("CLASS_KEYWORD")
    val CLASS_NAME_KEYWORD = GDElementType("CLASS_NAME_KEYWORD")
    val CONST_KEYWORD = GDElementType("CONST_KEYWORD")
    val ENUM_KEYWORD = GDElementType("ENUM_KEYWORD")
    val EXTENDS_KEYWORD = GDElementType("EXTENDS_KEYWORD")
    val FUNC_KEYWORD = GDElementType("FUNC_KEYWORD")
    val IN_KEYWORD = GDElementType("IN_KEYWORD")
    val IS_KEYWORD = GDElementType("IS_KEYWORD")
    val NAMESPACE_KEYWORD = GDElementType("NAMESPACE_KEYWORD")
    val PRELOAD_KEYWORD = GDElementType("PRELOAD_KEYWORD")
    val SELF_KEYWORD = GDElementType("SELF_KEYWORD")
    val SIGNAL_KEYWORD = GDElementType("SIGNAL_KEYWORD")
    val STATIC_KEYWORD = GDElementType("STATIC_KEYWORD")
    val SUPER_KEYWORD = GDElementType("SUPER_KEYWORD")
    val TRAIT_KEYWORD = GDElementType("TRAIT_KEYWORD")
    val VAR_KEYWORD = GDElementType("VAR_KEYWORD")
    val VOID_KEYWORD = GDElementType("VOID_KEYWORD")
    val YIELD_KEYWORD = GDElementType("YIELD_KEYWORD")

    // Operators
    val PLUS = GDElementType("PLUS") // +
    val MINUS = GDElementType("MINUS") // -
    val MULT = GDElementType("MULT") // *
    val EXP = GDElementType("EXP") // **
    val DIV = GDElementType("DIV") // /
    val PERC = GDElementType("PERC") // %
    val LTLT = GDElementType("LTLT") // <<
    val GTGT = GDElementType("GTGT") // >>
    val AND = GDElementType("AND") // &
    val OR = GDElementType("OR") // |
    val XOR = GDElementType("XOR") // ^
    val TILDE = GDElementType("TILDE") // ~
    val LT = GDElementType("LT") // <
    val GT = GDElementType("GT") // >
    val LE = GDElementType("LE") // <=
    val GE = GDElementType("GE") // >=
    val EQEQ = GDElementType("EQEQ") // ==
    val NE = GDElementType("NE") // !=

    // Assignment operators
    val PLUSEQ = GDElementType("PLUSEQ") // +=
    val MINUSEQ = GDElementType("MINUSEQ") // -=
    val MULTEQ = GDElementType("MULTEQ") // *=
    val DIVEQ = GDElementType("DIVEQ")  // /=
    val PERCEQ = GDElementType("PERCEQ") // %=
    val ANDEQ = GDElementType("ANDEQ") // &=
    val OREQ = GDElementType("OREQ") // |=
    val XOREQ = GDElementType("XOREQ") // ^=
    val LTLTEQ = GDElementType("LTLTEQ") // <<=
    val GTGTEQ = GDElementType("GTGTEQ") // >>=
    val EXPEQ = GDElementType("EXPEQ") // **=
    val RARROW = GDElementType("RARROW") // ->
    val COLONEQ = GDElementType("COLONEQ") // :=

    // Boolean keywords
    val TRUE_KEYWORD = GDElementType("TRUE_KEYWORD")
    val FALSE_KEYWORD = GDElementType("FALSE_KEYWORD");

    // Depth
    val INDENT = GDElementType("INDENT")
    val DEDENT = GDElementType("DEDENT")

    // Delimiters
    val LPAR = GDElementType("LPAR") // (
    val RPAR = GDElementType("RPAR") // )
    val LBRACKET = GDElementType("LBRACKET") // [
    val RBRACKET = GDElementType("RBRACKET") // ]
    val LBRACE = GDElementType("LBRACE") // {
    val RBRACE = GDElementType("RBRACE") // }
    val COMMA = GDElementType("COMMA") // ,
    val COLON = GDElementType("COLON") // :
    val SEMICOLON = GDElementType("SEMICOLON") // ;
    val DOLLAR = GDElementType("DOLLAR") // $
    val QUESTION = GDElementType("QUESTION") // ?
    val BACKTICK = GDElementType("BACKTICK") // `
    val EQ = GDElementType("EQ") // =

    /**
     * Comment:
     * A whole comment, from the starting hash to the end of the line
     */
    val COMMENT = GDElementType("COMMENT")
    val COMMENTS = TokenSet.create(COMMENT)

    /**
     * Annotation:
     * A whole annotation, from the starting at to the end of the identifier
     */
    val ANNOTATION = GDElementType("ANNOTATION")

    /* String related */
    // String markers
    val TRIPLE_STRING_MARKER = GDElementType("TRIPLE_STRING_MARKER") // """
    val SINGLE_STRING_MARKER = GDElementType("SINGLE_STRING_MARKER") // "
    val SMALL_STRING_MARKER = GDElementType("SMALL_STRING_MARKER") // '

    // String prefixes (only one allowed at a time)
    val RAW_STRING_PREFIX = GDElementType("RAW_STRING_PREFIX") // r
    val NODE_PATH_STRING_PREFIX = GDElementType("NODE_PATH_STRING_PREFIX") // ^
    val STRING_NAME_STRING_PREFIX = GDElementType("STRING_NAME_STRING_PREFIX") // &

    // String data
    val STRING_CONTENT_PART = GDElementType("STRING_CONTENT") // *
    val STRING_ESCAPE_PART = GDElementType("STRING_ESCAPE_PART") // \*
    val ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER =
        GDElementType("ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER")

    // String token set
    val STRING_LITERALS = TokenSet.create(
        TRIPLE_STRING_MARKER,
        SINGLE_STRING_MARKER,
        SMALL_STRING_MARKER,
        RAW_STRING_PREFIX,
        NODE_PATH_STRING_PREFIX,
        STRING_NAME_STRING_PREFIX,
        STRING_CONTENT_PART,
        STRING_ESCAPE_PART
    )

    /* Literals */
    val INTEGER_LITERAL = GDElementType("INTEGER_LITERAL")
    val FLOAT_LITERAL = GDElementType("FLOAT_LITERAL")
    val BINARY_LITERAL = GDElementType("BINARY_LITERAL")
    val HEX_LITERAL = GDElementType("HEX_LITERAL")
    var BOOLEAN_LITERALS = TokenSet.create(TRUE_KEYWORD, FALSE_KEYWORD)
    var NUMERIC_LITERALS = TokenSet.create(INTEGER_LITERAL, FLOAT_LITERAL, BINARY_LITERAL, HEX_LITERAL)
}
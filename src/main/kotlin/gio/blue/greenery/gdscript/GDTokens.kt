package gio.blue.greenery.gdscript

import com.intellij.psi.TokenType
import gio.blue.greenery.stage1.GDElementType

object GDTokens {
    val IDENTIFIER = GDElementType("IDENTIFIER")
    val LINE_BREAK = GDElementType("LINE_BREAK")
    val BAD_CHARACTER = TokenType.BAD_CHARACTER

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

    // Boolean keywords
    val TRUE_KEYWORD = GDElementType("TRUE_KEYWORD")
    var FALSE_KEYWORD = GDElementType("FALSE_KEYWORD");

    // Depth
    val INDENT = GDElementType("INDENT")
    val DEDENT = GDElementType("DEDENT")

    // String related
    var

    // Literals
    var NODE_PATH_LITERAL = GDElementType("NODE_PATH_LITERAL");
    var STRING_NAME_LITERAL = GDElementType("STRING_NAME_LITERAL");
    var INTEGER_LITERAL = GDElementType("INTEGER_LITERAL");
    var FLOAT_LITERAL = GDElementType("FLOAT_LITERAL");
    var NODE_PATH_LITERAL = GDElementType("NODE_PATH_LITERAL");


}
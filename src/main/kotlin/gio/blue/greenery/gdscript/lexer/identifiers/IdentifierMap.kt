package gio.blue.greenery.gdscript.lexer.identifiers

import gio.blue.greenery.gdscript.lexer.TokenLibrary

class IdentifierMap {
    companion object {
        val Value = mapOf(
            "if" to TokenLibrary.IF_KEYWORD,
            "elif" to TokenLibrary.ELIF_KEYWORD,
            "else" to TokenLibrary.ELSE_KEYWORD,
            "for" to TokenLibrary.FOR_KEYWORD,
            "while" to TokenLibrary.WHILE_KEYWORD,
            "match" to TokenLibrary.MATCH_KEYWORD,
            "break" to TokenLibrary.BREAK_KEYWORD,
            "continue" to TokenLibrary.CONTINUE_KEYWORD,
            "pass" to TokenLibrary.PASS_KEYWORD,
            "return" to TokenLibrary.RETURN_KEYWORD,

            "as" to TokenLibrary.AS_KEYWORD,
            // "assert" to TokenLibrary.ASSERT_KEYWORD,
            "await" to TokenLibrary.AWAIT_KEYWORD,
            "breakpoint" to TokenLibrary.BREAKPOINT_KEYWORD,
            "class" to TokenLibrary.CLASS_KEYWORD,
            "class_name" to TokenLibrary.CLASS_NAME_KEYWORD,
            "const" to TokenLibrary.CONST_KEYWORD,
            "enum" to TokenLibrary.ENUM_KEYWORD,
            "extends" to TokenLibrary.EXTENDS_KEYWORD,
            "func" to TokenLibrary.FUNC_KEYWORD,
            "get" to TokenLibrary.GET_KEYWORD,
            "set" to TokenLibrary.SET_KEYWORD,
            "in" to TokenLibrary.IN_KEYWORD,
            "is" to TokenLibrary.IS_KEYWORD,
            "not" to TokenLibrary.NOT_KEYWORD,
            "namespace" to TokenLibrary.NAMESPACE_KEYWORD,
            "preload" to TokenLibrary.PRELOAD_KEYWORD,
            "self" to TokenLibrary.SELF_KEYWORD,
            "signal" to TokenLibrary.SIGNAL_KEYWORD,
            "static" to TokenLibrary.STATIC_KEYWORD,
            "super" to TokenLibrary.SUPER_KEYWORD,
            "trait" to TokenLibrary.TRAIT_KEYWORD,
            "var" to TokenLibrary.VAR_KEYWORD,
            "void" to TokenLibrary.VOID_KEYWORD,
            "yield" to TokenLibrary.YIELD_KEYWORD,

            "true" to TokenLibrary.TRUE_KEYWORD,
            "false" to TokenLibrary.FALSE_KEYWORD
        )
    }
}
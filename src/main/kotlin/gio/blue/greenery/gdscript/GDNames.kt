package gio.blue.greenery.gdscript

object GDNames {
    const val PI = "PI"
    const val TAU = "TAU"
    const val INF = "INF"
    const val NAN = "NAN"

    const val IF = "if"
    const val ELIF = "elif"
    const val ELSE = "else"
    const val FOR = "for"
    const val WHILE = "while"
    const val MATCH = "match"
    const val BREAK = "break"
    const val CONTINUE = "continue"
    const val PASS = "pass"
    const val RETURN = "return"

    const val AS = "as"
    const val ASSERT = "assert"
    const val AWAIT = "await"
    const val BREAKPOINT = "breakpoint"
    const val CLASS = "class"
    const val CLASS_NAME = "class_name"
    const val CONST = "const"
    const val ENUM = "enum"
    const val EXTENDS = "extends"
    const val FUNC = "func"
    const val IN = "in"
    const val IS = "is"
    const val NAMESPACE = "namespace"
    const val PRELOAD = "preload"
    const val SELF = "self"
    const val SIGNAL = "signal"
    const val STATIC = "static"
    const val SUPER = "super"
    const val TRAIT = "trait"
    const val VAR = "var"
    const val VOID = "void"
    const val YIELD = "yield"

    val constants = setOf(
        PI,
        TAU,
        INF,
        NAN
    )

    val keywords = setOf(
        IF,
        ELIF,
        ELSE,
        FOR,
        WHILE,
        MATCH,
        BREAK,
        CONTINUE,
        PASS,
        RETURN,
        AS,
        ASSERT,
        AWAIT,
        BREAKPOINT,
        CLASS,
        CLASS_NAME,
        CONST,
        ENUM,
        EXTENDS,
        FUNC,
        IN,
        IS,
        NAMESPACE,
        PRELOAD,
        SELF,
        SIGNAL,
        STATIC,
        SUPER,
        TRAIT,
        VAR,
        VOID,
        YIELD,
    )
}
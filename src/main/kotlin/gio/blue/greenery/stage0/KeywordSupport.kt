package gio.blue.greenery.stage0

import gio.blue.greenery.stage0.core.TokenKind

val keywordMap = mapOf(
    "if" to TokenKind.If,
    "elif" to TokenKind.ElseIf,
    "else" to TokenKind.Else,
    "for" to TokenKind.For,
    "while" to TokenKind.While,
    "break" to TokenKind.Break,
    "continue" to TokenKind.Continue,
    "pass" to TokenKind.Pass,
    "return" to TokenKind.Return,
    "match" to TokenKind.Match,

    "as" to TokenKind.As,
    "assert" to TokenKind.Assert,
    "await" to TokenKind.Await,
    "breakpoint" to TokenKind.Breakpoint,
    "class" to TokenKind.Class,
    "class_name" to TokenKind.ClassName,
    "const" to TokenKind.Const,
    "enum" to TokenKind.Enum,
    "extends" to TokenKind.Extends,
    "func" to TokenKind.Func,
    "in" to TokenKind.In,
    "is" to TokenKind.Is,
    "namespace" to TokenKind.Namespace,
    "preload" to TokenKind.Preload,
    "self" to TokenKind.Self,
    "signal" to TokenKind.Signal,
    "static" to TokenKind.Static,
    "super" to TokenKind.Super,
    "trait" to TokenKind.Trait,
    "var" to TokenKind.Var,
    "void" to TokenKind.Void,
    "yield" to TokenKind.Yield,
)

fun findKeywordFromString(string: String): TokenKind? = keywordMap[string]


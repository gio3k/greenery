package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDTokens
import gio.blue.greenery.gdscript.language.GDCharacterUtil

val keywordMap = mapOf(
    "if" to GDTokens.IF_KEYWORD,
    "elif" to GDTokens.ELIF_KEYWORD,
    "else" to GDTokens.ELSE_KEYWORD,
    "for" to GDTokens.FOR_KEYWORD,
    "while" to GDTokens.WHILE_KEYWORD,
    "match" to GDTokens.MATCH_KEYWORD,
    "break" to GDTokens.BREAK_KEYWORD,
    "continue" to GDTokens.CONTINUE_KEYWORD,
    "pass" to GDTokens.PASS_KEYWORD,
    "return" to GDTokens.RETURN_KEYWORD,

    "as" to GDTokens.AS_KEYWORD,
    "assert" to GDTokens.ASSERT_KEYWORD,
    "await" to GDTokens.AWAIT_KEYWORD,
    "breakpoint" to GDTokens.BREAKPOINT_KEYWORD,
    "class" to GDTokens.CLASS_KEYWORD,
    "class_name" to GDTokens.CLASS_NAME_KEYWORD,
    "const" to GDTokens.CONST_KEYWORD,
    "enum" to GDTokens.ENUM_KEYWORD,
    "extends" to GDTokens.EXTENDS_KEYWORD,
    "func" to GDTokens.FUNC_KEYWORD,
    "in" to GDTokens.IN_KEYWORD,
    "is" to GDTokens.IS_KEYWORD,
    "namespace" to GDTokens.NAMESPACE_KEYWORD,
    "preload" to GDTokens.PRELOAD_KEYWORD,
    "self" to GDTokens.SELF_KEYWORD,
    "signal" to GDTokens.SIGNAL_KEYWORD,
    "static" to GDTokens.STATIC_KEYWORD,
    "super" to GDTokens.SUPER_KEYWORD,
    "trait" to GDTokens.TRAIT_KEYWORD,
    "var" to GDTokens.VAR_KEYWORD,
    "void" to GDTokens.VOID_KEYWORD,
    "yield" to GDTokens.YIELD_KEYWORD,

    "true" to GDTokens.TRUE_KEYWORD,
    "false" to GDTokens.FALSE_KEYWORD
)

/**
 * Attempts to parse a possible identifier or keyword
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun GDLexer.tryLexingPossibleIdentifier(): Boolean {
    if (!GDCharacterUtil.isValidCharacterForIdentifierStart(getCharAt(0))) return false

    // We actually need to read the identifier to figure out if it's a keyword
    val builder = StringBuilder()
    var endOffset = 0
    for (i in 1..getRemainingBoundarySize()) {
        val ci = tryGetCharAt(i)
        if (ci == null || !GDCharacterUtil.isValidCharacterForIdentifierBody(ci)) break
        endOffset = i
        builder.append(ci)
    }

    // Build string
    val string = builder.toString()

    // See if it's a keyword
    keywordMap[string]?.let {
        // It's a keyword, return it
        enqueue(it, 0, endOffset)
        return true
    }

    // Return it as an identifier
    enqueue(GDTokens.IDENTIFIER, 0, endOffset)
    return true
}
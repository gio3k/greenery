package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Attempts to parse a string prefix
 * @receiver TokenLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.parseStringPrefix(): Boolean {
    if (!hasCharAt(0) || !hasCharAt(1))
        return false

    val isC1ValidMarker = when (getCharAt(1)) {
        '\'', '"' -> true
        else -> false
    }

    if (!isC1ValidMarker)
        return false

    val prefixTokenType = when (getCharAt(0)) {
        'r' -> TokenLibrary.RAW_STRING_PREFIX
        '^' -> TokenLibrary.NODE_PATH_STRING_PREFIX
        '&' -> TokenLibrary.STRING_NAME_STRING_PREFIX
        else -> return false
    }

    enqueue(prefixTokenType)
    return true
}
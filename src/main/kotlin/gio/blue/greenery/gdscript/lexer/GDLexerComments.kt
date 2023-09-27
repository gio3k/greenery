package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDTokens

/**
 * Checks if a character can end a comment token
 * @param c Char
 * @return Boolean Whether the provided character can end the token
 */
private fun canCharEndCommentToken(c: Char): Boolean {
    return when (c) {
        '\r', '\n' -> true
        else -> false
    }
}

/**
 * Attempts to parse a comment line
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun GDLexer.tryLexingCommentLine(): Boolean {
    if (getCharAt(0) != '#') return false

    for (i in 1..getRemainingBoundarySize()) {
        val ci = tryGetCharAt(i)
        if (ci == null || canCharEndCommentToken(ci)) {
            // Whether it's empty (just a #) or a full comment, we need to set and return
            enqueue(GDTokens.COMMENT, 0, i - 1)
            return true
        }
    }

    return false
}
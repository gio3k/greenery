package gio.blue.greenery.gdscript.lexer

/**
 * Checks if a character can end an annotation token
 * @param c Char
 * @return Boolean Whether the provided character can end the token
 */
private fun canCharEndAnnotationToken(c: Char): Boolean {
    return when (c) {
        '\r', '\n', ' ', '(', '{', '[' -> true
        else -> false
    }
}

/**
 * Attempts to parse an annotation line
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.tryLexingAnnotation(): Boolean {
    if (getCharAt(0) != '@') return false

    for (i in 1..getRemainingBoundarySize()) {
        val ci = tryGetCharAt(i)
        if (ci == null || canCharEndAnnotationToken(ci)) {
            // Whether it's an empty or a full annotation, we need to set and return
            enqueue(TokenLibrary.ANNOTATION, 0, i - 1)
            return true
        }
    }

    return false
}
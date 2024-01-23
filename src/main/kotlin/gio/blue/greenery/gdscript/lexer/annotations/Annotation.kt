package gio.blue.greenery.gdscript.lexer.annotations

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Parse an annotation at the current character
 *
 * Starts with @
 *
 * @receiver TokenLexer
 * @return Boolean Whether an annotation was parsed
 */
fun TokenLexer.parseAnnotation(): Boolean {
    if (!hasCharAt(0))
        return false

    if (getCharAt(0) != '@')
        return false

    for (i in 1..getRemainingBoundarySize()) {
        if (!hasCharAt(i) || canCharEndAnnotationToken(getCharAt(i))) {
            // Whether it's an empty or a full annotation, we need to set and return
            enqueue(TokenLibrary.ANNOTATION, size = i)
            return true
        }
    }

    return false
}
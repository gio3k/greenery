package gio.blue.greenery.gdscript.lexer.comments

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Parse a comment starting at the current character
 *
 * Starts with #
 *
 * @receiver TokenLexer
 * @return Boolean Whether a comment was parsed
 */
fun TokenLexer.parseComment(): Boolean {
    if (!hasCharAt(0))
        return false

    if (getCharAt(0) != '#')
        return false

    for (i in 1..getRemainingBoundarySize()) {
        if (!hasCharAt(i) || canCharEndCommentToken(getCharAt(i))) {
            // Whether it's empty (just a #) or a full comment, we need to set and return
            enqueue(TokenLibrary.COMMENT, size = i)
            return true
        }
    }

    return false
}
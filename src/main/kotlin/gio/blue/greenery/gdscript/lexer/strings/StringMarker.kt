package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

internal fun TokenLexer.parseStringMarker(): TokenLexer.QueuedToken? {
    if (!hasCharAt(0))
        return null

    return when (getCharAt(0)) {
        '\'' -> enqueue(TokenLibrary.SMALL_STRING_MARKER)

        '\"' ->
            // Find out if this is a triple or single marker
            if (isTripleStringMarker()) {
                // Triple marker found
                enqueue(TokenLibrary.TRIPLE_STRING_MARKER, size = 3)
            } else {
                // Single marker found
                enqueue(TokenLibrary.SINGLE_STRING_MARKER)
            }

        else -> null
    }
}
package gio.blue.greenery.gdscript.lexer.numbers

import gio.blue.greenery.gdscript.lexer.TokenLexer

internal fun TokenLexer.findNumberEndOffsetAfterPrefix(): Int {
    // This is called *while still lexing the number*, right after the prefix
    // The first character is 0, the 2nd character is b or x
    for (i in 2..getRemainingBoundarySize()) {
        if (!hasCharAt(i))
            return i - 1

        when (getCharAt(i)) {
            '\n', '\t', ' ' -> return i - 1

            else -> {
                // This could be hex or binary, it could also be an unexpected character
                // We can't raise an issue though, let the next stage deal with it
            }
        }
    }
    return 1 // Return the offset of 'b' / 'x'
}
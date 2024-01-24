package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.GDCharacterUtil
import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

internal fun TokenLexer.parseStringContent(): TokenLexer.QueuedToken? {
    var size = 0
    for (i in 0..getRemainingBoundarySize()) {
        when (val cin = tryGetCharAt(i)) {
            '\\' -> {
                // Escape character! We need to parse this (it's a token of its own)
                // Break, so we can create the string content token first
                break
            }

            '\'', '"' -> {
                // String marker of some kind - whether this applies to the string doesn't matter (it's a token of its own)
                // Break, so we can create the string content token first
                break
            }

            null -> {
                // We ran out of characters to read
                // Break
                break
            }

            else -> {
                // Unknown, probably data character
                // Let's make sure it's valid
                if (GDCharacterUtil.isInvisibleTextDirectionControlCharacter(cin)) {
                    // Invisible text direction character
                    // Break
                    break
                }

                // This character is fine!
            }
        }

        size++
    }

    // Don't bother appending empty string content
    if (size == 0) return null
    return enqueue(TokenLibrary.STRING_CONTENT, 0, size)
}

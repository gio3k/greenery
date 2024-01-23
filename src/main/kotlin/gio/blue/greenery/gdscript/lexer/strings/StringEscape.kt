package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary


fun TokenLexer.parseStringEscapeCharacter(): Boolean {
    if (!hasCharAt(0))
        return false

    if (getCharAt(0) != '\\')
        return false

    if (!hasCharAt(1)) {
        enqueue(TokenLibrary.ISSUE_EOF)
        return false
    }

    // Escape characters have three forms
    // Single character: \*
    // Unicode UTF-16 codepoint (X = hex, case-insensitive): \uXXXX
    // Unicode UTF-32 codepoint (X = hex, case-insensitive): \UXXXXXX
    // Two UTF-16 escape codepoints can be combined into a single surrogate pair, we won't worry about that for now
    when (getCharAt(1)) {
        'u' -> {
            // UTF-16
            for (i in 2..5) {
                if (!hasCharAt(i) || !isCharHex(getCharAt(i))) {
                    enqueue(TokenLibrary.ISSUE_INVALID_ESCAPE, size = i + 1)
                    return false
                }
            }
            enqueue(TokenLibrary.STRING_ESCAPE_UNICODE_16, size = 6)
            return true
        }

        'U' -> {
            // UTF-32
            for (i in 2..7) {
                if (!hasCharAt(i) || !isCharHex(getCharAt(i))) {
                    enqueue(TokenLibrary.ISSUE_INVALID_ESCAPE, size = i + 1)
                    return false
                }
            }
            enqueue(TokenLibrary.STRING_ESCAPE_UNICODE_32, size = 8)
            return true
        }

        else -> {
            // Unknown - process as a single character escape
            enqueue(TokenLibrary.STRING_ESCAPE, size = 2)
            return true
        }
    }
}
package gio.blue.greenery.gdscript.lexer.lines

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Parse a line break
 * @receiver TokenLexer
 * @return Boolean Whether a line break was parsed
 */
fun TokenLexer.parseLineBreak(): Boolean {
    if (!hasCharAt(0))
        return false

    when (getCharAt(0)) {
        '\r' -> {
            if (hasCharAt(1) && getCharAt(1) != '\n') {
                // No newline - we just found a carriage return
                enqueue(TokenLibrary.ISSUE_STRAY_CARRIAGE_RETURN)
            } else {
                // We found a full line break
                enqueue(TokenLibrary.LINE_BREAK, 0, 1)
            }
        }

        '\n' -> {
            // We just found a new line token - this counts
            enqueue(TokenLibrary.LINE_BREAK)
        }

        else -> return false
    }

    return true
}
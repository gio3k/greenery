package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDTokens

/**
 * Attempts to parse a new line / line break
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun GDLexer.tryLexingLineBreak(): Boolean {
    when (getCharAt(0)) {
        '\r' -> {
            if (tryGetCharAt(1) != '\n') {
                // No newline - we just found a carriage return
                enqueue(GDTokens.ISSUE_STRAY_CARRIAGE_RETURN)
            } else {
                // We found a full line break
                enqueue(GDTokens.LINE_BREAK, 0, 1)
            }
            return true
        }

        '\n' -> {
            // We just found a new line token - this counts
            enqueue(GDTokens.LINE_BREAK)
            return true
        }

        else -> return false
    }
}
package gio.blue.greenery.stage0

import gio.blue.greenery.stage0.core.*
import gio.blue.greenery.stage0.core.issues.ScanIssueType

fun Tokenizer.setByAbsorbingLineBreak() {
    when (peek()) {
        '\r' -> {
            // We want to skip the carriage return and get to the newline
            // If there isn't a newline there, return an error
            mark()
            next()

            if (peek() != '\n') return bad(raise(marked, ScanIssueType.StrayCarriageReturn))
        }

        '\n' -> {
            mark()
        }
    }

    val result = Token(marked, TokenKind.LineBreak)

    // Advance to the first character of the next line
    next()
    hasAdvancedSinceLineBreak = false

    return ok(result)
}
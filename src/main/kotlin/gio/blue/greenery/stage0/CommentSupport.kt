package gio.blue.greenery.stage0

import gio.blue.greenery.stage0.core.TokenKind

fun Tokenizer.absorbComment() {
    mark()

    var char = peek()
    while (char != null) {
        if (char == '\r' || char == '\n') break

        mark()
        char = next()
    }

    setToken(TokenKind.Comment)
}
package gio.blue.greenery.stage0

import gio.blue.greenery.stage0.core.TokenKind

fun Tokenizer.setByAbsorbingAnnotation() {
    mark()
    absorbIdentifier()
    setToken(TokenKind.Annotation)
}

fun Tokenizer.setByAbsorbingComment() {
    mark()

    var char = peek()
    while (char != null) {
        if (char == '\r' || char == '\n') break

        mark()
        char = next()
    }

    setToken(TokenKind.Comment)
}
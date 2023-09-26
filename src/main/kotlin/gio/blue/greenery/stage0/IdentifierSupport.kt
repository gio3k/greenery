package gio.blue.greenery.stage0

import gio.blue.greenery.stage0.core.TokenKind
import gio.blue.greenery.stage0.helpers.CharUtil

fun Tokenizer.absorbIdentifier() {
    mark()

    var c = peek()
    while (c != null) {
        if (!CharUtil.isValidCharacterForIdentifierBody(c)) break
        mark()
        c = next()
    }
}

fun Tokenizer.absorbIdentifierIntoStringBuilder(builder: StringBuilder = StringBuilder()): StringBuilder {
    mark()

    var c = peek()
    while (c != null) {
        if (!CharUtil.isValidCharacterForIdentifierBody(c)) break
        builder.append(c)
        mark()
        c = next()
    }

    return builder
}

fun Tokenizer.setByAbsorbingIdentifier(builder: StringBuilder = absorbIdentifierIntoStringBuilder()) {
    val string = builder.toString()
    if (string.isEmpty()) next()

    findKeywordFromString(string)?.let {
        setToken(it)
        return
    }

    setToken(TokenKind.Identifier)
}
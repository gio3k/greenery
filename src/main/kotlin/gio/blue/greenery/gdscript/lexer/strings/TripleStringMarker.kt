package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.lexer.TokenLexer

fun TokenLexer.isTripleStringMarker(): Boolean {
    if (!hasCharAt(0) || !hasCharAt(1) || !hasCharAt(2))
        return false
    if (getCharAt(0) != '"' || getCharAt(1) != '"' || getCharAt(2) != '"')
        return false
    return true
}
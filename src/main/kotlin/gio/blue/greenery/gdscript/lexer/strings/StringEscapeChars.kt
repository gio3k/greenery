package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.lexer.TokenLexer

internal fun TokenLexer.isCharHex(c: Char): Boolean {
    return when (c) {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true
        'a', 'b', 'c', 'd', 'e', 'f' -> true
        'A', 'B', 'C', 'D', 'E', 'F' -> true
        else -> false
    }
}
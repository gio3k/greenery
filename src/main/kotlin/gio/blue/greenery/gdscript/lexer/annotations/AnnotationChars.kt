package gio.blue.greenery.gdscript.lexer.annotations

import gio.blue.greenery.gdscript.lexer.TokenLexer

/**
 * Check if a character can end an annotation
 * @receiver TokenLexer
 * @param c Char Character to check
 * @return Boolean If the character can stop an annotation from being parsed
 */
internal fun TokenLexer.canCharEndAnnotationToken(c: Char): Boolean {
    return when (c) {
        '\r', '\n', ' ', '(', '{', '[' -> true
        else -> false
    }
}
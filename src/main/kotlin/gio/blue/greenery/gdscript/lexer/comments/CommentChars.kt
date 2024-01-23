package gio.blue.greenery.gdscript.lexer.comments

import gio.blue.greenery.gdscript.lexer.TokenLexer

/**
 * Check if a character can end a comment
 * @receiver TokenLexer
 * @param c Char Character to check
 * @return Boolean If the character can stop a comment from being parsed
 */
internal fun TokenLexer.canCharEndCommentToken(c: Char): Boolean {
    return when (c) {
        '\r', '\n' -> true
        else -> false
    }
}
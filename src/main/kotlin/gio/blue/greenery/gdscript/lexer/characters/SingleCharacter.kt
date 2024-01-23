package gio.blue.greenery.gdscript.lexer.characters

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary


/**
 * Attempts to parse a single character token
 * @receiver TokenLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.parseSingleCharacter(): Boolean {
    if (!hasCharAt(0))
        return false

    when (getCharAt(0)) {
        '{' -> enqueue(TokenLibrary.LBRACE)
        '}' -> enqueue(TokenLibrary.RBRACE)
        '[' -> enqueue(TokenLibrary.LBRACKET)
        ']' -> enqueue(TokenLibrary.RBRACKET)
        '(' -> enqueue(TokenLibrary.LPAR)
        ')' -> enqueue(TokenLibrary.RPAR)

        ':' -> enqueue(TokenLibrary.COLON)
        ';' -> enqueue(TokenLibrary.SEMICOLON)
        '$' -> enqueue(TokenLibrary.DOLLAR)
        '`' -> enqueue(TokenLibrary.BACKTICK)
        '?' -> enqueue(TokenLibrary.QUESTION)
        ',' -> enqueue(TokenLibrary.COMMA)
        '.' -> enqueue(TokenLibrary.PERIOD)

        ' ' -> enqueue(TokenLibrary.SPACE)

        '\t' -> enqueue(TokenLibrary.TAB)

        else -> return false
    }

    return true
}
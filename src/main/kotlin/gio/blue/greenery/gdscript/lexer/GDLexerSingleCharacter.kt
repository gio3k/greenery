package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDTokens


/**
 * Attempts to parse a single character token
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun GDLexer.tryLexingSingleCharacter(): Boolean {
    when (getCharAt(0)) {
        '{' -> enqueue(GDTokens.LBRACE)
        '}' -> enqueue(GDTokens.RBRACE)
        '[' -> enqueue(GDTokens.LBRACKET)
        ']' -> enqueue(GDTokens.RBRACKET)
        '(' -> enqueue(GDTokens.LPAR)
        ')' -> enqueue(GDTokens.RPAR)

        ':' -> enqueue(GDTokens.COLON)
        ';' -> enqueue(GDTokens.SEMICOLON)
        '$' -> enqueue(GDTokens.DOLLAR)
        '`' -> enqueue(GDTokens.BACKTICK)
        '?' -> enqueue(GDTokens.QUESTION)
        ',' -> enqueue(GDTokens.COMMA)

        // Space wasn't handled anywhere else
        ' ' -> enqueue(GDTokens.SPACE)

        // Tab wasn't handled anywhere else
        '\t' -> enqueue(GDTokens.TAB)

        else -> return false
    }

    return true
}
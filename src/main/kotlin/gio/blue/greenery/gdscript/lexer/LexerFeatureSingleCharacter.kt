package gio.blue.greenery.gdscript.lexer


/**
 * Attempts to parse a single character token
 * @receiver TokenLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.tryLexingSingleCharacter(): Boolean {
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

        // Space wasn't handled anywhere else
        ' ' -> enqueue(TokenLibrary.SPACE)

        // Tab wasn't handled anywhere else
        '\t' -> enqueue(TokenLibrary.TAB)

        else -> return false
    }

    return true
}
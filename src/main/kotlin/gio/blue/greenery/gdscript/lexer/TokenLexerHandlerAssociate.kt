package gio.blue.greenery.gdscript.lexer

abstract class TokenLexerHandlerAssociate(val lexer: TokenLexer) {
    /**
     * Reset the handler state
     */
    abstract fun reset()
}
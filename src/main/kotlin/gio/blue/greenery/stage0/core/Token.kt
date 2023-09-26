package gio.blue.greenery.stage0.core

open class Token(var start: Int, var end: Int, var kind: TokenKind) {
    override fun toString(): String = "Token ($kind) @ $start - $end"

    companion object {
        fun empty(): Token = Token(0, 0, TokenKind.None)
    }
}
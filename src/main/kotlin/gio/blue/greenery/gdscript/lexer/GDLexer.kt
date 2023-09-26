package gio.blue.greenery.gdscript.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

class GDLexer : LexerBase() {
    // State (marking)
    private var markStart: Int = -1
    private var markEnd: Int = 0

    // Bounds to read
    private var boundsStart: Int = 0
    private var boundsEnd: Int = 0

    // State (token result)
    private var tokenType: IElementType? = null
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0

    private fun reset() {
        markStart = -1
        markEnd = 0

        boundsStart = 0
        boundsEnd = 0
        tokenType = null
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        reset()
    }

    override fun getState(): Int {
        TODO("Not yet implemented")
    }

    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        TODO("Not yet implemented")
    }

    override fun getBufferSequence(): CharSequence {
        TODO("Not yet implemented")
    }

    override fun getBufferEnd(): Int {
        TODO("Not yet implemented")
    }

    internal fun setToken(type: IElementType?, start: Int, end: Int) {
        tokenStart = start
        tokenEnd = end
        tokenType = type
    }

    internal fun setToken(type: IElementType?, position: Int) = setToken(type, position, position)
}
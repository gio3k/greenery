package gio.blue.greenery.stage1

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.stage0.Tokenizer
import gio.blue.kotgdr.stage0.core.CharSequenceView
import gio.blue.kotgdr.stage0.core.EmptyView

class LexerAdaptor : LexerBase() {
    val tokenizer = Tokenizer(EmptyView())
    val startOffset = 0
    val endOffset = 0
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        tokenizer.switch(CharSequenceView(buffer))
        TODO("Not yet implemented")
    }

    override fun getState(): Int {
        TODO("Not yet implemented")
    }

    override fun getTokenType(): IElementType? {
        TODO("Not yet implemented")
    }

    override fun getTokenStart(): Int {
        return startOffset
    }

    override fun getTokenEnd(): Int {
        return endOffset
    }

    override fun advance() {
        tokenizer.scan()
    }

    override fun getBufferSequence(): CharSequence {
        TODO("Not yet implemented")
    }

    override fun getBufferEnd(): Int {
        TODO("Not yet implemented")
    }

}
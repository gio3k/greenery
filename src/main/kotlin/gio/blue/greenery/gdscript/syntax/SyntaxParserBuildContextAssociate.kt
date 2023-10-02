package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType

abstract class SyntaxParserBuildContextAssociate(val parser: SyntaxParser, protected val builder: SyntaxTreeBuilder) {
    val tokenType: IElementType?
        get() = builder.tokenType

    protected fun nextExpect(expectation: IElementType, message: String): Boolean {
        next()
        if (builder.tokenType == expectation) return true
        builder.error(message)
        return false
    }

    protected fun nowExpect(expectation: IElementType, message: String): Boolean {
        if (builder.tokenType == expectation) {
            next()
            return true
        }
        builder.error(message)
        return false
    }

    protected fun next() {
        builder.advanceLexer()
    }

    protected fun mark(): SyntaxTreeBuilder.Marker {
        return builder.mark()
    }
}
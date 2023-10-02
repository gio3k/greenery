package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType

abstract class SyntaxParserBuildContextAssociate(val parser: SyntaxParser, private val builder: SyntaxTreeBuilder) {
    fun nextExpect(expectation: IElementType, message: String): Boolean {
        next()
        if (builder.tokenType == expectation) return true
        builder.error(message)
        return false
    }

    fun nowExpect(expectation: IElementType, message: String): Boolean {
        if (builder.tokenType == expectation) {
            next()
            return true
        }
        builder.error(message)
        return false
    }

    fun next() {
        builder.advanceLexer()
    }
}
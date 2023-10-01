package gio.blue.greenery.gdscript.ast

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import gio.blue.greenery.gdscript.elements.TokenLibrary

class StringLiteralExpressionNode(node: ASTNode) : ASTWrapperPsiElement(node), IStringLiteralExpressionNode {
    private var valueCacheResult: String? = null

    override fun getValue(): Any? {
        TODO("Not yet implemented")
    }

    override fun subtreeChanged() {
        super.subtreeChanged()
        invalidate()
    }

    private fun invalidate() {
        valueCacheResult = null
    }

    private fun getOrCacheValueAsString(): String {
        valueCacheResult?.let { return it }

        val childStringElements = node.getChildren(TokenLibrary.STRING_ELEMENTS)
        TODO("Not yet implemented")
    }
}
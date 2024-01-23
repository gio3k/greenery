package gio.blue.greenery.gdscript.ast.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import gio.blue.greenery.gdscript.ast.StringLiteralExpression
import gio.blue.greenery.gdscript.lexer.TokenLibrary

class StringLiteralExpressionImpl(node: ASTNode) : ASTWrapperPsiElement(node), StringLiteralExpression {
    private var valueCacheResult: String? = null

    override fun getValue(): Any? {
        return getOrCacheValueAsString()
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

        for (child in this.node.getChildren(TokenLibrary.STRING_ELEMENTS)) {

        }
        valueCacheResult = "test"
        return "test"
        //TODO("Not yet implemented")
    }
}
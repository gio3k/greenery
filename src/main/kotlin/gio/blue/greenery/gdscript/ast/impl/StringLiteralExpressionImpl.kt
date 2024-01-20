package gio.blue.greenery.gdscript.ast.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import gio.blue.greenery.gdscript.ast.StringLiteralExpression

class StringLiteralExpressionImpl(node: ASTNode) : ASTWrapperPsiElement(node), StringLiteralExpression {
    private var valueCacheResult: String? = null

    override fun getValue(): Any? {
        return "test"
        // TODO("Not yet implemented")
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

        return "test"
        //TODO("Not yet implemented")
    }
}
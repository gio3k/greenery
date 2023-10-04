package gio.blue.greenery.gdscript

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.GDLanguage
import org.jetbrains.annotations.NonNls


class ElementDescription : IElementType {
    private val migrateToPsiElementFunction: (ASTNode) -> PsiElement

    constructor(debugName: @NonNls String, fn: (ASTNode) -> PsiElement) : super(debugName, GDLanguage) {
        migrateToPsiElementFunction = fn
    }

    constructor(debugName: @NonNls String) : this(debugName, {
        throw IllegalStateException("GDElementType doesn't have migration function (element type ${it.elementType})")
    })

    /**
     * Migrate the provided AST node to become a PSI element
     * @param node ASTNode Node
     * @return PsiElement PSI element
     */
    fun migrateToPsi(node: ASTNode): PsiElement = migrateToPsiElementFunction(node)
}
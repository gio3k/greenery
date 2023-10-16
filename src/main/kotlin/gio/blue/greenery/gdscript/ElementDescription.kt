package gio.blue.greenery.gdscript

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls


class ElementDescription : IElementType {
    private val migrateToPsiElementFunction: (ASTNode) -> PsiElement

    constructor(debugName: @NonNls String, fn: (ASTNode) -> PsiElement) : super(debugName, GDLanguage) {
        migrateToPsiElementFunction = fn
    }

    constructor(debugName: @NonNls String) : this(debugName, {
        println("GDElementType ${it.elementType} doesn't have migration function, creating basic wrapper")
        ASTWrapperPsiElement(it)
    })

    /**
     * Migrate the provided AST node to become a PSI element
     * @param node ASTNode Node
     * @return PsiElement PSI element
     */
    fun migrateToPsi(node: ASTNode): PsiElement = migrateToPsiElementFunction(node)
}
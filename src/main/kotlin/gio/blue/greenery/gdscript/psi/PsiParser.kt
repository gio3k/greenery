package gio.blue.greenery.gdscript.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.LightPsiParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.syntax.SyntaxParser

class PsiParser : SyntaxParser(), PsiParser, LightPsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        parseLight(root, builder)
        return builder.treeBuilt
    }

    override fun parseLight(root: IElementType, builder: PsiBuilder) {
        parse(root, builder)
    }
}
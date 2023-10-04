package gio.blue.greenery.gdscript

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.psi.PsiWrapperSyntaxParser

class GDParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = TokenLexer()
    override fun createParser(project: Project?): PsiParser = PsiWrapperSyntaxParser()

    override fun getFileNodeType(): IFileElementType = GDLanguage.FILE
    override fun getCommentTokens(): TokenSet = TokenLibrary.COMMENTS
    override fun getStringLiteralElements(): TokenSet = TokenLibrary.STRING_ELEMENTS
    override fun getWhitespaceTokens(): TokenSet = TokenLibrary.WHITESPACE

    override fun createElement(node: ASTNode): PsiElement {
        val elementType = node.elementType
        if (elementType is ElementDescription) return elementType.migrateToPsi(node)
        return ASTWrapperPsiElement(node)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = GDFile(viewProvider)
}
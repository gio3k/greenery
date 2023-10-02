package gio.blue.greenery.gdscript

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
import gio.blue.greenery.gdscript.elements.TokenLibrary
import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.psi.GDPsiParser

class GDParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = TokenLexer()
    override fun createParser(project: Project?): PsiParser = GDPsiParser()

    override fun getFileNodeType(): IFileElementType {
        return GDLanguage.FILE
    }

    override fun getCommentTokens(): TokenSet {
        return TokenLibrary.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenLibrary.STRING_ELEMENTS
    }

    override fun getWhitespaceTokens(): TokenSet {
        return TokenLibrary.WHITESPACE
    }

    override fun createElement(node: ASTNode): PsiElement {
        println("creating psi element for $node")
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        println("createFile!")
        return GDFile(viewProvider)
    }
}
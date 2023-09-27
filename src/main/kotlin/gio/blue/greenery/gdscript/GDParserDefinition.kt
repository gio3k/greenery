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
import gio.blue.greenery.gdscript.lexer.GDLexer
import gio.blue.greenery.gdscript.psi.GDFile

class GDParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer {
        return GDLexer()
    }

    override fun createParser(project: Project?): PsiParser {
        TODO("Not yet implemented")
    }

    override fun getFileNodeType(): IFileElementType {
        TODO("Not yet implemented")
    }

    override fun getCommentTokens(): TokenSet {
        return GDTokens.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return GDTokens.STRING_LITERALS
    }

    override fun createElement(node: ASTNode?): PsiElement {
        TODO("Not yet implemented")
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = GDFile(viewProvider)
}
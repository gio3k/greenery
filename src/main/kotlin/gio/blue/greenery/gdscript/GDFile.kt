package gio.blue.greenery.gdscript

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class GDFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GDLanguage) {
    override fun getFileType(): FileType = GDFileType
}
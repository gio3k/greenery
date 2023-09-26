package gio.blue.greenery.stage1.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import gio.blue.greenery.stage1.GDFileType
import gio.blue.greenery.stage1.GDLanguage

class GDFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GDLanguage) {
    override fun getFileType(): FileType = GDFileType
}
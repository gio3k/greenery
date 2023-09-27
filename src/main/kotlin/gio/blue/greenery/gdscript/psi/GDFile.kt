package gio.blue.greenery.gdscript.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import gio.blue.greenery.gdscript.GDFileType
import gio.blue.greenery.gdscript.GDLanguage

class GDFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GDLanguage) {
    override fun getFileType(): FileType = GDFileType
}
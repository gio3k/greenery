package gio.blue.greenery.gdscript

import com.intellij.lang.Language
import com.intellij.psi.tree.IFileElementType

object GDLanguage : Language("GDScript") {
    val FILE = IFileElementType(this)
}
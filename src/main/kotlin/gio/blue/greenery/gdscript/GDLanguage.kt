package gio.blue.greenery.gdscript

import com.intellij.lang.Language

object GDLanguage : Language("GDScript") {
    private fun readResolve(): Any = GDLanguage
}
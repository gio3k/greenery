package gio.blue.greenery.stage1

import com.intellij.lang.Language

object GDLanguage : Language("GDScript") {
    private fun readResolve(): Any = GDLanguage
}


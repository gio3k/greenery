package gio.blue.greenery.gdscript.syntax

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey


object GDSyntaxBundle : DynamicBundle("messages.GDSyntaxBundle") {
    internal const val BUNDLE = "messages.GDSyntaxBundle"

    @Nls
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: String): String =
        getMessage(key, *params)
}
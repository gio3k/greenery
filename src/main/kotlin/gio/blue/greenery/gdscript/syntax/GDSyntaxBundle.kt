package gio.blue.greenery.gdscript.syntax

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey


object GDSyntaxBundle : DynamicBundle("messages.GDSyntaxBundle") {
    private const val BUNDLE = "messages.GDSyntaxBundle"

    @Nls
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String =
        getMessage(key, params)
}
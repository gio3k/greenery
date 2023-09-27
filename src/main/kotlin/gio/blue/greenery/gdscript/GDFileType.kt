package gio.blue.greenery.gdscript

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object GDFileType : LanguageFileType(GDLanguage) {
    override fun getName(): String = "GDScript File"
    override fun getDescription(): String = "Script file for the Godot game engine"
    override fun getDefaultExtension(): String = "gd"
    override fun getIcon(): Icon = Resources.FILE_ICON
}

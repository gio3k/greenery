package gio.blue.greenery.gdscript.psi

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.GDLanguage
import org.jetbrains.annotations.NonNls

class GDElementType(debugName: @NonNls String) : IElementType(debugName, GDLanguage)
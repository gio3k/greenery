package gio.blue.greenery.gdscript.psi

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.stage1.GDLanguage
import org.jetbrains.annotations.NonNls

class GDElementType(debugName: @NonNls String) : IElementType(debugName, GDLanguage)
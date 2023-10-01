package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType

/**
 * Syntax parser
 *
 * @constructor Create empty Syntax parser
 */
open class SyntaxParser {
    fun parse(root: IElementType, builder: SyntaxTreeBuilder) {
        val rootMarker = builder.mark()
    }
}
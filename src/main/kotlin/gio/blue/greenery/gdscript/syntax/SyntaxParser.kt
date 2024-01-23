package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.syntax.blocks.parseTopLevelBlock


/**
 * Syntax parser
 *
 * @constructor Create empty Syntax parser
 */
open class SyntaxParser {
    fun parse(root: IElementType, builder: SyntaxTreeBuilder) {
        val context = createContext(builder)
        val rootMarker = builder.mark()

        builder.setDebugMode(true)

        while (!builder.eof()) {
            context.blocks.parseTopLevelBlock()
        }

        rootMarker.done(root)
    }

    private fun createContext(builder: SyntaxTreeBuilder): SyntaxParserBuildContext =
        SyntaxParserBuildContext(this, builder)
}


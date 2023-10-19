package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

abstract class SyntaxParserBuildContextAssociate(
    val context: SyntaxParserBuildContext,
    protected val builder: SyntaxTreeBuilder
) {
    val tokenType: IElementType?
        get() = builder.tokenType

    /**
     * Skip all elements of provided type
     * @param et1 IElementType
     */
    protected fun skip(et1: IElementType) {
        while (tokenType == et1) {
            next()
        }
    }

    protected fun skipSet(ets: TokenSet) {
        while (ets.contains(tokenType)) {
            next()
        }
    }

    protected fun next() {
        builder.advanceLexer()
    }

    protected fun saveThenNext(): IElementType? {
        val save = tokenType
        next()
        return save
    }

    protected fun markSingleHere(type: IElementType) {
        val marker = mark()
        next()
        marker.done(type)
    }

    protected fun mark(): SyntaxTreeBuilder.Marker {
        return builder.mark()
    }

    protected fun assertType(et: IElementType) {
        if (tokenType != et) {
            throw AssertionError("Syntax assertion failed: $tokenType != expected $et")
        }
    }

    protected fun assertSet(ets: TokenSet) {
        if (ets.contains(tokenType)) {
            throw AssertionError("Syntax assertion failed: $tokenType not in expected set $ets")
        }
    }

    @Nls
    fun message(@PropertyKey(resourceBundle = GDSyntaxBundle.BUNDLE) key: String, vararg params: String): String =
        GDSyntaxBundle.getMessage(key, *params)
}
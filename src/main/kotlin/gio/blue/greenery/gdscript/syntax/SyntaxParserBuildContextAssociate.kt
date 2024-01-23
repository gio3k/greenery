package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

abstract class SyntaxParserBuildContextAssociate(
    val context: SyntaxParserBuildContext,
    protected val builder: SyntaxTreeBuilder
) {
    val tokenType: IElementType?
        get() = builder.tokenType

    @OptIn(ExperimentalContracts::class)
    inline fun want(block: () -> Boolean, onFail: (IElementType?) -> Any) {
        contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
        val foundTokenType = tokenType
        if (!block()) {
            next()
            onFail(foundTokenType)
        }
    }

    @OptIn(ExperimentalContracts::class)
    inline fun wantThenNext(block: () -> Boolean, onFail: (IElementType?) -> Any) {
        contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
        val foundTokenType = tokenType
        if (!block()) {
            next()
            onFail(foundTokenType)
        }
        next()
    }

    /**
     * Skip all elements of provided type
     * @param et1 IElementType
     */
    internal fun skip(et1: IElementType) {
        while (tokenType == et1) {
            next()
        }
    }

    internal fun skipUntil(et1: IElementType) {
        while (tokenType != null && tokenType != et1) {
            next()
            continue
        }
    }

    internal fun skipSet(ets: TokenSet) {
        while (ets.contains(tokenType)) {
            next()
        }
    }

    fun next() {
        builder.advanceLexer()
    }

    fun mark(): SyntaxTreeBuilder.Marker = builder.mark()

    internal fun assertType(et: IElementType) {
        if (tokenType != et) {
            throw AssertionError("Syntax assertion failed: $tokenType != expected $et")
        }
    }

    internal fun assertSet(ets: TokenSet) {
        if (!ets.contains(tokenType)) {
            throw AssertionError("Syntax assertion failed: $tokenType not in expected set $ets")
        }
    }

    @Nls
    fun message(@PropertyKey(resourceBundle = GDSyntaxBundle.BUNDLE) key: String, vararg params: String): String =
        GDSyntaxBundle.getMessage(key, *params)
}
package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class SyntaxParserBuildContext(val parser: SyntaxParser, builder: SyntaxTreeBuilder) {
    private val scopes = ArrayDeque<SyntaxParserBuildScope>()

    val expressions = ExpressionSyntaxBuildContextParser(this, builder)
    val statements = StatementSyntaxBuildContextParser(this, builder)
    val opex = OpExSyntaxBuildContextParser(this, builder)
    val blocks = BlockSyntaxBuildContextParser(this, builder)

    fun popScope(): SyntaxParserBuildScope = scopes.removeLast()
    fun pushScope(scope: SyntaxParserBuildScope) = scopes.addLast(scope)
    fun peekScope(): SyntaxParserBuildScope = scopes.last()

    @OptIn(ExperimentalContracts::class)
    inline fun withinScope(scope: SyntaxParserBuildScope, block: () -> Unit) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        pushScope(scope)
        block()
        popScope()
    }
}
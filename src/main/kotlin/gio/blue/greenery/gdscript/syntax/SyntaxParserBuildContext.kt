package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.syntax.basic.BlockSyntaxBuildContextParser
import gio.blue.greenery.gdscript.syntax.basic.OperationSyntaxBuildContextParser
import gio.blue.greenery.gdscript.syntax.expressions.ExpressionSyntaxBuildContextParser
import gio.blue.greenery.gdscript.syntax.statements.StatementSyntaxBuildContextParser

class SyntaxParserBuildContext(val parser: SyntaxParser, builder: SyntaxTreeBuilder) {
    private val scopes = ArrayDeque<SyntaxParserBuildScope>()

    val expressions = ExpressionSyntaxBuildContextParser(this, builder)
    val statements = StatementSyntaxBuildContextParser(this, builder)
    val operations = OperationSyntaxBuildContextParser(this, builder)
    val blocks = BlockSyntaxBuildContextParser(this, builder)

    val depth: Int
        get() = scopes.size

    fun popScope(): SyntaxParserBuildScope = scopes.removeLast()
    fun pushScope(scope: SyntaxParserBuildScope) = scopes.addLast(scope)
    fun peekScope(): SyntaxParserBuildScope = scopes.last()

    fun<R> withinScope(scope: SyntaxParserBuildScope, block: () -> R): R {
        pushScope(scope)
        val result = block()
        popScope()
        return result
    }
}
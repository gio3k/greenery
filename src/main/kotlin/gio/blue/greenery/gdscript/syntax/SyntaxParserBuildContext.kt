package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder

class SyntaxParserBuildContext(val parser: SyntaxParser, builder: SyntaxTreeBuilder) {
    private val scopes = ArrayDeque<SyntaxParserBuildScope>()

    val expressions = ExpressionSyntaxBuildContextParser(this, builder)
    val statements = StatementSyntaxBuildContextParser(this, builder)

    fun popScope(): SyntaxParserBuildScope = scopes.removeLast()
    fun pushScope(scope: SyntaxParserBuildScope) = scopes.addLast(scope)
    fun peekScope(): SyntaxParserBuildScope = scopes.last()
}
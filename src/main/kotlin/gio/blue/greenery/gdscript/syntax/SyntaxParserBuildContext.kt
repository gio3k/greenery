package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.SyntaxTreeBuilder

class SyntaxParserBuildContext(parser: SyntaxParser, builder: SyntaxTreeBuilder) {
    val expressions = ExpressionSyntaxBuildContextParser(parser, builder)
    val statements = StatementSyntaxBuildContextParser(parser, builder)
}
package gio.blue.greenery.gdscript.elements

import gio.blue.greenery.gdscript.ast.StringLiteralExpressionNode

object SyntaxLibrary {
    val BINARY_LITERAL_EXPRESSION = ElementDescription("BINARY_LITERAL_EXPRESSION")
    val INTEGER_LITERAL_EXPRESSION = ElementDescription("INTEGER_LITERAL_EXPRESSION")
    val FLOAT_LITERAL_EXPRESSION = ElementDescription("FLOAT_LITERAL_EXPRESSION")
    val HEX_LITERAL_EXPRESSION = ElementDescription("HEX_LITERAL_EXPRESSION")
    val BOOLEAN_LITERAL_EXPRESSION = ElementDescription("BOOLEAN_LITERAL_EXPRESSION")
    val STRING_LITERAL_EXPRESSION = ElementDescription("STRING_LITERAL_EXPRESSION") { StringLiteralExpressionNode(it) }
    val LIST_LITERAL_EXPRESSION = ElementDescription("LIST_LITERAL_EXPRESSION")

    val EXTENDS_STATEMENT = ElementDescription("EXTENDS_STATEMENT")
    val CLASS_NAME_STATEMENT = ElementDescription("CLASS_NAME_STATEMENT")
}
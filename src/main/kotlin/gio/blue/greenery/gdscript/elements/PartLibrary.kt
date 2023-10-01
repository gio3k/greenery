package gio.blue.greenery.gdscript.elements

import gio.blue.greenery.gdscript.ast.StringLiteralExpressionNode

object PartLibrary {
    val STRING_LITERAL_EXPRESSION = ElementDescription("STRING_LITERAL_EXPRESSION") { StringLiteralExpressionNode(it) }
    val LIST_LITERAL_EXPRESSION = ElementDescription("LIST_LITERAL_EXPRESSION")
}
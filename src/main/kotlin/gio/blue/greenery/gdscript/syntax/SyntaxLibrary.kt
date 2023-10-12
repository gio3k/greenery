package gio.blue.greenery.gdscript.syntax

import gio.blue.greenery.gdscript.ElementDescription
import gio.blue.greenery.gdscript.ast.impl.AnnotationStatementImpl
import gio.blue.greenery.gdscript.ast.impl.ClassNameStatementImpl
import gio.blue.greenery.gdscript.ast.impl.ExtendsStatementImpl
import gio.blue.greenery.gdscript.ast.impl.StringLiteralExpressionImpl

object SyntaxLibrary {
    val BINARY_LITERAL_EXPRESSION = ElementDescription("BINARY_LITERAL_EXPRESSION")
    val INTEGER_LITERAL_EXPRESSION = ElementDescription("INTEGER_LITERAL_EXPRESSION")
    val FLOAT_LITERAL_EXPRESSION = ElementDescription("FLOAT_LITERAL_EXPRESSION")
    val HEX_LITERAL_EXPRESSION = ElementDescription("HEX_LITERAL_EXPRESSION")
    val BOOLEAN_LITERAL_EXPRESSION = ElementDescription("BOOLEAN_LITERAL_EXPRESSION")
    val STRING_LITERAL_EXPRESSION = ElementDescription("STRING_LITERAL_EXPRESSION") { StringLiteralExpressionImpl(it) }
    val LIST_LITERAL_EXPRESSION = ElementDescription("LIST_LITERAL_EXPRESSION")

    val DICTIONARY_EXPRESSION = ElementDescription("DICTIONARY_EXPRESSION")
    val DICTIONARY_PAIR_EXPRESSION = ElementDescription("DICTIONARY_PAIR_EXPRESSION")

    val ARGUMENT = ElementDescription("ARGUMENT")
    val ARGUMENT_TYPE_HINT = ElementDescription("ARGUMENT_TYPE_HINT")
    val ARGUMENT_DEFAULT_ASSIGNMENT = ElementDescription("ARGUMENT_DEFAULT_ASSIGNMENT")

    val EXTENDS_STATEMENT = ElementDescription("EXTENDS_STATEMENT") { ExtendsStatementImpl(it) }
    val CLASS_NAME_STATEMENT = ElementDescription("CLASS_NAME_STATEMENT") { ClassNameStatementImpl(it) }
    val ANNOTATION_STATEMENT = ElementDescription("ANNOTATION_STATEMENT") { AnnotationStatementImpl(it) }
}
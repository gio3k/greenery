package gio.blue.greenery.gdscript.syntax

import gio.blue.greenery.gdscript.ElementDescription
import gio.blue.greenery.gdscript.ast.impl.AnnotationStatementImpl
import gio.blue.greenery.gdscript.ast.impl.ClassNameStatementImpl
import gio.blue.greenery.gdscript.ast.impl.ExtendsStatementImpl
import gio.blue.greenery.gdscript.ast.impl.StringLiteralExpressionImpl

object SyntaxLibrary {
    val STATEMENT_GROUP = ElementDescription("GROUP")

    val BINARY_LITERAL = ElementDescription("BINARY_LITERAL")
    val INTEGER_LITERAL = ElementDescription("INTEGER_LITERAL")
    val FLOAT_LITERAL = ElementDescription("FLOAT_LITERAL")
    val HEX_LITERAL = ElementDescription("HEX_LITERAL")
    val BOOLEAN_LITERAL = ElementDescription("BOOLEAN_LITERAL")
    val STRING_LITERAL = ElementDescription("STRING_LITERAL") { StringLiteralExpressionImpl(it) }
    val LIST_LITERAL = ElementDescription("LIST_LITERAL")

    val DICTIONARY = ElementDescription("DICTIONARY")
    val DICTIONARY_PAIR = ElementDescription("DICTIONARY_PAIR")

    val EXPRESSION_PREFIX = ElementDescription("EXPRESSION_PREFIX")
    val BINARY_EXPRESSION = ElementDescription("BINARY_EXPRESSION")

    val PARAMETER_LIST = ElementDescription("PARAMETER_LIST")
    val PARAMETER = ElementDescription("PARAMETER")
    val PARAMETER_TYPE_HINT = ElementDescription("PARAMETER_TYPE_HINT")
    val PARAMETER_DEFAULT_ASSIGNMENT = ElementDescription("PARAMETER_DEFAULT_ASSIGNMENT")

    val EXTENDS_STATEMENT = ElementDescription("EXTENDS_STATEMENT") { ExtendsStatementImpl(it) }
    val CLASS_NAME_STATEMENT = ElementDescription("CLASS_NAME_STATEMENT") { ClassNameStatementImpl(it) }
    val ANNOTATION_STATEMENT = ElementDescription("ANNOTATION_STATEMENT") { AnnotationStatementImpl(it) }
    val FUNCTION_STATEMENT = ElementDescription("FUNCTION_STATEMENT")
    val FOR_STATEMENT = ElementDescription("FOR_STATEMENT")
}
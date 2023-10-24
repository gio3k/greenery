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

    val IDENTIFIER = ElementDescription("IDENTIFIER")

    val DICTIONARY = ElementDescription("DICTIONARY")
    val DICTIONARY_PAIR = ElementDescription("DICTIONARY_PAIR")

    val EXPRESSION_PREFIX = ElementDescription("EXPRESSION_PREFIX")
    val EXPRESSION_LIST = ElementDescription("EXPRESSION_LIST")
    val BINARY_EXPRESSION = ElementDescription("BINARY_EXPRESSION")

    val CONSTANT_DECL_STATEMENT = ElementDescription("CONSTANT_DECL_STATEMENT")
    val VARIABLE_DECL_STATEMENT = ElementDescription("VARIABLE_DECL_STATEMENT")
    val VARIABLE_DECL_PROPERTY_SETTER = ElementDescription("VARIABLE_DECL_PROPERTY_SETTER")
    val VARIABLE_DECL_PROPERTY_GETTER = ElementDescription("VARIABLE_DECL_PROPERTY_GETTER")
    val VARIABLE_DECL_PROPERTY = ElementDescription("VARIABLE_DECL_PROPERTY")
    val VARIABLE_DECL_TYPE_HINT = ElementDescription("VARIABLE_DECL_TYPE_HINT")
    val VARIABLE_DECL_DEFAULT_ASSIGNMENT = ElementDescription("VARIABLE_DECL_DEFAULT_ASSIGNMENT")

    val FUNCTION_DECL_STATEMENT = ElementDescription("FUNCTION_DECL_STATEMENT")
    val FUNCTION_DECL_TYPE_HINT = ElementDescription("FUNCTION_DECL_TYPE_HINT")
    val FUNCTION_DECL_PARAMETER_LIST = ElementDescription("FUNCTION_DECL_PARAMETER_LIST")
    val FUNCTION_DECL_PARAMETER = ElementDescription("FUNCTION_DECL_PARAMETER")
    val FUNCTION_DECL_PARAMETER_TYPE_HINT = ElementDescription("FUNCTION_DECL_PARAMETER_TYPE_HINT")
    val FUNCTION_DECL_PARAMETER_DEFAULT_ASSIGNMENT = ElementDescription("FUNCTION_DECL_PARAMETER_DEFAULT_ASSIGNMENT")

    val EXTENDS_STATEMENT = ElementDescription("EXTENDS_STATEMENT") { ExtendsStatementImpl(it) }
    val CLASS_NAME_STATEMENT = ElementDescription("CLASS_NAME_STATEMENT") { ClassNameStatementImpl(it) }
    val ANNOTATION_STATEMENT = ElementDescription("ANNOTATION_STATEMENT") { AnnotationStatementImpl(it) }

    val IF_STATEMENT = ElementDescription("IF_STATEMENT")
    val IF_PART_IF = ElementDescription("IF_PART_IF")
    val IF_PART_ELSE = ElementDescription("IF_PART_ELSE")
    val IF_PART_ELIF = ElementDescription("IF_PART_ELIF")

    val FOR_STATEMENT = ElementDescription("FOR_STATEMENT")
    val WHILE_STATEMENT = ElementDescription("WHILE_STATEMENT")
}
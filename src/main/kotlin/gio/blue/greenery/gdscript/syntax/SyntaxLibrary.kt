package gio.blue.greenery.gdscript.syntax

import gio.blue.greenery.gdscript.ElementDescription
import gio.blue.greenery.gdscript.ast.impl.AnnotationStatementImpl
import gio.blue.greenery.gdscript.ast.impl.ClassNameStatementImpl
import gio.blue.greenery.gdscript.ast.impl.ExtendsStatementImpl
import gio.blue.greenery.gdscript.ast.impl.StringLiteralExpressionImpl

object SyntaxLibrary {
    // Statement Core
    val STATEMENT = ElementDescription("STATEMENT")
    val STATEMENT_GROUP = ElementDescription("STATEMENT_GROUP")

    // Expression Core
    val EXPRESSION = ElementDescription("EXPRESSION")
    val EXPRESSION_GROUP = ElementDescription("EXPRESSION_GROUP")

    // Literals
    val BINARY_LITERAL = ElementDescription("BINARY_LITERAL")
    val INTEGER_LITERAL = ElementDescription("INTEGER_LITERAL")
    val FLOAT_LITERAL = ElementDescription("FLOAT_LITERAL")
    val HEX_LITERAL = ElementDescription("HEX_LITERAL")
    val BOOLEAN_LITERAL = ElementDescription("BOOLEAN_LITERAL")
    val STRING_LITERAL = ElementDescription("STRING_LITERAL") { StringLiteralExpressionImpl(it) }
    val LIST_LITERAL = ElementDescription("LIST_LITERAL")

    // Expressions
    val IDENTIFIER = ElementDescription("IDENTIFIER")

    val ARRAY_CTOR = ElementDescription("ARRAY_CTOR")

    val DICTIONARY_CTOR = ElementDescription("DICTIONARY_CTOR")
    val DICTIONARY_PAIR = ElementDescription("DICTIONARY_PAIR")

    val NODE_PATH = ElementDescription("NODE_PATH")

    // Expression Prefixes
    // e.g. -target
    val EXPRESSION_PREFIX_UNARY = ElementDescription("EXPRESSION_PREFIX_UNARY")

    // e.g. !target
    val EXPRESSION_PREFIX_NEGATION = ElementDescription("EXPRESSION_PREFIX_NEGATION")

    // Expression Suffixes
    // e.g. lhs[index]
    val EXPRESSION_SUFFIX_INDEXER = ElementDescription("EXPRESSION_SUFFIX_INDEXER")

    // e.g. lhs.rhs
    val EXPRESSION_SUFFIX_MEMBER = ElementDescription("EXPRESSION_SUFFIX_MEMBER")

    // e.g. lhs = rhs
    val EXPRESSION_SUFFIX_ASSIGNMENT = ElementDescription("EXPRESSION_SUFFIX_ASSIGNMENT")

    // e.g.: lhs += rhs
    val EXPRESSION_SUFFIX_MATH_TARGETED = ElementDescription("EXPRESSION_SUFFIX_MATH_TARGETED")

    // e.g.: lhs + rhs
    val EXPRESSION_SUFFIX_MATH_BINOP = ElementDescription("EXPRESSION_SUFFIX_MATH_BINOP")

    // e.g.: lhs == rhs
    val EXPRESSION_SUFFIX_BOOL_BINOP = ElementDescription("EXPRESSION_SUFFIX_BOOL_BINOP")

    // e.g. lhs is rhs
    val EXPRESSION_SUFFIX_BOOL_IS = ElementDescription("EXPRESSION_SUFFIX_IS")

    // e.g. on_true if expr else on_false
    val EXPRESSION_SUFFIX_TERNARY = ElementDescription("EXPRESSION_SUFFIX_TERNARY")

    // Variable Statements
    val CONSTANT_DECL_STATEMENT = ElementDescription("CONSTANT_DECL_STATEMENT")
    val VARIABLE_DECL_STATEMENT = ElementDescription("VARIABLE_DECL_STATEMENT")
    val VARIABLE_DECL_PROPERTY_SETTER = ElementDescription("VARIABLE_DECL_PROPERTY_SETTER")
    val VARIABLE_DECL_PROPERTY_GETTER = ElementDescription("VARIABLE_DECL_PROPERTY_GETTER")
    val VARIABLE_DECL_PROPERTY = ElementDescription("VARIABLE_DECL_PROPERTY")
    val VARIABLE_DECL_TYPE_HINT = ElementDescription("VARIABLE_DECL_TYPE_HINT")
    val VARIABLE_DECL_DEFAULT_ASSIGNMENT = ElementDescription("VARIABLE_DECL_DEFAULT_ASSIGNMENT")

    // Function Statements
    val FUNCTION_DECL_STATEMENT = ElementDescription("FUNCTION_DECL_STATEMENT")
    val FUNCTION_DECL_TYPE_HINT = ElementDescription("FUNCTION_DECL_TYPE_HINT")
    val FUNCTION_DECL_PARAMETER_LIST = ElementDescription("FUNCTION_DECL_PARAMETER_LIST")
    val FUNCTION_DECL_PARAMETER = ElementDescription("FUNCTION_DECL_PARAMETER")
    val FUNCTION_DECL_PARAMETER_TYPE_HINT = ElementDescription("FUNCTION_DECL_PARAMETER_TYPE_HINT")
    val FUNCTION_DECL_PARAMETER_DEFAULT_ASSIGNMENT = ElementDescription("FUNCTION_DECL_PARAMETER_DEFAULT_ASSIGNMENT")

    // Statements
    val EXTENDS_STATEMENT = ElementDescription("EXTENDS_STATEMENT") { ExtendsStatementImpl(it) }
    val CLASS_NAME_STATEMENT = ElementDescription("CLASS_NAME_STATEMENT") { ClassNameStatementImpl(it) }
    val SIGNAL_STATEMENT = ElementDescription("SIGNAL_STATEMENT")
    val ANNOTATION_STATEMENT = ElementDescription("ANNOTATION_STATEMENT") { AnnotationStatementImpl(it) }

    // Logic Statements
    // If Statement
    val IF_STATEMENT = ElementDescription("IF_STATEMENT")
    val IF_PART_IF = ElementDescription("IF_PART_IF")
    val IF_PART_ELSE = ElementDescription("IF_PART_ELSE")
    val IF_PART_ELIF = ElementDescription("IF_PART_ELIF")

    // Loop / Iteration Statements
    val FOR_STATEMENT = ElementDescription("FOR_STATEMENT")
    val WHILE_STATEMENT = ElementDescription("WHILE_STATEMENT")
}
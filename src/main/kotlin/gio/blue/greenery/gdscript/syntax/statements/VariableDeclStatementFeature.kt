package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary

/**
 * Variable declaration
 *
 * (here! var keyword) (name: expression) [(type hint)] [(default value: expression)] [(property decl)]
 */
fun StatementSyntaxBuildContextParser.parseVariableDeclStatement(): Boolean {

}

/**
 * Property declaration
 *
 * (here! colon) _NEWLINE_ [(setter)] [_NEWLINE_ (getter)]
 */
fun StatementSyntaxBuildContextParser.parsePropertyDeclStatement(): Boolean {

}

/**
 * Property getter
 *
 * (here! get keyword) (colon) (block)
 */
private fun StatementSyntaxBuildContextParser.parsePropertyGetter(): Boolean {

}

/**
 * Property setter
 *
 * (here! set keyword) (lpar) (value variable identifier: expression) (rpar) (colon) (block)
 */
private fun StatementSyntaxBuildContextParser.parsePropertyGetter(): Boolean {

}

/**
 * Constant declaration
 *
 * (here! const keyword) (name: expression) [(type hint)] (value: expression)
 */
fun StatementSyntaxBuildContextParser.parseConstantDeclStatement(): Boolean {

}


/**
 * Variable declaration
 *
 * variable: (here! var keyword) (name: expression) [(type hint)] [(default value: expression)]
 * property: (here! colon) _NEWLINE_ [(setter)] [_NEWLINE_ (getter)]
 * constant: (here! const keyword) (name: expression) [(type hint)] (value: expression)
 */
fun StatementSyntaxBuildContextParser.parseVariableDeclStatement(): Boolean {
    assertType(TokenLibrary.VAR_KEYWORD)
    val marker = mark()
    this.next()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        return false
    }

    marker.done(SyntaxLibrary.CLASS_NAME_STATEMENT)
    return true
}
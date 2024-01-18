package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.pars.parseSet

/**
 * Parse an annotation statement starting from the current token
 *
 * (at)_NO SPACE_(here! identifier) [(lpar) (argument list) (rpar)]
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the annotation was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseAnnotation(): Boolean {
    assertType(TokenLibrary.ANNOTATION)
    val marker = mark()
    next()

    if (tokenType != TokenLibrary.LPAR) {
        marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
        return true
    }

    want({ context.expressions.parseSet() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
    return true
}
package gio.blue.greenery.gdscript.syntax.statements

import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.expressions.parseSetExpression

/**
 * Annotation statement
 *
 * (at)_NO SPACE_(here! identifier) [(lpar) (argument list) (rpar)]
 */
fun StatementSyntaxBuildContextParser.parseAnnotationStatement(): Boolean {
    assertType(TokenLibrary.ANNOTATION)
    val marker = mark()
    next()

    if (tokenType != TokenLibrary.LPAR) {
        marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
        return true
    }

    want({ context.expressions.parseSetExpression() }) {
        marker.drop()
        return false
    }

    marker.done(SyntaxLibrary.ANNOTATION_STATEMENT)
    return true
}
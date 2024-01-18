package gio.blue.greenery.gdscript.syntax.statements

/**
 * Parse a single expression starting from the current token as a statement
 *
 * (here! expression)
 *
 * @receiver StatementSyntaxBuildContextParser
 * @return Boolean Whether the expression was fully parsed
 */
fun StatementSyntaxBuildContextParser.parseExpression(): Boolean {
    val marker = mark()

    want({ context.expressions.parse() }) {
        marker.error(message("SYNTAX.generic.expected.expr.got.0", it.toString()))
        return false
    }

    marker.drop()
    return true
}
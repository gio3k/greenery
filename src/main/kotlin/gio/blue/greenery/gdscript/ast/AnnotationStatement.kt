package gio.blue.greenery.gdscript.ast

import gio.blue.greenery.gdscript.ast.base.Expression
import gio.blue.greenery.gdscript.ast.base.Statement

interface AnnotationStatement : Statement {
    fun getExpression(): Expression
    fun getTargetIfExists(): Statement?
}
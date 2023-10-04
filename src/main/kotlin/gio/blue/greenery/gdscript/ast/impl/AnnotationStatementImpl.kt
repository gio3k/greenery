package gio.blue.greenery.gdscript.ast.impl

import com.intellij.lang.ASTNode
import gio.blue.greenery.gdscript.ast.AnnotationStatement
import gio.blue.greenery.gdscript.ast.base.Expression
import gio.blue.greenery.gdscript.ast.base.Statement
import gio.blue.greenery.gdscript.ast.base.impl.ElementImpl

class AnnotationStatementImpl(node: ASTNode) : ElementImpl(node), AnnotationStatement {
    override fun getExpression(): Expression {
        TODO("Not yet implemented")
    }

    override fun getTargetIfExists(): Statement? {
        TODO("Not yet implemented")
    }
}
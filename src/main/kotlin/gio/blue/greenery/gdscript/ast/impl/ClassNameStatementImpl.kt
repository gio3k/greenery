package gio.blue.greenery.gdscript.ast.impl

import com.intellij.lang.ASTNode
import gio.blue.greenery.gdscript.ast.ClassNameStatement
import gio.blue.greenery.gdscript.ast.base.Expression
import gio.blue.greenery.gdscript.ast.base.impl.ElementImpl

class ClassNameStatementImpl(node: ASTNode) : ElementImpl(node), ClassNameStatement {
    override fun getExpression(): Expression {
        TODO("Not yet implemented")
    }
}
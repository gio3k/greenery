package gio.blue.greenery.gdscript.ast.impl

import com.intellij.lang.ASTNode
import gio.blue.greenery.gdscript.ast.ExtendsStatement
import gio.blue.greenery.gdscript.ast.base.Expression
import gio.blue.greenery.gdscript.ast.base.impl.ElementImpl

class ExtendsStatementImpl(node: ASTNode) : ElementImpl(node), ExtendsStatement {
    override fun getExpression(): Expression {
        TODO("Not yet implemented")
    }
}


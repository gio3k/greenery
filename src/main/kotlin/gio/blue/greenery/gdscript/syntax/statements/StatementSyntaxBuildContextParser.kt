package gio.blue.greenery.gdscript.syntax.statements

import com.intellij.lang.SyntaxTreeBuilder
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxLibrary
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContext
import gio.blue.greenery.gdscript.syntax.SyntaxParserBuildContextAssociate
import gio.blue.greenery.gdscript.syntax.statements.func.parseFunctionDeclaration
import gio.blue.greenery.gdscript.syntax.statements.logic.parseIf
import gio.blue.greenery.gdscript.syntax.statements.loops.parseFor
import gio.blue.greenery.gdscript.syntax.statements.loops.parseWhile
import gio.blue.greenery.gdscript.syntax.statements.top_level.parseClassName
import gio.blue.greenery.gdscript.syntax.statements.top_level.parseExtends
import gio.blue.greenery.gdscript.syntax.statements.variables.parseConstantDeclaration
import gio.blue.greenery.gdscript.syntax.statements.variables.parseVariableDeclaration

class StatementSyntaxBuildContextParser(context: SyntaxParserBuildContext, builder: SyntaxTreeBuilder) :
    SyntaxParserBuildContextAssociate(
        context, builder
    ) {

    /**
     * Parse a statement
     */
    fun parse(): Boolean {
        val marker = mark()

        if (parseInnerStatement()) {
            marker.done(SyntaxLibrary.STATEMENT)
            return true
        }

        marker.drop()
        return false
    }

    private fun parseInnerStatement(): Boolean {
        return when (tokenType) {
            TokenLibrary.ANNOTATION -> parseAnnotation()

            TokenLibrary.FUNC_KEYWORD -> parseFunctionDeclaration()
            TokenLibrary.IF_KEYWORD -> parseIf()
            TokenLibrary.PASS_KEYWORD -> parsePass()
            TokenLibrary.FOR_KEYWORD -> parseFor()
            TokenLibrary.WHILE_KEYWORD -> parseWhile()

            TokenLibrary.VAR_KEYWORD -> parseVariableDeclaration()
            TokenLibrary.CONST_KEYWORD -> parseConstantDeclaration()

            TokenLibrary.EXTENDS_KEYWORD -> parseExtends()
            TokenLibrary.CLASS_NAME_KEYWORD -> parseClassName()

            // Unknown token - try parsing as expression
            else -> parseExpression()
        }
    }
}


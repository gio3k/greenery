package gio.blue.greenery.gdscript.ast

import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.PsiLiteralValue
import gio.blue.greenery.gdscript.ast.base.Expression

interface StringLiteralExpression : Expression, PsiLiteralValue, ContributedReferenceHost


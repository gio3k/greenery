package gio.blue.greenery.gdscript.ast

import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.PsiLiteralValue

interface IStringLiteralExpressionNode : IExpressionNode, PsiLiteralValue, ContributedReferenceHost


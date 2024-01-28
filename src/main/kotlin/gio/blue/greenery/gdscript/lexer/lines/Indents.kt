package gio.blue.greenery.gdscript.lexer.lines

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

internal fun TokenLexer.parseIndents(): Boolean {
    var indentSize = 0

    for (i in 0..getRemainingBoundarySize()) {
        if (!hasCharAt(i))
            break

        val indentType = when (getCharAt(i)) {
            ' ' -> DepthIndentType.SPACE
            '\t' -> DepthIndentType.TAB

            else -> break
        }

        indentSize++

        if (knownIndentType == DepthIndentType.NONE)
            knownIndentType = indentType

        if (knownIndentType != indentType) {
            enqueue(TokenLibrary.ISSUE_MIXED_INDENTS, size = i)
            return false
        }
    }

    val lastIndentSize = if (depthStack.empty()) 0 else depthStack.peek()
    if (indentSize == lastIndentSize)
        return false

    // Figure out what token to enqueue
    if (indentSize > lastIndentSize) {
        depthStack.push(indentSize)
        enqueue(TokenLibrary.INDENT, size = indentSize)
        return true
    }

    while (!depthStack.empty()) {
        val newIndentSize = depthStack.peek()
        if (indentSize < newIndentSize) {
            enqueue(TokenLibrary.DEDENT, size = 1, updateBoundary = false)
        }
        depthStack.pop()
    }

    fastForward(indentSize)
    return true
}
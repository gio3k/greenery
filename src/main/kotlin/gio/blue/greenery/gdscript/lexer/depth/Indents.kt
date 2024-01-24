package gio.blue.greenery.gdscript.lexer.depth

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

internal fun TokenLexer.parseIndents(): Boolean {
    var indentSize = 0
    var detectedIndentTypeMismatch = false

    // Iterate to find out what we're looking at
    for (i in 0..getRemainingBoundarySize()) {
        if (!hasCharAt(i)) break

        val indentType = when (getCharAt(i)) {
            '\n', '\r' -> return false

            ' ' -> DepthIndentType.SPACE
            '\t' -> DepthIndentType.TAB
            else -> DepthIndentType.NONE
        }

        if (indentType == DepthIndentType.NONE)
            break

        if (knownIndentType == DepthIndentType.NONE)
            knownIndentType = indentType
        if (knownIndentType != indentType)
            detectedIndentTypeMismatch = true

        indentSize = i + 1
    }

    // Start handling indent size
    if (indentSize == 0) {
        // No indent found, and something was hit on the first character
        if (depthStack.isNotEmpty()) {
            // Queue dedents, without affecting the boundary state.
            // We're over a data character - we really don't want to skip it!
            for (i in 1..depthStack.size)
                enqueue(TokenLibrary.DEDENT, updateBoundary = false)
            depthStack.clear()
            return true
        }
        return false
    }

    // Handle indent mismatch
    if (detectedIndentTypeMismatch) {
        enqueue(TokenLibrary.ISSUE_MIXED_INDENTS, size = indentSize)
        return false
    }

    // Handle the stack / queue accordingly
    if (depthStack.empty()) {
        // Stack empty - add first item and queue
        depthStack.push(indentSize)
        enqueue(TokenLibrary.INDENT, size = indentSize)
        return true
    }

    // Stack non-empty, compare to last indent size
    val lastIndentSize = depthStack.peek()

    if (indentSize == lastIndentSize) {
        // Last indent size was the same indentSize as the current, just return with nothing
        return false
    }

    if (indentSize < lastIndentSize) {
        // Last indent size was larger than the current, return a dedent
        depthStack.pop()

        // Make sure the depth matches up
        if (!depthStack.empty()) {
            if (depthStack.peek() != indentSize) {
                // Depth doesn't match!
                enqueue(TokenLibrary.ISSUE_DEDENT_DEPTH_UNEXPECTED)
                return false
            }
        }

        enqueue(TokenLibrary.DEDENT, size = indentSize)
        return true
    }

    // Last indent size was lesser than the current, return an indent
    depthStack.push(indentSize)
    enqueue(TokenLibrary.INDENT, size = indentSize)
    return true
}
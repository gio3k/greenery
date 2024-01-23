package gio.blue.greenery.gdscript.lexer.depth

import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLexerHandlerAssociate
import gio.blue.greenery.gdscript.lexer.TokenLibrary
import java.util.*

class TokenLexerHandlerDepthAssociate(lexer: TokenLexer) : TokenLexerHandlerAssociate(lexer) {
    private var stack: Stack<Int> = Stack()
    private var knownIndentType = DepthIndentType.NONE
    override fun reset() {
        stack.clear()
        knownIndentType = DepthIndentType.NONE
    }

    fun tryLexingStartOfLineIndents(): Boolean {
        var indentSize = 0
        var detectedIndentTypeMismatch = false

        // Iterate to find out what we're looking at
        for (i in 0..lexer.getRemainingBoundarySize()) {
            if (!lexer.hasCharAt(i)) break

            val indentType = when (lexer.getCharAt(i)) {
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
        val endOffset = indentSize - 1
        if (indentSize == 0) {
            // No indent found, and something was hit on the first character
            if (stack.isNotEmpty()) {
                // Queue dedents, without affecting the boundary state.
                // We're over a data character - we really don't want to skip it!
                lexer.enqueue(TokenLibrary.DEDENT, startOffset = 0, count = stack.size, updateBoundary = false)
                stack.clear()
                return true
            }
            return false
        }

        // Handle indent mismatch
        if (detectedIndentTypeMismatch) {
            lexer.enqueue(TokenLibrary.ISSUE_MIXED_INDENTS, 0, endOffset)
            return false
        }

        // Handle the stack / queue accordingly
        if (stack.empty()) {
            // Stack empty - add first item and queue
            stack.push(indentSize)
            lexer.enqueue(TokenLibrary.INDENT, 0, endOffset)
            return true
        }

        // Stack non-empty, compare to last indent size
        val lastIndentSize = stack.peek()

        if (indentSize == lastIndentSize) {
            // Last indent size was the same indentSize as the current, just return with nothing
            return false
        }

        if (indentSize < lastIndentSize) {
            // Last indent size was larger than the current, return a dedent
            stack.pop()

            // Make sure the depth matches up
            if (!stack.empty()) {
                if (stack.pop() != indentSize) {
                    // Depth doesn't match!
                    lexer.enqueue(TokenLibrary.ISSUE_DEDENT_DEPTH_UNEXPECTED)
                }
            }

            lexer.enqueue(TokenLibrary.DEDENT, 0, endOffset)
            return true
        }

        // Last indent size was lesser than the current, return an indent
        stack.push(indentSize)
        lexer.enqueue(TokenLibrary.INDENT, 0, endOffset)
        return true
    }
}
package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.elements.TokenLibrary
import java.util.*

class GDLexerHandlerDepthAssociate(lexer: GDLexer) : GDLexer.GDLexerHandlerAssociate(lexer) {
    private var stack: Stack<Int> = Stack()
    override fun reset() {
        stack.clear()
    }

    fun tryLexingStartOfLineIndents(): Boolean {
        val isC0Space = when (lexer.tryGetCharAt(0)) {
            '\t' -> false
            ' ' -> true

            '\r', '\n' -> {
                // Just another new line! Return!
                return false
            }

            else -> {
                // Data / non-space was found at the start of the line
                // Reverse all indents, then return
                if (stack.isNotEmpty()) {
                    // Queue dedents, without affecting the boundary state.
                    // We're over a data character - we really don't want to skip it!
                    lexer.enqueue(TokenLibrary.DEDENT, startOffset = 0, count = stack.size, updateBoundary = false)
                    stack.clear()
                    return true
                }
                return false
            }
        }

        // Size / offset of the indent
        var size = 1
        for (i in 1..lexer.getRemainingBoundarySize()) {
            if (!lexer.hasCharAt(i)) break
            when (lexer.getCharAt(i)) {
                '\t' -> {
                    if (isC0Space) {
                        // Error! We used spaces before!
                        lexer.enqueue(TokenLibrary.ISSUE_MIXED_INDENTS)
                    }
                }

                ' ' -> {
                    if (!isC0Space) {
                        // Error! We used tabs before!
                        lexer.enqueue(TokenLibrary.ISSUE_MIXED_INDENTS)
                    }
                }

                else -> break
            }

            size++
        }

        val endOffset = size - 1

        // Handle the stack / queue accordingly
        if (stack.empty()) {
            // Stack empty - add first item and queue
            stack.push(size)
            lexer.enqueue(TokenLibrary.INDENT, 0, endOffset)
            return true
        }

        // Stack non-empty, compare to last indent size
        val lastIndentSize = stack.peek()
        if (size == lastIndentSize) {
            // Last indent size was the same size as the current, just return with nothing
            return false
        }

        if (size < lastIndentSize) {
            // Last indent size was larger than the current, return a dedent
            lexer.enqueue(TokenLibrary.DEDENT, 0, endOffset)
            return true
        }

        // Last indent size was lesser than the current, return an indent
        stack.push(size)
        lexer.enqueue(TokenLibrary.INDENT, 0, endOffset)
        return true
    }
}
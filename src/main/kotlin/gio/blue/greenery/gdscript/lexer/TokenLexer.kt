package gio.blue.greenery.gdscript.lexer

import com.intellij.lexer.LexerBase
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.annotations.parseAnnotation
import gio.blue.greenery.gdscript.lexer.characters.parseMultiCharacter
import gio.blue.greenery.gdscript.lexer.characters.parseSingleCharacter
import gio.blue.greenery.gdscript.lexer.comments.parseComment
import gio.blue.greenery.gdscript.lexer.identifiers.parseIdentifier
import gio.blue.greenery.gdscript.lexer.lines.DepthIndentType
import gio.blue.greenery.gdscript.lexer.lines.parseIndents
import gio.blue.greenery.gdscript.lexer.lines.parseLineBreak
import gio.blue.greenery.gdscript.lexer.numbers.parseNumber
import gio.blue.greenery.gdscript.lexer.strings.parseString
import java.util.*

class TokenLexer : LexerBase() {
    companion object {
        val LOG = Logger.getInstance(TokenLexer::class.java)
    }

    data class QueuedToken(val type: IElementType, val start: Int, val end: Int)

    /** Queue of tokens to return */
    private var queue: ArrayDeque<QueuedToken> = ArrayDeque()
    private var lastToken = QueuedToken(TokenLibrary.INVALID, 0, 0)

    // Bounds to read
    internal var boundsStart = 0
    internal var boundsEnd = 0

    private var state = 0
    private var buffer: CharSequence? = null

    // Depth handling
    internal var depthStack: Stack<Int> = Stack()
    internal var knownIndentType = DepthIndentType.NONE

    private fun reset() {
        boundsStart = 0
        boundsEnd = 0
        lastToken = QueuedToken(TokenLibrary.INVALID, 0, 0)
        queue.clear()

        depthStack.clear()
        knownIndentType = DepthIndentType.NONE
    }

    override fun start(buffer: CharSequence, start: Int, end: Int, initialState: Int) {
        reset()

        boundsStart = start
        boundsEnd = end

        this.buffer = buffer

        state = initialState

        advance()
    }

    override fun getState(): Int = state

    override fun getTokenType(): IElementType? {
        // We need to return null if we have no token!
        return if (lastToken.type == TokenLibrary.INVALID) {
            null
        } else {
            lastToken.type
        }
    }

    override fun getTokenStart(): Int = lastToken.start
    override fun getTokenEnd(): Int = lastToken.end

    override fun getBufferEnd(): Int = boundsEnd
    override fun getBufferSequence(): CharSequence {
        assert(buffer != null)
        return buffer!!
    }

    private fun process() {
        if (tokenType == TokenLibrary.INVALID) {
            fastForward(1)
            return
        }

        if (parseLineBreak()) {
            // We found a line break, handle indents
            parseIndents()
            return
        }

        if (!hasCharAt(0))
            return // No characters to read, give up

        val char = getCharAt(0)
        when (char) {
            '#' -> {
                if (parseComment())
                    return
            }

            '@' -> {
                if (parseAnnotation())
                    return
            }

            '"', '\'', 'r', '^', '&' -> {
                // These characters could be used for a string, but a few have other uses
                // Check for a string, return if we find one
                if (parseString())
                    return
            }
        }

        // Handle numbers
        if (parseNumber())
            return

        // Handle single characters (e.g. ',' '.')
        if (parseSingleCharacter())
            return

        // Handle multi characters (e.g. "*=" "->")
        if (parseMultiCharacter())
            return

        // Handle identifiers and keywords
        if (parseIdentifier())
            return

        // Unknown character at this point
        LOG.warn("Unknown character '${getCharAt(0)}' @ $boundsStart")
        enqueue(TokenLibrary.ISSUE_BAD_CHARACTER)
    }

    override fun advance() {
        // Check if we're out of characters to read
        if (boundsStart >= boundsEnd && queue.isEmpty()) {
            lastToken = QueuedToken(TokenLibrary.INVALID, 0, 0)
            return
        }

        // Check if we can just return from the queue
        if (queue.isNotEmpty()) {
            // Update token info
            lastToken = queue.remove()
            return
        }

        while (hasCharAt(0)) {
            process()

            if (queue.isNotEmpty()) {
                // Update token info
                lastToken = queue.remove()
                return
            }
        }
    }

    internal fun getRemainingBoundarySize(): Int = boundsEnd - boundsStart

    /**
     * Return whether the provided offset is inside the readable bounds
     * @param offset Int Offset added to the start of the boundary
     * @return Boolean
     */
    internal fun hasCharAt(offset: Int): Boolean {
        val i0 = offset + boundsStart
        if (i0 < 0) return false
        if (i0 >= boundsEnd) return false
        return true
    }

    /**
     * Get character at the provided offset
     * @param offset Int Offset added to the start of the boundary
     * @return Char
     */
    internal fun getCharAt(offset: Int): Char {
        val i0 = offset + boundsStart
        return buffer!![i0]
    }

    /**
     * Get character at the provided offset if it exists
     * @param offset Int Offset added to the start of the boundary
     * @return Char? Character or null
     */
    internal fun tryGetCharAt(offset: Int): Char? {
        if (!hasCharAt(offset)) return null
        return getCharAt(offset)
    }

    internal fun enqueue(
        type: IElementType,
        offset: Int = 0,
        size: Int = 1,
        updateBoundary: Boolean = true
    ): QueuedToken {
        val token = QueuedToken(type, boundsStart + offset, boundsStart + offset + (size - 1))
        queue.add(token)

        val sx = StringBuilder()
        if (type == TokenLibrary.LINE_BREAK) {
            sx.append("\\n")
        } else {
            for (i in 0..(size - 1)) {
                if (hasCharAt(i))
                    sx.append(getCharAt(i))
                else
                    sx.append("<NULL>")
            }
        }

        println("${type} @ ${boundsStart + offset} ($sx)")

        if (updateBoundary)
            boundsStart += size

        return token
    }

    internal fun fastForward(offset: Int) {
        boundsStart += offset
    }

    internal fun peek(): QueuedToken? = queue.peekLast()
}
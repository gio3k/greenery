package gio.blue.greenery.gdscript.lexer

import com.intellij.lexer.LexerBase
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.tree.IElementType
import java.util.*

class TokenLexer : LexerBase() {
    companion object {
        val LOG = Logger.getInstance(TokenLexer::class.java)
    }

    data class QueuedToken(val type: IElementType, val start: Int, val end: Int)

    /**
     * Queue of tokens to return
     */
    private var queue: Queue<QueuedToken> = LinkedList()
    private var lastToken = QueuedToken(TokenLibrary.INVALID, 0, 0)

    // Bounds to read
    internal var boundsStart = 0
    internal var boundsEnd = 0

    private var state = 0
    private var buffer: CharSequence? = null

    private val depthHandlerAssociate = TokenLexerHandlerDepthAssociate(this)

    private fun reset() {
        boundsStart = 0
        boundsEnd = 0
        lastToken = QueuedToken(TokenLibrary.INVALID, 0, 0)
        queue.clear()
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
        if (tryLexingLineBreak()) {
            // We found a line break - that means we're at the start of the line!
            depthHandlerAssociate.tryLexingStartOfLineIndents()
            return
        }

        if (tokenType == TokenLibrary.INVALID) {
            // We're the very first token seen by the lexer
            // Try to parse indents, and if it doesn't work out, just continue
            if (depthHandlerAssociate.tryLexingStartOfLineIndents()) return
        }

        if (tryLexingCommentLine()) return
        if (tryLexingAnnotation()) return

        if (tryLexingString()) return
        if (tryLexingNumber()) return

        // Handle single characters (e.g. ',' '.')
        if (tryLexingSingleCharacter()) return

        // Handle multi characters (e.g. "*=" "->")
        if (tryLexingMultiCharacter()) return

        // Handle identifiers and keywords
        if (tryLexingPossibleIdentifier()) return

        // Unknown character at this point
        LOG.warn("Unknown character @ $boundsStart")
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

    /**
     * Add a token / element to the return queue
     * @param type IElementType Token type to queue up
     * @param startOffset Int Start of the token as an offset of the boundary
     * @param endOffset Int End of the token as an offset of the boundary
     * @param count Int Number of tokens to add
     * @param updateBoundary Boolean Whether the boundary should be updated to move past the provided token
     */
    internal fun enqueue(
        type: IElementType,
        startOffset: Int = 0,
        endOffset: Int = startOffset,
        count: Int = 1,
        updateBoundary: Boolean = true
    ) {
        for (i in 1..count) {
            queue.add(
                QueuedToken(type, startOffset + boundsStart, endOffset + boundsStart)
            )
        }

        if (updateBoundary) {
            // Move reading boundary to the next character to read
            boundsStart += endOffset + 1
        }
    }

    internal fun peek(): QueuedToken? = queue.peek()
}
package gio.blue.greenery.gdscript.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.GDTokens
import java.util.*

class GDLexer : LexerBase() {
    abstract class GDLexerHandlerAssociate(val lexer: GDLexer) {
        /**
         * Reset the handler state
         */
        abstract fun reset()
    }

    data class QueuedToken(val type: IElementType, val start: Int, val end: Int)

    /**
     * Queue of tokens to return
     */
    private var queue: Queue<QueuedToken> = LinkedList()
    private var lastToken = QueuedToken(GDTokens.INVALID, 0, 0)

    // Bounds to read
    internal var boundsStart = 0
    internal var boundsEnd = 0

    private var state = 0
    private var buffer: CharSequence? = null

    private val depthHandlerAssociate = GDLexerHandlerDepthAssociate(this)

    private fun reset() {
        boundsStart = 0
        boundsEnd = 0
        lastToken = QueuedToken(GDTokens.INVALID, 0, 0)
        queue.clear()
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        reset()

        boundsStart = startOffset
        boundsEnd = endOffset

        this.buffer = buffer

        state = initialState
    }

    override fun getState(): Int = state

    override fun getTokenType(): IElementType = lastToken.type
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
        println("Unknown character @ $boundsStart")
        enqueue(GDTokens.ISSUE_BAD_CHARACTER)
    }

    override fun advance() {
        // Check if we can just return from the queue
        if (queue.isNotEmpty()) {
            // Update token info
            lastToken = queue.remove()
            return
        }

        while (hasCharAt(0)) {
            println(getCharAt(0))
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

    internal fun enqueue(type: IElementType, startOffset: Int, endOffset: Int) {
        // Move reading boundary to the next character to read
        boundsStart += endOffset + 1

        queue.add(
            QueuedToken(type, startOffset, endOffset)
        )
    }

    internal fun enqueue(type: IElementType, offset: Int = 0) = enqueue(type, offset, offset)
    internal fun peek(): QueuedToken? = queue.peek()
}
package gio.blue.greenery.gdscript.lexer

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.GDTokens
import kotlin.test.assertEquals

/**
 * Skip all spaces and tabs
 * @receiver GDLexer
 */
internal fun GDLexer.skipSpaces() {
    while (tokenType == GDTokens.INVALID || tokenType == GDTokens.SPACE || tokenType == GDTokens.TAB) {
        advance()
        if (getRemainingBoundarySize() <= 0)
            return
    }
}

internal fun GDLexer.assertType(type: IElementType, message: String? = null) {
    assertEquals(type, tokenType, message)
}

internal fun GDLexer.assert(type: IElementType, start: Int, end: Int = start, message: String? = null) {
    assertEquals(type, tokenType, message)
    assertEquals(start, tokenStart, message)
    assertEquals(end, tokenEnd, message)
}

internal fun GDLexer.expectTypeSkipSpaces(type: IElementType, message: String? = null) {
    skipSpaces()
    assertType(type, message)
    advance()
}

internal fun GDLexer.expectSkipSpaces(type: IElementType, start: Int, end: Int = start, message: String? = null) {
    skipSpaces()
    assert(type, start, end, message)
    advance()
}

internal fun GDLexer.expect(type: IElementType, start: Int, end: Int = start, message: String? = null) {
    assert(type, start, end, message)
    advance()
}

internal fun GDLexer.expectType(type: IElementType, message: String? = null) {
    assertType(type, message)
    advance()
}

internal fun createTestLexer(sequence: CharSequence): GDLexer {
    val lexer = GDLexer()
    lexer.start(sequence)
    lexer.advance() // Skip INVALID
    return lexer
}


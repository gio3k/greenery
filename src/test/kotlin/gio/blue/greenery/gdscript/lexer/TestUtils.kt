package gio.blue.greenery.gdscript.lexer

import com.intellij.psi.tree.IElementType
import kotlin.test.assertEquals

/**
 * Skip all spaces and tabs
 * @receiver GDLexer
 */
internal fun TokenLexer.skipSpaces() {
    while (tokenType == TokenLibrary.INVALID || tokenType == TokenLibrary.SPACE || tokenType == TokenLibrary.TAB) {
        advance()
        if (getRemainingBoundarySize() <= 0)
            return
    }
}

internal fun TokenLexer.assertType(type: IElementType, message: String? = null) {
    assertEquals(type, tokenType, message)
}

internal fun TokenLexer.assert(type: IElementType, start: Int, end: Int = start, message: String? = null) {
    assertEquals(type, tokenType, message)
    assertEquals(start, tokenStart, message)
    assertEquals(end, tokenEnd, message)
}

internal fun TokenLexer.expectType(type: IElementType, message: String? = null) {
    assertType(type, message)
    advance()
}

internal fun TokenLexer.expect(type: IElementType, start: Int, end: Int = start, message: String? = null) {
    assert(type, start, end, message)
    advance()
}

internal fun TokenLexer.expectTypeSkipSpaces(type: IElementType, message: String? = null) {
    skipSpaces()
    expectType(type, message)
}

internal fun TokenLexer.expectSkipSpaces(type: IElementType, start: Int, end: Int = start, message: String? = null) {
    skipSpaces()
    expect(type, start, end, message)
}


internal fun createTestLexer(sequence: CharSequence): TokenLexer {
    val lexer = TokenLexer()
    lexer.start(sequence)
    return lexer
}


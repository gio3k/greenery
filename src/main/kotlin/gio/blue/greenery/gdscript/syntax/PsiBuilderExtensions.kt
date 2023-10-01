package gio.blue.greenery.gdscript.syntax

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

/**
 * Advance the lexer and get the next token, error unless it's expected
 * @receiver PsiBuilder
 * @param expectation IElementType Expected token
 * @param message String Message to use as error message
 * @return Boolean Whether the token was expected
 */
fun PsiBuilder.nextExpect(expectation: IElementType, message: String): Boolean {
    advanceLexer()
    if (tokenType == expectation) return true
    error(message)
    return false
}

/**
 * Get the current token, error if it's not expected, advance if it is
 * @receiver PsiBuilder
 * @param expectation IElementType Expected token
 * @param message String Message to use as error message
 * @return Boolean Whether the token was expected
 */
fun PsiBuilder.nowExpect(expectation: IElementType, message: String): Boolean {
    if (tokenType == expectation) {
        advanceLexer()
        return true
    }
    error(message)
    return false
}

/**
 * Advance the lexer
 * @receiver PsiBuilder
 */
fun PsiBuilder.next() {
    advanceLexer()
}
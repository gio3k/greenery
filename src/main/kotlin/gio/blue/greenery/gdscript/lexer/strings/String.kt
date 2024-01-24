package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.lexer.TokenLexer

/**
 * Attempts to parse a string
 * @receiver TokenLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.parseString(): Boolean {
    // Try finding the string prefix first
    parseStringPrefix()

    // Parse the string marker if possible
    // Save the token as well - we need to use it for comparison later
    val marker0 = parseStringMarker() ?: return false

    // Parse the string content if possible until a string marker is found
    while (hasCharAt(0)) {
        // Parse the string marker if possible
        // Save the token as well - we need to compare it to the original marker
        val marker1 = parseStringMarker()
        if (marker1 != null && marker1.start != marker0.start) {
            break
        }

        if (parseStringContent() == null)
            fastForward(1)

        // We stopped reading string content - something stopped us!
        if (parseStringEscapeCharacter())
            continue
        if (parseStringControlCharacter())
            continue
    }

    return true
}
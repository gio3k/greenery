package gio.blue.greenery.gdscript.lexer.identifiers

import gio.blue.greenery.gdscript.GDCharacterUtil
import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

/**
 * Parse an identifier at the current character
 *
 * @receiver TokenLexer
 * @return Boolean Whether an identifier was parsed
 */
fun TokenLexer.parseIdentifier(): Boolean {
    if (!hasCharAt(0))
        return false

    if (!GDCharacterUtil.isValidCharacterForIdentifierStart(getCharAt(0)))
        return false

    // We actually need to read the identifier to figure out if it's a keyword
    val builder = StringBuilder()
    for (i in 0..getRemainingBoundarySize()) {
        if (!hasCharAt(i))
            break
        val ci = getCharAt(i)
        if (!GDCharacterUtil.isValidCharacterForIdentifierBody(ci))
            break
        builder.append(ci)
    }

    // Build string
    val string = builder.toString()

    // See if it's a keyword
    IdentifierMap.Value[string]?.let {
        // It's a keyword, return it
        enqueue(it, size = string.length)
        return true
    }

    // Return it as an identifier
    enqueue(TokenLibrary.IDENTIFIER, size = string.length)
    return true
}
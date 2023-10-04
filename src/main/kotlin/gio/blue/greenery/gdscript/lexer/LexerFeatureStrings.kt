package gio.blue.greenery.gdscript.lexer

import gio.blue.greenery.gdscript.GDCharacterUtil

/**
 * Attempts to parse a string prefix
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
private fun TokenLexer.tryLexingStringPrefix(): Boolean {
    fun isC1StringMarker(): Boolean {
        return when (tryGetCharAt(1)) {
            '\'' -> true
            '"' -> true
            else -> false
        }
    }
    when (getCharAt(0)) {
        'r' -> {
            if (isC1StringMarker()) {
                enqueue(TokenLibrary.RAW_STRING_PREFIX)
                return true
            }
        }

        '^' -> {
            if (isC1StringMarker()) {
                enqueue(TokenLibrary.NODE_PATH_STRING_PREFIX)
                return true
            }
        }

        '&' -> {
            if (isC1StringMarker()) {
                enqueue(TokenLibrary.STRING_NAME_STRING_PREFIX)
                return true
            }
        }
    }

    return false
}

private fun TokenLexer.isTripleStringMarker(): Boolean {
    // We already know the first character is correct, just check the next 2
    if (tryGetCharAt(1) != '"') return false
    if (tryGetCharAt(2) != '"') return false
    return true
}

private fun TokenLexer.tryLexingStringMarker(): Boolean {
    // See if we have a string marker - there could be no character here
    when (tryGetCharAt(0)) {
        '\'' -> {
            enqueue(TokenLibrary.SMALL_STRING_MARKER)
            return true
        }

        '\"' -> {
            // Find out if this is a triple or single marker
            if (isTripleStringMarker()) {
                // Triple marker found
                enqueue(TokenLibrary.TRIPLE_STRING_MARKER, 0, 2)
            } else {
                // Single marker found
                enqueue(TokenLibrary.SINGLE_STRING_MARKER)
            }

            return true
        }
    }
    return false
}

private fun TokenLexer.tryLexingStringContent(): Boolean {
    var size = 0
    for (i in 0..getRemainingBoundarySize()) {
        when (val cin = tryGetCharAt(i)) {
            '\\' -> {
                // Escape character! We need to parse this (it's a token of its own)
                // Break, so we can create the string content token first
                break
            }

            '\'', '"' -> {
                // String marker of some kind - whether this applies to the string doesn't matter (it's a token of its own)
                // Break, so we can create the string content token first
                break
            }

            null -> {
                // We ran out of characters to read
                // Break
                break
            }

            else -> {
                // Unknown, probably data character
                // Let's make sure it's valid
                if (GDCharacterUtil.isInvisibleTextDirectionControlCharacter(cin)) {
                    // Invisible text direction character
                    // Break
                    break
                }

                // This character is fine!
            }
        }

        size = i
    }

    // Don't bother appending empty string content
    if (size == 0) return false

    enqueue(TokenLibrary.STRING_CONTENT_PART, 0, size)
    return true
}

private fun TokenLexer.tryLexingEscapeCharacter(): Boolean {
    if (tryGetCharAt(0) != '\\') return false

    // Escape characters have three forms
    // Single character: \*
    // Unicode UTF-16 codepoint (X = hex, case-insensitive): \uXXXX
    // Unicode UTF-32 codepoint (X = hex, case-insensitive): \UXXXXXX
    // Two UTF-16 escape codepoints can be combined into a single surrogate pair, we won't worry about that for now

    when (tryGetCharAt(1)) {
        null -> {
            // End of content found while reading escape character
            // We won't deal with issue raising
            enqueue(TokenLibrary.STRING_ESCAPE_PART)
            return true
        }

        'u' -> {
            // UTF-16
            for (i in 5 downTo 2) {
                if (hasCharAt(i)) {
                    enqueue(TokenLibrary.STRING_ESCAPE_PART, 0, i)
                    return true
                }
            }
        }

        'U' -> {
            // UTF-32
            for (i in 7 downTo 2) {
                if (hasCharAt(i)) {
                    enqueue(TokenLibrary.STRING_ESCAPE_PART, 0, i)
                    return true
                }
            }
        }

        else -> {
            // Unknown - hopefully just a normal 2-character escape
            enqueue(TokenLibrary.STRING_ESCAPE_PART, 0, 1)
            return true
        }
    }

    // We definitely found the escape character - we don't know what the data is
    enqueue(TokenLibrary.STRING_ESCAPE_PART)
    return true
}

private fun TokenLexer.tryLexingInvisibleControlChar(): Boolean {
    val c0 = tryGetCharAt(0) ?: return false
    if (GDCharacterUtil.isInvisibleTextDirectionControlCharacter(c0)) {
        enqueue(TokenLibrary.ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER)
        return true
    }
    return false
}

/**
 * Attempts to parse a string
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.tryLexingString(): Boolean {
    // Try finding the string prefix first
    tryLexingStringPrefix()

    // Parse the string marker if possible
    // Save the type as well - we need to use it for comparison later
    val marker0 = when (tryLexingStringMarker()) {
        true -> peek()
        false -> return false
    }

    // Parse the string content if possible until a string marker is found
    while (true) {
        tryLexingStringContent()

        // We stopped reading string content - something stopped us!
        tryLexingEscapeCharacter()
        tryLexingInvisibleControlChar()

        if (tryLexingStringMarker()) {
            // We found a string marker
            val marker = peek()
            if (marker!!.type == marker0!!.type) {
                // Same type as the start marker - return here
                return true
            }

            // This is a string marker unrelated to the starting one
            // Just keep going
        }

        // Return if there are no more characters to read
        if (!hasCharAt(0)) return true
    }
}
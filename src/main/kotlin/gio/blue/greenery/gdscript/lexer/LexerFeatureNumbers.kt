package gio.blue.greenery.gdscript.lexer

fun TokenLexer.findNumberEndOffsetAfterPrefix(): Int {
    // This is called *while still lexing the number*, right after the prefix
    // The first character is 0, the 2nd character is b or x
    for (i in 2..getRemainingBoundarySize()) {
        when (tryGetCharAt(i)) {
            null, '\n', '\t', ' ' -> {
                return i - 1
            }

            else -> {
                // This could be hex or binary, it could also be an unexpected character
                // We can't raise an issue though, let the next stage deal with it
            }
        }
    }
    return 1 // Just return the offset of 'b' / 'x'
}

/**
 * Attempts to parse a number
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.tryLexingNumber(): Boolean {
    when (getCharAt(0)) {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {}
        '.' -> {
            // These could be part of the number
            if (tryGetCharAt(1)?.isDigit() != true) return false

            // They're part of the number, keep going
        }

        // Handle unknown character
        else -> return false
    }

    // If the 1st character is 0, the 2nd character could be a number prefix (hex, binary)
    if (getCharAt(0) == '0') {
        when (tryGetCharAt(1)) {
            'b' -> {
                enqueue(TokenLibrary.BINARY_LITERAL, 0, findNumberEndOffsetAfterPrefix())
                return true
            }

            'x' -> {
                enqueue(TokenLibrary.HEX_LITERAL, 0, findNumberEndOffsetAfterPrefix())
                return true
            }

            null -> {
                // The number was just 0
                enqueue(TokenLibrary.INTEGER_LITERAL)
                return true
            }
        }
    }

    // Keep going until we hit a stop
    var isFloatingPoint = false
    var endOffset = 0
    for (i in 1..getRemainingBoundarySize()) {
        if (!hasCharAt(i)) break
        when (getCharAt(i)) {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {}
            'e', 'E' -> {}
            '_' -> {}
            '.', ',' -> isFloatingPoint = true
            else -> break
        }

        endOffset++
    }

    if (isFloatingPoint) {
        enqueue(TokenLibrary.FLOAT_LITERAL, 0, endOffset)
    } else {
        enqueue(TokenLibrary.INTEGER_LITERAL, 0, endOffset)
    }

    return true
}
package gio.blue.greenery.stage0

import gio.blue.greenery.stage0.core.Ethic
import gio.blue.greenery.stage0.core.ILiteral
import gio.blue.greenery.stage0.core.bad
import gio.blue.greenery.stage0.core.issues.ScanIssueType
import gio.blue.greenery.stage0.core.literals.FloatLiteral
import gio.blue.greenery.stage0.core.literals.IntegerLiteral
import gio.blue.greenery.stage0.core.ok

fun Tokenizer.isHoveringNumberLiteral(): Boolean {
    return when (peek()) {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true
        else -> false
    }
}

fun isHexDigit(char: Char): Boolean {
    return when (char) {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true
        'a', 'b', 'c', 'd', 'e', 'f' -> true
        'A', 'B', 'C', 'D', 'E', 'F' -> true
        else -> false
    }
}

fun Tokenizer.readToNumberLiteral(
    base: Int, startAsFloatingPoint: Boolean = false, negate: Boolean = false
): Ethic<ILiteral> {
    val builder = StringBuilder()
    var isFloatingPoint = startAsFloatingPoint

    var previousCharacterWasDecimalPoint = false
    var previousCharacterWasExponentPoint = false
    var previousCharacterWasUnderscore = false
    var char = peek()
    while (char != null) {
        when {
            isFloatingPoint && (char == 'e' || char == 'E') -> {
                if (previousCharacterWasExponentPoint) raise(here, ScanIssueType.UnexpectedDoubleExponentPoint)
            }

            char == '.' -> {
                if (base != 10) {
                    raise(here, ScanIssueType.UnsupportedBaseForDecimalPoint)
                } else {
                    isFloatingPoint = true
                }
                if (previousCharacterWasDecimalPoint) {
                    raise(here, ScanIssueType.UnexpectedDoubleDecimalPoint)
                }
            }

            char == '_' -> {
                if (previousCharacterWasUnderscore) {
                    raise(here, ScanIssueType.UnexpectedDoubleUnderscore)
                }
            }

            base == 16 && isHexDigit(char) -> {}
            base == 10 -> {
                if (!char.isDigit()) break
            }

            base == 2 -> {
                if (!char.isDigit()) break
                if (char != '0' && char != '1') raise(here, ScanIssueType.UnsupportedNumberForCurrentBase)
            }

            else -> break
        }

        previousCharacterWasDecimalPoint = char == '.'
        previousCharacterWasExponentPoint = char == 'e' || char == 'E'
        previousCharacterWasUnderscore = char == '_'

        // Don't append underscores
        if (!previousCharacterWasUnderscore) builder.append(char)

        mark()
        char = next()
    }

    if (builder.isEmpty()) return bad(raise(here, ScanIssueType.UnexpectedEmptyWord))

    if (isFloatingPoint) {
        return try {
            var value = builder.toString().toFloat()
            if (negate) value = -value
            ok(
                FloatLiteral(value)
            )
        } catch (e: NumberFormatException) {
            bad(raise(marked, ScanIssueType.FloatParseFailure))
        }
    } else {
        return try {
            var value = builder.toString().toInt(base)
            if (negate) value = -value
            ok(IntegerLiteral(value))
        } catch (e: NumberFormatException) {
            bad(raise(marked, ScanIssueType.IntegerParseFailure))
        }
    }
}
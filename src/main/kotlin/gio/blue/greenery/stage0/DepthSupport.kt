package gio.blue.greenery.stage0

import gio.blue.greenery.stage0.core.Token
import gio.blue.greenery.stage0.core.TokenKind
import gio.blue.greenery.stage0.core.Ethic
import gio.blue.greenery.stage0.core.issues.ScanIssueType
import gio.blue.greenery.stage0.core.ok

fun Tokenizer.isAtStartOfLine(): Boolean = !hasAdvancedSinceLineBreak
fun Tokenizer.hasPendingDedents(): Boolean = pendingDedentCount != 0u
fun Tokenizer.getDedent(): Token = instantiatePendingDedent()

fun Tokenizer.reverseAllIndents() {
    pendingDedentCount = indentStack.count().toUInt()
    indentStack.clear()
}

private fun Tokenizer.instantiatePendingDedent(): Token {
    pendingDedentCount--
    return Token(here, TokenKind.Dedent)
}

fun Tokenizer.readLineDepth(): Ethic<Token?> {
    var value = 0u

    var char = peek()
    while (char != null) {
        when (char) {
            ' ' -> {
                mark()
                if (hasFoundFirstIndent && isTabBasedIndent) {
                    next()
                    raise(marked, ScanIssueType.UnexpectedChangeInIndentType)
                }
                isTabBasedIndent = false
            }

            '\t' -> {
                mark()
                if (hasFoundFirstIndent && !isTabBasedIndent) {
                    next()
                    raise(marked, ScanIssueType.UnexpectedChangeInIndentType)
                }
                isTabBasedIndent = true
            }

            else -> break
        }

        value += 1u
        hasFoundFirstIndent = true
        char = next()
    }

    // Update indent stack
    if (!indentStack.empty()) {
        val lastDepthValue = indentStack.peek()
        if (value == lastDepthValue) {
            return ok(null)
        }

        if (value < lastDepthValue) {
            // DEDENT
            indentStack.pop()
            return ok(
                Token(marked, TokenKind.Dedent)
            )
        }
    }

    // INDENT
    indentStack.push(value)
    return ok(
        Token(marked, TokenKind.Indent)
    )
}
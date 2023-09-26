package gio.blue.greenery.stage0

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import gio.blue.greenery.stage0.core.CodeSection
import gio.blue.greenery.stage0.core.Token
import gio.blue.greenery.stage0.core.TokenKind
import gio.blue.greenery.stage0.core.ValueElement
import gio.blue.greenery.stage0.core.issues.ScanIssueType
import gio.blue.greenery.stage0.core.literals.*
import gio.blue.greenery.stage0.helpers.CharUtil
import java.util.*

class Tokenizer : LexerBase() {
    // Depth
    internal var isTabBasedIndent = false
    internal var hasFoundFirstIndent = false
    internal var indentStack: Stack<UInt> = Stack()
    internal var pendingDedentCount = 0u
    internal var hasAdvancedSinceLineBreak = false

    // Current state (parsing)
    private var sequence: CharSequence? = null
    private var startBoundary = 0
    private var endBoundary = 0
    private var offset: Int = 0
    private var state: Int = 0

    // Current state (marking)
    private var markStart: Int? = null
    private var markEnd = 0

    // Current state (element)
    private var token: Token = Token.empty()

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        // Reset indent / depth data
        indentStack.clear()
        pendingDedentCount = 0u
        hasFoundFirstIndent = false
        isTabBasedIndent = false

        // Reset element marking
        markStart = null
        markEnd = 0

        // Reset parsing data
        hasAdvancedSinceLineBreak = false

        // Set data
        sequence = buffer
        offset = startOffset
        state = initialState
        startBoundary = startOffset
        endBoundary = endOffset
    }

    override fun getState(): Int = state

    override fun getTokenType(): IElementType? {
        TODO("Not yet implemented")
    }

    override fun getTokenStart(): Int = token.start
    override fun getTokenEnd(): Int = token.end
    override fun getBufferSequence(): CharSequence = sequence!!
    override fun getBufferEnd(): Int = endBoundary

    override fun advance() {
        token.kind = TokenKind.None

        while (peek() != null) {
            process()

            if (token.kind != TokenKind.None) break
        }

        startBoundary = if (token.kind == TokenKind.None) {
            // No token found, and we're at the end of the sequence
            endBoundary
        } else {
            // Token found
            token.end
        }
    }

    fun next(): Char? {
        if (offset >= sequence!!.length) return null
        if (offset >= endBoundary) return null
        val v = sequence!![offset]

        offset += 1
        hasAdvancedSinceLineBreak = true

        return v
    }

    fun peek(): Char? {
        if (offset >= sequence!!.length) return null
        if (offset >= endBoundary) return null
        return sequence!![offset]
    }

    /**
     * Mark that there is an element at this location
     */
    internal fun mark() {
        if (markStart == null) markStart = offset
        markEnd = offset
    }

    internal fun setToken(kind: TokenKind) {
        token.start = markStart ?: 0
        token.end = markEnd
        token.kind = kind
    }

    private fun process() {
        if (hasPendingDedents()) return getDedent()

        val char = peek()!!
        markStart = null
        markEnd = 0

        when (char) {
            '#' -> return createCommentElement().attempt({ it }, null)
            '@' -> return createAnnotationElement().attempt({ it }, null)
            '\r', '\n' -> return createLineBreakElement().attempt({ it }, null)
        }

        if (isAtStartOfLine()) {
            mark()
            when (char) {
                '\t', ' ' -> return readLineDepth().attempt({ it }, null)
                else -> reverseAllIndents()
            }
            if (hasPendingDedents()) return null
        }

        // Skip unidentified whitespace
        if (char == ' ') {
            next()
            return null
        }

        // Single character elements
        when (char) {
            '{' -> {
                mark()
                next()
                return Token(marked, TokenKind.BitwiseNot)
            }

            '~' -> {
                mark()
                next()
                return Token(marked, TokenKind.BitwiseNot)
            }

            ',' -> {
                mark()
                next()
                return Token(marked, TokenKind.Comma)
            }

            ':' -> {
                mark()
                next()
                return Token(marked, TokenKind.Colon)
            }

            ';' -> {
                mark()
                next()
                return Token(marked, TokenKind.Semicolon)
            }

            '$' -> {
                mark()
                next()
                return Token(marked, TokenKind.DollarSign)
            }

            '?' -> {
                mark()
                next()
                return Token(marked, TokenKind.QuestionMark)
            }

            '`' -> {
                mark()
                next()
                return Token(marked, TokenKind.Backtick)
            }
        }

        // Handle brackets
        if (isHoveringBracket()) {
            mark()
            return createBracketElement().attempt({ it }, null)
        }

        // Multi use start characters
        when (char) {
            'r' -> {
                // Identifier or raw string
                mark()
                next()
                return when {
                    peek() == null -> ValueElement(
                        marked, TokenKind.Identifier, StringLiteral("r")
                    )

                    peek() == '"' || peek() == '\'' -> {
                        mark()
                        startBuildingString(isRawString = true).attempt({
                            ValueElement(
                                marked, TokenKind.Literal, StringLiteral(it.toString())
                            )
                        }, null)
                    }

                    else -> {
                        val builder = StringBuilder()
                        builder.append('r')
                        buildIdentifierStringWithExistingBuilder(builder)
                        createIdentifierElement(builder).attempt({ it }, null)
                    }
                }
            }

            '0' -> {
                // Identifier, octal number (unsupported), hex
                mark()
                next()
                when {
                    // If the next digit is a number, just return and read it next iteration
                    peek()?.isDigit() == true -> return null

                    // Hex number
                    peek() == 'x' -> {
                        mark()
                        next()
                        return when {
                            peek() == '_' -> {
                                raise(
                                    here, ScanIssueType.UnexpectedUnderscoreAfterNumberTypePrefix
                                )
                                null
                            }

                            peek()?.let { isHexDigit(it) } == true -> {
                                // Read as hex
                                readToNumberLiteral(16).attempt({
                                    ValueElement(
                                        marked, TokenKind.Literal, it
                                    )
                                }, null)
                            }

                            else -> {
                                // Error - unfinished number
                                raise(
                                    marked, ScanIssueType.UnexpectedEmptyNumberData
                                )
                                null
                            }
                        }
                    }

                    // Binary number
                    peek() == 'b' -> {
                        mark()
                        next()
                        return when (peek()) {
                            '_' -> {
                                raise(
                                    here, ScanIssueType.UnexpectedUnderscoreAfterNumberTypePrefix
                                )
                                null
                            }

                            '1', '0' -> {
                                // Read as binary
                                readToNumberLiteral(2).attempt({
                                    ValueElement(
                                        marked, TokenKind.Literal, it
                                    )
                                }, null)
                            }

                            else -> {
                                // Error - unfinished number
                                raise(
                                    marked, ScanIssueType.UnexpectedEmptyNumberData
                                )
                                null
                            }
                        }
                    }

                    else -> return ValueElement(
                        marked, TokenKind.Literal, IntegerLiteral(0)
                    )
                }
            }
        }

        // Number literals
        if (isHoveringNumberLiteral() && peek() != '0') {
            mark()
            return readToNumberLiteral(10).attempt({
                ValueElement(
                    marked, TokenKind.Literal, it
                )
            }, null)
        }

        // Generic string literals
        if (char == '"' || char == '\'') {
            mark()
            return startBuildingString().attempt({
                ValueElement(
                    marked, TokenKind.Literal, StringLiteral(it.toString())
                )
            }, null)
        }

        // Multi character elements
        when (char) {
            '!' -> {
                mark()
                return when (next()) {
                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.NotEqualTo)
                    }

                    else -> Token(marked, TokenKind.Not)
                }
            }

            '.' -> {
                mark()
                next()
                return when {
                    peek() == '.' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.DoublePeriod)
                    }

                    peek()?.isDigit() == true -> {
                        mark()
                        readToNumberLiteral(10, startAsFloatingPoint = true).attempt({
                            val literal = it as FloatLiteral
                            ValueElement(
                                marked, TokenKind.Literal, literal.copy(value = -(literal.value * 0.001f))
                            )
                        }, null)
                    }

                    else -> Token(marked, TokenKind.Period)
                }
            }

            '+' -> {
                mark()
                next()
                return when {
                    peek() == '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedPlus)
                    }

                    peek()?.isDigit() == true && !canLastElementPrecedeBinOp() -> {
                        mark()
                        readToNumberLiteral(10).attempt({
                            ValueElement(
                                marked, TokenKind.Literal, it
                            )
                        }, null)
                    }

                    else -> Token(marked, TokenKind.Plus)
                }
            }

            '-' -> {
                mark()
                next()
                return when {
                    peek() == '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedMinus)
                    }

                    peek()?.isDigit() == true && !canLastElementPrecedeBinOp() -> {
                        mark()
                        readToNumberLiteral(10, negate = true).attempt({
                            ValueElement(
                                marked, TokenKind.Literal, it
                            )
                        }, null)
                    }

                    peek() == '>' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.ForwardArrow)
                    }

                    else -> Token(marked, TokenKind.Minus)
                }
            }

            '*' -> {
                mark()
                return when (next()) {
                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedMultiply)
                    }

                    '*' -> {
                        mark()
                        if (next() == '=') {
                            mark()
                            next()
                            return Token(marked, TokenKind.TargetedPower)
                        }

                        Token(marked, TokenKind.Power)
                    }

                    else -> Token(marked, TokenKind.Multiply)
                }
            }

            '/' -> {
                mark()
                return when (next()) {
                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedDivide)
                    }

                    else -> Token(marked, TokenKind.Divide)
                }
            }

            '%' -> {
                mark()
                return when (next()) {
                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedModulo)
                    }

                    else -> Token(marked, TokenKind.Modulo)
                }
            }

            '^' -> {
                mark()
                return when (next()) {
                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedBitwiseXor)
                    }

                    '"', '\'' -> {
                        mark()
                        startBuildingString().attempt({
                            ValueElement(
                                marked, TokenKind.Literal, NodePathLiteral(it.toString())
                            )
                        }, null)
                    }

                    else -> Token(marked, TokenKind.BitwiseXor)
                }
            }

            '&' -> {
                mark()
                return when (next()) {
                    '&' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.And)
                    }

                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedBitwiseAnd)
                    }

                    '"', '\'' -> {
                        mark()
                        startBuildingString().attempt({
                            ValueElement(
                                marked, TokenKind.Literal, StringNameLiteral(it.toString())
                            )
                        }, null)
                    }

                    else -> Token(marked, TokenKind.BitwiseAnd)
                }
            }

            '|' -> {
                mark()
                return when (next()) {
                    '|' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.Or)
                    }

                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.TargetedBitwiseOr)
                    }

                    else -> Token(marked, TokenKind.BitwiseOr)
                }
            }

            '=' -> {
                mark()
                return when (next()) {
                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.EqualTo)
                    }

                    else -> Token(marked, TokenKind.Assign)
                }
            }

            '<' -> {
                mark()
                return when (next()) {
                    '<' -> {
                        mark()
                        when (next()) {
                            '=' -> {
                                mark()
                                next()
                                Token(marked, TokenKind.TargetedBitwiseLeftShift)
                            }

                            else -> Token(marked, TokenKind.BitwiseLeftShift)
                        }
                    }

                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.LesserThanOrEqualTo)
                    }

                    else -> Token(marked, TokenKind.LesserThan)
                }
            }

            '>' -> {
                mark()
                return when (next()) {
                    '>' -> {
                        mark()
                        when (next()) {
                            '=' -> {
                                mark()
                                next()
                                Token(marked, TokenKind.TargetedBitwiseRightShift)
                            }

                            else -> Token(marked, TokenKind.BitwiseRightShift)
                        }
                    }

                    '=' -> {
                        mark()
                        next()
                        Token(marked, TokenKind.GreaterThanOrEqualTo)
                    }

                    else -> Token(marked, TokenKind.GreaterThan)
                }
            }
        }

        // Last resort - can we consider it the start of an identifier?
        if (CharUtil.isValidCharacterForIdentifierStart(char)) {
            val ethic = createIdentifierElement(startBuildingIdentifierString())
            if (ethic.isOk) return ethic.get()
        }

        println("Couldn't process char '$char'")
        next()
        return null
    }
}
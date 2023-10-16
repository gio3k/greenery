package gio.blue.greenery.gdscript.lexer

import com.intellij.psi.tree.IElementType

data class MultiCharacter(val type: IElementType, val c0: Char, val c1: Char? = null, var c2: Char? = null)

private val multi0 = setOf(
    MultiCharacter(TokenLibrary.PLUS, '+'),
    MultiCharacter(TokenLibrary.MINUS, '-'),
    MultiCharacter(TokenLibrary.MULT, '*'),
    MultiCharacter(TokenLibrary.DIV, '/'),
    MultiCharacter(TokenLibrary.PERC, '%'),
    MultiCharacter(TokenLibrary.LT, '<'),
    MultiCharacter(TokenLibrary.GT, '>'),
    MultiCharacter(TokenLibrary.AND, '&'),
    MultiCharacter(TokenLibrary.OR, '|'),
    MultiCharacter(TokenLibrary.XOR, '^'),
    MultiCharacter(TokenLibrary.TILDE, '~'),
    MultiCharacter(TokenLibrary.EQ, '='),
    MultiCharacter(TokenLibrary.EXCLAIM, '!'),
)

private val multi1 = setOf(
    MultiCharacter(TokenLibrary.PLUSEQ, '+', '='),
    MultiCharacter(TokenLibrary.MINUSEQ, '-', '='),
    MultiCharacter(TokenLibrary.EXP, '*', '*'),
    MultiCharacter(TokenLibrary.MULTEQ, '*', '='),
    MultiCharacter(TokenLibrary.DIVEQ, '/', '='),
    MultiCharacter(TokenLibrary.PERCEQ, '%', '='),
    MultiCharacter(TokenLibrary.LTLT, '<', '<'),
    MultiCharacter(TokenLibrary.LE, '<', '='),
    MultiCharacter(TokenLibrary.GTGT, '>', '>'),
    MultiCharacter(TokenLibrary.GE, '>', '='),
    MultiCharacter(TokenLibrary.EQEQ, '=', '='),
    MultiCharacter(TokenLibrary.NE, '!', '='),
    MultiCharacter(TokenLibrary.OREQ, '|', '='),
    MultiCharacter(TokenLibrary.ANDEQ, '&', '='),
    MultiCharacter(TokenLibrary.XOREQ, '^', '='),
    MultiCharacter(TokenLibrary.RARROW, '-', '>'),
    MultiCharacter(TokenLibrary.COLONEQ, ':', '='),
)

private val multi2 = setOf(
    MultiCharacter(TokenLibrary.LTLTEQ, '<', '<', '='),
    MultiCharacter(TokenLibrary.EXPEQ, '*', '*', '='),
    MultiCharacter(TokenLibrary.GTGTEQ, '>', '>', '='),
)

/**
 * Attempts to parse a multi character token
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun TokenLexer.tryLexingMultiCharacter(): Boolean {
    val c0 = getCharAt(0)
    val c1 = tryGetCharAt(1)
    val c2 = tryGetCharAt(2)

    for (i0 in multi0) {
        if (c0 != i0.c0) continue
        for (i1 in multi1) {
            if (c0 != i1.c0) continue
            if (c1 != i1.c1) continue
            for (i2 in multi2) {
                if (c0 != i2.c0) continue
                if (c1 != i2.c1) continue
                if (c2 != i2.c2) continue
                enqueue(i2.type, 0, 2)
                return true
            }

            enqueue(i1.type, 0, 1)
            return true
        }

        enqueue(i0.type, 0, 0)
        return true
    }

    return false
}
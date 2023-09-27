package gio.blue.greenery.gdscript.lexer

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.GDTokens

data class MultiCharacter(val type: IElementType, val c0: Char, val c1: Char? = null, var c2: Char? = null)

private val multi0 = setOf(
    MultiCharacter(GDTokens.PLUS, '+'),
    MultiCharacter(GDTokens.MINUS, '-'),
    MultiCharacter(GDTokens.MULT, '*'),
    MultiCharacter(GDTokens.DIV, '/'),
    MultiCharacter(GDTokens.PERC, '%'),
    MultiCharacter(GDTokens.LT, '<'),
    MultiCharacter(GDTokens.GT, '>'),
    MultiCharacter(GDTokens.AND, '&'),
    MultiCharacter(GDTokens.OR, '|'),
    MultiCharacter(GDTokens.XOR, '^'),
    MultiCharacter(GDTokens.TILDE, '~'),
    MultiCharacter(GDTokens.EQ, '='),
)

private val multi1 = setOf(
    MultiCharacter(GDTokens.PLUSEQ, '+', '='),
    MultiCharacter(GDTokens.MINUSEQ, '-', '='),
    MultiCharacter(GDTokens.EXP, '*', '*'),
    MultiCharacter(GDTokens.MULTEQ, '*', '='),
    MultiCharacter(GDTokens.DIVEQ, '/', '='),
    MultiCharacter(GDTokens.PERCEQ, '%', '='),
    MultiCharacter(GDTokens.LTLT, '<', '<'),
    MultiCharacter(GDTokens.LE, '<', '='),
    MultiCharacter(GDTokens.GTGT, '>', '>'),
    MultiCharacter(GDTokens.GE, '>', '='),
    MultiCharacter(GDTokens.EQEQ, '=', '='),
    MultiCharacter(GDTokens.NE, '!', '='),
    MultiCharacter(GDTokens.OREQ, '|', '='),
    MultiCharacter(GDTokens.ANDEQ, '&', '='),
    MultiCharacter(GDTokens.XOREQ, '^', '='),
    MultiCharacter(GDTokens.RARROW, '-', '>'),
    MultiCharacter(GDTokens.COLONEQ, ':', '='),
)

private val multi2 = setOf(
    MultiCharacter(GDTokens.LTLTEQ, '<', '<', '='),
    MultiCharacter(GDTokens.EXPEQ, '*', '*', '='),
    MultiCharacter(GDTokens.GTGTEQ, '>', '>', '='),
)

/**
 * Attempts to parse a multi character token
 * @receiver GDLexer
 * @return Boolean True if a token was parsed
 */
fun GDLexer.tryLexingMultiCharacter(): Boolean {
    val c0 = getCharAt(0)
    val c1 = tryGetCharAt(1)
    val c2 = tryGetCharAt(2)

    for (i0 in multi0) {
        if (c0 != i0.c0) continue
        for (i1 in multi1) {
            if (c1 != i1.c1) continue
            for (i2 in multi2) {
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
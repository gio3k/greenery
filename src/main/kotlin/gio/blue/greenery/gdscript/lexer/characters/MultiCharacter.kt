package gio.blue.greenery.gdscript.lexer.characters

import com.intellij.psi.tree.IElementType
import gio.blue.greenery.gdscript.lexer.TokenLexer

data class MultiCharacter(val type: IElementType, val c0: Char, val c1: Char? = null, var c2: Char? = null)

fun TokenLexer.parseMultiCharacter(): Boolean {
    val c0 = getCharAt(0)
    val c1 = tryGetCharAt(1)
    val c2 = tryGetCharAt(2)

    for (i0 in MultiCharacterMap.Value0) {
        if (c0 != i0.c0) continue
        for (i1 in MultiCharacterMap.Value1) {
            if (c0 != i1.c0) continue
            if (c1 != i1.c1) continue
            for (i2 in MultiCharacterMap.Value2) {
                if (c0 != i2.c0) continue
                if (c1 != i2.c1) continue
                if (c2 != i2.c2) continue
                enqueue(i2.type, size = 3)
                return true
            }

            enqueue(i1.type, size = 2)
            return true
        }

        enqueue(i0.type, size = 1)
        return true
    }

    return false
}
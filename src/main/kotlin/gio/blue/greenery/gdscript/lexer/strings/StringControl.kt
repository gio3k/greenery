package gio.blue.greenery.gdscript.lexer.strings

import gio.blue.greenery.gdscript.GDCharacterUtil
import gio.blue.greenery.gdscript.lexer.TokenLexer
import gio.blue.greenery.gdscript.lexer.TokenLibrary

fun TokenLexer.parseStringControlCharacter(): Boolean {
    if (!hasCharAt(0))
        return false

    if (GDCharacterUtil.isInvisibleTextDirectionControlCharacter(getCharAt(0))) {
        enqueue(TokenLibrary.ISSUE_STRING_INVISIBLE_TEXT_DIRECTION_CHARACTER)
        return true
    }

    return false
}
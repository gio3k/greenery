package gio.blue.greenery.gdscript.lexer.characters

import gio.blue.greenery.gdscript.lexer.TokenLibrary

class MultiCharacterMap {
    companion object {
        val Value0 = setOf(
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

        val Value1 = setOf(
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

        val Value2 = setOf(
            MultiCharacter(TokenLibrary.LTLTEQ, '<', '<', '='),
            MultiCharacter(TokenLibrary.EXPEQ, '*', '*', '='),
            MultiCharacter(TokenLibrary.GTGTEQ, '>', '>', '='),
        )
    }
}
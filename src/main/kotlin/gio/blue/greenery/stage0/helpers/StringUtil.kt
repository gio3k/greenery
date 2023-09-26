package gio.blue.greenery.stage0.helpers

object StringUtil {
    fun isInvisibleTextDirectionControl(char: Char): Boolean {
        if (char == 0x200E.toChar()) return true
        if (char == 0x200F.toChar()) return true
        if (char >= 0x202A.toChar() && char <= 0x202E.toChar()) return true
        if (char >= 0x2066.toChar() && char <= 0x2069.toChar()) return true
        return false
    }
}
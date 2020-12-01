package cn.maxmc.maxcore.utils

@Deprecated("Useless in console")
fun fromRGB(r:Int,g:Int,b:Int): String {
    return "\u001b[38;2;$r;$g;${b}m"
}
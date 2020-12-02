package cn.maxmc.maxhub

import io.izzel.taboolib.module.locale.TLocale
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

fun pSendTo(sender: CommandSender, path: String, vararg msg: String) {
    sender.sendMessage(
        settings.getStringColored("Base.prefix")+
        TLocale.asString(path,*msg)
    )
}

fun pSendToConsole(path: String, vararg msg: String) {
    Bukkit.getConsoleSender().sendMessage(
        TLocale.asString("console.prefix")+
                TLocale.asString(path,*msg)
    )
}

fun <K> ArrayList<K>.addAll(vararg elements:K) {
    this.addAll(elements)
}
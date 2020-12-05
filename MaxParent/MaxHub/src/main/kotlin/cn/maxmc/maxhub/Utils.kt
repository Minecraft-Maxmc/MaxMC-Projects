package cn.maxmc.maxhub

import cn.maxmc.maxhub.modules.AbstractModule
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Baffle
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

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
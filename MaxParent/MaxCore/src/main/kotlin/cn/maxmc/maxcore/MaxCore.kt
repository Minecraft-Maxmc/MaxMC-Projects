package cn.maxmc.maxcore

import io.izzel.taboolib.loader.Plugin
import org.bukkit.Bukkit

object MaxCore: Plugin() {

    override fun onEnable() {
        """
            §b,--. ,--.           ,--. ,--.
            §b|  | |  | ,---. . . |  | |  | ,---.
            §b|  '-'  | ,---|  X  |  '-'  | |
            §b|       | `---^ ' ' |       | `---'
            
            §b正在加载 §6MaxMC §aCore
        """.trimIndent().lines().forEach {
            Bukkit.getConsoleSender().sendMessage(it)
        }
    }

    override fun onDisable() {

    }
}
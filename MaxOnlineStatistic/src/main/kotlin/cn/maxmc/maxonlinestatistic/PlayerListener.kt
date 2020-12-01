package cn.maxmc.maxonlinestatistic

import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object PlayerListener: Listener {
    val loginTimeMap = HashMap<ProxiedPlayer,Long>()

    @EventHandler
    fun onJoin(e: PostLoginEvent) {
        loginTimeMap[e.player] = System.currentTimeMillis()
    }

    @EventHandler
    fun onLeft() {

    }
}
package cn.maxmc.maxhub.modules

import io.izzel.taboolib.module.inject.PlayerContainer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerPortalEvent

object PortalCommand: AbstractModule() {

    @EventHandler
    fun onPortal(e: PlayerMoveEvent) {
        if(e.to.block.type == Material.PORTAL) {
            if(config.getString("nether") != null && config.getString("nether") != "") {
                Bukkit.dispatchCommand(e.player, config.getString("nether"))
            }
        }

        if(e.to.block.type == Material.ENDER_PORTAL) {
            if(config.getString("nether") != null && config.getString("end") != "") {
                Bukkit.dispatchCommand(e.player, config.getString("end"))
            }
        }
    }
}
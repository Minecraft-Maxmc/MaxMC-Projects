package cn.maxmc.maxhub.modules

import cn.maxmc.maxhub.spawn
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent

object AntiVoid: IModule {

    @EventHandler
    fun onVoid(e: PlayerMoveEvent) {
        if(e.to.y < 0) {
            e.player.teleport(spawn)
        }
    }

}
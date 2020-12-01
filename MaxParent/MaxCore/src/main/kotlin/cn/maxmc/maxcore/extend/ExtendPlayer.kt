package cn.maxmc.maxcore.extend

import org.bukkit.entity.Player

private val pExtendMap = HashMap<Player,ExtendPlayer>()
val Player.extend: ExtendPlayer
    get() {
        return pExtendMap[this]!!
    }

data class ExtendPlayer(val player: Player){
    
}
package cn.maxmc.maxhub.modules

import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

object SpeedUp: IModule {
    enum class State(val item: ItemStack) {
        ACTIVE(ItemBuilder(Material.SULPHUR)
            .amount(1)
            .name("")
            .lore()
            .build()
        ),
        INACTIVE(ItemBuilder(Material.SUGAR)
            .amount(1)
            .name(TLocale.asString(""))
            .lore()
            .build()
        );

        fun switch(): State = if(this == ACTIVE) INACTIVE else ACTIVE
    }

    val playerSpeedMap = HashMap<Player,State>()

    @EventHandler
    fun onClickSugar(e: PlayerInteractEvent) {
        if(!e.hasItem() && (e.action != Action.RIGHT_CLICK_BLOCK || e.action != Action.RIGHT_CLICK_AIR )) {
            return
        }
        if(e.item != State.ACTIVE.item && e.item != State.INACTIVE.item) {
            return
        }

        switchPlayerState(e.player)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        playerSpeedMap.put(e.player,State.INACTIVE)
    }

    @EventHandler
    fun onLeft(e: PlayerQuitEvent) {
        playerSpeedMap.remove(e.player)
    }

    fun switchPlayerState(p: Player) {
        playerSpeedMap[p] = playerSpeedMap[p]!!.switch()
    }
}
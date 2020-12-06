package cn.maxmc.maxhub.modules

import cn.maxmc.maxhub.modules.SpeedUp.State.*
import cn.maxmc.maxhub.pSendTo
import cn.maxmc.maxhub.settings
import io.izzel.taboolib.module.inject.PlayerContainer
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Baffle
import io.izzel.taboolib.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.concurrent.TimeUnit

object SpeedUp: AbstractModule() {
    private val baffle = Baffle.of(config.getLong("switch_delay"),TimeUnit.SECONDS) as Baffle.BaffleTime

    enum class State(val item: ItemStack) {
        ACTIVE(ItemBuilder(Material.SULPHUR)
            .amount(1)
            .name(TLocale.asString("SpeedUp.active_item.name"))
            .lore(TLocale.asString("SpeedUp.active_item.lore").lines())
            .build()
        ),
        INACTIVE(ItemBuilder(Material.SUGAR)
            .amount(1)
            .name(TLocale.asString("SpeedUp.inactive_item.name"))
            .lore(TLocale.asString("SpeedUp.inactive_item.lore").lines())
            .shiny()
            .build()
        );

        fun switch(): State = if(this == ACTIVE) INACTIVE else ACTIVE
    }

    private val playerSpeedMap = HashMap<Player,State>()

    // Register speed up item in InvManager
    override fun onEnable() {
        InvManager.moduleItems[this] = mapOf(INACTIVE.item to 7)
    }

    override fun onDisable() {
        InvManager.moduleItems.remove(this)
    }
    var Player.speedUpState: State
        get() {
            return playerSpeedMap[this]!!
        }
        set(state) {
            playerSpeedMap[this] = state
        }

    @EventHandler
    fun onClickSugar(e: PlayerInteractEvent) {
        if(!e.hasItem() && e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) {
            return
        }
        if(e.item != ACTIVE.item && e.item != INACTIVE.item) {
            return
        }
        switchPlayerState(e.player)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        playerSpeedMap[e.player] = INACTIVE
    }

    @EventHandler
    fun onLeft(e: PlayerQuitEvent) {
        playerSpeedMap.remove(e.player)
        baffle.release(e.player,e.player.uniqueId.toString())
    }

    private fun switchPlayerState(p: Player) {
        if(!baffle.hasNext(p.name)) {
            pSendTo(p,"SpeedUp.delay", TimeUnit.MILLISECONDS.toMillis(baffle.nextTime(p.uniqueId.toString())).toString())
            return
        }

        p.speedUpState = p.speedUpState.switch()
        when(p.speedUpState) {
            ACTIVE -> {
                p.playSound(p.location, Sound.valueOf(config.getString("sound")),1f,2f)
                p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE,config.getDouble("active_speed").toInt(),false,false))
            }
            INACTIVE -> {
                p.playSound(p.location, Sound.valueOf(config.getString("sound")),1f,0f)
                p.removePotionEffect(PotionEffectType.SPEED)
            }
        }
        p.itemInHand = p.speedUpState.item
        pSendTo(p,"SpeedUp.${p.speedUpState.name.toLowerCase()}")
    }
}
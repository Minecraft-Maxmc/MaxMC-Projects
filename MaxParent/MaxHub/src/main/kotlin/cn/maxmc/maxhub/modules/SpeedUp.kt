package cn.maxmc.maxhub.modules

import cn.maxmc.maxhub.modules.SpeedUp.State.*
import cn.maxmc.maxhub.pSendTo
import cn.maxmc.maxhub.settings
import io.izzel.taboolib.module.inject.TSchedule
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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SpeedUp: AbstractModule() {
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
    private val playerDelayMap = HashMap<Player,Int>()

    // Register speed up item in InvManager
    init {
        InvManager.moduleItems[this] = mapOf(INACTIVE.item to 7)
    }

    var Player.delay: Int
        get() {
            return playerDelayMap[this] ?: 0
        }
        set(value) {
            if(value == 0) {
                playerDelayMap.remove(this)
                return
            }
            playerDelayMap[this] = value
        }

    val Player.isInDelay: Boolean
        get() {
            return playerDelayMap.containsKey(this)
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
        if(!e.hasItem() && (e.action != Action.RIGHT_CLICK_BLOCK || e.action != Action.RIGHT_CLICK_AIR)) {
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
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun switchPlayerState(p: Player) {
        if(playerDelayMap.containsKey(p)) {
            pSendTo(p,"SpeedUp.delay", playerDelayMap[p].toString())
            return
        }
        playerDelayMap[p] = settings.getInt("SpeedUp.switch_delay")
        playerSpeedMap[p] = playerSpeedMap[p]!!.switch()
        when(p.speedUpState) {
            ACTIVE -> {
                p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE,config.getDouble("active_speed").toInt(),false,false))
            }
            INACTIVE -> {
                p.removePotionEffect(PotionEffectType.SPEED)
            }
        }
        p.itemInHand = p.speedUpState.item
        pSendTo(p,"SpeedUp.${p.speedUpState.name.toLowerCase()}")
    }
    @TSchedule(period = 20)
    fun decreaseTime() {
        playerDelayMap.forEach { (p,time) ->
            playerDelayMap[p] = time - 1
            if (playerDelayMap[p]!! <= 0) {
                playerDelayMap.remove(p)
            }
        }
    }
}
package cn.maxmc.maxhub.modules

import cn.maxmc.maxhub.pSendTo
import io.izzel.taboolib.module.inject.PlayerContainer
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Baffle
import io.izzel.taboolib.util.item.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

object HidePlayer: AbstractModule() {
    @PlayerContainer(uniqueId = true)
    val baffle = Baffle.of(config.getLong("switch_delay"),TimeUnit.SECONDS) as Baffle.BaffleTime

    override fun onEnable() {
        InvManager.moduleItems[this] = mapOf(State.INACTIVE.item to 8)
    }

    override fun onDisable() {
        InvManager.moduleItems.remove(this)
    }

    enum class State(val item: ItemStack) {
        ACTIVE(
            ItemBuilder(Material.SLIME_BALL)
                .amount(1)
                .name(TLocale.asString("HIdePlayer.active_item.name"))
                .lore(TLocale.asString("HIdePlayer.active_item.lore").lines())
                .shiny()
                .build()
        ),
        INACTIVE(
            ItemBuilder(Material.FIREBALL)
                .amount(1)
                .name(TLocale.asString("HIdePlayer.inactive_item.name"))
                .lore(TLocale.asString("HIdePlayer.inactive_item.lore").lines())
                .shiny()
                .build()
        );

        fun switch(): State = if(this == ACTIVE) INACTIVE else ACTIVE
    }

    private val hideMap = HashMap<Player,State>()

    var Player.hidePlayerState: State
        get() {
            return hideMap[this]!!
        }
        set(state) {
            hideMap[this] = state
        }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        hideMap[e.player] = State.INACTIVE
        Bukkit.getOnlinePlayers()
            .filter { it.hidePlayerState == State.ACTIVE }
            .forEach {
                it.hidePlayer(e.player)
            }
    }

    @EventHandler
    fun onLeft(e: PlayerQuitEvent) {
        hideMap.remove(e.player)
    }

    @EventHandler
    fun onClick(e: PlayerInteractEvent) {
        if(!e.hasItem() && e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) {
            return
        }
        if(e.item != State.ACTIVE.item && e.item != State.INACTIVE.item) {
            return
        }
        switchPlayerState(e.player)
    }

    private fun switchPlayerState(player: Player) {
        if(!baffle.hasNext(player.uniqueId.toString())) {
            pSendTo(player,"HidePlayer.delay", TimeUnit.MILLISECONDS.toSeconds(baffle.nextTime(player.uniqueId.toString())).toString())
            return
        }
        player.hidePlayerState = player.hidePlayerState.switch()
        when(player.hidePlayerState) {
            State.ACTIVE -> {
                player.playSound(player.location, Sound.valueOf(config.getString("sound")),1f,2f)
                Bukkit.getOnlinePlayers()
                    .filter { it != player }
                    .forEach {
                        player.hidePlayer(it)
                    }
            }
            State.INACTIVE -> {
                player.playSound(player.location, Sound.valueOf(config.getString("sound")),1f,0f)
                Bukkit.getOnlinePlayers()
                    .filter { it != player }
                    .forEach {
                        player.showPlayer(it)
                    }
            }
        }
        player.itemInHand = player.hidePlayerState.item
        pSendTo(player,"HidePlayer.${player.hidePlayerState.name.toLowerCase()}")
    }
}
package cn.maxmc.maxhub.modules

import io.izzel.taboolib.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

object InvManager : AbstractModule(){

    val moduleItems = HashMap<AbstractModule,Map<ItemStack,Int>>()

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        if(config.getBoolean("no_drop")) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun pluginItem(e: PlayerJoinEvent) {

        // clean item
        if (config.getBoolean("clean")) {
            e.player.inventory.clear()
        }

        // Fill configured items
        if(config.getBoolean("join_items.enable")) {
            fillJoinItems(e.player.inventory)
        }

        // Fill module items
        moduleItems.forEach { module, itemMap ->
            if(!module.isEnable) {
                return@forEach
            }
            itemMap.forEach { item, slot ->
                e.player.inventory.setItem(slot,item)
            }
        }
    }

    private fun fillJoinItems(inventory: PlayerInventory) {
        // section = InvManager.join_items.items
        // true =    InvManager.join_items.items
        config.getConfigurationSection("join_items.items").getKeys(false).forEach {
            val section = config.getConfigurationSection("join_items.items.${it}")
            val builder = ItemBuilder(Material.matchMaterial(section.getString("type")))
                .name(section.getString("name").replace('&', '§'))
                .amount(section.getInt("amount"))
                .damage(section.getInt("damage"))
                .lore((section.getString("lore")?: "").lines())
            val enchantments = parseEnchantments(section.getConfigurationSection("enchantments"))
            if(enchantments.isNotEmpty()) {
                enchantments.forEach { (name,enchantment) ->
                    builder.enchant(enchantment,section.getInt("enchantments.${name}"))
                }
            }
            inventory.setItem(section.getInt("slot"),builder.build())
        }
    }

    private fun parseEnchantments(section: ConfigurationSection?): Map<String,Enchantment> {
        if(section == null || section.getKeys(false).isEmpty()) {
            return emptyMap()
        }
        val result = HashMap<String,Enchantment>()
        section.getKeys(false).forEach {result[it] = Enchantment.getByName(it.toUpperCase()) ?: return@forEach}
        return result
    }

}
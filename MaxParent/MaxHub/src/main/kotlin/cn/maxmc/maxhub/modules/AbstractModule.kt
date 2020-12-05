package cn.maxmc.maxhub.modules

import cn.maxmc.maxhub.pSendToConsole
import cn.maxmc.maxhub.settings
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

abstract class AbstractModule: Listener {
    var isEnable: Boolean = false
    private set
    val config: ConfigurationSection
    get() = settings.getConfigurationSection(this::class.java.simpleName)
    val name: String
        get() = this::class.java.simpleName
    fun enable(plugin: Plugin) {
        pSendToConsole("console.enable_module", this::class.java.simpleName)
        Bukkit.getPluginManager().registerEvents(this,plugin)
        isEnable = true
        onEnable()
    }

    open fun onEnable() {

    }

    fun disable() {
        pSendToConsole("console.disable_module", this::class.java.simpleName)
         this.javaClass.declaredMethods.forEach {
             if(it.getAnnotation(EventHandler::class.java) == null){
                 return@forEach
             }
             val declaredMethod = it.parameters[0].type.getDeclaredMethod("getHandlerList")
             val handlerList = declaredMethod(null) as HandlerList
             handlerList.unregister(this)
         }
        isEnable = false
        onDisable()
    }

    open fun onDisable() {

    }
}
package cn.maxmc.maxhub.modules

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

interface IModule: Listener {
    fun register(plugin: Plugin) {
        Bukkit.getPluginManager().registerEvents(this,plugin)
    }

    fun unRegister(plugin: Plugin) {
         this.javaClass.declaredMethods.forEach {
             if(it.getAnnotation(EventHandler::class.java) == null){
                 return@forEach
             }
             val declaredMethod = it.parameters[0].type.getDeclaredMethod("getHandlerList")
             val handlerList = declaredMethod(null) as HandlerList
             handlerList.unregister(this)
         }
    }

}
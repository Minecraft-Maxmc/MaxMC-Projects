package cn.maxmc.maxhub.modules

import cn.maxmc.maxhub.MaxHub
import cn.maxmc.maxhub.nms.AbsNMS
import io.izzel.taboolib.module.compat.PlaceholderHook
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

object ScoreBoard: AbstractModule() {
    private lateinit var task: BukkitTask
    private var pointer = 0
    private val titles = config.getStringList("title").map { it.replace('&','§') }

    override fun onEnable() {
        task = Bukkit.getScheduler().runTaskTimer(MaxHub.plugin, {
            pointer++
            if(pointer > titles.size-1) {
                pointer = 0
            }
            if(!PlaceholderHook.isHooked()) {
                Bukkit.getOnlinePlayers().forEach {
                    if (!it.hasPermission("maxhub.scoreboard")) {
                        return@forEach
                    }
                    AbsNMS.instance.sendScoreBoard(
                        it,
                        titles[pointer],
                        *config.getString("content").replace('&', '§').lines().toTypedArray()
                    )
                }
            }else {
                Bukkit.getOnlinePlayers().forEach {
                    if (!it.hasPermission("maxhub.scoreboard")) {
                        return@forEach
                    }
                    AbsNMS.instance.sendScoreBoard(
                        it,
                        PlaceholderHook.replace(it,config.getString("title").replace('&', '§')),
                        *PlaceholderHook.replace(it,config.getString("content").replace('&', '§').lines()).toTypedArray()
                    )
                }
            }
        }, 0, config.getInt("refresh") * 20L)
    }

    override fun onDisable() {
        task.cancel()
    }

}
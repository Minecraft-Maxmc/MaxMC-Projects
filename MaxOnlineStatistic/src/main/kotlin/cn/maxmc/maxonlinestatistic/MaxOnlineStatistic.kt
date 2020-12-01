package cn.maxmc.maxonlinestatistic

import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.nio.file.Files

lateinit var instance: MaxOnlineStatistic
private set

lateinit var config: Configuration

class MaxOnlineStatistic: Plugin() {
    init {
        instance = this
    }

    @Override
    override fun onEnable() {
        initConfig()
        config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load("settings.yml")
        this.proxy.pluginManager.registerListener(this,PlayerListener)
    }

    fun initConfig() {
        val file = File(dataFolder,"settings.yml")
        if(!file.exists()){
            Files.copy(getResourceAsStream("settings.yml"),file.toPath())
        }
    }
}
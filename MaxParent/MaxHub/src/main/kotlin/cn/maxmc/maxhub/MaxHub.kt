package cn.maxmc.maxhub

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject

@TInject("settings.yml",locale = "lang")
lateinit var settings: TConfig

object MaxHub: Plugin() {
    override fun onEnable() {

        settings.listener {
            pSendToConsole("console.reload")
        }

        if(settings.getString("version") == null || settings.getString("version") != "0.1") {
            settings.migrate()
            settings.reload()
        }

        ModuleManager.init()
    }
}
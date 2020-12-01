package cn.maxmc.maxhub

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject

@TInject("settings.yml")
lateinit var settings: TConfig

object MaxHub: Plugin()
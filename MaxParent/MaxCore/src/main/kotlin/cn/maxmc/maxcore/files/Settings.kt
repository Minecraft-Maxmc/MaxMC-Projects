package cn.maxmc.maxcore.files

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject

object Settings {
    @TInject(value = ["settings.yml"], locale = "lang")
    lateinit var settings: TConfig
}
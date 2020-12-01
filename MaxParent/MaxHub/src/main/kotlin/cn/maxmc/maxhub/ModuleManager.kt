package cn.maxmc.maxhub

import cn.maxmc.maxhub.modules.AntiVoid
import cn.maxmc.maxhub.modules.IModule

object ModuleManager {
    val modules = ArrayList<IModule>()

    init {
        modules.add(AntiVoid)
    }
}
package cn.maxmc.maxhub

import cn.maxmc.maxhub.modules.*

object ModuleManager {
    val modules = ArrayList<AbstractModule>()

    init {
        modules.addAll(
            AntiVoid,
            SpeedUp,
            InvManager,
            HidePlayer,
            PortalCommand,
            ScoreBoard,
        )
    }

    fun init() {
        pSendToConsole("console.load_modules")
        modules.forEach {
            val moduleName = it.javaClass.simpleName
            val enable = settings.getBoolean("${moduleName}.enable")
            if(enable && !it.isEnable) {
                it.enable(MaxHub.plugin)
            }
        }
        pSendToConsole("console.load_modules_success", modules.size.toString())
    }

    fun getModuleByName(name: String): AbstractModule? {
        modules.forEach {
            if(it.name == name){
                return it
            }
        }
        return null
    }
}
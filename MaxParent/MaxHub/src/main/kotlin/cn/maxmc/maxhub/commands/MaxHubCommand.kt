package cn.maxmc.maxhub.commands

import cn.maxmc.maxhub.*
import io.izzel.taboolib.module.command.base.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@BaseCommand(name = "maxhub")
class MaxHubCommand : BaseMainCommand(){

    @SubCommand(requiredPlayer = true, permission = "maxhub.setSpawn")
    val setSpawn = object : BaseSubCommand() {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            sender as Player
            settings["Base.spawnloc"] = sender.location.let {
                it.pitch = 0f
                it.yaw = 0f
                return@let it
            }
            settings.saveToFile()
            pSendTo(sender,"command.set_spawn", "${sender.location.x},${sender.location.y},${sender.location.z}")
        }
    }

    @SubCommand(requiredPlayer = true, permission = "maxhub.spawn")
    val spawn = object : BaseSubCommand() {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            (sender as Player).teleport(cn.maxmc.maxhub.spawn)
        }
    }

    @SubCommand(permission = "maxhub.enable")
    val enable = object : BaseSubCommand() {
        override fun getArguments(): Array<Argument> {
            return arrayOf(
                Argument("module", true) { ModuleManager.modules.map { it.javaClass.simpleName } }
            )
        }
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val module = ModuleManager.getModuleByName(args[0]) ?: return pSendTo(sender,"command.module_not_exist", args[0])
            if(module.isEnable) {
                pSendTo(sender, "command.module_already_enable", module.name)
                return
            }
            module.enable(MaxHub.plugin)
            pSendTo(sender, "command.module_enable", module.name)
        }
    }

    @SubCommand(permission = "maxhub.disable")
    val disable = object : BaseSubCommand() {
        override fun getArguments(): Array<Argument> {
            return arrayOf(
                Argument("module", true) { ModuleManager.modules.map { it.javaClass.simpleName } }
            )
        }
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val module = ModuleManager.getModuleByName(args[0]) ?: return pSendTo(sender,"command.module_not_exist", args[0])
            if(!module.isEnable) {
                pSendTo(sender, "command.module_already_disable", module.name)
                return
            }
            module.disable()
            pSendTo(sender, "command.module_disable", module.name)
        }
    }

    @SubCommand(permission = "maxhub.toggle")
    val toggle = object : BaseSubCommand() {
        override fun getArguments(): Array<Argument> {
            return arrayOf(
                Argument("module", true) { ModuleManager.modules.map { it.javaClass.simpleName } }
            )
        }
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val module = ModuleManager.getModuleByName(args[0]) ?: return pSendTo(sender,"command.module_not_exist", args[0])
            if(module.isEnable) {
                module.disable()
                pSendTo(sender, "command.module_disable", module.name)
            } else{
                module.enable(MaxHub.plugin)
                pSendTo(sender, "command.module_enable", module.name)
            }
        }
    }

}
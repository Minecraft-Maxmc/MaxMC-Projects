package cn.maxmc.maxhub.commands

import cn.maxmc.maxhub.*
import io.izzel.taboolib.module.command.base.*
import mkremins.fanciful.FancyMessage
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
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

    @SubCommand(permission = "maxhub.list")
    val list = object : BaseSubCommand() {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            pSendTo(sender,"command.list_module")
            ModuleManager.modules.forEach {
                val fancyMessage = FancyMessage(enableToColor(it.isEnable) + it.name)
                if(it.isEnable) {
                    fancyMessage
                        .then("[✗]")
                        .tooltip("command.module_disable_hover")
                        .command( "/maxhub toggle ${it.name}")
                } else {
                    fancyMessage
                        .then("[✔]")
                        .tooltip("command.module_enable_hover")
                        .command( "/maxhub toggle ${it.name}")
                }
                fancyMessage.send(sender)
            }
        }
    }

    @SubCommand(hideInHelp = true)
    val migrate = object : BaseSubCommand() {
        override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>) {
            settings.migrate()
            p0.sendMessage("§b已迁移")
        }
    }

    private fun enableToColor(enable: Boolean): String {
        return if(enable) "§a" else "§c"
    }
}
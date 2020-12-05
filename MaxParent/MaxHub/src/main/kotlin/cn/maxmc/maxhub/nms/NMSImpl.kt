package cn.maxmc.maxhub.nms

import io.izzel.taboolib.module.packet.TPacketHandler
import net.minecraft.server.v1_8_R3.*
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class NMSImpl: AbsNMS {
    override fun sendScoreBoard(player: Player, title: String, vararg lines: String) {
        val objectiveStop = PacketPlayOutScoreboardObjective()
        sendPacket(
            player,
            objectiveStop,
            "a" to "scoreBoard",
            "b" to ChatComponentText(title),
            "c" to IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER,
            "d" to 1
        )
        val objectiveStart = PacketPlayOutScoreboardObjective()
        sendPacket(
            player,
            objectiveStart,
            "a" to "scoreBoard",
            "b" to ChatComponentText(title),
            "c" to IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER,
            "d" to 0
        )
        val objectiveChange = PacketPlayOutScoreboardObjective()
        sendPacket(
            player,
            objectiveChange,
            "a" to "scoreBoard",
            "b" to ChatComponentText(title),
            "c" to IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER,
            "d" to 2
        )

        for (i in lines.size-1 downTo 0){
            val s = lines[i]
            sendPacket(
                player,
                PacketPlayOutScoreboardScore(),
                "a" to "scoreBoard",
                "b" to ChatColor.values()[i].toString()+"§r"+s,
                "c" to lines.size-1-i,
                "d" to PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE,
            )
        }
        val display = PacketPlayOutScoreboardDisplayObjective()
        sendPacket(
            player,
            display,
            "a" to 1,
            "b" to "scoreBoard"
        )
    }
}
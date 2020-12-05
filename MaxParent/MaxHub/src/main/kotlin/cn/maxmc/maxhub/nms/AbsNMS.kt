package cn.maxmc.maxhub.nms

import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.lite.SimpleReflection
import io.izzel.taboolib.module.packet.TPacketHandler
import org.bukkit.entity.Player

interface AbsNMS {
    companion object{
        @TInject(asm = "cn.maxmc.maxhub.nms.NMSImpl")
        lateinit var instance: AbsNMS
    }

    fun sendScoreBoard(player: Player,title: String,vararg lines: String)

    fun setFields(any: Any, vararg fields: Pair<String, Any>): Any {
        fields.forEach { (key, value) ->
            SimpleReflection.setFieldValue(any.javaClass, any, key, value, true)
        }
        return any
    }

    fun sendPacket(player: Player, packet: Any, vararg fields: Pair<String, Any>) {
        TPacketHandler.sendPacket(player, setFields(packet, *fields))
    }
}
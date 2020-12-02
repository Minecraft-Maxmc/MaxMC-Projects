package cn.maxmc.maxhub

import org.bukkit.Bukkit
import org.bukkit.Location

val spawn: Location
    get() {
        var location = settings.get("Base.spawnloc")
        if(location == null) {
            settings["Base.spawnloc"] = Bukkit.getWorlds()[0].spawnLocation
            location = Bukkit.getWorlds()[0].spawnLocation
            settings.saveToFile()
        }
        return location as Location
    }

val prefix
    get() = settings.getString("Base.prefix")!!
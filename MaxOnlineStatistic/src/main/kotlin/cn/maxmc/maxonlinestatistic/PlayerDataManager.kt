package cn.maxmc.maxonlinestatistic

import net.md_5.bungee.config.Configuration

object PlayerDataManager {
    init {
        Class.forName("com.mysql.jdbc.Driver")
    }

    val url = config.getString("database.url")
    val port = config.getInt("database.port")
    val username = config.getString("database.username")
    val password = config.getString("database.password")
    val database = config.getString("database.db")

    fun getConn() {

    }

    fun buildUrl(url: String, port: Int,database: String, profiles: Configuration?): String{
        val base = StringBuffer("jdbc:mysql://${url}:${port}/${database}")
        if(profiles == null || profiles.keys.isEmpty()) {
            return base.toString()
        }
        base.append("?")
        profiles.keys.forEach {
            base.append("${it}=${profiles.get(it)}&")
        }
        base.deleteCharAt(base.length-1)
        return base.toString()
    }
}
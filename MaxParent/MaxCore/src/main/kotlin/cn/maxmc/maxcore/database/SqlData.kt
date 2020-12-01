package cn.maxmc.maxcore.database

import cn.maxmc.maxcore.MaxCore
import cn.maxmc.maxcore.files.Settings
import io.izzel.taboolib.module.db.sql.SQLHost

class SqlData {
    val host = Settings.settings.getString("database.host")
    val port = Settings.settings.getString("database.port")
    val host = SQLHost("","3336","root","d8823577","maxmc",MaxCore.plugin)

}
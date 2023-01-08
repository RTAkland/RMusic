package cn.rtast.rmusic

import cn.rtast.rmusic.commands.RMusicCommand
import cn.rtast.rmusic.config.ConfigUtil
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier
import java.io.File

object RMusic : ModInitializer {
    val RMUSIC_PACKET_ID = Identifier("rmusic", "op")

    val API_ADDR_163 = ConfigUtil().getApiAddr163()

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            RMusicCommand().register(dispatcher)
        }

        val rmusicDir = File("./config/rmusic")
        if (!rmusicDir.exists()) {
            rmusicDir.mkdirs()
        }
    }
}
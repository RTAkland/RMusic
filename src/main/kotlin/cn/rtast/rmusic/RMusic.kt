/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic

import cn.rtast.rmusic.commands.RMusicCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier
import java.io.File

object RMusic : ModInitializer {
    val RMUSIC_PACKET_ID = Identifier("rmusic", "op")

    override fun onInitialize() {
        val dir = File("./config/rmusic/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            RMusicCommand().register(dispatcher)
        }
        println("server")
    }
}
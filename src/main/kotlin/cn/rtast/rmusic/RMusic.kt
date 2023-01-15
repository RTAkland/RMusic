/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic

import cn.rtast.rmusic.commands.RMusicCommand
import cn.rtast.rmusic.utils.ConfigUtil
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier

object RMusic : ModInitializer {
    val RMUSIC_PACKET_ID = Identifier("rmusic", "op")
    var API_URL_163 = ConfigUtil().get163URL()

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            RMusicCommand().register(dispatcher)
        }
        println("RMusic Startup! Powered by RTAkland")
        println("Github: https://github.com/RTAkland")
        println("Mod Site: https://mod.rtast.cn/RMusic")
    }
}
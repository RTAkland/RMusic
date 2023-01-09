package cn.rtast.rmusic

import cn.rtast.rmusic.commands.RMusicCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback

class RMusic : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            RMusicCommand().register(dispatcher)
        }
    }
}
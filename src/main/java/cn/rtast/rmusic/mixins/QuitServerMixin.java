/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.mixins;

import cn.rtast.rmusic.RMusic;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class QuitServerMixin {

    @Inject(method = "disconnect()V", at = @At("TAIL"))
    public void disconnectMixin(CallbackInfo ci) {
        RMusic.Companion.getPlayer().stop();
    }
}

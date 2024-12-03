/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.mixins;

import cn.rtast.rmusic.RMusic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler {

    @Shadow public abstract boolean isPaused();

    public MinecraftClientMixin(String string) {
        super(string);
    }

    @Unique
    public MinecraftClient client = (MinecraftClient) (Object) this;

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    public void onDisconnectedMixin(CallbackInfo ci) {
        RMusic.Companion.getPlayer().stop();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickMixin(CallbackInfo ci) {
        if (isPaused() && Objects.requireNonNull(RMusic.Companion.getConfigManager().getConfig()).getAutoPause()) {
            RMusic.Companion.getPlayer().pause();
        } else {
            RMusic.Companion.getPlayer().resume();
        }
    }
}

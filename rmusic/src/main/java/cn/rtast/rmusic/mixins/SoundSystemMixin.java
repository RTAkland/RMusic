/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.mixins;

import cn.rtast.rmusic.RMusicClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {

    @Unique
    public SoundCategory currentCategory;

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    public void play(SoundInstance soundInstance, CallbackInfo ci) {
        currentCategory = soundInstance.getCategory();
        if (RMusicClient.Companion.getPlayer().isPlaying()) {
            if (soundInstance.getCategory() == SoundCategory.MUSIC) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "updateSoundVolume", at = @At("HEAD"))
    public void updateSoundVolume(SoundCategory category, float volume, CallbackInfo ci) {
        if (category == SoundCategory.RECORDS) {
            if (volume == 0.0f) {
                RMusicClient.Companion.getPlayer().setMute(true);
            } else {
                RMusicClient.Companion.getPlayer().setMute(false);
                RMusicClient.Companion.getPlayer().setGain(volume);
            }
        }
    }
    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (RMusicClient.Companion.getPlayer().isPlaying()) {
            if (currentCategory == SoundCategory.MUSIC) {
                ci.cancel();
            }
        }
    }
}

/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.mixins;

import cn.rtast.rmusic.RMusic;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @Unique
    public SoundCategory currentCategory;

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    public void play(SoundInstance soundInstance, CallbackInfo ci) {
        currentCategory = soundInstance.getCategory();
        if (RMusic.Companion.getPlayer().isPlaying()) {
            if (soundInstance.getCategory() == SoundCategory.MUSIC) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "updateSoundVolume", at = @At("HEAD"))
    public void updateSoundVolume(SoundCategory category, float volume, CallbackInfo ci) {
        if (category == SoundCategory.RECORDS) {
            if (volume == 0.0f) {
                RMusic.Companion.getPlayer().setMute(true);
            } else {
                RMusic.Companion.getPlayer().setMute(false);
                RMusic.Companion.getPlayer().setGain(volume);
            }
        }
    }
    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (RMusic.Companion.getPlayer().isPlaying()) {
            if (currentCategory == SoundCategory.MUSIC) {
                ci.cancel();
            }
        }
    }
}

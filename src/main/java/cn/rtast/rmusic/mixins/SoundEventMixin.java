package cn.rtast.rmusic.mixins;

import cn.rtast.rmusic.client.RMusicClient;
import com.goxr3plus.streamplayer.enums.Status;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundEventMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    public void sound(SoundInstance sound, CallbackInfo ci) {
        if (RMusicClient.INSTANCE.getPlayer() != null) {
            if (RMusicClient.INSTANCE.getPlayer().getStatus() == Status.PLAYING) {
                SoundCategory category = sound.getCategory();
                if (category == SoundCategory.MUSIC) {
                    ci.cancel();  // 如果音乐播放器正在播放音乐取消播放背景音乐
                }
            }
        }
    }
}
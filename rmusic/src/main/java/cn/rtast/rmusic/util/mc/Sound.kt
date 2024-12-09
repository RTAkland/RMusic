/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/6
 */


package cn.rtast.rmusic.util.mc

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

fun ServerPlayerEntity.playButtonSound() {
    this.playSoundToPlayer(SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 1f, 1.2f)
}

fun ServerPlayerEntity.playLoginSuccess() {
    this.playSoundToPlayer(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1f, 1.6f)
}
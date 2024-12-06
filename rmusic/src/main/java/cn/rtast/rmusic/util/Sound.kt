/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/6
 */


package cn.rtast.rmusic.util

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvents

fun ServerPlayerEntity.playButtonSound() {
    this.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f, 1f)
}

fun ServerPlayerEntity.playPLing() {
    this.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1f, 1f)
}
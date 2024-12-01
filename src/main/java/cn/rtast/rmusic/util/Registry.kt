/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.defaultCoverId
import cn.rtast.rmusic.minecraftClient
import cn.rtast.rmusic.qrcodeId
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import java.io.File
import kotlin.concurrent.thread
import kotlin.io.path.pathString


fun registerQRCode(qrCode: ByteArray) {
    thread {
        val file =
            File(FabricLoader.getInstance().gameDir.pathString + "/cloud_music_qrcode.png").apply { writeBytes(qrCode.toPNG()) }
        val img = NativeImage.read(file.inputStream())
        val textureImage = NativeImageBackedTexture(img)
        minecraftClient.execute { minecraftClient.textureManager.registerTexture(qrcodeId, textureImage) }
    }
}

fun registerSongCover(cover: ByteArray) {
    thread {
        val img = NativeImage.read(cover.toPNG())
        val textureImage = NativeImageBackedTexture(img)
        minecraftClient.execute { minecraftClient.textureManager.registerTexture(defaultCoverId, textureImage) }
    }
}
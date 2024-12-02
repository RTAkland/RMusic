/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.defaultCoverId
import cn.rtast.rmusic.entity.ncm.SongDetail
import cn.rtast.rmusic.minecraftClient
import cn.rtast.rmusic.qrcodeId
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.text.Text
import net.minecraft.util.Identifier


var loadQRCode = true
var loadCover = true
var loadSongDetail = true

private val renderLayer = RenderLayer::getGuiTexturedOverlay

private fun registerTexture(textureResource: ByteArray, id: Identifier) {
    destroyTexture(qrcodeId)
    val nativeImage = NativeImage.read(textureResource)
    val nativeImageTexture = NativeImageBackedTexture(nativeImage)
    minecraftClient.execute { minecraftClient.textureManager.registerTexture(id, nativeImageTexture) }
}

fun renderQRCode(qrcode: ByteArray) {
    registerTexture(qrcode, qrcodeId)
    loadQRCode = true
    HudRenderCallback.EVENT.register { context, tickDeltaManager ->
        if (!loadQRCode) return@register
        context.drawTexture(renderLayer, qrcodeId, 0, 50, 0f, 0f, 96, 96, 96, 96)
    }
}

fun renderCover(cover: ByteArray) {
    loadCover = true
    HudRenderCallback.EVENT.register { context, tickDeltaManager ->
        if (!loadCover) return@register
        context.drawTexture(renderLayer, defaultCoverId, 5, 20, 0f, 0f, 48, 48, 48, 48)
    }
    registerTexture(cover, defaultCoverId)
}

fun destroyTexture(id: Identifier) {
    minecraftClient.execute { minecraftClient.textureManager.destroyTexture(id) }
}

fun renderSongDetail(detail: SongDetail) {
    loadSongDetail = true
    HudRenderCallback.EVENT.register { context, tickDeltaManager ->
        if (!loadSongDetail) return@register
        context.drawText(
            minecraftClient.textRenderer,
            Text.literal("${detail.name} - 《${detail.artists}》"),
            5, 80, 0xFFFF00, true
        )
    }
}
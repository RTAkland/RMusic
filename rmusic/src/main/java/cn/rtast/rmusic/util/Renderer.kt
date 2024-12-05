/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/2
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.RMusicClient
import cn.rtast.rmusic.cacheDir
import cn.rtast.rmusic.defaultCoverId
import cn.rtast.rmusic.entity.ncm.SongDetail
import cn.rtast.rmusic.enums.LyricPosition
import cn.rtast.rmusic.qrcodeId
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.joml.Quaternionf
import java.io.File
import java.net.URI
import kotlin.math.sin

object Renderer {
    var loadQRCode = false
    var loadCover = false
    var loadSongDetail = false
    var songInfo: SongDetail? = null
    var loadLyric = false
    var loadCurrentLyric = ""

    private val renderLayer = RenderLayer::getGuiTexturedOverlay
    private val minecraftClient: MinecraftClient = MinecraftClient.getInstance()

    init {
        var colorCycle = 0f
        val colorChangeSpeed = 0.01f
        var rotationAngle = 0f
        HudRenderCallback.EVENT.register { context, _ ->
            if (!loadLyric) return@register
            if (RMusicClient.configManager.config?.position == LyricPosition.TopLeft) {
                val lyric = Text.literal(loadCurrentLyric)
                    .styled { it.withColor(Formatting.YELLOW) }
                context.drawText(minecraftClient.textRenderer, lyric, 5, 103, 0xffffff, true)
            }
        }

        HudRenderCallback.EVENT.register { context, tickDeltaManager ->
            if (!loadSongDetail) return@register
            colorCycle += colorChangeSpeed * tickDeltaManager.lastDuration
            if (colorCycle > 1f) colorCycle = 0f
            val red = (sin(colorCycle * Math.PI * 2) * 0.5 + 0.5) * 255
            val green = (sin((colorCycle + 0.33) * Math.PI * 2) * 0.5 + 0.5) * 255
            val blue = (sin((colorCycle + 0.66) * Math.PI * 2) * 0.5 + 0.5) * 255
            val color = ((255 shl 24) or (red.toInt() shl 16) or (green.toInt() shl 8) or blue.toInt())
            context.drawText(
                minecraftClient.textRenderer,
                Text.literal("↓ 正在播放 ↓"),
                5, 73, color, true
            )
            context.drawText(
                minecraftClient.textRenderer,
                Text.literal("《${songInfo?.name}》"),
                5, 83, color, true
            )
            context.drawText(
                minecraftClient.textRenderer,
                Text.literal(songInfo?.artists),
                5, 93, color, true
            )
        }

        HudRenderCallback.EVENT.register { context, _ ->
            if (!loadCover) return@register
            rotationAngle += 1f
            if (rotationAngle >= 360f) rotationAngle = 0f
            val matrix = context.matrices
            matrix.push()
            matrix.translate(5f + 24f, 20f + 24f, 0f)
            val quaternion = Quaternionf().apply {
                rotateZ(Math.toRadians(rotationAngle.toDouble()).toFloat())
            }
            matrix.multiply(quaternion)
            matrix.translate(-(5f + 24f), -(20f + 24f), 0f)
            context.drawTexture(
                renderLayer, defaultCoverId, 5,
                20, 0f, 0f, 48,
                48, 48, 48
            )
            matrix.pop()
        }

        HudRenderCallback.EVENT.register { context, _ ->
            if (!loadQRCode) return@register
            context.drawTexture(renderLayer, qrcodeId, 0, 50, 0f, 0f, 80, 80, 80, 80)
        }
    }

    /**
     * 动态注册材质到注册表内
     */
    private fun registerTexture(textureResource: ByteArray, id: Identifier) {
        destroyTexture(qrcodeId)
        val nativeImage = NativeImage.read(textureResource)
        val nativeImageTexture = NativeImageBackedTexture(nativeImage)
        minecraftClient.execute { minecraftClient.textureManager.registerTexture(id, nativeImageTexture) }
    }

    /**
     * 销毁资源材质
     */
    fun destroyTexture(id: Identifier) {
        minecraftClient.execute { minecraftClient.textureManager.destroyTexture(id) }
    }

    /**
     * 渲染歌词(左上角)
     */
    fun renderLyric() {
        loadLyric = true
    }

    /**
     * 渲染歌曲详细信息
     */
    fun renderSongDetail() {
        loadSongDetail = true
    }

    /**
     * 渲染二维码到游戏界面内
     */
    fun renderQRCode(qrcode: ByteArray) {
        loadQRCode = true
        registerTexture(qrcode, qrcodeId)
    }

    /**
     * 渲染专辑封面到游戏界面内
     * 在专辑封面加载的时候先使用默认的
     * loading.png渲染加载完成后再
     * 使用下载好的专辑封面渲染到游戏内
     */
    fun renderCover(songDetail: SongDetail) {
        loadCover = true
        val cachedCover = File(cacheDir, "${songDetail.id}.png")
        val cover = if (cachedCover.exists()) cachedCover.readBytes() else {
            val coverBytes = URI(songDetail.cover + "?param=128y128")
                .toURL().readBytes().toPNG().cropToCircle()
                .toBufferedImage().createRecordImage().toByteArray()
            cachedCover.writeBytes(coverBytes)
            coverBytes
        }
        registerTexture(cover, defaultCoverId)
    }

    fun registerLoadingCover() {
        minecraftClient.textureManager.registerTexture(
            defaultCoverId, minecraftClient.textureManager.getTexture(defaultCoverId)
        )
    }
}
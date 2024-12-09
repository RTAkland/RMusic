/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/9
 */


package cn.rtast.rmusic.enums

import com.mojang.authlib.properties.Property
import com.mojang.authlib.properties.PropertyMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ProfileComponent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import java.util.*

enum class PlayerHead(val texture: String) {
    K("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA3ZjNiNjQ3YWMyNTM2YmRmZjgzNDgyZjgzYjQyYmRlMTE0OGY4ZmMyNzUwMTBhN2MzMTQ2NjMwOWZkMzUwIn19fQ=="),
    Q("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ExM2VhOWEyMDcwNTBjMjE0MThlYTg1YmIxZDgzOTZiNTVhMTkyMzRjYmRmNTgyNGI0MzczYzIzNzJkYmUxIn19fQ=="),
    W("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODViMmE5NWU0NGUxMTMxMjJlYmNkMGU3MzdlZGNhOGJiNGIyMThlMWU2N2FlNDhhNjQ2Njk3Y2U5ZDc0NWIxIn19fQ=="),
    BLACK_LEFT_ARROW("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY3ZGFkZjEwNjNiNGQ0NDE5ZWQ0YTVkOTAwNDU1NzU0OTg4YjIzNDE4YmFkOGNjYTJiZjc5NTBjMzA3MGFiZiJ9fX0=");

    companion object {
        fun PlayerHead.forItem(): ItemStack {
            val head = Items.PLAYER_HEAD.defaultStack
            val propertyMap = PropertyMap()
            propertyMap.put("textures", Property("textures", this.texture))
            head.set(
                DataComponentTypes.PROFILE,
                ProfileComponent(
                    Optional.empty(),
                    Optional.empty(),
                    propertyMap
                )
            )
            return head
        }
    }
}
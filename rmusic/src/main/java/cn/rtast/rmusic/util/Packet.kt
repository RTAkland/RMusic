/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.util

import cn.rtast.rmusic.entity.payload.ActionPacket
import cn.rtast.rmusic.enums.Action
import cn.rtast.rmusic.util.str.encodeToBase64
import cn.rtast.rmusic.util.str.toJson

fun createActionPacket(action: Action, payload: String): String {
    return ActionPacket(action, payload.encodeToBase64()).toJson()
}
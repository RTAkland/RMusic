/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.entity.payload

import cn.rtast.rmusic.enums.IntentAction

data class ActionPacket(
    val action: IntentAction,
    val body: String,
)
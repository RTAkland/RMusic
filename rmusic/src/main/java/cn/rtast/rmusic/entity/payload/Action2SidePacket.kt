/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.entity.payload

import cn.rtast.rmusic.enums.DispatchAction
import cn.rtast.rmusic.enums.InboundAction

data class Action2SidePacket(
    val action: DispatchAction,
    val body: String
)

data class ActionInboundPacket(
    val action: InboundAction,
    val body:
)
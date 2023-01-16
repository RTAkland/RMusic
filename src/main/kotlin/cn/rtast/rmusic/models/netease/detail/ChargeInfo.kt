/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.detail

data class ChargeInfo(
    val chargeMessage: Any,
    val chargeType: Long,
    val chargeUrl: Any,
    val rate: Long
)
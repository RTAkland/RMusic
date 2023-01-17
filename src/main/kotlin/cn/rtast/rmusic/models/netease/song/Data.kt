/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.song

data class Data(
    val br: Long,
    val canExtend: Boolean,
    val code: Int,
    val effectTypes: Any,
    val encodeType: String,
    val expi: Long,
    val fee: Long,
    val flag: Long,
    val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
    val freeTrialInfo: Any,
    val freeTrialPrivilege: FreeTrialPrivilege,
    val gain: Double,
    val id: Long,
    val level: String,
    val md5: String,
    val payed: Long,
    val peak: Any,
    val podcastCtrp: Any,
    val rightSource: Long,
    val size: Long,
    val time: Long,
    val type: String,
    val uf: Any,
    val url: String,
    val urlSource: Long
)
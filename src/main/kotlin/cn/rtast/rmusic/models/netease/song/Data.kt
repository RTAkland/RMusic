/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.song

data class Data(
    val br: Int,
    val canExtend: Boolean,
    val code: Int,
    val effectTypes: Any,
    val encodeType: String,
    val expi: Int,
    val fee: Int,
    val flag: Int,
    val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
    val freeTrialInfo: Any,
    val freeTrialPrivilege: FreeTrialPrivilege,
    val gain: Double,
    val id: Int,
    val level: String,
    val md5: String,
    val payed: Int,
    val peak: Any,
    val podcastCtrp: Any,
    val rightSource: Int,
    val size: Int,
    val time: Int,
    val type: String,
    val uf: Any,
    val url: String,
    val urlSource: Int
)
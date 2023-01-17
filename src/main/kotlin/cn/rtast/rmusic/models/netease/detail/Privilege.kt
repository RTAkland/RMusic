/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/15 23:11
 */

package cn.rtast.rmusic.models.netease.detail

data class Privilege(
    val chargeInfoList: List<ChargeInfo>,
    val cp: Long,
    val cs: Boolean,
    val dl: Long,
    val dlLevel: String,
    val downloadMaxBrLevel: String,
    val downloadMaxbr: Long,
    val fee: Long,
    val fl: Long,
    val flLevel: String,
    val flag: Long,
    val freeTrialPrivilege: FreeTrialPrivilege,
    val id: Long,
    val maxBrLevel: String,
    val maxbr: Long,
    val payed: Long,
    val pl: Long,
    val plLevel: String,
    val playMaxBrLevel: String,
    val playMaxbr: Long,
    val preSell: Boolean,
    val rscl: Any,
    val sp: Long,
    val st: Long,
    val subp: Long,
    val toast: Boolean
)
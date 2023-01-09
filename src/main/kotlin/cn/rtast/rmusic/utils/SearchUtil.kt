/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/9 12:26
 */

package cn.rtast.rmusic.utils

import cn.rtast.rmusic.music.Music163

class SearchUtil {
    fun searchNetease(keyword: String): MutableList<String> {
        val lst: MutableList<String> = mutableListOf()
        val result = Music163().search(keyword)
        for (s in result) {
            var temp = ""
            temp += "${s.name}|"
            for (ar in s.artists) {
                temp += "${ar.name} "
            }
            temp += "|${s.id}"
            lst.add(temp)
        }
        return lst
    }
}
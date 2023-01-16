/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/16 16:51
 */

package cn.rtast.rmusic.test

import java.util.*

class Utils {
    fun genRandom(length: Int = 20): String {
        val chars = "abcdefghijklmnopqrstuvwxyz"
        var result = ""
        (0..length).forEach { _ ->
            result += chars[Random().nextInt(0, 25)]
        }
        return result
    }

    fun genEmail(): String {
        val domains = listOf(
            ".org", ".com", ".cn", ".io", ".cf", ".tk", ".xxx",
            ".tv", ".me", ".in", ".net", ".wang", ".tech", ".asia",
            ".space", ".fun", ".top", ".club", ".xyz", ".ltd", ".ink",
            ".kim", ".games", ".co", ".cc", ".vip", ".sale", ".cloud",
            ".media", ".ren", ".shop", ".link", ".studio", ".law", ".fyi",
            ".cab", ".news", ".market", ".vin", ".shopping", ".mba", ".cafe",
            ".fans", ".technology", ".group", ".site", ".icu", ".art", ".pro",
            ".info", ".online", ".mobi", ".jp", ".eu", ".fashion", ".yoga",
            ".luxe", ".love", ".store", ".run", ".pub", ".chat", ".red", ".city",
            ".blue", ".ski", ".pink"
        )

        val names = listOf(
            "Markus", "Johan", "Maria", "lily", "Shitbro", "1145141919810",
            "Robot", "guox", "QWDA", "insolitum", "BobMowzie", "JamiesWhiteShirt",
            "Ladysnake", "hartl3y94", "lllziyu98", "twoone-3", "Juuxel", "emilyploszaj",
            "GreedySky", "DunamicSky", "ZainCheung", "AstralOrdana", "arcxinggye",
            "Fuzss", "MrCrayfish", "MehVahdJukaar", "BraveUX", "journey", "johnnie",
            "chitosai", "luoye663", "halo", "innnky", "Raphael", "StevenTey"
        )
        val name = names[Random().nextInt(0, names.size - 1)]
        val domain = domains[Random().nextInt(0, domains.size - 1)]

        return "$name@$name$domain"
    }
}
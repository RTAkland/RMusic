/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/11
 */


package cn.rtast.rmusic.util.music

import cn.rtast.rmusic.util.Http

object Bilibili {
    private const val COOKIE = "buvid3=E8800E3B-FE67-AD42-6E22-2C37A1E76B9883712infoc; b_nut=1729757483; _uuid=654510A89-65B5-76F7-1F1D-EC6D1054E422784400infoc; buvid_fp=fc39660745d8331133fbbda3aaadbedc; enable_web_push=DISABLE; home_feed_column=5; buvid4=CF90F204-CF66-4FC2-A25A-5A11B2FF3B3685365-024102408-ZYL3o3%2BiZQ5ShwVhxhJxsg%3D%3D; header_theme_version=CLOSE; DedeUserID=355265740; DedeUserID__ckMd5=3da1cd3b87b31703; rpdid=|(YYY~)~|JY0J'u~J|||uJuR; CURRENT_QUALITY=80; is-2022-channel=1; fingerprint=0799706f582e729dca42d1457d664c9e; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MzM5MDQyOTAsImlhdCI6MTczMzY0NTAzMCwicGx0IjotMX0.F-EcBPjOOLlkjvH1Xx_fzp-I9vrvIONpd2FbMLXt5KA; bili_ticket_expires=1733904230; SESSDATA=e05f86d0%2C1749197091%2C35b00%2Ac1CjC3jFsGWBuG7DlhX8Dcc7wJmVXpQDZqZaRGGeeLhfNiKkKZe904UXxBWRbJqfGy8nwSVi02VUI3X2lDeE1xV2dHVHNIaGgxTmltaDBNd3Z5cS1vTGVuMWg3Q1lQTWxQUTRoUm9XWHJBb2YwNW9EWVU0N2VYT05TVUt3VVp2aWRodlF2OFJucmpnIIEC; bili_jct=301b9228fc2221f0fd5ae17fb866bb2b; bmg_af_switch=1; bmg_src_def_domain=i2.hdslb.com; bp_t_offset_355265740=1009229538313371648; sid=86j8nfn0; b_lsid=C4DA5BF7_193B05354EE; CURRENT_FNVAL=4048; browser_resolution=1536-282"
    private const val SEARCH_API = "https://api.bilibili.com/x/web-interface/wbi/search/all/v2"

    private inline fun <reified T> get(endpoint: String, params: Map<String, Any>? = null) :T {
        val header = mapOf("cookie" to COOKIE)
        return Http.get<T>(endpoint, params = params, headers = header)
    }

    fun search(keyword: String) {
        val result = this.get<String>(SEARCH_API, mapOf("keyword" to keyword))
    }
}
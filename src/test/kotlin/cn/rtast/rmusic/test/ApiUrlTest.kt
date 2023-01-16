/**
 * @Author: RTAkland
 * @EMail: rtakland@outlook.com
 * @Date: 2023/1/16 16:33
 */

package cn.rtast.rmusic.test

import cn.rtast.rmusic.utils.ConfigUtil
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.jupiter.api.Test
import java.net.URL


class ApiUrlTest {

    @Test
    fun testConfigUrl() {
        val url = ConfigUtil().get163URL()
        assertEquals(url, "https://api.163.rtast.cn")
    }

    @Test
    fun testUrlConn() {
        val url = ConfigUtil().get163URL()
        val result = URL(url)
        assertNotNull(result)
    }

    @Test
    fun testConfigWrite() {
        val rndUrl = Utils().genRandom()
        ConfigUtil().set163URL(rndUrl)
        val url = ConfigUtil().get163URL()
        assertEquals(url, rndUrl)
    }
}
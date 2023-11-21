/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package cn.rtast.rmusic.utils.logger

import cn.rtast.rmusic.enums.logger.Level
import cn.rtast.rmusic.utils.TimeUtil

class ConsoleLogger : RLogger {

    private var level: Level? = null
    private val timeUtil = TimeUtil()


    override fun setLogLevel(level: Level) {
        this.level = level
    }

    override fun log(level: Level, message: String) {
        if (this.level?.level!! >= level.level) {
            val currentTime = this.timeUtil.getCurrentDate().toString()
            println("[$currentTime]+[${this.javaClass}]:$message")
        }
    }

    override fun debug(message: String) {
        this.log(Level.DEBUG, message)
    }

    override fun info(message: String) {
        this.log(Level.INFO, message)
    }

    override fun warn(message: String) {
        this.log(Level.WARN, message)
    }

    override fun error(message: String) {
        this.log(Level.ERROR, message)
    }
}
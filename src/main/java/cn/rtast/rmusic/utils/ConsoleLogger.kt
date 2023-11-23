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


package cn.rtast.rmusic.utils

import cn.rtast.rmusic.enums.Level

class ConsoleLogger {
    private var level: Level = Level.DEBUG
    private val timeUtil: TimeUtil = TimeUtil()

    fun setLogLevel(level: Level) {
        this.level = level
    }

    private fun getCallMethod(): String {
        return Throwable().stackTrace[2].methodName
    }

    private fun getCallClass(): String {
        return Throwable().stackTrace[2].className
    }

    private fun getCallLine(): Int {
        return Throwable().stackTrace[2].lineNumber
    }

    private fun log(level: Level, message: Any?) {
        if (this.level.level >= level.level) {
            val currentTime = this.timeUtil.getCurrentDate().toString()
            println("${level.color}$currentTime+[${this.getCallClass()}:${this.getCallMethod()}:${this.getCallLine()}]:$message")
        }
    }

    fun debug(message: Any?) {
        this.log(Level.DEBUG, message)
    }

    fun info(message: Any?) {
        this.log(Level.INFO, message)
    }

    fun warn(message: Any?) {
        this.log(Level.WARN, message)
    }

    fun error(message: Any?) {
        this.log(Level.ERROR, message)
    }
}
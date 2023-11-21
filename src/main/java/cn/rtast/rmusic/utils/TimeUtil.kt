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

import cn.rtast.rmusic.models.TimeModel
import java.time.LocalDateTime

class TimeUtil {

    fun getCurrentDate(): TimeModel {
        val currentDateTime = LocalDateTime.now()

        val year = currentDateTime.year
        val month = currentDateTime.monthValue
        val day = currentDateTime.dayOfMonth
        val hour = currentDateTime.hour
        val minute = currentDateTime.minute
        val second = currentDateTime.second

        return TimeModel(year, month, day, hour, minute, second)
    }
}
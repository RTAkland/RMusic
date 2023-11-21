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


package cn.rtast.rmusic.models


data class TimeModel(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
) {
    private fun isValidYear(): Boolean {
        return year in 1..9999
    }

    private fun isValidMonth(): Boolean {
        return month in 1..12
    }

    private fun isValidDay(): Boolean {
        val maxDays = when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        return day in 1..maxDays
    }

    private fun isValidHour(): Boolean {
        return hour in 0..23
    }

    private fun isValidMinute(): Boolean {
        return minute in 0..59
    }

    private fun isValidSecond(): Boolean {
        return second in 0..59
    }

    fun validateDateTime(): Boolean {
        return isValidYear() && isValidMonth() && isValidDay() && isValidHour() && isValidMinute() && isValidSecond()
    }

    override fun toString(): String {
        return "[$year-$month-$day+$hour:$minute:$second]"
    }
}
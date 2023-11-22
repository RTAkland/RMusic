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


package cn.rtast.rmusic.enums

import cn.rtast.rmusic.GREEN_ANSI_PREFIX
import cn.rtast.rmusic.RED_ANSI_PREFIX
import cn.rtast.rmusic.WHITE_ANSI_PREFIX
import cn.rtast.rmusic.YELLOW_ANSI_PREFIX

enum class Level(val level: Int, val color: String) {
    DEBUG(3, WHITE_ANSI_PREFIX),
    INFO(2, GREEN_ANSI_PREFIX),
    WARN(1, YELLOW_ANSI_PREFIX),
    ERROR(0, RED_ANSI_PREFIX)
}
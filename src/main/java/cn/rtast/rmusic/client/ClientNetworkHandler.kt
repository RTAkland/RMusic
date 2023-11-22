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
package cn.rtast.rmusic.client

import cn.rtast.rmusic.RMusic
import cn.rtast.rmusic.utils.decompress
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.PacketByteBuf

class ClientNetworkHandler {

    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(RMusic.RNetworkChannel) {
                _,
                _, buf: PacketByteBuf,
                _,
            ->
            val ogByteArray = buf.readByteArray()
            val decompressed = decompress(ogByteArray)
            this.onPacket(decompressed)
        }
    }

    private fun onPacket(packet: String) {

    }
}

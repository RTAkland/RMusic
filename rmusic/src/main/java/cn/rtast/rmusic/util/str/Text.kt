/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/5
 */


package cn.rtast.rmusic.util.str

import net.minecraft.text.Text
import java.util.function.Supplier

fun Text.supplier(): Supplier<Text> {
    return Supplier { this }
}
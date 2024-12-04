/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/4
 */


package cn.rtast.rmusic.util

import net.minecraft.text.Text
import java.util.function.Supplier

fun Text.createSupplierText(): Supplier<Text> {
    return Supplier { this }
}
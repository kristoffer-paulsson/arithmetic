package org.example.arithmetic

public class BitInputBuffer(private val data: ByteArray) {
    private var bytePos = 0
    private var bitPos = 0

    public fun readBit(): Int {
        if (bytePos >= data.size) return 0
        val bit = (data[bytePos].toInt() shr (7 - bitPos)) and 1
        bitPos++
        if (bitPos == 8) {
            bitPos = 0
            bytePos++
        }
        return bit
    }
}
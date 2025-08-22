package org.example.arithmetic

public class BitOutputBuffer(size: Int) {
    private val bytes = ByteArray(size)
    private var byteIndex = 0
    private var currentByte = 0
    private var numBitsFilled = 0

    public fun writeBit(bit: Int) {
        if (byteIndex >= bytes.size) return // Ignore if buffer is full
        currentByte = currentByte shl 1 or (bit and 1)
        numBitsFilled++
        if (numBitsFilled == 8) {
            bytes[byteIndex++] = currentByte.toByte()
            numBitsFilled = 0
            currentByte = 0
        }
    }

    public fun toByteArray(): ByteArray {
        var length = byteIndex
        if (numBitsFilled > 0 && byteIndex < bytes.size) {
            bytes[byteIndex++] = (currentByte shl (8 - numBitsFilled)).toByte()
            length = byteIndex
        }
        return bytes.copyOf(length)
    }
}
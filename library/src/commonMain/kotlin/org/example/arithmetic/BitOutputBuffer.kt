package org.example.arithmetic

public class BitOutputBuffer {
    private val bytes = mutableListOf<Byte>()
    private var currentByte = 0
    private var numBitsFilled = 0

    public fun writeBit(bit: Int) {
        currentByte = currentByte shl 1 or bit
        numBitsFilled++
        if (numBitsFilled == 8) {
            bytes.add(currentByte.toByte())
            numBitsFilled = 0
            currentByte = 0
        }
    }

    public fun toByteArray(): ByteArray {
        if (numBitsFilled > 0) {
            bytes.add((currentByte shl (8 - numBitsFilled)).toByte())
        }
        return bytes.toByteArray()
    }
}
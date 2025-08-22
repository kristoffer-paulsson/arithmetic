package org.example.arithmetic

public class ArithmeticEncoder(private val precision: Int = 32) {
    private val fullRange = (1L shl precision) - 1
    private val halfRange = 1L shl (precision - 1)
    private val quarterRange = halfRange shr 1

    private var low: Long = 0
    private var high: Long = fullRange
    private var underflow = 0

    private val output = BitOutputBuffer()

    public fun encodeSymbol(symbol: Int, freqs: FrequencyTable) {
        val total = freqs.getTotal()
        val lowCount = freqs.getLow(symbol)
        val highCount = freqs.getHigh(symbol)

        val range = high - low + 1
        high = low + (range * highCount / total) - 1
        low = low + (range * lowCount / total)

        while (true) {
            when {
                high < halfRange -> {
                    output.writeBit(0)
                    repeat(underflow) { output.writeBit(1) }
                    underflow = 0
                }
                low >= halfRange -> {
                    output.writeBit(1)
                    repeat(underflow) { output.writeBit(0) }
                    underflow = 0
                    low -= halfRange
                    high -= halfRange
                }
                low >= quarterRange && high < 3 * quarterRange -> {
                    underflow++
                    low -= quarterRange
                    high -= quarterRange
                }
                else -> break
            }

            low = low shl 1 and fullRange
            high = (high shl 1 and fullRange) or 1
        }
    }

    public fun finish() : ByteArray {
        underflow++
        if (low < quarterRange) {
            output.writeBit(0)
            repeat(underflow) { output.writeBit(1) }
        } else {
            output.writeBit(1)
            repeat(underflow) { output.writeBit(0) }
        }
        return output.toByteArray()
    }
}
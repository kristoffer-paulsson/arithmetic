package org.example.arithmetic

public class ArithmeticDecoder(private val precision: Int = 32, private val input: BitInputBuffer) {
    private val fullRange = (1L shl precision) - 1
    private val halfRange = 1L shl (precision - 1)
    private val quarterRange = halfRange shr 1

    private var low: Long = 0
    private var high: Long = fullRange
    private var code: Long = 0

    init {
        repeat(precision) {
            code = (code shl 1) or input.readBit().toLong()
        }
    }

    public fun readSymbol(freqs: FrequencyTable): Int {
        val total = freqs.getTotal()
        val range = high - low + 1
        val offset = code - low
        val value = ((offset + 1) * total - 1) / range

        var symbol = 0
        for (i in 0 until freqs.getSymbolLimit()) {
            if (freqs.getLow(i) <= value && value < freqs.getHigh(i)) {
                symbol = i
                break
            }
        }

        val lowCount = freqs.getLow(symbol)
        val highCount = freqs.getHigh(symbol)
        high = low + (range * highCount / total) - 1
        low = low + (range * lowCount / total)

        while (true) {
            when {
                high < halfRange -> {
                    // do nothing
                }
                low >= halfRange -> {
                    code -= halfRange
                    low -= halfRange
                    high -= halfRange
                }
                low >= quarterRange && high < 3 * quarterRange -> {
                    code -= quarterRange
                    low -= quarterRange
                    high -= quarterRange
                }
                else -> break
            }

            low = low shl 1 and fullRange
            high = (high shl 1 and fullRange) or 1
            code = (code shl 1 and fullRange) or input.readBit().toLong()
        }

        return symbol
    }
}
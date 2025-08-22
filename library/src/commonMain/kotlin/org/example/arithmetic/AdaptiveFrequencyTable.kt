package org.example.arithmetic

public class AdaptiveFrequencyTable(private val limit: Int = 257) : FrequencyTable {
    private val freq = ByteArray(limit) { 1 }
    private val cumFreq = IntArray(limit + 1)

    init {
        rebuildCumulative()
    }

    private fun rebuildCumulative() {
        var sum = 0
        for (i in 0 until limit) {
            cumFreq[i] = sum
            sum += freq[i].toInt() and 0xFF
            if (sum > 255) sum = 255
        }
        cumFreq[limit] = sum
    }

    override fun getSymbolLimit(): Int = limit

    override fun getTotal(): Int = cumFreq[limit]

    override fun getLow(symbol: Int): Int = cumFreq[symbol]

    override fun getHigh(symbol: Int): Int = cumFreq[symbol] + (freq[symbol].toInt() and 0xFF)

    override fun increment(symbol: Int) {
        if (freq[symbol] < 255.toByte()) {
            freq[symbol] = (freq[symbol] + 1).toByte()
        }
        rebuildCumulative()
    }
}
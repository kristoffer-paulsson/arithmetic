package org.example.arithmetic

public class AdaptiveArithmeticDecoder(private val precision: Int = 32, compressed: ByteArray, private val freqs: AdaptiveFrequencyTable) {
    private val decoder = ArithmeticDecoder(precision, BitInputBuffer(compressed))

    public fun decode(): ByteArray {
        val result = mutableListOf<Byte>()
        while (true) {
            val symbol = decoder.readSymbol(freqs)
            if (symbol == 256) break // EOF
            result.add(symbol.toByte())
            freqs.increment(symbol)
        }
        return result.toByteArray()
    }
}
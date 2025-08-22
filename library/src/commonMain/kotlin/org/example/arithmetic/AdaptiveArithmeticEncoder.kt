package org.example.arithmetic

public class AdaptiveArithmeticEncoder(private val precision: Int = 32, private val inputSize: Int) {
    private val encoder = ArithmeticEncoder(precision, inputSize)
    private val freqs = AdaptiveFrequencyTable(257)

    public fun encode(data: ByteArray): ByteArray {
        for (b in data) {
            val symbol = b.toInt() and 0xFF
            encoder.encodeSymbol(symbol, freqs)
            freqs.increment(symbol)
        }
        val eof = 256
        encoder.encodeSymbol(eof, freqs)
        freqs.increment(eof)
        return encoder.finish()
    }

    public fun getFrequencyTable(): AdaptiveFrequencyTable {
        return freqs
    }
}
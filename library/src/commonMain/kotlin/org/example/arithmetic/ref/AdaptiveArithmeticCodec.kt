/*
 * Reference arithmetic coding
 *
 * Copyright (c) Project Nayuki
 * MIT License. See readme file.
 * https://www.nayuki.io/page/reference-arithmetic-coding
 */

package org.example.arithmetic.ref

import org.example.arithmetic.ref.ArithmeticDecoder.read

public object AdaptiveArithmeticCodec {

    // To allow unit testing, this method is package-private instead of private.
    public fun compress(src: BitInputBuffer, dst: BitOutputBuffer) {
        val initFreqs = FlatFrequencyTable(257)
        val freqs: FrequencyTable = SimpleFrequencyTable(initFreqs)
        val enc = ArithmeticEncoder(32, dst)
        while (true) {
            // Read and encode one byte
            val symbol: Int = src.read()
            if (symbol == -1) break
            enc.write(freqs, symbol)
            freqs.increment(symbol)
        }
        enc.write(freqs, 256) // EOF
        enc.finish() // Flush remaining code bits
    }

    // To allow unit testing, this method is package-private instead of private.
    public fun decompress(src: BitInputBuffer, dst: BitOutputBuffer) {
        val initFreqs = FlatFrequencyTable(257)
        val freqs: FrequencyTable = SimpleFrequencyTable(initFreqs)
        val dec = ArithmeticDecoder(32, src)
        while (true) {
            // Decode and write one byte
            val symbol = dec.read(freqs)
            if (symbol == 256)  // EOF symbol
                break
            dst.write(symbol)
            freqs.increment(symbol)
        }
    }
}
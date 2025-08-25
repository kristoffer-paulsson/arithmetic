/**
 * Reference arithmetic coding
 *
 * Copyright (c) 2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/reference-arithmetic-coding
 *
 * This software is available under the terms of the MIT license.
 * The legal terms are attached to the LICENSE file and are made
 * available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *      Nayuki - initial Java implementation
 *      Kristoffer Paulsson - porting and adaption to Kotlin for alternative use
 */
package org.example.arithmetic

import org.example.arithmetic.io.BitOutput
import org.example.arithmetic.io.ByteInput

/**
 * Compression application using adaptive arithmetic coding.
 *
 * Usage: java AdaptiveArithmeticCompress InputFile OutputFile
 *
 * Then use the corresponding "AdaptiveArithmeticDecompress" application to recreate the original input file.
 *
 * Note that the application starts with a flat frequency table of 257 symbols (all set to a frequency of 1),
 * and updates it after each byte encoded. The corresponding decompressor program also starts with a flat
 * frequency table and updates it after each byte decoded. It is by design that the compressor and
 * decompressor have synchronized states, so that the data can be decompressed properly.
 */
public abstract class AbstractAdaptiveArithmeticCompress {

    public fun compress(inp: ByteInput, out: BitOutput) {
        val initFreqs = FlatFrequencyTable(257)
        val freqs: FrequencyTable = SimpleFrequencyTable(initFreqs)
        val enc = ArithmeticEncoder(32, out)
        while (true) {
            // Read and encode one byte
            val symbol: Int = inp.read()
            if (symbol == -1) break
            enc.write(freqs, symbol)
            freqs.increment(symbol)
        }
        enc.write(freqs, 256) // EOF
        enc.finish() // Flush remaining code bits
    }
}

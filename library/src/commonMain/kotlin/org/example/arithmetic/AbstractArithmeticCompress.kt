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
 * Compression application using static arithmetic coding.
 *
 * Usage: java ArithmeticCompress InputFile OutputFile
 *
 * Then use the corresponding "ArithmeticDecompress" application to recreate the original input file.
 *
 * Note that the application uses an alphabet of 257 symbols - 256 symbols for the byte
 * values and 1 symbol for the EOF marker. The compressed file format starts with a list
 * of 256 symbol frequencies, and then followed by the arithmetic-coded data.
 */
public abstract class AbstractArithmeticCompress {

    // Returns a frequency table based on the bytes in the given file.
    // Also contains an extra entry for symbol 256, whose frequency is set to 0.
    public fun getFrequencies(input: ByteInput): FrequencyTable {
        val freqs: FrequencyTable = SimpleFrequencyTable(IntArray(257))
        while (true) {
            val b: Int = input.read()
            if (b == -1) break
            freqs.increment(b)
        }
        return freqs
    }


    // To allow unit testing, this method is package-private instead of private.
    public fun writeFrequencies(out: BitOutput, freqs: FrequencyTable) {
        for (i in 0..255) writeInt(out, 32, freqs.get(i))
    }


    // To allow unit testing, this method is package-private instead of private.
    public fun compress(freqs: FrequencyTable, inp: ByteInput, out: BitOutput) {
        val enc = ArithmeticEncoder(32, out)
        while (true) {
            val symbol: Int = inp.read()
            if (symbol == -1) break
            enc.write(freqs, symbol)
        }
        enc.write(freqs, 256) // EOF
        enc.finish() // Flush remaining code bits
    }


    // Writes an unsigned integer of the given bit width to the given stream.
    private fun writeInt(out: BitOutput, numBits: Int, value: Int) {
        require(!(numBits < 0 || numBits > 32))

        for (i in numBits - 1 downTo 0) out.write((value ushr i) and 1) // Big endian
    }
}

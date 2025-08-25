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

/**
 * Encodes symbols and writes to an arithmetic-coded bit stream. Not thread-safe.
 * @see org.example.arithmetic.ArithmeticDecoder
 */
public class ArithmeticEncoder public constructor(numBits: Int, out: BitOutput) : ArithmeticCoderBase(numBits) {
    private val output: BitOutput

    // Number of saved underflow bits. This value can grow without bound,
    // so a truly correct implementation would use a BigInteger.
    private var numUnderflow = 0


    /**
     * Constructs an arithmetic coding encoder based on the specified bit output stream.
     * @param numBits the number of bits for the arithmetic coding range
     * @param out the bit output stream to write to
     * @throws NullPointerException if the output stream is `null`
     * @throws IllegalArgumentException if stateSize is outside the range [1, 62]
     */
    init {
        output = out
    }

    /**
     * Encodes the specified symbol based on the specified frequency table.
     * This updates this arithmetic coder's state and may write out some bits.
     * @param freqs the frequency table to use
     * @param symbol the symbol to encode
     * @throws NullPointerException if the frequency table is `null`
     * @throws IllegalArgumentException if the symbol has zero frequency
     * or the frequency table's total is too large
     */
    public fun write(freqs: FrequencyTable, symbol: Int) {
        write(CheckedFrequencyTable(freqs), symbol)
    }

    /**
     * Encodes the specified symbol based on the specified frequency table.
     * Also updates this arithmetic coder's state and may write out some bits.
     * @param freqs the frequency table to use
     * @param symbol the symbol to encode
     * @throws NullPointerException if the frequency table is `null`
     * @throws IllegalArgumentException if the symbol has zero frequency
     * or the frequency table's total is too large
     */
    public fun write(freqs: CheckedFrequencyTable, symbol: Int) {
        update(freqs, symbol)
    }

    /**
     * Terminates the arithmetic coding by flushing any buffered bits, so that the output can be decoded properly.
     * It is important that this method must be called at the end of the each encoding process.
     *
     * Note that this method merely writes data to the underlying output stream but does not close it.
     */
    public fun finish() {
        output.write(1)
    }

    override fun shift() {
        val bit = (low ushr (numStateBits - 1)).toInt() // Fix
        output.write(bit)

        // Write out the saved underflow bits
        while (numUnderflow > 0) {
            output.write(bit xor 1)
            numUnderflow--
        }
    }


    override fun underflow() {
        if (numUnderflow == Int.MAX_VALUE) throw ArithmeticException("Maximum underflow reached")
        numUnderflow++
    }
}
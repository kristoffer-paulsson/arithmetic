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

import org.example.arithmetic.io.BitInput

/**
 * Reads from an arithmetic-coded bit stream and decodes symbols. Not thread-safe.
 * @see ArithmeticEncoder
 */
public class ArithmeticDecoder public constructor(numBits: Int, inp: BitInput) : ArithmeticCoderBase(numBits) {
    private val input: BitInput

    // The current raw code bits being buffered, which is always in the range [low, high].
    private var code: Long = 0

    /**
     * Constructs an arithmetic coding decoder based on the
     * specified bit input stream, and fills the code bits.
     * @param numBits the number of bits for the arithmetic coding range
     * @param inp the bit input stream to read from
     * @throws NullPointerException if the input steam is `null`
     * @throws IllegalArgumentException if stateSize is outside the range [1, 62]
     */
    init {
        input = inp
        for (i in 0..<numStateBits) code = code shl 1 or readCodeBit().toLong()
    }

    /**
     * Decodes the next symbol based on the specified frequency table and returns it.
     * Also updates this arithmetic coder's state and may read in some bits.
     * @param freqs the frequency table to use
     * @return the next symbol
     * @throws NullPointerException if the frequency table is `null`
     */
    public fun read(freqs: FrequencyTable): Int {
        return read(CheckedFrequencyTable(freqs))
    }


    /**
     * Decodes the next symbol based on the specified frequency table and returns it.
     * Also updates this arithmetic coder's state and may read in some bits.
     * @param freqs the frequency table to use
     * @return the next symbol
     * @throws NullPointerException if the frequency table is `null`
     * @throws IllegalArgumentException if the frequency table's total is too large
     */
    public fun read(freqs: CheckedFrequencyTable): Int {
        // Translate from coding range scale to frequency table scale
        val total: Long = freqs.getTotal().toLong() // Fix
        require(!(total > maximumTotal)) { "Cannot decode symbol because total is too large" }
        val range: Long = high - low + 1
        val offset: Long = code - low
        val value = ((offset + 1) * total - 1) / range
        if (value * range / total > offset) throw AssertionError()
        if (!(0 <= value && value < total)) throw AssertionError()


        // A kind of binary search. Find highest symbol such that freqs.getLow(symbol) <= value.
        var start = 0
        var end: Int = freqs.getSymbolLimit()
        while (end - start > 1) {
            val middle = (start + end) ushr 1
            if (freqs.getLow(middle) > value) end = middle
            else start = middle
        }
        if (start + 1 != end) throw AssertionError()

        val symbol = start
        if (!(freqs.getLow(symbol) * range / total <= offset && offset < freqs.getHigh(symbol) * range / total)) throw AssertionError()
        update(freqs, symbol)
        if (!(low <= code && code <= high)) throw AssertionError("Code out of range")
        return symbol
    }

    override fun shift() {
        code = ((code shl 1) and stateMask) or readCodeBit().toLong() // Fix
    }

    override fun underflow() {
        code = (code and halfRange) or ((code shl 1) and (stateMask ushr 1)) or readCodeBit().toLong() // Fix
    }

    // Returns the next bit (0 or 1) from the input stream. The end
    // of stream is treated as an infinite number of trailing zeros.
    private fun readCodeBit(): Int {
        var temp: Int = input.read()
        if (temp == -1) temp = 0
        return temp
    }
}
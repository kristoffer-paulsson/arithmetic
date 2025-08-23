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

import java.io.IOException

/**
 * Provides the state and behaviors that arithmetic coding encoders and decoders share.
 * @see ArithmeticEncoder
 *
 * @see ArithmeticDecoder
 */
public abstract class ArithmeticCoderBase public constructor(numBits: Int) {
    /**
     * Number of bits for the 'low' and 'high' state variables. Must be in the range [1, 62].
     *
     *  * For state sizes less than the midpoint of around 32, larger values are generally better -
     * they allow a larger maximum frequency total (maximumTotal), and they reduce the approximation
     * error inherent in adapting fractions to integers; both effects reduce the data encoding loss
     * and asymptotically approach the efficiency of arithmetic coding using exact fractions.
     *  * But for state sizes greater than the midpoint, because intermediate computations are limited
     * to the long integer type's 63-bit unsigned precision, larger state sizes will decrease the
     * maximum frequency total, which might constrain the user-supplied probability model.
     *  * Therefore numStateBits=32 is recommended as the most versatile setting
     * because it maximizes maximumTotal (which ends up being slightly over 2^30).
     *  * Note that numStateBits=62 is legal but useless because it implies maximumTotal=1,
     * which means a frequency table can only support one symbol with non-zero frequency.
     *
     */
    protected val numStateBits: Int

    /** Maximum range (high+1-low) during coding (trivial), which is 2^numStateBits = 1000...000.  */
    protected val fullRange: Long

    /** The top bit at width numStateBits, which is 0100...000.  */
    protected val halfRange: Long

    /** The second highest bit at width numStateBits, which is 0010...000. This is zero when numStateBits=1.  */
    protected val quarterRange: Long

    /** Minimum range (high+1-low) during coding (non-trivial), which is 0010...010.  */
    protected val minimumRange: Long

    /** Maximum allowed total from a frequency table at all times during coding.  */
    protected val maximumTotal: Long

    /** Bit mask of numStateBits ones, which is 0111...111.  */
    protected val stateMask: Long

    /**
     * Low end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 0s.
     */
    protected var low: Long

    /**
     * High end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 1s.
     */
    protected var high: Long


    /**
     * Constructs an arithmetic coder, which initializes the code range.
     * @param numBits the number of bits for the arithmetic coding range
     * @throws IllegalArgumentException if stateSize is outside the range [1, 62]
     */
    init {
        require(1 <= numBits && numBits <= 62) { "State size out of range" }
        numStateBits = numBits
        fullRange = 1L shl numStateBits
        halfRange = fullRange ushr 1 // Non-zero
        quarterRange = halfRange ushr 1 // Can be zero
        minimumRange = quarterRange + 2 // At least 2
        maximumTotal = Math.min(Long.MAX_VALUE / fullRange, minimumRange)
        stateMask = fullRange - 1

        low = 0
        high = stateMask
    }

    /**
     * Updates the code range (low and high) of this arithmetic coder as a result
     * of processing the specified symbol with the specified frequency table.
     *
     * Invariants that are true before and after encoding/decoding each symbol
     * (letting fullRange = 2<sup>numStateBits</sup>):
     *
     *  * 0  low  code  high &lt; fullRange. ('code' exists only in the decoder.)
     * Therefore these variables are unsigned integers of numStateBits bits.
     *  * low &lt; 1/2  fullRange  high.
     * In other words, they are in different halves of the full range.
     *  * (low &lt; 1/4  fullRange) || (high  3/4  fullRange).
     * In other words, they are not both in the middle two quarters.
     *  * Let range = high  low + 1, then fullRange/4 &lt; minimumRange  range
     * fullRange. These invariants for 'range' essentially dictate the maximum total that the
     * incoming frequency table can have, such that intermediate calculations don't overflow.
     *
     * @param freqs the frequency table to use
     * @param symbol the symbol that was processed
     * @throws IllegalArgumentException if the symbol has zero frequency or the frequency table's total is too large
     */
    protected fun update(freqs: CheckedFrequencyTable, symbol: Int) {
        // State check
        if (low >= high || (low and stateMask) != low || (high and stateMask) != high) throw AssertionError("Low or high out of range")
        val range = high - low + 1
        if (!(minimumRange <= range && range <= fullRange)) throw AssertionError("Range out of range")


        // Frequency table values check
        val total: Long = freqs.getTotal().toLong() // Fix
        val symLow: Long = freqs.getLow(symbol).toLong() // Fix
        val symHigh: Long = freqs.getHigh(symbol).toLong() // Fix
        require(symLow != symHigh) { "Symbol has zero frequency" }
        require(total <= maximumTotal) { "Cannot code symbol because total is too large" }


        // Update range
        val newLow = low + symLow * range / total
        val newHigh = low + symHigh * range / total - 1
        low = newLow
        high = newHigh


        // While low and high have the same top bit value, shift them out
        while (((low xor high) and halfRange) == 0L) {
            shift()
            low = ((low shl 1) and stateMask)
            high = ((high shl 1) and stateMask) or 1L
        }

        // Now low's top bit must be 0 and high's top bit must be 1

        // While low's top two bits are 01 and high's are 10, delete the second highest bit of both
        while ((low and high.inv() and quarterRange) != 0L) {
            underflow()
            low = (low shl 1) xor halfRange
            high = ((high xor halfRange) shl 1) or halfRange or 1L
        }
    }


    /**
     * Called to handle the situation when the top bit of `low` and `high` are equal.
     */
    protected abstract fun shift()


    /**
     * Called to handle the situation when low=01(...) and high=10(...).
     */
    protected abstract fun underflow()
}

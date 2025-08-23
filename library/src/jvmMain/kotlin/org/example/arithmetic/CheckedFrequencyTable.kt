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

import java.util.Objects

/**
 * A wrapper that checks the preconditions (arguments) and postconditions (return value)
 * of all the frequency table methods. Useful for finding faults in a frequency table
 * implementation. However, arithmetic overflow conditions are not checked.
 */
public class CheckedFrequencyTable public constructor(freq: FrequencyTable) : FrequencyTable {
    private val freqTable: FrequencyTable

    init {
        freqTable = Objects.requireNonNull(freq)!!
    }

    public override fun getSymbolLimit(): Int {
        val result: Int = freqTable.getSymbolLimit()
        if (result <= 0) throw AssertionError("Non-positive symbol limit")
        return result
    }

    public override fun get(symbol: Int): Int {
        val result: Int = freqTable.get(symbol)
        if (!isSymbolInRange(symbol)) throw AssertionError("IllegalArgumentException expected")
        if (result < 0) throw AssertionError("Negative symbol frequency")
        return result
    }

    public override fun getTotal(): Int {
        val result: Int = freqTable.getTotal()
        if (result < 0) throw AssertionError("Negative total frequency")
        return result
    }

    public override fun getLow(symbol: Int): Int {
        if (isSymbolInRange(symbol)) {
            val low: Int = freqTable.getLow(symbol)
            val high: Int = freqTable.getHigh(symbol)
            if (!(0 <= low && low <= high && high <= freqTable.getTotal())) throw AssertionError("Symbol low cumulative frequency out of range")
            return low
        } else {
            freqTable.getLow(symbol)
            throw AssertionError("IllegalArgumentException expected")
        }
    }


    public override fun getHigh(symbol: Int): Int {
        if (isSymbolInRange(symbol)) {
            val low: Int = freqTable.getLow(symbol)
            val high: Int = freqTable.getHigh(symbol)
            if (!(0 <= low && low <= high && high <= freqTable.getTotal())) throw AssertionError("Symbol high cumulative frequency out of range")
            return high
        } else {
            freqTable.getHigh(symbol)
            throw AssertionError("IllegalArgumentException expected")
        }
    }

    public override fun toString(): String {
        return "CheckedFrequencyTable (" + freqTable.toString() + ")"
    }

    public override fun set(symbol: Int, freq: Int) {
        freqTable.set(symbol, freq)
        if (!isSymbolInRange(symbol) || freq < 0) throw AssertionError("IllegalArgumentException expected")
    }

    public override fun increment(symbol: Int) {
        freqTable.increment(symbol)
        if (!isSymbolInRange(symbol)) throw AssertionError("IllegalArgumentException expected")
    }

    private fun isSymbolInRange(symbol: Int): Boolean {
        return 0 <= symbol && symbol < this.getSymbolLimit()
    }
}

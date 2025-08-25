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

/**
 * An immutable frequency table where every symbol has the same frequency of 1.
 * Useful as a fallback model when no statistics are available.
 */
public class FlatFrequencyTable public constructor(numSyms: Int) : FrequencyTable {
    private val numSymbols: Int

    /**
     * Constructs a flat frequency table with the specified number of symbols.
     * @param numSyms the number of symbols, which must be at least 1
     * @throws IllegalArgumentException if the number of symbols is less than 1
     */
    init {
        require(numSyms >= 1) { "Number of symbols must be positive" }
        numSymbols = numSyms
    }

    /**
     * Returns the number of symbols in this table, which is at least 1.
     * @return the number of symbols in this table
     */
    public override fun getSymbolLimit(): Int {
        return numSymbols
    }

    /**
     * Returns the frequency of the specified symbol, which is always 1.
     * @param symbol the symbol to query
     * @return the frequency of the symbol, which is 1
     * @throws IllegalArgumentException if `symbol` &lt; 0 or `symbol`  `getSymbolLimit()`
     */
    public override fun get(symbol: Int): Int {
        checkSymbol(symbol)
        return 1
    }

    /**
     * Returns the total of all symbol frequencies, which is
     * always equal to the number of symbols in this table.
     * @return the total of all symbol frequencies, which is `getSymbolLimit()`
     */
    public override fun getTotal(): Int {
        return numSymbols
    }

    /**
     * Returns the sum of the frequencies of all the symbols strictly below
     * the specified symbol value. The returned value is equal to `symbol`.
     * @param symbol the symbol to query
     * @return the sum of the frequencies of all the symbols below `symbol`, which is `symbol`
     * @throws IllegalArgumentException if `symbol` &lt; 0 or `symbol`  `getSymbolLimit()`
     */
    public override fun getLow(symbol: Int): Int {
        checkSymbol(symbol)
        return symbol
    }

    /**
     * Returns the sum of the frequencies of the specified symbol and all
     * the symbols below. The returned value is equal to `symbol + 1`.
     * @param symbol the symbol to query
     * @return the sum of the frequencies of `symbol` and all symbols below, which is `symbol + 1`
     * @throws IllegalArgumentException if `symbol` &lt; 0 or `symbol`  `getSymbolLimit()`
     */
    public override fun getHigh(symbol: Int): Int {
        checkSymbol(symbol)
        return symbol + 1
    }

    // Returns silently if 0 <= symbol < numSymbols, otherwise throws an exception.
    private fun checkSymbol(symbol: Int) {
        require(symbol in 0..<numSymbols) { "Symbol out of range" }
    }

    /**
     * Returns a string representation of this frequency table. The format is subject to change.
     * @return a string representation of this frequency table
     */
    public override fun toString(): String {
        return "FlatFrequencyTable=$numSymbols"
    }

    /**
     * Unsupported operation, because this frequency table is immutable.
     * @param symbol ignored
     * @param freq ignored
     * @throws UnsupportedOperationException because this frequency table is immutable
     */
    public override fun set(symbol: Int, freq: Int) {
        throw UnsupportedOperationException()
    }

    /**
     * Unsupported operation, because this frequency table is immutable.
     * @param symbol ignored
     * @throws UnsupportedOperationException because this frequency table is immutable
     */
    public override fun increment(symbol: Int) {
        throw UnsupportedOperationException()
    }
}
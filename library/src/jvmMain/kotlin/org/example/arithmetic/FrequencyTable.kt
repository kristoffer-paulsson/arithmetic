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
 * A table of symbol frequencies. The table holds data for symbols numbered from 0
 * to getSymbolLimit()1. Each symbol has a frequency, which is a non-negative integer.
 *
 * Frequency table objects are primarily used for getting cumulative symbol
 * frequencies. These objects can be mutable depending on the implementation.
 * The total of all symbol frequencies must not exceed Integer.MAX_VALUE.
 */
public interface FrequencyTable {
    /**
     * Returns the number of symbols in this frequency table, which is a positive number.
     * @return the number of symbols in this frequency table
     */
    public fun getSymbolLimit(): Int

    /**
     * Returns the frequency of the specified symbol. The returned value is at least 0.
     * @param symbol the symbol to query
     * @return the frequency of the symbol
     * @throws IllegalArgumentException if the symbol is out of range
     */
    public fun get(symbol: Int): Int

    /**
     * Sets the frequency of the specified symbol to the specified value.
     * The frequency value must be at least 0.
     * @param symbol the symbol to set
     * @param freq the frequency value to set
     * @throws IllegalArgumentException if the frequency is negative or the symbol is out of range
     * @throws ArithmeticException if an arithmetic overflow occurs
     */
    public fun set(symbol: Int, freq: Int)

    /**
     * Increments the frequency of the specified symbol.
     * @param symbol the symbol whose frequency to increment
     * @throws IllegalArgumentException if the symbol is out of range
     * @throws ArithmeticException if an arithmetic overflow occurs
     */
    public fun increment(symbol: Int)

    /**
     * Returns the total of all symbol frequencies. The returned value is at
     * least 0 and is always equal to `getHigh(getSymbolLimit() - 1)`.
     * @return the total of all symbol frequencies
     */
    public fun getTotal(): Int

    /**
     * Returns the sum of the frequencies of all the symbols strictly
     * below the specified symbol value. The returned value is at least 0.
     * @param symbol the symbol to query
     * @return the sum of the frequencies of all the symbols below `symbol`
     * @throws IllegalArgumentException if the symbol is out of range
     */
    public fun getLow(symbol: Int): Int

    /**
     * Returns the sum of the frequencies of the specified symbol
     * and all the symbols below. The returned value is at least 0.
     * @param symbol the symbol to query
     * @return the sum of the frequencies of `symbol` and all symbols below
     * @throws IllegalArgumentException if the symbol is out of range
     */
    public fun getHigh(symbol: Int): Int
}

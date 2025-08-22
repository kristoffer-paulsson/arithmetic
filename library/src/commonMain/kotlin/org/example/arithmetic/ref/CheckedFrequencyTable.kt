/*
 * Reference arithmetic coding
 *
 * Copyright (c) Project Nayuki
 * MIT License. See readme file.
 * https://www.nayuki.io/page/reference-arithmetic-coding
 */
package org.example.arithmetic.ref


/**
 * A wrapper that checks the preconditions (arguments) and postconditions (return value)
 * of all the frequency table methods. Useful for finding faults in a frequency table
 * implementation. However, arithmetic overflow conditions are not checked.
 */
public class CheckedFrequencyTable(private val freqTable: FrequencyTable) : FrequencyTable {

    /*---- Methods ----*/
    override fun getSymbolLimit(): Int {
        val result = freqTable.getSymbolLimit()
        check(result > 0) { "Non-positive symbol limit" }
        //if (result <= 0) throw java.lang.AssertionError("Non-positive symbol limit")
        return result
    }


    override fun get(symbol: Int): Int {
        val result = freqTable.get(symbol)
        check(isSymbolInRange(symbol)) { "IllegalArgumentException expected" }
        check(result >= 0) { "Negative symbol frequency" }
        //if (!isSymbolInRange(symbol)) throw java.lang.AssertionError("IllegalArgumentException expected")
        //if (result < 0) throw java.lang.AssertionError("Negative symbol frequency")
        return result
    }


    override fun getTotal(): Int {
        val result = freqTable.getTotal()
        if (result < 0) throw java.lang.AssertionError("Negative total frequency")
        return result
    }


    override fun getLow(symbol: Int): Int {
        if (isSymbolInRange(symbol)) {
            val low = freqTable.getLow(symbol)
            val high = freqTable.getHigh(symbol)
            if (!(0 <= low && low <= high && high <= freqTable.getTotal())) throw java.lang.AssertionError("Symbol low cumulative frequency out of range")
            return low
        } else {
            freqTable.getLow(symbol)
            error("IllegalArgumentException expected")
            //throw java.lang.AssertionError()
        }
    }


    override fun getHigh(symbol: Int): Int {
        if (isSymbolInRange(symbol)) {
            val low = freqTable.getLow(symbol)
            val high = freqTable.getHigh(symbol)
            if (!(0 <= low && low <= high && high <= freqTable.getTotal())) throw java.lang.AssertionError("Symbol high cumulative frequency out of range")
            return high
        } else {
            freqTable.getHigh(symbol)
            throw java.lang.AssertionError("IllegalArgumentException expected")
        }
    }


    override fun toString(): String {
        return "CheckedFrequencyTable (" + freqTable.toString() + ")"
    }


    override fun set(symbol: Int, freq: Int) {
        freqTable.set(symbol, freq)
        if (!isSymbolInRange(symbol) || freq < 0) throw java.lang.AssertionError("IllegalArgumentException expected")
    }


    override fun increment(symbol: Int) {
        freqTable.increment(symbol)
        if (!isSymbolInRange(symbol)) throw java.lang.AssertionError("IllegalArgumentException expected")
    }


    private fun isSymbolInRange(symbol: Int): Boolean {
        return 0 <= symbol && symbol < getSymbolLimit()
    }
}
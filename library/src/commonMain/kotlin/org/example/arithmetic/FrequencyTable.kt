package org.example.arithmetic

public interface FrequencyTable {
    public fun getSymbolLimit(): Int
    public fun getTotal(): Int
    public fun getLow(symbol: Int): Int
    public fun getHigh(symbol: Int): Int
    public fun increment(symbol: Int)
}
/**
 * Reference arithmetic coding
 *
 * Copyright (c) 2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * Copyright (c) Project Nayuki
 * MIT License. See readme file.
 * https://www.nayuki.io/page/reference-arithmetic-coding
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

public class PpmModel (order: Int, symbolLimit: Int, escapeSymbol: Int) {
    /*---- Fields ----*/
    public val modelOrder: Int

    private val symbolLimit: Int
    private val escapeSymbol: Int

    public var rootContext: Context = nullCtx
    public val orderMinus1Freqs: FrequencyTable


    /*---- Constructors ----*/
    init {
        require(order >= -1 && 0 <= escapeSymbol && escapeSymbol < symbolLimit)
        this.modelOrder = order
        this.symbolLimit = symbolLimit
        this.escapeSymbol = escapeSymbol

        if (order >= 0) {
            rootContext = Context(symbolLimit, order >= 1)
            rootContext.frequencies.increment(escapeSymbol)
        } else rootContext = nullCtx
        orderMinus1Freqs = FlatFrequencyTable(symbolLimit)
    }


    /*---- Methods ----*/
    public fun incrementContexts(history: IntArray, symbol: Int) {
        if (modelOrder == -1) return
        require(history.size <= modelOrder && 0 <= symbol && symbol < symbolLimit)

        var ctx = rootContext
        ctx.frequencies.increment(symbol)
        var i = 0
        for (sym in history) {
            val subctxs = ctx.subcontexts
            if (subctxs.isEmpty()) throw AssertionError()

            if (subctxs[sym] == nullCtx) {
                subctxs[sym] = Context(symbolLimit, i + 1 < modelOrder)
                subctxs[sym].frequencies.increment(escapeSymbol)
            }
            ctx = subctxs[sym]
            ctx.frequencies.increment(symbol)
            i++
        }
    }


    /*---- Helper structure ----*/
    public class Context public constructor(symbols: Int, hasSubctx: Boolean) {
        public val frequencies: FrequencyTable

        public var subcontexts: Array<Context> = emptyArray()


        init {
            frequencies = SimpleFrequencyTable(IntArray(symbols))
            if (hasSubctx) subcontexts = Array(symbols) { nullCtx }
            else subcontexts = emptyArray()
        }
    }

    public companion object {
        public val nullCtx: Context = Context(1, false);
    }
}

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

public class PpmModel (order: Int, symbolLimit: Int, escapeSymbol: Int) {
    public val modelOrder: Int

    private val symbolLimit: Int
    private val escapeSymbol: Int

    public var rootContext: Context = nullCtx
    public val orderMinus1Freqs: FrequencyTable

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

    public fun incrementContexts(history: MutableList<Int>, symbol: Int) {
        if (modelOrder == -1) return
        require(history.size <= modelOrder && 0 <= symbol && symbol < symbolLimit)

        var ctx = rootContext
        ctx.frequencies.increment(symbol)
        var i = 0
        for (sym in history) {
            val subctxs = ctx.subcontexts
            //if (subctxs.isEmpty()) throw AssertionError()
            check(subctxs.isNotEmpty())

            if (subctxs[sym] == nullCtx) {
                subctxs[sym] = Context(symbolLimit, i + 1 < modelOrder)
                subctxs[sym].frequencies.increment(escapeSymbol)
            }
            ctx = subctxs[sym]
            ctx.frequencies.increment(symbol)
            i++
        }
    }

    public class Context public constructor(symbols: Int, hasSubctx: Boolean) {
        public val frequencies: FrequencyTable = SimpleFrequencyTable(IntArray(symbols))
        public var subcontexts: Array<Context> = emptyArray()

        init {
            if (hasSubctx) subcontexts = Array(symbols) { nullCtx }
            else subcontexts = emptyArray()
        }
    }

    public companion object {
        public val nullCtx: Context = Context(1, false);
    }
}
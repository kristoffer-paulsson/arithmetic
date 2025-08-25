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
import org.example.arithmetic.io.ByteInput

/**
 * Compression application using prediction by partial matching (PPM) with arithmetic coding.
 *
 * Usage: java PpmCompress InputFile OutputFile
 *
 * Then use the corresponding "PpmDecompress" application to recreate the original input file.
 *
 * Note that both the compressor and decompressor need to use the same PPM context modeling logic.
 * The PPM algorithm can be thought of as a powerful generalization of adaptive arithmetic coding.
 */
public abstract class AbstractPpmCompress {
    // Must be at least -1 and match PpmDecompress. Warning: Exponential memory usage at O(257^n).

    // To allow unit testing, this method is package-private instead of private.
    public fun compress(inp: ByteInput, out: BitOutput) {
        // Set up encoder and model. In this PPM model, symbol 256 represents EOF;
        // its frequency is 1 in the order -1 context but its frequency
        // is 0 in all other contexts (which have non-negative order).
        val enc = ArithmeticEncoder(32, out)
        val model = PpmModel(MODEL_ORDER, 257, 256)
        val history = mutableListOf<Int>()

        while (true) {
            // Read and encode one byte
            val symbol: Int = inp.read()
            if (symbol == -1) break
            encodeSymbol(model, history, symbol, enc)
            model.incrementContexts(history, symbol)

            if (model.modelOrder >= 1) {
                // Prepend current symbol, dropping oldest symbol if necessary
                history.add(symbol)
                if (history.size >= model.modelOrder) history.removeLast()
            }
        }

        encodeSymbol(model, history, 256, enc) // EOF
        enc.finish() // Flush remaining code bits
    }

    private fun encodeSymbol(model: PpmModel, history: MutableList<Int>, symbol: Int, enc: ArithmeticEncoder) {
        // Try to use highest order context that exists based on the history suffix, such
        // that the next symbol has non-zero frequency. When symbol 256 is produced at a context
        // at any non-negative order, it means "escape to the next lower order with non-empty
        // context". When symbol 256 is produced at the order -1 context, it means "EOF".
        outer@ for (order in history.size downTo 0) {
            var ctx: PpmModel.Context = model.rootContext
            for (i in 0..<order) {
                //if (ctx.subcontexts.isEmpty()) throw AssertionError()
                check(ctx.subcontexts.isNotEmpty())
                ctx = ctx.subcontexts[history[i]]
                if (ctx === PpmModel.nullCtx) continue@outer
            }
            if (symbol != 256 && ctx.frequencies.get(symbol) > 0) {
                enc.write(ctx.frequencies, symbol)
                return
            }
            // Else write context escape symbol and continue decrementing the order
            enc.write(ctx.frequencies, 256)
        }
        // Logic for order = -1
        enc.write(model.orderMinus1Freqs, symbol)
    }

    public companion object{
        private const val MODEL_ORDER = 3
    }
}

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

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Arrays

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
public object PpmCompress {
    // Must be at least -1 and match PpmDecompress. Warning: Exponential memory usage at O(257^n).
    private const val MODEL_ORDER = 3


    @Throws(IOException::class)
    public fun main(args: Array<String>) {
        // Handle command line arguments
        if (args.size != 2) {
            System.err.println("Usage: java PpmCompress InputFile OutputFile")
            System.exit(1)
            return
        }
        val inputFile: File = File(args[0])
        val outputFile: File = File(args[1])

        BufferedInputStream(FileInputStream(inputFile)).use { `in` ->
            BitOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { out ->
                compress(`in`, out)
            }
        }
    }


    // To allow unit testing, this method is package-private instead of private.
    @Throws(IOException::class)
    public fun compress(`in`: InputStream, out: BitOutputStream) {
        // Set up encoder and model. In this PPM model, symbol 256 represents EOF;
        // its frequency is 1 in the order -1 context but its frequency
        // is 0 in all other contexts (which have non-negative order).
        val enc: ArithmeticEncoder = ArithmeticEncoder(32, out)
        val model: PpmModel = PpmModel(MODEL_ORDER, 257, 256)
        var history = IntArray(0)

        while (true) {
            // Read and encode one byte
            val symbol: Int = `in`.read()
            if (symbol == -1) break
            encodeSymbol(model, history, symbol, enc)
            model.incrementContexts(history, symbol)

            if (model.modelOrder >= 1) {
                // Prepend current symbol, dropping oldest symbol if necessary
                if (history.size < model.modelOrder) history = Arrays.copyOf(history, history.size + 1)
                System.arraycopy(history, 0, history, 1, history.size - 1)
                history[0] = symbol
            }
        }

        encodeSymbol(model, history, 256, enc) // EOF
        enc.finish() // Flush remaining code bits
    }


    @Throws(IOException::class)
    private fun encodeSymbol(model: PpmModel, history: IntArray, symbol: Int, enc: ArithmeticEncoder) {
        // Try to use highest order context that exists based on the history suffix, such
        // that the next symbol has non-zero frequency. When symbol 256 is produced at a context
        // at any non-negative order, it means "escape to the next lower order with non-empty
        // context". When symbol 256 is produced at the order -1 context, it means "EOF".
        outer@ for (order in history.size downTo 0) {
            var ctx: PpmModel.Context = model.rootContext
            for (i in 0..<order) {
                if (ctx.subcontexts.isEmpty()) throw AssertionError()
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
}

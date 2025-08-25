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
public object PpmCompress : AbstractPpmCompress() {
    // Must be at least -1 and match PpmDecompress. Warning: Exponential memory usage at O(257^n).
    private const val MODEL_ORDER = 3

    public fun main(args: Array<String>) {
        // Handle command line arguments
        if (args.size != 2) {
            System.err.println("Usage: java PpmCompress InputFile OutputFile")
            System.exit(1)
            return
        }
        val inputFile: File = File(args[0])
        val outputFile: File = File(args[1])

        BufferedInputStream(FileInputStream(inputFile)).use { inp ->
            BitOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { out ->
                compress(ByteInputWrapper(inp), out)
            }
        }
    }
}

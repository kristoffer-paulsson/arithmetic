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


/**
 * Compression application using static arithmetic coding.
 *
 * Usage: java ArithmeticCompress InputFile OutputFile
 *
 * Then use the corresponding "ArithmeticDecompress" application to recreate the original input file.
 *
 * Note that the application uses an alphabet of 257 symbols - 256 symbols for the byte
 * values and 1 symbol for the EOF marker. The compressed file format starts with a list
 * of 256 symbol frequencies, and then followed by the arithmetic-coded data.
 */
public object ArithmeticCompress : AbstractArithmeticCompress() {

    public fun main(args: Array<String>) {
        // Handle command line arguments
        if (args.size != 2) {
            System.err.println("Usage: java ArithmeticCompress InputFile OutputFile")
            System.exit(1)
            return
        }
        val inputFile: File = File(args[0])
        val outputFile: File = File(args[1])


        // Read input file once to compute symbol frequencies
        val freqs: FrequencyTable = getFrequencies(ByteInputWrapper(FileInputStream(inputFile)))
        freqs.increment(256) // EOF symbol gets a frequency of 1

        BufferedInputStream(FileInputStream(inputFile)).use { inp ->
            BitOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { out ->
                writeFrequencies(out, freqs)
                compress(freqs, ByteInputWrapper(inp), out)
            }
        }
    }
}

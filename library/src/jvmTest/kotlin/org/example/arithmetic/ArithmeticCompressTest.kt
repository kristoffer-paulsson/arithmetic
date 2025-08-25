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

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Tests [ArithmeticCompress] coupled with [AbstractArithmeticDecompress].
 */
class ArithmeticCompressTest : ArithmeticCodingTest() {

    override fun compress(b: ByteArray): ByteArray {

        val arithmeticCompress = object : AbstractArithmeticCompress() {}

        /*val freqs: FrequencyTable = SimpleFrequencyTable(IntArray(257))
        for (x in b) freqs.increment(x.toInt() and 0xFF)
        freqs.increment(256) // EOF symbol gets a frequency of 1*/

        val freqs = arithmeticCompress.getFrequencies(ByteInputWrapper(ByteArrayInputStream(b)))

        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        BitOutputStream(out).use { bitOut ->
            arithmeticCompress.writeFrequencies(bitOut, freqs)
            arithmeticCompress.compress(freqs, ByteInputWrapper(`in`), bitOut)
        }
        return out.toByteArray()
    }

    override fun decompress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        val bitIn = BitInputStream(`in`)

        val arithmeticDecompress = object : AbstractArithmeticDecompress() {}

        val freqs = arithmeticDecompress.readFrequencies(bitIn)
        arithmeticDecompress.decompress(freqs, bitIn, ByteOutputWrapper(out))
        return out.toByteArray()
    }
}

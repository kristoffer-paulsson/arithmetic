/*
* Reference arithmetic coding
*
* Copyright (c) Project Nayuki
* MIT License. See readme file.
* https://www.nayuki.io/page/reference-arithmetic-coding
*/
package org.example.arithmetic

import org.example.arithmetic.ArithmeticCompress.compress
import org.example.arithmetic.ArithmeticCompress.writeFrequencies
import org.example.arithmetic.ArithmeticDecompress.decompress
import org.example.arithmetic.ArithmeticDecompress.readFrequencies
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Tests [ArithmeticCompress] coupled with [ArithmeticDecompress].
 */
class ArithmeticCompressTest : ArithmeticCodingTest() {
    @Throws(IOException::class)
    override fun compress(b: ByteArray): ByteArray {
        val freqs: FrequencyTable = SimpleFrequencyTable(IntArray(257))
        for (x in b) freqs.increment(x.toInt() and 0xFF)
        freqs.increment(256) // EOF symbol gets a frequency of 1

        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        BitOutputStream(out).use { bitOut ->
            writeFrequencies(bitOut, freqs)
            compress(freqs, `in`, bitOut)
        }
        return out.toByteArray()
    }


    @Throws(IOException::class)
    override fun decompress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        val bitIn = BitInputStream(`in`)

        val freqs = readFrequencies(bitIn)
        decompress(freqs, bitIn, out)
        return out.toByteArray()
    }
}

/*
* Reference arithmetic coding
*
* Copyright (c) Project Nayuki
* MIT License. See readme file.
* https://www.nayuki.io/page/reference-arithmetic-coding
*/
package org.example.arithmetic

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Tests [AdaptiveArithmeticCompress] coupled with [AdaptiveArithmeticDecompress].
 */
class AdaptiveArithmeticCompressTest : ArithmeticCodingTest() {
    @Throws(IOException::class)
    override fun compress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        BitOutputStream(out).use { bitOut ->
            AdaptiveArithmeticCompress.compress(`in`, bitOut)
        }
        return out.toByteArray()
    }


    @Throws(IOException::class)
    override fun decompress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        AdaptiveArithmeticDecompress.decompress(BitInputStream(`in`), out)
        return out.toByteArray()
    }
}

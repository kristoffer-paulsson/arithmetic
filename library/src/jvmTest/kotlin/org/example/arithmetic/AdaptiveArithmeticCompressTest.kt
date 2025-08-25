/*
* Reference arithmetic coding
*
* Copyright (c) 2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
 * Tests [AbstractAdaptiveArithmeticCompress] coupled with [AbstractAdaptiveArithmeticDecompress].
 */
class AdaptiveArithmeticCompressTest : ArithmeticCodingTest() {
    override fun compress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        val adaptiveCompress = object : AbstractAdaptiveArithmeticCompress() {}
        BitOutputStream(out).use { bitOut ->
            adaptiveCompress.compress(ByteInputWrapper(`in`), bitOut)
        }
        return out.toByteArray()
    }


    override fun decompress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        val adaptiveDecompress = object : AbstractAdaptiveArithmeticDecompress() {}
        adaptiveDecompress.decompress(BitInputStream(`in`), ByteOutputWrapper(out))
        return out.toByteArray()
    }
}

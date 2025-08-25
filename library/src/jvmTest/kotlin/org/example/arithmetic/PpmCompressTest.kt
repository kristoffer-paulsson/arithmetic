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
 * Tests [AbstractPpmCompress] coupled with [AbstractPpmDecompress].
 */
class PpmCompressTest : ArithmeticCodingTest() {
    @Throws(IOException::class)
    override fun compress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        val ppmCompress = object : AbstractPpmCompress() {}
        BitOutputStream(out).use { bitOut ->
            ppmCompress.compress(ByteInputWrapper(`in`), bitOut)
        }
        return out.toByteArray()
    }


    @Throws(IOException::class)
    override fun decompress(b: ByteArray): ByteArray {
        val `in`: InputStream = ByteArrayInputStream(b)
        val out = ByteArrayOutputStream()
        val ppmDecompress = object : AbstractPpmDecompress() {}
        ppmDecompress.decompress(BitInputStream(`in`), ByteOutputWrapper(out))
        return out.toByteArray()
    }
}

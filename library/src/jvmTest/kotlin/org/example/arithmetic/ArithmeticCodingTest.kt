/*
* Reference arithmetic coding
*
* Copyright (c) Project Nayuki
* MIT License. See readme file.
* https://www.nayuki.io/page/reference-arithmetic-coding
*/
package org.example.arithmetic

import org.junit.Assert
import org.junit.Test
import java.io.EOFException
import java.io.IOException
import java.util.*
import kotlin.math.max

/**
 * Tests the compression and decompression of a complete arithmetic coding application, using the JUnit test framework.
 */
abstract class ArithmeticCodingTest {
    /*---- Test cases ----*/
    @Test
    fun testEmpty() {
        test(ByteArray(0))
    }


    @Test
    fun testOneSymbol() {
        test(ByteArray(10))
    }


    @Test
    fun testSimple() {
        test(byteArrayOf(0, 3, 1, 2))
    }


    @Test
    fun testEveryByteValue() {
        val b = ByteArray(256)
        for (i in b.indices) b[i] = i.toByte()
        test(b)
    }


    @Test
    fun testUnderflow() {
        test(byteArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2))
    }


    @Test
    fun testUniformRandom() {
        for (i in 0..99) {
            val b = ByteArray(random.nextInt(1000))
            random.nextBytes(b)
            test(b)
        }
    }


    @Test
    fun testRandomDistribution() {
        for (i in 0..999) {
            val m: Int = random.nextInt(255) + 1 // Number of different symbols present
            val n = max(random.nextInt(1000), m) // Length of message


            // Create distribution
            val freqs = IntArray(m)
            var sum = 0
            for (j in freqs.indices) {
                freqs[j] = random.nextInt(10000) + 1
                sum += freqs[j]
            }
            val total = sum


            // Rescale frequencies
            sum = 0
            var index = 0
            for (j in freqs.indices) {
                val newsum = sum + freqs[j]
                val newindex = (n - m) * newsum / total + j + 1
                freqs[j] = newindex - index
                sum = newsum
                index = newindex
            }
            Assert.assertEquals(n.toLong(), index.toLong())


            // Create symbols
            val message = ByteArray(n)
            run {
                var k = 0
                var j = 0
                while (k < freqs.size) {
                    var l = 0
                    while (l < freqs[k]) {
                        message[j] = k.toByte()
                        l++
                        j++
                    }
                    k++
                }
            }


            // Shuffle message (Durstenfeld algorithm)
            for (j in message.indices) {
                val k: Int = random.nextInt(message.size - j) + j
                val temp = message[j]
                message[j] = message[k]
                message[k] = temp
            }

            test(message)
        }
    }


    /*---- Utilities ----*/ // Tests that the given byte array can be compressed and decompressed to the same data, and not throw any exceptions.
    private fun test(b: ByteArray) {
        try {
            val compressed = compress(b)
            val decompressed = decompress(compressed)
            Assert.assertArrayEquals(b, decompressed)
        } catch (e: EOFException) {
            Assert.fail("Unexpected EOF")
        } catch (e: IOException) {
            throw AssertionError(e)
        }
    }


    /*---- Abstract methods ----*/ // Compression method that needs to be supplied by a subclass.
    @Throws(IOException::class)
    protected abstract fun compress(b: ByteArray): ByteArray

    // Decompression method that needs to be supplied by a subclass.
    @Throws(IOException::class)
    protected abstract fun decompress(b: ByteArray): ByteArray

    companion object {
        private val random = Random()
    }
}

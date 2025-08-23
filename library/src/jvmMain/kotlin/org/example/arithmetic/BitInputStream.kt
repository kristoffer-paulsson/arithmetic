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

import java.io.EOFException
import java.io.InputStream

/**
 * A stream of bits that can be read. Because they come from an underlying byte stream,
 * the total number of bits is always a multiple of 8. The bits are read in big endian.
 * Mutable and not thread-safe.
 * @see BitOutputStream
 */
public class BitInputStream public constructor(inp: InputStream) : AutoCloseable {
    private val input: InputStream

    // Either in the range [0x00, 0xFF] if bits are available, or -1 if end of stream is reached.
    private var currentByte = 0

    // Number of remaining bits in the current byte, always between 0 and 7 (inclusive).
    private var numBitsRemaining = 0

    /**
     * Constructs a bit input stream based on the specified byte input stream.
     * @param `in` the byte input stream
     * @throws NullPointerException if the input stream is `null`
     */
    init {
        input = inp
    }

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or -1 if
     * the end of stream is reached. The end of stream always occurs on a byte boundary.
     * @return the next bit of 0 or 1, or -1 for the end of stream
     */
    public fun read(): Int {
        if (currentByte == -1) return -1
        if (numBitsRemaining == 0) {
            currentByte = input.read()
            if (currentByte == -1) return -1
            numBitsRemaining = 8
        }
        if (numBitsRemaining <= 0) throw AssertionError()
        numBitsRemaining--
        return (currentByte ushr numBitsRemaining) and 1
    }

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or throws an `EOFException`
     * if the end of stream is reached. The end of stream always occurs on a byte boundary.
     * @return the next bit of 0 or 1
     * @throws EOFException if the end of stream is reached
     */
    public fun readNoEof(): Int {
        val result = read()
        if (result != -1) return result
        else throw EOFException()
    }

    /**
     * Closes this stream and the underlying input stream.
     */
    public override fun close() {
        input.close()
        currentByte = -1
        numBitsRemaining = 0
    }
}

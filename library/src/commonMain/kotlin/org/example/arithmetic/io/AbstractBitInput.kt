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
package org.example.arithmetic.io

public abstract class AbstractBitInput<E>(inp: E): BitInput {
    protected val input: E = inp

    // Either in the range [0x00, 0xFF] if bits are available, or -1 if end of stream is reached.
    protected var currentByte: Int = 0

    // Number of remaining bits in the current byte, always between 0 and 7 (inclusive).
    protected var numBitsRemaining: Int = 0

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or -1 if
     * the end of stream is reached. The end of stream always occurs on a byte boundary.
     * @return the next bit of 0 or 1, or -1 for the end of stream
     */
    public override fun read(): Int {
        if (currentByte == -1) return -1
        if (numBitsRemaining == 0) {
            currentByte = readImpl()
            if (currentByte == -1) return -1
            numBitsRemaining = 8
        }
        if (numBitsRemaining <= 0) throw AssertionError()
        numBitsRemaining--
        return (currentByte ushr numBitsRemaining) and 1
    }

    protected abstract fun readImpl(): Int

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or throws an `EOFException`
     * if the end of stream is reached. The end of stream always occurs on a byte boundary.
     * @return the next bit of 0 or 1
     * @throws EOFException if the end of stream is reached
     */
    public override fun readNoEof(): Int {
        val result = read()
        if (result != -1) return result
        else throw EOFException()
    }

    protected abstract fun closeImpl()

    /**
     * Closes this stream and the underlying input stream.
     */
    public override fun close() {
        closeImpl()
        currentByte = -1
        numBitsRemaining = 0
    }
}
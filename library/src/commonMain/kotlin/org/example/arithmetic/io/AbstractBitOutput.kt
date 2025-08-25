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

public abstract class AbstractBitOutput<E>(out: E): BitOutput {
    private val output: E = out

    // The accumulated bits for the current byte, always in the range [0x00, 0xFF].
    private var currentByte = 0

    // Number of accumulated bits in the current byte, always between 0 and 7 (inclusive).
    private var numBitsFilled = 0

    protected abstract fun writeImpl(b: Int)

    /**
     * Writes a bit to the stream. The specified bit must be 0 or 1.
     * @param b the bit to write, which must be 0 or 1
     */
    public override fun write(b: Int) {
        require(!(b != 0 && b != 1)) { "Argument must be 0 or 1" }
        currentByte = (currentByte shl 1) or b
        numBitsFilled++
        if (numBitsFilled == 8) {
            writeImpl(currentByte)
            currentByte = 0
            numBitsFilled = 0
        }
    }

    protected abstract fun closeImpl()

    /**
     * Closes this stream and the underlying output stream. If called when this
     * bit stream is not at a byte boundary, then the minimum number of "0" bits
     * (between 0 and 7 of them) are written as padding to reach the next byte boundary.
     */
    public override fun close() {
        while (numBitsFilled != 0) write(0)
        closeImpl()
    }
}
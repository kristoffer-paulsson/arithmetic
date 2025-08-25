/**
 * Additional IO interfaces for bytewise operations.
 *
 * Copyright (c) 2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
 *      Kristoffer Paulsson - initial implementation
 */
package org.example.arithmetic.io

public class ByteOutputBuffer(private val buffer: ByteArray) : ByteOutput {
    private var position = 0

    /**
     * Writes the specified byte (the low eight bits of the argument b).
     * @param b the byte to write, which must be in the range 0 to 255
     * @throws IllegalArgumentException if `b` is outside the range 0 to 255
     */
    public override fun write(b: Int) {
        require(b in 0..255) { "Argument must be a byte" }
        if (position >= buffer.size) throw EOFException("Buffer overflow")
        buffer[position++] = b.toByte()
    }
}
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

public class BitOutputBuffer(buffer: ByteArray) : AbstractBitOutput<ByteArray>(buffer) {
    private var position = 0

    override fun writeImpl(b: Int) {
        output[position++] = (b and 0xFF).toByte()
    }

    override fun closeImpl() {

    }

    public fun toByteArray(): ByteArray = output.copyOfRange(0, position)
}
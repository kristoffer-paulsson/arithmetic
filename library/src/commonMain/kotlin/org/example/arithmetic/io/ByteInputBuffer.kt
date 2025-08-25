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

public class ByteInputBuffer(private val buffer: ByteArray) : ByteInput{
    private var position = 0

    /**
     * Reads and returns one byte, or -1 on end of stream.
     * @return the byte read as an integer in the range 0 to 255, or -1 on end of stream
     */
    public override fun read(): Int = if(position < buffer.size) buffer[position++].toInt().and(0xFF) else -1
}
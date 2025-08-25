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

public interface ByteOutput {

    /**
     * Writes the specified byte (the low eight bits of the argument b).
     * @param b the byte to write, which must be in the range 0 to 255
     * @throws IllegalArgumentException if `b` is outside the range 0 to 255
     * @throws java.io.IOException if an I/O exception occurred
     */
    public fun write(b: Int)
}
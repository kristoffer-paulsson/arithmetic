/**
 * Additional IO interfaces for bitwise operations.
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
package org.example.arithmetic

import org.example.arithmetic.io.ByteOutput
import java.io.OutputStream

/**
 * Wraps an OutputStream as a ByteOutput.
 *
 * @param output The underlying output stream to write bytes to.
 */
public class ByteOutputWrapper(private val output: OutputStream) : ByteOutput {

    override fun write(b: Int) {
        output.write(b)
    }
}
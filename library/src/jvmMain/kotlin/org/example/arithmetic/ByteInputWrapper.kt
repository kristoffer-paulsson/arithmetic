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

import org.example.arithmetic.io.ByteInput
import java.io.InputStream

/**
 * Wraps an InputStream as a ByteInput.
 *
 * @param input The underlying input stream to read bytes from.
 */
public class ByteInputWrapper(private val input: InputStream) : ByteInput {

    override fun read(): Int {
        return input.read()
    }
}
/**
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
 *      Kristoffer Paulsson - porting and adaption to Kotlin for alternative use
 */
package org.example.arithmetic

public object Math

public fun Math.addExact(x: Int, y: Int): Int {
    val r = x + y
    if (((x xor r) and (y xor r)) < 0) {
        throw ArithmeticException("integer overflow")
    }
    return r
}
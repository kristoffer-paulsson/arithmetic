/**
 * Additional exception for end-of-file scenarios.
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

public class EOFException(message: String = "") : IllegalStateException(message)
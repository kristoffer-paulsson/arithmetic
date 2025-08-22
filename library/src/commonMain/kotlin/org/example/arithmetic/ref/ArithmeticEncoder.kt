/*
 * Reference arithmetic coding
 *
 * Copyright (c) Project Nayuki
 * MIT License. See readme file.
 * https://www.nayuki.io/page/reference-arithmetic-coding
 */
package org.example.arithmetic.ref


/**
 * Encodes symbols and writes to an arithmetic-coded bit stream. Not thread-safe.
 * @see ArithmeticDecoder
 */
/**
 * Constructs an arithmetic coding encoder based on the specified bit output stream.
 * @param numBits the number of bits for the arithmetic coding range
 * @param out the bit output stream to write to
 * @throws NullPointerException if the output stream is `null`
 * @throws IllegalArgumentException if stateSize is outside the range [1, 62]
 */
public class ArithmeticEncoder(numBits: Int, buffer: BitOutputBuffer) : ArithmeticCoderBase(numBits) {
    /*---- Fields ----*/ // The underlying bit output stream (not null).
    private val output: BitOutputBuffer = buffer

    // Number of saved underflow bits. This value can grow without bound,
    // so a truly correct implementation would use a BigInteger.
    private var numUnderflow = 0


    /*---- Methods ----*/
    /**
     * Encodes the specified symbol based on the specified frequency table.
     * This updates this arithmetic coder's state and may write out some bits.
     * @param freqs the frequency table to use
     * @param symbol the symbol to encode
     * @throws NullPointerException if the frequency table is `null`
     * @throws IllegalArgumentException if the symbol has zero frequency
     * or the frequency table's total is too large
     * @throws IOException if an I/O exception occurred
     */
    public fun write(freqs: FrequencyTable, symbol: Int) {
        write(CheckedFrequencyTable(freqs), symbol)
    }


    /**
     * Encodes the specified symbol based on the specified frequency table.
     * Also updates this arithmetic coder's state and may write out some bits.
     * @param freqs the frequency table to use
     * @param symbol the symbol to encode
     * @throws NullPointerException if the frequency table is `null`
     * @throws IllegalArgumentException if the symbol has zero frequency
     * or the frequency table's total is too large
     * @throws IOException if an I/O exception occurred
     */
    public fun write(freqs: CheckedFrequencyTable, symbol: Int) {
        update(freqs, symbol)
    }


    /**
     * Terminates the arithmetic coding by flushing any buffered bits, so that the output can be decoded properly.
     * It is important that this method must be called at the end of the each encoding process.
     *
     * Note that this method merely writes data to the underlying output stream but does not close it.
     * @throws IOException if an I/O exception occurred
     */
    public fun finish() {
        output.write(1)
        output.close()
    }


    override fun shift() {
        val bit = (low ushr (numStateBits - 1)).toInt()
        output.write(bit)


        // Write out the saved underflow bits
        while (numUnderflow > 0) {
            output.write(bit xor 1)
            numUnderflow--
        }
    }

    override fun underflow() {
        check(numUnderflow != Int.MAX_VALUE) { "Maximum underflow reached" }
        //if (numUnderflow == Int.Companion.MAX_VALUE) throw java.lang.ArithmeticException("Maximum underflow reached")
        numUnderflow++
    }
}
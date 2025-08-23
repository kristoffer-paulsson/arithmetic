/*
 * Reference arithmetic coding
 *
 * Copyright (c) Project Nayuki
 * MIT License. See readme file.
 * https://www.nayuki.io/page/reference-arithmetic-coding
 */
package org.example.arithmetic.ref


/**
 * A stream where bits can be written to. Because they are written to an underlying
 * byte stream, the end of the stream is padded with 0's up to a multiple of 8 bits.
 * The bits are written in big endian. Mutable and not thread-safe.
 * @see BitInputStream
 */
/**
 * Constructs a bit output stream based on the specified byte output stream.
 * @param out the byte output stream
 * @throws NullPointerException if the output stream is `null`
 */
public class BitOutputBuffer(size: Int) {
    /*---- Fields ----*/ // The underlying byte stream to write to (not null).
    private val buffer = ByteArray(size)
    private var byteIndex = 0

    // The accumulated bits for the current byte, always in the range [0x00, 0xFF].
    private var currentByte = 0

    // Number of accumulated bits in the current byte, always between 0 and 7 (inclusive).
    private var numBitsFilled = 0

    /*---- Methods ----*/
    /**
     * Writes a bit to the stream. The specified bit must be 0 or 1.
     * @param b the bit to write, which must be 0 or 1
     * @throws IOException if an I/O exception occurred
     */
    /*@Throws(java.io.IOException::class)
    fun write(b: Int) {
        require(!(b != 0 && b != 1)) { "Argument must be 0 or 1" }
        currentByte = (currentByte shl 1) or b
        numBitsFilled++
        if (numBitsFilled == 8) {
            output.write(currentByte)
            currentByte = 0
            numBitsFilled = 0
        }
    }*/

    /**
     * Writes a bit (0 or 1) to the buffer. Ignores writes if buffer is full or closed.
     */
    public fun write(bit: Int) {
        require(bit == 0 || bit == 1) { "Argument must be 0 or 1" }
        if (byteIndex >= buffer.size) return
        currentByte = (currentByte shl 1) or bit
        numBitsFilled++
        if (numBitsFilled == 8) {
            buffer[byteIndex++] = currentByte.toByte()
            currentByte = 0
            numBitsFilled = 0
        }
    }


    /**
     * Closes this stream and the underlying output stream. If called when this
     * bit stream is not at a byte boundary, then the minimum number of "0" bits
     * (between 0 and 7 of them) are written as padding to reach the next byte boundary.
     * @throws IOException if an I/O exception occurred
     */
    /*@Throws(java.io.IOException::class)
    override fun close() {
        while (numBitsFilled != 0) write(0)
        output.close()
    }*/

    /**
     * Closes the buffer, writing any remaining bits as a padded byte.
     * After closing, no more bits can be written.
     */
    public fun close() {
        while (numBitsFilled != 0) write(0)
    }

    /**
     * Returns the written bytes, truncated to the actual number of bytes filled.
     * Call `close()` before calling this to ensure all bits are flushed.
     */
    public fun toByteArray(): ByteArray {
        return buffer.copyOf(byteIndex)
    }
}
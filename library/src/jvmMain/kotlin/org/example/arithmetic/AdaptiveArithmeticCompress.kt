/*
* Reference arithmetic coding
*
* Copyright (c) Project Nayuki
* MIT License. See readme file.
* https://www.nayuki.io/page/reference-arithmetic-coding
*/
package org.example.arithmetic

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Compression application using adaptive arithmetic coding.
 *
 * Usage: java AdaptiveArithmeticCompress InputFile OutputFile
 *
 * Then use the corresponding "AdaptiveArithmeticDecompress" application to recreate the original input file.
 *
 * Note that the application starts with a flat frequency table of 257 symbols (all set to a frequency of 1),
 * and updates it after each byte encoded. The corresponding decompressor program also starts with a flat
 * frequency table and updates it after each byte decoded. It is by design that the compressor and
 * decompressor have synchronized states, so that the data can be decompressed properly.
 */
public object AdaptiveArithmeticCompress {
    @Throws(IOException::class)
    public fun main(args: Array<String>) {
        // Handle command line arguments
        if (args.size != 2) {
            System.err.println("Usage: java AdaptiveArithmeticCompress InputFile OutputFile")
            System.exit(1)
            return
        }
        val inputFile: File = File(args[0])
        val outputFile: File = File(args[1])

        BufferedInputStream(FileInputStream(inputFile)).use { `in` ->
            BitOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { out ->
                compress(`in`, out)
            }
        }
    }


    // To allow unit testing, this method is package-private instead of private.
    @Throws(IOException::class)
    public fun compress(`in`: InputStream, out: BitOutputStream) {
        val initFreqs: FlatFrequencyTable = FlatFrequencyTable(257)
        val freqs: FrequencyTable = SimpleFrequencyTable(initFreqs)
        val enc: ArithmeticEncoder = ArithmeticEncoder(32, out)
        while (true) {
            // Read and encode one byte
            val symbol: Int = `in`.read()
            if (symbol == -1) break
            enc.write(freqs, symbol)
            freqs.increment(symbol)
        }
        enc.write(freqs, 256) // EOF
        enc.finish() // Flush remaining code bits
    }
}

package org.example.arithmetic

import org.example.arithmetic.io.BitOutputBuffer
import org.example.arithmetic.io.ByteInputBuffer
import kotlin.test.Test

class ArithmeticCodecTest {


    @Test
    fun testEncodeDecode() {
        val encoder = object : AbstractArithmeticCompress() {}
        val output = ByteArray(lipsum.size + 2048) // Extra space for compression overhead
        val outputBuffer = BitOutputBuffer(output)

        val freqs: FrequencyTable = encoder.getFrequencies(ByteInputBuffer(lipsum))
        encoder.writeFrequencies(outputBuffer, freqs)
        encoder.compress(freqs, ByteInputBuffer(lipsum), outputBuffer)

        println("Original size: ${lipsum.size} bytes")
        println("Compressed size: ${outputBuffer.toByteArray().size} bytes")
    }

    companion object {
        val lipsum: ByteArray = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus auctor ut augue vitae dignissim. Sed pretium erat nec 
metus pulvinar, ut finibus diam porttitor. Quisque vitae odio a eros vehicula sodales. Curabitur facilisis iaculis 
justo et efficitur. Sed ipsum turpis, vulputate quis gravida non, posuere in odio. Vivamus bibendum, justo eget euismod 
cursus, lorem arcu facilisis felis, eu dignissim nunc nibh nec ex. Morbi maximus placerat felis in lobortis. Integer 
nec felis in risus semper fringilla. Aenean vel justo mauris. Proin ac congue urna, ut placerat sapien. Sed ultrices 
mauris at ultricies lacinia. Nunc cursus rhoncus mauris vel scelerisque. Etiam sit amet pretium orci. Donec consequat 
pulvinar est, eget volutpat ipsum convallis in. Phasellus feugiat elementum enim, nec facilisis ipsum maximus in. 
Aenean ut tincidunt nulla.

In pretium ante non laoreet dignissim. Sed non mauris massa. Etiam quis posuere purus, ac interdum nisi. Curabitur 
convallis sem a tellus ullamcorper, in vehicula dui efficitur. Nulla varius et ante nec tincidunt. Phasellus tincidunt 
nunc erat, eget accumsan justo molestie quis. Vestibulum molestie massa et diam ullamcorper, sed consequat elit 
efficitur. Donec ac turpis sapien. Sed quis pulvinar nisl. Mauris consectetur diam in viverra consectetur. Sed vel 
lacinia odio, vel auctor eros. Aenean quis eleifend nisl. Phasellus vulputate erat sodales rhoncus tempus. Mauris odio 
urna, hendrerit non eleifend sed, posuere vel sem. Mauris dictum feugiat lectus sit amet gravida. Aenean mattis in 
purus vel varius.

Donec venenatis accumsan orci, ac ornare tortor maximus in. Integer mollis sed orci eu porta. Duis tristique augue eu 
malesuada feugiat. Fusce interdum, est vitae varius facilisis, justo odio euismod dui, eget interdum nisl odio id arcu. 
Fusce auctor vestibulum nisi, vestibulum malesuada arcu mollis fringilla. Nam at metus at felis lobortis suscipit vitae 
eu felis. Integer nec pharetra mi, in fringilla dolor. Mauris aliquet faucibus ligula a rutrum. Morbi condimentum ipsum 
a nunc blandit cursus. Nulla orci turpis, tristique et eleifend eget, consequat id sem. Phasellus eget porttitor 
turpis. Nulla enim metus, maximus vitae bibendum eu, condimentum sit amet sapien. Curabitur augue quam, sagittis id 
sagittis vitae, efficitur a quam. Vestibulum ultrices, leo nec euismod sagittis, sapien mauris ultrices purus, ut 
cursus lorem sapien eget nulla. Donec pulvinar velit in urna euismod iaculis. Vivamus quis pulvinar diam.

Fusce fringilla urna nibh, sed malesuada metus tincidunt vitae. Cras aliquet nec nibh vel blandit. Ut viverra quis 
purus quis mollis. Ut viverra nisi id orci condimentum, facilisis dictum lorem malesuada. Nulla sit amet lacinia lacus, 
id finibus nisi. Nunc dignissim massa eros, a sagittis purus varius eu. Nullam euismod metus a ante facilisis maximus. 
Mauris a ornare sem, a maximus quam. Nam fermentum, sem id pretium consectetur, urna ex dictum erat, sed lacinia nibh 
est ac arcu. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent 
venenatis lectus eu aliquam molestie. Pellentesque ante massa, sollicitudin vel tortor in, dapibus vestibulum massa. 
Nunc gravida odio id auctor volutpat. Aliquam nibh elit, pellentesque ut mi ut, venenatis ullamcorper tellus.

Cras consectetur purus elit, at feugiat lacus tristique ut. Aliquam erat volutpat. Nullam sagittis dolor quis justo 
convallis aliquam. Cras ac lacus massa. Morbi ex dui, rhoncus at ante nec, elementum hendrerit eros. Nullam elementum 
magna quis purus dignissim, id cursus metus vehicula. Sed posuere dolor ac sapien fermentum, eget interdum augue 
posuere. Phasellus mollis malesuada ante, id hendrerit mauris vehicula et.
""".trimIndent().encodeToByteArray()
    }
}
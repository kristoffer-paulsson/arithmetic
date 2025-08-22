package org.example.arithmetic.ref

import kotlin.test.Test
import kotlin.test.assertContentEquals

class AdaptiveArithmeticCodecTest {

    val information: ByteArray = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse efficitur dui neque, quis tempus nunc interdum nec. Suspendisse non malesuada ligula. Fusce malesuada massa ipsum. Quisque viverra nunc id tincidunt venenatis. Etiam fermentum consectetur sem, ut posuere arcu viverra eget. Ut non fermentum leo. Vivamus vehicula, lectus id volutpat finibus, sapien leo fermentum dui, sit amet consequat purus orci in nisi. Vivamus vel pellentesque turpis, id dapibus nulla.

Nullam tempor est id nisl interdum faucibus. Ut nec elit tincidunt, venenatis tellus a, luctus dolor. In hac habitasse platea dictumst. Morbi metus urna, mollis non augue ac, feugiat imperdiet nulla. Nunc venenatis augue id ex sodales, quis elementum nulla lobortis. Quisque sit amet purus at tellus varius consectetur eget ac mauris. Nullam at odio vel sem viverra tempor vel id ante.

Pellentesque at elementum mi. Ut consequat vehicula quam, eget bibendum erat convallis vel. Curabitur eu augue ex. Nam sit amet lacus ultrices, luctus tortor vel, lobortis nisi. Curabitur suscipit sapien erat, porttitor mollis massa varius nec. Nunc suscipit ante ac elementum accumsan. Praesent maximus odio non bibendum euismod. Pellentesque luctus semper tempor. Duis suscipit massa id ipsum blandit maximus eu eget sapien. Sed vulputate tortor a ante porttitor, lobortis faucibus ligula tempus. Donec tellus libero, rutrum lacinia justo at, vestibulum viverra enim. Maecenas in dui ligula.

Etiam libero dolor, sodales id metus sit amet, congue aliquam nibh. Nulla sed consectetur diam. Sed faucibus eros ligula, ac tristique erat hendrerit a. Ut eget commodo elit. Ut eget metus convallis, porttitor justo non, posuere enim. Phasellus iaculis tellus eget accumsan volutpat. Etiam vitae mauris sodales, rutrum tellus sit amet, tempus elit. Vestibulum molestie augue id massa tempor, id pulvinar arcu mollis. Vestibulum sollicitudin aliquam neque, quis condimentum eros sodales sed. Duis eget auctor diam.

Nulla et ex ultricies, interdum nibh ut, blandit tellus. Etiam accumsan eros sit amet lectus varius lobortis. Donec lacinia neque in ligula dignissim, vel porttitor orci porta. Suspendisse risus diam, rutrum luctus consectetur et, pharetra nec mi. Nulla ultrices, velit in finibus eleifend, odio velit dapibus odio, eu pellentesque diam augue vitae lorem. Aenean convallis, nibh at bibendum tempor, augue ipsum rutrum ante, a convallis mi nunc sit amet purus. Nulla faucibus justo at rutrum ornare. Nullam at tempor tortor, sit amet pharetra enim. Morbi scelerisque pharetra lacus, non vehicula diam vulputate at. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Suspendisse vitae nibh nec dolor consectetur tincidunt venenatis ac nibh. Aliquam lobortis eget odio sit amet aliquam. Aliquam ultricies tempor felis quis tincidunt. Quisque sit amet bibendum elit, sed volutpat augue.

Aliquam tristique ante eget interdum dictum. Morbi mollis libero vel placerat feugiat. Nulla ut hendrerit elit. Integer leo elit, dapibus vitae eros sit amet, aliquet semper leo. Nullam sodales id nibh nec lobortis. Nam sed fermentum erat. Morbi non tincidunt est. Donec pellentesque odio quis ipsum hendrerit, ac pellentesque neque posuere.

Sed ac diam sed lorem aliquet ultrices. Donec enim justo, imperdiet non tortor vitae, feugiat fermentum ipsum. Etiam venenatis egestas suscipit. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec quis elit consectetur, varius lacus et, rutrum nulla. Vivamus sit amet quam fermentum, interdum massa a, sollicitudin nisl. Nam ac sem quis nunc finibus fermentum id eu velit. Mauris vestibulum non augue a dictum. Nullam at augue maximus, consectetur elit quis, blandit dolor. Proin non libero ut lectus molestie vestibulum. Maecenas tempus eget lacus et ultrices. Donec consectetur, velit id volutpat gravida, sem odio blandit sem, ac luctus nisl erat vitae ipsum. Sed varius tellus non lectus tincidunt tristique. Nam fringilla nunc dictum tortor ornare, nec viverra turpis consequat.

Integer eleifend a ex vitae fringilla. Nam auctor dui vitae dictum pulvinar. Vestibulum blandit interdum consequat. Duis vel libero nulla. Aenean sit amet tortor vestibulum, rhoncus ante vel, semper odio. Sed aliquet blandit erat quis scelerisque. Aliquam in orci at nisl congue porta sit amet nec purus. Sed eu volutpat libero. Fusce aliquam condimentum porttitor. Nulla sed ante vel eros convallis mattis sit amet nec lacus. Vivamus non tempus eros, egestas consequat erat. Phasellus porttitor congue odio. Vestibulum vitae neque ut leo elementum laoreet. Sed ultricies varius ipsum a gravida.

Nullam rhoncus tempus magna, ac hendrerit massa malesuada non. Phasellus vestibulum sapien odio, ut posuere tellus fringilla vel. Praesent vulputate, elit sed malesuada sodales, tellus ipsum facilisis turpis, sed tristique risus leo in ipsum. Praesent mattis nisl id iaculis dapibus. Nunc erat ex, bibendum nec ligula eget, feugiat sagittis leo. Sed tempor ligula in eros porta bibendum. Morbi leo urna, bibendum quis ante quis, facilisis ullamcorper lectus. Vivamus tempor ante eget gravida feugiat. Sed erat turpis, pretium non tortor eu, lobortis vehicula dolor.

Vestibulum congue, urna nec dictum aliquet, nibh massa accumsan orci, sit amet porttitor mauris sem sit amet nibh. Praesent mattis tortor non arcu bibendum viverra. Mauris laoreet luctus libero. Sed enim urna, congue non ante vitae, tempor sollicitudin ligula. In finibus massa eget sapien consequat ultricies. Sed pellentesque a orci quis aliquet. Vivamus pellentesque enim tristique tellus ullamcorper vehicula. Nullam ut neque urna. Pellentesque vitae metus elementum, vestibulum massa et, porttitor mauris. Vivamus eget diam volutpat, molestie risus non, tempor ante. Mauris imperdiet vel mi vel ultrices. Etiam venenatis sed urna ut porttitor. Phasellus pretium vel ante et luctus. Cras mollis, metus ac dictum vehicula, leo ipsum fermentum velit, at tincidunt ligula nibh eget ex. Quisque fermentum elementum nulla, non tristique enim fermentum vitae.

Vivamus maximus tempor consectetur. Duis varius, enim nec viverra volutpat, diam justo semper metus, non lobortis orci felis aliquet mi. Pellentesque et felis lacinia nulla aliquet lacinia finibus et eros. Quisque accumsan sapien vitae ex ultricies commodo. Proin accumsan tincidunt massa, ut consectetur nisi hendrerit in. Sed nec ante semper, porttitor felis in, posuere tellus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Sed tincidunt dolor ut pulvinar feugiat. Donec egestas, lorem nec scelerisque consectetur, purus lacus mattis ligula, vitae feugiat nulla dui eget augue. Aliquam imperdiet nunc quis mauris dapibus, et eleifend nisl dictum. Etiam ac metus.
""".trimIndent().encodeToByteArray()

    @Test
    fun testCompress() {
        val size = information.size
        val input = BitInputBuffer(information)
        val output = BitOutputBuffer(size)
        AdaptiveArithmeticCodec.compress(input, output)
        println("Original Data Size: ${size} bytes")
        println("Compressed Data Size: ${output.toByteArray().size} bytes")
        assertContentEquals(information, output.toByteArray())
    }

    @Test
    fun testDecompress() {
        // Implement your test for decompression here
    }
}
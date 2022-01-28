package modules.basic

import com.soywiz.korim.color.RGBA
import kotlin.math.abs

/** This bad boy can be used to store and move around colours
 * it is initialized using companion functions of various colour entry types
 * base type is always 0f-1f, but various other input types are supported
 */
class Colour private constructor(r: Double, g: Double, b: Double, a: Double) {
    companion object{
        fun rgba256(r: Int, g: Int, b: Int, a: Int=255): Colour {
            return rgba(r/255.0,g/255.0,b/255.0,a/255.0)
        }

        fun rgba(r: Double, g: Double, b: Double, a: Double=1.0): Colour {
            return Colour(r.coerceIn(0.0,1.0),g.coerceIn(0.0,1.0),b.coerceIn(0.0,1.0),a.coerceIn(0.0,1.0)).also {
                it.setHex()
                it.setHsv()
            }
        }

        fun hsva256(h: Int, s: Int, v: Int, a: Int=255): Colour {
            return hsva(h/255.0,s/255.0,v/255.0,a/255.0)
        }

        fun hsva(h: Double, s: Double, v: Double, a: Double=1.0): Colour{
            val hue = h.coerceIn(0.0,1.0)
            val saturation = s.coerceIn(0.0,1.0)
            val value = v.coerceIn(0.0,1.0)
            val alpha = a.coerceIn(0.0,1.0)
            val hCirc = (hue*360).toInt()
            val c = value*saturation
            val m: Double = value-c
            val x: Double = c*(1f - abs((hCirc/60.0)%2-1.0))
            return when(hCirc){
                in 0..60 ->{
                    Colour(c+m,x+m,m,alpha)
                }
                in 60..120->{
                    Colour(x+m,c+m,m,alpha)
                }
                in 120..180->{
                    Colour(m,c+m,x+m,alpha)
                }
                in 180..240->{
                    Colour(m,x+m,c+m,alpha)
                }
                in 240..300->{
                    Colour(x+m,m,c+m,alpha)
                }
                in 300..360->{
                    Colour(c+m,m,x+m,alpha)
                }
                else ->{
                    Colour(m,m,m,alpha)
                }
            }.also {
                it.setHex()
                it.hue = hue
                it.saturation = saturation
                it.value = value
            }
        }

        fun byHex(hexCode: String): Colour {
            return when(hexCode.length){
                3->{
                    hexCode.map { decodeHex(it)*17/255.0 }.let {
                        Colour(it[0],it[1],it[2],1.0)
                    }
                }
                4->{
                    hexCode.map { decodeHex(it)*17/255.0 }.let {
                        Colour(it[0],it[1],it[2],it[3])
                    }
                }
                6->{
                    (0..2).map { decodeHex(hexCode[2*it])*16 + decodeHex(hexCode[2*it+1])  }.let{
                        Colour(it[0]/255.0,it[1]/255.0,it[2]/255.0,1.0)
                    }

                }
                8->{
                    (0..3).map { decodeHex(hexCode[2*it])*16 + decodeHex(hexCode[2*it+1])  }.let{
                        Colour(it[0]/255.0,it[1]/255.0,it[2]/255.0,it[3]/255.0)
                    }
                }
                else -> {
                    throw Exception("Error, $hexCode is invalid")
                }
            }.also {
                it.setHsv()
                it.hex= hexCode
            }
        }

        fun byKorgeRGBA(r: RGBA): Colour {
            return rgba(r.rd,r.gd,r.bd,r.ad)
        }

        private fun decodeHex(c: Char): Int {
            return try{
                "$c".toInt()
            }catch (e: Exception){
                when(c){
                    'A' -> 10
                    'B' -> 11
                    'C' -> 12
                    'D' -> 13
                    'E' -> 14
                    'F' -> 15
                    else -> throw (Exception("Error, invalid char $c for hex decoding"))
                }
            }
        }

        /** Common types
         *
         */
        val WHITE = Colour.rgba256(255,255,255)
        val BLACK = Colour.rgba256(0,0,0)
        val RED = Colour.rgba256(255,0,0)
        val GREEN = Colour.rgba256(0,255,0)
        val BLUE = Colour.rgba256(0,0,255)
        val CYAN = Colour.rgba256(0,255,255)
        val MAGENTA = Colour.rgba256(255,0,255)
        val YELLOW = Colour.rgba256(255,255,0)
        val GRIZEL = Colour.byHex("171819")
    }



    var red= r
        private set
    var green= g
        private set
    var blue= b
        private set
    var alpha= a
        private set
    var hex = ""
        private set
    var hue=0.0
        private set
    var saturation=0.0
        private set
    var value=0.0
        private set
    val korgeColor: RGBA
        get() {
            return RGBA.float(red,green,blue,alpha)
        }



    fun copy(): Colour {
        return rgba(red,green,blue,alpha)
    }

    private fun setHex() {
        var h = ""
        listOf(red,green,blue,alpha).forEach {
            (it*255).toInt().also {it2->
                h += toHexDigit(it2/16)
                h += toHexDigit(it2%16)
            }
        }
        hex = h
    }

    private fun toHexDigit(n: Int): String {
        return if(n>9){
            ('A'.code -10+n).toChar()
        }else{
            n
        }.toString()

    }

    private fun setHsv(){
        val cMax = listOf(red,green,blue).maxOrNull()?:0.0
        val cMin = listOf(red,green,blue).minOrNull()?:0.0
        val delta = cMax-cMin
        hue = when (cMax) {
            red -> {
                60.0*((green-blue)/delta%6)
            }
            green -> {
                60.0*((blue-red)/delta+2)
            }
            blue -> {
                60.0*((red-green)/delta+4)
            }
            else -> {
                0.0
            }
        }/360f
        saturation = if(cMax>0.0){
            delta/cMax
        }else{
            0.0
        }
        value = cMax
    }


}
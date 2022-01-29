package application

import com.soywiz.korge.input.onUp
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

class JarPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("jarPuntainer",relativeRectangle) {

    private suspend fun init() {


        resourcesVfs["workshop/Liquid.png"].readBitmap().also {
            punImage("liquid", oneRectangle(), bitmap = it).also {
                it.colorMul = Colour.rgba(1.0,1.0,1.0,0.48).korgeColor
            }
        }

        resourcesVfs["workshop/Jar.png"].readBitmap().also {
            punImage("jar", oneRectangle(), bitmap = it)
        }

        resourcesVfs["workshop/Pickle.png"].readBitmap().also {
            punImage("pickle", oneRectangle(), bitmap = it).also {
                it.visible=false
            }
        }

        resourcesVfs["workshop/Jam.png"].readBitmap().also {
            punImage("jam", oneRectangle(), bitmap = it).also {
                it.visible=false
            }
        }

    }

    var onReturn = {}


    fun signVisible(n: Int){
        when(n){
            -1 -> {
                puntainers.first { it.id=="liquid" }.visible = false
                puntainers.first { it.id=="jar" }.visible = true
                puntainers.first { it.id=="pickle" }.visible = false
                puntainers.first { it.id=="jam" }.visible = false
            }
            0 -> {
                puntainers.first { it.id=="liquid" }.also {
                    it.visible = true
                    it.colorMul = Colour.rgba256(241,287,57,120).korgeColor
                }

                puntainers.first { it.id=="jar" }.visible = true
                puntainers.first { it.id=="pickle" }.visible = true
                puntainers.first { it.id=="jam" }.visible = false
            }
            1->{
                puntainers.first { it.id=="liquid" }.also {
                    it.visible = true
                    it.colorMul = Colour.rgba256(100,28,58,120).korgeColor
                }
                puntainers.first { it.id=="jar" }.visible = true
                puntainers.first { it.id=="pickle" }.visible = false
                puntainers.first { it.id=="jam" }.visible = true
            }
            else ->{
                puntainers.first { it.id=="liquid" }.visible = false
                puntainers.first { it.id=="jar" }.visible = false
                puntainers.first { it.id=="pickle" }.visible = false
                puntainers.first { it.id=="jam" }.visible = false
            }
        }

    }

    companion object {
        suspend fun create(relativeRectangle: Rectangle
        ): JarPuntainer {
            return JarPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}
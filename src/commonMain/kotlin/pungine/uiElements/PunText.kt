package pungine.uiElements

import com.soywiz.korge.view.Image
import com.soywiz.korge.view.Text
import com.soywiz.korge.view.text
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.font.Font
import com.soywiz.korim.text.TextAlignment
import modules.basic.Colour
import pungine.InternalGlobalAccess
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

class PunText: Puntainer{

    constructor(id: String?=null, relativeRectangle: Rectangle = oneRectangle(), text: String, font: Font, textSize:Double=24.0, colour: Colour= Colour.WHITE, scaleTextSize: Boolean=false, alignment: TextAlignment = TextAlignment.TOP_CENTER, padding: Double=12.0, zOrder: Int=0): super(id,relativeRectangle,zOrder) {
        this.scaleText = scaleTextSize
        this.alignment = alignment
        this.font = font
        this.textSize = textSize
        this.textColour = colour
        this.padding=padding
        this._text = text
        generateTextLines(text)

    }



    override fun reshape(r: Rectangle) {
        if(scaleText){
            super.reshape(r)
        }else{
            x = x-globalBounds.x+r.left + ( r.width)*alignment.horizontal.ratio
            y = y - globalBounds.y + (InternalGlobalAccess.virtualSize.height - r.top) + (-scaledHeight+ r.height)*alignment.vertical.ratio

            lineWrapperRecursive(wrapWidth = r.width-padding*2,0)
            textInstances.forEachIndexed {index, it->
                it.y = it.scaledHeight*(index - (textInstances.size-1)*alignment.vertical.ratio)
                it.x = -it.scaledWidth*alignment.horizontal.ratio - padding*(alignment.horizontal.ratio-0.5)
            }
        }
    }

    private fun generateTextLines(text: String){
        text.lines().forEach {
            val i = generateText(it)
            this.addChild(i)
            textInstances.add(i)
        }
    }

    private fun generateText(t: String): Text {
        return Text(t,textSize = textSize,font = font,color=textColour.korgeColor)
    }


    private fun lineWrapperRecursive(wrapWidth: Double, index: Int){
        val currentList = textInstances
        if(index<textInstances.size){
            val it = currentList[index]
            if(it.scaledWidth>wrapWidth){
                val splits = it.text.split(regex = Regex(" "))
                var s = splits[0]
                var s2= ""
                val t = generateText(splits[0])
                for(i in 1 until splits.size) {
                    t.text=s+" ${splits[i]}"
                    if (t.scaledWidth>wrapWidth){
                        s2 += if(s2==""){
                            splits[i]
                        }else{
                            " ${splits[i]}"
                        }
                    }else{
                        s+=" ${splits[i]}"
                    }
                }
                it.text=s
                t.text=s2
                this.addChild(t)
                textInstances.add(index+1,t)
            }
            lineWrapperRecursive(wrapWidth,index+1)
        }
    }

    var text: String
    get() {
        return _text
    }
    set(value) {
        _text=value
        this.children.clear()
        textInstances.clear()
        generateTextLines(value)
    }


    private var _text: String
    private val textInstances = mutableListOf<Text>()
    var scaleText: Boolean = false
    var alignment: TextAlignment // .horizontal.ratio : 0.0-> left, 0.5-> centre, 1.0 ->right
    val font: Font
    var textSize: Double
    var textColour: Colour
    var padding: Double




}
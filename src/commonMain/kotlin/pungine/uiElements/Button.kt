package pungine.uiElements

import com.soywiz.korge.input.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.sliceWithSize
import com.soywiz.korim.color.RGBA
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

class Button: Puntainer {
    constructor(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), upBitmap: Bitmap, downBitmap: Bitmap, hoverBitmap: Bitmap?=null, inactiveBitmap: Bitmap?=null, zOrder: Int=0): super(id,relativeRectangle,zOrder){
        this.addPuntainer(PunImage("up",bitmap = upBitmap))
        this.addPuntainer(PunImage("down",bitmap = downBitmap))
        if(hoverBitmap!=null){
            this.addPuntainer(PunImage("hover",bitmap = hoverBitmap))
        }
        if(inactiveBitmap!=null){
            this.addPuntainer(PunImage("inactive",bitmap = inactiveBitmap))
        }
        events()
        adjustVisibility()
    }


    constructor(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), upBitmap: Bitmap, downRate: Double=0.5, inactiveRate: Double=0.2, hoverOffset: Int=20, zOrder: Int=0): super(id,relativeRectangle,zOrder){
        this.addPuntainer(PunImage("up",bitmap = upBitmap))

        PunImage("down",bitmap = upBitmap).also { down->
            down.tint = Colour.rgba(downRate,downRate,downRate,1.0).korgeColor
            this.addPuntainer(down)
            down.visible=false
        }
        PunImage("inactive",bitmap = upBitmap).also { inactive->
            inactive.tint = Colour.rgba(inactiveRate,inactiveRate,inactiveRate,1.0).korgeColor
            this.addPuntainer(inactive)
            inactive.visible=false
        }

        val newBitmap = upBitmap.clone()

        for(x in 0 until newBitmap.width){
            for(y in 0 until newBitmap.height){
                val r = newBitmap.getRgba(x,y)
                val c2 = RGBA(r.r+hoverOffset,r.g+hoverOffset,r.b+hoverOffset,r.a)
                newBitmap.setRgba(x,y,c2)
            }
        }

        PunImage("hover",bitmap = newBitmap).also { hover->
            this.addPuntainer(hover)
            hover.visible=false
        }
        events()
        adjustVisibility()
    }


    constructor(id: String?=null, relativeRectangle: Rectangle= oneRectangle(), bitmap: Bitmap, upRect: Rectangle, downRect: Rectangle, hoverRect: Rectangle?=null, inactiveRect: Rectangle?=null, zOrder: Int=0): super(id,relativeRectangle,zOrder){
        val upBitmap = bitmap.sliceWithSize(upRect.left.toInt(), bitmap.height-upRect.top.toInt(), upRect.width.toInt(), upRect.height.toInt())
        val downBitmap = bitmap.sliceWithSize(downRect.left.toInt(), bitmap.height-downRect.top.toInt(), downRect.width.toInt(), downRect.height.toInt())
        this.addPuntainer(PunImage("up",bitmapSlice = upBitmap))
        this.addPuntainer(PunImage("down",bitmapSlice = downBitmap))
        if(hoverRect!=null){
            val hoverBitmap = bitmap.sliceWithSize(hoverRect.left.toInt(), bitmap.height-hoverRect.top.toInt(), hoverRect.width.toInt(), hoverRect.height.toInt())
            this.addPuntainer(PunImage("hover",bitmapSlice = hoverBitmap))
        }
        if(inactiveRect!=null){

            val inactiveBitmap = bitmap.sliceWithSize(inactiveRect.left.toInt(), bitmap.height-inactiveRect.top.toInt(), inactiveRect.width.toInt(), inactiveRect.height.toInt())
            this.addPuntainer(PunImage("inactive",bitmapSlice = inactiveBitmap))
        }
        events()
        adjustVisibility()
    }

    private fun events(){
        this.onDown {
            pushing=true
            hovering=true
            adjustVisibility()
        }

        this.onUp {
            pushing=false
            adjustVisibility()
        }

        this.onOut {
            hovering=false
            pushing=false
            adjustVisibility()
        }

        this.onOver {
            hovering=true
            adjustVisibility()
        }
    }

    private fun adjustVisibility(){
        if(inactive){
            setVisibleImage("inactive")
        }else{
            if(hovering.not()){
                setVisibleImage("up")
            }else{
                if(pushing){
                    setVisibleImage("down")
                }else{
                    setVisibleImage("hover")
                }
            }
        }
    }

    /** Sets image with ID visible, if no such image, Up is set visible
     *
     */
    private fun setVisibleImage(id: String){
        puntainers.forEach { it.visible=false }
        puntainers.filter { it.id==id }.also {
            if(it.isEmpty()){
                setVisibleImage("up")
            }else{
                it.forEach {it2-> it2.visible=true }
            }
        }
    }




    var clickFunction= {}
    var pushing = false //this variable is true when the button is being pushed, false otherwise
        private set(value) {
        if(value.not()){
            if(hovering&&inactive.not()){
                clickFunction()
            }
        }
        field=value
    }



    var hovering = false // if true mouse is on the button
        private set
    var inactive = false // if true, button is disabled
        set(value) {
            field=value
            adjustVisibility()
        }





}
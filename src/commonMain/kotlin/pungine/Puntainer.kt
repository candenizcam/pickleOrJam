package pungine

import application.GlobalAccess
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import modules.basic.Colour
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.PunImage
import pungine.uiElements.PunImageEditable

/** This is the one size fits all container for pungine, any specialized views should be inherited from this first, and then implemented as view children
 *
 */
open class Puntainer: Container {
    /** The rectangle in this function is relative to its surroundings and is used for later reference
     * rect is always relative to parent
     */
    constructor(id: String?=null, rect: Rectangle= oneRectangle(),  zOrder: Int= 0) : super(){
        this.id = id
        this.position(x,y)
        this.relativeRectangle = rect
        this.zOrder=zOrder
    }

    val id: String? //in this version, id is optional, it is null unless user wants to find it


    var relativeRectangle: Rectangle // this is a rectangle that is used by parent to determine this' size
        private set

    //var exactRectangle: Rectangle? =null // this is a rectangle that is used directly to find size
        //private set

    val puntainers= mutableListOf<Puntainer>()

    //var virtualRectangle: Rectangle = InternalGlobalAccess.virtualRect // active rectangle with virtual pixels
    //TODO a rectangle that gives the exact size

    var zOrder: Int = 0

    fun relativePoint(screenRatedPoint: Vector): Vector {
        return relativeRectangle.ratePoint(screenRatedPoint)
    }



    //fun reshape(width: Double?=null, height: Double?=null, centreX: Double?=null, centreY:Double?=null){
    //    reshape(Rectangle(Vector(centreX?:this.centre.x,centreY?:this.centre.y),width?:this.width,height?:this.height))
    //}

    fun translateRelative(vector: Vector){
        relativeRectangle = relativeRectangle.moved(vector)
    }

    fun translateRelative(x: Double=0.0, y:Double=0.0){
        translateRelative(Vector(x,y))
    }

    /** This resizes using a new rectangle
     */
    fun resizeRect(relativeRectangle: Rectangle){
        this.relativeRectangle = relativeRectangle
    }

    fun resizeRect(width: Double?=null, height: Double?=null){
        relativeRectangle = Rectangle(relativeRectangle.centre,width?: relativeRectangle.width,height?: relativeRectangle.height)
    }

    /** This is the wrapper for the Rectangle.resized function
     */
    fun resizeRelative(width: Double, height: Double,fromCorner: Rectangle.Corners?=null){
        this.relativeRectangle = relativeRectangle.resized(width,height,fromCorner)
    }


    /** This method takes a rectangle (of pixels) as input and sets the size of this puntainer
     * it should be called by parent whenever a rescaling should happen
     *
     */
    open fun reshape(r: Rectangle){
        children.forEach {
            if( it !is Puntainer){
                scaledWidth = r.width
                scaledHeight = r.height
                r.getCorner(Rectangle.Corners.TOP_LEFT).also {
                    position(it.x,InternalGlobalAccess.virtualSize.height -it.y)
                }

            }
        }
        puntainers.forEach {
            it.reshape(r.fromRated(it.relativeRectangle))
        }
    }



    fun addPuntainer(p: Puntainer){
        addChild(p)
        puntainers.add(p)
    }

    /** puntainers are sorted based on z order
     * default z order is 0
     */
    fun sortPuntainersByZ(lowToBottom: Boolean=false){
        val size = puntainers.size
        val pairs = puntainers.mapIndexed {index,it->
            Pair(index.toDouble()/size.toDouble() + it.zOrder,it)
        }

        pairs.sortedBy { it.first }.forEachIndexed {index, it->
            it.second.index = index
        }

        puntainers.sortBy { it.index }
        children.sortBy { it.index }
        if(lowToBottom){
            puntainers.reverse()
            children.reverse()
        }
    }


    /**This puts a relative puntainer in this puntainer with relative rect. as given
     * for exact size, Puntainer can be directly called into an add Puntainer
     */
    fun relativePuntainer(id: String?=null,relativeRectangle: Rectangle, zOrder: Int=0,callFunc: (it: Puntainer)->Any): Puntainer {
        return Puntainer(id, relativeRectangle,zOrder).also {
            addPuntainer(it)
            callFunc(it)
        }
    }

    fun solidRect(id: String?=null,relativeRectangle: Rectangle,colour: Colour, zOrder: Int=0): Puntainer {
        return relativePuntainer(id, relativeRectangle,zOrder){
            it.solidRect(100.0,100.0,colour.korgeColor)
        }
    }

    fun roundRect(id: String?=null, relativeRectangle: Rectangle, rX: Double, rY: Double, colour: Colour =Colour.WHITE, zOrder: Int=0): Puntainer{
        return relativePuntainer(id,relativeRectangle,zOrder){
            it.addChild(RoundRect(100.0, 100.0,rX, rY, colour.korgeColor))
        }
    }

    fun punImage(id: String?=null, relativeRectangle: Rectangle, bitmap: Bitmap, zOrder: Int=0, editable: Boolean=false): Puntainer{
        return if(editable){
            PunImageEditable(id, relativeRectangle, bitmap, zOrder).also {
                this.addPuntainer(it)
            }

        }else{
            PunImage(id,relativeRectangle,bitmap,zOrder).also {
                this.addPuntainer(it)
            }
        }

    }

    fun punImage(id: String?=null, relativeRectangle: Rectangle, bitmap: BitmapSlice<Bitmap>, zOrder: Int=0, editable: Boolean=false): Puntainer{
        if(editable){
            return PunImageEditable(id,relativeRectangle,bitmap,zOrder).also {
                this.addPuntainer(it)
            }
        }else{
            return PunImage(id,relativeRectangle,bitmap,zOrder).also {
                this.addPuntainer(it)
            }
        }

    }


}


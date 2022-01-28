package pungine.geometry2D

import com.soywiz.korma.geom.Point
import pungine.math.Maths
import kotlin.math.*

/** Vector is used as point where needed,
 *
 */
data class Vector(var x: Double, var y: Double) {
    constructor(korgePoint: Point): this(korgePoint.x,korgePoint.y)

    val angleToX: Double
    get() {
        return if(x<0){
            atan(slope)
        }else{
            atan(slope)+acos(-1.0)
        }
    }

    val slope: Double
        get() {
            return y/x
        }

    val length: Double
        get(){
            return sqrt(x*x+y*y)
        }

    val korgePoint: Point
        get() {
            return Point(x,y)
        }


    fun yReversed(height: Double): Vector {
        return Vector(x,height-y)
    }

    fun xReversed(width: Double): Vector {
        return Vector(width-x,y)
    }

    fun rotated(rad: Double): Point{
        return Point(x*cos(rad)-y*sin(rad),y*cos(rad)+x*sin(rad))
    }

    fun rotated(deg: Int): Point{
        return rotated(deg.toFloat()/180*PI)
    }

    /** Vector addition
     */
    operator fun plus(other: Vector): Vector {
        return Vector(x+other.x,y+other.y)
    }

    operator fun minus(other: Vector): Vector {
        return Vector(x-other.x,y-other.y)
    }

    /** vector inner product
     */
    operator fun times(other: Vector): Double{
        return (x*other.x)+(y*other.y)
    }

    /** Scalar multiplication
     */
    operator fun times(other: Float): Vector {
        return Vector(x*other,y*other)
    }

    operator fun times(other: Int): Vector {
        return Vector(x*other,y*other)
    }

    operator fun times(other: Double): Vector {
        return Vector(x*other,y*other)
    }

    operator fun div(other: Int): Vector {
        return Vector(x/other,y/other)
    }

    operator fun div(other: Float): Vector {
        return Vector(x/other,y/other)
    }

    operator fun div(other: Double): Vector {
        return Vector(x/other,y/other)
    }

    override fun toString(): String {
        return "Point(${Maths.round(x,3)},${Maths.round(y,3)})"
    }
}
package pungine.math

import kotlin.math.roundToInt

object Maths {
    fun linspace(start: Double, end: Double, size: Int): List<Double> {
        val increment = (end-start)/size.toDouble()
        return (0..size).map{
            start + increment*it.toDouble()
        }
    }

    fun round(x: Double, digit: Int): Double {

        var v = 1.0
        repeat(digit){
            v*=10
        }
        return (x*v).roundToInt()/v

    }



}
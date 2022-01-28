package pungine.geometry2D

import com.soywiz.korim.vector.Context2d
import com.soywiz.korma.geom.bezier.Bezier
import com.soywiz.korma.geom.vector.cubic
import pungine.math.Maths
import pungine.uiElements.PunImageEditable
import kotlin.math.*
import kotlin.math.abs

/** This class takes bezier from KorIM to handle composite bezier curve construction
 * important terminology: point, a point that curve passes, relative point is relative to that and control is relative + point
 */
class CompositeBezier {
    constructor(firstPoint: Vector, firstRelativePoint: Vector){
        val b = BezierPoint(firstPoint,firstRelativePoint*(-1))
        points.add(b)
        this.resolution = resolution
    }

    var resolution: Int = 100


    fun addPointAndRelative(point: Vector,relativePoint: Vector){
        points.add(BezierPoint(point,relativePoint))
        beziers = getCubicBeziers()
    }

    fun addPointAndControl(point: Vector, controlPoint: Vector){
        points.add(BezierPoint(point,controlPoint-point))
        beziers = getCubicBeziers()
    }

    fun addPointAndAngle(point: Vector, angle: Double, length: Double){
        points.add(BezierPoint(point,angle, length))
        beziers = getCubicBeziers()
    }

    fun removeLastPoint(){
        points.removeLast()
        beziers = getCubicBeziers()
    }

    fun getCubicBeziers(canvasHeight: Int? = null): MutableList<Bezier.Cubic> {
        val points = if(canvasHeight!=null){
            val top = Vector(0.0,canvasHeight.toDouble())
            points.map { BezierPoint(Vector(it.point.x,canvasHeight.toDouble()-it.point.y),Vector(it.relativePoint.x,-it.relativePoint.y)) }
        }else{
            points
        }

        val curves = mutableListOf<Bezier.Cubic>()
        for(i in 0 until points.size-1){
            curves.add(points[i].getCurve(points[i+1]))
        }
        return curves
    }

    fun getEdgePoints(): List<Vector> {
        return points.map { it.point }
    }

    fun getControlPoints(): List<Vector> {
        val ps = mutableListOf<Vector>()
        for (i in points.indices){
            if(i==0){
                ps.add(points[i].firstControlPoint())
            }else{
                ps.add(points[i].secondControlPoint())
            }
        }
        return ps
    }

    fun getControlPointsPair(): List<Vector> {
        val ps = mutableListOf<Vector>()
        for (i in 0 until points.size-1){
            ps.add(points[i].firstControlPoint())
            ps.add(points[i+1].secondControlPoint())
        }
        return ps.toList()
    }

    fun drawLine(context : Context2d){
        getCubicBeziers(context.height).forEach {
            context.cubic(it)
        }
    }

    fun pointFromTau(tau: Double): Vector {
        if(tau>largestTau){
            throw Exception("tau too large $tau")
        }else if (tau==largestTau){
            return Vector(beziers.last().p3)
        }else if(tau<0.0){
            throw Exception("tau too small")
        }
        val index = tau.toInt().coerceIn(beziers.indices)
        val t = tau%1.0
        return Vector(beziers[index].calc(t))
    }

    /** Finds the tau of the point closest to the given target
     * for about 15 curves and the default mesh, this takes less then 5 microseconds so if something is slow, it's not this
     */

    fun closestPoint(target: Vector, coarseMesh: Int = 10, fineMesh: Int = 100): Double {

        var minT = 0.0
        var mindex = 0
        // coarse search
        var minD = -1.0
        for (i in beziers.indices){
            for (j in 0..coarseMesh){
                val t = j.toDouble()/coarseMesh
                val d = (Vector(beziers[i].calc(t)) - target).length
                if(minD == -1.0){
                    minD = d
                }else if (minD>d){
                    minD = d
                    minT = t
                    mindex = i
                }
            }
        }



        val upT = (minT+1.0/coarseMesh.toDouble()).coerceAtMost(1.0)
        val downT = (minT-1.0/coarseMesh.toDouble()).coerceAtLeast(0.0)
        val tDiff = (upT-downT)/fineMesh

        // fine search
        for (i in 0..fineMesh){
            val t = downT + i*tDiff
            val d = (Vector(beziers[mindex].calc(t))- target).length
            if(d<minD){
                minT = t
                minD = d
            }
        }

        return mindex.toDouble()+minT
    }
//Batur did stuff here
    fun closestPointNotInRectangles(target: Vector, rectList: MutableList<Rectangle>, img2: PunImageEditable, coarseMesh: Int = 10, fineMesh: Int = 100): Double {

        var minT = 0.0
        var mindex = 0
        // coarse search
        var minD = -1.0
        for (i in beziers.indices){
            for (j in 0..coarseMesh){
                val t = j.toDouble()/coarseMesh
                if(rectList.none { isItIN((beziers[i].calc(t)).x,(beziers[i].calc(t)).y,it,img2) }){
                    val d = (Vector(beziers[i].calc(t)) - target).length
                    if(minD == -1.0){
                        minD = d
                    }else if (minD>d){
                        minD = d
                        minT = t
                        mindex = i
                    }
                }
            }
        }
        val upT = (minT+1.0/coarseMesh.toDouble()).coerceAtMost(1.0)
        val downT = (minT-1.0/coarseMesh.toDouble()).coerceAtLeast(0.0)
        val tDiff = (upT-downT)/fineMesh

        // fine search
        for (i in 0..fineMesh){
            val t = downT + i*tDiff
            if(rectList.none { isItIN((beziers[mindex].calc(t)).x,(beziers[mindex].calc(t)).y,it,img2) }){
            val d = (Vector(beziers[mindex].calc(t))- target).length
                if(d<minD){
                    minT = t
                    minD = d
                }
            }
        }

        return mindex.toDouble()+minT
    }
    //Batur stuff ends

    private fun beamLength(tau1: Double,tau2: Double): Double {
        return (pointFromTau(tau2) - pointFromTau(tau1)).length
    }

    fun isItIN(x: Double,y: Double,rectangle: Rectangle,img: PunImageEditable): Boolean {
        return (x<=(rectangle.right*img.width) && x>=(rectangle.left*img.width) && y<=(rectangle.top*img.height) && y>=(rectangle.bottom*img.height))
    }

    /** Finds distance between two tau points
     * mit eisen und blut i've managed to reduce this computation time to less then 10 microseconds, which should be enough for whatever
     * also works with fairly well accuracy, so, yay me!
     */
    fun distanceBetween(tau1: Double=0.0, tau2: Double = beziers.size.toDouble(), mesh: Int = 100): Double {
        val increment = 1.0/mesh.toDouble()
        var minTau = tau1.coerceAtMost(tau2)
        var maxTau = tau1.coerceAtLeast(tau2)
        var multiplier = if(tau2>tau1){
            1.0
        }else{
            -1.0
        }

        if((maxTau-minTau)<increment){
            return beamLength(maxTau,minTau)*multiplier // single beam case
        }
        val v1 = (minTau*mesh)%1.0/mesh
        minTau -= v1
        val v2 = (maxTau*mesh)%1.0/mesh
        maxTau -= v2
        if(minTau.toInt()==maxTau.toInt()){
            var d = 0.0
            val range = Maths.linspace(minTau,maxTau,mesh)
            for(i in 1 until range.size){
                d+=beamLength(range[i],range[i-1]) // single arc case
            }
            return d*multiplier
        }
        // this is if we have a multi arc situation
        var firstArc = 0.0
        val startIndex = if(minTau== minTau.toInt().toDouble()){
            minTau.toInt()
        }else{
            val inc1 = (1.0-minTau+minTau.toInt())/mesh.toDouble()
            for(i in 1..mesh){
                firstArc+=beamLength(minTau+inc1*i,minTau+inc1*(i-1))
            }
            minTau.toInt()+1

        }

        var lastArc = 0.0
        val endIndex = if(maxTau == maxTau.toInt().toDouble()){
            maxTau.toInt()
        }else{
            val inc2 = (maxTau-maxTau.toInt())/mesh.toDouble()
            for(i in 1..mesh){
                lastArc+=beamLength(maxTau.toInt()+inc2*i,maxTau.toInt()+inc2*(i-1))
            }
            maxTau.toInt()
        }


        var d3 = 0.0
        for (i in startIndex until endIndex){
            d3+=lengths[i]
        }
        return (firstArc+lastArc+d3)*multiplier
    }

    private fun lengthOfACurve(b: Bezier.Cubic, mesh: Int): Double {
        var d = 0.0
        for (i in 1..mesh){
            d+=(Vector(b.calc(i/mesh.toDouble()))-Vector(b.calc((i-1)/mesh.toDouble()))).length
        }

        val mesh2 = mesh*10
        var d2 = 0.0
        for (i in 1..mesh2){
            d2+=(Vector(b.calc(i/mesh2.toDouble()))-Vector(b.calc((i-1)/mesh2.toDouble()))).length
        }

        return if(abs(d2-d)>0.1.coerceAtLeast(d/1000.0)){
            lengthOfACurve(b,mesh2)
        }else{
            d2
        }
    }


    /** Finds the point distance away from a given tau
     * returns a pair of tau and remaining distance (0 if within, number if not)
     * this is a slow calculator that works well with large distances and higher tolerance
     */
    fun distanceFromTau(tau: Double, distance: Double, accuracy: Double?=null): Pair<Double, Double> {
        if(distance>0){
            val fullDistance = distanceBetween(tau1 = tau)
            val tol = accuracy ?: (fullDistance / 1000.0)
            if(fullDistance<distance){
                return Pair(largestTau, distance-fullDistance)
            }
            var minTau = tau
            var maxTau = largestTau
            if(abs(minTau-maxTau)<0.0001){
                return Pair(minTau, distance-fullDistance)
            }
            var mesh = 100
            var remainingDistance = distance
            var midTau: Double = 0.0
            var delta: Double = fullDistance
            repeat(100){
                midTau = minTau*0.5 + maxTau*0.5
                delta = remainingDistance-distanceBetween(minTau, midTau,mesh)
                val diff = distanceBetween(minTau,maxTau,mesh)
                if( abs(delta)< tol ){
                    return Pair(midTau, delta)
                }
                if(abs(delta)>abs(diff)){
                    val a = minTau
                    val b = maxTau
                    minTau = 2*a-b
                    maxTau = 2*b-a
                    maxTau = maxTau.coerceAtMost(largestTau)
                    minTau = minTau.coerceAtLeast(0.0)
                    mesh = mesh*10
                }else{
                    if(delta>0){
                        minTau = midTau
                        remainingDistance =delta
                    }else{
                        maxTau = midTau
                    }
                }
            }
            println("did not converge")
            return Pair(midTau, delta)
        }else{
            val fullDistance = distanceBetween(tau2 = tau)
            val tol = accuracy ?: (fullDistance / 1000.0)
            if(fullDistance<abs(distance)){
                return Pair(0.0, distance+fullDistance)
            }

            var targetDist =distance
            var minTau = 0.0
            var mesh = 100
            var maxTau = tau
            var midTau = 0.0
            var delta = 0.0
            repeat(100){
                midTau = minTau*0.5+maxTau*0.5
                delta = distanceBetween(maxTau,midTau,mesh)-targetDist
                val diff = distanceBetween(minTau, maxTau)
                if(abs(delta) < tol){
                    return Pair(midTau, delta)
                }
                if(abs(delta)>abs(diff)){
                    val a = minTau
                    val b = maxTau
                    minTau = 2*a-b
                    maxTau = 2*b-a
                    mesh = mesh*10
                }else{
                    if(delta<0){
                        minTau = midTau
                    }else{
                        maxTau = midTau
                        targetDist = -delta
                    }
                }
            }
            println("did not converge")
            return Pair(midTau,delta)
        }
    }


    /** This is a faster calculator, it works fast for small distances with very high precission
     */
    fun fastDistanceFromTau(tau: Double, distance: Double, step: Double?=null): Pair<Double, Double> {
        if(distance>0){
            val fullDistance =distanceBetween(tau)
            if(fullDistance<distance){
                return Pair(largestTau, distance-fullDistance)
            }
        }else{
            val fullDistance =distanceBetween(tau2 = tau)
            if(fullDistance<abs(distance)){
                return Pair(0.0, distance+fullDistance)
            }
        }

        var d = 0.0
        var nt = tau
        val increment: Double
        val mesh: Int
        if(step==null){
            mesh = 10000
            increment = 1.0/mesh.toDouble()
        }else{
            increment=step
            mesh = (1.0/step).toInt()
        }
        if(distance>0){
            repeat(mesh){
                d+=beamLength(nt,nt+increment)
                nt+=increment
                if(d>distance){
                    return Pair(nt,distanceBetween(tau, nt)-distance)
                }
            }
        }else{
            repeat(mesh){
                d-=beamLength(nt,nt+increment)
                nt-=increment
                if(d<distance){
                    return Pair(nt,distanceBetween(tau,nt)-distance)
                }
            }
        }
        println("short did not converge which is nuts")
        return Pair(-1.0,-1.0) // this is effectively an error throw, this shouldnt happen

    }

    // Im thinking of a third iteration that kinda merges two ideas but i'll implement it later


    private val points = mutableListOf<BezierPoint>()
    private var beziers = mutableListOf<Bezier.Cubic>()
    set(value) {
        field=value
        lengths= beziers.map {
            lengthOfACurve(it,10)
        }


    }
    private var lengths = listOf<Double>()
    set(value) {
        field=value
        var d = 0.0
        value.forEach {
            d+=it
        }
        length=d
    }
    var length = 0.0
    val largestTau: Double
    get() {
        return beziers.size.toDouble()
    }


    data class BezierPoint(val point: Vector, val relativePoint: Vector){
        constructor(p: Vector, angle: Double, length: Double): this(p,Vector(cos(angle)*length,sin(angle)*length))

        fun firstControlPoint(): Vector {
            return point - relativePoint
        }

        fun secondControlPoint(): Vector{
            return point + relativePoint
        }

        fun getCurve(other: BezierPoint): Bezier.Cubic {
            return Bezier.Cubic(point.korgePoint,firstControlPoint().korgePoint,other.secondControlPoint().korgePoint,other.point.korgePoint)
        }

    }
}
package pungine.math

class Matrix{
    private val data: MutableList<MutableList<Double>>
    var columnNo = 0
    var rowNo = 0
    constructor(data: MutableList<MutableList<Double>>){
        this.data = data
        columnNo = data[0].size
        rowNo = data.size
    }

    constructor(rows: Int, cols: Int=rows, value: Double=0.0){
        this.data = MutableList(rows){
            MutableList(cols){value}
        }
        columnNo = cols
        rowNo = rows
    }

    fun copy(): Matrix {
        val m = Matrix(rowNo, columnNo)
        m.forEachElement{i,j->
            this[i,j]
        }
        return m
    }

    fun forEachElement(f: (Int, Int)->Double){
        for (i in 1..rowNo){
            for (j in 1..columnNo){
                this[i,j] = f(i,j)
            }
        }
    }

    /** Operator set access uses 1 as the starting index, not zero in accordance with pungo standards
     * fight me
     */
    operator fun get(i: Int, j: Int): Double {
        return this.data[i-1][j-1]
    }

    operator fun set(i: Int, j: Int, value: Double){
        this.data[i-1][j-1] = value
    }

    fun sameSize(other: Matrix): Boolean {
        return ((this.rowNo == other.rowNo) and (this.columnNo == other.columnNo))
    }

    fun isEqual(other: Matrix): Boolean {
        if(!sameSize(other)){
            return false
        }

        for (i in 1..rowNo){
            for (j in 1..columnNo){
                if(this[i,j] != other[i,j]) return false
            }
        }
        return true
    }

    override fun toString(): String {
        var v = "["
        data.forEach {
            it.forEach {
                v+="${it.toInt().toString().padStart(5, ' ')},"
            }
            v = v.dropLast(1)
            v += ";\n"
        }
        v.dropLast(2)
        v+="]"
        return v
    }

}